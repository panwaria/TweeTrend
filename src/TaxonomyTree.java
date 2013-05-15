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
		mTaxonomyNodeHashMap = new HashMap<Integer, TaxonomyNode>();
		mTaxonomyNodeNameList = new ArrayList<String>();
		
		mRootNode = createNode(ROOT_NODE_VALUE, null, "-1");
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
	
	// Getter for TaxonomyNodeHashMap
	public Map<Integer, TaxonomyNode> getTaxonomyNodeHashMap() {	return mTaxonomyNodeHashMap; }
	
	// Getter for TaxonomyNodeNameArray
	public String[] getTaxonomyNodeNameArray() 
	{
		String[] taxonomyNodeNameArray = mTaxonomyNodeNameList.toArray(new String[mTaxonomyNodeNameList.size()]);
		return taxonomyNodeNameArray;
	}
	
	public TaxonomyNode createNode(String name, TaxonomyNode parentNode, String tmdbNodeId)
	{
		// Increase the node count first.
		mNodeCount++;
		
		// Create the node.
		TaxonomyNode node = new TaxonomyNode(name, parentNode, mNodeCount, tmdbNodeId);
		
		// Also push node into the hash map.
		mTaxonomyNodeHashMap.put(mNodeCount, node);
		mTaxonomyNodeNameList.add(name);
		
		return node;
	}
	
	/**
	 * Method to print all nodes in the tree.
	 */
	public void printTree()
	{
		AppUtils.printLog("TaxonomyTree", "\n--------------------------\nPRINTING TAXONOMY TREE\n--------------------------\n");

		if(mRootNode != null)
			mRootNode.printTwoLevelTree();
		else
			AppUtils.printLog("TaxonomyTree", "Tree has no nodes yet!");
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
		AppUtils.println("\n--------------------------\nPRINTING TAXONOMY PREFIX MAP\n--------------------------\n");

		if(mRootNode != null)
			TaxonomyPrefixMap.getPrefixMap().print();
		else
			AppUtils.println("Tree has no nodes yet!");
	}
	
	/**
	 * Print the TaxonomyNodeHashMap
	 */
	public void printTaxonomyNodeHashMap()
	{
		AppUtils.println("\n--------------------------\nPRINTING TAXONOMYNODE HASH MAP\n--------------------------\n");

		if(mTaxonomyNodeHashMap.size() > 0)
		{
			for (Map.Entry<Integer, TaxonomyNode> entry : mTaxonomyNodeHashMap.entrySet())
			{
				AppUtils.println("[" + entry.getKey() + ", " + entry.getValue().mNodeName + "]");
			}
		}
		else
			AppUtils.println("No entries in the TaxonomyNode Hash Map!\n");
	}
	
	/**
	 * Print the TaxonomyNodeNameArray
	 */
	public void printTaxonomyNodeNameArray()
	{
		AppUtils.println("\n--------------------------\nPRINTING TAXONOMY NODE NAME ARRAY\n--------------------------\n");

		String[] taxonomyNodeNameArray = getTaxonomyNodeNameArray();
		int length = taxonomyNodeNameArray.length;
		if(length > 0)
		{
			for (int i = 0; i < length; i++)
			{
				AppUtils.println("[" + i + "]\t" + taxonomyNodeNameArray[i]);
			}
		}
		else
			AppUtils.println("No entries in the Taxonomy Node Name Array!\n");
	}
	
	// Member variables
	private static TaxonomyTree sTaxonomyTree;		// Singleton TaxonomyTree Object
	private TaxonomyNode mRootNode = null;			// Root Node of the tree
	private int mNodeCount = -1;					// Count of number of nodes in the tree
	private Map<Integer, TaxonomyNode> mTaxonomyNodeHashMap = null;	// Mapping of NodeID to Node object
	
	private ArrayList<String> mTaxonomyNodeNameList = null;
	private static final String ROOT_NODE_VALUE = "_ROOT_";
}
