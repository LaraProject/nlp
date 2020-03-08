package org.lara.nlp;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import java.util.ArrayList;
import de.biomedical_imaging.edu.wlu.cs.levy.CG.KDTree;

class Dj4jWord2Vec extends Word {
	// Structure
	private Word2Vec vec;
	private SentenceIterator iter;

	// Constructor
	public Dj4jWord2Vec(ArrayList<String> words, Integer minWordFrequency, Integer iterations, Integer epochs, Integer dimension) {
		this.words = words;
		kd = new KDTree<String>(dimension);
		iter = new CollectionSentenceIterator(words);
		iter.setPreProcessor(new SentencePreProcessor() {
            @Override
            public String preProcess(String sentence) {
                return sentence.toLowerCase();
            }
        });
        // Split on white spaces in the line to get words
		TokenizerFactory t = new DefaultTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());
        vec = new Word2Vec.Builder()
                .minWordFrequency(minWordFrequency)  // exclude words we can't build an accurate context for
                .iterations(iterations)
                .epochs(epochs)
                .layerSize(dimension)  // the number of features in the word vector
                .seed(42)
                .windowSize(5)   // rolling skip gram window size
                .iterate(iter)   // the input sentences
                .tokenizerFactory(t)  // the tokenizer
                .build();
	}

	// Constructor with a given model
	public Dj4jWord2Vec(String model_path) {
		vec = WordVectorSerializer.readWord2VecModel(model_path);
	}

	// Initialiaze
	public void init() {
        vec.fit();
        initKD();
	}

	// Output to a file
	public void write_file(String path) throws Exception {
		WordVectorSerializer.writeWordVectors(vec, path);
	}

	// Output model to a file
	public void save_model(String path) throws Exception {
		WordVectorSerializer.writeWord2VecModel(vec, path);
	}

	// Convert a word to a vector
	public double[] word2vec(String word) {
		return vec.getWordVector(word);
	}
}