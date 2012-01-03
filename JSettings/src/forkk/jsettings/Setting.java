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

package forkk.jsettings;

/**
 * Interface for different values in the settings object.
 * @author Andrew Okin
 */
public interface Setting<VType>
{
	/**
	 * Sets the setting's value to the given value
	 * @param val the new value
	 */
	public void set(VType val);
	
	/**
	 * @return the value of the setting
	 */
	public VType get();
	
	/**
	 * @return the setting's ID. This will be used as the setting's XML tag name
	 */
	public String getID();
}
