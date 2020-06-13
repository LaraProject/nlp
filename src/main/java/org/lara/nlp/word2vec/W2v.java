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
import org.apache.commons.cli.*;
import org.lara.nlp.context.SimpleLineIterator;
import org.lara.nlp.context.SimplePreProcessor;

public class W2v {
	// Structure
	private Word2Vec vec;
	// Parameters
	int minWordFrequency = 20;
	int dimension = 100;
	int window_size = 5;
	int epochs = 5;
	int iterations = 5;
	int workers = 2;
	int batch_size = 512;

	// Constructor
	public W2v(ArrayList<String> words, int minWordFrequency, int iterations, int epochs, int dimension, int workers, int batch_size, int window_size) {
		// Iterator
		SentenceIterator iter = new CollectionSentenceIterator(words);
		new W2v(iter, minWordFrequency, iterations, epochs, dimension, workers, batch_size, window_size);
	}

	public W2v(String input_path, int minWordFrequency, int iterations, int epochs, int dimension, int workers, int batch_size, int window_size) throws Exception {
		// Iterator
		SentenceIterator iter = new SimpleLineIterator(input_path);
		iter.setPreProcessor(new SimplePreProcessor());
		new W2v(iter, minWordFrequency, iterations, epochs, dimension, workers, batch_size, window_size);
	}

	public W2v(SentenceIterator iter, int minWordFrequency, int iterations, int epochs, int dimension, int workers, int batch_size, int window_size) {
		// Split on white spaces in the line to get words
		TokenizerFactory t = new DefaultTokenizerFactory();
		t.setTokenPreProcessor(new CommonPreprocessor());
		// Set parameters
		this.minWordFrequency = minWordFrequency;
		this.dimension = dimension;
		this.window_size = window_size;
		this.epochs = epochs;
		this.iterations = iterations;
		this.workers = workers;
		this.batch_size = batch_size;
		// Build NN
		vec = new Word2Vec.Builder()
			.minWordFrequency(minWordFrequency) // exclude words we can't build an accurate context for
			.iterations(iterations)
			.epochs(epochs)
			.layerSize(dimension) // the number of features in the word vector
			.seed(40)
			.windowSize(window_size) // rolling skip gram window size
			.iterate(iter) // the input sentences
			.tokenizerFactory(t) // the tokenizer
			.batchSize(batch_size)
			.workers(workers)
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
		WordVectorSerializer.writeWord2VecModel(vec, path);
	}

	// Export the model
	public Word2Vec getModel() {
		return vec;
	}

	// Get model from command line arguments
	public static Options getOptions() {

		Option minWordFrequencyOption = Option.builder("minWordFrequency") 
				.desc("Minimum number of occurence") 
				.hasArg(true) 
				.argName("num")
				.required(false) 
				.build();
		Option dimOption = Option.builder("dim") 
				.longOpt("dimension") 
				.desc("Dimension of the vectors") 
				.hasArg(true) 
				.argName("size") 
				.required(false) 
				.build();
		Option windowSizeOption = Option.builder("window") 
				.desc("Size of the window for Word2Vec") 
				.hasArg(true) 
				.argName("size") 
				.required(false) 
				.build();
		Option epochsOption = Option.builder("epochs") 
				.desc("Number of epochs for Word2Vec") 
				.hasArg(true) 
				.argName("num") 
				.required(false) 
				.build();
		Option iterationsOption = Option.builder("iterations") 
				.desc("Number of epochs for Word2Vec") 
				.hasArg(true) 
				.argName("num") 
				.required(false) 
				.build();
		Option workersOption = Option.builder("workers") 
				.desc("Number of threads to use") 
				.hasArg(true) 
				.argName("num") 
				.required(false)
				.build();
		Option batchSizeOption = Option.builder("batch_size") 
				.desc("Size of the batches") 
				.hasArg(true) 
				.argName("num") 
				.required(false)
				.build();
		Option simpleIteratorOption = Option.builder("useCustomIterator") 
				.desc("Use custom SimpleLineIterator iterator") 
				.required(false)
				.build();

		Options options = new Options();
		options.addOption(minWordFrequencyOption);
		options.addOption(dimOption);
		options.addOption(windowSizeOption);
		options.addOption(epochsOption);
		options.addOption(iterationsOption);
		options.addOption(workersOption);
		options.addOption(batchSizeOption);
		options.addOption(simpleIteratorOption);

		return options;
	}

	public void parse(Options options, String[] args)  throws ParseException {

		CommandLineParser parser = new DefaultParser();
		CommandLine line = parser.parse(options, args);

		String minWordFrequency_str = line.getOptionValue("minWordFrequency", "20");
		try {
			minWordFrequency =  Integer.valueOf(minWordFrequency_str);
		} catch (Exception e) {
			System.err.println("Bad parameter: minWordFrequency");
			System.exit(3);
		}

		String dimension_str = line.getOptionValue("dimension", "100");
		try {
			dimension =  Integer.valueOf(dimension_str);
		} catch (Exception e) {
			System.err.println("Bad parameter: dimension");
			System.exit(3);
		}

		String window_size_str = line.getOptionValue("window_size", "5");
		try {
			window_size =  Integer.valueOf(window_size_str);
		} catch (Exception e) {
			System.err.println("Bad parameter: window_size");
			System.exit(3);
		}


		String epochs_str = line.getOptionValue("epochs", "5");
		try {
			epochs =  Integer.valueOf(epochs_str);
		} catch (Exception e) {
			System.err.println("Bad parameter: epochs");
			System.exit(3);
		}

		String iterations_str = line.getOptionValue("iterations", "5");
		try {
			iterations =  Integer.valueOf(iterations_str);
		} catch (Exception e) {
			System.err.println("Bad parameter: iterations");
			System.exit(3);
		}

		String workers_str = line.getOptionValue("workers", "2");
		try {
			workers =  Integer.valueOf(workers_str);
		} catch (Exception e) {
			System.err.println("Bad parameter: workers");
			System.exit(3);
		}

		String batch_size_str = line.getOptionValue("batch_size", "512");
		try {
			batch_size =  Integer.valueOf(batch_size_str);
		} catch (Exception e) {
			System.err.println("Bad parameter: batch_size");
			System.exit(3);
		}
	}

	public W2v(ArrayList<String> words, Options options, String[] args) throws ParseException {
		parse(options, args);
		new W2v(words, minWordFrequency, iterations, epochs, dimension, workers, batch_size, window_size);
	}

	public W2v(String input_path, Options options, String[] args) throws Exception {
		parse(options, args);
		new W2v(input_path, minWordFrequency, iterations, epochs, dimension, workers, batch_size, window_size);
	}
}