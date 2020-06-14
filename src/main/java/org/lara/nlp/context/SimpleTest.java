package org.lara.nlp.context;

import org.lara.nlp.context.Simple;
import org.apache.commons.cli.*;
import org.lara.nlp.OptionUtils;

class SimpleTest {
	public static void main(String[] args) throws Exception {
		// Arguments
        Options options = Simple.getOptions();
		Option exportPathOption = Option.builder("export") 
                .desc("Specify the path to export") 
                .hasArg(true) 
                .argName("path") 
                .required(true) 
                .build();
        options.addOption(exportPathOption);
		Option unkOption = Option.builder("add_unk") 
                .desc("Number of unknown tokens to add") 
                .hasArg(true) 
                .argName("num") 
                .required(false) 
                .build();
        options.addOption(unkOption);

        // Help function
        OptionUtils.help("SimpleTest", options, args);

        CommandLine line = OptionUtils.parseArgs(options, args);
        String export_path = line.getOptionValue("export");
        String input_path = line.getOptionValue("conversations_file");
        int add_unk = OptionUtils.getOptionValue(line, "add_unk", 0);

		// Cornell data
		Simple context = new Simple(line);
		System.out.println("SimpleTest: initializing from " + input_path + " ...");
		context.init();
		System.out.println("SimpleTest: cleaning text...");
		context.cleaning();
		System.out.println("SimpleTest: exporting ... ");
		context.exportData(export_path, add_unk);
	}
}
