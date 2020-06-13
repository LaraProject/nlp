package org.lara.nlp.context;

import org.lara.nlp.context.Cornell;
import org.apache.commons.cli.*;

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
			formatter.printHelp("CornellTest", options, true);
			System.exit(0);
		}

        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(options, args);
        String export_path = line.getOptionValue("export");
        String lines_path = line.getOptionValue("lines_file");
        String conversations_path = line.getOptionValue("conversations_file");
        String add_unk_str = line.getOptionValue("add_unk", "0");
        int add_unk = 0;
        try {
            add_unk =  Integer.valueOf(add_unk_str);
        } catch (Exception e) {
            System.err.println("Bad parameter: add_unk");
            System.exit(3);
        }

		// Cornell data
		Cornell context = new Cornell(options, args);
		System.out.println("CornellTest: initializing from " + lines_path + " and " + conversations_path + "...");
		context.init();
		System.out.println("CornellTest: cleaning text...");
		context.cleaning();
		System.out.println("CornellTest: exporting ... ");
		context.exportData(export_path, add_unk);
	}
}