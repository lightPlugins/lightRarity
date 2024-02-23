package de.lightplugins.rarity.master;

import de.lightplugins.rarity.commands.manager.MainCommandManager;
import de.lightplugins.rarity.commands.tabs.MainTabCompleter;
import de.lightplugins.rarity.events.ItemsAdderOnLoad;
import de.lightplugins.rarity.inv.CheckItemStack;
import de.lightplugins.rarity.inv.CheckOnRepair;
import de.lightplugins.rarity.kits.KitBuilder;
import de.lightplugins.rarity.util.ColorTranslation;
import de.lightplugins.rarity.util.FileManager;
import de.lightplugins.rarity.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Main extends JavaPlugin {


    public static Main getInstance;
    public static final String consolePrefix = "§r[light§cRepair§r] ";
    public static FileManager settings;
    public static FileManager messages;
    public static FileManager kits;
    public static ColorTranslation colorTranslation;
    public static Util util;
    public static KitBuilder kitBuilder;
    public boolean isItemsAdder = false;

    public void onLoad() {


    }


    public void onEnable() {

        getInstance = this;
        settings = new FileManager(this, "settings.yml", true);
        messages = new FileManager(this, "messages.yml", true);
        kits = new FileManager(this, "kits.yml", false);

        if (Bukkit.getPluginManager().getPlugin("ItemsAdder") == null) {
            Bukkit.getConsoleSender().sendMessage("\n\n    §4ERROR\n\n" +
                    "    §cCould not find §4Itemsadder\n" +
                    "    §rDownload the latest version if ItemsAdder\n" +
                    "    §chttps://builtbybit.com/resources/itemsadder.10839/\n\n\n");
            isItemsAdder = false;
            return;
        } else {
            isItemsAdder = true;
        }

        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new CheckOnRepair(), this);
        pm.registerEvents(new CheckItemStack(), this);

        if(isItemsAdder) {
            pm.registerEvents(new ItemsAdderOnLoad(), this);
        } else {
            kitBuilder.reloadKits();
        }


        util = new Util();
        colorTranslation = new ColorTranslation();

        kitBuilder = new KitBuilder();

        Objects.requireNonNull(this.getCommand("lrepair")).setExecutor(new MainCommandManager(this));
        Objects.requireNonNull(this.getCommand("lrepair")).setTabCompleter(new MainTabCompleter());

    }


    public void onDisable() {

    }


}