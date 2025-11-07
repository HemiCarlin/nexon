package nexon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public final class Compiler {
    static String name = "";
    static ArrayList<String> imports = new ArrayList<>();

    static public void readFile(String filePath, boolean run) throws InterruptedException {
        StringBuilder contentBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            name = filePath.split("\\.")[0];
            String className = name;
            String line;

            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append(System.getProperty("line.separator"));
                if (line.contains("import ")) {
                    imports.add(line);
                } else if (line.contains("fil")) {
                    readFile(line.split("fil ")[1], false);
                } else if (!line.equals("")) {
                    className = newClass(line, className);
                    line = modifyLine(line);
                    Command.excCommand("echo " + line + " >> " + className + ".java");
                    Thread.sleep(200);
                }
            }
            reader.close();
        } catch (Exception e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        }
    }

    public static String newClass(String input, String currentClass) throws Exception {
        if (input.contains("class")) {
            String subName = input.split("class")[1].split(" ")[1];
            Command.excCommand("type nul > " + subName + ".java");
            for (String a : imports) {
                Command.excCommand("echo " + a + " >> " + subName + ".java");
            }
            Thread.sleep(200);
            return subName;
        }
        Thread.sleep(200);
        return currentClass;
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

    public static void run(String file) throws IOException, InterruptedException {
        Command.excCommand("javac " + file + ".java");
        Thread.sleep(2000);
        try {
            Command.excCommand("java " + file);
        } catch (Exception e) {
            System.out.println("tried to run");
        }

    }
}
