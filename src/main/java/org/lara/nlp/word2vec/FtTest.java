package org.lara.nlp.word2vec;

import java.util.ArrayList;
import org.lara.nlp.word2vec.Ft;
import org.apache.commons.cli.*;

class FtTest {
	public static void main(String[] args) throws Exception {
		// Arguments
		Options options = Ft.getOptions();
		Option exportPathOption = Option.builder("export") 
                .desc("Specify the path to export") 
                .hasArg(true) 
                .argName("path") 
                .required(true) 
                .build();
        options.addOption(exportPathOption);

        // Help function
		Option helpFileOption = Option.builder("h") 
				.longOpt("help") 
				.desc("Show help message") 
				.build();
		Options firstOptions = new Options();
    	firstOptions.addOption(helpFileOption);
		CommandLineParser firstParser = new DefaultParser();
		CommandLine firstLine = firstParser.parse(firstOptions, args, true);
		boolean helpMode = firstLine.hasOption("help");
		if (helpMode) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("FtTest", options, true);
			System.exit(0);
		}

        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(options, args);
        String export_path = line.getOptionValue("export");

		System.out.println("FtTest: creating the fasttext object...");
		Ft ft = new Ft(options, args);
		System.out.println("FtTest: export fasttext model to " + export_path);
		ft.write_vectors(export_path);
	}	
}