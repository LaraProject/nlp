package org.lara.nlp;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import de.biomedical_imaging.edu.wlu.cs.levy.CG.KDTree;

class HotWord extends Word {
	// Structure
	private String alphabet;
	private Integer length_alphabet;
	private HashMap<Character,Integer> char_index;

	// Constructor
	public HotWord(ArrayList<String> words, String alphabet) {
		this.alphabet = alphabet;
		this.length_alphabet = alphabet.length();
		this.words = words;
		kd = new KDTree<String>(length_alphabet);
		char_index = new HashMap<Character,Integer>();
	}

	// Initialize everything
	public void init() {
		initCharIndex();
		initKD();
	}

	// Initialize char indexes
	private void initCharIndex() {
		int count = 0;
		for (int i = 0; i < length_alphabet; i++) {
			char_index.put(alphabet.charAt(i),count);
			count++;
		}
	}

	// Implement word API
	public double[] word2vec(String word) {
		double[] ret = new double[length_alphabet];
		for (int i = 0; i < word.length(); i++) {
			System.out.println(word.charAt(i));
			int tmp = char_index.get(word.charAt(i));
			ret[tmp] = 1.;
		}
		return ret;
	}
}