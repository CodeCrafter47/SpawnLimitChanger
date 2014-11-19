package SpawnLimitChanger;

import SpawnLimitChanger.org.bukkit.configuration.file.YamlConfiguration;
import PluginReference.MC_Server;
import PluginReference.PluginBase;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by florian on 16.11.14.
 */
public class MyPlugin extends PluginBase {

	private YamlConfiguration config;

	@SneakyThrows
	@Override public void onStartup(MC_Server argServer) {
		loadConfig();
		injectLimits();
	}

	@SneakyThrows
	private void injectLimits() {
		Class GeneralMobType = Class.forName("joebkt.GeneralMobType");
		Field Monsters = GeneralMobType.getDeclaredField("MONSTER");
		Field Animals = GeneralMobType.getDeclaredField("CREATURE");
		Field Ambient = GeneralMobType.getDeclaredField("AMBIENT");
		Field Water = GeneralMobType.getDeclaredField("WATER_CREATURE");
		Field f = GeneralMobType.getDeclaredField("f");
		Object monsters = Monsters.get(null);
		Object animals = Animals.get(null);
		Object ambient = Ambient.get(null);
		Object water = Water.get(null);
		f.setAccessible(true);
		f.set(monsters, config.getInt("limits.monster"));
		f.set(animals, config.getInt("limits.animals"));
		f.set(ambient, config.getInt("limits.ambient"));
		f.set(water, config.getInt("limits.water"));
	}

	private void loadConfig() throws IOException {
		File configFile = new File("plugins_mod" + File.separator + "SpawnLimitChanger" + File.separator + "config.yml");
		config = YamlConfiguration.loadConfiguration(configFile);
		YamlConfiguration defaults = new YamlConfiguration();
		defaults.set("limits.monster", 70);
		defaults.set("limits.animals", 10);
		defaults.set("limits.ambient", 15);
		defaults.set("limits.water", 5);
		config.setDefaults(defaults);
		if(!configFile.exists())defaults.save(configFile);
	}
}
