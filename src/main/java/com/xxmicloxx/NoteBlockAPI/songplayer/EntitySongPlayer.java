package com.xxmicloxx.NoteBlockAPI.songplayer;

import com.xxmicloxx.NoteBlockAPI.model.Layer;
import com.xxmicloxx.NoteBlockAPI.model.Note;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EntitySongPlayer extends RangeSongPlayer {

    private final Location reusableLocation = new Location(null, 0.0d, 0.0d, 0.0d);
    private final Location reusableLocation2 = new Location(null, 0.0d, 0.0d, 0.0d);
    private Entity entity;

    public EntitySongPlayer(Song song) {
        super(song);
    }

    public EntitySongPlayer(Song song, SoundCategory soundCategory) {
        super(song, soundCategory);
    }

    public EntitySongPlayer(Playlist playlist, SoundCategory soundCategory) {
        super(playlist, soundCategory);
    }

    public EntitySongPlayer(Playlist playlist) {
        super(playlist);
    }

    @Override
    public void playTick(Player player) {
        if (entity.isDead()) {
            if (autoDestroy) {
                destroy();
            } else {
                setPlaying(false);
            }
        }
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
            channelMode.play(player, entity.getLocation(reusableLocation2), song, layer, note, soundCategory, volume, !enable10Octave);
        }
    }

    /**
     * Returns true if the Player is able to hear the current {@link EntitySongPlayer}
     *
     * @param player in range
     * @return ability to hear the current {@link EntitySongPlayer}
     */
    @Override
    public boolean isInRange(Player player) {
        return player.getWorld().equals(entity.getWorld()) &&
                player.getLocation(reusableLocation).distanceSquared(entity.getLocation(reusableLocation2)) <= getDistanceSquared();
    }

    /**
     * Set entity associated with this {@link EntitySongPlayer}
     *
     * @param entity
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    /**
     * Get {@link Entity} associated with this {@link EntitySongPlayer}
     *
     * @return
     */
    public Entity getEntity() {
        return entity;
    }
}
