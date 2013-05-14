import java.util.ArrayList;
import java.util.List;


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
	
	public void insertCast(String castName, Long taxonomyNodeId)
	{
		castName = castName.toLowerCase();
		char[] wordCharArray = castName.toCharArray();
		TrieNode curr = root;
		for(char ch : wordCharArray)
		{
			if(Character.isAlphabetic(ch) || Character.isDigit(ch))
			{
				curr = curr.getChild(ch);
			}
		}
		curr.getMovieNodes().add(taxonomyNodeId);
		curr.setLeaf(true);
	}
	
	public void insert(String word, Double value)
	{
		word = word.toLowerCase();
		char[] wordCharArray = word.toCharArray();
		TrieNode curr = root;
		for(char ch : wordCharArray)
		{
			if(Character.isAlphabetic(ch) || Character.isDigit(ch))
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
			if(Character.isAlphabetic(ch) || Character.isDigit(ch))
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
			if(((ch >= 'a') && (ch <= 'z')) || ((ch >= '0') && (ch <= '9')))
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
	
	public List<Long> getMovieNodeIdList(String word)
	{
		word = word.toLowerCase();
		char[] wordCharArray = word.toCharArray();
		TrieNode curr = root;
		for(char ch : wordCharArray)
		{
			if(Character.isAlphabetic(ch) || Character.isDigit(ch))
			{
				if(curr.hasChild(ch))
					curr = curr.getChild(ch);
				else
					return new ArrayList<Long>();
			}
			else
			{
				return new ArrayList<Long>();
			}
		}
		
		return curr.getMovieNodes();
	}
	
	public double get(String word)
	{
		word = word.toLowerCase();
		char[] wordCharArray = word.toCharArray();
		TrieNode curr = root;
		for(char ch : wordCharArray)
		{
			if(Character.isAlphabetic(ch) || Character.isDigit(ch))
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
