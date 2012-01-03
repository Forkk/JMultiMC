/**
 * Copyright 2012 Andrew Okin
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package forkk.multimc.gui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.security.InvalidParameterException;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import forkk.jsettings.BoolSetting;
import forkk.jsettings.IntSetting;
import forkk.jsettings.SettingsFile;
import forkk.jsettings.StringSetting;
import forkk.jsettings.errors.SettingsLoadException;
import forkk.multimc.data.Instance;
import forkk.multimc.data.InstanceListModel;
import forkk.multimc.data.Version;
import forkk.multimc.data.exceptions.InstanceLoadException;
import forkk.multimc.data.exceptions.InstanceSaveException;
import forkk.multimc.task.BackgroundTask;
import forkk.multimc.task.UpdateCheck;

public class SelectionWindow implements ActionListener,
		BackgroundTask.ProgressChangeListener, BackgroundTask.StatusChangeListener,
		BackgroundTask.TaskListener, BackgroundTask.ErrorMessageListener
{
	public static final Version currentVersion = new Version(2, 0, 0);
	
	private static String MainWindowTitle = "MultiMC";
	
	private static SettingsFile settings;
	private static final String settingsFileName = "multimc.cfg";
	
	/**
	 * The web page that is opened for manual updates.
	 */
	private static String updatePage = "http://www.tinyurl.com/multiplemc";
	
	/**
	 * @return MultiMC's main settings file
	 */
	public static SettingsFile getSettings()
	{
		try
		{
			if (settings == null)
				return initSettings();
			else
				return settings;
		} catch (SettingsLoadException e)
		{
			// TODO Handle this error properly ;)
			e.printStackTrace();
			return null;
		}
	}
	
	private static SettingsFile initSettings() throws SettingsLoadException
	{
		settings = new SettingsFile(new File(settingsFileName));
		
		// Instance launch settings
		settings.addSetting(new StringSetting(settings, "LauncherFile", "launcher.jar"));
		settings.addSetting(new IntSetting(settings, "InitialMemAlloc", 512));
		settings.addSetting(new IntSetting(settings, "MaxMemAlloc", 1024));
		
		// Instance console settings
		settings.addSetting(new BoolSetting(settings, "ShowConsole", false));
		settings.addSetting(new BoolSetting(settings, "AutoCloseConsole", false));
		
		// Other settings
		settings.addSetting(new BoolSetting(settings, "AutoCheckUpdates", true));
		
		return settings;
	}
	
	
	private JFrame mainFrame;
	
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
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					SelectionWindow window = new SelectionWindow();
					window.mainFrame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the application.
	 */
	public SelectionWindow()
	{
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		// Initialize Data
		instList = new InstanceListModel();
		
		// Initialize GUI
		mainFrame = new JFrame();
		mainFrame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowOpened(WindowEvent e)
			{
				if (((BoolSetting) getSettings().getSetting("AutoCheckUpdates")).get())
				{
					checkUpdates();
				}
			}
		});
		mainFrame.setTitle(MainWindowTitle);
		mainFrame.setBounds(100, 100, 620, 400);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		instListView = new JList<Instance>();
		instListView.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				if (e.getButton() == MouseEvent.BUTTON3)
				{
					instListView.setSelectedIndex(instListView.locationToIndex(e.getPoint()));
					instPopupMenu.show(instListView, e.getPoint().x, e.getPoint().y);
				}
				
				if (e.getClickCount() >= 2 && e.getButton() == MouseEvent.BUTTON1)
				{
					actionPerformed(new ActionEvent(instListView,
							ActionEvent.ACTION_PERFORMED, "launch"));
				}
			}
		});
		instListView.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		instListView.setVisibleRowCount(0);
		instListView.setModel(instList);
		instListView.setCellRenderer(new InstanceListRenderer());
		instListView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		instListView.addMouseListener(new MouseAdapter()
//		{
//		});
		mainFrame.getContentPane().add(instListView, BorderLayout.CENTER);
		
		instPopupMenu = new JPopupMenu();
		instPopupMenu.setLabel("");
		//addPopup(instListView, instPopupMenu);
		
		mntmLaunch = new JMenuItem("Launch");
		mntmLaunch.addActionListener(this);
		instPopupMenu.add(mntmLaunch);
		
		separator = new JSeparator();
		instPopupMenu.add(separator);
		
		mntmRename = new JMenuItem("Rename");
		mntmRename.addActionListener(this);
		instPopupMenu.add(mntmRename);
		
		mntmChangeIcon = new JMenuItem("Change Icon");
		mntmChangeIcon.addActionListener(this);
		instPopupMenu.add(mntmChangeIcon);
		
		mntmNotes = new JMenuItem("Notes");
		mntmNotes.addActionListener(this);
		instPopupMenu.add(mntmNotes);
		
		mntmViewFolder = new JMenuItem("View Folder");
		mntmViewFolder.addActionListener(this);
		instPopupMenu.add(mntmViewFolder);
		
		separator_1 = new JSeparator();
		instPopupMenu.add(separator_1);
		
		mntmEditMods = new JMenuItem("Edit Mods");
		mntmEditMods.addActionListener(this);
		instPopupMenu.add(mntmEditMods);
		
		mntmRebuildMinecraftjar = new JMenuItem("Rebuild minecraft.jar");
		mntmRebuildMinecraftjar.addActionListener(this);
		instPopupMenu.add(mntmRebuildMinecraftjar);
		
		separator_2 = new JSeparator();
		instPopupMenu.add(separator_2);
		
		mntmDeleteInstance = new JMenuItem("Delete Instance");
		mntmDeleteInstance.addActionListener(this);
		instPopupMenu.add(mntmDeleteInstance);
		
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		mainFrame.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		btnNewInst = new JButton("");
		toolBar.add(btnNewInst);
		btnNewInst.setToolTipText("New Instance");
		btnNewInst.setIcon(new ImageIcon(SelectionWindow.class.getResource("/forkk/multimc/icons/NewInstIcon.png")));
		btnNewInst.addActionListener(this);
		
		btnViewFolder = new JButton("");
		btnViewFolder.setEnabled(false);
		toolBar.add(btnViewFolder);
		btnViewFolder.setToolTipText("View Folder");
		btnViewFolder.setIcon(new ImageIcon(SelectionWindow.class.getResource("/forkk/multimc/icons/ViewFolderIcon.png")));
		btnViewFolder.addActionListener(this);
		
		btnRefresh = new JButton("");
		toolBar.add(btnRefresh);
		btnRefresh.setIcon(new ImageIcon(SelectionWindow.class.getResource("/forkk/multimc/icons/RefreshInstIcon.png")));
		btnRefresh.setToolTipText("Refresh");
		btnRefresh.addActionListener(this);
		
		toolBar.add(new JToolBar.Separator());
		
		btnSettings = new JButton("");
		btnSettings.setEnabled(false);
		btnSettings.setIcon(new ImageIcon(SelectionWindow.class.getResource("/forkk/multimc/icons/SettingsIcon.png")));
		btnSettings.setToolTipText("Settings");
		toolBar.add(btnSettings);
		btnSettings.addActionListener(this);
		
		btnCheckForUpdates = new JButton("");
		btnCheckForUpdates.setIcon(new ImageIcon(SelectionWindow.class.getResource("/forkk/multimc/icons/CheckUpdateIcon.png")));
		btnCheckForUpdates.setToolTipText("Check for updates...");
		toolBar.add(btnCheckForUpdates);
		btnCheckForUpdates.addActionListener(this);
		
		toolBar.add(Box.createHorizontalGlue());
		
		btnHelp = new JButton("Help");
		btnHelp.setEnabled(false);
		btnHelp.setIcon(new ImageIcon(SelectionWindow.class.getResource("/forkk/multimc/icons/HelpIcon.png")));
		toolBar.add(btnHelp);
		btnHelp.addActionListener(this);
		
		btnAbout = new JButton("About");
		btnAbout.setEnabled(false);
		btnAbout.setIcon(new ImageIcon(SelectionWindow.class.getResource("/forkk/multimc/icons/AboutIcon.png")));
		toolBar.add(btnAbout);
		btnAbout.addActionListener(this);
		
		statusBar = new JToolBar();
		statusBar.setFloatable(false);
		mainFrame.getContentPane().add(statusBar, BorderLayout.SOUTH);
		
		taskProgressBar = new JProgressBar();
		taskProgressBar.setVisible(false);
		taskProgressBar.setStringPainted(true);
		taskProgressBar.setValue(50);
		statusBar.add(taskProgressBar);
		
		lblTaskStatus = new JLabel("Task status...");
		lblTaskStatus.setVisible(false);
		statusBar.add(lblTaskStatus);
		
		mainFrame.setTitle(MainWindowTitle + " " + currentVersion);
		
		loadInstances();
	}
	
	private static final Path InstanceDirectory = FileSystems.getDefault().getPath("instances");
	
	private BackgroundTask currentTask;
	
	public void startTask(BackgroundTask task)
	{
		if (isTaskRunning())
			throw new InvalidParameterException("MultiMC can't multitask! " +
					"(Background task can't start because one is already running)");
		
		this.currentTask = task;
		currentTask.AddErrorListener(this);
		currentTask.AddProgressListener(this);
		currentTask.AddStatusListener(this);
		currentTask.AddTaskListener(this);
		currentTask.start();
	}
	
	/**
	 * Checks for updates
	 */
	private void checkUpdates()
	{
		if (!isTaskRunning())
			
		startTask(new UpdateCheck());
	}
	
	public boolean isTaskRunning()
	{
		return (currentTask != null && currentTask.isRunning());
	}
	
	/**
	 * Loads instances from the instance directory
	 */
	private void loadInstances()
	{
		try
		{
			instList.clear();
			
			if (!Files.exists(InstanceDirectory, LinkOption.NOFOLLOW_LINKS))
				Files.createDirectory(InstanceDirectory);
			
			DirectoryStream<Path> dirStream = Files.newDirectoryStream(InstanceDirectory);
			for (Path p : dirStream)
			{
				if (Files.exists(p.resolve(Instance.InstanceDataFileName), LinkOption.NOFOLLOW_LINKS))
				{
					try
					{
						instList.addElement(new Instance(p.toString()));
					} catch (InstanceLoadException e)
					{
						e.printStackTrace();
						System.out.println("Failed to load instance from " + p.toString());
					}
				}
			}
			dirStream.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent event)
	{
		//							Launch
		if (event.getSource() == instListView || event.getSource() == mntmLaunch)
		{
			System.out.println("Launching " + instListView.getSelectedValue().getName());
			instListView.getSelectedValue().Launch();
		}
		
		
		/////////////////////////// MAIN TOOLBAR /////////////////////////////
		// 							New instance
		else if (event.getSource() == btnNewInst)
		{
			String instName = JOptionPane.showInputDialog(mainFrame, 
					"Type a name for your new instance:", "New Instance", JOptionPane.QUESTION_MESSAGE);
			if (instName != null)
			{
				try
				{
					Path instPath = InstanceDirectory.resolve(instName.replaceAll("[^a-zA-Z0-9]", "_"));
					Instance newInst = new Instance(instName, instPath.toString());
					newInst.Save();
				} catch (InstanceLoadException e1)
				{
					e1.printStackTrace();
				} catch (InstanceSaveException e1)
				{
					e1.printStackTrace();
				}
				loadInstances();
			}
		}
		
		//							View folder
		else if (event.getSource() == btnViewFolder)
		{
			try
			{
				new ProcessBuilder(InstanceDirectory.toAbsolutePath().toString()).start();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		// 							Refresh
		else if (event.getSource() == btnRefresh)
		{
			loadInstances();
		}
		
		//							Settings
		else if (event.getSource() == btnSettings)
		{
			// TODO Settings button
		}
		
		//							Update
		else if (event.getSource() == btnCheckForUpdates)
		{
			checkUpdates();
		}
		
		//							Help
		else if (event.getSource() == btnHelp)
		{
			// TODO Help button
		}
		
		//							About
		else if (event.getSource() == btnAbout)
		{
			// TODO About button
		}
		
		
		////////////////////////// INSTANCE RIGHT CLICK MENU //////////////////
		//							Rename
		else if (event.getSource() == mntmRename)
		{
			// TODO Rename
		}
		
		//							Change icon
		else if (event.getSource() == mntmChangeIcon)
		{
			// TODO Change icon
		}
		
		//							Notes
		else if (event.getSource() == mntmNotes)
		{
			// TODO Notes
		}
		
		//							View Folder
		else if (event.getSource() == mntmViewFolder)
		{
			if (Desktop.isDesktopSupported())
			{
				try
				{
					Desktop.getDesktop().open(instListView.getSelectedValue().getRootDir());
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		//							Edit Mods
		else if (event.getSource() == mntmEditMods)
		{
			// TODO Edit Mods
		}
		
		//							Rebuild Jar
		else if (event.getSource() == mntmRebuildMinecraftjar)
		{
			// TODO Rebuild Jar
		}
		
		//							Delete
		else if (event.getSource() == mntmDeleteInstance)
		{
			String reply = JOptionPane.showInputDialog(null, "Type DELETE in " +
					"all caps to delete this instance. THIS CANNOT BE UNDONE!",
					"Are you sure?", JOptionPane.QUESTION_MESSAGE);
			
			if (reply != null && reply.equals("DELETE"))
			{
				Instance inst = instListView.getSelectedValue();
				System.out.println("Deleting instance " + inst.getName());
				instList.removeElement(inst);
				inst.getRootDir().delete();
				loadInstances();
			}
		}
	}
	
	InstanceListModel instList;
	private JButton btnNewInst;
	private JButton btnViewFolder;
	private JButton btnRefresh;
	private JButton btnSettings;
	private JButton btnCheckForUpdates;
	private JButton btnHelp;
	private JButton btnAbout;
	private JToolBar toolBar;
	private JToolBar statusBar;
	private JLabel lblTaskStatus;
	private JProgressBar taskProgressBar;
	private JList<Instance> instListView;
	private JPopupMenu instPopupMenu;
	private JMenuItem mntmRename;
	private JSeparator separator;
	private JMenuItem mntmChangeIcon;
	private JMenuItem mntmNotes;
	private JSeparator separator_1;
	private JMenuItem mntmEditMods;
	private JMenuItem mntmRebuildMinecraftjar;
	private JMenuItem mntmViewFolder;
	private JSeparator separator_2;
	private JMenuItem mntmDeleteInstance;
	private JMenuItem mntmLaunch;

	@Override
	public void taskProgressChange(BackgroundTask t, int p)
	{
		taskProgressBar.setValue(p);
	}

	@Override
	public void taskStart(BackgroundTask t)
	{
		lblTaskStatus.setVisible(true);
		taskProgressBar.setIndeterminate(t.isProgressIndeterminate());
		taskProgressBar.setStringPainted(!t.isProgressIndeterminate());
		taskProgressBar.setVisible(true);
	}

	@Override
	public void taskEnd(BackgroundTask t)
	{
		lblTaskStatus.setVisible(false);
		taskProgressBar.setVisible(false);
		
		if (t instanceof UpdateCheck)
		{
			boolean updateAvailable = ((UpdateCheck) t).isUpdateAvailable();
			if (updateAvailable)
			{
				int result = JOptionPane.showConfirmDialog(null, "A new update " +
						"is available. Would you like to download it?", "Update", 
						JOptionPane.YES_NO_OPTION);
				
				if (result == JOptionPane.YES_OPTION && Desktop.isDesktopSupported())
				{
					try
					{
						Desktop.getDesktop().browse(new URI(updatePage));
					} catch (IOException e)
					{
						e.printStackTrace();
					} catch (URISyntaxException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void taskStatusChange(BackgroundTask t, String status)
	{
		lblTaskStatus.setText(status);
	}

	@Override
	public void taskErrorMessage(BackgroundTask t, String message)
	{
		JOptionPane.showMessageDialog(null, message, "Error", 
				JOptionPane.ERROR_MESSAGE);
	}
}
