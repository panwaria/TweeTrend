import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Class to parse the XML file and generate a hierarchical structure out of it.
 */
public class TaxonomyParser  extends DefaultHandler
{
	/**
	 *  Pre-processing of XML:
	 *  Please note, before sending XML file to parser, we need to pre-process it to avoid any
	 *  incompatibilities with the parser. For example, replace all the occurrences
	 *  of '&' to '&amp;', '>' to '&gt;', etc. We can also trim any extra white 
	 *  spaces in the node names. 
	 */

	private Node mCurrentNode = null;	// Current Node in question
	
	private static final String ROOT_TAG = "taxonomy";
	private static final String GENERAL_TAG = "node";
	private static final boolean VERBOSE = true;
	
	/**
	 * Method to create a Tree from given XML file
	 * 
	 * @param filename	XML file name
	 * @return			Generated tree
	 */
	public TaxonomyTree createTreeFromXML(String filename)
	{
		parseXML(filename);
		
		return TaxonomyTree.getTaxonomyTree();
	}
	
	/**
	 * Method to invoke parsing of the XML file
	 * 
	 * @param filename		XML file name
	 */
	private void parseXML(String filename)
	{
		// Create a factory instance and then the parser
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try
		{
			SAXParser parser = factory.newSAXParser();
			parser.parse(filename, this);
		} 
		catch (ParserConfigurationException | SAXException | IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void startElement(String uri, String localname, String qname, Attributes attributes) throws SAXException 
	{
		if(VERBOSE) System.out.println("Start Element : " + qname + "\t name : " + attributes.getValue("name"));
		
		// Get the TaxonomyTree Object	
		TaxonomyTree taxonomyTree = TaxonomyTree.getTaxonomyTree();

		if(qname.equalsIgnoreCase(ROOT_TAG))
		{
			// As it is the first XML tag, make RootNode the currently visiting node.
			mCurrentNode = taxonomyTree.getRootNode();
		}
		else if(qname.equalsIgnoreCase(GENERAL_TAG))
			mCurrentNode = taxonomyTree.createNode(attributes.getValue("name"), mCurrentNode);
	}
		
	@Override
	public void endElement(String uri, String localname, String qname) throws SAXException 
	{
		if (VERBOSE) System.out.println("End Element : " + qname);

		if(qname.equalsIgnoreCase(GENERAL_TAG))
		{
			// Setting this node as a child of it's parent.
			mCurrentNode.mParentNode.mChildNodeList.add(mCurrentNode);
			
			// Now let's go one level up.
			mCurrentNode = mCurrentNode.mParentNode;
		}
		else if(qname.equalsIgnoreCase(ROOT_TAG))
		{
			// Do nothing. We're actually done!
		}
	}
}
