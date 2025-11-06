package nexon;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public final class CreateFile {
    static ArrayList<String> imports = new ArrayList<>();

    public static void readFile(String filePath, boolean run) throws InterruptedException {
        StringBuilder contentBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            System.out.println("hello");
            String name = filePath.split("\\.")[0];
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
            Command.excCommand("javac " + name + ".java");
            if (run) {
                Thread.sleep(2000);
                try {
                    
                Command.excCommand("java " + name);
                } catch (Exception e) {
                    System.out.println("tried to run");
                }
                
            }
            reader.close();
        } catch (Exception e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        }
    }

    public static String newClass(String input, String currentClass) throws Exception {
        if (input.contains("class")) {
            String name = input.split("class")[1].split(" ")[1];
            Command.excCommand("type nul > " + name + ".java");
            for (String a : imports) {
                Command.excCommand("echo " + a + " >> " + name + ".java");
            }
            Thread.sleep(200);
            return name;
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
}
