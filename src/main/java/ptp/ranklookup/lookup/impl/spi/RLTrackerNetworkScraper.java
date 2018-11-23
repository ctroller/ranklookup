package ptp.ranklookup.lookup.impl.spi;

import org.jetbrains.annotations.Nullable;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ptp.ranklookup.lookup.api.EPlatform;
import ptp.ranklookup.lookup.api.EPlaylist;
import ptp.ranklookup.lookup.impl.spi.data.PlayerData;
import ptp.ranklookup.lookup.spi.IPlayerDataProvider;
import ptp.ranklookup.lookup.spi.data.IPlayerData;

public class RLTrackerNetworkScraper implements IPlayerDataProvider {
    private static final String BASE_URL = "https://rocketleague.tracker.network/profile/";

    // html pinpoints
    private static final String REWARD_LEVEL_ID = "Reward Level";

    // playlist names to id
    private enum PlaylistName {
        UNRANKED("Un-Ranked", EPlaylist.UNRANKED.getPlaylistId()),
        SOLO("Ranked Duel 1v1", EPlaylist.RANKED_SOLO.getPlaylistId()),
        DOUBLES("Ranked Doubles 2v2", EPlaylist.RANKED_DUO.getPlaylistId()),
        STANDARD("Ranked Standard 3v3", EPlaylist.RANKED_STANDARD.getPlaylistId()),
        SOLO_STANDARD("Ranked Solo Standard 3v3", EPlaylist.RANKED_SOLO_STANDARD.getPlaylistId()),
        DROPSHOT("Dropshot", EPlaylist.RANKED_DROPSHOT.getPlaylistId()),
        RUMBLE("Rumble", EPlaylist.RANKED_RUMBLE.getPlaylistId()),
        HOOPS("Hoops", EPlaylist.RANKED_HOOPS.getPlaylistId()),
        SNOWDAY("Snowday", EPlaylist.RANKED_SNOWDAY.getPlaylistId());

        private static final Map<String, Integer> LOOKUP = Stream.of(values())
                .collect(Collectors.toMap(PlaylistName::getPlaylistName, PlaylistName::getPlaylistId));

        private final String playlistName;
        private final int playlistId;

        PlaylistName(String name, int id) {
            playlistName = name;
            playlistId = id;
        }

        public String getPlaylistName() {
            return playlistName;
        }

        public int getPlaylistId() {
            return playlistId;
        }

        public static int getPlaylistIdByName(String playlistName) {
            return LOOKUP.get(playlistName);
        }
    }

    @Override
    public @Nullable IPlayerData lookup(int platformId, String name) {
        return lookupPlayer(platformId, name);
    }

    private static IPlayerData lookupPlayer(int platformId, String name) {
        try {
            Connection.Response response = Jsoup.connect(getBaseUrl(platformId) + name)
                    .execute();

            return parseResponse(platformId, response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static IPlayerData parseResponse(int platformId, Connection.Response response) throws IOException {
        Document doc = response.parse();

        // check for errors
        Elements alertElements = doc.select(".alert.alert-danger");
        if (!alertElements.isEmpty()) {
            String appendedMessage = alertElements.stream()
                    .map(Element::text)
                    .collect(Collectors.joining("\n"));

            throw new RLTrackerException(appendedMessage + "\nUrl: " + response.url()
                    .toString());
        }

        return parsePlayer(platformId, doc);
    }

    private static IPlayerData parsePlayer(int platformId, Document doc) {
        PlayerData returnValue = new PlayerData();

        String playerName = doc.select(".info h1.name")
                .first()
                .ownText();

        returnValue.setName(playerName);
        returnValue.setPlatformId(platformId);

        Elements seasonTables = doc.getElementsByClass("season-table");
        parseSeasonTables(returnValue, seasonTables);

        return returnValue;
    }

    private static void parseSeasonTables(PlayerData returnValue, Elements seasonTables) {
        for (Element seasonTable : seasonTables) {
            int season = Integer.parseInt(seasonTable.id()
                    .replace("season-", ""));

            Elements tableData = seasonTable.select(".card-table.items");
            for (Element table : tableData) {
                if (REWARD_LEVEL_ID.equals(table.select("thead tr th")
                        .first()
                        .ownText())) {
                    continue;
                }

                parsePlaylistTable(returnValue, season, table);
            }
        }
    }

    private static void parsePlaylistTable(PlayerData returnValue, int season, Element table) {
        Elements rows = table.select("tbody tr");
        boolean hasDivUpDownRows = !table.select( "thead th:contains(Div Down)" ).isEmpty();

        for (Element row : rows) {
            parsePlaylistRow(returnValue, season, row, hasDivUpDownRows);
        }

    }

    private static void parsePlaylistRow(PlayerData returnValue, int season, Element row, boolean hasDivUpDownRows) {
        String playlistName = row.child(1)
                .ownText();
        int playlistId = getPlaylistId(playlistName);
        int childIndex = hasDivUpDownRows ? 3 : 2;
        int playlistMmr = Integer.parseInt(row.child(childIndex)
                .ownText()
                .replace(",", ""));

        returnValue.addPlaylistStats(season, playlistId, playlistMmr);
    }

    private static int getPlaylistId(String playlistName) {
        return PlaylistName.getPlaylistIdByName(playlistName);
    }

    private static String getBaseUrl(int platformId) {
        String returnValue = BASE_URL;
        switch (EPlatform.forId(platformId)) {
            case STEAM:
                returnValue += "steam/";
                break;
            case PS4:
                returnValue += "steam/";
                break;
            case XBOX:
                returnValue += "steam/";
                break;
            default:
                throw new IllegalArgumentException(String.format("Platform %s (id %d) not supported.", EPlatform.forId(platformId), platformId));
        }

        return returnValue;
    }
}
