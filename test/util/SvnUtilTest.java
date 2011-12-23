package util;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class SvnUtilTest extends AbstractTest {
    
    private final String CHECKOUT_DIR = TEMP_DIR + "test-app";
    
    @Before
    public void setUp() {
        deleteDir(CHECKOUT_DIR);
    }

    @Test
    public void testCheckoutRepositoryByName() {
        SvnUtil svnUtil = new SvnUtil("app-svn", "viscondedeinhauma@37");
        File repository = new File(CHECKOUT_DIR);

        assertTrue(svnUtil.checkout("http://apps.wdev.com.br/svn/wdev/ips/scripts", repository));
    }
    
    @Test
    public void testCheckoutUsingInvalidCredentials() {
        SvnUtil svnUtil = new SvnUtil("xxxx", "11111");
        File repository = new File(CHECKOUT_DIR);

        assertFalse(svnUtil.checkout("http://apps.wdev.com.br/svn/wdev/ips/scripts", repository));
    }
    
    
}
