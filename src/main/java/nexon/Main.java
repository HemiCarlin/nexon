package nexon;
public class Main {

    public static void main(String[] args) {
        try {
            if(args[0].equals("read")) {
                Compiler.readFile(args[1], true);
            } else if(args[0].equals("run")) {
                Compiler.run(args[1]);
            } else if(args[0].equals("clean")) {
                Command.excCommand("del " + args[1] + ".java");
                Command.excCommand("del " + args[1] + ".class");
            }// C:\\Users\\hcarlin845\\Documents\\GitHub\\garbadge\\
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}