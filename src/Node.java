import java.util.ArrayList;

public class Node
{
	/**
	 * Constructor
	 * 
	 * @param name			Node name
	 * @param parentNode	Parent Node
	 * @param nodeID		Node Identifier
	 */
	public Node (String name, Node parentNode, long nodeID)
	{
		mName = name;
		mNodeID = nodeID;
		mParentNode = parentNode;
		mChildNodeList  = new ArrayList<Node>();
	}
	
	/**
	 * Method to print a two-level tree. This method can 
	 * work for our case as we have just two-level taxonomy.
	 */
	public void printTwoLevelTree()
	{
		System.out.println("Printing tree beneath the node with value = "  + mName);
		System.out.println("=================================================");
		
		for (Node node : mChildNodeList)
		{
			System.out.println("\t[" + node.mNodeID + "] " + node.mName);
			System.out.println("\t-----------");
			
			for (Node childNode : node.mChildNodeList)
				System.out.println("\t\t[" + childNode.mNodeID + "] " + childNode.mName);
		}
	}
	
	// Member Variables -- Keeping all public for now.
	public ArrayList<Node> mChildNodeList = null;	// List of child nodes
	public Node mParentNode = null;					// Parent node ('null' for Root Node)
	public String mName = null;						// Node Value
	public long mNodeID = -1;						// NodeID
}
