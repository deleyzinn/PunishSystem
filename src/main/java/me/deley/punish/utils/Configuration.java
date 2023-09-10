package me.deley.punish.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;
import me.deley.punish.PunishMain;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public abstract class Configuration {

    private static final ExecutorService saveAsyncExecutor = Executors
            .newCachedThreadPool(new ThreadFactoryBuilder().setDaemon(true).setNameFormat("Save Async Executor %d").build());

    private final File configFile;
    private final FileConfiguration config;

    public Configuration(File folder, String child) {
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try {
            (this.configFile = new File(folder, child)).createNewFile();
        } catch (IOException e) {
            throw new IllegalStateException("Could not create file " + child);
        }

        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    protected void load(Map<String, String> map, Object source) {
        for (Field field : source.getClass().getDeclaredFields()) {
            String path = map.get(field.getName());

            if (path == null) {
                continue;
            }

            field.setAccessible(true);

            if (this.config.isSet(path)) {
                try {
                    field.set(source, this.config.get(path));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    this.config.set(path, field.get(source));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        saveSync();
    }

    public void saveSync() {
        try {
            this.config.save(this.configFile);
        } catch (Exception e) {
        }
    }

    public void saveAsync() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    config.save(configFile);
                } catch (Exception e) {
                }
            }
        }.runTaskAsynchronously(PunishMain.getInstance());
    }

    public abstract void init();
}
