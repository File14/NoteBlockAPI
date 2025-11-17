package com.xxmicloxx.NoteBlockAPI.songplayer;

import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

/**
 * SongPlayer playing only in specified distance
 */
public abstract class RangeSongPlayer extends SongPlayer {

    protected static final float VOLUME_SCALE = 1F / 16F;

    private int distance = 16;
    private int distanceSquared = distance * distance;

    public RangeSongPlayer(Song song, SoundCategory soundCategory) {
        super(song, soundCategory);
    }

    public RangeSongPlayer(Song song) {
        super(song);
    }

    public RangeSongPlayer(Playlist playlist, SoundCategory soundCategory) {
        super(playlist, soundCategory);
    }

    public RangeSongPlayer(Playlist playlist) {
        super(playlist);
    }

    /**
     * Returns true if the player is within range of the song player.
     *
     * @param player in range
     * @return ability to hear the current RangeSongPlayer
     */
    public abstract boolean isInRange(Player player);

    /**
     * Sets distance in blocks where would be player able to hear sound.
     *
     * @param distance (Default 16 blocks)
     */
    public void setDistance(int distance) {
        this.distance = distance;
        this.distanceSquared = distance * distance;
    }

    public int getDistance() {
        return distance;
    }

    public int getDistanceSquared() {
        return distanceSquared;
    }
}
