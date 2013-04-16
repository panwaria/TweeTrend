import java.util.ArrayList;

public class Node
{
	/**
	 * Default Constructor
	 */
	public Node() 
	{
		mName = new String (ROOT_NODE_VALUE);
		mParentNode = null;
		mChildNodeList  = new ArrayList<Node>();
	}
	
	/**
	 * Constructor
	 * @param name			Node name
	 * @param parentNode	Parent Node
	 */
	public Node (String name, Node parentNode)
	{
		mName = name;
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
			System.out.println("\t" + node.mName);
			System.out.println("\t-------");
			
			for (Node childNode : node.mChildNodeList)
				System.out.println("\t\t" + childNode.mName);
		}
	}
	
	// Member Variables
	public ArrayList<Node> mChildNodeList = null;	// List of child nodes
	public Node mParentNode = null;					// Parent node ('null' for Root Node)
	public String mName = null;						// Node Value
	
	private static final String ROOT_NODE_VALUE = "ROOT";
}
