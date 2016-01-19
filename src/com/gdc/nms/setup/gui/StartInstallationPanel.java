package com.gdc.nms.setup.gui;
import com.gdc.nms.setup.util.Language;
import com.gdc.wizard.Button;

public class StartInstallationPanel extends WizardTextPanel {

	private static final long serialVersionUID = 8143072879323572963L;

	public StartInstallationPanel() {
		super(Language.get("startinstallation.panel.title"), Language.get("startinstallation.panel.subtitle"), Language.get("startinstallation.panel.information"));
		setContent(Language.get("startinstallation.panel.content"));
	}

	@Override
	public void beforeShowing() {
		getWizard().setButtonText(Button.Next, "Install");
	}
}
