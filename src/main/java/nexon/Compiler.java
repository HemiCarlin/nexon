package nexon;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class Compiler {

    private static final List<String> imports = new ArrayList<>();

    /**
     * Entry point: reads and compiles a source file, creating .java files for each public class.
     */
    public static void readFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> classBuffer = new ArrayList<>();
            String currentClassName = null;
            boolean insideClass = false;

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.strip();
                if (line.isEmpty()) continue;

                // --- Handle imports ---
                if (line.startsWith("import ")) {
                    imports.add(line);
                    continue;
                }

                // --- Handle file includes ---
                if (line.startsWith("fil ")) {
                    String includedFile = extractFileName(line);
                    if (includedFile != null) {
                        System.out.println("Including file: " + includedFile);
                        readFile(includedFile); // recursive include
                    }
                    continue;
                }

                // --- Detect new public class ---
                if (line.startsWith("public class ")) {
                    // Write previous class (if any)
                    if (insideClass && currentClassName != null) {
                        writeClassFile(currentClassName, classBuffer);
                        classBuffer.clear();
                    }

                    insideClass = true;
                    currentClassName = extractClassName(line);
                    classBuffer.add(line);
                    continue;
                }

                // --- Collect lines for the current class ---
                if (insideClass) {
                    classBuffer.add(line);
                }
            }

            // Write last class if still open
            if (insideClass && currentClassName != null) {
                writeClassFile(currentClassName, classBuffer);
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Extracts a class name from a 'public class ClassName' line.
     */
    private static String extractClassName(String line) {
        String[] parts = line.split("\\s+");
        for (int i = 0; i < parts.length - 1; i++) {
            if (parts[i].equals("class")) {
                return parts[i + 1].replace("{", "").trim();
            }
        }
        return "UnknownClass";
    }

    /**
     * Extracts the filename from a line like: fil "file.txt"
     */
    private static String extractFileName(String line) {
        int firstQuote = line.indexOf('"');
        int lastQuote = line.lastIndexOf('"');
        if (firstQuote != -1 && lastQuote > firstQuote) {
            return line.substring(firstQuote + 1, lastQuote);
        }
        System.err.println("Invalid file include syntax: " + line);
        return null;
    }

    /**
     * Writes a list of lines to a .java file with its imports.
     */
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

    /**
     * Compiles and runs a Java class by name.
     */
    public static void run(String className) throws IOException, InterruptedException {
        Command.excCommand("javac " + className + ".java");
        Thread.sleep(2000);
        Command.excCommand("java " + className);
    }

    public static String modifyLine(String input) {
        String output = input;
        output = output.replace("main", "public static void main(String[] args)");
        if (output.charAt(output.length() - 1) != ';' && output.charAt(output.length() - 1) != '{'
                && output.charAt(output.length() - 1) != '}' && output.charAt(output.length() - 1) != ',') {
            output = output + ";";
        }
        return output;
    }
}
