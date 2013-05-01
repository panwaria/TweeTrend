
public class TrieNode
{
	private TrieNode[] children;
	private double value = 0.0;
	private boolean isLeaf = false;
	
	public TrieNode()
	{
		this.children = new TrieNode[256];
	}
	
	public TrieNode getChild(char ch)
	{
		if(children[ch - 'a'] == null)
			children[ch - 'a'] = new TrieNode();
		return children[ch - 'a'];
	}
	
	public boolean isLeaf()
	{
		return isLeaf;
	}
	
	public void setLeaf(boolean isLeaf)
	{
		this.isLeaf = isLeaf;
	}

	public boolean hasChild(char ch)
	{
		if(children[ch - 'a'] == null)
			return false;
		return true;
	}
	
	public double getValue()
	{
		return value;
	}

	public void setValue(double value)
	{
		this.value = value;
	}
}
