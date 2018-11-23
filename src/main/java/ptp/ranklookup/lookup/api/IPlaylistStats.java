package ptp.ranklookup.lookup.api;

public interface IPlaylistStats {
    int getMMR();

    ERankGroup getRankGroup();

    Integer getTier();

    int getDivision();
}
