package org.lara.nlp.word2vec;

import java.util.ArrayList;
import org.lara.nlp.context.Simple;
import org.lara.nlp.word2vec.W2v;
import org.apache.commons.cli.*;
import org.lara.nlp.OptionUtils;

class W2vSimpleTest {
	public static void main(String[] args) throws Exception {
		// Arguments
		Options options = Simple.getOptions();
		Options w2v_options = W2v.getOptions();
		for (Option op: w2v_options.getOptions()) {
			options.addOption(op);
		}
		Option exportPathOption = Option.builder("export") 
                .desc("Specify the path to export") 
                .hasArg(true) 
                .argName("path") 
                .required(true) 
                .build();
        options.addOption(exportPathOption);
		Option customIteratorOption = Option.builder("useCustomIterator") 
				.desc("Use SimpleLineIterator custom iterator") 
				.required(false) 
				.build();
		options.addOption(customIteratorOption);

        // Help function
        OptionUtils.help("W2vSimpleTest", options, args);

        CommandLine line = OptionUtils.parseArgs(options, args);
        String export_path = line.getOptionValue("export");
        String input_path = line.getOptionValue("conversations_file");
        boolean useCustomIterator = line.hasOption("useCustomIterator");
        int minWordFrequency = OptionUtils.getOptionValue(line, "minWordFrequency", 20);

		// Cornell data
		if (!useCustomIterator) {
			Simple context = new Simple(line);
			System.out.println("W2vSimpleTest: initializing from " + input_path + " ...");
			context.init();
			System.out.println("W2vSimpleTest: cleaning text...");
			context.cleaning();
			System.out.println("W2vSimpleTest: tokenizing...");
			context.tokenize();

			// Word2Vec
			ArrayList<String> allWords = new ArrayList<String>();
			allWords.addAll(context.questions);
			allWords.addAll(context.answers);
			for (int i = 0; i < (minWordFrequency*2); i++) {
				allWords.add("<UNK>");
			}
			System.out.println("W2vSimpleTest: creating the W2v object...");
			W2v w2v = new W2v(allWords, line);
			System.out.println("W2vSimpleTest: export W2v model to " + export_path);
			w2v.write_vectors(export_path);
		} else {
			System.out.println("W2vSimpleTest: creating the W2v object with custom iterator ...");
			W2v w2v = new W2v(input_path, line);
			System.out.println("W2vSimpleTest: export W2v model to " + export_path);
			w2v.write_vectors(export_path);
		}
	}	
}
