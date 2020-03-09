package org.lara.nlp;

import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.models.glove.Glove;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import java.util.ArrayList;

class DL4JGlove {
    // Structure
    Glove glove;

    public DL4JGlove(ArrayList < String > sentences) {
		// Creating SentenceIterator wrapping our training corpus
        SentenceIterator iter = new CollectionSentenceIterator(sentences);
		// Split on white spaces in the line to get words
		TokenizerFactory t = new DefaultTokenizerFactory();
		t.setTokenPreProcessor(new CommonPreprocessor());
		Glove glove = new Glove.Builder()
			.iterate(iter)
			.tokenizerFactory(t)
			.alpha(0.75)
			.learningRate(0.1)
			.epochs(25)
			.xMax(100)
			.batchSize(1000)
			.shuffle(true)
			.symmetric(true)
			.build();
		glove.fit();
    }

    // Get the the model
    public WordVectors getModel() {
        return glove;
    }
}