/* *****************************************************************************
 * Copyright (c) 2015-2019 Skymind, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ******************************************************************************/

package org.lara.rnn;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.lara.nlp.context.Context;

public class CorpusProcessor {
    static final String SPECIALS = "!\"#$;%^:?*()[]{}<>«»,.–—=+…";
    private Set<String> dictSet = new HashSet<>();
    private Map<String, Double> freq = new HashMap<>();
    private Map<String, Double> dict = new HashMap<>();
    private Context context;
    private boolean countFreq;
    private int rowSize;

    CorpusProcessor(Context context, int rowSize, boolean countFreq) {
    	this.context = context;
        this.rowSize = rowSize;
        this.countFreq = countFreq;
    }

    public void start() {
    	for (String s : context.questions) {
    		processLine(s);
    	}
    	for (String s : context.answers) {
    		processLine(s);
    	}
    }

    protected void processLine(String lastLine) {
        tokenizeLine(lastLine, dictSet, false);
    }

    // here we not only split the words but also store punctuation marks
    void tokenizeLine(String lastLine, Collection<String> resultCollection, boolean addSpecials) {
        String[] words = lastLine.split(" ");
        for (String word : words) {
            if (!word.isEmpty()) {
                boolean specialFound = true;
                while (specialFound && !word.isEmpty()) {
                    for (int i = 0; i < word.length(); ++i) {
                        int idx = SPECIALS.indexOf(word.charAt(i));
                        specialFound = false;
                        if (idx >= 0) {
                            String word1 = word.substring(0, i);
                            if (!word1.isEmpty()) {
                                addWord(resultCollection, word1);
                            }
                            if (addSpecials) {
                                addWord(resultCollection, String.valueOf(word.charAt(i)));
                            }
                            word = word.substring(i + 1);
                            specialFound = true;
                            break;
                        }
                    }
                }
                if (!word.isEmpty()) {
                    addWord(resultCollection, word);
                }
            }
        }
    }

    private void addWord(Collection<String> coll, String word) {
        if (coll != null) {
            coll.add(word);
        }
        if (countFreq) {
            Double count = freq.get(word);
            if (count == null) {
                freq.put(word, 1.0);
            } else {
                freq.put(word, count + 1);
            }
        }
    }

    Map<String, Double> getFreq() {
        return freq;
    }

    void setDict(Map<String, Double> dict) {
        this.dict = dict;
    }

    /**
     * Converts an iterable sequence of words to a list of indices. This will
     * never return {@code null} but may return an empty {@link java.util.List}.
     *
     * @param words
     *            sequence of words
     * @return list of indices.
     */
    final List<Double> wordsToIndexes(final Iterable<String> words) {
        int i = rowSize;
        final List<Double> wordIdxs = new LinkedList<>();
        for (final String word : words) {
            if (--i == 0) {
                break;
            }
            final Double wordIdx = dict.get(word);
            if (wordIdx != null) {
                wordIdxs.add(wordIdx);
            } else {
                wordIdxs.add(0.0);
            }
        }
        return wordIdxs;
    }

}
