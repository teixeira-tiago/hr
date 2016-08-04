/**
 * 
 */
package utiliter.component;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import utiliter.Util;

/**
 * @author Tiago A. Teixeira {@link tiago.a.teixeira@acm.org}
 *
 */
@SuppressWarnings({ "serial" })
public class TComboBoxRenderer extends AComboBox implements TableCellRenderer {
	public TComboBoxRenderer(String[] items) {
		super(items);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			super.setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}
		if (util.isNumeric(String.valueOf(value))) {
			try {
				setSelectedIndex(Integer.parseInt(String.valueOf(value)) - 1);
			} catch (Exception e) {
				setSelectedIndex(-1);
			}
		} else {
			setSelectedItem(value);
		}
		setSize(table.getColumnModel().getColumn(column).getWidth(),
				getPreferredSize().height);
		if (table.getRowHeight(row) != ((int) (getPreferredSize().height * 0.8))) {
			table.setRowHeight(row, ((int) (getPreferredSize().height * 0.8)));
		}
		return this;
	}

	private final Util util = new Util();
}
