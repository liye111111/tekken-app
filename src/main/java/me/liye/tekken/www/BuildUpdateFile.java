package me.liye.tekken.www;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.liye.tekken.wiki.db.TKDB2;
import me.liye.tekken.wiki.doamin.Role;
import me.liye.tekken.wiki.doamin.SkillEntry;

import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSON;

/*
 * @author <a href="mailto:ye.liy@alibaba-inc.com">ye.liy</a>
 */
public class BuildUpdateFile {

    File         out   = new File("/Users/liye/www/tekkenhandbook/v1");
    List<String> index = new ArrayList();

    public void buildRoleList() {
        List<Role> roles = TKDB2.INSTANCE.listRole("skill_tt2");

        for (Role role : roles) {
            role.setCates("出招表,确反,投技,浮空,下裁,Tag技,Tag组合,十连技,角色分析");
        }

        String json = JSON.toJSONString(roles);
        save("roles", json);
        for (Role role : roles) {
            List<SkillEntry> skills = TKDB2.INSTANCE.listSkill("skill_tt2_en", "special", role.getEnName());
            String fn = "".format("skill_%s_%s", role.getEnName(), "special");
            save(fn, JSON.toJSONString(skills));
            index.add(fn);
        }
        for (Role role : roles) {
            List<SkillEntry> skills = TKDB2.INSTANCE.listSkill("skill_tt2_xb", "special", role.getEnName());
            String fn = "".format("skillxb_%s_%s", role.getEnName(), "special");
            save(fn, JSON.toJSONString(skills));
            index.add(fn);
        }
    }

    private void save(String fileName, String json) {
        try {
            FileUtils.write(new File(out, fileName + ".json"), json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void buildIndex() {
        index.add("roles");
        save("index", JSON.toJSONString(index));
    }

    public static void main(String[] args) {
        BuildUpdateFile builder = new BuildUpdateFile();
        builder.buildRoleList();
        //
        builder.buildIndex();
    }

}
