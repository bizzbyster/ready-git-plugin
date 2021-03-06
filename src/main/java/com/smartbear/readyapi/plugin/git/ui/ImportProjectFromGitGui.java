package com.smartbear.readyapi.plugin.git.ui;

import com.eviware.soapui.plugins.vcs.ImportProjectFromVcsGui;
import com.eviware.soapui.plugins.vcs.VcsIntegrationException;
import com.eviware.soapui.plugins.vcs.VcsRepositoryInfo;
import com.smartbear.readyapi.plugin.git.GitCommandHelper;

import java.awt.Component;
import java.io.File;

import static com.smartbear.readyapi.plugin.git.ui.help.HelpUrls.GIT_PLUGIN_WIKI;

public class ImportProjectFromGitGui extends AbstractRepositorySelectionGui implements ImportProjectFromVcsGui {
    private GitCommandHelper gitCommandHelper;

    public ImportProjectFromGitGui( ) {
        this.gitCommandHelper = new GitCommandHelper();
    }

    @Override
    public Component getComponent() {
        return createGui(GIT_PLUGIN_WIKI, "Learn about importing projects using Git", null);
    }

    @Override
    public VcsRepositoryInfo downloadProjectFiles(File emptyDirectory) {
        try {
            gitCommandHelper.cloneRepository(getSelected().getRepositoryPath(), getSelected().getCredentialsProvider(), emptyDirectory);
            return new VcsRepositoryInfo("Git", getSelected().getRepositoryPath());
        } catch (Exception e) {
            throw new VcsIntegrationException("Failed to clone remote repository", e);
        }
    }

    @Override
    public boolean isValidInput() {
        return getSelected().isValid();
    }
}
