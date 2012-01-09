package forkk.multimc.gui;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import forkk.multimc.task.BackgroundTask;

public class ForegroundTaskDialog extends JDialog
{
	private static final long serialVersionUID = -8233885819803211100L;
	
	BackgroundTask bgTask;
	private JProgressBar progressBar;
	private JLabel lblStatus;
	
	/**
	 * Create the frame.
	 */
	public ForegroundTaskDialog(BackgroundTask task)
	{
		setUndecorated(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 450, 40);
		
		progressBar = new JProgressBar();
		getContentPane().add(progressBar, BorderLayout.SOUTH);
		
		lblStatus = new JLabel("Status");
		getContentPane().add(lblStatus, BorderLayout.NORTH);
		
		this.bgTask = task;
		bgTask.AddTaskListener(new BackgroundTask.TaskListener()
		{
			
			@Override
			public void taskStatusChange(BackgroundTask t, String status)
			{
				setTitle(status);
				lblStatus.setText(status);
			}
			
			@Override
			public void taskStart(BackgroundTask t)
			{
				setTitle(t.getStatus());
				lblStatus.setText(t.getStatus());
			}
			
			@Override
			public void taskProgressChange(BackgroundTask t, int p)
			{
				progressBar.setValue(p);
			}
			
			@Override
			public void taskErrorMessage(BackgroundTask t, String status)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void taskEnd(BackgroundTask t)
			{
				setVisible(false);
				dispose();
			}
		});
	}
}
