import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class GoldenDataMentionsExtractor extends DefaultHandler
{
	private static final String TWEET_TAG = "tweet";
	private static final String MENTION_TAG = "mention";
	
	public static void main(String[] args)
	{
		String filename = "golden_data_in_use\\golden_tweets_saurabh.xml";
		GoldenDataMentionsExtractor extractor = new GoldenDataMentionsExtractor();
		extractor.parseXML(filename);
	}
	
	public TaxonomyTree createTreeFromXML(String filename)
	{
		parseXML(filename);
		
		return TaxonomyTree.getTaxonomyTree();
	}
	
	private void parseXML(String filename)
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try
		{
			SAXParser parser = factory.newSAXParser();
			
			File file = new File(filename);
			InputStream inputStream= new FileInputStream(file);
			Reader reader = new InputStreamReader(inputStream,"UTF-8");
			 
			InputSource is = new InputSource(reader);
			is.setEncoding("UTF-8");
			 
			parser.parse(is, this);
			
		} 
		catch (ParserConfigurationException | SAXException | IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void startElement(String uri, String localname, String qname, Attributes attributes) throws SAXException 
	{
		System.out.println("<start: " + qname + ">");
	}
		
	@Override
	public void endElement(String uri, String localname, String qname) throws SAXException 
	{
		System.out.println("</end: " + qname + ">" + "\n");
	}
	/*
	public void characters( char[] data, int start, int length )
	{
		System.out.println("data = " + new String(data));
	}
	*/
}
