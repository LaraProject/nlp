package org.lara.nlp.context;

import org.deeplearning4j.text.sentenceiterator.*;

public class SimplePreProcessor implements SentencePreProcessor {
	public String preProcess(String sentence) {
		return "<start> " + sentence + " <end>";
	}
}