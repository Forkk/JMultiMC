package forkk.multimc.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import forkk.multimc.data.Instance;

public class EditModsDialog extends JDialog
{
	private static final long serialVersionUID = 3322383503698078689L;

	private final JPanel contentPanel = new JPanel();
	
	private Instance instance;
	
	private DefaultTableModel modFileList;
	private JTable modFileTable;
	
	/**
	 * Create the dialog.
	 */
	public EditModsDialog(Instance inst)
	{
		instance = inst;
		
		setTitle("Select mod files to remove");
		setBounds(100, 100, 700, 450);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane);
			{
				modFileList = new DefaultTableModel(
						new Object[][] {},
						new String[] {"Filename", "Install time", "Delete"})
				{
					private static final long serialVersionUID = 7159868994511577836L;
					
					Class<?>[] columnTypes = new Class[]
							{ String.class, Object.class, Boolean.class };
					
					public Class<?> getColumnClass(int columnIndex)
					{
						return columnTypes[columnIndex];
					}
					
					@Override
					public boolean isCellEditable(int row, int column)
					{
						return column == 2;
					}
				};
				
				modFileTable = new JTable();
				modFileTable.setFillsViewportHeight(true);
				modFileTable.setModel(modFileList);
				modFileTable.getColumnModel().getColumn(0).setPreferredWidth(350);
				modFileTable.getColumnModel().getColumn(1).setPreferredWidth(175);
				modFileTable.getColumnModel().getColumn(1).setCellRenderer(new DateCellRenderer());
				modFileTable.getColumnModel().getColumn(2).setResizable(false);
				modFileTable.setShowHorizontalLines(true);
				modFileTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				scrollPane.setViewportView(modFileTable);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						for (int i = 0; i < modFileList.getRowCount(); i++)
						{
							if (modFileList.getValueAt(i, 2) != null &&
									modFileList.getValueAt(i, 2).equals(true))
							{
								File selFile = new File(
										modFileList.getValueAt(i, 0).toString());
								selFile.delete();
							}
						}
						
						setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		for (File f : instance.getInstMods().listFiles())
		{
			recursiveAddToList(f);
		}
	}

	private void recursiveAddToList(File file)
	{
		if (file.isDirectory())
		{
			for (File f : file.listFiles())
			{
				recursiveAddToList(f);
			}
		}
		else if (file.isFile())
		{
			modFileList.addRow(new Object[] { file, instance.getInstallTime(file), false });
		}
	}
}
