package com.xxmicloxx.NoteBlockAPI.songplayer;

import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.event.*;
import com.xxmicloxx.NoteBlockAPI.model.FadeType;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.model.playmode.ChannelMode;
import com.xxmicloxx.NoteBlockAPI.model.playmode.MonoMode;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Plays a Song for a list of Players
 */
public abstract class SongPlayer {

    private static final Random RANDOM = new Random();

    protected Song song;
    protected Playlist playlist;
    protected int actualSong;

    protected boolean playing;
    protected boolean fading;
    protected int tick = -1;

    protected boolean autoDestroy;

    protected byte volume = 100;
    protected Fade fadeIn;
    protected Fade fadeOut;
    protected Fade fadeTemp = null;
    protected RepeatMode repeat = RepeatMode.NO;
    protected boolean random;

    protected List<Song> playedSongs = new ArrayList<>();

    protected NoteBlockAPI plugin;
    private BukkitTask task;

    protected SoundCategory soundCategory;
    protected ChannelMode channelMode = new MonoMode();
    protected boolean enable10Octave;

    public SongPlayer(Song song) {
        this(new Playlist(song), SoundCategory.MASTER);
    }

    public SongPlayer(Song song, SoundCategory soundCategory) {
        this(new Playlist(song), soundCategory);
    }

    public SongPlayer(Song song, SoundCategory soundCategory, boolean random) {
        this(new Playlist(song), soundCategory, random);
    }

    public SongPlayer(Playlist playlist) {
        this(playlist, SoundCategory.MASTER);
    }

    public SongPlayer(Playlist playlist, SoundCategory soundCategory) {
        this(playlist, soundCategory, false);
    }

    public SongPlayer(Playlist playlist, SoundCategory soundCategory, boolean random) {
        this.playlist = playlist;
        this.random = random;
        this.soundCategory = soundCategory;
        this.plugin = NoteBlockAPI.getAPI();

        fadeIn = new Fade(FadeType.NONE, 60);
        fadeIn.setFadeStart((byte) 0);
        fadeIn.setFadeTarget(volume);

        fadeOut = new Fade(FadeType.NONE, 60);
        fadeOut.setFadeStart(volume);
        fadeOut.setFadeTarget((byte) 0);

        if (random) {
            actualSong = RANDOM.nextInt(playlist.getCount());
        }
        this.song = playlist.get(actualSong);
    }

    /**
     * Check if 6 octave range is enabled
     *
     * @return true if enabled, false otherwise
     */
    public boolean isEnable10Octave() {
        return enable10Octave;
    }

    /**
     * Enable or disable 6 octave range
     * <p>
     * If not enabled, notes will be transposed to 2 octave range
     *
     * @param enable10Octave true if enabled, false otherwise
     */
    public void setEnable10Octave(boolean enable10Octave) {
        this.enable10Octave = enable10Octave;
    }

