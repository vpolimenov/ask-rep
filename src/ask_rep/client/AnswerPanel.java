package ask_rep.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import ask_rep.shared.Snippet;

import com.google.gwt.event.dom.client.ProgressEvent;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;

@SuppressWarnings("serial")
public final class AnswerPanel extends TextArea implements Serializable{

	MainEntryPoint mainEntryPoint;
	TabPanel containingPane;
	final TextArea codePanel = new TextArea();;

	// ProgressEvent progressBar;
	// int progressBarIndex = -1;

	public AnswerPanel() {

	}

	public AnswerPanel(final MainEntryPoint mainEntryPoint, int width,
			int height) {

		this.mainEntryPoint = mainEntryPoint;
		// setLayout(new BorderLayout());

		containingPane = new TabPanel();

		codePanel.setCharacterWidth(40);
		codePanel.setVisibleLines(35);

		containingPane.setPixelSize(width, height);
		// containingPane.setPreferredSize(new Dimension(width, height));
		// add(containingPane, BorderLayout.CENTER);
		// containingPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

	}

	// protected JComponent generatePan(String panelName) {
	// JPanel panel = new JPanel(new FlowLayout(), false);
	// JLabel panelHeader = new JLabel(panelName);
	// panelHeader.setHorizontalAlignment(JLabel.CENTER);
	// panel.setLayout(new GridLayout());
	// panel.add(panelHeader);
	// return panel;
	// }

	public void clear() {
		// containingPane.removeAll();
	}

	public void addAnswers(ArrayList<Snippet> snippets) {
		int snippetID = 1;
		Collections.sort(snippets);

		for (Snippet snippet : snippets) {

			if (containingPane.getWidgetCount() >= 20)
				return;

			if (snippet.length() < 100)
				continue;

			TextArea pane = new TextArea();
			// pane.setFont(new Font("Courier New", Font.PLAIN, 12));
			pane.setText(snippet.toString());
			containingPane.add(pane,
					"Snippet " + snippetID + " R: " + snippet.getRating());
			snippetID++;
		}
	}

	// public void printError(String errorText) {
	// JTextPane pane = new JTextPane();
	// pane.setText(errorText);
	// containingPane.addTab("Link Error", pane);
	//
	// }
}
