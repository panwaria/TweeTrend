/**
 * Class to represent the Value of the PrefixMap.
 */
public class TaxonomyPrefixMapValue
{
	private long mNodeID;			// Node Identifier
	private boolean mIsLast;		// Should we look for other strings as prefixes in the map?
	
	/**
	 * Constructor
	 * 
	 * @param nodeId	// Node Identifier
	 * @param isLast	// Should we look for other strings as prefixes in the map?
	 */
	public TaxonomyPrefixMapValue(long nodeId, boolean isLast)
	{
		this.mNodeID = nodeId;
		this.mIsLast = isLast;
	}
	
	// Getter & Setter for NodeID
	public long getNodeId() { return mNodeID; }
	public void setNodeId(long nodeId) { this.mNodeID = nodeId; }
	
	// Getter & Setter for isLast
	public boolean isLast() { return mIsLast; }
	public void setLast(boolean isLast)	{ this.mIsLast = isLast; }
	
}
