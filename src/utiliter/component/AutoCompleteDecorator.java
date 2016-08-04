package utiliter.component;

import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ComboBoxEditor;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

import org.jdesktop.swingx.autocomplete.AbstractAutoCompleteAdaptor;
import org.jdesktop.swingx.autocomplete.AutoCompleteComboBoxEditor;
import org.jdesktop.swingx.autocomplete.AutoCompleteDocument;
import org.jdesktop.swingx.autocomplete.ComboBoxAdaptor;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;
import org.jdesktop.swingx.autocomplete.workarounds.MacOSXPopupLocationFix;

/**
 * This class contains only static utility methods that can be used to set up
 * automatic completion for some Swing components.
 * <p>
 * Usage examples:
 * </p>
 * <p>
 * 
 * <pre>
 * <code>
 * JComboBox comboBox = [...];
 * AutoCompleteDecorator.<b>decorate</b>(comboBox);
 * 
 * List items = [...];
 * JTextField textField = [...];
 * AutoCompleteDecorator.<b>decorate</b>(textField, items);
 * 
 * JList list = [...];
 * JTextField textField = [...];
 * AutoCompleteDecorator.<b>decorate</b>(list, textField);
 * </code>
 * </pre>
 * 
 * </p>
 * 
 * @author Thomas Bierhance
 */
public class AutoCompleteDecorator {

	/**
	 * Enables automatic completion for the given JComboBox. The automatic
	 * completion will be strict (only items from the combo box can be selected)
	 * if the combo box is not editable.
	 * 
	 * @param comboBox
	 *            a combo box
	 */
	@SuppressWarnings("rawtypes")
	public static void decorate(final JComboBox comboBox) {
		decorate(comboBox, ObjectToStringConverter.DEFAULT_IMPLEMENTATION);
	}

