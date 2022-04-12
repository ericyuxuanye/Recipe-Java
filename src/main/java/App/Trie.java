package App;

import java.util.HashMap;

/**
 * Bare minimum (not even!) implementation of a trie
 */
public class Trie {

	private static class TrieNode {
		HashMap<Character, TrieNode> letters = new HashMap<>();
		boolean isEnd = false;
		void addChild(String s, int index) {
			if (index == s.length()) {
				isEnd = true;
				return;
			}
			Character nextIndex = s.charAt(index);
			TrieNode next = letters.get(nextIndex);
			if (next == null) {
				next = new TrieNode();
				letters.put(nextIndex, next);
			}
			next.addChild(s, index + 1);
		}
		boolean find(String s, int index) {
			if (index == s.length()) {
				return isEnd;
			}
			if (isEnd) {
				return true;
			}
			TrieNode next = letters.get(s.charAt(index));
			if (next == null) {
				return false;
			}
			return next.find(s, index + 1);
		}
	}

	// the TrieNode we start with
	private TrieNode head = new TrieNode();

	/**
	 * Creates a trie of the keywords
	 * @param  keywords  the keywords to store in trie
	 */
	public Trie(String[] keywords) {
		for (String keyword : keywords) {
			head.addChild(keyword, 0);
		}
	}

	/**
	* returns whether the trie contains a substring of s
	* @param  s  The string to search
	* @return whether the string contains a substring of the given String
	*/
	public boolean contains(String s) {
		int sz = s.length();
		for (int i = 0; i < sz; i++) {
			if (head.find(s, i)) {
				return true;
			}
		}
		return false;
	}
}
