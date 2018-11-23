package ptp.ranklookup.lookup.impl.spi.data;

import ptp.ranklookup.lookup.spi.data.IPlaylistStatsData;

public class PlaylistStatsData implements IPlaylistStatsData {
    private int mmr;
    private int rankGroupId;
    private Integer tier;
    private int division;

    public PlaylistStatsData()
    {

    }

    @Override
    public int getMMR() {
        return mmr;
    }

    public void setMmr(int mmr) {
        this.mmr = mmr;
    }

    @Override
    public int getRankGroupId() {
        return rankGroupId;
    }

    public void setRankGroupId(int rankGroupId) {
        this.rankGroupId = rankGroupId;
    }

    @Override
    public Integer getTier() {
        return tier;
    }

    public void setTier(Integer tier) {
        this.tier = tier;
    }

    @Override
    public int getDivision() {
        return division;
    }

    public void setDivision(int division) {
        this.division = division;
    }
}
