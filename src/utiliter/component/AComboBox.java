package utiliter.component;

import java.util.Collection;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

@SuppressWarnings({ "rawtypes", "serial" })
public class AComboBox extends JComboBox {
	public AComboBox() {
		super();
		initialize(false);
	}

	@SuppressWarnings("unchecked")
	public AComboBox(ComboBoxModel aModel) {
		super(aModel);
		initialize(false);
	}

	@SuppressWarnings("unchecked")
	public AComboBox(Object... items) {
		super(items);
		initialize(false);
	}

	@SuppressWarnings("unchecked")
	public AComboBox(String[] items) {
		super(items);
		initialize(false);
	}

	@SuppressWarnings("unchecked")
	public AComboBox(Collection<?> items) {
		super(new Vector<Object>(items));
		initialize(false);
	}

	public AComboBox(boolean editable) {
		super();
		initialize(editable);
	}

	@SuppressWarnings("unchecked")
	public AComboBox(ComboBoxModel aModel, boolean editable) {
		super(aModel);
		initialize(editable);
	}

	@SuppressWarnings("unchecked")
	public AComboBox(Collection<?> items, boolean editable) {
		super(new Vector<Object>(items));
		initialize(editable);
	}

	private void initialize(boolean editable) {
		setEditable(editable);
		AutoCompleteDecorator.decorate(this);
	}

	@Override
	public void setEnabled(boolean b) {
		super.setEnabled(b);
		getEditor().getEditorComponent().setBackground(getBackground());
	}
}