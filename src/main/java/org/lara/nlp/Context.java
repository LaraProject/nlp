package org.lara.nlp;

import java.util.ArrayList;

abstract class Context {
	public ArrayList<String> questions;
	public ArrayList<String> answers;
	public abstract void init();
}