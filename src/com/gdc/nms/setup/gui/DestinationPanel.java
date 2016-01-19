package com.gdc.nms.setup.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.gdc.nms.setup.util.Environment;
import com.gdc.nms.setup.util.Installation;
import com.gdc.nms.setup.util.Language;
import com.gdc.wizard.WizardPanel;

public class DestinationPanel extends WizardPanel {

    private static final long serialVersionUID = -1327211360511952362L;

    private static final String DEFAULT_INSTALLATION_NAME = "NMS";

    private JPanel mainPanel;
    private JLabel textLabel;
    private JPanel destinationFolderPanel;
    private JTextField destinationField;
    private JButton browseButton;
    private JFileChooser fileChooser;

    public DestinationPanel() {
        super(Language.get("destination.panel.title"), Language.get("destination.panel.subtitle"), Language
            .get("destination.panel.information"));
        this.mainPanel = new JPanel();
        this.textLabel = new JLabel(Language.get("destination.panel.content"));
        this.textLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        this.destinationFolderPanel = new JPanel();
        this.destinationField = new JTextField(this.getDefaultInstallationDir());
        this.browseButton = new JButton("Browse...");
        this.fileChooser = new JFileChooser();
        this.fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        this.initComponents();
        this.addListeners();
    }

    public void setDestinationField(String destination) {
        if (destination == null || destination.isEmpty()) {
            this.destinationField.setText(this.getDefaultInstallationDir());
        } else {
            this.destinationField.setText(Paths.get(destination, DEFAULT_INSTALLATION_NAME).toString());
        }
    }

    private String getDefaultInstallationDir() {
        return Environment.getOS().getDefaultInstallationPath().resolve(DEFAULT_INSTALLATION_NAME).toString();
    }

    private void initComponents() {
        this.initPanel();
        this.initMainPanel();
        this.initTextLabel();
        this.initDestinationFolderPanel();
        this.initDestinationField();
        this.initBrowseButton();
    }

    private void initPanel() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
        this.setLayout(gridBagLayout);
    }

    private void initMainPanel() {
        GridBagConstraints gbc_mainPanel = new GridBagConstraints();
        gbc_mainPanel.fill = GridBagConstraints.BOTH;
        gbc_mainPanel.gridx = 0;
        gbc_mainPanel.gridy = 0;
        this.add(this.mainPanel, gbc_mainPanel);
        GridBagLayout gbl_mainPanel = new GridBagLayout();
        gbl_mainPanel.columnWidths = new int[] { 0, 0 };
        gbl_mainPanel.rowHeights = new int[] { 0, 0, 0, 0, 46, 0, 0 };
        gbl_mainPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gbl_mainPanel.rowWeights = new double[] { 0.0, 1.0, 1.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
        this.mainPanel.setLayout(gbl_mainPanel);
        this.mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 22, 0, 22));
    }

    private void initTextLabel() {
        GridBagConstraints gbc_textLabel = new GridBagConstraints();
        gbc_textLabel.fill = GridBagConstraints.BOTH;
        gbc_textLabel.insets = new Insets(0, 0, 5, 0);
        gbc_textLabel.gridx = 0;
        gbc_textLabel.gridy = 0;
        this.mainPanel.add(this.textLabel, gbc_textLabel);
    }

    private void initDestinationFolderPanel() {
        GridBagConstraints gbc_destinationFolderPanel = new GridBagConstraints();
        gbc_destinationFolderPanel.insets = new Insets(0, 0, 5, 0);
        gbc_destinationFolderPanel.fill = GridBagConstraints.BOTH;
        gbc_destinationFolderPanel.gridx = 0;
        gbc_destinationFolderPanel.gridy = 4;
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Destination Folder");
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 12));
        this.destinationFolderPanel.setBorder(titledBorder);
        this.mainPanel.add(this.destinationFolderPanel, gbc_destinationFolderPanel);
        GridBagLayout gbl_destinationFolderPanel = new GridBagLayout();
        gbl_destinationFolderPanel.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        gbl_destinationFolderPanel.rowHeights = new int[] { 0, 0 };
        gbl_destinationFolderPanel.columnWeights = new double[] { 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0,
                Double.MIN_VALUE };
        gbl_destinationFolderPanel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
        this.destinationFolderPanel.setLayout(gbl_destinationFolderPanel);
    }

    private void initDestinationField() {
        GridBagConstraints gbc_destinationField = new GridBagConstraints();
        gbc_destinationField.gridwidth = 6;
        gbc_destinationField.insets = new Insets(0, 0, 0, 5);
        gbc_destinationField.fill = GridBagConstraints.HORIZONTAL;
        gbc_destinationField.gridx = 1;
        gbc_destinationField.gridy = 0;
        this.destinationFolderPanel.add(this.destinationField, gbc_destinationField);
        this.destinationField.setColumns(10);
    }

    private void initBrowseButton() {
        this.browseButton.setFont(new Font("", Font.PLAIN, 11));
        GridBagConstraints gbc_browseButton = new GridBagConstraints();
        gbc_browseButton.insets = new Insets(0, 0, 0, 5);
        gbc_browseButton.gridx = 8;
        gbc_browseButton.gridy = 0;
        this.destinationFolderPanel.add(this.browseButton, gbc_browseButton);
    }

    private void addListeners() {
        this.browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JFileChooser.APPROVE_OPTION == DestinationPanel.this.fileChooser
                    .showSaveDialog(DestinationPanel.this.getWizard().getComponent())) {
                    DestinationPanel.this.setDestinationField(DestinationPanel.this.fileChooser.getSelectedFile()
                        .getAbsolutePath());
                }
            }
        });
    }

    private boolean validateDestination() {
        String dst = this.destinationField.getText();
        if (!dst.isEmpty()) {
            Path destination = Paths.get(dst);
            Path parent = destination.getParent();
            if (Files.exists(parent)) {
                if (Files.isWritable(parent)) {
                    return true;
                } else {
                    this.wizard.showMessageDialog("No es posible escribir en '" + parent.toString() + "'.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                this.wizard.showMessageDialog("La ruta '" + parent.toString() + "' no existe.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }
        this.destinationField.setText(this.getDefaultInstallationDir());
        return true;
    }

    @Override
    public boolean beforeFollowing() {
        boolean continueWizard = this.validateDestination();
        if (continueWizard) {
            Installation.setInstallationPath(Paths.get(this.destinationField.getText()));
        }
        return continueWizard;
    }
}
