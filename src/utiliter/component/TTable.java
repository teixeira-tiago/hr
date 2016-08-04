/**
 * 
 */
package utiliter.component;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 * @author Tiago A. Teixeira {@link tiago.a.teixeira@acm.org}
 * 
 */
@SuppressWarnings("serial")
public class TTable extends JTable {

	public TTable(DefaultTableModel modelo) {
		super(modelo, null, null);
	}

	public TTable() {
		super(null, null, null);
	}

	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row,
			int column) {
		// get the current row
		Component comp = super.prepareRenderer(renderer, row, column);
		// even index, not selected
		if (row % 2 == 0 && !isCellSelected(row, column)) {
			comp.setBackground(Color.white);
		} else if (!isCellSelected(row, column)) {
			comp.setBackground(Color.lightGray);
		} else {
			comp.setBackground(Color.blue);
		}
		return comp;
	}
}
