package org.lara.nlp.context;

import java.io.*;
import java.util.Iterator;
import org.deeplearning4j.text.sentenceiterator.*;

public class SimpleLineIterator implements SentenceIterator, Iterable<String> {

    private BufferedReader reader;
    private InputStream backendStream;
    private SentencePreProcessor preProcessor;
    private boolean internal = false;
    private int count_line = 0;

    public SimpleLineIterator(File file) throws FileNotFoundException {
        this(new FileInputStream(file));
        this.internal = true;
    }

    public SimpleLineIterator(InputStream stream) {
        this.backendStream = stream;
        reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(backendStream, 10 * 1024 * 1024)));
    }

    public SimpleLineIterator(String filePath) throws FileNotFoundException {
        this(new FileInputStream(filePath));
        this.internal = true;
    }

    @Override
    public synchronized String nextSentence() {
        try {
            String next = reader.readLine();
			if (count_line % 2 == 0)
				next = next.substring(11);
			else
				next = next.substring(9);
			count_line = count_line + 1;
            if (preProcessor != null)
                next = this.preProcessor.preProcess(next);
			return next + "\n";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized boolean hasNext() {
        try {
            return reader.ready();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public synchronized void reset() {
        try {
            if (backendStream instanceof FileInputStream) {
                ((FileInputStream) backendStream).getChannel().position(0);
            } else backendStream.reset();
            reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(backendStream, 8192)));
            count_line = 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void finish() {
        try {
            if (this.internal && backendStream != null) backendStream.close();
            if (reader != null) reader.close();
        } catch (Exception e) {
            // do nothing here
        }
    }

    @Override
    public SentencePreProcessor getPreProcessor() {
        return preProcessor;
    }

    @Override
    public void setPreProcessor(SentencePreProcessor preProcessor) {
        this.preProcessor = preProcessor;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            if (this.internal && backendStream != null) backendStream.close();
            if (reader != null) reader.close();
        } catch (Exception e) {
            // do nothing here
            e.printStackTrace();
        }
        super.finalize();
    }

    /**
     * Implentation for Iterable interface.
     * Please note: each call for iterator() resets underlying SentenceIterator to the beginning;
     *
     * @return
     */
    @Override
    public Iterator<String> iterator() {
        this.reset();
        Iterator<String> ret =  new Iterator<String>() {
            @Override
            public boolean hasNext() {
                return SimpleLineIterator.this.hasNext();
            }

            @Override
            public String next() {
                return SimpleLineIterator.this.nextSentence();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };

        return ret;
    }
}