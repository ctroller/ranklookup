package ptp.ranklookup;

import ptp.ranklookup.lookup.api.EPlatform;
import ptp.ranklookup.lookup.api.IPlayer;
import ptp.ranklookup.lookup.api.IPlayerLookupService;
import ptp.ranklookup.lookup.spi.IPlayerDataProvider;
import ptp.ranklookup.util.ServiceRegistry;

public class Application {

    public static void main( String... args )
    {
        IPlayerLookupService lookup = ServiceRegistry.getService(IPlayerLookupService.class);

        IPlayer player = lookup.lookup(EPlatform.STEAM, "trawks");
        System.out.println( player );
    }
}
