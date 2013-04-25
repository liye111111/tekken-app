/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class M {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int k = 0;
        for (int i = 0; i < 1000000; i++) {
            double r = Math.random();
            if (r > 0.9) {
                k++;
            }
        }

        System.out.println(k);
    }

}
