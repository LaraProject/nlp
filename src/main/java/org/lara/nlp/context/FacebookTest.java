package org.lara.nlp.context;

import org.lara.nlp.context.Facebook;
import org.apache.commons.cli.*;

class FacebookTest {
	public static void main(String[] args) throws Exception {
		// Arguments
		Options options = Facebook.getOptions();
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
			formatter.printHelp("FacebookTest", options, true);
			System.exit(0);
		}
 
        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(options, args);
        String export_path = line.getOptionValue("export");
        String input_path = line.getOptionValue("fb_json");

		// Cornell data
		Facebook context = new Facebook(options, args);
		System.out.println("FacebookTest: initializing from " + input_path + " ...");
		context.init();
		System.out.println("FacebookTest: cleaning text...");
		context.cleaning();
		System.out.println("FacebookTest: exporting ... ");
		context.exportData(export_path);
	}	
}
