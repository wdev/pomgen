package util;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import play.Play;

public class ProjectUtilTest extends AbstractTest {

    private BsadProject project;
    
    @Before
    public void setUp() {
        File projectDir = Play.getFile("test/resources/BSAD2-Full");
        project = new BsadProject(projectDir);
    }
    
    @Test
    public void testCheckModulesExists() {
        assertTrue(ProjectUtil.hasModule(project, "ear"));
        assertTrue(ProjectUtil.hasModule(project, "ejb"));
        assertTrue(ProjectUtil.hasModule(project, "ejbClient"));
        assertTrue(ProjectUtil.hasModule(project, "web"));
        assertTrue(ProjectUtil.hasModule(project, "util"));
        assertTrue(ProjectUtil.hasModule(project, "ws"));
    }
    
        
}
