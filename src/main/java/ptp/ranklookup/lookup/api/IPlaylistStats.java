package ptp.ranklookup.lookup.api;

import java.io.Serializable;

public interface IPlaylistStats extends Serializable {
    int getMMR();

    ERankGroup getRankGroup();

    Integer getTier();

    int getDivision();
}
