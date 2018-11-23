package ptp.ranklookup.lookup.spi.data;

import java.util.Map;

public interface IPlayerData {
    String getName ();

    int getPlatformId ();

    Map<Integer, Map<Integer, IPlaylistStatsData>> getPlaylistStats();
}
