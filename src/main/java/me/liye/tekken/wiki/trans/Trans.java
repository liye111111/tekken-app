package me.liye.tekken.wiki.trans;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.liye.tekken.wiki.db.TKDB;
import me.liye.tekken.wiki.doamin.SkillEntry;

import org.apache.log4j.Logger;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class Trans {

    private static final Logger log        = Logger.getLogger(Trans.class);
    static Dictionary           buttons    = new Dictionary(Word.TYPE_BUTTON,
                                                            Mapping.getWords(Word.TYPE_BUTTON).toArray(new String[0]));
    static Dictionary           directions = new Dictionary(
                                                            Word.TYPE_DIRECTION,
                                                            Mapping.getWords(Word.TYPE_DIRECTION).toArray(new String[0]));
    static Dictionary           poses      = new Dictionary(Word.TYPE_POSE,
                                                            Mapping.getWords(Word.TYPE_POSE).toArray(new String[0]));
    static Dictionary           others     = new Dictionary(Word.TYPE_OTHER,
                                                            Mapping.getWords(Word.TYPE_OTHER).toArray(new String[0]));

    static List<Dictionary>     dictionarys;
    private static Set<String>  ignores;
    static {
        ignores = new HashSet();
        dictionarys = new ArrayList();
        dictionarys.add(buttons);
        dictionarys.add(directions);
        dictionarys.add(poses);
        dictionarys.add(others);

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            TKDB.INSTANCE.update("delete from skill_en", null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        List<SkillEntry> rows = TKDB.INSTANCE.listSkill(null, null);
        for (SkillEntry row : rows) {
            String id = row.getId();
            System.out.println(id);
            String cmd = row.getCommand();
            System.out.println(cmd);
            List<Word> words = dnf(cmd);
            System.out.println(words);
            List<Word> enWords = transJp2En(words);
            System.out.println(enWords);

            SkillEntry sk = row;
            String enCommand = joinWords(enWords);
            sk.setCommand(enCommand);
            System.out.println(enCommand);
            TKDB.INSTANCE.insertSkillEntry(sk, "skill_en");
        }
        // System.out.println(dnf("1LP"));
        System.err.println(ignores);
    }

    public static String trans(String commandJp) {
        List<Word> words = dnf(commandJp);
        List<Word> enWords = transJp2En(words);
        return joinWords(enWords);
    }

    private static String joinWords(List<Word> words) {
        StringBuilder sb = new StringBuilder();
        Word preWord = null;
        for (Word w : words) {

            // 根据前一个word类型，决定是否加入空格
            if (preWord != null) {
                String pt = preWord.getType();
                String type = w.getType();
                // if (w.getInput().equals("or")) {
                // if (pt.equals(Word.TYPE_DIRECTION)) {
                // sb.append(" ");
                // }
                // }
                // if (type.equals(Word.TYPE_DIRECTION) && preWord.getInput().equals("or")) {
                // sb.append(" ");
                // }
            }
            sb.append(w.getInput());
            preWord = w;
        }
        return sb.toString();
    }

    private static List<Word> transJp2En(List<Word> words) {
        List<Word> result = new ArrayList();
        for (Word w : words) {
            String in = w.getInput();
            String out = Mapping.getEn(in);
            Word ow = new Word();
            ow.setInput(out);
            ow.setType(w.getType());
            result.add(ow);
        }
        return result;
    }

    private static List<Word> dnf(String cmd) {
        // 忽略的子串
        StringBuilder ignore = new StringBuilder();
        List<Word> result = new ArrayList();
        for (int i = 0; i < cmd.length();) {
            char c = cmd.charAt(i);
            String sub = cmd.substring(i);
            String type = null;
            List<String> ls = null;

            for (Dictionary dict : dictionarys) {
                type = dict.getType();
                ls = dict.getKwMap().get(c);
                if (ls != null) {
                    break;
                }
            }

            if (ls == null) {

                // ignore one char
                log.debug(String.format("i %s ignore %s, remain %s ", i, c, cmd.substring(i + 1)));
                i++;
                ignore.append(c);
            } else {

                int size = lookup(type, sub, ls, result);
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

    private static int lookup(String type, String sub, List<String> dict, List<Word> result) {
        for (String btn : dict) {
            if (sub.startsWith(btn)) {
                // find input
                Word w = new Word();
                w.setType(type);
                w.setInput(btn);
                result.add(w);
                return btn.length();
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
