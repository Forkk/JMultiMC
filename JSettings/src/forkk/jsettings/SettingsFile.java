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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import forkk.jsettings.errors.SettingsLoadException;
import forkk.jsettings.errors.SettingsSaveException;

public class SettingsFile
{
	// Code
	private HashMap<String, Setting<?>> settingsMap;
	
	private Document xmlDoc;
	private DocumentBuilder docBuilder;
	
	private boolean autosave;
	
	public SettingsFile(File file) throws SettingsLoadException
	{
		settingsMap = new HashMap<String, Setting<?>>();
		settingsFile = file;
		autosave = true;
		try
		{
			docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			
			if (getSettingsFile().exists())
				xmlDoc = docBuilder.parse(getSettingsFile());
			else
				xmlDoc = docBuilder.newDocument();
		} catch (ParserConfigurationException e)
		{
			e.printStackTrace();
			throw new SettingsLoadException("Failed to load the instance " +
					"because the XML parser was misconfigured.");
		} catch (SAXException e)
		{
			e.printStackTrace();
			throw new SettingsLoadException("Failed to parse the instance's " +
					"XML file. " + e.getMessage());
		} catch (IOException e)
		{
			e.printStackTrace();
			throw new SettingsLoadException("Unknown IO exception when " +
					"loading instance. " + e.getMessage());
		}
	}
	
	/**
	 * @param id The ID of the setting to get
	 * @return The setting with the given ID
	 */
	public Setting<?> getSetting(String id)
	{
		return this.settingsMap.get(id);
	}
	
	/**
	 * Adds the given setting to the file. If there is already a setting with
	 * the setting's ID, it will be overwritten. This method is called from
	 * within the constructors of the setting types
	 * @param setting the new setting
	 */
	protected void settingAdded(Setting<?> setting)
	{
		this.settingsMap.put(setting.getID(), setting);
	}
	
	/**
	 * Registers the given setting with this settings file. This method should be 
	 * automatically called by setting types
	 * @param newSetting
	 */
	protected void addSetting(Setting<?> newSetting)
	{
		
	}
	
	protected void AutoSave()
	{
		if (autosave)
		{
			try
			{
				Save();
			} catch (SettingsSaveException e)
			{
				e.printStackTrace();
				return;
			}
		}
	}
	
	/**
	 * Saves the settings XML document
	 * @throws SettingsSaveException If the settings fail to save
	 */
	public void Save() throws SettingsSaveException
	{
		try
		{
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			
			if (!getSettingsFile().exists())
				getSettingsFile().createNewFile();
			StreamResult result = new StreamResult(getSettingsFile());
			DOMSource source = new DOMSource(xmlDoc);
			transformer.transform(source, result);
		} catch (TransformerConfigurationException e)
		{
			e.printStackTrace();
			throw new SettingsSaveException("Instance failed to save because " +
					"the XML transformer was misconfigured.");
		} catch (TransformerFactoryConfigurationError e)
		{
			e.printStackTrace();
			throw new SettingsSaveException("Instance failed to save because " +
					"the XML transformer factory was misconfigured.");
		} catch (TransformerException e)
		{
			e.printStackTrace();
			throw new SettingsSaveException("Instance failed to save because " +
					"the XML transformer failed.");
		} catch (IOException e)
		{
			e.printStackTrace();
			throw new SettingsSaveException("Couldn't create instance data " +
					"file.");
		}
	}
	
	private Node getRootNode()
	{
		final String name = "instance";
		NodeList nodes = xmlDoc.getElementsByTagName(name);
		if (nodes.getLength() <= 0)
		{
			return xmlDoc.appendChild(xmlDoc.createElement(name));
		}
		else
		{
			return nodes.item(0);
		}
	}
	
	/**
	 * Returns the XML element with the given name. The element will be created
	 * if it doesn't exist
	 * This method is only meant to be called from setting classes in order for
	 * them to get / set their values.
	 * @param element the name of the element to get
	 * @param parent the name of the element's parent
	 * @return the XML node
	 */
	protected Node getXmlNode(String element)
	{
		return getXmlNode(element, null);
	}
	
	/**
	 * Returns the XML element with the given name. The element will be created
	 * if it doesn't exist
	 * This method is only meant to be called from setting classes in order for
	 * them to get / set their values.
	 * @param element the name of the element to get
	 * @param parent the name of the element's parent
	 * @return the XML node
	 */
	protected Node getXmlNode(String element, Element parent)
	{
		return getXmlNode(element, parent, null);
	}
	
	/**
	 * Returns the XML element with the given name. The element will be created
	 * if it doesn't exist
	 * This method is only meant to be called from setting classes in order for
	 * them to get / set their values.
	 * @param element the name of the element to get
	 * @param parent the name of the element's parent
	 * @param defValue the default value of the new node
	 * @return the XML node
	 */
	protected Node getXmlNode(String element, Element parent, String defValue)
	{
		if (parent == null)
			parent = (Element) getRootNode();
		NodeList nodes = parent.getElementsByTagName(element);
		if (nodes.getLength() <= 0)
		{
			if (defValue == null)
				return parent.appendChild(xmlDoc.createElement(element));
			else
			{
				Element newElement = xmlDoc.createElement(element);
				newElement.setTextContent(defValue);
				return parent.appendChild(newElement);
			}
		}
		else
		{
			return nodes.item(0);
		}
	}
	
	// Files
	private File getSettingsFile() { return settingsFile; }
	private File settingsFile;
}
