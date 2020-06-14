package org.lara.nlp;

import org.apache.commons.cli.*;

public class OptionUtils {

	// Parse integers
	public static int getOptionValue(CommandLine line, String input, int default_value) {
        String arg_value = line.getOptionValue(input, Integer.toString(default_value));
        int val = default_value;
        try {
            val =  Integer.valueOf(arg_value);
        } catch (Exception e) {
            System.err.println("Bad parameter: " + input);
            System.exit(3);
        }
        return val;
	}

    // Parse a command line
    public static CommandLine parseArgs(Options options, String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse(options, args);
            return line;
        } catch (Exception e) {
            System.err.println("parseArgs: Couldn't parse");
            e.printStackTrace();
            System.exit(3);
            return null;
        }
    }

    // Help fonction
   public static void help(String name, Options options, String[] args) {
        Option helpFileOption = Option.builder("h") 
                .longOpt("help") 
                .desc("Show help message") 
                .build();
        Options firstOptions = new Options();
        firstOptions.addOption(helpFileOption);
        try {
            CommandLineParser firstParser = new DefaultParser();
            CommandLine firstLine = firstParser.parse(firstOptions, args, true);
            boolean helpMode = firstLine.hasOption("help");
            if (helpMode) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp(name, options, true);
                System.exit(0);
            }
        } catch (Exception e) {
            System.err.println("help: Couldn't parse");
            e.printStackTrace();
            System.exit(3);
        }
   }
}