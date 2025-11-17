package com.xxmicloxx.NoteBlockAPI.model.playmode;

import com.xxmicloxx.NoteBlockAPI.model.Layer;
import com.xxmicloxx.NoteBlockAPI.model.Note;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.MathUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

/**
 * Decides how is {@link Note} played to {@link Player}
 */
public abstract class ChannelMode {

    public abstract void play(Player player,
                              Location location,
                              Song song,
                              Layer layer,
                              Note note,
                              SoundCategory soundCategory,
                              float volume,
                              boolean doTranspose
    );


    protected void playSound(Player player, Location location, String sound,
                             SoundCategory category, float volume, float pitch, float distance) {
        player.playSound(MathUtils.stereoPan(location, distance), Sound.valueOf(sound), category, volume, pitch);
    }

    protected void playSound(Player player, Location location, Sound sound,
                             SoundCategory category, float volume, float pitch, float distance) {
        player.playSound(MathUtils.stereoPan(location, distance), sound, category, volume, pitch);
    }
}