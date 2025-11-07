package nexon;

import java.io.File;

import org.eclipse.jgit.api.Git;

public class GitStuff {

    public static void commit(String file) throws Exception{
        try (Git git = Git.open(new File("C:\\Users\\hcarlin845\\Documents\\GitHub\\nexon"))) {
            git.add().addFilepattern(".").call();
            git.commit().setMessage("Running Save").call();
            System.out.println("Committed changes");
        }
    }

}
