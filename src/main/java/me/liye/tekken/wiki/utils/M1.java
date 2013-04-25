package me.liye.tekken.wiki.utils;

import java.lang.reflect.Field;

import me.liye.tekken.wiki.doamin.SkillEntry;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class M1 {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Field[] fs = SkillEntry.class.getDeclaredFields();
        System.out.println(fs.length);
        for (Field f : fs) {
            System.out.println("".format("\"%s\" VARCHAR,", f.getName()));
            // System.out.println(Modifier.isTransient(f.getModifiers()));
            // System.out.print("".format("%s,", f.getName()));
        }

    }
}
