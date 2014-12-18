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
	final TextArea codePanel = new TextArea();

	public AnswerPanel() {

	}

	public AnswerPanel(final MainEntryPoint mainEntryPoint, int width,
			int height) {

		this.mainEntryPoint = mainEntryPoint;

		containingPane = new TabPanel();

		codePanel.setCharacterWidth(40);
		codePanel.setVisibleLines(35);

		containingPane.setPixelSize(width, height);
	}

	public void clear() {
		
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
			pane.setText(snippet.toString());
			containingPane.add(pane,
					"Snippet " + snippetID + " R: " + snippet.getRating());
			snippetID++;
		}
	}
}
