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

public class PrefixMap //Singleton Class
{
	private static PrefixMap prefixmap;
	
	private Map<String, PrefixMapValue> map;
	
	private PrefixMap()
	{
		map = new HashMap<String, PrefixMapValue>();
	}
	
	public static PrefixMap getPrefixMap()
	{
		if(prefixmap == null)
			prefixmap = new PrefixMap();
		return prefixmap;
	}
	
	public void insert(String str, int nodeId)
	{
		String[] tokens = str.split("\\s+");
		String key = "";
		for(int i = 0; i < tokens.length; i++)
		{
			if(i != 0)
				key += " ";
			key += tokens[i];
			PrefixMapValue value = null;
			int nodeIdValue = -1;
			boolean isLastValue = false;
			if(i == (tokens.length - 1))
			{
				nodeIdValue = nodeId;
				isLastValue = true;
			}
			if(map.containsKey(key))
			{
				value = map.get(key);
				map.remove(value);
				
				//If the existing name was shorter, don't touch nodeId set earlier, just set isLast to false.
				if(value.isLast())
					value.setLast(isLastValue);
				else //If it was longer, set the nodeId and exit.
					value.setNodeId(nodeIdValue);
			}
			else
			{
				value = new PrefixMapValue(nodeIdValue, isLastValue);
			}
			map.put(key, value);
		}
	}
	
	public PrefixMapValue retrieve(String key)
	{
		return map.get(key);
	}
}