package me.vennlmao.arisadder;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.zip.*;

public class AdminCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!s.hasPermission("arisadder.admin")) {
            s.sendMessage(ArisAdder.getInstance().getConfig().getString("messages.no-permission"));
            return true;
        }
        if (a.length > 0) {
            if (a[0].equalsIgnoreCase("reload")) {
                ArisAdder.getInstance().reloadPlugin();
                s.sendMessage(ArisAdder.getInstance().getConfig().getString("messages.reload"));
                return true;
            }
            if (a[0].equalsIgnoreCase("pack")) {
                generatePack(s);
                return true;
            }
        }
        if (s instanceof Player p) {
            Inventory gui = Bukkit.createInventory(null, 54, "§0ArisAdder - Items");
            ArisAdder.getInstance().getItemLoader().getLoadedItems().forEach((id, item) -> gui.addItem(item));
            p.getScheduler().execute(ArisAdder.getInstance(), () -> p.openInventory(gui), null, 0L);
        }
        return true;
    }

    private void generatePack(CommandSender s) {
        File folder = new File(ArisAdder.getInstance().getDataFolder(), "resourcepack");
        File zip = new File(ArisAdder.getInstance().getDataFolder(), "pack.zip");
        try {
            if (zip.exists()) zip.delete();
            zipFolder(folder.toPath(), zip.toPath());
            String hash = getSHA1(zip);
            ArisAdder.getInstance().getConfig().set("resource-pack.hash", hash);
            ArisAdder.getInstance().saveConfig();
            s.sendMessage(ArisAdder.getInstance().getConfig().getString("messages.pack-success"));
            s.sendMessage("§eSHA-1: §b" + hash);
        } catch (Exception e) { s.sendMessage("§cLoi: " + e.getMessage()); }
    }

    private void zipFolder(Path src, Path out) throws IOException {
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(out))) {
            Files.walk(src).filter(p -> !Files.isDirectory(p)).forEach(p -> {
                ZipEntry ze = new ZipEntry(src.relativize(p).toString());
                try {
                    zs.putNextEntry(ze);
                    Files.copy(p, zs);
                    zs.closeEntry();
                } catch (IOException e) { e.printStackTrace(); }
            });
        }
    }

    private String getSHA1(File f) throws Exception {
        MessageDigest d = MessageDigest.getInstance("SHA-1");
        try (InputStream is = new FileInputStream(f)) {
            byte[] b = new byte[8192];
            int n;
            while ((n = is.read(b)) != -1) d.update(b, 0, n);
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : d.digest()) sb.append(String.format("%02x", b));
        return sb.toString();
    }
                }
