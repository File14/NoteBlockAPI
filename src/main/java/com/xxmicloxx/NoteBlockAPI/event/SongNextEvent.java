package com.xxmicloxx.NoteBlockAPI.event;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SongNextEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final SongPlayer song;

    public SongNextEvent(SongPlayer song) {
        this.song = song;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    /**
     * Returns SongPlayer which is going to play next song in playlist
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
