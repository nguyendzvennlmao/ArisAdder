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

    public void loadAll(File contentsFolder) {
        items.clear();
        File[] folders = contentsFolder.listFiles(File::isDirectory);
        if (folders == null) return;

        for (File sub : folders) {
            File configDir = new File(sub, "configs");
            if (!configDir.exists()) continue;

            File[] yamls = configDir.listFiles((dir, name) -> name.endsWith(".yml"));
            if (yamls == null) continue;

            for (File f : yamls) {
                YamlConfiguration c = YamlConfiguration.loadConfiguration(f);
                ConfigurationSection s = c.getConfigurationSection("items");
                if (s == null) continue;

                for (String key : s.getKeys(false)) {
                    ConfigurationSection itemSec = s.getConfigurationSection(key);
                    if (itemSec == null) continue;
                    
                    String matStr = itemSec.getString("resource.material", "PAPER").toUpperCase();
                    Material m = Material.getMaterial(matStr);
                    int id = itemSec.getInt("resource.model_id", 0);
                    String name = itemSec.getString("display_name", key);

                    ItemStack is = new ItemStack(m != null ? m : Material.PAPER);
                    ItemMeta im = is.getItemMeta();
                    if (im != null) {
                        im.setDisplayName(name.replace("&", "§"));
                        if (id != 0) im.setCustomModelData(id);
                        im.getPersistentDataContainer().set(ArisAdder.ITEM_ID_KEY, PersistentDataType.STRING, key);
                        is.setItemMeta(im);
                    }
                    items.put(key, is);
                }
            }
        }
    }

    public Map<String, ItemStack> getLoadedItems() { return items; }
                }
