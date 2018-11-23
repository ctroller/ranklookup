package ptp.ranklookup.lookup.api;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface IPlayer {
    @NotNull
    String getName();

    @NotNull
    EPlatform getPlatform();

    @NotNull
    Map<EPlaylist, IPlaylistStats> getPlaylistStats ( int season );

    @NotNull
    Map<EPlaylist, IPlaylistStats> getLatestSeasonPlaylistStats ();
}
