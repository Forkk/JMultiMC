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

public abstract class Task extends Thread
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
		public abstract void taskStart(Task t);
		public abstract void taskEnd(Task t);
		public abstract void taskProgressChange(Task t, int p);
		public abstract void taskStatusChange(Task t, String status);
		public abstract void taskErrorMessage(Task t, String status);
	}
	private ArrayList<TaskListener> taskListeners = 
			new ArrayList<Task.TaskListener>();
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
		for (TaskListener l : taskListeners.toArray(new TaskListener[0]))
		{
			l.taskEnd(this);
		}
	}
	
	public void AddProgressListener(TaskListener l) { taskListeners.add(l); }
	public void RemoveProgressListener(TaskListener l) { taskListeners.remove(l); }
	protected void OnProgressChange(int p)
	{
		for (TaskListener l : taskListeners)
		{
			l.taskProgressChange(this, p);
		}
	}
	
	public void AddStatusListener(TaskListener l) { taskListeners.add(l); }
	public void RemoveStatusListener(TaskListener l) { taskListeners.remove(l); }
	protected void OnStatusChange(String newStatus)
	{
		for (TaskListener l : taskListeners)
		{
			l.taskStatusChange(this, newStatus);
		}
	}
	
	public void AddErrorListener(TaskListener l) { taskListeners.add(l); }
	public void RemoveErrorListener(TaskListener l) { taskListeners.remove(l); }
	protected void OnErrorMessage(String errorMessage)
	{
		for (TaskListener l : taskListeners)
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
