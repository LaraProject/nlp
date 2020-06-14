package org.lara.nlp.word2vec;

import org.deeplearning4j.models.fasttext.FastText;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.apache.commons.cli.*;
import java.io.*;
import org.lara.nlp.context.SimpleLineIterator;
import org.lara.nlp.context.SimplePreProcessor;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.lara.nlp.OptionUtils;

public class Ft {
	// Structure
	private FastText fasttext;

	// Constructor without custom iterator
	public Ft(String input_file, String output_file, boolean cbow, boolean skipgram, int min_count, int dim, int window_size, int epochs, int workers) {
		if (cbow && skipgram) {
			System.out.println("CBOW and skipgram cannot be enabled at the same time");
			System.exit(3);
		}
		if (!cbow && !skipgram) {
			System.out.println("CBOW and skipgram cannot be disabled at the same time");
			System.exit(3);
		}
		// Build model
		if (cbow)
			fasttext = FastText.builder()
							.cbow(true)
							.minCount(min_count)
							.dim(dim)
							.contextWindowSize(window_size)
							.epochs(epochs)
							.numThreads(workers)
							.inputFile(input_file)
							.outputFile(output_file)
							.build();
		else if (skipgram)
			fasttext = FastText.builder()
							.skipgram(true)
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

	// Constructor with custom iterator
	// Imported from https://github.com/eclipse/deeplearning4j/blob/master/deeplearning4j/deeplearning4j-nlp-parent/deeplearning4j-nlp/src/main/java/org/deeplearning4j/models/fasttext/FastText.java#L195
	public String loadIterator(SentenceIterator iterator) {
		if (iterator != null) {
			try {
				File tempFile = File.createTempFile("FTX", ".txt");
				BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
				while (iterator.hasNext()) {
					String sentence = iterator.nextSentence();
					writer.write(sentence);
				}
				return tempFile.getAbsolutePath();
			} catch (IOException e) {
           		System.err.println("Ft: Cannot create temp file");
            	e.printStackTrace();
            	System.exit(3);
            	return null;
			}
		}
		return null;
	}

	public Ft(String input_file, String output_file, boolean cbow, boolean skipgram, int min_count, int dim, int window_size, int epochs, int workers, boolean useCustomIterator) throws Exception {
		SentenceIterator iter = new SimpleLineIterator(input_file);
		iter.setPreProcessor(new SimplePreProcessor());
		if (cbow && skipgram) {
			System.out.println("CBOW and skipgram cannot be enabled at the same time");
			System.exit(3);
		}
		if (!cbow && !skipgram) {
			System.out.println("CBOW and skipgram cannot be disabled at the same time");
			System.exit(3);
		}
		// Build model
		if (cbow)
			fasttext = FastText.builder()
							.cbow(true)
							.minCount(min_count)
							.dim(dim)
							.contextWindowSize(window_size)
							.epochs(epochs)
							.numThreads(workers)
							.inputFile(loadIterator(iter))
							.outputFile(output_file)
							.build();
		else if (skipgram)
			fasttext = FastText.builder()
							.skipgram(true)
							.minCount(min_count)
							.dim(dim)
							.contextWindowSize(window_size)
							.epochs(epochs)
							.numThreads(workers)
							.inputFile(loadIterator(iter))
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

	public Ft() {
	}

	public Ft(CommandLine line, boolean useCustomIterator) throws Exception {
		this(line.getOptionValue("input"),
			line.getOptionValue("output"),
			line.hasOption("cbow"),
			line.hasOption("skipgram"),
			OptionUtils.getOptionValue(line, "min_count", 20),
			OptionUtils.getOptionValue(line, "dimension", 100),
			OptionUtils.getOptionValue(line, "window_size", 5),
			OptionUtils.getOptionValue(line, "epochs", 5),
			OptionUtils.getOptionValue(line, "workers", 2),
			true);
	}

	public Ft(CommandLine line) throws Exception {
		this(line.getOptionValue("input"),
			line.getOptionValue("output"),
			line.hasOption("cbow"),
			line.hasOption("skipgram"),
			OptionUtils.getOptionValue(line, "min_count", 20),
			OptionUtils.getOptionValue(line, "dimension", 100),
			OptionUtils.getOptionValue(line, "window_size", 5),
			OptionUtils.getOptionValue(line, "epochs", 5),
			OptionUtils.getOptionValue(line, "workers", 2));
	}
}