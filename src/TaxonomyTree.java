import java.util.ArrayList;
import java.util.List;


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
		mRootNode = new TaxonomyNode(ROOT_NODE_VALUE, null, mNodeCount++);
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
	public void setRootNode(TaxonomyNode rootNode) { mRootNode = rootNode; }
	public TaxonomyNode getRootNode() { return mRootNode; }
	
	/**
	 * A Utility method to create a node in the tree.
	 * 
	 * @param name			Node Name
	 * @param parentNode	ParentNode
	 * @return				Created Node
	 */
	public TaxonomyNode createNode(String name, TaxonomyNode parentNode)
	{
		return new TaxonomyNode(name, parentNode, mNodeCount++);
	}
	
	/**
	 * Method to print all nodes in the tree.
	 */
	public void printTree()
	{
		System.out.println("\n--------------------------\nPRINTING TAXONOMY TREE\n--------------------------\n");

		if(mRootNode != null)
			mRootNode.printTwoLevelTree();
		else
			System.out.println("Tree has no nodes yet!");
	}
	
	/**
	 * Method to create a Prefix Map of the Taxonomy Tree.
	 */
	public TaxonomyPrefixMap createPrefixMap()
	{
		TaxonomyPrefixMap prefixMap = TaxonomyPrefixMap.getPrefixMap();
		
		List<TaxonomyNode> nodeQueue = new ArrayList<TaxonomyNode>();
		nodeQueue.add(mRootNode);
				
		while(!nodeQueue.isEmpty())
		{
			TaxonomyNode currentNode = nodeQueue.remove(0);
			prefixMap.insert(currentNode.mNodeName, currentNode.mNodeID);
			
			if(currentNode.mChildNodeList.size() > 0)
			{
				for(TaxonomyNode node : currentNode.mChildNodeList)
					nodeQueue.add(node);
			}
		}
		
		return prefixMap;
	}
	
	/**
	 * Method to print all nodes in the tree.
	 */
	public void printPrefixMap()
	{
		System.out.println("\n--------------------------\nPRINTING TAXONOMY PREFIX MAP\n--------------------------\n");

		if(mRootNode != null)
			TaxonomyPrefixMap.getPrefixMap().print();
		else
			System.out.println("Tree has no nodes yet!");
	}
	
	// Member variables
	private static TaxonomyTree sTaxonomyTree;		// Singleton TaxonomyTree Object
	private TaxonomyNode mRootNode = null;					// Root Node of the tree
	public long mNodeCount = 0;						// Cout of number of nodes in the tree.
	
	private static final String ROOT_NODE_VALUE = "ROOT";
}
