package com.xxmicloxx.NoteBlockAPI.event;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called whenever a SongPlayer is destroyed
 *
 * @see SongPlayer
 */
public class SongPlayerDestroyEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final SongPlayer song;

    public SongPlayerDestroyEvent(SongPlayer song) {
        this.song = song;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    /**
     * Returns SongPlayer which is being destroyed
     *
     * @return SongPlayer
     */
    public SongPlayer getSongPlayer() {
        return song;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
