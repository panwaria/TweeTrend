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
	private Node mCurrentNode = null;	// Current Node in question
	private Node mRootNode = null;		// Root Node of the Tree
	
	private static final String ROOT_TAG = "taxonomy";
	private static final String GENERAL_TAG = "node";
	private static final boolean VERBOSE = false;
	
	/**
	 * Method to create a Tree from given XML file
	 * 
	 * @param filename	XML file name
	 * @return			Root Node of the generated tree
	 */
	public Node createTreeFromXML(String filename)
	{
		parseXML(filename);
		return mRootNode;
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
		
		if(qname.equalsIgnoreCase(ROOT_TAG))
		{
			mRootNode = new Node();
			mCurrentNode = mRootNode;
		}
		else if(qname.equalsIgnoreCase(GENERAL_TAG))
		{
			Node childNode = new Node(attributes.getValue("name"), mCurrentNode);
			mCurrentNode = childNode;
		}
	}
		
	@Override
	public void endElement(String uri, String localname, String qname) throws SAXException 
	{
		if (VERBOSE) System.out.println("End Element : " + qname);

		if(qname.equalsIgnoreCase(GENERAL_TAG))
		{
			// Setting this node as a child of it's parent.
			mCurrentNode.mParentNode.mChildNodeList.add(mCurrentNode);
			mCurrentNode = mCurrentNode.mParentNode;
		}
		else if(qname.equalsIgnoreCase(ROOT_TAG))
		{
			// Do nothing
		}
	}
}
