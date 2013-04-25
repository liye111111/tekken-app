package me.liye.tekken.wiki.extracter;

import java.io.File;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class BasicSkillExtracter extends AbstractSkillExtracter {

    public static void main(String[] args) {
        new BasicSkillExtracter().process(new File("/Users/liye/Documents/tkwiki/tt2u"));
    }

    @Override
    public String getInitSql() {
        return "delete from skill where category='basic'";
    }

    @Override
    public String getCategory() {
        return "basic";
    }

    @Override
    public String getRowPattern() {
        return "//xhtml:TR";
    }

    @Override
    public String getColPattern() {
        return "./xhtml:TD";
    }
}
