package com.gdc.nms.setup.gui;

import com.gdc.wizard.WizardPanel;
import java.awt.Color;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import java.awt.GridBagConstraints;
import javax.swing.JTextArea;

import java.awt.Font;

public class WizardTextPanel extends WizardPanel {

	private static final long serialVersionUID = 2906221217462005791L;

	private JTextArea contentText;
	private JScrollPane scrollPane;

	public WizardTextPanel(String title, String subtitle, String information) {
		super(title, subtitle, information);
		contentText = new JTextArea();
		scrollPane = new JScrollPane();
		initComponents();
	}

	private void initComponents() {
		initPanel();
		initScrollPane();
		initContentText();
	}

	private void initPanel() {
	    setBorder(null);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
	}

	private void initScrollPane() {
	    scrollPane.setBorder(null);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		add(scrollPane, gbc_scrollPane);
	}

	private void initContentText() {
		contentText.setFont(new Font("Arial", Font.PLAIN, 11));
		scrollPane.setViewportView(contentText);
		contentText.setBackground(new Color(0.0f,0.0f,0.0f,0.0f));
		contentText.setBorder(BorderFactory.createEmptyBorder(21, 25, 21, 25));
//		contentText.setOpaque(false);
		contentText.setEditable(false);
		contentText.setLineWrap(true);
	}

	public void setContent(String content) {
		contentText.setText(content);
	}

	public String getContent() {
		return contentText.getText();
	}
}
