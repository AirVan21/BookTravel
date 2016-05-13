package daemon;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static spark.Spark.*;

public class Main {

    public static String directoryName = "uploaded";
    private static File directory = new File(directoryName);
    private static Thread workingThread;
    private static List<File> files = Collections.synchronizedList(new LinkedList<>());
    private volatile static int handledCount = 0;
    private volatile static int currentCount = 0;

    public static void main(String[] args) {

        System.out.println(directory.getAbsolutePath());

        if (args.length > 0) directoryName = args[1];

        workingThread = new Thread(() -> {
            while (!Thread.interrupted()) {
                updateList();
                files.forEach(Main::handle);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
        });

        workingThread.start();

        get("/statistic", (req, res) -> "files in list " + currentCount + "; handled " + handledCount);
    }

    private static void updateList() {
        files.clear();
        try {
            Files.walk(Paths.get(directoryName)).forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    files.add(filePath.toFile());
                }
            });
        } catch (IOException ignored) {
        }
        currentCount = files.size();
    }

    private static void handle(File file) {
        ++handledCount;
//        System.out.println(file.getName());
    }
}
