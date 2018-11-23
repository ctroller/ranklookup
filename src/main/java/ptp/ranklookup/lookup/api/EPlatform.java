package ptp.ranklookup.lookup.api;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum EPlatform {
    STEAM(1, "steam"),
    PSN(2, "psn" ),
    XBOX(3, "xbox");

    private static final Map<Integer, EPlatform> LOOKUP = Stream.of(values())
            .collect(Collectors.toMap(EPlatform::getPlatformId, Function.identity()));
    private static final Map<String, EPlatform> NAME_LOOKUP = Stream.of(values())
            .collect(Collectors.toMap(EPlatform::getPlatformName, Function.identity()));

    private final int platformId;
    private final String platformName;

    EPlatform(int platformId, String platformName) {
        this.platformId = platformId;
        this.platformName = platformName;
    }

    public int getPlatformId() {
        return platformId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public static EPlatform forId(int platformId) {
        return LOOKUP.get(platformId);
    }

    public static EPlatform forName(String platformName) {
        return NAME_LOOKUP.get(platformName);
    }
}
