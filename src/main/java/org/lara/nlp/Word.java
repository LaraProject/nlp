package org.lara.nlp;

import de.biomedical_imaging.edu.wlu.cs.levy.CG.KDTree;
import java.util.ArrayList;

abstract class Word {
	// Structure
	public KDTree<String> kd;
	public ArrayList<String> words;

	// Methods
	public abstract double[] word2vec(String word);
	public abstract void init();

	// Initialize the kdtree
	public void initKD() {
		try {
			for (String w: words) {
				kd.insert(word2vec(w), w);
			}
		} catch (de.biomedical_imaging.edu.wlu.cs.levy.CG.KeyDuplicateException e) {
		} catch (de.biomedical_imaging.edu.wlu.cs.levy.CG.KeySizeException e) {
			System.err.println("Wrong key size");
		}
	}
	// Common methods
	public String vec2word(double[] vec) {
		try {
			return kd.nearest(vec);
		} catch (de.biomedical_imaging.edu.wlu.cs.levy.CG.KeySizeException e) {
			System.err.println("Wrong key size");
			return null;
		}
	}
}