package org.lara.nlp.context;

import org.lara.nlp.context.Simple;

class SimpleTest {
	public static void main(String[] args) throws Exception {
		// Simple data
		Simple context = new Simple(args[0], 5, 99999);
		System.out.println("SimpleTest: filename = " + args[0]);
		System.out.println("SimpleTest: min_length = 5 | max_length = 99999");
		System.out.println("SimpleTest: initializing...");
		context.init();
		System.out.println("SimpleTest: cleaning text...");
		context.cleaning();
		// Export before cleaning
		System.out.println("SimpleTest: exporting to " + args[1]);
		context.exportData(args[1]);
	}
}