package controllers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import models.Project;

import play.mvc.Controller;
import util.SvnUtil;

public class Wizard extends Controller {

    public static void step1() {
        render();
    }
    
    public static void checkout() {
        String login = params.get("svnLogin");
        String password = params.get("svnPassword");
        String repository = params.get("svnRepository");
        
        SvnUtil svnUtil = new SvnUtil(login, password);
        
        String checkoutDirName = getCheckoutDirFrom(repository);
        File checkoutDir = new File(checkoutDirName);

        Map map = new HashMap();
        map.put("repository", repository);
        
        try {
            svnUtil.checkout(repository, checkoutDir);
            
            Project project = new Project(checkoutDir);
            map.put("modules", project.getModules());
            map.put("success", true);
            map.put("message", "ok");
        }
        catch (Exception e) {
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        
        renderJSON(map);
    }

    private static String getCheckoutDirFrom(String repository) {
        String[] splited = repository.split("/");
        String workingCopy = splited[splited.length - 1];
        String tempDir = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator");
        return tempDir + workingCopy;
    }
}
