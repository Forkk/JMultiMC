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
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import forkk.multimc.settings.AppSettings;

public class SettingsDialog extends JDialog
{
	private static final long serialVersionUID = -7764610862804154562L;
	
	private final JPanel contentPanel = new JPanel();
	private JPanel advancedTab;
	private JPanel generalTab;
	private JSpinner initialMemorySpinner;
	private JSpinner maxMemorySpinner;
	private JCheckBox chckbxShowConsole;
	private JCheckBox chckbxAutoCloseConsole;
	private JCheckBox chckbxAutoUpdate;
	private JTextField launcherFileTextField;
	
//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args)
//	{
//		try
//		{
//			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//		} catch (Throwable e)
//		{
//			e.printStackTrace();
//		}
//		try
//		{
//			SettingsDialog dialog = new SettingsDialog();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	
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
				launcherFileTextField = new JTextField(AppSettings.getLauncherFilename());
				launcherFileTextField.setColumns(10);
				launcherFileTextField.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						AppSettings.setLauncherFilename(launcherFileTextField.getText());
					}
				});
				
				JLabel lblConsoleSettings = new JLabel("Console Settings:");
				
				chckbxShowConsole = new JCheckBox("Show console");
				chckbxShowConsole.getModel().setSelected(AppSettings.getShowConsole());
				chckbxShowConsole.getModel().addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						AppSettings.setShowConsole(chckbxShowConsole.getModel().isSelected());
					}
				});
				
				chckbxAutoCloseConsole = new JCheckBox("Close console when instance exits.");
				chckbxAutoCloseConsole.getModel().setSelected(AppSettings.getAutoCloseConsole());
				chckbxAutoCloseConsole.getModel().addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						AppSettings.setAutoCloseConsole(chckbxAutoCloseConsole.getModel().isSelected());
					}
				});
				
				chckbxAutoUpdate = new JCheckBox("Automatically check for updates when MultiMC starts");
				chckbxAutoUpdate.getModel().setSelected(AppSettings.getAutoUpdate());
				chckbxAutoUpdate.getModel().addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						AppSettings.setCheckUpdates(chckbxAutoUpdate.getModel().isSelected());
					}
				});
				
				GroupLayout gl_generalTab = new GroupLayout(generalTab);
				gl_generalTab.setHorizontalGroup(
					gl_generalTab.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_generalTab.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_generalTab.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_generalTab.createSequentialGroup()
									.addComponent(lblLauncherFilename)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(launcherFileTextField, GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE))
								.addGroup(gl_generalTab.createSequentialGroup()
									.addGap(6)
									.addGroup(gl_generalTab.createParallelGroup(Alignment.LEADING)
										.addComponent(chckbxAutoCloseConsole)
										.addComponent(chckbxShowConsole)))
								.addComponent(chckbxAutoUpdate)
								.addComponent(lblConsoleSettings))
							.addContainerGap())
				);
				gl_generalTab.setVerticalGroup(
					gl_generalTab.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_generalTab.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_generalTab.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblLauncherFilename)
								.addComponent(launcherFileTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addComponent(lblConsoleSettings)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chckbxShowConsole)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chckbxAutoCloseConsole)
							.addGap(18)
							.addComponent(chckbxAutoUpdate)
							.addContainerGap(198, Short.MAX_VALUE))
				);
				generalTab.setLayout(gl_generalTab);
			}
			{
				advancedTab = new JPanel();
				tabbedPane.addTab("Advanced", null, advancedTab, "Advanced settings such as memory allocations.");
				
				initialMemorySpinner = new JSpinner();
				initialMemorySpinner.setModel(new SpinnerNumberModel(
						AppSettings.getInitialMemAlloc(),
						new Integer(512), null, new Integer(512)));
				initialMemorySpinner.getModel().addChangeListener(new ChangeListener()
				{
					@Override
					public void stateChanged(ChangeEvent arg0)
					{
						AppSettings.setInitialMemAlloc((Integer) initialMemorySpinner.getModel().getValue());
					}
				});
				
				JLabel lblInitialMemoryAllocation = new JLabel("Initial Memory Allocation (MB): ");
				
				maxMemorySpinner = new JSpinner();
				maxMemorySpinner.setModel(new SpinnerNumberModel(
						AppSettings.getMaxMemAlloc(), 
						new Integer(1024), null, new Integer(512)));
				maxMemorySpinner.getModel().addChangeListener(new ChangeListener()
				{
					@Override
					public void stateChanged(ChangeEvent e)
					{
						AppSettings.setMaxMemAlloc((Integer) maxMemorySpinner.getModel().getValue());
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
							.addGroup(gl_advancedTab.createParallelGroup(Alignment.LEADING, false)
								.addComponent(maxMemorySpinner)
								.addComponent(initialMemorySpinner))
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
							.addContainerGap(276, Short.MAX_VALUE))
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
						AppSettings.setLauncherFilename(launcherFileTextField.getText());
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
