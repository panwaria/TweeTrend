
public class MainClass
{
	/**
	 * Main method to invoke the parson code.
	 * @param args
	 */
	public static void main(String[] args)
	{
		// Prepare Taxonomy Data Strcutures - Tree + PrefixMap
		prepareTaxonomyIndex();
		
		// Read Twitter Data and Process it.
		//TweetReader.read();		
	}
	
	private static void prepareTaxonomyIndex()
	{
		// Create a Taxonomy Parser.
		TaxonomyParser taxonomyParser = new TaxonomyParser();
		
		// Generate a tree structure from XML file.
		TaxonomyTree taxonomyTree = taxonomyParser.createTreeFromXML(AppConstants.TAXONOMY_SOURCE_XML);
		
		// [TESTING] Print the Tree.
		taxonomyTree.printTree();
		
		// [TESTING] Print the TaxonomyNodeHashMap
		taxonomyTree.printTaxonomyNodeHashMap();
		
		// Next Step: Create a Prefix Map.
		taxonomyTree.createPrefixMap();
		
		// [TESTING] Print the Prefix Map.
		taxonomyTree.printPrefixMap();
	}
	
}
