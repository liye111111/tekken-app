package me.liye.tekken.wiki.doamin;

import java.util.ArrayList;
import java.util.List;

/*
 * wiki中的一个表格块，代表了一类技能
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class SkillBlock {

    String           blockName;
    List<String>     columnNames = new ArrayList();
    List<SkillEntry> rows        = new ArrayList();

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public List<SkillEntry> getRows() {
        return rows;
    }

    public void setRows(List<SkillEntry> rows) {
        this.rows = rows;
    }

}
