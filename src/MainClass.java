
public class MainClass
{
	/**
	 * Main method to invoke the parson code.
	 * @param args
	 */
	public static void main(String[] args)
	{
		// Create a Taxonomy Parser
		TaxonomyParser taxonomyParser = new TaxonomyParser();
		
		// Generate a tree strcuture from XML file.
		Node rootNode = taxonomyParser.createTreeFromXML("B:\\Codebase\\ws_cs784\\TweeTrend\\src\\Taxonomy_Movies.xml");
		
		// [TESTING] Print the Tree.
		rootNode.printTwoLevelTree();
	}

}
