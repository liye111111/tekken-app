package me.liye.tekken.wiki.tk7fr;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class BuildSite {
    private static final Logger log = Logger.getLogger(BuildSite.class);

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        BuildSite bs = new BuildSite();
        FileUtils.deleteDirectory(Config.out_www);
        bs.build(Config.out_trans, Config.out_www);
    }

    public void build(File src, File dest) throws Exception {
        File www = dest;
        if (!www.exists()) {
            www.mkdirs();
        }

        Set<String> players = new HashSet<>();
        String[] skills = src.list();
        for (File f : FileUtils.listFiles(src, new String[]{"html"}, true)) {
            players.add(StringUtils.substringBefore(f.getName(), "."));
        }


        ArrayList<String> playerList = new ArrayList<String>();
        playerList.addAll(players);

        Collections.sort(playerList);

        writeIndex(playerList, new File(www, "index.html"));

        for (String player : playerList) {
            File playerHome = new File(dest, player);
            playerHome.mkdirs();
            log.debug("create player home:"+playerHome);

            for(String skill:skills){
                File sFile = new File(src, skill + "/" + player + ".html");
                File dFile = new File(playerHome,skill+".html");
                FileUtils.copyFile(sFile,dFile);
                writePlayerIndex(player,new File(playerHome,"index.html"));
            }



        }

    }

    private void writeIndex(ArrayList<String> playerList, File out) throws IOException {
        playerList.remove("SpecailReact");

        Map<String, Object> map = new HashMap();
        map.put("title", "TEKKEN 7 FATED RETRIBUTION");
        map.put("players", playerList);
        String content = Utils.mergeHtml("index.html", map);
        log.debug(content);
        FileUtils.writeStringToFile(out, content);
    }
    private void writePlayerIndex(String player, File out) throws IOException {

        Map<String, Object> map = new HashMap();
        map.put("title", "TEKKEN 7 FATED RETRIBUTION");
        map.put("player", player);
        String content = Utils.mergeHtml("player_index.html", map);
        log.debug(content);
        FileUtils.writeStringToFile(out, content);
    }
}
