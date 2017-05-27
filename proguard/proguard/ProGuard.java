/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2014 Eric Lafortune (eric@graphics.cornell.edu)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package proguard;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import proguard.classfile.ClassConstants;
import proguard.classfile.ClassPool;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.editor.ClassElementSorter;
import proguard.classfile.editor.NamedAttributeDeleter;
import proguard.classfile.visitor.AllMethodVisitor;
import proguard.classfile.visitor.ClassPrinter;
import proguard.classfile.visitor.ClassVersionFilter;
import proguard.obfuscate.Obfuscator;
import proguard.optimize.Optimizer;
import proguard.preverify.Preverifier;
import proguard.preverify.SubroutineInliner;
import proguard.shrink.Shrinker;

/**
 * Tool for shrinking, optimizing, obfuscating, and preverifying Java classes.
 *
 * @author Eric Lafortune
 */
public class ProGuard
{
    public static final String VERSION = "ProGuard, version 5.1";

    private final Configuration configuration;
    private       ClassPool     programClassPool = new ClassPool();
    private final ClassPool     libraryClassPool = new ClassPool();


    /**
     * Creates a new ProGuard object to process jars as specified by the given
     * configuration.
     */
    public ProGuard(Configuration configuration)
    {
        this.configuration = configuration;
    }

    private static long BYTES_PER_MB = 1024 * 1024;

    public static void log(String prefixAppendMessage,long begin) {
        long heapSize = Runtime.getRuntime().totalMemory();
        long heapMaxSize = Runtime.getRuntime().maxMemory();
        // long heapFreeSize=Runtime.getRuntime().freeMemory();

        long heapSizeM = heapSize / BYTES_PER_MB;
        long heapMaxSizeM = heapMaxSize / BYTES_PER_MB;
        // long heapFreeSizeM=heapFreeSize/Constant.Capacity.BYTES_PER_MB;
        short currentPercent = (short) ((double) heapSizeM / heapMaxSizeM * 100);
        System.out.println(prefixAppendMessage+",cost:" + (System.currentTimeMillis() - begin) + "," + "jvm used percent:" + currentPercent + "%" + ",heap size:" + heapSizeM + "M,max:" + heapMaxSizeM + "M");
    }
    /**
     * Performs all subsequent ProGuard operations.
     */
    public void execute() throws IOException
    {
        long totalBegin = System.currentTimeMillis();
        long begin = totalBegin;
        System.out.println(VERSION);

        GPL.check();

        if (configuration.printConfiguration != null)
        {
            begin = System.currentTimeMillis();
            printConfiguration();
            log("printConfiguration", begin);
        }
        begin = System.currentTimeMillis();
        new ConfigurationChecker(configuration).check();
        log("ConfigurationChecker.check", begin);

        if (configuration.programJars != null     &&
            configuration.programJars.hasOutput() &&
            new UpToDateChecker(configuration).check())
        {
            return;
        }
        begin = System.currentTimeMillis();
        readInput();
        log("readInput", begin);
        if (configuration.shrink    ||
            configuration.optimize  ||
            configuration.obfuscate ||
            configuration.preverify)
        {
            begin = System.currentTimeMillis();
            clearPreverification();
            log("clearPreverification", begin);
        }

        if (configuration.printSeeds != null ||
            configuration.shrink    ||
            configuration.optimize  ||
            configuration.obfuscate ||
            configuration.preverify)
        {
            begin = System.currentTimeMillis();
            initialize();
            log("initialize", begin);
        }

        if (configuration.targetClassVersion != 0)
        {
            begin = System.currentTimeMillis();
            target();
            log("targetClassVersion", begin);
        }

        if (configuration.printSeeds != null)
        {
            begin = System.currentTimeMillis();
            printSeeds();
            log("printSeeds", begin);
        }

        if (configuration.shrink)
        {
            begin = System.currentTimeMillis();
            shrink();
            log("shrink", begin);
        }

        if (configuration.preverify)
        {
            begin = System.currentTimeMillis();
            inlineSubroutines();
            log("inlineSubroutines", begin);
        }

        if (configuration.optimize)
        {
            for (int optimizationPass = 0;
                 optimizationPass < configuration.optimizationPasses;
                 optimizationPass++)
            {
                begin = System.currentTimeMillis();
                if (!optimize())
                {
                    // Stop optimizing if the code doesn't improve any further.
                    break;
                }

                // Shrink again, if we may.
                if (configuration.shrink)
                {
                    // Don't print any usage this time around.
                    configuration.printUsage       = null;
                    configuration.whyAreYouKeeping = null;

                    shrink();
                }
                log("optimize & shrink", begin);
            }
        }

        if (configuration.obfuscate)
        {
            begin = System.currentTimeMillis();
            obfuscate();
            log("obfuscate", begin);
        }

        if (configuration.preverify)
        {
            begin = System.currentTimeMillis();
            preverify();
            log("preverify", begin);
        }

        if (configuration.shrink    ||
            configuration.optimize  ||
            configuration.obfuscate ||
            configuration.preverify)
        {
            begin = System.currentTimeMillis();
            sortClassElements();
            log("sortClassElements", begin);
        }

        if (configuration.programJars.hasOutput())
        {
            begin = System.currentTimeMillis();
            writeOutput();
            log("writeOutput", begin);
        }

        if (configuration.dump != null)
        {
            begin = System.currentTimeMillis();
            dump();
            log("dump", begin);
        }
        log("Proguard all execute", totalBegin);
    }


