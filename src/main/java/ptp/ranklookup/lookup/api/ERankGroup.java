package ptp.ranklookup.lookup.api;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ERankGroup {
    UNRANKED(0, "Unranked"),
    BRONZE(1, "Bronze"),
    SILVER(2, "Silver"),
    GOLD(3, "Gold"),
    PLATINUM(4, "Platinum"),
    DIAMOND(5, "Diamond"),
    CHAMPION(6, "Champion"),
    GRAND_CHAMPION(7, "Grand Champion");

    private final int rankGroupId;
    private final String rankGroupName;

    private static final Map<Integer, ERankGroup> LOOKUP = Stream.of(values())
            .collect(Collectors.toMap(ERankGroup::getRankGroupId, Function.identity()));

    private static final Map<String, ERankGroup> NAME_LOOKUP = Stream.of(values())
            .collect(Collectors.toMap(ERankGroup::getRankGroupName, Function.identity()));

    ERankGroup( int rankGroupId, String rankGroupName )
    {
        this.rankGroupId = rankGroupId;
        this.rankGroupName = rankGroupName;
    }

    public int getRankGroupId() {
        return rankGroupId;
    }

    public String getRankGroupName() {
        return rankGroupName;
    }

    public static ERankGroup forId(int rankGroupId )
    {
        return LOOKUP.get( rankGroupId );
    }

    public static ERankGroup forName(String name)
    {
        return NAME_LOOKUP.get(name);
    }
}
