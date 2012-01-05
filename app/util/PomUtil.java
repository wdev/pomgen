package util;

import java.io.File;
import java.io.IOException;

public interface PomUtil {

    public void createPom(File pom) throws IOException;
    
    public void createPom(File module, boolean override) throws IOException;
}
