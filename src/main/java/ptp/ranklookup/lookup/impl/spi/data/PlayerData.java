package ptp.ranklookup.lookup.impl.spi.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import ptp.ranklookup.lookup.api.EPlaylist;
import ptp.ranklookup.lookup.spi.data.IPlayerData;

public class PlayerData implements IPlayerData {

    private String name;
    private int platformId;
    private Map<Integer, Map<Integer, Integer>> playlistStats = new LinkedHashMap<>(EPlaylist.values().length);

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getPlatformId() {
        return platformId;
    }

    public void setPlatformId(int platformId) {
        this.platformId = platformId;
    }

    @Override
    public Map<Integer, Map<Integer, Integer>> getPlaylistStats() {
        return playlistStats;
    }

    public void addPlaylistStats( int season, int playlistId, int mmr )
    {
        playlistStats.computeIfAbsent(season, k -> new HashMap<>(5))
                .put(playlistId, mmr);
    }
}
