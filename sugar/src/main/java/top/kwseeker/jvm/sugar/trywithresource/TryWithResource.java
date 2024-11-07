package top.kwseeker.jvm.sugar.trywithresource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 语法糖：try-with-resource
 * java -jar tools/cfr-0.152.jar --tryresources false sugar/target/classes/top/kwseeker/jvm/sugar/trywithresource/TryWithResource.class
 */
public class TryWithResource {
    public static void main(String[] args) {
        try(BufferedReader br = new BufferedReader(new FileReader("test.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
//public class TryWithResource {
//    public static void main(String[] args) {
//        try {
//            BufferedReader br = new BufferedReader(new FileReader("test.txt"));
//            Throwable throwable = null;
//            try {
//                String line;
//                while ((line = br.readLine()) != null) {
//                    System.out.println(line);
//                }
//            }
//            catch (Throwable throwable2) {
//                throwable = throwable2;
//                throw throwable2;
//            }
//            finally {
//                if (br != null) {
//                    if (throwable != null) {
//                        try {
//                            br.close();
//                        }
//                        catch (Throwable throwable3) {
//                            throwable.addSuppressed(throwable3);
//                        }
//                    } else {
//                        br.close();
//                    }
//                }
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
