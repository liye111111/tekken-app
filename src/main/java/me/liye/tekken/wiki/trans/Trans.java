package me.liye.tekken.wiki.trans;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class Trans {

    private static final Logger log     = Logger.getLogger(Trans.class);

    Mapping                     mapping = new Mapping();
    List<Dictionary>            dictionarys;
    private Set<String>         ignores;

    // static File f1 = new File("/Users/liye/mywork/tekken-app/src/main/resources/mapping.txt");
    // static File f2 = new File("/Users/liye/mywork/tekken-app/src/main/resources/mapping_group.txt");

    static Map<String, File>    f1      = new HashMap();
    static Trans                t1;

    static {
        f1.put(Word.TYPE_BUTTON, new File("/Users/liye/mywork/tekken-app/src/main/resources/mapping_button.txt"));
        f1.put(Word.TYPE_DIRECTION, new File("/Users/liye/mywork/tekken-app/src/main/resources/mapping_direction.txt"));
        f1.put(Word.TYPE_POSE, new File("/Users/liye/mywork/tekken-app/src/main/resources/mapping_pose.txt"));
        f1.put(Word.TYPE_OTHER, new File("/Users/liye/mywork/tekken-app/src/main/resources/mapping_other.txt"));
        f1.put(Word.TYPE_GROUP, new File("/Users/liye/mywork/tekken-app/src/main/resources/mapping_group.txt"));
        f1.put(Word.TYPE_COMBO, new File("/Users/liye/mywork/tekken-app/src/main/resources/mapping_combo.txt"));

        t1 = new Trans(f1);
    }

    // static Trans t2 = new Trans(f2);

    public static String transCommand(String cmdJp) {
        return t1.trans(cmdJp);
    }

    public static String transGroup(String groupJp) {
        String en = t1.trans(groupJp);
        en = en.trim();
        if (en != null && en.length() > 1 && en.charAt(0) == '~') {
            en = en.substring(1);
        }
        return en;
    }

    public static String transCombo(String comboJp) {
        comboJp = comboJp.trim();
        if (comboJp.startsWith("＞")) {
            comboJp = comboJp.substring(1);
        }

        List<String> ls = new ArrayList();

        String[] ss = StringUtils.split(comboJp, "＞");
        for (String s : ss) {
            String cmd = StringUtils.substringBetween(s, "(", ")");
            if (StringUtils.isEmpty(cmd)) {
                cmd = s;
            }
            ls.add(t1.trans(cmd));
        }

        return StringUtils.join(ls, ">");
    }

    public Trans(Map<String, File> files) {
        dictionarys = new ArrayList();
        ignores = new HashSet();
        for (String type : files.keySet()) {
            mapping.addFile(files.get(type));
            Dictionary d1 = new Dictionary(type, mapping.getWords().toArray(new String[0]));

            dictionarys.add(d1);
        }

        // Mapping mp2 = new Mapping();

    }

    public String trans(String commandJp) {
        if (commandJp == null) {
            return null;
        }
        List<Word> words = dnf(commandJp);
        List<Word> enWords = transJp2En(words);
        // 去掉翻译中引入的无效空格
        return joinWords(enWords).trim();
    }

    private String joinWords(List<Word> words) {
        StringBuilder sb = new StringBuilder();
        Word preWord = null;
        for (Word w : words) {
            if (w.getInput().trim().isEmpty()) {
                continue;
            }
            // 根据前一个word类型，决定是否加入空格
            if (preWord != null) {
                String pt = preWord.getType();
                String type = w.getType();
                if (Word.TYPE_BUTTON.equals(pt) && !Word.TYPE_BUTTON.equals(type)) {
                    if (!",".equals(w.getInput()) && !")".equals(w.getInput()) && !"]".equals(w.getInput())) {
                        sb.append(" ");
                    }
                } else if (Word.TYPE_DIRECTION.equals(pt) && Word.TYPE_DIRECTION.equals(type)) {
                    if (!"n".equals(w.getInput())) {
                        sb.append("/");
                    }
                }

                // if (w.getInput().equals("or")) {
                // if (pt.equals(Word.TYPE_DIRECTION)) {
                // sb.append(" ");
                // }
                // }
                // if (type.equals(Word.TYPE_DIRECTION) && preWord.getInput().equals("or")) {
                // sb.append(" ");
                // }
            }
            if (!",".equals(w.getInput())) {
                sb.append(w.getInput());
            }
            preWord = w;
        }
        return sb.toString().trim();
    }

    private List<Word> transJp2En(List<Word> words) {
        List<Word> result = new ArrayList();
        for (Word w : words) {
            String in = w.getInput();
            String out = mapping.getEn(in);
            Word ow = new Word();
            ow.setInput(out);
            ow.setType(w.getType());
            result.add(ow);
        }
        return result;
    }

    private List<Word> dnf(String cmd) {
        // 忽略的子串
        StringBuilder ignore = new StringBuilder();
        List<Word> result = new ArrayList();
        for (int i = 0; i < cmd.length();) {
            char c = cmd.charAt(i);
            String sub = cmd.substring(i);
            String type = null;
            List<Word> ls = new ArrayList();

            for (Dictionary dict : dictionarys) {
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

            if (ls == null) {

                // ignore one char
                log.debug(String.format("i %s ignore %s, remain %s ", i, c, cmd.substring(i + 1)));
                i++;
                ignore.append(c);
            } else {

                int size = lookup(sub, ls, result);
                if (size == 0) {
                    i++;
                    ignore.append(c);
                } else {
                    // 命中，ignore清零
                    if (ignore.length() > 0) {
                        ignores.add(ignore.toString());
                    }
                    ignore = new StringBuilder();
                    i += size;
                }
            }
        }
        return result;
    }

    private int lookup(String sub, List<Word> dicts, List<Word> result) {
        Collections.sort(dicts, new Comparator<Word>() {

            @Override
            public int compare(Word o1, Word o2) {
                return o2.getInput().length() - o1.getInput().length();
            }
        });

        for (Word dict : dicts) {
            if (sub.startsWith(dict.getInput())) {
                // find input
                // Word w = new Word();
                // w.setType(type);
                // w.setInput(btn);
                result.add(dict);
                return dict.getInput().length();
            }
        }
        return 0;
    }

    public static class Dictionary {

        String[]                     keywords;
        String                       type;
        Map<Character, List<String>> kwMap;

        public Dictionary(String type, String[] keywords) {
            super();
            this.keywords = keywords;
            this.type = type;
            kwMap = list2map(keywords);
        }

        public String[] getKeywords() {
            return keywords;
        }

        public void setKeywords(String[] keywords) {
            this.keywords = keywords;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            type = type;
        }

        public Map<Character, List<String>> getKwMap() {
            return kwMap;
        }

        public void setKwMap(Map<Character, List<String>> kwMap) {
            this.kwMap = kwMap;
        }

        private Map<Character, List<String>> list2map(String[] ss) {
            HashMap<Character, List<String>> result = new HashMap();
            for (String btn : ss) {
                List<String> ls = result.get(btn.charAt(0));
                if (ls == null) {
                    ls = new ArrayList();
                    result.put(btn.charAt(0), ls);
                }
                ls.add(btn);
            }
            for (List ls : result.values()) {
                Collections.sort(ls, new Comparator<String>() {

                    @Override
                    public int compare(String o1, String o2) {
                        return o2.length() - o1.length();
                    }
                });
            }
            return result;
        }

    }

    public static class Word {

        public final static String TYPE_BUTTON    = "button";
        public final static String TYPE_DIRECTION = "direction";
        public final static String TYPE_OTHER     = "other";
        public final static String TYPE_POSE      = "pose";
        public final static String TYPE_GROUP     = "group";
        public final static String TYPE_COMBO     = "combo";
        String                     type;
        String                     input;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Word [type=");
            builder.append(type);
            builder.append(", input=");
            builder.append(input);
            builder.append("]");
            return builder.toString();
        }

    }
}
