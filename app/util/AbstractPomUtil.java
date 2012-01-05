package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import models.Project;

import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;

public abstract class AbstractPomUtil implements PomUtil {

    protected final String VERSION = "1.0-SNAPSHOT";
    
    protected Project project;
    
    
    public void createPom(File module, boolean override) throws IOException {
        if (override) {
            module.delete();
        }
        createPom(module);
    }
    
    protected Model getMavenModel(String moduleName, String packaging) {
        Model model = new Model();
        model.setModelEncoding("ISO-8859-1");
        model.setModelVersion("4.0.0");
        model.setVersion(VERSION);
        model.setPackaging(packaging);
        model.setArtifactId(moduleName);
        model.setName(moduleName);
        return model;
    }
    
    protected Parent getParent(String moduleName) {
        Parent parent = new Parent();
        parent.setGroupId(project.name);
        parent.setArtifactId(project.name + "-pom");
        parent.setVersion(VERSION);
        parent.setRelativePath("../" + project.name + "-pom/pom.xml");
        return parent;
    }
    
    protected void savePomFile(File pom, Model model) throws IOException {
        new MavenXpp3Writer().write(new FileWriter(pom), model);
    }
}
