
public class PrefixMapValue
{
	private int nodeId;
	private boolean isLast;
	
	public PrefixMapValue(int nodeId, boolean isLast)
	{
		this.nodeId = nodeId;
		this.isLast = isLast;
	}
	
	public int getNodeId()
	{
		return nodeId;
	}
	public void setNodeId(int nodeId)
	{
		this.nodeId = nodeId;
	}
	public boolean isLast()
	{
		return isLast;
	}
	public void setLast(boolean isLast)
	{
		this.isLast = isLast;
	}
}
