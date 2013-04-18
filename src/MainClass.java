
public class MainClass
{
	/**
	 * Main method to invoke the parson code.
	 * @param args
	 */
	public static void main(String[] args)
	{
		// Create a Taxonomy Parser.
		TaxonomyParser taxonomyParser = new TaxonomyParser();
		
		// Generate a tree structure from XML file.
		TaxonomyTree taxonomyTree = taxonomyParser.createTreeFromXML("B:\\Codebase\\ws_cs784\\TweeTrend\\src\\Taxonomy_Movies.xml");
		
		// [TESTING] Print the Tree.
		taxonomyTree.printTree();
		
		// Next Step: Create a Prefix Map.
		taxonomyTree.createPrefixMap();
		
		// [TESTING] Print the Prefix Map.
		taxonomyTree.printPrefixMap();
	}
}
