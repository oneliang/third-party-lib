package proguard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.oneliang.util.file.FileUtil;

public class Main {

    private static List<String> proguardConfigList = new ArrayList<String>();
    private static List<ProguardJarPair> proguardJarPairList = new ArrayList<ProguardJarPair>();
    private static List<String> classpathList = new ArrayList<String>();
    private static List<String> originalJarList = new ArrayList<String>();

    static {
        originalJarList.add("D:/builder-gen/boot/build/public/build/optimized/original/public.jar");
        originalJarList.add("D:/builder-gen/boot/build/optimized/original/boot.jar");
        originalJarList.add("D:/builder-gen/boot/build/optimized/original/9483587eb71782760af0725befd6ea7d.jar");
        originalJarList.add("D:/builder-gen/boot/build/optimized/original/38c5418211a6ef9e2f3b70e595f5794a.jar");
        originalJarList.add("D:/builder-gen/boot/build/optimized/original/841a831141d64bb278b12525e200d847.jar");
        originalJarList.add("D:/builder-gen/boot/build/optimized/original/bda6ff9432d8284d83507be1f382eb58.jar");
        originalJarList.add("D:/builder-gen/boot/build/optimized/original/4a307146b370110782b7044227bc1f04.jar");
        originalJarList.add("D:/builder-gen/boot/build/optimized/original/8f34284df90f205e9919983e541bee23.jar");
        originalJarList.add("D:/builder-gen/boot/build/optimized/original/d5099a7b53b8abc155ff2ed27d807897.jar");
        originalJarList.add("D:/builder-gen/boot/build/optimized/original/76cdb2bad9582d23c1f6f4d868218d6c.jar");
        originalJarList.add("D:/builder-gen/boot/build/optimized/original/eb26605b64f109a1773703697baa3781.jar");
        originalJarList.add("D:/builder-gen/boot/build/optimized/original/a162ab3390e29b25c55749ac8a860a5e.jar");
        originalJarList.add("D:/builder-gen/boot/build/optimized/original/c67dbf9454e32e4c399c905dd13a7315.jar");
        originalJarList.add("D:/builder-gen/boot/build/optimized/original/8dd93c58bcc7b650f4f3968b95008d40.jar");
        originalJarList.add("D:/builder-gen/boot/build/optimized/original/3241b53944dea6baaae6b486e70c0974.jar");
        originalJarList.add("D:/builder-gen/boot/build/optimized/original/6a5a09590a12a88681cbd9104d73cd2f.jar");
        originalJarList.add("D:/builder-gen/boot/build/optimized/original/c5a1a4d0e132746c9f809cd79a68f97a.jar");
        originalJarList.add("D:/builder-gen/boot/build/optimized/original/f3d3cfee9982effea369f094573b4343.jar");
        originalJarList.add("D:/builder-gen/boot/build/optimized/original/813f6c1cece890a476b432caf7e77bdf.jar");
        originalJarList.add("D:/builder-gen/boot/build/optimized/original/6d2010483186d006436d718bf36ee74d.jar");
        originalJarList.add("D:/builder-gen/boot/build/optimized/original/b67790089453be64074a5d0a178dba60.jar");
        originalJarList.add("D:/builder-gen/boot/build/optimized/original/ad83f003953284d7a538f8aedf3afa59.jar");
        originalJarList.add("D:/builder-gen/boot/build/optimized/original/e24dae3aa3ed73fd7215ea583946b8e6.jar");
        originalJarList.add("D:/builder-gen/boot/build/optimized/original/a1e05a1408978c0d50c01ce04da7573f.jar");
        originalJarList.add("D:/builder-gen/app/build/optimized/original/app.jar");
        originalJarList.add("D:/builder-gen/app/build/optimized/original/7d77584ef30433a6207db2ad913c2b0c.jar");
        originalJarList.add("D:/builder-gen/app/build/optimized/original/e18c4208492f7e1f3d0d17c826494cbd.jar");
        originalJarList.add("D:/builder-gen/app/build/optimized/original/15d098aba836449f92b39ee9acf9ba70.jar");
        originalJarList.add("D:/builder-gen/app/build/optimized/original/ccdf7b1a738998760cf1b77e737826e1.jar");
        originalJarList.add("D:/builder-gen/app/build/optimized/original/175459361a1e7a8203a321af8877e69e.jar");
        originalJarList.add("D:/builder-gen/app/build/optimized/original/10c813e0feaaa380c5ff9148c0a9daf6.jar");
        originalJarList.add("D:/builder-gen/app/build/optimized/original/0ea1fe8659ecac9083c27121fa9a89f6.jar");
        originalJarList.add("D:/builder-gen/app/build/optimized/original/a6ac92157c00cb453305c5e65528a1d4.jar");
        originalJarList.add("D:/builder-gen/app/build/optimized/original/59be1875d95fb9d0850048e166de022a.jar");
        originalJarList.add("D:/builder-gen/app/build/optimized/original/2d7605bc6c56ee846e3f45ed9e13668a.jar");
        originalJarList.add("D:/builder-gen/app/build/optimized/original/f787b674bbb948edee3a9740edf1d128.jar");
        originalJarList.add("D:/builder-gen/app/build/optimized/original/2e77329b33abfd04753ea6f592c1bb17.jar");
        originalJarList.add("D:/builder-gen/app/build/optimized/original/2dde05bd85427a16fd5623c974b5885c.jar");
        originalJarList.add("D:/builder-gen/app/build/optimized/original/00e262da755610e7ea900a2135cd9dcc.jar");
        originalJarList.add("D:/builder-gen/app/build/optimized/original/8a7a538eb93749001037479054141803.jar");
        originalJarList.add("D:/builder-gen/app/build/optimized/original/f0de7c7e57abf9a2408c9d895a843e0d.jar");
        originalJarList.add("D:/builder-gen/app/build/optimized/original/006081c20058ab79f7ff56305c7e742d.jar");
        originalJarList.add("D:/builder-gen/app/build/optimized/original/a4a45d2abc221cdd9a4bfdd1256c9cc0.jar");
        originalJarList.add("D:/builder-gen/app/build/optimized/original/d638ce01bc3547f5e2f0bc5524dde2c4.jar");
        originalJarList.add("D:/builder-gen/app/build/optimized/original/db70444ac6167820d0c0dcfd8b703f77.jar");
        originalJarList.add("D:/builder-gen/app/build/optimized/original/a583d8e63e007d597e1f734cea973a5e.jar");
        originalJarList.add("D:/builder-gen/app/build/optimized/original/3f5a0eda4745c0911c0f3d4ce3096c49.jar");
        originalJarList.add("D:/builder-gen/app/build/optimized/original/22fc7adc43f221eb055b05a4e5c6d2b2.jar");
        originalJarList.add("D:/builder-gen/app/build/optimized/original/1c9db637fea1a3d1c7339f9a7b39f871.jar");
        originalJarList.add("D:/builder-gen/plugin-auth/build/optimized/original/plugin-auth.jar");
        originalJarList.add("D:/builder-gen/plugin-auth/build/optimized/original/02456215fe0e246f0fb8b73a5539a3b5.jar");
        originalJarList.add("D:/builder-gen/plugin-messenger-foundation/build/optimized/original/plugin-messenger-foundation.jar");
        originalJarList.add("D:/builder-gen/mmkernel/build/optimized/original/mmkernel.jar");
        originalJarList.add("D:/builder-gen/mmkernel/build/optimized/original/0d96249221b54ba0de9dfd4a8da603bb.jar");
        originalJarList.add("D:/builder-gen/mmkernel/build/optimized/original/d68ba14f02a7807b9dc1d54b54c6f4eb.jar");
        originalJarList.add("D:/builder-gen/mmkernel/build/optimized/original/6ed5287f56ed3224bea293857a1db161.jar");
        originalJarList.add("D:/builder-gen/group-search/plugin-search/build/optimized/original/group-search-plugin-search.jar");
        originalJarList.add("D:/builder-gen/group-lbs/plugin-location/build/optimized/original/group-lbs-plugin-location.jar");
        originalJarList.add("D:/builder-gen/group-messenger/plugin-notification/build/optimized/original/group-messenger-plugin-notification.jar");
        originalJarList.add("D:/builder-gen/plugin-welab/build/optimized/original/plugin-welab.jar");
        originalJarList.add("D:/builder-gen/.api/plugin-messenger-foundation@api/build/optimized/original/.api-plugin-messenger-foundation@api.jar");
        originalJarList.add("D:/builder-gen/.api/group-messenger/plugin-notification@api/build/optimized/original/.api-group-messenger-plugin-notification@api.jar");
        originalJarList.add("D:/builder-gen/.api/group-lbs/plugin-location@api/build/optimized/original/.api-group-lbs-plugin-location@api.jar");
        originalJarList.add("D:/builder-gen/.api/plugin-welab@api/build/optimized/original/.api-plugin-welab@api.jar");
        originalJarList.add("D:/builder-gen/group-messenger/plugin-messenger/build/optimized/original/group-messenger-plugin-messenger.jar");
        originalJarList.add("D:/builder-gen/libmmui/build/optimized/original/libmmui.jar");
        originalJarList.add("D:/builder-gen/libmmui/build/optimized/original/47bf2f1efe1dd46585278052a21d08ca.jar");
        originalJarList.add("D:/builder-gen/plugin-report/build/optimized/original/plugin-report.jar");
        originalJarList.add("D:/builder-gen/plugin-performance/build/optimized/original/plugin-performance.jar");
        originalJarList.add("D:/builder-gen/recovery/build/optimized/original/recovery.jar");
        originalJarList.add("D:/builder-gen/group-plugin-sdk/plugin-hotcode/build/optimized/original/group-plugin-sdk-plugin-hotcode.jar");
        originalJarList.add("D:/builder-gen/group-plugin-sdk/plugin-hotcode/build/optimized/original/48d5850860a7db0b6a8938baa9bc8efd.jar");
        originalJarList.add("D:/builder-gen/group-plugin-sdk/plugin-coldcode/build/optimized/original/group-plugin-sdk-plugin-coldcode.jar");
        originalJarList.add("D:/builder-gen/group-biz/plugin-biz/build/optimized/original/group-biz-plugin-biz.jar");
        originalJarList.add("D:/builder-gen/group-media/plugin-audiosdk/build/optimized/original/group-media-plugin-audiosdk.jar");
        originalJarList.add("D:/builder-gen/group-media/plugin-audiosdk/build/optimized/original/290308f92561d2742dcd65b809f305c6.jar");
        originalJarList.add("D:/builder-gen/group-media/plugin-audiologic/build/optimized/original/group-media-plugin-audiologic.jar");
        originalJarList.add("D:/builder-gen/libaccessibility_capture/build/optimized/original/libaccessibility_capture.jar");
        originalJarList.add("D:/builder-gen/sdk/build/optimized/original/sdk.jar");
        originalJarList.add("D:/builder-gen/sdk/build/optimized/original/5e14e5f3f45a6cf2ba1c47084cff83a3.jar");
        originalJarList.add("D:/builder-gen/loader/build/optimized/original/loader.jar");
        originalJarList.add("D:/builder-gen/loader/build/optimized/original/1e2aceaddded474782a59e49041a19b5.jar");
        originalJarList.add("D:/builder-gen/libraries/libcrash/build/optimized/original/libraries-libcrash.jar");
        originalJarList.add("D:/builder-gen/libraries/libimageloader/build/optimized/original/libraries-libimageloader.jar");
        originalJarList.add("D:/builder-gen/libraries/libjnicomm/build/optimized/original/libraries-libjnicomm.jar");
        originalJarList.add("D:/builder-gen/libraries/libxlog/build/optimized/original/libraries-libxlog.jar");
        originalJarList.add("D:/builder-gen/libraries/libcompatible/build/optimized/original/libraries-libcompatible.jar");
        originalJarList.add("D:/builder-gen/libraries/libcompatible/build/optimized/original/6551cb1feacc980334df17711c54adea.jar");
        originalJarList.add("D:/builder-gen/libraries/libsfs/build/optimized/original/libraries-libsfs.jar");
        originalJarList.add("D:/builder-gen/libmmui_kit/build/optimized/original/libmmui_kit.jar");
        originalJarList.add("D:/builder-gen/libsvg/build/optimized/original/libsvg.jar");
        originalJarList.add("D:/builder-gen/group-messenger/plugin-chatroom/build/optimized/original/group-messenger-plugin-chatroom.jar");
        originalJarList.add("D:/builder-gen/.api/group-search/plugin-search@api/build/optimized/original/.api-group-search-plugin-search@api.jar");
        originalJarList.add("D:/builder-gen/libwxperf/build/optimized/original/libwxperf.jar");
        originalJarList.add("D:/builder-gen/group-plugin-sdk/compat/build/optimized/original/group-plugin-sdk-compat.jar");
        originalJarList.add("D:/builder-gen/.api/group-plugin-sdk/plugin-coldcode@api/build/optimized/original/.api-group-plugin-sdk-plugin-coldcode@api.jar");

        classpathList.add("D:/Dandelion/android/android-sdks/add-ons/addon-google_apis-google-23/libs/effects.jar");
        classpathList.add("D:/Dandelion/android/android-sdks/add-ons/addon-google_apis-google-23/libs/maps.jar");
        classpathList.add("D:/Dandelion/android/android-sdks/add-ons/addon-google_apis-google-23/libs/usb.jar");
        classpathList.add("D:/Dandelion/android/android-sdks/platforms/android-23/android.jar");
        classpathList.add("D:/Dandelion/android/android-sdks/platforms/android-23/optional/org.apache.http.legacy.jar");
    }

