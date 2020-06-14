package org.lara.nlp.word2vec;

import java.util.ArrayList;
import org.lara.nlp.context.Cornell;
import org.lara.nlp.word2vec.W2v;
import org.apache.commons.cli.*;
import org.lara.nlp.OptionUtils;

class W2vTest {
	public static void main(String[] args) throws Exception {
		// Arguments
		Options options = Cornell.getOptions();
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

        // Help function
        OptionUtils.help("W2vTest", options, args);

        CommandLine line = OptionUtils.parseArgs(options, args);
        String export_path = line.getOptionValue("export");
        String lines_filename = line.getOptionValue("lines_file");
        String conversations_filename = line.getOptionValue("conversations_file");
        int minWordFrequency = OptionUtils.getOptionValue(line, "minWordFrequency", 20);

		// Cornell data
		Cornell context = new Cornell(line);
		System.out.println("W2vTest: initializing from " + lines_filename + " and " + conversations_filename + "...");
		context.init();
		System.out.println("W2vTest: cleaning text...");
		context.cleaning();
		System.out.println("W2vTest: tokenizing...");
		context.tokenize();

		// Word2Vec
		ArrayList<String> allWords = new ArrayList<String>();
		allWords.addAll(context.questions);
		allWords.addAll(context.answers);
		for (int i = 0; i < (minWordFrequency*2); i++) {
			allWords.add("<UNK>");
		}
		System.out.println("W2vTest: creating the W2v object...");
		try {
			W2v w2v = new W2v(allWords, line);
			System.out.println("W2vTest: export W2v model to " + export_path);
			w2v.write_vectors(export_path);
		} catch (Exception e) {
            System.err.println("W2vTest: problem when creating W2v object");
            e.printStackTrace();
            System.exit(3);
		}
	}	
}