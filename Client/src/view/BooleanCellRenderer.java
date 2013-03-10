package view;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class BooleanCellRenderer extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		boolean b = (Boolean) value;
		if(column == 2)
		{
			if(b)
				setText(Main.texts.getString("yes"));
			else
				setText(Main.texts.getString("no"));
		}else
		{
			if(b)
				setText(Main.texts.getString("ongoing"));
			else
				setText(Main.texts.getString("waiting"));
		}
			

		return this;
	}
}