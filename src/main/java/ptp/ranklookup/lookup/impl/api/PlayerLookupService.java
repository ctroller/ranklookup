package ptp.ranklookup.lookup.impl.api;

import org.jetbrains.annotations.Nullable;

import ptp.ranklookup.lookup.api.EPlatform;
import ptp.ranklookup.lookup.api.IPlayer;
import ptp.ranklookup.lookup.api.IPlayerLookupService;
import ptp.ranklookup.lookup.spi.IPlayerDataProvider;
import ptp.ranklookup.lookup.spi.data.IPlayerData;
import ptp.ranklookup.util.ServiceRegistry;

public class PlayerLookupService implements IPlayerLookupService {

    @Override
    public @Nullable IPlayer lookup(EPlatform platform, String name) {
        IPlayer returnValue = null;

        IPlayerData data = null;
        for (IPlayerDataProvider provider : ServiceRegistry.getServices(IPlayerDataProvider.class)) {
            if ((data = provider.lookup(platform.getPlatformId(), name)) != null) {
                break;
            }
        }

        if (data != null) {
            returnValue = new Player(data);
        }

        return returnValue;
    }
}