	/**
	 * Enables automatic completion for the given JComboBox. The automatic
	 * completion will be strict (only items from the combo box can be selected)
	 * if the combo box is not editable.
	 * 
	 * @param comboBox
	 *            a combo box
	 * @param stringConverter
	 *            the converter used to transform items to strings
	 */
	@SuppressWarnings("rawtypes")
	public static void decorate(final JComboBox comboBox,
			final ObjectToStringConverter stringConverter) {
		boolean strictMatching = !comboBox.isEditable();
		// has to be editable
		comboBox.setEditable(true);
		// fix the popup location
		MacOSXPopupLocationFix.install(comboBox);

		// configure the text component=editor component
		JTextComponent editorComponent = (JTextComponent) comboBox.getEditor()
				.getEditorComponent();
		final AbstractAutoCompleteAdaptor adaptor = new ComboBoxAdaptor(
				comboBox);
		final AutoCompleteDocument document = new AutoCompleteDocument(adaptor,
				strictMatching, stringConverter);
		decorate(editorComponent, document, adaptor);

		// show the popup list when the user presses a key
		final KeyListener keyListener = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent keyEvent) {
				// don't popup on action keys (cursor movements, etc...)
				if (keyEvent.isActionKey())
					return;
				// don't popup if the combobox isn't visible anyway
				if (comboBox.isDisplayable() && !comboBox.isPopupVisible()) {
					int keyCode = keyEvent.getKeyCode();
					// don't popup when the user hits shift,ctrl or alt
					if (keyCode == KeyEvent.VK_SHIFT
							|| keyCode == KeyEvent.VK_CONTROL
							|| keyCode == KeyEvent.VK_ALT)
						return;
					// don't popup when the user hits escape (see issue #311)
					if (keyCode == KeyEvent.VK_ESCAPE
							|| keyCode == KeyEvent.VK_ENTER)
						return;

					if (keyEvent.isAltDown() || keyEvent.isControlDown())
						return;

					comboBox.setPopupVisible(true);
				}
			}
		};
		editorComponent.addKeyListener(keyListener);

		if (stringConverter != ObjectToStringConverter.DEFAULT_IMPLEMENTATION) {
			comboBox.setEditor(new AutoCompleteComboBoxEditor(comboBox
					.getEditor(), stringConverter));
		}

		// Changing the l&f can change the combobox' editor which in turn
		// would not be autocompletion-enabled. The new editor needs to be
		// set-up.
		comboBox.addPropertyChangeListener("editor",
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent e) {
						ComboBoxEditor editor = (ComboBoxEditor) e
								.getNewValue();
						if (editor != null
								&& editor.getEditorComponent() != null) {
							if (!(editor instanceof AutoCompleteComboBoxEditor)
									&& stringConverter != ObjectToStringConverter.DEFAULT_IMPLEMENTATION) {
								comboBox.setEditor(new AutoCompleteComboBoxEditor(
										editor, stringConverter));
								// Don't do the decorate step here because
								// calling setEditor will trigger
								// the propertychange listener a second time,
								// which will do the decorate
								// and addKeyListener step.
							} else {
								decorate((JTextComponent) editor
										.getEditorComponent(), document,
										adaptor);
								editor.getEditorComponent().addKeyListener(
										keyListener);
							}
						}
					}
				});
	}

	/**
	 * Decorates a given text component for automatic completion using the given
	 * AutoCompleteDocument and AbstractAutoCompleteAdaptor.
	 * 
	 * @param textComponent
	 *            a text component that should be decorated
	 * @param document
	 *            the AutoCompleteDocument to be installed on the text component
	 * @param adaptor
	 *            the AbstractAutoCompleteAdaptor to be used
	 */
	public static void decorate(JTextComponent textComponent,
			AutoCompleteDocument document,
			final AbstractAutoCompleteAdaptor adaptor) {
		// install the document on the text component
		textComponent.setDocument(document);

		// mark entire text when the text component gains focus
		// otherwise the last mark would have been retained which is quiet
		// confusing
		textComponent.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				adaptor.markEntireText();
			}
		});

		// Tweak some key bindings
		InputMap editorInputMap = textComponent.getInputMap();
		if (document.isStrictMatching()) {
			// move the selection to the left on VK_BACK_SPACE
			editorInputMap.put(KeyStroke.getKeyStroke(
					java.awt.event.KeyEvent.VK_BACK_SPACE, 0),
					DefaultEditorKit.selectionBackwardAction);
			// ignore VK_DELETE and CTRL+VK_X and beep instead when strict
			// matching
			editorInputMap.put(KeyStroke.getKeyStroke(
					java.awt.event.KeyEvent.VK_DELETE, 0), errorFeedbackAction);
			editorInputMap.put(KeyStroke.getKeyStroke(
					java.awt.event.KeyEvent.VK_X,
					java.awt.event.InputEvent.CTRL_DOWN_MASK),
					errorFeedbackAction);
		} else {
			ActionMap editorActionMap = textComponent.getActionMap();
			// leave VK_DELETE and CTRL+VK_X as is
			// VK_BACKSPACE will move the selection to the left if the selected
			// item is in the list
			// it will delete the previous character otherwise
			editorInputMap.put(KeyStroke.getKeyStroke(
					java.awt.event.KeyEvent.VK_BACK_SPACE, 0),
					"nonstrict-backspace");
			editorActionMap
					.put("nonstrict-backspace",
							new NonStrictBackspaceAction(
									editorActionMap
											.get(DefaultEditorKit.deletePrevCharAction),
									editorActionMap
											.get(DefaultEditorKit.selectionBackwardAction),
									adaptor));
		}
	}

	@SuppressWarnings("serial")
	static class NonStrictBackspaceAction extends TextAction {
		Action backspace;
		Action selectionBackward;
		AbstractAutoCompleteAdaptor adaptor;

		public NonStrictBackspaceAction(Action backspace,
				Action selectionBackward, AbstractAutoCompleteAdaptor adaptor) {
			super("nonstrict-backspace");
			this.backspace = backspace;
			this.selectionBackward = selectionBackward;
			this.adaptor = adaptor;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (adaptor.listContainsSelectedItem()) {
				selectionBackward.actionPerformed(e);
			} else {
				backspace.actionPerformed(e);
			}
		}
	}

	/**
	 * A TextAction that provides an error feedback for the text component that
	 * invoked the action. The error feedback is most likely a "beep".
	 */
	@SuppressWarnings("serial")
	static Object errorFeedbackAction = new TextAction("provide-error-feedback") {
		@Override
		public void actionPerformed(ActionEvent e) {
			UIManager.getLookAndFeel()
					.provideErrorFeedback(getTextComponent(e));
		}
	};
}