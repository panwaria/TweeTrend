import java.util.ArrayList;
import java.util.List;


public class TrieNode
{
	private TrieNode[] children;
	private boolean isLeaf = false;
	
	//For Go Words
	private double value = 0.0;
	
	//For Movie Casts
	private List<Long> movieNodes;
	
	public TrieNode()
	{
		this.children = new TrieNode[256];
		movieNodes = new ArrayList<Long>();
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
	
	public List<Long> getMovieNodes()
	{
		return movieNodes;
	}
}
