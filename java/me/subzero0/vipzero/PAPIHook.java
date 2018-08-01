package me.subzero0.vipzero;

import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PAPIHook extends EZPlaceholderHook {

    private final Main plugin;

    public PAPIHook(Main plugin) {
        super(plugin, "vipzero");
        this.plugin = plugin;
    }

    @Override
    public String onPlaceholderRequest(Player player, String string) {
        if(string.equalsIgnoreCase("vip")){
            for(String groups : plugin.getConfig().getStringList("vip_groups")){
                if(Main.perms.getPrimaryGroup(player).equalsIgnoreCase(groups)){
                    return "true";
                }
            }
            return "false";
        }else if(string.equalsIgnoreCase("leftday")){
            if(plugin.getConfig().getBoolean("MySQL.use")) {
                int days = 0;
                try {
                    Connection con = DriverManager.getConnection(plugin.mysql_url, plugin.mysql_user, plugin.mysql_pass);
                    PreparedStatement pst = con.prepareStatement("SELECT * FROM `vips` WHERE `nome`='" + player.getName() + "';");
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        for (String gname : plugin.getConfig().getStringList("vip_groups")) {
                            if (rs.getInt(gname.trim()) != 0) {
                                days = rs.getInt(gname.trim());
                            }
                        }
                    }
                    pst.close();
                    rs.close();
                    con.close();
                } catch (Exception e) {
                }
                return days + "";
            }else {
                for(String gname : plugin.getConfig().getStringList("vip_groups")){
                    if(plugin.getConfig().contains("vips."+ player.getName()+ "." + gname.trim())){
                        return plugin.getConfig().getInt("vips."+ player.getName()+"."+gname) + "";
                    }
                }
                return "0";
            }
        }else if(string.equalsIgnoreCase("group")){
            return Main.perms.getPrimaryGroup(player);
        }else {
            return "false";
        }
    }
}
