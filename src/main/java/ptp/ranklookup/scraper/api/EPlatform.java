package ptp.ranklookup.scraper.api;

public enum EPlatform {
    STEAM(1),
    PS4(2),
    XBOX(3),
    SWITCH(4);

    private final int platformId;

    EPlatform(int platformId) {
        this.platformId = platformId;
    }

    public int getPlatformId() {
        return platformId;
    }
}
