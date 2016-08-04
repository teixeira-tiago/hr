/**
 * 
 */
package utiliter.component;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author Tiago A. Teixeira {@link tiago.a.teixeira@acm.org}
 *
 */
@SuppressWarnings("serial")
public class TPButton extends JPanel {

	/**
	 * Create the panel.
	 */
	public TPButton(String text) {
		init(text);
	}

	public TPButton() {
		init("");
	}

	private void init(String text) {
		setLayout(null);
		setBounds(0, 0, 24, 24);
		btn = new JButton();
		btn.setText(text);
		btn.setSize(20, 20);
		btn.setLocation(2, 2);
		add(btn);
	}

	public void setText(String text) {
		btn.setText(text);
	}

	public String getText() {
		return btn.getText();
	}

	public void addActionListener(ActionListener act) {
		btn.addActionListener(act);
	}

	public void setIcon(ImageIcon img) {
		btn.setIcon(img);
	}

	@Override
	public void setEnabled(boolean b) {
		btn.setEnabled(b);
	}

	public JButton btn;

}
