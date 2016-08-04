/**
Copyright 2004 Juan Heyns. All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are
permitted provided that the following conditions are met:

   1. Redistributions of source code must retain the above copyright notice, this list of
      conditions and the following disclaimer.

   2. Redistributions in binary form must reproduce the above copyright notice, this list
      of conditions and the following disclaimer in the documentation and/or other materials
      provided with the distribution.

THIS SOFTWARE IS PROVIDED BY JUAN HEYNS ``AS IS'' AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JUAN HEYNS OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

The views and conclusions contained in the software and documentation are those of the
authors and should not be interpreted as representing official policies, either expressed
or implied, of Juan Heyns.
 */
package utiliter.component;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

import org.jdatepicker.DateModel;
import org.jdatepicker.JDatePanel;
import org.jdatepicker.JDatePicker;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.UtilDateModel;

import utiliter.Util;

/**
 * Created on 25 Mar 2004 Refactored 21 Jun 2004 Refactored 14 May 2009
 * Refactored 16 April 2010 Updated 26 April 2010 Updated 10 August 2012 Updated
 * 10 January 2015
 * 
 * @author Juan Heyns
 * @author JC Oosthuizen
 * @author Yue Huang
 * @param <T>
 */
@SuppressWarnings("serial")
public class TDate extends JPanel implements JDatePicker {

	private Popup popup;
	private final JFormattedTextField formattedTextField;
	private final JButton button;

	private final JDatePanelImpl datePanel;
	private final InternalEventHandler internalEventHandler;

	/**
	 * You are able to set the format of the date being displayed on the label.
	 * Formatting is described at:
	 * 
	 * http://java.sun.com/j2se/1.4.2/docs/api/java/text/SimpleDateFormat.html
	 * 
	 * @param datePanel
	 * @param formatter
	 */
	public TDate() {
		this.datePanel = init();

		// Initialise Variables
		popup = null;
		datePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		internalEventHandler = new InternalEventHandler();

		// Create Layout
		setLayout(null);
		setBounds(0, 0, 120, 28);

		// Create and Add Components
		// Add and Configure TextField
		formattedTextField = new JFormattedTextField();
		try {
			formattedTextField.setFormatterFactory(new DefaultFormatterFactory(
					new MaskFormatter("##/##/####")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		formattedTextField.setBounds(0, 0, 93, 28);
		DateModel<?> model = datePanel.getModel();
		setTextFieldValue(formattedTextField, model.getYear(),
				model.getMonth(), model.getDay(), model.isSelected());
		formattedTextField.setEditable(false);
		add(formattedTextField);

		// Add and Configure Button
		button = new JButton("...");
		button.setBounds(97, 4, 20, 20);
		button.setFocusable(true);
		add(button);

		// Add event listeners
		addHierarchyBoundsListener(internalEventHandler);
		button.addActionListener(internalEventHandler);
		formattedTextField.addPropertyChangeListener("value",
				internalEventHandler);
		datePanel.addActionListener(internalEventHandler);
		datePanel.getModel().addChangeListener(internalEventHandler);
	}

	private JDatePanelImpl init() {
		UtilDateModel modelo = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		return new JDatePanelImpl(modelo, p);
	}

	public void setText(String Date) {
		formattedTextField.setText(Date);
	}

	@Override
	public void addActionListener(ActionListener actionListener) {
		datePanel.addActionListener(actionListener);
	}

	@Override
	public void removeActionListener(ActionListener actionListener) {
		datePanel.removeActionListener(actionListener);
	}

	@Override
	public DateModel<?> getModel() {
		return datePanel.getModel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdatepicker.JDatePicker#setTextEditable(boolean)
	 */
	@Override
	public void setTextEditable(boolean editable) {
		formattedTextField.setEditable(editable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdatepicker.JDatePicker#isTextEditable()
	 */
	@Override
	public boolean isTextEditable() {
		return formattedTextField.isEditable();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdatepicker.JDatePicker#setButtonFocusable(boolean)
	 */
	@Override
	public void setButtonFocusable(boolean focusable) {
		button.setFocusable(focusable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdatepicker.JDatePicker#getButtonFocusable()
	 */
	@Override
	public boolean getButtonFocusable() {
		return button.isFocusable();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdatepicker.JDatePicker#getJDateInstantPanel()
	 */
	public JDatePanel getJDateInstantPanel() {
		return datePanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdatepicker.JDatePicker#getJFormattedTextField()
	 */
	public JFormattedTextField getJFormattedTextField() {
		return formattedTextField;
	}

	public JButton getJButton() {
		return button;
	}

	/**
	 * Called internally to popup the dates.
	 */
	private void showPopup() {
		if (popup == null) {
			PopupFactory fac = new PopupFactory();
			Point xy = getLocationOnScreen();
			datePanel.setVisible(true);
			popup = fac.getPopup(this, datePanel, (int) xy.getX(),
					(int) (xy.getY() + this.getHeight()));
			popup.show();
		}
	}

	/**
	 * Called internally to hide the popup dates.
	 */
	private void hidePopup() {
		if (popup != null) {
			popup.hide();
			popup = null;
		}
	}

	/**
	 * This internal class hides the public event methods from the outside
	 */
	private class InternalEventHandler implements ActionListener,
			HierarchyBoundsListener, ChangeListener, PropertyChangeListener {

		@Override
		public void ancestorMoved(HierarchyEvent arg0) {
			hidePopup();
		}

		@Override
		public void ancestorResized(HierarchyEvent arg0) {
			hidePopup();
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getSource() == button) {
				if (popup == null) {
					showPopup();
				} else {
					hidePopup();
				}
			} else if (arg0.getSource() == datePanel) {
				hidePopup();
			}
		}

		@Override
		public void stateChanged(ChangeEvent arg0) {
			if (arg0.getSource() == datePanel.getModel()) {
				DateModel<?> model = datePanel.getModel();
				setTextFieldValue(formattedTextField, model.getYear(),
						model.getMonth(), model.getDay(), model.isSelected());
			}
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (formattedTextField.isEditable()
					&& formattedTextField.getValue() != null) {
				Calendar value = Calendar.getInstance();
				value.setTime(util.strToDate(formattedTextField.getText()));
				datePanel.getModel().setDate(value.get(Calendar.YEAR),
						value.get(Calendar.MONTH), value.get(Calendar.DATE));
				datePanel.getModel().setSelected(true);
			}
		}

	}

	@Override
	public boolean isDoubleClickAction() {
		return datePanel.isDoubleClickAction();
	}

	@Override
	public boolean isShowYearButtons() {
		return datePanel.isShowYearButtons();
	}

	@Override
	public void setDoubleClickAction(boolean doubleClickAction) {
		datePanel.setDoubleClickAction(doubleClickAction);
	}

	@Override
	public void setShowYearButtons(boolean showYearButtons) {
		datePanel.setShowYearButtons(showYearButtons);
	}

	private void setTextFieldValue(JFormattedTextField textField, int year,
			int month, int day, boolean isSelected) {
		if (!isSelected) {
			textField.setValue(null);
		} else {
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month, day, 0, 0, 0);
			calendar.set(Calendar.MILLISECOND, 0);

			textField.setText(util.dataToStr(calendar.getTime()));
		}
	}

	private final Util util = new Util();;

}
