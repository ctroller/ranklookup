package ptp.ranklookup.lookup.api;

import org.jetbrains.annotations.Nullable;

/**
 * Service interface to lookup players
 */
public interface IPlayerLookupService {
    @Nullable
    IPlayer lookup( EPlatform platform, String name );
}
