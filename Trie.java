class TrieNode 
{
    TrieNode[] arr;
	public int id;
    public TrieNode() 
	{
        this.arr = new TrieNode[26];
		id = -1;
    } 
}
 
public class Trie 
{
    private TrieNode root;
	private TrieNode searchingNode;
	private int num;
	
    public Trie() 
	{
        root = new TrieNode();
		num = 0;
    }
 
    // Inserts a word into the trie.
    public void insert(String word) 
	{
		int index;
		char c;
		if (search(word) >= 0) return;
        TrieNode p = root;
        for(int i = 0; i < word.length(); i++)
		{
            c = word.charAt(i);
            index = c -'a';
            if (p.arr[index] == null)
			{
                TrieNode temp = new TrieNode();
                p.arr[index] = temp;
                p = temp;
            }
			else
                p = p.arr[index];
        }
        p.id = num;
		num++;
    }
 
    // Returns if the word is in the trie.
    public int search(String word) 
	{
        TrieNode p = searchNode(word);
        if(p == null)
            return -1;
        else
		{
            if(p.id >= 0)
                return p.id;
        }
        return -1;
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
	
	public int searching(char s)
	{
		int index = s - 'a', id;
		if (searchingNode == null)
			searchingNode = root;
		if (searchingNode.arr[index] != null)
		{
			searchingNode = searchingNode.arr[index];
			if (searchingNode.id >= 0)
			{
				id = searchingNode.id;
				searchingNode = root;		
				return id;
			}
			return -1;
		}
		searchingNode = root;
		return -2;
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
	
	public int size()
	{
		return num;
	}
	
	public void resetNode()
	{
		searchingNode = root;
	}
}