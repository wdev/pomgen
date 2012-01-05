package models;

import java.io.File;
import java.util.List;

import org.junit.Test;

import play.Play;
import play.test.UnitTest;

public class ProjectTest extends UnitTest {

    @Test
    public void testGetModules() {
        File dir = Play.getFile("test/resources/BSAD2-Full");
        Project project = new Project(dir);
        List<String> modules = project.getModules();
        
        assertTrue(modules.contains("BSAD2-Full"));
        assertTrue(modules.contains("BSAD2-Full-ejb"));
        assertTrue(modules.contains("BSAD2-Full-ejbClient"));
        assertTrue(modules.contains("BSAD2-Full-pom"));
        assertTrue(modules.contains("BSAD2-Full-ws"));
        assertTrue(modules.contains("BSAD2-Full-web"));
        assertTrue(modules.contains("BSAD2-Full-util"));
    }
}
