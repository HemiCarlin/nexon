package nexon;

import java.io.File;

import org.eclipse.jgit.api.Git;

public class GitStuff {
    // Git git = Git.init().setDirectory("/path/to/repo").call();
    public static void main(String[] args) throws Exception {
        try (Git git = Git.open(new File("C:\\Users\\hcarlin845\\Documents\\GitHub\\nexon"))) {
            git.add().addFilepattern(".").call();
            git.commit().setMessage("My commit message").call();
            System.out.println("Committed changes");
        }
    }

}