    /**
     * Tick function called by the scheduler
     */
    private void tick() {
        if (playing || fading) {
            if (fadeTemp != null) {
                if (fadeTemp.isDone()) {
                    fadeTemp = null;
                    fading = false;

                    if (!playing) {
                        SongStopEvent event = new SongStopEvent(this);
                        plugin.getPlugin().getServer().getPluginManager().callEvent(event);
                        volume = fadeIn.getFadeTarget();
                        return;
                    }
                } else {
                    int fade = fadeTemp.calculateFade();

                    if (fade != -1) {
                        volume = (byte) fade;
                    }
                }
            } else if (tick < fadeIn.getFadeDuration()) {
                int fade = fadeIn.calculateFade();

                if (fade != -1) {
                    volume = (byte) fade;
                }
            } else if (tick >= song.getLength() - fadeOut.getFadeDuration()) {
                int fade = fadeOut.calculateFade();

                if (fade != -1) {
                    volume = (byte) fade;
                }
            }
            tick++;

            if (tick > song.getLength()) {
                tick = -1;
                fadeIn.setFadeDone(0);
                fadeOut.setFadeDone(0);
                volume = fadeIn.getFadeTarget();

                if (repeat == RepeatMode.ONE) {
                    SongLoopEvent event = new SongLoopEvent(this);
                    plugin.getPlugin().getServer().getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        return;
                    }
                } else {
                    if (random) {
                        playedSongs.add(song);
                        List<Song> left = new ArrayList<>();

                        for (Song playlistSong : playlist.getSongList()) {
                            if (!playedSongs.contains(playlistSong)) {
                                left.add(playlistSong);
                            }
                        }

                        if (left.isEmpty()) {
                            playedSongs.clear();
                            left.addAll(playlist.getSongList());
                            song = left.get(RANDOM.nextInt(left.size()));
                            actualSong = playlist.getIndex(song);

                            if (repeat == RepeatMode.ALL) {
                                SongLoopEvent event = new SongLoopEvent(this);
                                plugin.getPlugin().getServer().getPluginManager().callEvent(event);

                                if (!event.isCancelled()) {
                                    return;
                                }
                            }
                        } else {
                            song = left.get(RANDOM.nextInt(left.size()));
                            actualSong = playlist.getIndex(song);

                            SongNextEvent event = new SongNextEvent(this);
                            plugin.getPlugin().getServer().getPluginManager().callEvent(event);
                            return;
                        }
                    } else {
                        if (playlist.hasNext(actualSong)) {
                            actualSong++;
                            song = playlist.get(actualSong);

                            SongNextEvent event = new SongNextEvent(this);
                            plugin.getPlugin().getServer().getPluginManager().callEvent(event);
                            return;
                        } else {
                            actualSong = 0;
                            song = playlist.get(actualSong);

                            if (repeat == RepeatMode.ALL) {
                                SongLoopEvent event = new SongLoopEvent(this);
                                plugin.getPlugin().getServer().getPluginManager().callEvent(event);

                                if (!event.isCancelled()) {
                                    return;
                                }
                            }
                        }
                    }
                }
                playing = false;
                SongEndEvent event = new SongEndEvent(this);
                plugin.getPlugin().getServer().getPluginManager().callEvent(event);

                if (autoDestroy) {
                    destroy();
                }
                return;
            }
            for (Player player : plugin.getPlugin().getServer().getOnlinePlayers()) {
                playTick(player);
            }
        }
    }

    /**
     * Returns {@link Fade} for Fade in effect
     *
     * @return Fade
     */
    public Fade getFadeIn() {
        return fadeIn;
    }

    /**
     * Returns {@link Fade} for Fade out effect
     *
     * @return Fade
     */
    public Fade getFadeOut() {
        return fadeOut;
    }

    /**
     * Returns whether the SongPlayer is set to destroy itself when no one is listening
     * or when the Song ends
     *
     * @return if autoDestroy is enabled
     */
    public boolean getAutoDestroy() {
        return autoDestroy;
    }

    /**
     * Sets whether the SongPlayer is going to destroy itself when no one is listening
     * or when the Song ends
     *
     * @param autoDestroy if autoDestroy is enabled
     */
    public void setAutoDestroy(boolean autoDestroy) {
        this.autoDestroy = autoDestroy;
    }

    public abstract void playTick(Player player);

    /**
     * SongPlayer will destroy itself
     */
    public void destroy() {
        SongPlayerDestroyEvent event = new SongPlayerDestroyEvent(this);
        plugin.getPlugin().getServer().getPluginManager().callEvent(event);

        playing = false;
        playedSongs.clear();
        setTick(-1);

        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    /**
     * Returns whether the SongPlayer is actively playing
     *
     * @return is playing
     */
    public boolean isPlaying() {
        return playing;
    }

    /**
     * Sets whether the SongPlayer is playing
     *
     * @param playing
     */
    public void setPlaying(boolean playing) {
        setPlaying(playing, null);
    }

    /**
     * Sets whether the SongPlayer is playing and whether it should fade if previous value was different
     *
     * @param playing
     * @param fade
     */
    public void setPlaying(boolean playing, boolean fade) {
        setPlaying(playing, fade ? (playing ? fadeIn : fadeOut) : null);
    }

    public void setPlaying(boolean playing, Fade fade) {
        if (this.playing == playing) {
            return;
        }
        if (task != null) {
            task.cancel();
        }
        task = plugin.getPlugin().getServer().getScheduler().runTaskTimer(plugin.getPlugin(),
                this::tick,
                0L,
                (long) song.getDelay()
        );
        this.playing = playing;

        if (fade != null && fade.getType() != FadeType.NONE) {
            fadeTemp = new Fade(fade.getType(), fade.getFadeDuration());
            fadeTemp.setFadeStart(playing ? 0 : volume);
            fadeTemp.setFadeTarget(playing ? volume : 0);
            fading = true;
        } else {
            fading = false;
            fadeTemp = null;
            volume = fadeIn.getFadeTarget();

            if (!playing) {
                SongStopEvent event = new SongStopEvent(this);
                plugin.getPlugin().getServer().getPluginManager().callEvent(event);
            }
        }
    }

    /**
     * Gets the current tick of this SongPlayer
     *
     * @return
     */
    public int getTick() {
        return tick;
    }

    /**
     * Sets the current tick of this SongPlayer
     *
     * @param tick
     */
    public void setTick(int tick) {
        this.tick = tick;
    }

    /**
     * Gets the current volume of this SongPlayer
     *
     * @return volume (0-100)
     */
    public byte getVolume() {
        return volume;
    }

    /**
     * Sets the current volume of this SongPlayer
     *
     * @param volume (0-100)
     */
    public void setVolume(byte volume) {
        if (volume > 100) {
            volume = 100;
        } else if (volume < 0) {
            volume = 0;
        }
        this.volume = volume;
        fadeIn.setFadeTarget(volume);
        fadeOut.setFadeStart(volume);

        if (fadeTemp != null) {
            if (playing) {
                fadeTemp.setFadeTarget(volume);
            } else {
                fadeTemp.setFadeStart(volume);
            }
        }
    }

    /**
     * Gets the Song being played by this SongPlayer
     *
     * @return
     */
    public Song getSong() {
        return song;
    }

    /**
     * Gets the Playlist being played by this SongPlayer
     *
     * @return
     */
    public Playlist getPlaylist() {
        return playlist;
    }

    /**
     * Sets the Playlist being played by this SongPlayer. Will affect next Song
     */
    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    /**
     * Get index of actually played {@link Song} in {@link Playlist}
     *
     * @return
     */
    public int getPlayedSongIndex() {
        return actualSong;
    }

    /**
     * Start playing {@link Song} at specified index in {@link Playlist}
     * If there is no {@link Song} at this index, {@link SongPlayer} will continue playing current song
     *
     * @param index
     */
    public void playSong(int index) {
        if (playlist.exist(index)) {
            song = playlist.get(index);
            actualSong = index;
            tick = -1;
            fadeIn.setFadeDone(0);
            fadeOut.setFadeDone(0);
        }
    }

    /**
     * Start playing {@link Song} that is next in {@link Playlist} or random {@link Song} from {@link Playlist}
     */
    public void playNextSong() {
        tick = song.getLength();
    }

    /**
     * Gets the SoundCategory of this SongPlayer
     *
     * @return SoundCategory of this SongPlayer
     * @see SoundCategory
     */
    public SoundCategory getCategory() {
        return soundCategory;
    }

    /**
     * Sets the SoundCategory for this SongPlayer
     *
     * @param soundCategory
     */
    public void setCategory(SoundCategory soundCategory) {
        this.soundCategory = soundCategory;
    }

    /**
     * Sets SongPlayer's {@link RepeatMode}
     *
     * @param repeatMode
     */
    public void setRepeatMode(RepeatMode repeatMode) {
        this.repeat = repeatMode;
    }

    /**
     * Gets SongPlayer's {@link RepeatMode}
     *
     * @return
     */
    public RepeatMode getRepeatMode() {
        return repeat;
    }

    /**
     * Sets whether the SongPlayer will choose next song from player randomly
     *
     * @param random
     */
    public void setRandom(boolean random) {
        this.random = random;
    }

    /**
     * Gets whether the SongPlayer will choose next song from player randomly
     *
     * @return is random
     */
    public boolean isRandom() {
        return random;
    }

    public ChannelMode getChannelMode() {
        return channelMode;
    }
}
