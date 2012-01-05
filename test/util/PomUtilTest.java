package util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.Project;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.Resource;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.Before;
import org.junit.Test;

import play.Play;
import play.test.UnitTest;

public class PomUtilTest extends AbstractTest {

    private final String POM = System.getProperty("file.separator") + "pom.xml";
    
    private Project project;
    
    @Before
    public void setUp() throws IOException {
        File original = Play.getFile("test/resources/BSAD2-Full");
        File clone = new File(TEMP_DIR + "BSAD2-Full");
        deleteDir(clone);
        FileUtils.copyDirectory(original, clone);
        
        project = new Project(clone);
    }
  
    @Test
    public void testCreateEarPom() {
        try {
            File earPom = new File(project.getEar().getPath() + POM);
            
            PomUtil pomUtil = new EarPomUtil(project);
            pomUtil.createPom(earPom, true);
            
            assertTrue(earPom.isFile());
            
            Model model = getMavenModelFrom(earPom);
            assertEquals("4.0.0", model.getModelVersion());
            assertEquals("BSAD2-Full", model.getArtifactId());
            assertEquals("BSAD2-Full", model.getName());
            assertEquals("ear", model.getPackaging());
            
            Parent parent = model.getParent();
            assertEquals("BSAD2-Full", parent.getGroupId());
            assertEquals("BSAD2-Full-pom", parent.getArtifactId());
            assertEquals("1.0-SNAPSHOT", parent.getVersion());
            assertEquals("../BSAD2-Full-pom/pom.xml", parent.getRelativePath());
            
            assertEquals(0, model.getDependencies().size());
            
            Build build = model.getBuild();
            assertEquals("${project.groupId}", build.getFinalName());
            
            Resource resource = build.getResources().get(0);
            assertEquals(1, build.getResources().size());
            assertEquals("${basedir}/**/ibmconfig/cells/defaultCell", resource.getDirectory());
            assertEquals("**/resources.xml/**, **/variables.xml/**, **/security.xml/**", resource.getExcludes().get(0));
            
            List<Plugin> plugins = build.getPlugins();
            assertEquals(2, plugins.size());
            assertEquals("org.apache.maven.plugins", plugins.get(0).getGroupId());
            assertEquals("maven-resources-plugin", plugins.get(0).getArtifactId());
            
            Plugin earPlugin = plugins.get(1);
            assertEquals("org.apache.maven.plugins", earPlugin.getGroupId());
            assertEquals("maven-ear-plugin", earPlugin.getArtifactId());
            
            Xpp3Dom configuration = (Xpp3Dom) earPlugin.getConfiguration();
            assertEquals("${project.basedir}", configuration.getChild("earSourceDirectory").getValue());
            assertEquals("target/**, **/resources.xml/**, **/variables.xml/**, **/security.xml/**", configuration.getChild("earSourceExcludes").getValue());
            assertEquals("false", configuration.getChild("generateApplicationXml").getValue());
            
            assertEquals(3, configuration.getChild("modules").getChildCount());
        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    @Test
    public void testCreateModulePom() {
        try {
            File pom = new File(project.getPom().getPath() + POM);
            
            PomUtil pomUtil = new MainPomUtil(project);
            pomUtil.createPom(pom, true);
            
            assertTrue(pom.isFile());
            
            Model model = getMavenModelFrom(pom);
            assertEquals("4.0.0", model.getModelVersion());
            assertEquals("BSAD2-Full-pom", model.getArtifactId());
            assertEquals("BSAD2-Full-pom", model.getName());
            assertEquals("pom", model.getPackaging());
            
            Parent parent = model.getParent();
            assertEquals("BSAD2-Full", parent.getGroupId());
            assertEquals("BSAD2-Full-pom", parent.getArtifactId());
            assertEquals("1.0-SNAPSHOT", parent.getVersion());

            Build build = model.getBuild();
            assertEquals("install", build.getDefaultGoal());
            
            List<Plugin> plugins = build.getPlugins();
            assertEquals(1, plugins.size());
            assertEquals("org.apache.maven.plugins", plugins.get(0).getGroupId());
            assertEquals("maven-compiler-plugin", plugins.get(0).getArtifactId());
            
            Xpp3Dom configuration = (Xpp3Dom) plugins.get(0).getConfiguration();
            assertEquals("${project.build.sourceEncoding}", configuration.getChild("encoding").getValue());
            assertEquals("${maven.compiler.target}", configuration.getChild("target").getValue());
            assertEquals("${maven.compiler.source}", configuration.getChild("source").getValue());
            assertEquals("true", configuration.getChild("debug").getValue());
            assertEquals("lines,vars,source", configuration.getChild("debugLevel").getValue());
            assertEquals("true", configuration.getChild("verbose").getValue());
            assertEquals("true", configuration.getChild("showWarnings").getValue());
            assertEquals("true", configuration.getChild("showDeprecation").getValue());
            assertEquals(1, configuration.getChild("compilerArguments").getChildCount());
        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    private Model getMavenModelFrom(File pom) throws IOException, XmlPullParserException {
        FileReader reader = new FileReader(pom);
        MavenXpp3Reader mavenreader = new MavenXpp3Reader();
        return mavenreader.read(reader);
    }
}
