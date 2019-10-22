import java.io.File;
import java.util.*;

public class Test {

    public static void main(String[] args) {

        System.out.println(String.format("%s", 3 + Double.valueOf(3.0/10)));

//        TimeZone tz = TimeZone.getTimeZone("Asia/Tehran");
//        System.out.println("Time Zone: " + tz);

//        String[] id = TimeZone.getAvailableIDs();
//        System.out.println("In TimeZone class available Ids are: ");
//        for (int i = 0; i < id.length; i++) {
//            System.out.println(id[i] + " === " + TimeZone.getTimeZone(id[i]));
//        }

    }

    public static void fileSearch(File file, List<File> result) {
        if (file.isFile()) {
            if (file.getName().endsWith(".xhtml")) {
                result.add(file);
            }
        } else if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                fileSearch(f, result);
            }
        }
    }

    public void test() {
        List<File> results = new ArrayList<>();
        System.out.println("File under: " + new File("./src/main/resources/META-INF/resources/").getAbsolutePath());
        for (File file : new File("./src/main/resources/META-INF/resources/").listFiles()) {
            fileSearch(file, results);
        }

        results.forEach(file -> System.out.println(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("resources")).replace("resources\\", "")));

    }
}
