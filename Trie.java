class TrieNode 
{
    TrieNode[] arr;
    public boolean isEnd;
    // Initialize your data structure here.
    public TrieNode() 
	{
        this.arr = new TrieNode[26];
    } 
}
 
public class Trie 
{
    private TrieNode root;
	private TrieNode searchingNode;
    public Trie() 
	{
        root = new TrieNode();
    }
 
    // Inserts a word into the trie.
    public void insert(String word) 
	{
		if (search(word))
			return;
        TrieNode p = root;
        for(int i = 0; i < word.length(); i++)
		{
            char c = word.charAt(i);
            int index = c -'a';
            if (p.arr[index] == null)
			{
                TrieNode temp = new TrieNode();
                p.arr[index]=temp;
                p = temp;
            }
			else
                p = p.arr[index];
        }
        p.isEnd = true;
    }
 
    // Returns if the word is in the trie.
    public boolean search(String word) 
	{
        TrieNode p = searchNode(word);
        if(p == null)
            return false;
        else
		{
            if(p.isEnd)
                return true;
        }
        return false;
    }
 
    // Returns if there is any word in the trie
    // that starts with the given prefix.
    public boolean startsWith(String prefix) 
	{
        TrieNode p = searchNode(prefix);
        if(p == null)
            return false;
        return true;
    }
 
    public TrieNode searchNode(String s)
	{
        TrieNode p = root;
        for(int i = 0; i < s.length(); i++)
		{
            char c = s.charAt(i);
            int index = c - 'a';
            if(p.arr[index] != null)
                p = p.arr[index];
			else
                return null;
        }
        if(p == root)
            return null;
        return p;
    }
	public int searching(String s)
	{
		int index = s.charAt(0) - 'a';
		if (searchingNode == null)
			searchingNode = root;
		if (searchingNode.arr[index] != null)
		{
			searchingNode = searchingNode.arr[index];
			if (searchingNode.isEnd)
			{
				searchingNode = root;			
				return 1;
			}
			return 0;
		}
		searchingNode = root;
		return -1;
	}
	public String possibleNext()
	{
		String ans = "";
		if (searchingNode == null)
			searchingNode = root;
		for (int i = 0; i < 26; i++)
			if (searchingNode.arr[i] != null)
				ans += (char) (i + 'a');
		return ans;
	}
}