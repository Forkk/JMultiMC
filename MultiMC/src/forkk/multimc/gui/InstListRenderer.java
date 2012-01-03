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

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import forkk.multimc.data.Instance;
import forkk.multimc.instIcons.InstIconList;

public class InstListRenderer implements ListCellRenderer<Instance>
{
	protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
	
	@Override
	public Component getListCellRendererComponent(JList<? extends Instance> list,
			Instance value, int index, boolean isSelected, boolean cellHasFocus)
	{
		JLabel cell = (JLabel) defaultRenderer.getListCellRendererComponent(list,
				value, index, isSelected, cellHasFocus);
		
		if (InstIconList.getList() == null)
			System.out.println("null");
		
		cell.setText(value.getName());
		cell.setIcon(InstIconList.getList().get(value.getIconKey()));
		
		cell.setHorizontalTextPosition(JLabel.CENTER);
		cell.setVerticalTextPosition(JLabel.BOTTOM);
		
		return cell;
	}
}
