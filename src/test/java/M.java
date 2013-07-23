import java.util.Stack;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class M {

    /**
     * @param args
     */
    public static void main(String[] args) {

        System.out.println(longestValidParentheses("((()()()())"));
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
