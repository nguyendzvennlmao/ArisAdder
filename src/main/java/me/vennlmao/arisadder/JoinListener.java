package me.vennlmao.arisadder;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (ArisAdder.getInstance().getConfig().getBoolean("resource-pack.enabled")) {
            String u = ArisAdder.getInstance().getConfig().getString("resource-pack.url");
            String h = ArisAdder.getInstance().getConfig().getString("resource-pack.hash");
            if (!u.isEmpty()) e.getPlayer().setResourcePack(u, h);
        }
    }
}
