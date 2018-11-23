package ptp.ranklookup.http;

import org.glassfish.grizzly.utils.Exceptions;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ptp.ranklookup.lookup.api.EPlatform;
import ptp.ranklookup.lookup.api.IPlayer;
import ptp.ranklookup.lookup.api.IPlayerLookupService;
import ptp.ranklookup.util.ServiceRegistry;

@Path("/lookup")
public class PlayerLookupResource {

    @Path("{platform}/{name}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlayer(@PathParam("platform") String platform, @PathParam("name") String name) throws Exception {
        try {
            IPlayerLookupService service = ServiceRegistry.getService(IPlayerLookupService.class);

            EPlatform p = EPlatform.forName(platform);
            if (p == null) {
                return null;
            }

            return Response.status(200).entity(service.lookup(p, name)).build();
        }
        catch( Exception ex )
        {
            return Response.status(500).entity(Exceptions.getStackTraceAsString(ex)).build();
        }
    }
}
