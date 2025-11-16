package com.xxmicloxx.NoteBlockAPI.utils;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

/**
 * Fields/methods for reflection &amp; version checking
 */
public class CompatibilityUtils {

    /**
     * Plays a sound using NMS &amp; reflection
     *
     * @param player
     * @param location
     * @param sound
     * @param category
     * @param volume
     * @param pitch
     * @param distance
     */
    public static void playSound(Player player, Location location, String sound,
                                 SoundCategory category, float volume, float pitch, float distance) {
        player.playSound(MathUtils.stereoPan(location, distance), Sound.valueOf(sound), category, volume, pitch);
    }

    /**
     * Plays a sound using NMS &amp; reflection
     *
     * @param player
     * @param location
     * @param sound
     * @param category
     * @param volume
     * @param pitch
     * @param distance
     */
    public static void playSound(Player player, Location location, Sound sound,
                                 SoundCategory category, float volume, float pitch, float distance) {
        player.playSound(MathUtils.stereoPan(location, distance), sound, category, volume, pitch);
    }
}
