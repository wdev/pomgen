package util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.Project;

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Plugin;
import org.codehaus.plexus.util.xml.Xpp3Dom;

public class MainPomUtil extends AbstractPomUtil {

    public MainPomUtil(Project project) {
        this.project = project;
    }
    
    public void createPom(File pom) throws IOException {
        String moduleName = project.getPom().getName();
        
        Model model = getMavenModel(moduleName, "pom");

        Parent parent = getParent(moduleName);
        parent.setRelativePath(null);
        model.setParent(parent);
        
        Build build = getBuild();
        build.setPlugins(getPlugins());
        model.setBuild(build);
        
        model.setModules(getModules());        
        
        savePomFile(pom, model);
    }

    private Build getBuild() {
        Build build = new Build();
        build.setDefaultGoal("install");
        return build;
    }

    private List<String> getModules() {
        List<String> modules = new ArrayList<String>();
        modules = addIfExists(modules, project.getEar());
        modules = addIfExists(modules, project.getEjb());
        modules = addIfExists(modules, project.getEjbClient());
        modules = addIfExists(modules, project.getWeb());
        modules = addIfExists(modules, project.getWs());
        modules = addIfExists(modules, project.getUtil());
        return modules;
    }

    private List<String> addIfExists(List<String> modules, File file) {
        if (file.isDirectory()) {
            modules.add(file.getName());
        }
        return modules;
    }
    
    private List<Plugin> getPlugins() {
        Plugin plugin = new Plugin();
        plugin.setGroupId("org.apache.maven.plugins");
        plugin.setArtifactId("maven-compiler-plugin");
        
        Xpp3Dom configuration = new Xpp3Dom("configuration");
        
        Xpp3Dom encoding = getXmlNode("encoding", "${project.build.sourceEncoding}");
        configuration.addChild(encoding);
        
        Xpp3Dom target = getXmlNode("target", "${maven.compiler.target}");
        configuration.addChild(target);
        
        Xpp3Dom source = getXmlNode("source", "${maven.compiler.source}");
        configuration.addChild(source);
        
        Xpp3Dom debug = getXmlNode("debug", "true");
        configuration.addChild(debug);
        
        Xpp3Dom debugLevel = getXmlNode("debugLevel", "lines,vars,source");
        configuration.addChild(debugLevel);
        
        Xpp3Dom verbose = getXmlNode("verbose", "true");
        configuration.addChild(verbose);
        
        Xpp3Dom showWarnings = getXmlNode("showWarnings", "true");
        configuration.addChild(showWarnings);
        
        Xpp3Dom showDeprecation = getXmlNode("showDeprecation", "true");
        configuration.addChild(showDeprecation);
        
        Xpp3Dom xlint = getXmlNode("Xlint", "");
        
        Xpp3Dom compilerArguments = new Xpp3Dom("compilerArguments");
        compilerArguments.addChild(xlint);
        configuration.addChild(compilerArguments);
        
        plugin.setConfiguration(configuration);
        
        List<Plugin> plugins = new ArrayList<Plugin>();
        plugins.add(plugin);
        
        return plugins;
    }

    private Xpp3Dom getXmlNode(String name, String value) {
        Xpp3Dom node = new Xpp3Dom(name);
        node.setValue(value);
        return node;
    }
}
