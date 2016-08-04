/**
 * 
 */
package utiliter.component;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

/**
 * @author Tiago A. Teixeira {@link tiago.a.teixeira@acm.org}
 *
 */
@SuppressWarnings("serial")
public class TDefaultTableModel extends DefaultTableModel {

	@SuppressWarnings("rawtypes")
	public TDefaultTableModel(Vector data, Vector columnNames) {
		setDataVector(data, columnNames);
	}

	public TDefaultTableModel() {
		super(0, 0);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return String.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
		case 3:
			return String.class;
		}
		return null;
	}
}