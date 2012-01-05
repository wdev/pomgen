package models;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.db.jpa.Model;

@Entity(name="projects")
public class Project extends Model {

    @Column(unique=true)
    public String name;
    
    public String status;
    
    @Temporal(TemporalType.DATE)
    public Date date;
    
    private File directory;
    
    public Project(File project) {
        this.directory = project;
        this.name = project.getName();
    }
    
    public List<String> getModules() {
        List<String> modules = new ArrayList<String>();
        modules.add(getEar().getName()); 
        modules.add(getEjb().getName());
        modules.add(getEjbClient().getName());
        modules.add(getPom().getName());
        modules.add(getWeb().getName());
        modules.add(getWs().getName());
        modules.add(getUtil().getName());
        for (int index=0; index < modules.size(); index++) {
            if (modules.get(index) == null || modules.get(index).equals("")) {
                modules.remove(index);
            }
        }
        
        return modules;
    }
    
    public File getEar() {
        return getModuleIfExists("");
    }
    
    public File getEjb() {
        return getModuleIfExists("-ejb");
    }
    
    public File getEjbClient() {
        return getModuleIfExists("-ejbClient");
    }
    
    public File getPom() {
        return getModuleIfExists("-pom");
    }
    
    public File getWeb() {
        return getModuleIfExists("-web");
    }
    
    public File getWs() {
        return getModuleIfExists("-ws");
    }
    
    public File getUtil() {
        return getModuleIfExists("-util");
    }
    
    public File getModuleIfExists(String module) {
        String moduleName = directory.getName() + module;
        if (moduleExists(moduleName)) {
            return new File(directory.getPath() + System.getProperty("file.separator") + moduleName);
        }
        return new File("");
    }
    
    private boolean moduleExists(final String moduleName) {
        if (directory.isDirectory()) {
            FilenameFilter filter = new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.equals(moduleName);
                }
            };
            
            String[] files = directory.list(filter);
            return files.length > 0;
        }
        return false;
    }
    
}
