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


public class BoolSetting implements Setting<Boolean>
{
	SettingsFile settings;
	Boolean defValue;
	String id;
	
	public BoolSetting(SettingsFile settings, String id, Boolean defValue)
	{
		this.defValue = defValue;
		this.settings = settings;
		this.id = id;
	}
	
	public BoolSetting(SettingsFile settings, String id)
	{
		this.settings = settings;
		this.id = id;
	}
	
	@Override
	public void set(Boolean val)
	{
		settings.getXmlNode(id, null, defValue.toString()).setTextContent(val.toString());
		settings.AutoSave();
	}

	@Override
	public Boolean get()
	{
		return Boolean.parseBoolean(
				settings.getXmlNode(id, null, defValue.toString()).getTextContent());
	}
	
	@Override
	public String getID()
	{
		return id;
	}
}
