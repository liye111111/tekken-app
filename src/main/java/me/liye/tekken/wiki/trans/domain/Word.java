package me.liye.tekken.wiki.trans.domain;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class Word {

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
