import java.util.ArrayList;

public class TaxonomyNode
{
	/**
	 * Constructor
	 * 
	 * @param name			Node name
	 * @param parentNode	Parent Node
	 * @param nodeID		Node Identifier
	 */
	public TaxonomyNode (String name, TaxonomyNode parentNode, long nodeID, String tmdbMovieId)
	{
		mNodeName = name;
		mNodeID = nodeID;
		mParentNode = parentNode;
		mChildNodeList  = new ArrayList<TaxonomyNode>();
		mTmdbMovieId = Integer.parseInt(tmdbMovieId);
	}
	
	/**
	 * Method to print a two-level tree. This method can 
	 * work for our case as we have just two-level taxonomy.
	 */
	public void printTwoLevelTree()
	{
		AppUtils.println("Printing tree beneath the node with value = "  + mNodeName);
		AppUtils.println("=================================================");
		
		for (TaxonomyNode node : mChildNodeList)
		{
			AppUtils.println("\t[" + node.mNodeID + "] " + node.mNodeName);
			AppUtils.println("\t-------------");
			
			for (TaxonomyNode childNode : node.mChildNodeList)
				AppUtils.println("\t\t|--> [" + childNode.mNodeID + "] " + childNode.mNodeName);
		}
	}
	
	// Member Variables -- Keeping all public for now.
	public ArrayList<TaxonomyNode> mChildNodeList = null;	// List of child nodes
	public TaxonomyNode mParentNode = null;					// Parent node ('null' for Root Node)
	public String mNodeName = null;							// Node Value
	public long mNodeID = -1;								// NodeID
	public int mTmdbMovieId = -1;
	public boolean mNodeVisited = false;
}
