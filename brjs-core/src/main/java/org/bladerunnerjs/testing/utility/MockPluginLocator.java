package org.bladerunnerjs.testing.utility;

import java.util.ArrayList;
import java.util.List;

import org.bladerunnerjs.core.plugin.ModelObserverPlugin;
import org.bladerunnerjs.core.plugin.PluginLocator;
import org.bladerunnerjs.core.plugin.PluginLocatorUtils;
import org.bladerunnerjs.core.plugin.bundler.BundlerPlugin;
import org.bladerunnerjs.core.plugin.command.CommandPlugin;
import org.bladerunnerjs.core.plugin.minifier.MinifierPlugin;
import org.bladerunnerjs.core.plugin.servlet.ServletPlugin;
import org.bladerunnerjs.core.plugin.taghandler.TagHandlerPlugin;
import org.bladerunnerjs.model.BRJS;


public class MockPluginLocator implements PluginLocator
{
	public List<BundlerPlugin> bundlers = new ArrayList<BundlerPlugin>();
	public List<CommandPlugin> pluginCommands = new ArrayList<CommandPlugin>();
	public List<ModelObserverPlugin> modelObservers = new ArrayList<ModelObserverPlugin>();
	public List<MinifierPlugin> minifiers = new ArrayList<MinifierPlugin>();
	public List<ServletPlugin> servlets = new ArrayList<ServletPlugin>();
	public List<TagHandlerPlugin> tagHandlers = new ArrayList<TagHandlerPlugin>();
	
	public void createPlugins(BRJS brjs) {
		PluginLocatorUtils.setBRJSForPlugins(brjs, bundlers);
		PluginLocatorUtils.setBRJSForPlugins(brjs, pluginCommands);
		PluginLocatorUtils.setBRJSForPlugins(brjs, modelObservers);
		PluginLocatorUtils.setBRJSForPlugins(brjs, minifiers);
		PluginLocatorUtils.setBRJSForPlugins(brjs, servlets);
		PluginLocatorUtils.setBRJSForPlugins(brjs, tagHandlers);
	}
	
	@Override
	public List<BundlerPlugin> getBundlerPlugins()
	{
		return bundlers;
	}
	
	@Override
	public List<CommandPlugin> getCommandPlugins()
	{
		return pluginCommands;
	}
	
	@Override
	public List<ModelObserverPlugin> getModelObservers()
	{
		return modelObservers;
	}
	
	@Override
	public List<MinifierPlugin> getMinifiers() {
		return minifiers;
	}
	
	@Override
	public List<ServletPlugin> getServlets() {
		return servlets;
	}
	
	@Override
	public List<TagHandlerPlugin> getTagHandlers() {
		return tagHandlers;
	}
}