    /**
     * Prints out the configuration that ProGuard is using.
     */
    private void printConfiguration() throws IOException
    {
        if (configuration.verbose)
        {
            System.out.println("Printing configuration to [" + fileName(configuration.printConfiguration) + "]...");
        }

        PrintStream ps = createPrintStream(configuration.printConfiguration);
        try
        {
            new ConfigurationWriter(ps).write(configuration);
        }
        finally
        {
            closePrintStream(ps);
        }
    }


    /**
     * Reads the input class files.
     */
    private void readInput() throws IOException
    {
        if (configuration.verbose)
        {
            System.out.println("Reading input...");
        }

        // Fill the program class pool and the library class pool.
        new InputReader(configuration).execute(programClassPool, libraryClassPool);
    }


    /**
     * Initializes the cross-references between all classes, performs some
     * basic checks, and shrinks the library class pool.
     */
    private void initialize() throws IOException
    {
        if (configuration.verbose)
        {
            System.out.println("Initializing...");
        }

        new Initializer(configuration).execute(programClassPool, libraryClassPool);
    }


    /**
     * Sets that target versions of the program classes.
     */
    private void target() throws IOException
    {
        if (configuration.verbose)
        {
            System.out.println("Setting target versions...");
        }

        new Targeter(configuration).execute(programClassPool);
    }


    /**
     * Prints out classes and class members that are used as seeds in the
     * shrinking and obfuscation steps.
     */
    private void printSeeds() throws IOException
    {
        if (configuration.verbose)
        {
            System.out.println("Printing kept classes, fields, and methods...");
        }

        PrintStream ps = createPrintStream(configuration.printSeeds);
        try
        {
            new SeedPrinter(ps).write(configuration, programClassPool, libraryClassPool);
        }
        finally
        {
            closePrintStream(ps);
        }
    }


    /**
     * Performs the shrinking step.
     */
    private void shrink() throws IOException
    {
        if (configuration.verbose)
        {
            System.out.println("Shrinking...");

            // We'll print out some explanation, if requested.
            if (configuration.whyAreYouKeeping != null)
            {
                System.out.println("Explaining why classes and class members are being kept...");
            }

            // We'll print out the usage, if requested.
            if (configuration.printUsage != null)
            {
                System.out.println("Printing usage to [" + fileName(configuration.printUsage) + "]...");
            }
        }

        // Perform the actual shrinking.
        programClassPool =
            new Shrinker(configuration).execute(programClassPool, libraryClassPool);
    }


    /**
     * Performs the subroutine inlining step.
     */
    private void inlineSubroutines()
    {
        if (configuration.verbose)
        {
            System.out.println("Inlining subroutines...");
        }

        // Perform the actual inlining.
        new SubroutineInliner(configuration).execute(programClassPool);
    }


