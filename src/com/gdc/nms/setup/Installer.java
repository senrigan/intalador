package com.gdc.nms.setup;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JOptionPane;

import com.gdc.nms.setup.gui.DestinationPanel;
import com.gdc.nms.setup.gui.FinishPanel;
import com.gdc.nms.setup.gui.InstallingPanel;
import com.gdc.nms.setup.gui.LicensePanel;
import com.gdc.nms.setup.gui.StartInstallationPanel;
import com.gdc.nms.setup.gui.WizardTextPanel;
import com.gdc.nms.setup.util.Environment;
import com.gdc.nms.setup.util.Language;
import com.gdc.wizard.Button;
import com.gdc.wizard.Wizard;

public class Installer extends Thread {

    private static final Installer INSTANCE = new Installer();

    private List<String> errorMessages;
    private List<String> foldersList;
    private List<String> filesList;

    private Wizard wizard;
    private WizardTextPanel welcomePanel;
    private LicensePanel licensePanel;
    private DestinationPanel destinationPanel;
    private StartInstallationPanel startInstallationPanel;
    private InstallingPanel installingPanel;
    private FinishPanel finishPanel;

    private String clientHost;
    private String buildVersion;
    private String changeSet;
    private long libVersion;

    private Installer() {
        wizard = new Wizard(Language.getTitle());
        errorMessages = new ArrayList<String>();
        foldersList = new ArrayList<String>();
        filesList = new ArrayList<String>();
        welcomePanel = new WizardTextPanel(Language.get("welcome.panel.title"),Language.get("welcome.panel.subtitle"), Language.get("welcome.panel.information"));
        welcomePanel.setContent(Language.get("welcome.panel.content"));
        welcomePanel.setButtons(Button.Next, Button.Cancel);
        licensePanel = new LicensePanel();
        destinationPanel = new DestinationPanel();
        startInstallationPanel = new StartInstallationPanel();
        installingPanel = new InstallingPanel();
        finishPanel = new FinishPanel();

    }

    public WizardTextPanel getWelcomePanel() {
        return welcomePanel;
    }

    public LicensePanel getLicensePanel() {
        return licensePanel;
    }

    public DestinationPanel getDestinationPanel() {
        return destinationPanel;
    }

    public StartInstallationPanel getStartInstallationPanel() {
        return startInstallationPanel;
    }

    public InstallingPanel getInstallingPanel() {
        return installingPanel;
    }

    public FinishPanel getFinishPanel() {
        return finishPanel;
    }

    public String getClientHost() {
        return clientHost;
    }

    public String getBuildVersion() {
        return buildVersion;
    }

    public String getChangeSet() {
        return changeSet;
    }

    public long getLibVersion() {
        return libVersion;
    }

    public List<String> getFilesList() {
        return filesList;
    }

    public List<String> getFoldersList() {
        return foldersList;
    }

    public void addMessage(String message) {
        errorMessages.add(message);
    }

    public List<String> getErrorMessage() {
        return errorMessages;
    }

    private void getFilesNames() {
        JarFile file = null;
        try {
            file = new JarFile(Environment.getSelfJar().toFile());
            Enumeration<JarEntry> entries = file.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.startsWith("nms/") && !name.equals("nms/")) {
                    if (entry.isDirectory()) {
                        if (!name.contains("lib") && !name.contains("images")) {
                            clientHost = name.replace("nms/", "").replace("/", "");
                        }
                        foldersList.add(name);
                    } else {
                        filesList.add(name);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (file != null) {
                readServerProperties(file);
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void readServerProperties(JarFile file) {
        JarEntry serverEntry = file.getJarEntry("nms/"+getClientHost()+"/server.jar");
        OutputStream out = null;
        InputStream in = null;
        
        Path tmpFile = null;
        try {
            tmpFile = Files.createTempFile(getClientHost(), "server.jar");
            out = Files.newOutputStream(tmpFile);
            in = file.getInputStream(serverEntry);
            byte[] buf = new byte[4028];
            int read = in.read(buf);
            while (read > -1) {
                out.write(buf, 0, read);
                out.flush();
                read = in.read(buf);
            }
            JarFile server = new JarFile(tmpFile.toFile());
            JarEntry propertiesEntry = server.getJarEntry("META-INF/server.properties");
            Properties properties = new Properties();
            properties.load(server.getInputStream(propertiesEntry));
            buildVersion = properties.getProperty("build.tag");
            changeSet = properties.getProperty("changeset.tag");
            libVersion = Long.parseLong(properties.getProperty("lib.version", "0"));
            properties.clear();
            properties = null;
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (tmpFile != null) {
                try {
                    Files.delete(tmpFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void init() {
        getFilesNames();
    }

    @Override
    public void run() {
        if ( getClientHost() == null || getClientHost().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Host of client was not found.", "Error",
                JOptionPane.ERROR_MESSAGE);
        } else if (getBuildVersion() == null || getBuildVersion().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Build Version was not found.", "Error",
                JOptionPane.ERROR_MESSAGE);
        } else if (getChangeSet() == null || getChangeSet().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Changeset was not found.", "Error",
                JOptionPane.ERROR_MESSAGE);
        }  else if (getLibVersion() <= 0) {
            JOptionPane.showMessageDialog(null, "Invalid lib version.", "Error",
                JOptionPane.ERROR_MESSAGE);
        } else {
            wizard.addPanel(getWelcomePanel());
            wizard.addPanel(getLicensePanel());
            wizard.addPanel(getDestinationPanel());
            wizard.addPanel(getStartInstallationPanel());
            wizard.addPanel(getInstallingPanel());
            wizard.addPanel(getFinishPanel());
            wizard.show();
        }
    }

    public static Installer getInstance() {
        return INSTANCE;
    }

}
