package com.xxmicloxx.NoteBlockAPI.event;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called whenever a SongPlayer is stopped
 *
 * @see SongPlayer
 */
public class SongStopEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final SongPlayer songPlayer;

    public SongStopEvent(SongPlayer songPlayer) {
        this.songPlayer = songPlayer;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    /**
     * Returns SongPlayer which is now stopped
     *
     * @return SongPlayer
     */
    public SongPlayer getSongPlayer() {
        return songPlayer;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
