package me.vennlmao.arisadder;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ItemLoader {
    private final Map<String, ItemStack> items = new HashMap<>();
    public void loadAll(File folder) {
        items.clear();
        if (!folder.exists() || folder.listFiles() == null) return;
        for (File f : folder.listFiles()) {
            if (!f.getName().endsWith(".yml")) continue;
            YamlConfiguration c = YamlConfiguration.loadConfiguration(f);
            ConfigurationSection s = c.getConfigurationSection("items");
            if (s == null) continue;
            for (String k : s.getKeys(false)) {
                String p = "items." + k + ".";
                Material m = Material.valueOf(c.getString(p + "resource.material", "PAPER").toUpperCase());
                int id = c.getInt(p + "resource.model_id", 0);
                String n = c.getString(p + "display_name", k);
                ItemStack is = new ItemStack(m);
                ItemMeta im = is.getItemMeta();
                if (im != null) {
                    im.setDisplayName(n.replace("&", "§"));
                    if (id != 0) im.setCustomModelData(id);
                    im.getPersistentDataContainer().set(ArisAdder.ITEM_ID_KEY, PersistentDataType.STRING, k);
                    is.setItemMeta(im);
                }
                items.put(k, is);
            }
        }
    }
    public Map<String, ItemStack> getLoadedItems() { return items; }
                        }
