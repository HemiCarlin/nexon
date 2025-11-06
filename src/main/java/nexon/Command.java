package nexon;
import java.io.IOException;

public final class Command {
    public static void excCommand(String command) throws IOException {
        Runtime rt = Runtime.getRuntime();
        // rt.exec(new String[] { "cmd.exe", "/c", "type nul > " + "hello.java" });
        rt.exec(new String[] { "cmd.exe", "/c", command });
    }
}
