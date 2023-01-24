package com.jovannmc.islandturfs.utils;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Config {
	private JavaPlugin plugin;

	private String configName;

	private String folder;

	private File configurationFile;

	private FileConfiguration configuration;

	public Config(JavaPlugin plugin, String configName, String folderName) {
		if (plugin == null)
			throw new IllegalStateException("Plugin must not be null!");
		this.plugin = plugin;
		this.configName = configName;
		this.folder = folderName;
		if (this.folder == null) {
			this.configurationFile = new File(plugin.getDataFolder(), configName);
		} else {
			this.configurationFile = new File(plugin.getDataFolder() + "/" + this.folder, configName);
		}
	}

	public FileConfiguration getConfiguration() {
		if (this.configuration == null)
			reloadConfig();
		return this.configuration;
	}

	public File getFile() {
		return this.configurationFile;
	}

	public void reloadConfig() {
		this.configuration = (FileConfiguration) YamlConfiguration.loadConfiguration(this.configurationFile);
		InputStream defConfigStream = this.plugin.getResource(this.configName);
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
			this.configuration.setDefaults((Configuration) defConfig);
		}
	}

	public void saveConfig() {
		if (this.configuration != null && this.configurationFile != null)
			try {
				getConfiguration().save(this.configurationFile);
			} catch (IOException ex) {
				this.plugin.getLogger().info("Configuration save failed!");
			}
	}

	public void saveDefaultConfig() {
		if (!this.configurationFile.exists())
			this.plugin.saveResource(this.configName, false);
	}

	public void deleteConfig() {
		this.configurationFile.delete();
	}
}
