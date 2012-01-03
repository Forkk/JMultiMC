package forkk.multimc.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import forkk.multimc.data.Instance;

public class EditNotesDialog extends JDialog
{
	private static final long serialVersionUID = -7025215118975074408L;
	
	private final JPanel contentPanel = new JPanel();
	
	Instance instance;
	private JTextArea notesArea;
	
	/**
	 * Create the dialog.
	 */
	public EditNotesDialog(Instance inst)
	{
		setType(Type.POPUP);
		this.instance = inst;
		setModal(true);
		setTitle("Edit Notes");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			notesArea = new JTextArea(inst.getNotes());
			notesArea.setLineWrap(true);
			contentPanel.add(notesArea);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setMnemonic('o');
				okButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						System.out.println("Setting notes");
						instance.setNotes(notesArea.getText());
						setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setMnemonic('c');
				cancelButton.setMnemonic(KeyEvent.VK_ESCAPE);
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
