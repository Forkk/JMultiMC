package forkk.multimc.gui;

import java.awt.Component;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class DateCellRenderer extends DefaultTableCellRenderer
{
	private static final long serialVersionUID = 1745589859390913887L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
	{
		Component component = super.getTableCellRendererComponent(table, value, 
				isSelected, hasFocus, row, column);
		
		if (value instanceof Long)
		{
			Long millis = (Long) value;
			Date installDate = new Date(millis);
			
			if (component instanceof JLabel)
			{
				((JLabel) component).setText(installDate.toString());
			}
		}
		
		return component;
	}
}
