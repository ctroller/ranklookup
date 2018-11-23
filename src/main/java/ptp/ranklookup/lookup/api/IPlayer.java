package ptp.ranklookup.lookup.api;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;

public interface IPlayer extends Serializable {
    @NotNull
    String getName();

    @NotNull
    EPlatform getPlatform();

    @NotNull
    Map<EPlaylist, IPlaylistStats> getPlaylistStats ( int season );

    @NotNull
    Map<EPlaylist, IPlaylistStats> getLatestSeasonPlaylistStats ();

    @NotNull
    Map<Integer, Map<EPlaylist, IPlaylistStats>> getAllPlaylistStats ();
}
