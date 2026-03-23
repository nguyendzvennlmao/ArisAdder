package me.vennlmao.arisadder;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public class ArisAdder extends JavaPlugin {
    private static ArisAdder instance;
    private ItemLoader itemLoader;
    public static NamespacedKey ITEM_ID_KEY;

    @Override
    public void onEnable() {
        instance = this;
        ITEM_ID_KEY = new NamespacedKey(this, "aris_id");
        saveDefaultConfig();
        createFolders();
        itemLoader = new ItemLoader();
        reloadPlugin();
        getCommand("arisadder").setExecutor(new AdminCommand());
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
    }

    private void createFolders() {
        File c = new File(getDataFolder(), "configs");
        File r = new File(getDataFolder(), "resourcepack/assets");
        if (!c.exists()) c.mkdirs();
        if (!r.exists()) r.mkdirs();
    }

    public void reloadPlugin() {
        reloadConfig();
        itemLoader.loadAll(new File(getDataFolder(), "configs"));
    }

    public static ArisAdder getInstance() { return instance; }
    public ItemLoader getItemLoader() { return itemLoader; }
  }
