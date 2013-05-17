import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;


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
		prepareMovieCastTrie(taxonomyTree);
		
		// Read Twitter Data and Process it.
		TweetProcessor tweetProcessor = new TweetProcessor(taxonomyTree, AppConstants.TWITTER_GOLDEN_DATA_FILE);
		Map<String, TaxonomyNodeScore> scoreMap = tweetProcessor.getTaxonomyNodeScoreMap();
        System.out.println("CHECKPOINT: Final Taxonomy Node Score Updated.");
        
        // Process User's Query
        processUserQuery(taxonomyTree, scoreMap);
        
	}
	
	private static MovieCastTrie prepareMovieCastTrie(TaxonomyTree taxonomyTree)
	{
		
		MovieCastCreator.writeToMovieCastXML(AppConstants.MOVIE_CAST_SOURCE_XML, taxonomyTree);
		
		// Generate a trie structure from XML file.
		MovieCastParser movieCastParser = new MovieCastParser();
		MovieCastTrie movieCastTrie = movieCastParser.createTrieFromXML(AppConstants.MOVIE_CAST_SOURCE_XML);
		
		System.out.println("CHECKPOINT: Movie Cast Trie Created.");
		
		return movieCastTrie;
	}
	
	private static TaxonomyTree prepareTaxonomyIndex()
	{
		// Generate taxonomy XML file from TMDB API.
		TaxonomyCreator.writeToTaxonomyXML(AppConstants.TAXONOMY_SOURCE_XML);
		
		// Create a Taxonomy Parser.
		TaxonomyParser taxonomyParser = new TaxonomyParser();
		
		// Generate a tree structure from XML file.
		TaxonomyTree taxonomyTree = taxonomyParser.createTreeFromXML(AppConstants.TAXONOMY_SOURCE_XML);
		taxonomyTree = taxonomyParser.createTreeFromXML(AppConstants.GOLDEN_TAXONOMY_SOURCE_XML);
		taxonomyTree = taxonomyParser.createTreeFromXML(AppConstants.GOLDEN_TAXONOMY_2_SOURCE_XML);
		
		System.out.println("CHECKPOINT: Taxonomy Tree Created.");
		
		// [TESTING] Print the Tree.
		taxonomyTree.printTree();
		
 		// [TESTING] Print the TaxonomyNodeHashMap
		//taxonomyTree.printTaxonomyNodeHashMap();
		// [TESTING] Print the Taxonomy Node Name Array
		//taxonomyTree.printTaxonomyNodeNameArray();
		
		// Next Step: Create a Prefix Map.
		taxonomyTree.createPrefixMap();
		System.out.println("CHECKPOINT: Taxonomy Prefix Map Generated.");
		
		// [TESTING] Print the Prefix Map.
		taxonomyTree.printPrefixMap();
		
		return taxonomyTree;
	}
	
	private static void processUserQuery(TaxonomyTree tree, Map<String, TaxonomyNodeScore> scoreMap)
	{
		ArrayList<TaxonomyNodeScoreClass> queryScoreArrayList = new ArrayList<TaxonomyNodeScoreClass>();

//		Scanner in = new Scanner(System.in);
//	    System.out.println("Enter a string: \t");
//	    String input = in.nextLine();
	    //System.out.println("You entered string "+ input);
	    
	    System.out.println("Resutls for ALL GENRES :");
	    TaxonomyNode rootNode = tree.getRootNode();
	    for(TaxonomyNode genreNode : rootNode.mChildNodeList)
	    {
	    	queryScoreArrayList.clear();
	    	
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
    	
	    boolean	nodeNameFound = true;	//It has to be true for genres extracted from TaxonomyTree
	    if(nodeNameFound)
	    {
	    	System.out.println("NodeName [" + input + "] found with NodeID=" + nodeID);
	    	
	    	// Get Node Object out of nodeID
	    	//tree.printTaxonomyNodeHashMap();
	    	
	    	Map<Integer, TaxonomyNode> nodeHashMap = tree.getTaxonomyNodeHashMap();
	    	TaxonomyNode node = null;
	    	if(nodeHashMap.containsKey(nodeID))
	    		node = (TaxonomyNode) nodeHashMap.get(nodeID);
	    	else
	    		System.out.println("\nSOMETHING BAD HAPPENED nodeID=" + nodeID);
	    	
	    	// Since it is just one-level tree for now
	    	for(TaxonomyNode childNode : node.mChildNodeList)
	    	{
	    		String childNodeName = childNode.mNodeName;
	    		if(scoreMap.containsKey(childNodeName))
	    			queryScoreArrayList.add(new TaxonomyNodeScoreClass(childNodeName, scoreMap.get(childNodeName)));
	    	 }
	    }
	    else
	    	AppUtils.println("NodeName [" + input + "] NOT FOUND");
    	
	    
	    if(queryScoreArrayList.size() == 0) continue;
	    
//	    System.out.println("-------------------------------------------");
//	    System.out.println("QueryScoreArrayList =" + input.toUpperCase());
//	    System.out.println("-------------------------------------------\n");
//	    for(TaxonomyNodeScoreClass queryScore : queryScoreArrayList)
//	    	System.out.println("[" + queryScore.mNodeName + ", " + queryScore.mTaxonomyNodeScore.mNodeScore + "]");

	    TaxonomyNodeScoreClass[] queryScoreArray = new TaxonomyNodeScoreClass[queryScoreArrayList.size()];
	    queryScoreArrayList.toArray(queryScoreArray);
	    Arrays.sort(queryScoreArray);
	    
//	    System.out.println("-------------------------------------------");
//	    System.out.println("QueryScoreArray =" + input.toUpperCase());
//	    System.out.println("-------------------------------------------\n");
//	    for(TaxonomyNodeScoreClass queryScore : queryScoreArray)
//	    	System.out.println("[" + queryScore.mNodeName + ", " + queryScore.mTaxonomyNodeScore.mNodeScore + "]");

	    int count = 1, subCount = 1, index = 0;
	    int mod = (int) Math.ceil(Math.min(AppConstants.MAX_TOP_LIMIT, queryScoreArray.length)/10.0);
	    double[] thresholdArr = new double[10];
	    for(TaxonomyNodeScoreClass queryScore : queryScoreArray)
		{
	    	if(subCount == mod)
	    	{
	    		subCount = 0;
	    		thresholdArr[index++] = queryScore.mTaxonomyNodeScore.mNodeScore;
	    	}
	    	
	    	if(count++ == AppConstants.MAX_TOP_LIMIT)
	    		break;
	    	
	    	subCount++;
		}
	    
	    System.out.println("-------------------------------------------");
	    System.out.println("RESULTS for Query = " + input.toUpperCase());
	    System.out.println("-------------------------------------------\n");

	    String htmlString = "";
		if(queryScoreArray.length > 0)
		{
			System.out.println("No. of movies in this category = " + queryScoreArray.length + "\n");
			System.out.println("But TOP (max limit = 30) are:\n");
			
			for(TaxonomyNodeScoreClass queryScore : queryScoreArrayList)
			{
//				System.out.println("[" + queryScore.mNodeName + ", " + queryScore.mTaxonomyNodeScore.mNodeScore + "]");
				
				int level = AppUtils.getTagCloudLevel(queryScore.mTaxonomyNodeScore.mNodeScore, thresholdArr);
				if (level == -1) continue;
				
				htmlString += AppConstants.OUTPUT_HTML_PART1 + level + AppConstants.OUTPUT_HTML_PART2 + 
						queryScore.mNodeName + AppConstants.OUTPUT_HTML_PART3;
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
