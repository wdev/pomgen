package util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.Project;

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.Resource;
import org.codehaus.plexus.util.xml.Xpp3Dom;

public class EarPomUtil extends AbstractPomUtil {

    public EarPomUtil(Project project) {
        this.project = project;
    }
    
    public void createPom(File pom) throws IOException {
        String moduleName = project.getEar().getName();
        
        Model model = getMavenModel(moduleName, "ear");

        model.setParent(getParent(moduleName));
        
        Build build = getBuild();
        build.setResources(getResources());
        build.setPlugins(getPlugins());
        model.setBuild(build);
        
        savePomFile(pom, model);
    }

    private List<Plugin> getPlugins() {
        List<Plugin> plugins = new ArrayList<Plugin>();
        Plugin resourcePlugin = new Plugin();
        resourcePlugin.setGroupId("org.apache.maven.plugins");
        resourcePlugin.setArtifactId("maven-resources-plugin");
        
        Plugin earPlugin = new Plugin();
        earPlugin.setGroupId("org.apache.maven.plugins");
        earPlugin.setArtifactId("maven-ear-plugin");
        
        Xpp3Dom earSourceDirectory = new Xpp3Dom("earSourceDirectory");
        earSourceDirectory.setValue("${project.basedir}");
        
        Xpp3Dom earSourceExcludes = new Xpp3Dom("earSourceExcludes");
        earSourceExcludes.setValue("target/**, **/resources.xml/**, **/variables.xml/**, **/security.xml/**");
        
        Xpp3Dom generateApplicationXml = new Xpp3Dom("generateApplicationXml");
        generateApplicationXml.setValue("false");
        
        Xpp3Dom configuration = new Xpp3Dom("configuration");
        configuration.addChild(earSourceDirectory);
        configuration.addChild(earSourceExcludes);
        configuration.addChild(generateApplicationXml);
        configuration.addChild(getEarModules());
        
        earPlugin.setConfiguration(configuration);
        
        plugins.add(resourcePlugin);
        plugins.add(earPlugin);
        
        return plugins;
    }
    
    
    private Xpp3Dom getEarModules() {
        Xpp3Dom modules = new Xpp3Dom("modules");
        
        Xpp3Dom groupId = new Xpp3Dom("groupId");
        groupId.setValue("${project.groupId}");
        
        if (project.getEjb() != null) {
            Xpp3Dom artifactId = new Xpp3Dom("artifactId");
            artifactId.setValue(project.getEjb().getName());
            
            Xpp3Dom ejbModule = new Xpp3Dom("ejbModule");
            ejbModule.addChild(groupId);
            ejbModule.addChild(artifactId);
            
            modules.addChild(ejbModule);
        }
        
        if (project.getWeb() != null) {
            Xpp3Dom artifactId = new Xpp3Dom("artifactId");
            artifactId.setValue(project.getWeb().getName());
            
            Xpp3Dom webModule = new Xpp3Dom("webModule");
            webModule.addChild(groupId);
            webModule.addChild(artifactId);
            
            modules.addChild(webModule);
        }
        
        if (project.getWs() != null) {
            Xpp3Dom artifactId = new Xpp3Dom("artifactId");
            artifactId.setValue(project.getWs().getName());
            
            Xpp3Dom webModule = new Xpp3Dom("webModule");
            webModule.addChild(groupId);
            webModule.addChild(artifactId);
            
            modules.addChild(webModule);
        }
        
        return modules;
    }

    private List<Resource> getResources() {
        List<Resource> resources = new ArrayList<Resource>();
        Resource resource = new Resource();
        resource.setFiltering(false);
        resource.setDirectory("${basedir}/**/ibmconfig/cells/defaultCell");
        
        List<String> excludes = new ArrayList<String>();
        excludes.add("**/resources.xml/**, **/variables.xml/**, **/security.xml/**");
        resource.setExcludes(excludes);
        
        resources.add(resource);
        return resources;
    }

    private Build getBuild() {
        Build build = new Build();
        build.setFinalName("${project.groupId}");
        return build;
    }

}
