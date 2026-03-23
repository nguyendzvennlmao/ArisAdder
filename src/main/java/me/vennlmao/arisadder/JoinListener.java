package me.vennlmao.arisadder;

import org.bukkit.event.*;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        String u = ArisAdder.getInstance().getConfig().getString("resource-pack.url");
        String h = ArisAdder.getInstance().getConfig().getString("resource-pack.hash");
        if (ArisAdder.getInstance().getConfig().getBoolean("resource-pack.enabled") && !u.isEmpty()) {
            e.getPlayer().setResourcePack(u, h);
        }
    }
}
