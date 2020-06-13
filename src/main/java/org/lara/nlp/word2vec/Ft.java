package org.lara.nlp.word2vec;

import org.deeplearning4j.models.fasttext.FastText;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import java.io.File;

public class Ft {
	// Structure
	private FastText fasttext;

	// Constructor
	public Ft(String input_file, String output_file, boolean cbow, boolean skipgram, int min_count, int dim, int window_size, int epochs, int workers) {
		FastText fastText = FastText.builder()
						.cbow(cbow)
						.skipgram(skipgram)
						.minCount(min_count)
						.dim(dim)
						.contextWindowSize(window_size)
						.epochs(epochs)
						.numThreads(workers)
						.inputFile(input_file)
						.outputFile(output_file)
						.build();
        fasttext.fit();
	}

	// Constructor with a given model
	public Ft(String path) throws Exception {
		File saved_model = new File(path);
		fasttext = new FastText(saved_model);
	}

	// Output to a file
	public void write_vectors(String path) throws Exception {
		File saved_model = new File(path);
		WordVectorSerializer.writeWordVectors(fasttext, saved_model);
	}

	// Export the model
	public FastText getModel() {
		return fasttext;
	}

}