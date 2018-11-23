package ptp.ranklookup.lookup.api;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum EPlatform {
    STEAM(1),
    PS4(2),
    XBOX(3),
    SWITCH(4);

    private static final Map<Integer, EPlatform> LOOKUP = Stream.of(values())
            .collect(Collectors.toMap(EPlatform::getPlatformId, Function.identity()));

    private final int platformId;

    EPlatform(int platformId) {
        this.platformId = platformId;
    }

    public int getPlatformId() {
        return platformId;
    }

    public static EPlatform forId(int platformId) {
        return LOOKUP.get(platformId);
    }
}
