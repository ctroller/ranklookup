package ptp.ranklookup.lookup.impl.api;

import net.jcip.annotations.Immutable;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ptp.ranklookup.lookup.api.EPlatform;
import ptp.ranklookup.lookup.api.EPlaylist;
import ptp.ranklookup.lookup.api.ERankGroup;
import ptp.ranklookup.lookup.api.IPlayer;
import ptp.ranklookup.lookup.api.IPlaylistStats;
import ptp.ranklookup.lookup.spi.data.IPlayerData;
import ptp.ranklookup.lookup.spi.data.IPlaylistStatsData;

@Immutable
public class Player implements IPlayer {
    private final String name;
    private final EPlatform platform;
    private Map<Integer, Map<EPlaylist, IPlaylistStats>> playlistStats;
    private int latestSeason = -1;


    Player(IPlayerData data) {
        name = data.getName();
        platform = EPlatform.forId(data.getPlatformId());
        playlistStats = computePlaylistStats(data.getPlaylistStats());
    }

    private Map<Integer, Map<EPlaylist, IPlaylistStats>> computePlaylistStats(Map<Integer, Map<Integer, IPlaylistStatsData>> playlistStats) {
        Map<Integer, Map<EPlaylist, IPlaylistStats>> returnValue = new HashMap<>();
        int latestSeason = -1;
        for (Map.Entry<Integer, Map<Integer, IPlaylistStatsData>> outer : playlistStats.entrySet()) {
            int season = outer.getKey();
            if (season > latestSeason) {
                latestSeason = season;
            }

            Map<EPlaylist, IPlaylistStats> seasonMap = new HashMap<>();
            returnValue.put(season, seasonMap);
            for (Map.Entry<Integer, IPlaylistStatsData> seasonStats : outer.getValue()
                    .entrySet()) {
                seasonMap.put(EPlaylist.forId(seasonStats.getKey()), computeStats( seasonStats.getValue() ) );
            }
        }

        this.latestSeason = latestSeason;

        return returnValue;
    }

    private IPlaylistStats computeStats(IPlaylistStatsData value) {
        return new PlaylistStats( value.getMMR(), ERankGroup.forId( value.getRankGroupId() ), value.getTier(), value.getDivision() );
    }

    @Override
    @NotNull
    public String getName() {
        return name;
    }

    @Override
    @NotNull
    public EPlatform getPlatform() {
        return platform;
    }

    @Override
    public @NotNull Map<EPlaylist, IPlaylistStats> getPlaylistStats(int season) {
        return playlistStats.getOrDefault(season, Collections.emptyMap());
    }

    @Override
    public @NotNull Map<EPlaylist, IPlaylistStats> getLatestSeasonPlaylistStats() {
        return getPlaylistStats(latestSeason);
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", platform=" + platform +
                ", playlistStats=" + getPlaylistStatsString() +
                '}';
    }

    private String getPlaylistStatsString() {
        StringBuilder returnValue = new StringBuilder();
        for (Map.Entry<Integer, Map<EPlaylist, IPlaylistStats>> entry : playlistStats.entrySet()) {
            returnValue.append("\nSeason ")
                    .append(entry.getKey())
                    .append(": \n");
            for (Map.Entry<EPlaylist, IPlaylistStats> stats : entry.getValue()
                    .entrySet()) {
                returnValue.append("    ");
                returnValue.append(stats.getKey())
                        .append(": ")
                        .append(stats.getValue())
                        .append("\n");
            }
        }

        return returnValue.toString();
    }
}
