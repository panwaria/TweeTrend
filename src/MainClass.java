
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
		
		// Read Twitter Data and Process it.
		TweetProcessor tweetProcessor = new TweetProcessor(taxonomyTree, AppConstants.TWITTER_DATA_FILE);
		tweetProcessor.processTweets();		
        System.out.println("CHECKPOINT: Final Taxonomy Node Score Updated.");
	}
	
	private static TaxonomyTree prepareTaxonomyIndex()
	{
		// Create a Taxonomy Parser.
		TaxonomyParser taxonomyParser = new TaxonomyParser();
		
		// Generate a tree structure from XML file.
		TaxonomyTree taxonomyTree = taxonomyParser.createTreeFromXML(AppConstants.TAXONOMY_SOURCE_XML);
		System.out.println("CHECKPOINT: Taxonomy Tree Created.");
		
		// [TESTING] Print the Tree.
		taxonomyTree.printTree();
		
// 		// [TESTING] Print the TaxonomyNodeHashMap
//		taxonomyTree.printTaxonomyNodeHashMap();
		// [TESTING] Print the Taxonomy Node Name Array
		taxonomyTree.printTaxonomyNodeNameArray();
		
		// Next Step: Create a Prefix Map.
		taxonomyTree.createPrefixMap();
		System.out.println("CHECKPOINT: Taxonomy Prefix Map Generated.");
		
		// [TESTING] Print the Prefix Map.
		taxonomyTree.printPrefixMap();
		
		return taxonomyTree;
	}
	
}
