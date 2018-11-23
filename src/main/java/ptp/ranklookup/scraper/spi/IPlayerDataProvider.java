package ptp.ranklookup.scraper.spi;

import org.jetbrains.annotations.Nullable;

import ptp.ranklookup.scraper.api.EPlatform;
import ptp.ranklookup.scraper.spi.data.IPlayerData;

/**
 * Service provider interface to look up player ranks
 */
public interface IPlayerDataProvider {

    /**
     * Searches this player data provider for given player on given platform.
     *
     * @param platformId the platform to look on
     * @param name     the name of the player (e.g. steamid, ps4 username, ...)
     * @return the found player data, or {@code null} if not found
     */
    @Nullable
    IPlayerData lookup(int platformId, String name);
}
