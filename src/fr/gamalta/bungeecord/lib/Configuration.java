package fr.gamalta.bungeecord.lib;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Configuration {

	private net.md_5.bungee.config.Configuration config;
	private File file;
	private String fileName;
	private String parentFileName;
	private Plugin plugin;

	public Configuration(Plugin plugin, String parentFileName, String fileName) {

		this.plugin = plugin;
		this.fileName = fileName.contains(".yml") ? fileName : fileName + ".yml";
		this.parentFileName = parentFileName.substring(parentFileName.length() - 1).equals("/") ? parentFileName : parentFileName + File.separator;
		file = new File("plugins/RedBlock/" + this.parentFileName + this.fileName);
		createConfig();
	}

	public boolean contains(String path) {

		return config.contains(path);
	}

	private void createConfig() {

		if (!file.exists()) {
			
			if (!file.getParentFile().exists()) {

				file.getParentFile().mkdirs();
			}

			try (InputStream in = plugin.getResourceAsStream(parentFileName + fileName)) {
				
				Files.copy(in, file.toPath());

			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	Object get(String path) {

		return config.get(path);
	}

	public boolean getBoolean(String path) {

		return config.getBoolean(path);
	}

	@Deprecated
	public net.md_5.bungee.config.Configuration getConfig() {

		return config;
	}

	public double getDouble(String path) {

		return config.getDouble(path);
	}

	public float getFloat(String path) {

		return config.getFloat(path);
	}

	public int getInt(String path) {

		return config.getInt(path);
	}

	public net.md_5.bungee.config.Configuration getSection(String path) {

		return config.getSection(path);
	}

	public String getString(String path) {

		return config.getString(path);
	}

	public List<String> getStringList(String path) {

		return config.getStringList(path);

	}

	public void loadConfig() {

		try {

			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	private void saveConfig(net.md_5.bungee.config.Configuration config) {

		try {

			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public void set(String path, Object obj) {

		config.set(path, obj);
		saveConfig(config);
	}
}