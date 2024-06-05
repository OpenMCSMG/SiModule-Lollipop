package cn.cyanbukkit.lollipop.cyanlib.launcher;

import cn.cyanbukkit.lollipop.Lollipop;
import cn.cyanbukkit.lollipop.command.ModeEntry;
import cn.cyanbukkit.lollipop.command.SetUpCommand;
import cn.cyanbukkit.lollipop.cyanlib.loader.KotlinBootstrap;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;

/**
 * 嵌套框架
 */

public class CyanPluginLauncher extends JavaPlugin {

    public static CyanPluginLauncher cyanPlugin;
    public File yaml;
    public YamlConfiguration config;
    public String mainPlugin = "SiModuleGame";
    public String me = getDescription().getName();

    public CyanPluginLauncher() {
        cyanPlugin = this;
        KotlinBootstrap.init();
    }

    public void registerCommand(Command command) {
        Class<?> pluginManagerClass = cyanPlugin.getServer().getPluginManager().getClass();
        try {
            Field field = pluginManagerClass.getDeclaredField("commandMap");
            field.setAccessible(true);
            SimpleCommandMap commandMap = (SimpleCommandMap) field.get(cyanPlugin.getServer().getPluginManager());
            commandMap.register(cyanPlugin.getName(), command);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveDefaultConfig() {
        yaml = new File("plugins/" + mainPlugin + "/addon/" + me + "/config.yml");
        if (!yaml.exists()) {
            try {
                if (yaml.getParentFile().mkdirs()) {
                    InputStream is = getResource("config.yml");
                    if (is != null) {
                        Files.copy(is, yaml.toPath());
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        config = YamlConfiguration.loadConfiguration(yaml);
    }

    @Override
    public void saveConfig() {
        try {
            config.save(yaml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(yaml);
    }


    @NotNull
    @Override
    public FileConfiguration getConfig() {
        return config;
    }


    @Override
    protected File getFile() {
        return yaml;
    }


    @Override
    public void onLoad() {
    }


    @Override
    public void onEnable() {
        saveDefaultConfig();
        registerCommand(ModeEntry.INSTANCE);
        registerCommand(SetUpCommand.INSTANCE);
        Lollipop.INSTANCE.init();

    }

    @Override
    public void onDisable() {

    }


}