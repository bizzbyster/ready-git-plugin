package com.smartbear.readyapi.plugin.git.ui;

import com.eviware.soapui.model.project.Project;
import com.eviware.soapui.plugins.vcs.RepositorySelectionGui;
import com.eviware.soapui.support.UISupport;
import net.miginfocom.swing.MigLayout;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.merge.MergeStrategy;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Component;
import java.awt.Label;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class GitRepositorySelectionGui implements RepositorySelectionGui {

    private JTextField repositoryUrlField;
    private JTextField usernameField;
    private JTextField passwordField;
    private Project project;
    private JTextField commitMessageField;

    public GitRepositorySelectionGui(Project project) {
        this.project = project;
    }

    @Override
    public Component getComponent() {
        JPanel panel = new JPanel(new MigLayout("wrap 2", "8[shrink]8[grow,fill]8", "8[]8"));

        panel.add(new JLabel("Repository URL:"));
        repositoryUrlField = new JTextField();
        repositoryUrlField.getDocument().addDocumentListener(new RepositoryUrlListener());
        panel.add(repositoryUrlField);

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new Label("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new Label("Commit message: "));
        commitMessageField = new JTextField();
        panel.add(commitMessageField);
        return panel;
    }

    @Override
    public void createRemoteRepository() { // TODO: Rename to shareProject or something
        if (isValidInput()){
            try {
                Git git = initRepository();

                git.add().addFilepattern(".").call();
                git.commit().setMessage(commitMessageField.getText()).call();
                git.pull().setStrategy(MergeStrategy.OURS).call();
                git.push().setPushAll().call();

                UISupport.showInfoMessage("Your project has been successfully shared.");
            } catch (MalformedURLException e) {
                UISupport.showErrorMessage("Invalid repository URL: " + repositoryUrlField.getText());
            } catch (GitAPIException | IOException e) {
                UISupport.showErrorMessage("Problem: " + e.getMessage());
            }
        }
    }

    private Git initRepository() throws GitAPIException, IOException {
        Git git = Git.init().setDirectory(new File(project.getPath())).call();

        StoredConfig config = git.getRepository().getConfig();
        config.setString("remote", "origin", "url", repositoryUrlField.getText());
        config.setString("remote", "origin", "fetch", "+refs/heads/*:refs/remotes/origin/*");
        config.save();

        return git;
    }

    @Override
    public String getRemoteRepositoryId() {
        return null;
    }

    @Override
    public boolean isValidInput() {
        return true;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
    }

    private class RepositoryUrlListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent event) {
            validateRepositoryURL(repositoryUrlField.getText());
        }

        @Override
        public void removeUpdate(DocumentEvent event) {
        }

        @Override
        public void changedUpdate(DocumentEvent event) {
        }

        private void validateRepositoryURL(String text) {

        }
    }

}
