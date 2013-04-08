package view;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


/**
 * CellRenderer customisé pour afficher la difficulté en fonction de sa valeur numérique
 *
 */
public class IntCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, 
			boolean isSelected, boolean hasFocus, int row, int column) {

		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		int d = (Integer) value;

		switch(d) {
		case 1:
			setText(Main.texts.getString("easy"));
			break;
		case 2:
			setText(Main.texts.getString("medium"));
			break;
		case 3:
			setText(Main.texts.getString("hard"));
			break;
		default:
			setText(Main.texts.getString("difficulty"));
			break;
		}

		return this;
	}
}
