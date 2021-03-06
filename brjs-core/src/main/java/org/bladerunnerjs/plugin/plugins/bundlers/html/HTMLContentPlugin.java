package org.bladerunnerjs.plugin.plugins.bundlers.html;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;

import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.StartTag;
import net.htmlparser.jericho.StreamedSource;

import org.apache.commons.io.IOUtils;
import org.bladerunnerjs.aliasing.NamespaceException;
import org.bladerunnerjs.model.Asset;
import org.bladerunnerjs.model.BRJS;
import org.bladerunnerjs.model.BundleSet;
import org.bladerunnerjs.model.UrlContentAccessor;
import org.bladerunnerjs.model.ParsedContentPath;
import org.bladerunnerjs.model.exception.RequirePathException;
import org.bladerunnerjs.model.exception.request.MalformedTokenException;
import org.bladerunnerjs.model.exception.request.ContentFileProcessingException;
import org.bladerunnerjs.model.exception.request.ContentProcessingException;
import org.bladerunnerjs.plugin.AssetPlugin;
import org.bladerunnerjs.plugin.CharResponseContent;
import org.bladerunnerjs.plugin.ResponseContent;
import org.bladerunnerjs.plugin.Locale;
import org.bladerunnerjs.plugin.base.AbstractContentPlugin;
import org.bladerunnerjs.utility.ContentPathParser;
import org.bladerunnerjs.utility.ContentPathParserBuilder;
import org.bladerunnerjs.utility.NamespaceUtility;
import org.bladerunnerjs.utility.AppMetadataUtility;


public class HTMLContentPlugin extends AbstractContentPlugin
{
	private ContentPathParser contentPathParser;
	private Map<String, Asset> identifiers = new TreeMap<String, Asset>();
	private final List<String> requestPaths = new ArrayList<>();
	
	private AssetPlugin htmlAssetPlugin;
	private BRJS brjs;
	
	{
		try {
			ContentPathParserBuilder contentPathParserBuilder = new ContentPathParserBuilder();
			contentPathParserBuilder.accepts("html/bundle.html").as("bundle-request");
			contentPathParser = contentPathParserBuilder.build();
			requestPaths.add(contentPathParser.createRequest("bundle-request"));
		}
		catch(MalformedTokenException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setBRJS(BRJS brjs)
	{
		htmlAssetPlugin = brjs.plugins().assetPlugin(HTMLAssetPlugin.class);
		this.brjs = brjs;
	}
	
	@Override
	public String getRequestPrefix() {
		return "html";
	}

	@Override
	public String getCompositeGroupName() {
		return null;
	}
	
	@Override
	public ContentPathParser getContentPathParser()
	{
		return contentPathParser;
	}

	@Override
	public List<String> getValidDevContentPaths(BundleSet bundleSet, Locale... locales) throws ContentProcessingException
	{
		return getValidContentPaths(bundleSet);
	}
	
	@Override
	public List<String> getValidProdContentPaths(BundleSet bundleSet, Locale... locales) throws ContentProcessingException
	{
		return getValidContentPaths(bundleSet);
	}

	@Override
	public ResponseContent handleRequest(ParsedContentPath contentPath, BundleSet bundleSet, UrlContentAccessor output, String version) throws ContentProcessingException
	{
		identifiers = new TreeMap<String, Asset>();
		List<Asset> htmlAssets = bundleSet.getResourceFiles(htmlAssetPlugin);
		
		List<Reader> readerList = new ArrayList<Reader>();
		for(Asset htmlAsset : htmlAssets){
			try {
				validateSourceHtml(htmlAsset);

				try(Reader reader = htmlAsset.getReader()) {
					readerList.add(new StringReader("\n<!-- " + htmlAsset.getAssetName() + " -->\n"));

					String bundlePath = AppMetadataUtility.getRelativeVersionedBundlePath(version, "").replaceFirst("/$", "");
					String xmlBundlePathToken = AppMetadataUtility.XML_BUNDLE_PATH_TOKEN;

					String htmlContent = IOUtils.toString(reader);
					String replaced =  htmlContent.replace(xmlBundlePathToken, bundlePath);
					readerList.add(new StringReader(replaced));
				}
			}
			catch (IOException | NamespaceException | RequirePathException e) {
				throw new ContentProcessingException(e, "Error while bundling asset '" + htmlAsset.getAssetPath() + "'.");
			}
		}
		
		return new CharResponseContent( brjs, readerList );		
	}
	
	private List<String> getValidContentPaths(BundleSet bundleSet) {
		return (bundleSet.getResourceFiles(htmlAssetPlugin).isEmpty()) ? Collections.emptyList() : requestPaths;
	}
	
	private void validateSourceHtml(Asset htmlAsset) throws IOException, ContentFileProcessingException, NamespaceException, RequirePathException
	{
		StartTag startTag = getStartTag(htmlAsset);
		String identifier = startTag.getAttributeValue("id");
		
		if(identifier == null)
		{
			String idMessage = (htmlAsset.assetLocation().assetContainer().isNamespaceEnforced()) ?
				"a namespaced ID of '" + NamespaceUtility.convertToNamespace(htmlAsset.assetLocation().requirePrefix()) + ".*'" : "an ID";
			
			throw new NamespaceException( "HTML template found without an identifier: '" +
					startTag.toString() + "'.  Root element should have " + idMessage + ".");
		}
		
		htmlAsset.assetLocation().assertIdentifierCorrectlyNamespaced(identifier);
		
		Asset assetWithDuplicateId = identifiers.get(identifier);
		if(assetWithDuplicateId == null){
			identifiers.put(identifier, htmlAsset);
		}else{
			throw new NamespaceException("HTML template found with a duplicate identifier: '" +
						identifier + "'. The same identifier is used for the file:\n'" 
						+ assetWithDuplicateId.getAssetPath()
						+ "'.");
		}
	}
	
	
	private StartTag getStartTag(Asset htmlAsset) throws IOException
	{
		try(Reader reader = htmlAsset.getReader())
		{
			StreamedSource streamedSource = new StreamedSource(reader);
			StartTag startTag = null;
			
			try
			{
				for(Segment nextSegment : streamedSource)
				{
					if(nextSegment instanceof StartTag)
					{
						startTag = (StartTag) nextSegment;
						break;
					}
				}
			}
			finally
			{
				streamedSource.close();
			}
			
			return startTag;
		}
	}
	
}
