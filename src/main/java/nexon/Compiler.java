package nexon;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class Compiler {

    private static final List<String> imports = new ArrayList<>();

    public static void readFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> classBuffer = new ArrayList<>();
            String currentClassName = null;
            boolean insideClass = false;

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.strip();

                if (line.isEmpty()) continue;

                // Capture imports globally
                if (line.startsWith("import ")) {
                    imports.add(line);
                    continue;
                }

                // Detect public class
                if (line.startsWith("public class ")) {
                    // If already inside a class, write it to file first
                    if (insideClass && currentClassName != null) {
                        writeClassFile(currentClassName, classBuffer);
                        classBuffer.clear();
                    }

                    insideClass = true;
                    currentClassName = extractClassName(line);
                    classBuffer.add(line);
                    continue;
                }

                // Collect lines for the current class
                if (insideClass) {
                    classBuffer.add(line);
                }
            }

            // Write last class if one remains
            if (insideClass && currentClassName != null) {
                writeClassFile(currentClassName, classBuffer);
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private static String extractClassName(String line) {
        String[] parts = line.split("\\s+");
        for (int i = 0; i < parts.length - 1; i++) {
            if (parts[i].equals("class")) {
                return parts[i + 1].replace("{", "").trim();
            }
        }
        return "UnknownClass";
    }

    private static void writeClassFile(String className, List<String> classLines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(className + ".java"))) {
            // Write imports first
            for (String imp : imports) {
                writer.write(imp);
                writer.newLine();
            }
            writer.newLine();

            // Write class content
            for (String line : classLines) {
                writer.write(modifyLine(line));
                writer.newLine();
            }

            System.out.println("Created file: " + className + ".java");
        } catch (IOException e) {
            System.err.println("Error writing " + className + ".java: " + e.getMessage());
        }
    }

    public static void run(String className) throws IOException, InterruptedException {
        Command.excCommand("javac " + className + ".java");
        Thread.sleep(1000);
        Command.excCommand("java " + className);
    }

    public static String modifyLine(String line) {
        try {
            if(line.lastIndexOf(';') == line.length() - 1) {
                line += ";";
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return line;
    }
}
