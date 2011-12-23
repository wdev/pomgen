package util;

import java.io.File;

public class BsadProject {

    private File directory;

    public BsadProject(File projectDir) {
        this.directory = projectDir;
    }
    
    public File getDirectory() {
        return directory;
    }
    
}
