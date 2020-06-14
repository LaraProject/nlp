package org.lara.nlp.context;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.commons.cli.*;

import org.lara.nlp.OptionUtils;

public class Cornell extends Context {
	// Structure
	private HashMap<String,String> id_to_line;
	private ArrayList<String[]> conversations_ids;

	// Constructor
	public Cornell(String lines_filename, String conversations_filename, int min_length, int max_length) {
		super(min_length, max_length);
		id_to_line = getLines(lines_filename);
		conversations_ids = getConversations(conversations_filename);
	}

	// Creating a dictionary that maps each line with its id
	private static HashMap<String,String> getLines(String lines_filename) {
		HashMap<String,String> id_to_line = new HashMap<String,String> ();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(lines_filename));
			String line;
			String[] _line;
			while ((line = reader.readLine()) != null) {
				_line = line.split(" \\+\\+\\+\\$\\+\\+\\+ ");
				if (_line.length == 5) {
					id_to_line.put(_line[0], _line[4]);
				} else {
					id_to_line.put(_line[0], "");
				}
			}
			reader.close();
			return id_to_line;
		} catch (Exception e) {
			System.err.format("Exception occurred trying to read '%s'.", lines_filename);
			e.printStackTrace();
			return null;
		}
	}

	// Creating a list of all of the conversations
	// TODO: Optitmize me (no lists)
	private static ArrayList<String[]> getConversations(String conversations_filename) {
		ArrayList<String[]> conversations_ids = new ArrayList<String[]> ();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(conversations_filename));
			String conversation;
			String _conversation;
			String[] tmp;
			while ((conversation = reader.readLine()) != null) {
				tmp = conversation.split(" \\+\\+\\+\\$\\+\\+\\+ ");
				_conversation = tmp[tmp.length - 1];
				_conversation = _conversation.substring(1, _conversation.length() - 1);
				_conversation = _conversation.replace("'", "");
				_conversation = _conversation.replace(" ", "");
				conversations_ids.add(_conversation.split(","));
			}
			reader.close();
			return conversations_ids;
		} catch (Exception e) {
			System.err.format("Exception occurred trying to read '%s'.", conversations_filename);
			e.printStackTrace();
			return null;
		}
	}

	// Get questions and answers
	public void init() {
		for (String[] conversation: conversations_ids) {
			for (int i = 0; i < (conversation.length - 1); i++) {
				questions.add(id_to_line.get(conversation[i]));
				answers.add(id_to_line.get(conversation[i + 1]));
			}
		}
	}

    // Get context from command line arguments
    public static Options getOptions() {

        Option linesFileOption = Option.builder("lines_file") 
                .desc("Specify the path to the lines file") 
                .hasArg(true) 
                .argName("path") 
                .required(true) 
                .build();
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
        options.addOption(linesFileOption);
        options.addOption(conversationsOption);
        options.addOption(minLengthOption);
        options.addOption(maxLengthOption);

        return options;
    }

    // Pass arguments for the constructor
    public Cornell(CommandLine line) {
        this(line.getOptionValue("lines_file"),
        	line.getOptionValue("conversations_file"),
        	OptionUtils.getOptionValue(line, "min_length", 5),
        	OptionUtils.getOptionValue(line, "max_length", 30));
    }
}