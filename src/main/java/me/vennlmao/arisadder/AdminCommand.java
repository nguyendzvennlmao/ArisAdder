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
        if (!s.hasPermission("arisadder.admin")) return true;
        if (a.length > 0) {
            if (a[0].equalsIgnoreCase("reload")) {
                ArisAdder.getInstance().reloadPlugin();
                s.sendMessage("§aReloaded!");
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
            p.openInventory(gui);
        }
        return true;
    }

    private void generatePack(CommandSender s) {
        File zip = new File(ArisAdder.getInstance().getDataFolder(), "pack.zip");
        try {
            if (zip.exists()) zip.delete();
            try (ZipOutputStream zs = new ZipOutputStream(new FileOutputStream(zip))) {
                File contents = new File(ArisAdder.getInstance().getDataFolder(), "contents");
                File[] folders = contents.listFiles(File::isDirectory);
                if (folders != null) {
                    for (File f : folders) {
                        File assets = new File(f, "resourcepack/assets");
                        if (!assets.exists()) continue;
                        addFolderToZip(assets, "assets", zs);
                    }
                }
                zs.putNextEntry(new ZipEntry("pack.mcmeta"));
                zs.write("{\"pack\":{\"pack_format\":15,\"description\":\"Aris Pack\"}}".getBytes());
                zs.closeEntry();
            }
            String hash = getSHA1(zip);
            ArisAdder.getInstance().getConfig().set("resource-pack.hash", hash);
            ArisAdder.getInstance().saveConfig();
            s.sendMessage("§aPack.zip created! SHA-1: " + hash);
        } catch (Exception e) { s.sendMessage("§cError: " + e.getMessage()); }
    }

    private void addFolderToZip(File folder, String base, ZipOutputStream zs) throws IOException {
        for (File f : folder.listFiles()) {
            if (f.isDirectory()) {
                addFolderToZip(f, base + "/" + f.getName(), zs);
            } else {
                zs.putNextEntry(new ZipEntry(base + "/" + f.getName()));
                Files.copy(f.toPath(), zs);
                zs.closeEntry();
            }
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
