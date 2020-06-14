package org.lara.nlp.context;

import org.lara.nlp.context.Cornell;
import org.apache.commons.cli.*;
import org.lara.nlp.OptionUtils;

class CornellTest {
	public static void main(String[] args) throws Exception {
		// Arguments
		Options options = Cornell.getOptions();
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
        OptionUtils.help("CornellTest", options, args);

        CommandLine line = OptionUtils.parseArgs(options, args);
        String export_path = line.getOptionValue("export");
        String lines_path = line.getOptionValue("lines_file");
        String conversations_path = line.getOptionValue("conversations_file");
        int add_unk = OptionUtils.getOptionValue(line, "add_unk", 0);

		// Cornell data
		Cornell context = new Cornell(line);
		System.out.println("CornellTest: initializing from " + lines_path + " and " + conversations_path + "...");
		context.init();
		System.out.println("CornellTest: cleaning text...");
		context.cleaning();
		System.out.println("CornellTest: exporting ... ");
		context.exportData(export_path, add_unk);
	}
}