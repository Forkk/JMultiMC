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

package forkk.multimc.task;

import java.util.ArrayList;

public abstract class BackgroundTask extends Thread
{
	public void run()
	{
		OnProgressChange(0);
		OnStatusChange("");
		TaskStart();
	}
	
	/**
	 * The task's entry-point
	 */
	public abstract void TaskStart();
	
	/**
	 * @return false if the progress bar should show progress. Default is true
	 */
	public boolean isProgressIndeterminate() { return true; }
	
	// Events
	public static interface TaskListener
	{
		public abstract void taskStart(BackgroundTask t);
		public abstract void taskEnd(BackgroundTask t);
	}
	private ArrayList<TaskListener> taskListeners = 
			new ArrayList<BackgroundTask.TaskListener>();
	public void AddTaskListener(TaskListener l) { taskListeners.add(l); }
	public void RemoveTaskListener(TaskListener l) { taskListeners.add(l); }
	protected void OnTaskStart()
	{
		running = true;
		for (TaskListener l : taskListeners)
		{
			l.taskStart(this);
		}
	}
	protected void OnTaskEnd()
	{
		running = false;
		for (TaskListener l : taskListeners)
		{
			l.taskEnd(this);
		}
	}
	
	public static interface ProgressChangeListener
	{
		public abstract void taskProgressChange(BackgroundTask t, int p);
	}
	private ArrayList<ProgressChangeListener> progressListeners = 
			new ArrayList<BackgroundTask.ProgressChangeListener>();
	public void AddProgressListener(ProgressChangeListener l) { progressListeners.add(l); }
	public void RemoveProgressListener(ProgressChangeListener l) { progressListeners.remove(l); }
	protected void OnProgressChange(int p)
	{
		for (ProgressChangeListener l : progressListeners)
		{
			l.taskProgressChange(this, p);
		}
	}
	
	public static interface StatusChangeListener
	{
		public abstract void taskStatusChange(BackgroundTask t, String status);
	}
	private ArrayList<StatusChangeListener> statusListeners = 
			new ArrayList<BackgroundTask.StatusChangeListener>();
	public void AddStatusListener(StatusChangeListener l) { statusListeners.add(l); }
	public void RemoveStatusListener(StatusChangeListener l) { statusListeners.remove(l); }
	protected void OnStatusChange(String newStatus)
	{
		for (StatusChangeListener l : statusListeners)
		{
			l.taskStatusChange(this, newStatus);
		}
	}
	
	public interface ErrorMessageListener
	{
		public abstract void taskErrorMessage(BackgroundTask t, String status);
	}
	private ArrayList<ErrorMessageListener> errorListeners = 
			new ArrayList<BackgroundTask.ErrorMessageListener>();
	public void AddErrorListener(ErrorMessageListener l) { errorListeners.add(l); }
	public void RemoveErrorListener(ErrorMessageListener l) { errorListeners.remove(l); }
	protected void OnErrorMessage(String errorMessage)
	{
		for (ErrorMessageListener l : errorListeners)
		{
			l.taskErrorMessage(this, errorMessage);
		}
	}
	
	
	// Properties
	String status;
	public String getStatus() { return status; }
	public void setStatus(String s) { status = s; OnStatusChange(s); }
	
	int progress;
	public int getProgress() { return progress; }
	public void setProgress(int p) { progress = p; OnProgressChange(p); }
	
	boolean running;
	public boolean isRunning() { return running; }
}
