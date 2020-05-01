package org.lara.nlp.context;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

public abstract class Context {
	public ArrayList<String> questions;
	public ArrayList<String> answers;
	public Hashtable<String, String> dictionnary;

	public abstract void init();
	// Limits
	public int min_length;
	public int max_length;

	// Execute the cleaning
	public void cleaning() {
		Processer process = new Processer(questions, answers, dictionnary, min_length, max_length);
		process.process();
		this.questions = process.questions;
		this.answers = process.answers;
		this.dictionnary = process.dictionnary;
	}

	// Save the questions and answers
	public void save(String path_questions, String path_answers) throws Exception {
		FileWriter writer_questions = new FileWriter(path_questions);
		FileWriter writer_answers = new FileWriter(path_answers);
		for (String str: questions) {
			writer_questions.write(str + System.lineSeparator());
		}
		for (String str: answers) {
			writer_answers.write(str + System.lineSeparator());
		}
		writer_questions.close();
		writer_answers.close();
	}

	// Restore from a file
	public void restore(String path_questions, String path_answers) {
		questions = new ArrayList<String> ();
		answers = new ArrayList<String> ();
		try {
			BufferedReader reader_questions = new BufferedReader(new FileReader(path_questions));
			String line;
			while ((line = reader_questions.readLine()) != null) {
				questions.add(line);
			}
			reader_questions.close();
		} catch (Exception e) {
			System.err.format("Exception occurred trying to read '%s'.", path_questions);
			e.printStackTrace();
		}
		try {
			BufferedReader reader_answers = new BufferedReader(new FileReader(path_answers));
			String line;
			while ((line = reader_answers.readLine()) != null) {
				questions.add(line);
			}
			reader_answers.close();
		} catch (Exception e) {
			System.err.format("Exception occurred trying to read '%s'.", path_answers);
			e.printStackTrace();
		}
	}

	// Export the dictionnary to python
	public void exportDictionnary(String script_path) throws Exception {
		Iterator<String> it_questions = questions.iterator();
		Iterator<String> it_answers = answers.iterator();
		FileWriter write_script = new FileWriter(script_path);
		write_script.write("import numpy as np\n");
		write_script.write("dict = {}\n");
		while (it_questions.hasNext() && it_answers.hasNext()) {
			write_script.write("dict['" + it_questions.next() + "'] = '" + it_answers.next() + "'\n");
		}
		write_script.write("np.save('conversationDictionary.npy', dict)\n");
		write_script.close();
	}	
}
