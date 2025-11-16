package com.xxmicloxx.NoteBlockAPI.songplayer;

import com.xxmicloxx.NoteBlockAPI.model.Layer;
import com.xxmicloxx.NoteBlockAPI.model.Note;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.model.playmode.ChannelMode;
import com.xxmicloxx.NoteBlockAPI.model.playmode.MonoMode;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

/**
 * SongPlayer playing to everyone added to it no matter where he is
 */
public class RadioSongPlayer extends SongPlayer {

    public RadioSongPlayer(Song song) {
        super(song);
    }

    public RadioSongPlayer(Song song, SoundCategory soundCategory) {
        super(song, soundCategory);
    }

    public RadioSongPlayer(Playlist playlist, SoundCategory soundCategory) {
        super(playlist, soundCategory);
    }

    public RadioSongPlayer(Playlist playlist) {
        super(playlist);
    }

    @Override
    public void playTick(Player player) {
        for (Layer layer : song.getLayerHashMap().values()) {
            Note note = layer.getNote(tick);

            if (note == null) {
                continue;
            }
            float volume = (layer.getVolume() * this.volume * note.getVelocity()) / 1_000_000F;
            channelMode.play(player, player.getEyeLocation(), song, layer, note, soundCategory, volume, !enable10Octave);
        }
    }

    /**
     * Sets how will be {@link Note} played to {@link Player} (eg. mono or stereo). Default is {@link MonoMode}.
     *
     * @param mode
     */
    public void setChannelMode(ChannelMode mode) {
        this.channelMode = mode;
    }
}
