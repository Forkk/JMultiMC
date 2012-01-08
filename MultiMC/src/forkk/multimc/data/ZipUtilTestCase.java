package forkk.multimc.data;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import forkk.multimc.util.ZipUtils;

public class ZipUtilTestCase
{
	public static void main(String[] args)
	{
		try
		{
			File jarFile = new File("instances/Test/.minecraft/bin/minecraft.jar");
			File dest = new File("instances/Test/mcJar");
			if (dest.exists())
				dest.delete();
			dest.mkdir();
			File reZip = new File("instances/Test/mcJar.zip");
			if (reZip.exists())
				reZip.delete();
			reZip.createNewFile();
			
			ZipUtils.ExtractAllFromZip(new ZipFile(jarFile), dest);
			ZipUtils.recursiveAddToZip(dest, reZip, "");
		} catch (ZipException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
