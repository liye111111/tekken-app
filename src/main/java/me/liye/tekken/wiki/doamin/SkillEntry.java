package me.liye.tekken.wiki.doamin;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class SkillEntry {

    String domContent;

    // 角色
    String charactor;
    // 分类
    String category;

    // //////////////

    // 招名
    String name;
    // 指令
    String command;
    // 判定
    String judge;
    // 伤害
    String damage;
    // 帧数 发生
    String f_init;
    // 帧数 防御
    String f_block;
    // 帧数 命中
    String f_hit;
    // 帧数 破招
    String f_ch;
    // 备注
    String memo;
    // 新技
    String isNew;

    public String getCharactor() {
        return charactor;
    }

    public void setCharactor(String charactor) {
        this.charactor = charactor;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getJudge() {
        return judge;
    }

    public void setJudge(String judge) {
        this.judge = judge;
    }

    public String getDamage() {
        return damage;
    }

    public String getF_init() {
        return f_init;
    }

    public void setF_init(String f_init) {
        this.f_init = f_init;
    }

    public String getF_block() {
        return f_block;
    }

    public void setF_block(String f_block) {
        this.f_block = f_block;
    }

    public String getF_hit() {
        return f_hit;
    }

    public void setF_hit(String f_hit) {
        this.f_hit = f_hit;
    }

    public String getF_ch() {
        return f_ch;
    }

    public void setF_ch(String f_ch) {
        this.f_ch = f_ch;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }

    public String getDomContent() {
        return domContent;
    }

    public void setDomContent(String domContent) {
        this.domContent = domContent;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SkillEntry [charactor=");
        builder.append(charactor);
        builder.append(", category=");
        builder.append(category);
        builder.append(", name=");
        builder.append(name);
        builder.append(", command=");
        builder.append(command);
        builder.append(", judge=");
        builder.append(judge);
        builder.append(", damage=");
        builder.append(damage);
        builder.append(", f_init=");
        builder.append(f_init);
        builder.append(", f_block=");
        builder.append(f_block);
        builder.append(", f_hit=");
        builder.append(f_hit);
        builder.append(", f_ch=");
        builder.append(f_ch);
        builder.append(", memo=");
        builder.append(memo);
        builder.append(", isNew=");
        builder.append(isNew);
        builder.append("]");
        return builder.toString();
    }

}
