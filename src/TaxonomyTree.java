import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
		mTaxonomyNodeHashMap = new HashMap<Long, TaxonomyNode>();
		mRootNode = createNode(ROOT_NODE_VALUE, null);
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
	public final TaxonomyNode getRootNode() { return mRootNode; }
	
	// Getter for TaxonomyNodeHashMap
	public final Map<Long, TaxonomyNode> getTaxonomyNodeHashMap() {	return mTaxonomyNodeHashMap; }
	
	/**
	 * A Utility method to create a node in the tree.
	 * 
	 * @param name			Node Name
	 * @param parentNode	ParentNode
	 * @return				Created Node
	 */
	public TaxonomyNode createNode(String name, TaxonomyNode parentNode)
	{
		// Increase the node count first.
		mNodeCount++;
		
		// Create the node.
		TaxonomyNode node = new TaxonomyNode(name, parentNode, mNodeCount);
		
		// Also push node into the hash map.
		mTaxonomyNodeHashMap.put(mNodeCount, node);
		
		return node;
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
		
		// Using Breadth First Traversal of the Tree to create PrefixMap
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
	
	/**
	 * Print the TaxonomyNodeHashMap
	 */
	public void printTaxonomyNodeHashMap()
	{
		System.out.println("\n--------------------------\nPRINTING TAXONOMYNODE HASH MAP\n--------------------------\n");

		if(mTaxonomyNodeHashMap.size() > 0)
		{
			for (Map.Entry<Long, TaxonomyNode> entry : mTaxonomyNodeHashMap.entrySet())
			{
				System.out.println("[" + entry.getKey() + ", " + entry.getValue().mNodeName + "]");
			}
		}
		else
			System.out.println("No entries in the TaxonomyNode Hash Map!");

	}
	
	// Member variables
	private static TaxonomyTree sTaxonomyTree;		// Singleton TaxonomyTree Object
	private TaxonomyNode mRootNode = null;			// Root Node of the tree
	private long mNodeCount = -1;					// Count of number of nodes in the tree
	private Map<Long, TaxonomyNode> mTaxonomyNodeHashMap = null;	// Mapping of NodeID to Node object
	
	private static final String ROOT_NODE_VALUE = "_ROOT_";
}
