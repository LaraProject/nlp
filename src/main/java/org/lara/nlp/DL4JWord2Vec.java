package org.lara.nlp;

import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import java.util.ArrayList;

class Dj4jWord2Vec {
	// Structure
	private Word2Vec vec;

	// Constructor
	public Dj4jWord2Vec(ArrayList < String > words, Integer minWordFrequency, Integer iterations, Integer epochs, Integer dimension) {
		// Iterator
		SentenceIterator iter = new CollectionSentenceIterator(words);
		// Split on white spaces in the line to get words
		TokenizerFactory t = new DefaultTokenizerFactory();
		t.setTokenPreProcessor(new CommonPreprocessor());
		vec = new Word2Vec.Builder()
			.minWordFrequency(minWordFrequency) // exclude words we can't build an accurate context for
			.iterations(iterations)
			.epochs(epochs)
			.layerSize(dimension) // the number of features in the word vector
			.seed(42)
			.windowSize(5) // rolling skip gram window size
			.iterate(iter) // the input sentences
			.tokenizerFactory(t) // the tokenizer
			.build();
		vec.fit();
	}

	// Constructor with a given model
	public Dj4jWord2Vec(String path) throws Exception {
		vec = WordVectorSerializer.readWord2VecModel(path);
	}
	// Output to a file
	public void write_vectors(String path) throws Exception {
		WordVectorSerializer.writeWordVectors(vec, path);
	}
	// Save the model
	public void save_model(String path) throws Exception {
		vec = WordVectorSerializer.readWord2VecModel(path);
	}
}