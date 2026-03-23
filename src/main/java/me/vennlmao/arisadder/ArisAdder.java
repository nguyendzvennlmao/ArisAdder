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
        File contents = new File(getDataFolder(), "contents");
        if (!contents.exists()) contents.mkdirs();
        itemLoader = new ItemLoader();
        reloadPlugin();
        AdminCommand adminCmd = new AdminCommand();
        getCommand("arisadder").setExecutor(adminCmd);
        getCommand("arisadder").setTabCompleter(adminCmd);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
    }

    public void reloadPlugin() {
        reloadConfig();
        itemLoader.loadAll(new File(getDataFolder(), "contents"));
    }

    public static ArisAdder getInstance() { return instance; }
    public ItemLoader getItemLoader() { return itemLoader; }
                                    }
