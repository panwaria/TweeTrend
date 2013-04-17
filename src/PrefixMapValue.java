/**
 * Class to represent the Value of the PrefixMap.
 */
public class PrefixMapValue
{
	private int nodeId;			// Node Identifier
	private boolean isLast;		// Should we look for other strings as prefixes in the map?
	
	/**
	 * Constructor
	 * 
	 * @param nodeId	// Node Identifier
	 * @param isLast	// Should we look for other strings as prefixes in the map?
	 */
	public PrefixMapValue(int nodeId, boolean isLast)
	{
		this.nodeId = nodeId;
		this.isLast = isLast;
	}
	
	// Getter & Setter for NodeID
	public int getNodeId() { return nodeId;	}
	public void setNodeId(int nodeId) {	this.nodeId = nodeId; }
	
	// Getter & Setter for isLast
	public boolean isLast() { return isLast; }
	public void setLast(boolean isLast)	{ this.isLast = isLast; }
	
}
