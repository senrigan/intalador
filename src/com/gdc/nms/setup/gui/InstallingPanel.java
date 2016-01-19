package com.gdc.nms.setup.gui;

import com.gdc.nms.setup.Installer;
import com.gdc.nms.setup.util.Environment;
import com.gdc.nms.setup.util.Installation;
import com.gdc.nms.setup.util.Language;
import com.gdc.nms.setup.util.os.OperatingSystem;
import com.gdc.wizard.Button;
import com.gdc.wizard.WizardPanel;

import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.GridBagConstraints;

import javax.swing.JProgressBar;

import java.awt.Insets;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.JLabel;

public class InstallingPanel extends WizardPanel implements Runnable {

	private static final long serialVersionUID = 4837622321338929537L;
	private JPanel panel;
	private JLabel currentProcessLabel;
	private JLabel statusLabel;
	private JProgressBar progressBar;

	public InstallingPanel() {
		super(Language.get("installing.panel.title"), Language.get("installing.panel.subtitle"), Language.get("installing.panel.information"));
		panel = new JPanel();
		statusLabel = new JLabel();
		currentProcessLabel = new JLabel();
		progressBar = new JProgressBar();
		initComponents();
	}

	public void setCurrentFileLabel(String currentFile) {
		currentProcessLabel.setText(currentFile);
	}

	public void setProgress(int progress) {
		if (progress > 100) {
			progressBar.setValue(100);
		} else {
			progressBar.setValue(progress);
		}
	}

	private void initComponents() {
		initPanel();
		initContentPanel();
		initCopyingLabel();
		initCurrentFileLabel();
		initProgressBar();
	}

	private void initPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
	}

	private void initContentPanel() {
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 27, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		panel.setBorder(BorderFactory.createEmptyBorder(25, 22, 25, 22));
	}

	private void initCopyingLabel() {
		GridBagConstraints gbc_statusLabel = new GridBagConstraints();
		gbc_statusLabel.anchor = GridBagConstraints.WEST;
		gbc_statusLabel.insets = new Insets(0, 0, 5, 0);
		gbc_statusLabel.gridx = 0;
		gbc_statusLabel.gridy = 0;
		panel.add(statusLabel, gbc_statusLabel);
	}

	private void initCurrentFileLabel() {
		GridBagConstraints gbc_currentProcessLabel = new GridBagConstraints();
		gbc_currentProcessLabel.anchor = GridBagConstraints.WEST;
		gbc_currentProcessLabel.insets = new Insets(0, 0, 5, 0);
		gbc_currentProcessLabel.gridx = 0;
		gbc_currentProcessLabel.gridy = 1;
		panel.add(currentProcessLabel, gbc_currentProcessLabel);
	}

	private void initProgressBar() {
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar.insets = new Insets(0, 0, 5, 0);
		gbc_progressBar.gridx = 0;
		gbc_progressBar.gridy = 2;
		panel.add(progressBar, gbc_progressBar);
	}

	@Override
	public void beforeShowing() {
		getWizard().setButtonEnabled(Button.Back, false);
		getWizard().setButtonEnabled(Button.Cancel, false);
		getWizard().setButtonEnabled(Button.Next, false);
		getWizard().setButtonText(Button.Next, "Install");
		new Thread(this).start();
	}

	@Override
	public void run() {
	    FinishPanel finishPanel = Installer.getInstance().getFinishPanel();
	    Installation.addClient(Installer.getInstance().getClientHost());
	    Installation.setLibVersion(Installer.getInstance().getLibVersion());
		Path installationPath = Installation.getInstallaPath();
		int filesTotal = Installer.getInstance().getFilesList().size() + Installer.getInstance().getFilesList().size();
		int filesCount = 0;
		float percent = 100.0f / filesTotal;
		int value = Math.round(percent);
		try {
		    statusLabel.setText("Creando directorio:");
			Files.createDirectories(installationPath);
			
            // Creating folders
            for (String folder : Installer.getInstance().getFoldersList()) {
                folder = folder.replace("nms/", "");
                currentProcessLabel.setText(folder);
                Files.createDirectories(installationPath.resolve(folder));
                setProgress(value * (++filesCount));
                System.out.println(value * (++filesCount));
            }

            statusLabel.setText("Copiando archivo:");
            // Uncompress files
            ZipFile zip = new ZipFile(Environment.getSelfJar().toFile());
            List<String> files = Installer.getInstance().getFilesList();
            for (String file : files) {
                String name = file.replace("nms/", "");
                if (name.endsWith(".exe") && !Environment.isWindows()) {
                    continue;
                }
                currentProcessLabel.setText(name);
                ZipEntry entry = zip.getEntry(file);
                Path f = installationPath.resolve(name);
                if (Files.exists(f)) {
                    Files.delete(f);
                }
                Files.createFile(f);
                InputStream in = zip.getInputStream(entry);
                FileOutputStream out = new FileOutputStream(f.toFile());
                byte[] buffer = new byte[4096];
                int read = in.read(buffer);
                while (read != -1) {
                    out.write(buffer, 0, read);
                    read = in.read(buffer);
                }
                out.close();
                in.close();
                setProgress(value * (++filesCount));
                System.out.println(value * (++filesCount));
            }
            zip.close();
            setProgress(100);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    progressBar.setIndeterminate(true);
                    statusLabel.setText("");
                    currentProcessLabel.setText("Finalizando la instalacion");                    
                }
            });
			// Creating links
			try {
			    OperatingSystem os = Environment.getOS();
			    os.createLinks(installationPath, Installer.getInstance().getClientHost());
			} catch (UnsupportedOperationException o) {
			    Installer.getInstance().addMessage("Create shortcuts is unsupported in this system.");
			} catch (Exception e) {
			    Installer.getInstance().addMessage("An error happen while trying to create the shortcuts.");
			}

			Installation.store();
			progressBar.setIndeterminate(false);
			if (Installer.getInstance().getErrorMessage().size() > 0) {
			    finishPanel.setSubtitle(Language.get("finish.panel.fail.subtitle"));
                finishPanel.setContent(Language.get("finish.panel.content.fail"));
			    wizard.showMessageDialog("La instalacion finalizo con algunos errores.", "Finalizo", JOptionPane.WARNING_MESSAGE);
			} else {
			    finishPanel.setSubtitle(Language.get("finish.panel.success.subtitle"));
                finishPanel.setContent(Language.get("finish.panel.content.success"));
			    wizard.showMessageDialog("La instalacion termino exitosamente", "Finalizo", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (Exception e) {
		    e.printStackTrace();
		    finishPanel.setSubtitle(Language.get("finish.panel.several.fail.subtitle"));
            finishPanel.setContent(Language.get("finish.panel.content.several.fail"));
            Installer.getInstance().addMessage("Exception: " + e.getMessage());
			wizard.showMessageDialog("Un error ha ocurrido", "Error", JOptionPane.ERROR_MESSAGE);
		}

		getWizard().setButtonEnabled(Button.Next, true);
		getWizard().setButtonText(Button.Next, "Next >");
	}
}
