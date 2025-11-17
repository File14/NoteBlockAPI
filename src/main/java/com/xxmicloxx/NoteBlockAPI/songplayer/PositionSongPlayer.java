package com.xxmicloxx.NoteBlockAPI.songplayer;

import com.xxmicloxx.NoteBlockAPI.model.Layer;
import com.xxmicloxx.NoteBlockAPI.model.Note;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

/**
 * SongPlayer created at a specified Location
 */
public class PositionSongPlayer extends RangeSongPlayer {

    private final Location reusableLocation = new Location(null, 0.0d, 0.0d, 0.0d);
    private Location targetLocation;

    public PositionSongPlayer(Song song) {
        super(song);
    }

    public PositionSongPlayer(Song song, SoundCategory soundCategory) {
        super(song, soundCategory);
    }

    public PositionSongPlayer(Playlist playlist, SoundCategory soundCategory) {
        super(playlist, soundCategory);
    }

    public PositionSongPlayer(Playlist playlist) {
        super(playlist);
    }

    @Override
    public void playTick(Player player) {
        if (!isInRange(player)) {
            return;
        }
        float distanceFactor = VOLUME_SCALE * getDistance();
        float baseVolume = this.volume * distanceFactor;

        for (Layer layer : song.getLayerHashMap().values()) {
            Note note = layer.getNote(tick);

            if (note == null) {
                continue;
            }
            float volume = (layer.getVolume() * note.getVelocity() * baseVolume) / 1_000_000F;
            channelMode.play(player, targetLocation, song, layer, note, soundCategory, volume, !enable10Octave);
        }
    }

    /**
     * Returns true if the Player is able to hear the current PositionSongPlayer
     *
     * @param player in range
     * @return ability to hear the current PositionSongPlayer
     */
    @Override
    public boolean isInRange(Player player) {
        return player.getWorld().equals(targetLocation.getWorld()) &&
                player.getLocation(reusableLocation).distanceSquared(targetLocation) <= getDistanceSquared();
    }

    /**
     * Gets location on which is the PositionSongPlayer playing
     *
     * @return {@link Location}
     */
    public Location getTargetLocation() {
        return targetLocation;
    }

    /**
     * Sets location on which is the PositionSongPlayer playing
     */
    public void setTargetLocation(Location targetLocation) {
        this.targetLocation = targetLocation;
    }
}
