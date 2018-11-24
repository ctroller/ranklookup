# rankscraper
Java-Based Player Rank API that scrapes https://rocketleague.tracker.network/ for rank data at the moment.
Designed for easy implementation of different stats data providers (See interface IPlayerDataProvider).


Comes bundled with a built-in Jersey HTTP server (Grizzly HTTP) and support for Docker.


To make the HTTP Server work, simply rename the '''application.properties.dist''' file to '''application.properties''' and edit the listening property inside.



# Build
Requires maven to build - main class is '''Application''' which starts the HTTP server

