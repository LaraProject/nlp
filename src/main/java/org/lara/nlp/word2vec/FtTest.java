package org.lara.nlp.word2vec;

import java.util.ArrayList;
import org.lara.nlp.word2vec.Ft;
import org.apache.commons.cli.*;
import org.lara.nlp.OptionUtils;

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
		Option customIteratorOption = Option.builder("useCustomIterator") 
				.desc("Use SimpleLineIterator custom iterator") 
				.required(false) 
				.build();
		options.addOption(customIteratorOption);

        // Help function
        OptionUtils.help("FtTest", options, args);

        CommandLine line = OptionUtils.parseArgs(options, args);
        String export_path = line.getOptionValue("export");
		boolean useCustomIterator = line.hasOption("useCustomIterator");

		try {
			Ft ft = new Ft();
			if (useCustomIterator) {
				System.out.println("FtTest: creating the fasttext object with custom iterator ...");
				ft = new Ft(line, useCustomIterator);
			} else {
				System.out.println("FtTest: creating the fasttext object...");
				ft = new Ft(line);
			}
			System.out.println("FtTest: export fasttext model to " + export_path);
			ft.write_vectors(export_path);
        } catch (Exception e) {
            System.err.println("FtTest: problem when creating Ft object");
            e.printStackTrace();
            System.exit(3);
        }
	}	
}