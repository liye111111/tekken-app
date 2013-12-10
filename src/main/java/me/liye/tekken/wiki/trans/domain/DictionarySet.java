package me.liye.tekken.wiki.trans.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class DictionarySet {

    Map<String, Dictionary> dictionarys = new HashMap();

    public void addDictionary(Dictionary dict) {
        dictionarys.put(dict.getType(), dict);
    }

    public Dictionary getDictionary(String type) {
        return dictionarys.get(type);
    }

    public List<Dictionary> listDictionary() {
        return new ArrayList(dictionarys.values());
    }
}
