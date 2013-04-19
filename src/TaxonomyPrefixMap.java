import java.util.HashMap;
import java.util.Map;

/*
 * Sample Usage:
 * 
 * PrefixMap pmap = PrefixMap.getPrefixMap();
 * pmap.insert("Politics of Love", 1);
 * pmap.insert("Politics", 2);
 * System.out.println(pmap.retrieve("Politics of Love").getNodeId());
 * System.out.println(pmap.retrieve("Politics of Love").isLast());
 * System.out.println(pmap.retrieve("Politics of").getNodeId());
 * System.out.println(pmap.retrieve("Politics of").isLast());
 * System.out.println(pmap.retrieve("Politics").getNodeId());
 * System.out.println(pmap.retrieve("Politics").isLast());
 */

/**
 * Singleton class to generate a PrefixMap for the TaxonomyTree created using TaxonomyParser.
 */
public class TaxonomyPrefixMap
{
	/**
	 * Default Constructor
	 */
	private TaxonomyPrefixMap()
	{
		mMap = new HashMap<String, TaxonomyPrefixMapValue>();
	}
	
	/**
	 * Method to get the singleton PrefixMap object.
	 * @return	PrefixMap object
	 */
	public static TaxonomyPrefixMap getPrefixMap()
	{
		if(sTaxonomyPrefixmap == null)
			sTaxonomyPrefixmap = new TaxonomyPrefixMap();
		return sTaxonomyPrefixmap;
	}
	
	/**
	 * Method to insert a key-value pair in the map.
	 * 
	 * @param str		Key in the Prefix Map
	 * @param nodeId	Node Identifier
	 */
	public void insert(String str, long nodeId)
	{
		String[] tokens = str.split(AppConstants.TAXONOMY_DELIMITER_STRING);
		String key = "";
		
		// Iterating through all the tokens.
		for(int i = 0; i < tokens.length; i++)
		{
			if(i != 0)
				key += " ";
			key += tokens[i].trim();		
			
			TaxonomyPrefixMapValue value = null;
			long nodeIdValue = -1;
			boolean isLastValue = false;
			
			// If we reach the last token of the string, we're sure that a node with this key 
			// exists (with nodeID passed in the method). 
			if(i == (tokens.length - 1))
			{
				nodeIdValue = nodeId;
				isLastValue = true;
			}
			
			// If Map already contains this key, we can append the nodeID to the list.
			// TODO: We might need to change this if-block.
			if(mMap.containsKey(key))
			{
				value = mMap.get(key);
				mMap.remove(value);
				
				//If the existing name was shorter, don't touch nodeId set earlier, just set isLast to false.
				if(value.isLast())
					value.setLast(isLastValue);
				else //If it was longer, set the nodeId and exit.
					value.setNodeId(nodeIdValue);
			}
			else
			{
				value = new TaxonomyPrefixMapValue(nodeIdValue, isLastValue);
			}
			
			// Add <key, value> pair to the map.
			mMap.put(key, value);
		}
	}
	
	/**
	 * Method to retrieve prefix-map value (nodeID, isLast) for a given string.
	 * 
	 * @param key	A String
	 * @return		Prefix-Map Value (nodeID, isLast?)
	 */
	public TaxonomyPrefixMapValue retrieve(String key)
	{
		return mMap.get(key);
	}
	
	/**
	 * Method to print the Prefix Map.
	 */
	public void print()
	{
		if(mMap.size() > 0)
		{
			for (Map.Entry<String, TaxonomyPrefixMapValue> entry : mMap.entrySet())
			{
				System.out.println("<" + entry.getValue().getNodeId() + ", " + entry.getValue().isLast()
										+ "> \t - [" + entry.getKey() + "]");
			}
		}
		else
			System.out.println("No entries in the Prefix Map!");
	}
	
	// Member Variables
	private static TaxonomyPrefixMap sTaxonomyPrefixmap;
	private Map<String, TaxonomyPrefixMapValue> mMap;
	
}