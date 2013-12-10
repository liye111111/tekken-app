package me.liye.tekken.wiki.trans;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import me.liye.tekken.wiki.trans.domain.Dictionary;
import me.liye.tekken.wiki.trans.domain.DictionarySet;
import me.liye.tekken.wiki.trans.domain.Word;

import org.apache.log4j.Logger;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class Translator {

    private static final Logger log = Logger.getLogger(Translator.class);

    // 字典
    DictionarySet               dictionarySet;
    IgnoreHandler               ignoreHandler;
    SepartorProvider            separtorProvider;

    public Translator(Map<String, File> dictFiles, IgnoreHandler ignoreHandler, SepartorProvider separtorProvide) {
        this.ignoreHandler = ignoreHandler;
        this.separtorProvider = separtorProvide;
        dictionarySet = new DictionarySet();
        for (Map.Entry<String, File> et : dictFiles.entrySet()) {
            Dictionary dict = new Dictionary(et.getKey(), et.getValue());
            dictionarySet.addDictionary(dict);
        }
    }

    public String process(String src) {

        if (src == null) {
            return null;
        }
        // 分词
        List<Word> words = dnf(src);
        // 翻译
        trans(words);
        // 合并
        return joinWords(words).trim();

    }

    // 分词
    private List<Word> dnf(String src) {
        // 忽略的子串
        StringBuilder ignore = new StringBuilder();
        List<Word> result = new ArrayList();
        for (int i = 0; i < src.length();) {
            char c = src.charAt(i);
            String sub = src.substring(i);
            String type = null;
            List<Word> ls = new ArrayList();

            for (Dictionary dict : dictionarySet.listDictionary()) {
                type = dict.getType();
                List<String> l = dict.getKwMap().get(c);
                if (l != null) {
                    for (String li : l) {
                        Word w = new Word();
                        w.setType(type);
                        w.setInput(li);
                        ls.add(w);
                    }
                }
            }

            // 字典中没有匹配,后移一位
            if (ls == null || ls.isEmpty()) {
                // ignore one char
                // log.debug(String.format("i %s ignore %s, remain %s ", i, c, src.substring(i + 1)));
                i++;
                ignore.append(c);
            }
            // 字典中有匹配
            else {
                // 查找最长匹配
                int size = lookup(sub, ls, result);
                // ignore清零
                if (size == 0) {
                    i++;
                    ignore.append(c);
                } else {
                    if (ignore.length() > 0) {
                        ignoreHandler.handle(ignore.toString());
                    }
                    ignore = new StringBuilder();
                    i += size;
                }
            }
        }
        return result;
    }

    // 最长匹配
    private int lookup(String src, List<Word> dicts, List<Word> result) {
        Collections.sort(dicts, new Comparator<Word>() {

            @Override
            public int compare(Word o1, Word o2) {
                return o2.getInput().length() - o1.getInput().length();
            }
        });

        for (Word dict : dicts) {
            if (src.startsWith(dict.getInput())) {
                result.add(dict);
                return dict.getInput().length();
            }
        }
        return 0;
    }

    private void trans(List<Word> words) {
        List<Word> result = new ArrayList();
        for (Word w : words) {
            String in = w.getInput();
            String out = dictionarySet.getDictionary(w.getType()).getTo(in);
            w.setInput(out);
        }
    }

    private String joinWords(List<Word> words) {
        StringBuilder sb = new StringBuilder();
        Word preWord = null;
        for (Word w : words) {
            if (w.getInput().trim().isEmpty()) {
                continue;
            }
            String split = separtorProvider.provide(preWord, w);
            sb.append(split);
            sb.append(w.getInput());
            preWord = w;
        }
        return sb.toString().trim();
    }

    // ////////////

    public static interface IgnoreHandler {

        void handle(String ignore);
    }

    public static interface SepartorProvider {

        String provide(Word preWord, Word currentWord);
    }

}
