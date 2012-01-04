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
    
    public Project(File projectDir) {
        this.directory = projectDir;
    }
    
    public List<String> getModules() {
        String name = directory.getName();
        
        List<String> modules = new ArrayList<String>();
        
        if (checkExists("ear")) modules.add(name + "-ear"); 
        if (checkExists("ejb")) modules.add(name + "-ejb");
        if (checkExists("ejbClient")) modules.add(name + "-ejbClient");
        if (checkExists("pom")) modules.add(name + "-pom");
        if (checkExists("web")) modules.add(name + "-web");
        if (checkExists("ws")) modules.add(name + "-ws");
        if (checkExists("util")) modules.add(name + "-util");
        
        return modules;
    }
    
    private boolean checkExists(String module) {
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
