package com.gdc.nms.setup.gui;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import com.gdc.nms.setup.Installer;
import com.gdc.nms.setup.util.Language;
import com.gdc.wizard.Button;
import com.gdc.wizard.WizardPanel;

public class FinishPanel extends WizardPanel {

    private static final long serialVersionUID = 3432187341589150128L;
    private JLabel contentLabel;
    private JButton detailsButton;
    private JScrollPane scrollPane;
    private JPanel contentPanel;
    private JTextPane textPane;

    public FinishPanel() {
        super(Language.get("finish.panel.title"), Language.get("finish.panel.success.subtitle"), Language.get("finish.panel.information"), Button.Finish);
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
        setLayout(gridBagLayout);

        final JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.fill = GridBagConstraints.BOTH;
        gbc_panel.gridx = 0;
        gbc_panel.gridy = 0;
        add(panel, gbc_panel);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 0, 0 };
        gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
        gbl_panel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
        panel.setLayout(gbl_panel);

        contentLabel = new JLabel(Language.get("finish.panel.content.success"));
        contentLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        GridBagConstraints gbc_contentLabel = new GridBagConstraints();
        gbc_contentLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_contentLabel.insets = new Insets(0, 0, 5, 0);
        gbc_contentLabel.gridx = 0;
        gbc_contentLabel.gridy = 1;
        panel.add(contentLabel, gbc_contentLabel);

        detailsButton = new JButton("Details");
        detailsButton.setVisible(false);
        GridBagConstraints gbc_detailsButton = new GridBagConstraints();
        gbc_detailsButton.anchor = GridBagConstraints.WEST;
        gbc_detailsButton.insets = new Insets(0, 0, 5, 0);
        gbc_detailsButton.gridx = 0;
        gbc_detailsButton.gridy = 2;
        panel.add(detailsButton, gbc_detailsButton);
        detailsButton.setFont(new Font("Arial", Font.PLAIN, 11));

        contentPanel = new JPanel();
        GridBagConstraints gbc_contentPanel = new GridBagConstraints();
        gbc_contentPanel.insets = new Insets(0, 0, 5, 0);
        gbc_contentPanel.fill = GridBagConstraints.BOTH;
        gbc_contentPanel.gridx = 0;
        gbc_contentPanel.gridy = 3;
        panel.add(contentPanel, gbc_contentPanel);
        contentPanel.setLayout(new CardLayout(0, 0));

        scrollPane = new JScrollPane();
        contentPanel.add(scrollPane, "name_3300380768373655");

        textPane = new JTextPane();
        scrollPane.setViewportView(textPane);

        contentPanel.setVisible(false);

        detailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (contentPanel.isVisible()) {
                    contentPanel.setVisible(false);
                } else {
                    contentPanel.setVisible(true);
                }
            }
        });
    }

    public void setContent(String content) {
        contentLabel.setText(content);
    }

    @Override
    public void beforeShowing() {
        textPane.setText("");
        if (Installer.getInstance().getErrorMessage().isEmpty()) {
            detailsButton.setVisible(false);
        } else {
            detailsButton.setVisible(true);
            StringBuffer buf = new StringBuffer();
            for (String text : Installer.getInstance().getErrorMessage()) {
                buf.append(text).append(System.lineSeparator());
            }
            textPane.setText(buf.toString());
        }
    }
}