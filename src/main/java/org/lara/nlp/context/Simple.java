package org.lara.nlp.context;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import org.apache.commons.cli.*;

public class Simple extends Context {
	// Structure
	String filename;

	// Constructor
	public Simple(String filename, int min_length, int max_length) {
		this.filename = filename;
		questions = new ArrayList<String> ();
		answers = new ArrayList<String> ();
		this.min_length = min_length;
		this.max_length = max_length;
	}

	// Creating a dictionary that maps each line with its id
	private void get(String filename) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			int count_line = 0;
			boolean skip_next = false;
			while ((line = reader.readLine()) != null) {
				if (count_line % 2 == 0)
					if (line.length() > 11) {
						questions.add(line.substring(11));
					} else
						skip_next = true;
				else
					if (skip_next)
						skip_next = false;
					else
						if (line.length() > 9) {
							answers.add(line.substring(9));
						}
						else
							questions.remove(questions.size()-1);
				count_line = count_line + 1;
			}
			reader.close();
		} catch (Exception e) {
			System.err.format("Exception occurred trying to read '%s'.", filename);
			e.printStackTrace();
		}
	}

	public void init() {
		get(filename);
	}

    // Get context from command line arguments
    public static Options getOptions() {

        Option conversationsOption = Option.builder("conversations_file") 
                .desc("Specify the path to the conversations file") 
                .hasArg(true) 
                .argName("path") 
                .required(true) 
                .build();
        Option minLengthOption = Option.builder("min_length") 
                .desc("Minimum length of the sentences") 
                .hasArg(true) 
                .argName("size") 
                .required(false) 
                .build();
        Option maxLengthOption = Option.builder("max_length") 
                .desc("Maximum length of the sentences") 
                .hasArg(true) 
                .argName("size") 
                .required(false) 
                .build();

        Options options = new Options();
        options.addOption(conversationsOption);
        options.addOption(minLengthOption);
        options.addOption(maxLengthOption);

        return options;
    }

    public Simple(Options options, String[] args) throws ParseException {

        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(options, args);

        String filename = line.getOptionValue("conversations_file");

        String min_length_str = line.getOptionValue("min_length", "0");
        int min_length = 0;
        try {
            min_length =  Integer.valueOf(min_length_str);
        } catch (Exception e) {
            System.err.println("Bad parameter: min_length");
            System.exit(3);
        }

        String max_length_str = line.getOptionValue("max_length", "99999");
        int max_length = 99999;
        try {
            max_length =  Integer.valueOf(max_length_str);
        } catch (Exception e) {
            System.err.println("Bad parameter: max_length");
            System.exit(3);
        }

        new Simple(filename, min_length, max_length);
    }
}