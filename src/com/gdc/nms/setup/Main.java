package com.gdc.nms.setup;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.gdc.nms.setup.util.Installation;
import com.gdc.nms.setup.util.Language;

public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    static {

        setSystemLookAndFeel();

        try {
            Language.load();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "An exception occurred while trying to read the language.", e);
            JOptionPane.showMessageDialog(null, "Unexpected error while setup try to start.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        try {
            Installation.load();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "An exception occurred while trying to read the registry.", e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        try {
            Installer.getInstance().init();
        } catch (Exception e) {
            e.printStackTrace();
            LOG.log(Level.SEVERE, "An exception occurred while trying to read NMS installation files.", e);
            JOptionPane.showMessageDialog(null, "An exception occurred while trying to read NMS installation files.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

	private static void setSystemLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			LOG.log(Level.INFO, "Unable to set the default look and feel of the system.", e);
		}
	}

	private static String getLocalClientChangeset(String client) throws IOException {
	    Path server = Installation.getInstallaPath().resolve(client).resolve("server.jar");
	    JarFile file = new JarFile(server.toFile());
	    ZipEntry entry = file.getEntry("META-INF/server.properties");
	    Properties properties = new Properties();
	    properties.load(file.getInputStream(entry));
	    String changeset = properties.getProperty("changeset.tag");
	    file.close();
	    return changeset;
	}

	private static void startInstallation() {
        if (Installer.getInstance().getLibVersion() <= Installation.getLibVersion()) {
            List<String> list = new ArrayList<String>(Installer.getInstance().getFilesList());
            for (String file : list) {
                if (file.contains("lib")) {
                    Installer.getInstance().getFilesList().remove(file);
                }
            }
        }
        Installer.getInstance().start();
	}

	public static void main(String[] args) {
		if (Installation.isInstalled()) {
		    if (Installation.isClientInstalled(Installer.getInstance().getClientHost())) {
		        try {
                    String localChangeset = getLocalClientChangeset(Installer.getInstance().getClientHost());
                    if (Installer.getInstance().getChangeSet().equals(localChangeset)) {
                        JOptionPane.showMessageDialog(null, "Esta version ya se encuentra instalada.", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        startInstallation();
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Se presento un error al intentar obtener el changeset del cliente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
		    } else {
		        startInstallation();
		    }
		} else {
		    Installer.getInstance().start();
		}
	}

}
