import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.commons.io.FileUtils;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class M {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        System.out.println(longestValidParentheses("((()()()())"));
        List<String> lines = FileUtils.readLines(new File(
                                                          "/Users/liye/mywork/tekken-app/src/main/resources/mapping_pose.txt"));

        List<String> out = new ArrayList();
        for (String line : lines) {
            if (line.endsWith("中")) {
                System.out.println(line.substring(0, line.length() - 1));
            } else {
                // out.add(line);
                System.out.println(line);
            }
        }

    }

    static int longestValidParentheses(String s) {
        char l = '(';
        char r = ')';
        int count = 0;

        Stack stack = new Stack();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == l) {
                stack.push(l);
            } else if (c == r) {
                if (stack.pop() != null) {
                    count++;
                }
            }
        }

        return count * 2;
    }

}
