package me.vennlmao.arisadder;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class AdminCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!s.hasPermission("arisadder.admin")) {
            s.sendMessage(ArisAdder.getInstance().getConfig().getString("messages.no-permission"));
            return true;
        }
        if (a.length > 0 && a[0].equalsIgnoreCase("reload")) {
            ArisAdder.getInstance().reloadPlugin();
            s.sendMessage(ArisAdder.getInstance().getConfig().getString("messages.reload"));
            return true;
        }
        if (s instanceof Player p) {
            Inventory gui = Bukkit.createInventory(null, 54, "§0ArisAdder - Items");
            ArisAdder.getInstance().getItemLoader().getLoadedItems().forEach((id, item) -> gui.addItem(item));
            p.getScheduler().execute(ArisAdder.getInstance(), () -> p.openInventory(gui), null, 0L);
        }
        return true;
    }
}
