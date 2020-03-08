package org.lara.nlp;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

class HotWord extends Word {
	// Context
	ArrayList<String> words; 
	// Structure
	private HashMap<String,Integer> words_to_int;
	private HashMap<Integer,String> int_to_words;

	// Constructor
	public HotWord(ArrayList<String> words) {
		this.words = words;
		words_to_int = new HashMap<String,Integer>();
		int_to_words = new HashMap<Integer,String>();
	}

	// Initialise everything
	public void init() {
		getWordCount();
		inverseDictionnary();
	}

	// Creating a dictionary that maps each word to its number of occurrences
	private void getWordCount() {
		for (String s: words) {
			for (String w: s.split(" ")) {
				if (!(words_to_int.containsKey(w))) {
					words_to_int.put(w,1);
				} else {
					words_to_int.put(w,words_to_int.get(w)+1);
				}
			}
		}
	}

	// Create inverse dictionnaries
	private void inverseDictionnary() {
		for (Map.Entry<String, Integer> entry: words_to_int.entrySet()) {
			int_to_words.put(entry.getValue(), entry.getKey());
		}
	}

	// Implement core API
	public int word2int(String word) {
		return words_to_int.get(word);
	}
	public String int2word(Integer code) {
		return int_to_words.get(code);
	}
}