package de.lightplugins.rarity.util;

import de.lightplugins.rarity.master.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.logging.Level;

public class FileManager {

    /*
     *
     * Configuration-Manager by lightPlugins © 2023
     *
     */


    private final Main plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;
    private final String configName;
    private final boolean loadDefaultsOneReload;

    public FileManager(Main plugin, String configName, boolean loadDefaultsOneReload) {
        this.plugin = plugin;
        this.loadDefaultsOneReload = loadDefaultsOneReload;
        this.configName = configName;
        saveDefaultConfig(configName);

    }

    public void reloadConfig(String configName) {
        if(this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), configName);

        this.plugin.reloadConfig();

        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defaultStream = this.plugin.getResource(configName);
        if(defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if(this.dataConfig == null)
            reloadConfig(configName);

        return this.dataConfig;

    }

    public void saveConfig() {
        if(this.dataConfig == null || this.configFile == null)
            return;

        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, e);
        }
    }

    private void saveDefaultConfig(String configName) {
        if (this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), this.configName);

        if (!this.configFile.exists()) {
            this.plugin.saveResource(configName, false);
        } else {
            // Merge the default config into the existing config

            if(loadDefaultsOneReload) {
                FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(
                        new InputStreamReader(Objects.requireNonNull(this.plugin.getResource(configName))));
                FileConfiguration existingConfig = getConfig();
                for (String key : defaultConfig.getKeys(true)) {
                    if (!existingConfig.getKeys(true).contains(key)) {
                        Bukkit.getConsoleSender().sendMessage(Main.consolePrefix +
                                "Found §cnon existing config key§r. Adding §c" + key + " §rinto §c" + configName);
                        existingConfig.set(key, defaultConfig.get(key));

                    }
                }

                try {

                    existingConfig.save(configFile);
                    Bukkit.getConsoleSender().sendMessage(Main.consolePrefix +
                            "Your config §c" + configName + " §ris up to date.");

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                saveConfig();
            }
        }
    }
}
