package com.xxmicloxx.NoteBlockAPI;

import org.bukkit.plugin.Plugin;

public class NoteBlockAPI {

    private static NoteBlockAPI instance;
    private final Plugin plugin;

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
        instance = null;
    }

    public static NoteBlockAPI getAPI() {
        return instance;
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