    /**
     * Performs the optimization step.
     */
    private boolean optimize() throws IOException
    {
        if (configuration.verbose)
        {
            System.out.println("Optimizing...");
        }

        // Perform the actual optimization.
        return new Optimizer(configuration).execute(programClassPool, libraryClassPool);
    }


    /**
     * Performs the obfuscation step.
     */
    private void obfuscate() throws IOException
    {
        if (configuration.verbose)
        {
            System.out.println("Obfuscating...");

            // We'll apply a mapping, if requested.
            if (configuration.applyMapping != null)
            {
                System.out.println("Applying mapping [" + fileName(configuration.applyMapping) + "]");
            }

            // We'll print out the mapping, if requested.
            if (configuration.printMapping != null)
            {
                System.out.println("Printing mapping to [" + fileName(configuration.printMapping) + "]...");
            }
        }

        // Perform the actual obfuscation.
        new Obfuscator(configuration).execute(programClassPool, libraryClassPool);
    }


    /**
     * Clears any JSE preverification information from the program classes.
     */
    private void clearPreverification()
    {
        programClassPool.classesAccept(
            new ClassVersionFilter(ClassConstants.CLASS_VERSION_1_6,
            new AllMethodVisitor(
            new AllAttributeVisitor(
            new NamedAttributeDeleter(ClassConstants.ATTR_StackMapTable)))));
    }


    /**
     * Performs the preverification step.
     */
    private void preverify()
    {
        if (configuration.verbose)
        {
            System.out.println("Preverifying...");
        }

        // Perform the actual preverification.
        new Preverifier(configuration).execute(programClassPool);
    }


    /**
     * Sorts the elements of all program classes.
     */
    private void sortClassElements()
    {
        programClassPool.classesAccept(new ClassElementSorter());
    }


    /**
     * Writes the output class files.
     */
    private void writeOutput() throws IOException
    {
        if (configuration.verbose)
        {
            System.out.println("Writing output...");
        }

        // Write out the program class pool.
        new OutputWriter(configuration).execute(programClassPool);
    }


    /**
     * Prints out the contents of the program classes.
     */
    private void dump() throws IOException
    {
        if (configuration.verbose)
        {
            System.out.println("Printing classes to [" + fileName(configuration.dump) + "]...");
        }

        PrintStream ps = createPrintStream(configuration.dump);
        try
        {
            programClassPool.classesAccept(new ClassPrinter(ps));
        }
        finally
        {
            closePrintStream(ps);
        }
    }


    /**
     * Returns a print stream for the given file, or the standard output if
     * the file name is empty.
     */
    private PrintStream createPrintStream(File file)
    throws FileNotFoundException
    {
        return file == Configuration.STD_OUT ? System.out :
            new PrintStream(
            new BufferedOutputStream(
            new FileOutputStream(file)));
    }


    /**
     * Closes the given print stream, or closes it if is the standard output.
     * @param printStream
     */
    private void closePrintStream(PrintStream printStream)
    {
        if (printStream == System.out)
        {
            printStream.flush();
        }
        else
        {
            printStream.close();
        }
    }


    /**
     * Returns the canonical file name for the given file, or "standard output"
     * if the file name is empty.
     */
    private String fileName(File file)
    {
        if (file == Configuration.STD_OUT)
        {
            return "standard output";
        }
        else
        {
            try
            {
                return file.getCanonicalPath();
            }
            catch (IOException ex)
            {
                return file.getPath();
            }
        }
    }


    /**
     * The main method for ProGuard.
     */
    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            System.out.println(VERSION);
            System.out.println("Usage: java proguard.ProGuard [options ...]");
//            System.exit(1);
        }

        // Create the default options.
        Configuration configuration = new Configuration();

        try
        {
            // Parse the options specified in the command line arguments.
            ConfigurationParser parser = new ConfigurationParser(args,
                                                                 System.getProperties());
            try
            {
                parser.parse(configuration);
            }
            finally
            {
                parser.close();
            }

            // Execute ProGuard with these options.
            new ProGuard(configuration).execute();
        }
        catch (Exception ex)
        {
            if (configuration.verbose)
            {
                // Print a verbose stack trace.
                ex.printStackTrace();
            }
            else
            {
                // Print just the stack trace message.
                System.err.println("Error: "+ex.getMessage());
            }

//            System.exit(1);
        }

    }
}
