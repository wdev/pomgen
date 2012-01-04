package util;

import java.io.File;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.auth.SVNAuthentication;
import org.tmatesoft.svn.core.auth.SVNPasswordAuthentication;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

public class SvnUtil {
    
    private String username;
    private String password;
    
    public SvnUtil(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void checkout(String url, File folder) throws SVNException {
        SVNURL svnUrl = SVNURL.parseURIDecoded(url);
        SVNRepository repository = getRepository(svnUrl, getAuthManager(username, password));
        SVNClientManager ourClientManager = SVNClientManager.newInstance(null, repository.getAuthenticationManager());
    
        SVNUpdateClient updateClient = ourClientManager.getUpdateClient();
        updateClient.setIgnoreExternals(false);
        updateClient.doCheckout(svnUrl, folder, SVNRevision.HEAD, SVNRevision.HEAD, true);
    }
    
    private ISVNAuthenticationManager getAuthManager(String username, String password) {
        DAVRepositoryFactory.setup();
        ISVNAuthenticationManager authManager = new BasicAuthenticationManager(username, password);
        return authManager;
    }
    
    private SVNRepository getRepository(SVNURL svnUrl, ISVNAuthenticationManager authManager) throws SVNException {
        SVNRepository repository = SVNRepositoryFactory.create(svnUrl, null);
        repository.setAuthenticationManager(authManager);
            
        return repository;
    }

    
}
