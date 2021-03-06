package org.bladerunnerjs.spec.brjs.appserver;
// TODO: this copy of MockTagHandler doesn't need to be copied back when the tests are moved back to 'brjs-core'

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;

import org.bladerunnerjs.model.BRJS;
import org.bladerunnerjs.model.BundleSet;
import org.bladerunnerjs.plugin.Locale;
import org.bladerunnerjs.plugin.TagHandlerPlugin;
import org.bladerunnerjs.plugin.base.AbstractTagHandlerPlugin;


public class MockTagHandler extends AbstractTagHandlerPlugin implements TagHandlerPlugin
{
	
	String name;
	String devContent;
	String prodContent;
	boolean printLocales;
	
	public MockTagHandler(String name, String devContent, String prodContent)
	{
		this(name, devContent, prodContent, false);
	}
	
	public MockTagHandler(String name, String devContent, String prodContent, boolean printLocales)
	{
		this.name = name;
		this.devContent = devContent;
		this.prodContent = prodContent;
		this.printLocales = printLocales;
	}

	@Override
	public void setBRJS(BRJS brjs)
	{
	}

	@Override
	public String getTagName()
	{
		return name;
	}
	
	@Override
	public void writeDevTagContent(Map<String, String> tagAttributes, BundleSet bundleSet, Locale locale, Writer writer, String version) throws IOException
	{
		writeOutContentAndTagAttributes(writer, devContent, tagAttributes, locale);
	}

	@Override
	public void writeProdTagContent(Map<String, String> tagAttributes, BundleSet bundleSet, Locale locale, Writer writer, String version) throws IOException
	{
		writeOutContentAndTagAttributes(writer, prodContent, tagAttributes, locale);
	}
	
	private void writeOutContentAndTagAttributes(Writer writer, String content, Map<String, String> tagAttributes, Locale locale) throws IOException
	{
		PrintWriter printWriter = new PrintWriter(writer);
		if (!content.equals(""))
		{
			printWriter.print(content);
			if (tagAttributes.keySet().size() > 0) {
				printWriter.println("");
			}
			for (String attributeKey : tagAttributes.keySet())
			{
				String attributeValue = tagAttributes.get(attributeKey);
				printWriter.println(attributeKey+"="+attributeValue);
			}
		}
		
		if (printLocales) {
			printWriter.println("- "+locale);
		}
	}

}
