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

/**
 * Class to parse the XML file and generate a hierarchical structure out of it.
 */
public class MovieCastParser  extends DefaultHandler
{
	/**
	 *  Pre-processing of XML:
	 *  Please note, before sending XML file to parser, we need to pre-process it to avoid any
	 *  incompatibilities with the parser. For example, replace all the occurrences
	 *  of '&' to '&amp;', '>' to '&gt;', etc. We can also trim any extra white 
	 *  spaces in the node names. 
	 */
	
	/**
	 * Method to create a Tree from given XML file
	 * 
	 * @param filename	XML file name
	 * @return			Generated tree
	 */
	public MovieCastTrie createTrieFromXML(String filename)
	{
		parseXML(filename);
		
		return MovieCastTrie.getMovieCastTrie();
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
		MovieCastTrie movieCastTrie = MovieCastTrie.getMovieCastTrie();

		if(qname.equalsIgnoreCase(MOVIE_TAG))
		{
			mCurrentMovieId = attributes.getValue("id");
		}
		else if(qname.equalsIgnoreCase(CAST_TAG))
		{
			String castName = attributes.getValue("name");
			movieCastTrie.addCast(castName, Long.parseLong(mCurrentMovieId));
		}
	}
		
	@Override
	public void endElement(String uri, String localname, String qname) throws SAXException 
	{
	}
	
	// Member Variables
	private String mCurrentMovieId = null;
	
	private static final String ROOT_TAG = "moviecast";
	private static final String MOVIE_TAG = "movie";
	private static final String CAST_TAG = "cast";
}
