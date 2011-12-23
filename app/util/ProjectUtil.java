package util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;

public class ProjectUtil {

    public static boolean hasModule(BsadProject project, String module) {
        return checkExists(project, module);
    }

    private static boolean checkExists(BsadProject project, String module) {
        File directory = project.getDirectory();
        if (directory.isDirectory()) {
            final String dirName = directory.getName() + "-" + module;
            
            FilenameFilter filter = new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.equals(dirName);
                }
            };
            
            String[] files = directory.list(filter);
            return files.length > 0;
        }
        return false;
    }

}
