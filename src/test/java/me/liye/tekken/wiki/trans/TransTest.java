package me.liye.tekken.wiki.trans;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class TransTest {

    public static void main(String[] args) {
        String jp = "デリンジャーエラワン、ダブルニー&エラワン 　3LP【RK,RK】　4RK,LK,【2RK,RK】";
        String en = Trans.transGroup(jp);
        System.out.println(en);
    }

}
