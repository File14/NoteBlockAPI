package com.xxmicloxx.NoteBlockAPI.model.playmode;

import com.xxmicloxx.NoteBlockAPI.model.CustomInstrument;
import com.xxmicloxx.NoteBlockAPI.model.Layer;
import com.xxmicloxx.NoteBlockAPI.model.Note;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.InstrumentUtils;
import com.xxmicloxx.NoteBlockAPI.utils.NoteUtils;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

/**
 * {@link Note} is played inside of {@link Player}'s head.
 */
public class MonoMode extends ChannelMode {

    @Override
    public void play(Player player, Location location, Song song, Layer layer, Note note, SoundCategory soundCategory, float volume, boolean doTranspose) {
        float pitch;

        if (doTranspose) {
            pitch = NoteUtils.getPitchTransposed(note);
        } else {
            pitch = NoteUtils.getPitchInOctave(note);
        }

        if (InstrumentUtils.isCustomInstrument(note.getInstrument())) {
            CustomInstrument instrument = song.getCustomInstruments()[note.getInstrument() - InstrumentUtils.getCustomInstrumentFirstIndex()];

            if (!doTranspose) {
                playSound(player, location, InstrumentUtils.warpNameOutOfRange(instrument.getSoundFileName(), note.getKey(), note.getPitch()), soundCategory, volume, pitch, 0);
            } else {
                if (instrument.getSound() != null) {
                    playSound(player, location, instrument.getSound(), soundCategory, volume, pitch, 0);
                } else {
                    playSound(player, location, instrument.getSoundFileName(), soundCategory, volume, pitch, 0);
                }
            }
        } else {
            if (NoteUtils.isOutOfRange(note.getKey(), note.getPitch()) && !doTranspose) {
                playSound(player, location, InstrumentUtils.warpNameOutOfRange(note.getInstrument(), note.getKey(), note.getPitch()), soundCategory, volume, pitch, 0);
            } else {
                playSound(player, location, InstrumentUtils.getInstrument(note.getInstrument()), soundCategory, volume, pitch, 0);
            }
        }
    }
}
