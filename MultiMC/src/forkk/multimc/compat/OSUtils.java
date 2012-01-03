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

package forkk.multimc.compat;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Utilities to help with cross-platform
 * @author Andrew Okin
 */
public class OSUtils
{
	public static OS getCurrentOS()
	{
		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("win") >= 0)
		{
			return OS.WINDOWS;
		}
		else if (os.indexOf("mac") >= 0)
		{
			return OS.MAC;
		}
		else if (os.indexOf("nux") >= 0 || os.indexOf("nix") >= 0)
		{
			return OS.LINUX;
		}
		else
		{
			return OS.OTHER;
		}
	}
	
	public static File getMinecraftDir()
	{
		switch (getCurrentOS())
		{
		case WINDOWS:
			return new File(System.getenv("appdata"), ".minecraft");
			
		case MAC:
			try
			{
				return new File(new URI(System.getProperty("user.home"), 
						"Library", "Application Support", "minecraft"));
			} catch (URISyntaxException e)
			{
				e.printStackTrace();
				return new File(System.getProperty("user.home"), 
						"Library/Application Support/.minecraft");
			}
		
		default:
			return new File(System.getProperty("user.home"), ".minecraft");
		}
	}
}
