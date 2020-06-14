package org.lara.nlp.context;

import org.lara.nlp.context.Facebook;
import org.apache.commons.cli.*;
import org.lara.nlp.OptionUtils;

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
		Option unkOption = Option.builder("add_unk") 
                .desc("Number of unknown tokens to add") 
                .hasArg(true) 
                .argName("num") 
                .required(false) 
                .build();
        options.addOption(unkOption);

        // Help function
        OptionUtils.help("FacebookTest", options, args);
 
        CommandLine line = OptionUtils.parseArgs(options, args);
        String export_path = line.getOptionValue("export");
        String input_path = line.getOptionValue("fb_json");
        int add_unk = OptionUtils.getOptionValue(line, "add_unk", 0);

		// Cornell data
		Facebook context = new Facebook(line);
		System.out.println("FacebookTest: initializing from " + input_path + " ...");
		context.init();
		System.out.println("FacebookTest: cleaning text...");
		context.cleaning();
		System.out.println("FacebookTest: exporting ... ");
		context.exportData(export_path, add_unk);
	}	
}
