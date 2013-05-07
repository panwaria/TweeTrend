import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class MainClass
{
	/**
	 * Main method to invoke the parson code.
	 * @param args
	 */
	public static void main(String[] args)
	{
		// Prepare Taxonomy Data Structures - Tree + PrefixMap
		TaxonomyTree taxonomyTree = prepareTaxonomyIndex();
		
		// Prepare Movie Cast Trie
//		MovieCastTrie movieCastTrie = prepareMovieCastTrie(taxonomyTree);
		
		// Read Twitter Data and Process it.
		TweetProcessor tweetProcessor = new TweetProcessor(taxonomyTree, AppConstants.TWITTER_DATA_FILE);
		//tweetProcessor.processTweets();	
		Map<String, TaxonomyNodeScore> scoreMap = tweetProcessor.getTaxonomyNodeScoreMap();
        System.out.println("CHECKPOINT: Final Taxonomy Node Score Updated.");
        
        // Process User's Query
        processUserQuery(taxonomyTree, scoreMap);
        
	}
	
	private static MovieCastTrie prepareMovieCastTrie(TaxonomyTree taxonomyTree)
	{
		MovieCastTrie movieCastTrie = new MovieCastTrie();
		movieCastTrie.updateMovieCastTrie(taxonomyTree);
		
		return movieCastTrie;
	}
	
	private static TaxonomyTree prepareTaxonomyIndex()
	{
		// Generate taxonomy XML file from TMDB API.
		TaxonomyCreator.constructTaxonomyTree(AppConstants.TAXONOMY_SOURCE_XML);
		
		// Create a Taxonomy Parser.
		TaxonomyParser taxonomyParser = new TaxonomyParser();
		
		// Generate a tree structure from XML file.
		TaxonomyTree taxonomyTree = taxonomyParser.createTreeFromXML(AppConstants.TAXONOMY_SOURCE_XML);
		
		System.out.println("CHECKPOINT: Taxonomy Tree Created.");
		
		// [TESTING] Print the Tree.
		//taxonomyTree.printTree();
		
 		// [TESTING] Print the TaxonomyNodeHashMap
		//taxonomyTree.printTaxonomyNodeHashMap();
		// [TESTING] Print the Taxonomy Node Name Array
		//taxonomyTree.printTaxonomyNodeNameArray();
		
		// Next Step: Create a Prefix Map.
		taxonomyTree.createPrefixMap();
		System.out.println("CHECKPOINT: Taxonomy Prefix Map Generated.");
		
		// [TESTING] Print the Prefix Map.
		//taxonomyTree.printPrefixMap();
		
		return taxonomyTree;
	}
	
	private static void processUserQuery(TaxonomyTree tree, Map<String, TaxonomyNodeScore> scoreMap)
	{
		Map<String, TaxonomyNodeScore> queryScoreMap = new HashMap<String, TaxonomyNodeScore>();
	  
//		Scanner in = new Scanner(System.in);
//	    System.out.println("Enter a string: \t");
//	    String input = in.nextLine();
	    //System.out.println("You entered string "+ input);
	    
	    System.out.println("Resutls for ALL GENRES :");
	    TaxonomyNode rootNode = tree.getRootNode();
	    for(TaxonomyNode genreNode : rootNode.mChildNodeList)
	    {
	    	queryScoreMap.clear();
	    	/*
	    // Linear Search for now.
	    String[] nodeNameArray = tree.getTaxonomyNodeNameArray();
	    tree.printTaxonomyNodeNameArray();
	    System.out.println("Size of NodeNameArray = " + nodeNameArray.length);
	    
	    int nodeID = 0;
	    boolean nodeNameFound = false;
	    for(String nodeName : nodeNameArray)
	    {
	    	AppUtils.println("node["+ nodeID + "] = " + nodeName);
	    	if(input.toLowerCase().equals(nodeName.toLowerCase()))
	    	{
	    		nodeNameFound = true;
	    		break;
	    	}
	    	nodeID++;
	    }*/
	      
    	String input = genreNode.mNodeName;
    	int nodeID = (int) genreNode.mNodeID;
    	
	    boolean	nodeNameFound = true;
	    if(nodeNameFound)
	    {
	    	System.out.println("NodeName [" + input + "] found with NodeID=" + nodeID);
	    	
	    	// Get Node Object out of nodeID
	    	//tree.printTaxonomyNodeHashMap();
	    	
	    	Map<Integer, TaxonomyNode> nodeHashMap = tree.getTaxonomyNodeHashMap();
	    	TaxonomyNode node = null;
	    	if(nodeHashMap.containsKey(nodeID))
	    	{
	    		node = (TaxonomyNode) nodeHashMap.get(nodeID);
	    	}
	    	else
	    	{
	    		System.out.println("\nSOMETHING BAD HAPPENED nodeID=" + nodeID);
	    	}
	    	
	    	// Since it is just one-level tree for now
	    	for(TaxonomyNode childNode : node.mChildNodeList)
	    	{
	    		String childNodeName = childNode.mNodeName;
	    		if(scoreMap.containsKey(childNodeName))
	    		{
	    			queryScoreMap.put(childNode.mNodeName, scoreMap.get(childNodeName));
	    		}
	    	 }
	    }
	    else
	    	AppUtils.println("NodeName [" + input + "] NOT FOUND");
    	
	      
	    System.out.println("-------------------------------------------");
	    System.out.println("RESULTS for Query=" + input);
	    System.out.println("-------------------------------------------\n");

	    String htmlString = "";
		if(queryScoreMap.size() > 0)
		{
			System.out.println("No. of movies in this category = " + queryScoreMap.size() + "\n");
			for (Map.Entry<String, TaxonomyNodeScore> entry : queryScoreMap.entrySet())
			{
				System.out.println("[" + entry.getKey() + ", " + entry.getValue().mNodeScore + "]");
				
				int level = AppUtils.getTagCloudLevel(entry.getValue().mNodeScore);
				if (level == -1) continue;
				
				htmlString += AppConstants.OUTPUT_HTML_PART1 + level + AppConstants.OUTPUT_HTML_PART2 + 
							entry.getKey() + AppConstants.OUTPUT_HTML_PART3;
			}
			
			String finalString = AppConstants.OUTPUT_HTML_START_STRING + htmlString + AppConstants.OUTPUT_HTML_END_STRING;
			
			AppUtils.printToFile(AppConstants.OUTPUT_HTML_FILE_BASE_NAME + input +".html", finalString);
		}    
		else
			AppUtils.println("No entries in the User Query Score Map!\n");
		
	    }
		
//	    in.close();
	}
	
}
