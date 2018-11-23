package ptp.ranklookup.lookup.impl.api;

import ptp.ranklookup.lookup.api.ERankGroup;
import ptp.ranklookup.lookup.api.IPlaylistStats;

public class PlaylistStats implements IPlaylistStats {

    private int mmr;
    private ERankGroup group;
    private Integer tier;
    private int division;

    PlaylistStats(int mmr, ERankGroup group, Integer tier, int division) {
        this.mmr = mmr;
        this.group = group;
        this.tier = tier;
        this.division = division;
    }

    @Override
    public int getMMR() {
        return mmr;
    }

    @Override
    public ERankGroup getRankGroup() {
        return group;
    }

    @Override
    public Integer getTier() {
        return tier;
    }

    @Override
    public int getDivision() {
        return division;
    }

    @Override
    public String toString() {
        return "{" +
                "mmr=" + mmr +
                ", group=" + group +
                ", tier=" + tier +
                ", division=" + division + '}';
    }
}