    public static void main(String[] args) throws Exception{
        // FileUtil.mergeZip("/D:/proguard/allOutput.jar", originalJarList);
        String projectRealPath = new File("").getAbsolutePath();
        String logFullFilename = projectRealPath + "/log/default.log";
        // loggerList.add(new BaseLogger(Logger.Level.VERBOSE));
        FileUtil.deleteAllFile("/D:/proguard/output");
        File logFile = new File(logFullFilename);
        logFile.delete();
        logFile.getParentFile().mkdirs();
        logFile.createNewFile();
        PrintStream printStream = new PrintStream(new FileOutputStream(logFile, true));
        System.setOut(printStream);
        System.setErr(printStream);

        proguardJarPairList.add(new ProguardJarPair("/D:/proguard/input/allOutput.jar", "/D:/proguard/output/allProguardOutput.jar"));
        proguardConfigList.add("D:/Dandelion/android/android-sdks/tools/proguard/proguard-android-optimize.txt");
        proguardConfigList.add("D:/proguard/input/tinker_proguard.pro");
        proguardConfigList.add("D:/proguard/input/proguard.cfg");
        proguard(proguardConfigList, proguardJarPairList, classpathList);
    }

    public static class ProguardJarPair {
        public final String inputJar;
        public final String outputJar;

        public ProguardJarPair(String inputJar, String outputJar) {
            this.inputJar = inputJar;
            this.outputJar = outputJar;
        }
    }

    public static void proguard(List<String> proguardConfigList, List<ProguardJarPair> proguardJarPairList, List<String> classpathList) {
        List<String> parameterList = new ArrayList<String>();
        if (proguardConfigList != null) {
            for (String proguardConfig : proguardConfigList) {
                parameterList.add("-include");
                parameterList.add(proguardConfig);
            }
        }
        if (proguardJarPairList != null) {
            for (ProguardJarPair proguardJarPair : proguardJarPairList) {
                parameterList.add("-injars");
                parameterList.add(proguardJarPair.inputJar);
                parameterList.add("-outjars");
                parameterList.add(proguardJarPair.outputJar);
            }
        }
        if (classpathList != null) {
            for (String classpath : classpathList) {
                parameterList.add("-libraryjars");
                parameterList.add(classpath);
            }
        }
        ProGuard.main(parameterList.toArray(new String[] {}));
    }
}
