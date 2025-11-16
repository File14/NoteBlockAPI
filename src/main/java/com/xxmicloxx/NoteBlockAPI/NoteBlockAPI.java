package com.xxmicloxx.NoteBlockAPI;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitWorker;

import java.util.List;

public class NoteBlockAPI {

    private static NoteBlockAPI instance;
    private final Plugin plugin;

    private boolean disabling = false;

    private NoteBlockAPI(Plugin plugin) {
        this.plugin = plugin;
        instance = this;
    }

    public static NoteBlockAPI init(Plugin plugin) {
        if (instance == null) {
            return new NoteBlockAPI(plugin);
        }
        return instance;
    }

    public void shutdown() {
        disabling = true;

        Bukkit.getScheduler().cancelTasks(plugin);
        List<BukkitWorker> workers = Bukkit.getScheduler().getActiveWorkers();
        for (BukkitWorker worker : workers) {
            if (!worker.getOwner().equals(this)) {
                continue;
            }
            worker.getThread().interrupt();
        }
    }

    public void doSync(Runnable runnable) {
        if (!disabling) {
            plugin.getServer().getScheduler().runTask(plugin, runnable);
        }
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public static NoteBlockAPI getAPI() {
        return instance;
    }

    public boolean isDisabling() {
        return disabling;
    }
}
