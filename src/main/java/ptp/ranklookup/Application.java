package ptp.ranklookup;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;

import javax.net.ssl.SSLContext;

import ptp.ranklookup.lookup.api.IPlayerLookupService;
import ptp.ranklookup.lookup.impl.api.PlayerLookupService;
import ptp.ranklookup.lookup.impl.spi.RLTrackerNetworkScraper;
import ptp.ranklookup.lookup.spi.IPlayerDataProvider;
import ptp.ranklookup.util.ServiceRegistry;

public class Application {

    private static Properties getProperties() throws IOException {
        Properties returnValue = new Properties();

        returnValue.load(Files.newInputStream(Paths.get("application.properties")));
        return returnValue;
    }

    public static void main(String... args) throws Exception {
        Properties props = getProperties();
        String listenAddr = props.getProperty("listen");
        System.setProperty("https.protocols", "TLSv1.2");

        ServiceRegistry.register(IPlayerLookupService.class, new PlayerLookupService());
        ServiceRegistry.register(IPlayerDataProvider.class, new RLTrackerNetworkScraper());


        final ResourceConfig rc = new ResourceConfig().packages("ptp.ranklookup.http");

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(listenAddr), rc);
        System.in.read();
        server.shutdown();
    }
}
