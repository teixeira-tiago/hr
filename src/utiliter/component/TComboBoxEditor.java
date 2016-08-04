/**
 * 
 */
package utiliter.component;

import javax.swing.DefaultCellEditor;

/**
 * @author Tiago A. Teixeira {@link tiago.a.teixeira@acm.org}
 *
 */
@SuppressWarnings("serial")
public class TComboBoxEditor extends DefaultCellEditor {
	public TComboBoxEditor(String[] items) {
		super(new AComboBox(items));
	}
}