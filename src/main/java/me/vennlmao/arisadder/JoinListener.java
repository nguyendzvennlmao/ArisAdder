package me.vennlmao.arisadder;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        String url = ArisAdder.getInstance().getConfig().getString("resource-pack.url", "");
        String hash = ArisAdder.getInstance().getConfig().getString("resource-pack.hash", "");
        if (!url.isEmpty()) e.getPlayer().setResourcePack(url, hash.toLowerCase());
    }
}
