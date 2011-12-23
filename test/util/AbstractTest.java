package util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import play.test.UnitTest;

public class AbstractTest extends UnitTest {

    protected final String TEMP_DIR = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator");
    
    protected void deleteDir(String name) {
        this.deleteDir(new File(name));
    }
    
    protected void deleteDir(File path) {
        try {
            FileUtils.deleteDirectory(path);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
