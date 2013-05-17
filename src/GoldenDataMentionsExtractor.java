import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class GoldenDataMentionsExtractor extends DefaultHandler
{
	public static void main(String[] args)
	{
		//String filename = "golden_data_in_use\\golden_tweets_saurabh.xml";
		String filename = "golden_data_in_use\\golden_tweets_prakhar.xml";
		GoldenDataMentionsExtractor extractor = new GoldenDataMentionsExtractor();
		extractor.parseXML(filename);
		//System.out.println(extractor.count);
		
		String[] theArray = new String[extractor.arraylist.size()];
		int i = 0;
		for(String str : extractor.arraylist)
			theArray[i++] = str;
		Arrays.sort(theArray);
		
		extractor.arraylist.clear();
		
		/*
		TheMovieDbApi tmdb = null;
		try
		{
			tmdb = new TheMovieDbApi(AppConstants.TMDB_API_KEY);
		} catch (MovieDbException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		ArrayList<String> allResults = new ArrayList<String>();
		for(int j = 0; j < theArray.length; j++)
		{
			if(j != 0)
			{
				if(theArray[j].equals(theArray[j-1]))
					continue;
			}
			/*
			if(theArray[j] == null)
				continue;
			if(theArray[j].equals(""))
				continue;
			*/
			System.out.println(theArray[j]);
			/*
			List<MovieDb> a = null;
			try
			{
				a = tmdb.searchMovie(theArray[j], 0, "", true, 0);
			} catch (MovieDbException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if((a != null) && (a.get(0) != null) && (a.get(0).getGenres() != null))
				System.out.println("\t" + a.get(0).getGenres().get(0));
			else
				System.out.println();
			*/
		}
		
	}
	
	public int count = 0;
	public List<String> arraylist = new ArrayList<String>();
	
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
		//System.out.println("<start: " + qname + ">");
		if(qname.equals("mention"))
		{
			String movieName = attributes.getValue("name").trim();
			//System.out.println(movieName);
			arraylist.add(movieName);
		}
	}
	
	@Override
	public void endElement(String uri, String localname, String qname) throws SAXException 
	{
		if(qname.equals("tweet"))
			count ++;
		//System.out.println("</end: " + qname + ">");
	}	
}
