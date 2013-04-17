
/**
 * Singleton class to represent the TaxonomyTree for the Taxonomy given in XML format.
 */
public class TaxonomyTree
{
	/**
	 * Default Constructor - Creating the Root Node of the tree.
	 */
	private TaxonomyTree()
	{
		mRootNode = new Node(ROOT_NODE_VALUE, null, mNodeCount++);
	}
	
	/**
	 * Method to get the Singleton TaxonomyTree Object.
	 * @return 	TaxonomyTree Object
	 */
	public static TaxonomyTree getTaxonomyTree()
	{
		if(sTaxonomyTree == null)
			sTaxonomyTree = new TaxonomyTree();
		return sTaxonomyTree;
	}
	
	// Getter and setter for Root Node
	public void setRootNode(Node rootNode) { mRootNode = rootNode; }
	public Node getRootNode() { return mRootNode; }
	
	/**
	 * A Utility method to create a node in the tree.
	 * 
	 * @param name			Node Name
	 * @param parentNode	ParentNode
	 * @return				Created Node
	 */
	public Node createNode(String name, Node parentNode)
	{
		return new Node(name, parentNode, mNodeCount++);
	}
	
	/**
	 * Method to print all nodes in the tree.
	 */
	public void print()
	{
		mRootNode.printTwoLevelTree();
	}
	
	// Member variables
	private static TaxonomyTree sTaxonomyTree;		// Singleton TaxonomyTree Object
	private Node mRootNode = null;					// Root Node of the tree
	public long mNodeCount = 0;						// Cout of number of nodes in the tree.
	
	private static final String ROOT_NODE_VALUE = "ROOT";
}
