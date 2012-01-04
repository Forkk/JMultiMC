package forkk.multimc.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import forkk.jsettings.IntSetting;

public class SettingsDialog extends JDialog
{
	private static final long serialVersionUID = -7764610862804154562L;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JPanel advancedTab;
	private JPanel generalTab;
	private JSpinner initialMemorySpinner;
	private JSpinner maxMemorySpinner;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		try
		{
			SettingsDialog dialog = new SettingsDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the dialog.
	 */
	public SettingsDialog()
	{
		setBounds(100, 100, 400, 450);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			contentPanel.add(tabbedPane, BorderLayout.CENTER);
			{
				generalTab = new JPanel();
				tabbedPane.addTab("General", null, generalTab, "General settings.");
				tabbedPane.setEnabledAt(0, true);
				
				JLabel lblLauncherFilename = new JLabel("Launcher Filename:");
				textField = new JTextField();
				textField.setColumns(10);
				
				JLabel lblConsoleSettings = new JLabel("Console Settings:");
				
				JCheckBox chckbxShowConsole = new JCheckBox("Show console");
				JCheckBox chckbxCloseConsoleWhen = new JCheckBox("Close console when instance exits.");
				JCheckBox chckbxAutomaticallyCheckFor = new JCheckBox("Automatically check for updates when MultiMC starts");
				GroupLayout gl_generalTab = new GroupLayout(generalTab);
				gl_generalTab.setHorizontalGroup(
					gl_generalTab.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_generalTab.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_generalTab.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_generalTab.createSequentialGroup()
									.addComponent(lblLauncherFilename)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(textField, GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE))
								.addGroup(gl_generalTab.createSequentialGroup()
									.addGap(6)
									.addGroup(gl_generalTab.createParallelGroup(Alignment.LEADING)
										.addComponent(chckbxCloseConsoleWhen)
										.addComponent(chckbxShowConsole)))
								.addComponent(chckbxAutomaticallyCheckFor)
								.addComponent(lblConsoleSettings))
							.addContainerGap())
				);
				gl_generalTab.setVerticalGroup(
					gl_generalTab.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_generalTab.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_generalTab.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblLauncherFilename)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addComponent(lblConsoleSettings)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chckbxShowConsole)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chckbxCloseConsoleWhen)
							.addGap(18)
							.addComponent(chckbxAutomaticallyCheckFor)
							.addContainerGap(198, Short.MAX_VALUE))
				);
				generalTab.setLayout(gl_generalTab);
			}
			{
				advancedTab = new JPanel();
				tabbedPane.addTab("Advanced", null, advancedTab, "Advanced settings such as memory allocations.");
				
				initialMemorySpinner = new JSpinner();
				initialMemorySpinner.addChangeListener(new ChangeListener()
				{
					public void stateChanged(ChangeEvent e)
					{
						((IntSetting) SelectionWindow.getSettings().getSetting("InitialMemAlloc")).set((Integer) initialMemorySpinner.getValue());
					}
				});
				
				JLabel lblInitialMemoryAllocation = new JLabel("Initial Memory Allocation (MB): ");
				
				maxMemorySpinner = new JSpinner();
				maxMemorySpinner.addChangeListener(new ChangeListener()
				{
					public void stateChanged(ChangeEvent e)
					{
						((IntSetting) SelectionWindow.getSettings().getSetting("MaxMemAlloc")).set((Integer) maxMemorySpinner.getValue());
					}
				});
				
				JLabel labelMaxMemAlloc = new JLabel("Maximum Memory Allocation (MB): ");
				GroupLayout gl_advancedTab = new GroupLayout(advancedTab);
				gl_advancedTab.setHorizontalGroup(
					gl_advancedTab.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_advancedTab.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_advancedTab.createParallelGroup(Alignment.LEADING)
								.addComponent(labelMaxMemAlloc)
								.addComponent(lblInitialMemoryAllocation))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_advancedTab.createParallelGroup(Alignment.LEADING)
								.addComponent(initialMemorySpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(maxMemorySpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(144))
				);
				gl_advancedTab.setVerticalGroup(
					gl_advancedTab.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_advancedTab.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_advancedTab.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblInitialMemoryAllocation)
								.addComponent(initialMemorySpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_advancedTab.createParallelGroup(Alignment.BASELINE)
								.addComponent(labelMaxMemAlloc)
								.addComponent(maxMemorySpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addContainerGap(288, Short.MAX_VALUE))
				);
				advancedTab.setLayout(gl_advancedTab);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Close");
				okButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						setVisible(false);
					}
				});
				okButton.setMnemonic('C');
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
}
