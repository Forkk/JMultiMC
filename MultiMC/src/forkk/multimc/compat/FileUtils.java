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
import java.io.IOException;

public class FileUtils
{
	/**
	 * @param f File to check
	 * @return true if f is a symbolic link
	 */
	public static boolean isSymlink(File f)
	{
		if (f == null)
			throw new NullPointerException("File cannot be null.");
		try
		{
			return !f.getAbsolutePath().equals(f.getCanonicalPath());
		} catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
