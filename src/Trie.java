
public class Trie
{
	private TrieNode root;
	private double defaultValue = 1.0;
	
	public Trie()
	{
		root = new TrieNode();
	}
	
	public TrieNode getRoot()
	{
		return root;
	}
	
	public void insert(String word, double value)
	{
		word = word.toLowerCase();
		char[] wordCharArray = word.toCharArray();
		TrieNode curr = root;
		for(char ch : wordCharArray)
		{
			if(((ch - 'a') >= 0) && ((ch - 'a') < 26))
			{
				curr = curr.getChild(ch);
			}
		}
		curr.setValue(value);
		curr.setLeaf(true);
		
		defaultValue = Math.min(defaultValue, 1.0 - value);
	}
	
	public void insert(String word)
	{
		insert(word, 0.0);
	}
	
	public boolean contains(String word)
	{
		word = word.toLowerCase();
		char[] wordCharArray = word.toCharArray();
		TrieNode curr = root;
		for(char ch : wordCharArray)
		{
			if(((ch - 'a') >= 0) && ((ch - 'a') < 26))
			{
				if(curr.hasChild(ch))
					curr = curr.getChild(ch);
				else
					return false;
			}
		}
		
		return curr.isLeaf();
	}
	
	public boolean containsStrict(String word)
	{
		word = word.toLowerCase();
		char[] wordCharArray = word.toCharArray();
		TrieNode curr = root;
		for(char ch : wordCharArray)
		{
			if(((ch - 'a') >= 0) && ((ch - 'a') < 26))
			{
				if(curr.hasChild(ch))
					curr = curr.getChild(ch);
				else
					return false;
			}
			else
			{
				return false;
			}
		}
		
		return curr.isLeaf();
	}
	
	public double get(String word)
	{
		word = word.toLowerCase();
		char[] wordCharArray = word.toCharArray();
		TrieNode curr = root;
		for(char ch : wordCharArray)
		{
			if(((ch - 'a') >= 0) && ((ch - 'a') < 26))
			{
				if(curr.hasChild(ch))
					curr = curr.getChild(ch);
			}
		}
		
		return curr.getValue();
	}
	
	public static void main(String[] args)
	{
		Trie trie = new Trie();
		trie.insert("aAAa bb");
		System.out.println(trie.contains("aAAAbb"));
		System.out.println(trie.contains("abdd"));
	}

	public double getDefaultValue()
	{
		return defaultValue;
	}
}
