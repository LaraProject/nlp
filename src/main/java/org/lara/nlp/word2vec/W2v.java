package org.lara.nlp.word2vec;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.factory.Nd4j;

public class W2v {
	// Structure
	private Word2Vec vec;

	// Constructor
	public W2v(ArrayList<String> words, int minWordFrequency, int iterations, int epochs,
		int dimension) {
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
	public W2v(String path) throws Exception {
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

	// Get the cosine similarity
	public double similarity(String word1, String word2) {
		return vec.similarity(word1, word2);
	}

	// Export the model
	public Word2Vec getModel() {
		return vec;
	}

	// Export embedding matrix to a numpy array
	public void exportEmbedding(String export_path) throws Exception {
		File export_file = new File(export_path);
		Nd4j.writeAsNumpy(vec.lookupTable().getWeights(), export_file);
	}

	/// Export all words
	public void exportWords(String export_path) throws Exception {
		FileWriter words_file = new FileWriter(export_path);
		for (VocabWord w : vec.vocab().vocabWords())
			words_file.write(w.getWord() + "\n");
		words_file.close();
	}
}