package forkk.multimc.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import forkk.multimc.data.Instance;
import forkk.multimc.instIcons.InstIconList;

public class ChangeIconDialog extends JDialog
{
	private static final long serialVersionUID = 2986256150660257570L;
	
	private final JPanel contentPanel = new JPanel();
	
	Instance instance;
	private JList iconListView;
	DefaultListModel iconList;
	private JButton okButton;
	
	/**
	 * Create the dialog.
	 */
	public ChangeIconDialog(Instance inst)
	{
		instance = inst;
		setBounds(100, 100, 620, 400);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			iconListView = new JList();
			iconListView.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					if (e.getClickCount() >= 2)
						okButton.doClick();
				}
			});
			iconListView.setVisibleRowCount(0);
			iconListView.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			iconListView.setCellRenderer(new IconListRenderer());
			iconListView.setModel(iconList = new DefaultListModel());
			contentPanel.add(iconListView);
			
			for (String key : InstIconList.getList().keySet())
			{
				iconList.addElement(key);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						instance.setIconKey(iconListView.getSelectedValue().toString());
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
	}
	
}
