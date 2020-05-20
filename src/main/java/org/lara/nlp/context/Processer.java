package org.lara.nlp.context;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

class Processer {
	// Structure
	public ArrayList<String> questions;
	public ArrayList<String> answers;

	// Limits
	private int min_length;
	private int max_length;

	public Processer(ArrayList<String> questions, ArrayList<String> answers, int min_length, int max_length) {
		this.questions = questions;
		this.answers = answers;
		this.min_length = min_length;
		this.max_length = max_length;
	}

	// Simplifying and cleaning the text using regex
	private static String clean_text(String orig) {
		String text = orig.toLowerCase();
		text = text.replaceAll("i'm", "i am");
		text = text.replaceAll("he's", "he is");
		text = text.replaceAll("she's", "she is");
		text = text.replaceAll("that's", "that is");
		text = text.replaceAll("what's", "what is");
		text = text.replaceAll("where's", "where is");
		text = text.replaceAll("how's", "how is");
		text = text.replaceAll("who's", "who is");
		text = text.replaceAll("here's", "here is");
		text = text.replaceAll("it's", "it is");
		text = text.replaceAll("there's", "there is");
		text = text.replaceAll("\'ll", " will");
		text = text.replaceAll("\'ve", " have");
		text = text.replaceAll("\'re", " are");
		text = text.replaceAll("\'d", " would");
		text = text.replaceAll("n't", " not");
		text = text.replaceAll("won't", "will not");
		text = text.replaceAll("can't", "cannot");
		text = text.replaceAll("<u>","");
		text = text.replaceAll("</u>", "");
		text = text.replaceAll("<i>","");
		text = text.replaceAll("</i>", "");
		text = text.replaceAll("<b>","");
		text = text.replaceAll("</b>", "");
		text = text.replaceAll("[\"#$%&()*+,-./:;<=>[\\@]^_`{|}~]", "");
		text = text.replaceAll("\\r\\n|\\r|\\n", " ");
		//text = text.replaceAll("[^a-zA-Z ]", "")
		return text;
	}

	// Clean questions and answers
	private void cleanQuestionsAnswers() {
		ArrayList<String> clean_questions = new ArrayList<String> ();
		ArrayList<String> clean_answers = new ArrayList<String> ();
		for (String q: questions)
			clean_questions.add(clean_text(q));
		for (String a: answers)
			clean_answers.add(clean_text(a));
		questions = clean_questions;
		answers = clean_answers;
	}

	// Check length of a string
	private boolean checkLength(String str) {
		int l = str.length();
		return ((min_length <= l) && (l <= max_length));
	}

	// Filter out the questions and answers that are too short or too long
	private void lengthFilter() {
		ArrayList<String> short_questions = new ArrayList<String> ();
		ArrayList<String> short_answers = new ArrayList<String> ();
		Iterator<String> it_questions = questions.iterator();
		Iterator<String> it_answers = answers.iterator();
		while (it_questions.hasNext() && it_answers.hasNext()) {
			String question = it_questions.next();
			String answer = it_answers.next();
			if (checkLength(question) && checkLength(answer)) {
				short_questions.add(question);
				short_answers.add(answer);
			}
		}
		questions = short_questions;
		answers = short_answers;
	}

	// Tokenize
	private String tokenize_sentence(String s) {
		String ret = s;
		int l = (s.split(" ")).length;
		return "<START> " + ret + " <END>";
	}
	private ArrayList<String> tokenize_set(ArrayList<String> set) {
		ArrayList<String> ret = new ArrayList<String> ();
		for (String s: set) {
			ret.add(tokenize_sentence(s));
		}
		return ret;
	}
	public void tokenize() {
		questions = tokenize_set(questions);
		answers = tokenize_set(answers);
	}

	// Process everything
	public void process() {
		lengthFilter();
		cleanQuestionsAnswers();
	}
}