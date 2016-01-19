package com.gdc.nms.setup.gui;

import com.gdc.nms.setup.util.Language;
import com.gdc.wizard.Button;
import com.gdc.wizard.WizardPanel;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import java.awt.GridBagLayout;

import javax.swing.JTextArea;

import java.awt.GridBagConstraints;

import javax.swing.JLabel;

import java.awt.Insets;
import java.awt.Font;

public class LicensePanel extends WizardPanel {

	private static final long serialVersionUID = 8012612945003914888L;

	public LicensePanel() {
		super(Language.get("license.panel.title"), Language.get("license.panel.subtitle"), Language.get("licence.panel.information"));
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0};
		gbl_panel.rowHeights = new int[]{23, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);

		panel.setBorder(BorderFactory.createEmptyBorder(15, 22, 0, 22));
		
		JLabel lblNewLabel = new JLabel(Language.get("license.panel.content"));
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		panel.add(lblNewLabel, gbc_lblNewLabel);
		
		JTextArea txtrGeneralDatacommDe = new JTextArea();
		txtrGeneralDatacommDe.setFont(new Font("Arial", Font.PLAIN, 11));
		txtrGeneralDatacommDe.setText(Language.get("license.panel.license"));
		GridBagConstraints gbc_txtrGeneralDatacommDe = new GridBagConstraints();
		gbc_txtrGeneralDatacommDe.insets = new Insets(0, 0, 5, 0);
		gbc_txtrGeneralDatacommDe.fill = GridBagConstraints.BOTH;
		gbc_txtrGeneralDatacommDe.gridx = 0;
		gbc_txtrGeneralDatacommDe.gridy = 1;
		panel.add(txtrGeneralDatacommDe, gbc_txtrGeneralDatacommDe);
		txtrGeneralDatacommDe.setEditable(false);
		txtrGeneralDatacommDe.setLineWrap(true);
		txtrGeneralDatacommDe.setBorder(BorderFactory.createLineBorder(new Color(127, 157, 185)));
	}

	@Override
	public void beforeShowing() {
		getWizard().setButtonText(Button.Next, "I Agree");
	}

}
