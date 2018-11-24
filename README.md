# rankscraper
Java-Based Player Rank API that scrapes https://rocketleague.tracker.network/ for rank data at the moment.
Designed for easy implementation of different stats data providers (See interface `IPlayerDataProvider`).


Comes bundled with a built-in Jersey HTTP server (Grizzly HTTP) and support for Docker.


To make the HTTP Server work, simply rename the `application.properties.dist` file to `application.properties` and edit the listening property inside.



# Build
Requires maven to build - main class is `ptp.ranklookup.Application` which starts the HTTP server and registers the service registry (this is subject to change to the `ServiceLoader` java API, but I had problems making it work with Docker). 

# Extending
The API can be freely used and modified. 
The simplest way to use a different site to scrape or RL API is to implement your own `IPlayerDataProvider` class and register it with `ServiceRegistry.register( IPlayerDataProvider.class, new YourPlayerDataProvider() )`. The current `IPlayerLookupService` iterates over all `IPlayerDataProvider`s and breaks once it found a non-`null` value from one of the data providers.


# TODO
* Make the `ServiceRegistry` work with the Java `ServiceLoader` API instead of using an own, non-scaling implementation.
