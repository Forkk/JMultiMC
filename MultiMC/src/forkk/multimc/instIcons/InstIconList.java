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

package forkk.multimc.instIcons;

import java.net.URL;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class InstIconList extends HashMap<String, Icon>
{
	private static final long serialVersionUID = -3023113349114910001L;
	
	private static InstIconList list;
	
	public static InstIconList getList()
	{
		if (list == null)
			list = new InstIconList();
		return list;
	}

	/**
	 * The resource path for default icons
	 */
	private static final String DefaultIconResourcePath = "/forkk/multimc/instIcons/";
	
	private static final Icon DefaultIcon = new ImageIcon(
			InstIconList.class.getResource(DefaultIconResourcePath + "stone.png"));
	
	/**
	 * The keys for all of the default icons
	 */
	private static final String[] DefIconKeys = new String[] { "brick",
			"diamond", "dirt", "gold", "grass", "iron", "planks", "stone",
			"tnt", };
	
	private InstIconList()
	{
		loadDefaultIcons();
	}
	
	/**
	 * Adds the specified icon with the given key
	 * 
	 * @param iconKey
	 *            The key that will be used to access the icon
	 * @param iconFile
	 *            The file to bind to the key
	 */
	public void addIcon(String iconKey, URL iconFile)
	{
		put(iconKey, new ImageIcon(iconFile));
	}
	
	/**
	 * Loads the default icons
	 */
	private void loadDefaultIcons()
	{
		for (String key : DefIconKeys)
		{
			URL url = InstIconList.class.getResource(DefaultIconResourcePath
					+ key + ".png");
//			System.out.println("Loading default icon: " + url);
			addIcon(key, url);
		}
	}
	
	public Icon get(String key)
	{
		if (key == null || !containsKey(key))
			return DefaultIcon;
		else
			return super.get("key");
	}
}
