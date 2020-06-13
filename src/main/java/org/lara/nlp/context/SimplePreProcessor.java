package org.lara.nlp.context;

import org.deeplearning4j.text.sentenceiterator.*;

class SimplePreProcessor implements SentencePreProcessor {
	public String preProcess(String sentence) {
		return "<start>" + sentence + "<end>";
	}
}