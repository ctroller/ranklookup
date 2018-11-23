package ptp.ranklookup.lookup.api;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum EPlaylist {
    UNRANKED(0),
    RANKED_SOLO(1),
    RANKED_DUO(2),
    RANKED_STANDARD(3),
    RANKED_SOLO_STANDARD(4),
    RANKED_HOOPS(5),
    RANKED_RUMBLE(6),
    RANKED_SNOWDAY(7),
    RANKED_DROPSHOT(8);

    private static final Map<Integer, EPlaylist> LOOKUP = Stream.of(values())
            .collect(Collectors.toMap(EPlaylist::getPlaylistId, Function.identity()));


    private final int playlistId;

    EPlaylist( int playlistId )
    {
        this.playlistId = playlistId;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public static EPlaylist forId( int playlistId )
    {
        return LOOKUP.get(playlistId);
    }
}
