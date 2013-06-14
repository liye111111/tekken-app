package me.liye.tekken.wiki.extracter;

import java.io.File;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class SpecialSkillExtracter extends AbstractSkillExtracter {

    public static void main(String[] args) {
        new SpecialSkillExtracter().process(new File("/Users/liye/Documents/tkwiki/tt2u"));
    }

    @Override
    public String getInitSql() {
        return "delete from skill where category='special'";
    }

    @Override
    public String getCategory() {
        return "special";
    }

}
