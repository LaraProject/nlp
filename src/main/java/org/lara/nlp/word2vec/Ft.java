package org.lara.nlp.word2vec;

import org.deeplearning4j.models.fasttext.FastText;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.apache.commons.cli.*;
import java.io.File;

public class Ft {
	// Structure
	private FastText fasttext;

	// Constructor
	public Ft(String input_file, String output_file, boolean cbow, boolean skipgram, int min_count, int dim, int window_size, int epochs, int workers) {
		FastText fastText = FastText.builder()
						.cbow(cbow)
						.skipgram(skipgram)
						.minCount(min_count)
						.dim(dim)
						.contextWindowSize(window_size)
						.epochs(epochs)
						.numThreads(workers)
						.inputFile(input_file)
						.outputFile(output_file)
						.build();
        fasttext.fit();
	}

	// Constructor with a given model
	public Ft(String path) throws Exception {
		File saved_model = new File(path);
		fasttext = new FastText(saved_model);
	}

	// Output to a file
	public void write_vectors(String path) throws Exception {
		File saved_model = new File(path);
		WordVectorSerializer.writeWordVectors(fasttext, saved_model);
	}

	// Export the model
	public FastText getModel() {
		return fasttext;
	}


	// Get model from command line arguments
	public static Options getOptions() {

		Option cbowOption = Option.builder("cbow") 
				.desc("Enables CBOW algorithm") 
				.hasArg(false) 
				.required(false) 
				.build();
		Option skipgramOption = Option.builder("skipgram") 
				.desc("Enables skipgram algorithm") 
				.hasArg(false) 
				.required(false) 
				.build();
		Option minCountOption = Option.builder("min_count") 
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
				.desc("Size of the window for FastText") 
				.hasArg(true) 
				.argName("size") 
				.required(false) 
				.build();
		Option epochsOption = Option.builder("epochs") 
				.desc("Number of epochs for FastText") 
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
		Option inputFileOption = Option.builder("i") 
				.longOpt("input") 
				.desc("Input file") 
				.hasArg(true) 
				.argName("path") 
				.required(true) 
				.build();
		Option outputFileOption = Option.builder("o") 
				.longOpt("output") 
				.desc("Output file") 
				.hasArg(true) 
				.argName("path") 
				.required(true) 
				.build();

		Options options = new Options();
		options.addOption(cbowOption);
		options.addOption(skipgramOption);
		options.addOption(minCountOption);
		options.addOption(dimOption);
		options.addOption(windowSizeOption);
		options.addOption(epochsOption);
		options.addOption(workersOption);
		options.addOption(inputFileOption);
		options.addOption(outputFileOption);

		return options;
	}

	public Ft(Options options, String[] args) throws ParseException {

		CommandLineParser parser = new DefaultParser();
		CommandLine line = parser.parse(options, args);
		String input_file = line.getOptionValue("input");
		String output_file = line.getOptionValue("output");
		boolean cbow = line.hasOption("cbow");
		boolean skipgram = line.hasOption("skipgram");

		String min_count_str = line.getOptionValue("min_count", "20");
		int min_count = 20;
		try {
			min_count =  Integer.valueOf(min_count_str);
		} catch (Exception e) {
			System.err.println("Bad parameter: min_count");
			System.exit(3);
		}

		String dimension_str = line.getOptionValue("dimension", "100");
		int dimension = 100;
		try {
			dimension =  Integer.valueOf(dimension_str);
		} catch (Exception e) {
			System.err.println("Bad parameter: dimension");
			System.exit(3);
		}

		String window_size_str = line.getOptionValue("window_size", "5");
		int window_size = 5;
		try {
			window_size =  Integer.valueOf(window_size_str);
		} catch (Exception e) {
			System.err.println("Bad parameter: window_size");
			System.exit(3);
		}


		String epochs_str = line.getOptionValue("epochs", "5");
		int epochs = 5;
		try {
			epochs =  Integer.valueOf(epochs_str);
		} catch (Exception e) {
			System.err.println("Bad parameter: epochs");
			System.exit(3);
		}

		String workers_str = line.getOptionValue("workers", "2");
		int workers = 2;
		try {
			workers =  Integer.valueOf(workers_str);
		} catch (Exception e) {
			System.err.println("Bad parameter: workers");
			System.exit(3);
		}

		new Ft(input_file, output_file, cbow, skipgram, min_count, dimension, window_size, epochs, workers);
	}

}