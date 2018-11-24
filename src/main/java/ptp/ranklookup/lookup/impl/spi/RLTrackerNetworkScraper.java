package ptp.ranklookup.lookup.impl.spi;

import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ptp.ranklookup.lookup.api.EPlatform;
import ptp.ranklookup.lookup.api.EPlaylist;
import ptp.ranklookup.lookup.api.ERankGroup;
import ptp.ranklookup.lookup.impl.spi.data.PlayerData;
import ptp.ranklookup.lookup.impl.spi.data.PlaylistStatsData;
import ptp.ranklookup.lookup.spi.IPlayerDataProvider;
import ptp.ranklookup.lookup.spi.data.IPlayerData;

public class RLTrackerNetworkScraper implements IPlayerDataProvider {
    private static final String BASE_URL = "https://rocketleague.tracker.network/profile/";

    // html pinpoints
    private static final String REWARD_LEVEL_ID = "Reward Level";

    // playlist names to id
    private enum EPlaylistName {
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
                .collect(Collectors.toMap(EPlaylistName::getPlaylistName, EPlaylistName::getPlaylistId));

        private final String playlistName;
        private final int playlistId;

        EPlaylistName(String name, int id) {
            playlistName = name;
            playlistId = id;
        }

        String getPlaylistName() {
            return playlistName;
        }

        int getPlaylistId() {
            return playlistId;
        }

        static int getPlaylistIdByName(String playlistName) {
            return LOOKUP.get(playlistName);
        }
    }

    @Override
    public @Nullable IPlayerData lookup(int platformId, String name) {
        return lookupPlayer(platformId, name);
    }

    private static IPlayerData lookupPlayer(int platformId, String name) {
        String url = getBaseUrl(platformId) + name;
        try {
            Document doc = Jsoup.connect( url )
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:41.0) Gecko/20100101 Firefox/41.0")
                    .timeout( 60000 )
                    .get();

            return parseResponse(platformId, doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch( RLTrackerException e )
        {
            throw new RLTrackerException( "URL: " + url + ", error: \n" + e.getMessage() );
        }

        return null;
    }

    private static IPlayerData parseResponse(int platformId, Document doc) {

        // check for errors
        Elements alertElements = doc.select(".alert.alert-danger");
        if (!alertElements.isEmpty()) {
            String appendedMessage = alertElements.stream()
                    .map(Element::text)
                    .collect(Collectors.joining("\n"));

            throw new RLTrackerException(appendedMessage);
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

    private static final Pattern PSTATS_PATTERN = Pattern.compile( "(Unranked|Bronze|Silver|Gold|Platinum|Diamond|Champion|Grand Champion) (I{1,3} )?Division (I{1,3}V?)" );
    private static void parsePlaylistRow(PlayerData returnValue, int season, Element row, boolean hasDivUpDownRows) {
        String playlistName = row.child(1)
                .ownText();
        int playlistId = getPlaylistId(playlistName);
        int childIndex = hasDivUpDownRows ? 3 : 2;
        int playlistMmr = Integer.parseInt(row.child(childIndex)
                .ownText()
                .replace(",", ""));

        Matcher playlistStatsMatcher = PSTATS_PATTERN.matcher( row.child(1).child(0).ownText() );
        if( playlistStatsMatcher.matches() ) {
            extractStatsData(returnValue, season, playlistId, playlistMmr, playlistStatsMatcher);
        }
    }

    private static void extractStatsData(PlayerData returnValue, int season, int playlistId, int playlistMmr, Matcher playlistStatsMatcher) {
        String rankGroup;
        String tier;
        String division;
        try {
            rankGroup = playlistStatsMatcher.group(1);
            tier = playlistStatsMatcher.group(2);
            if (tier != null) {
                tier = tier.trim();
            }

            division = playlistStatsMatcher.group(3);
        } catch (Exception ex) {
            rankGroup = ERankGroup.UNRANKED.getRankGroupName();
            tier = null;
            division = null;
        }


        Integer tierInt = tier != null ? romanToInt(tier) : null;
        int divisionInt = romanToInt(division);

        PlaylistStatsData data = new PlaylistStatsData();
        data.setMmr(playlistMmr);
        data.setDivision(divisionInt);
        data.setTier(tierInt);
        data.setRankGroupId(ERankGroup.forName(rankGroup)
                .getRankGroupId());
        returnValue.addPlaylistStats(season, playlistId, data);
    }

    private static final String ROMAN_1 = "I";
    private static final String ROMAN_2 = "II";
    private static final String ROMAN_3 = "III";
    private static final String ROMAN_4 = "IV";
    private static int romanToInt( String roman )
    {
        if( roman == null )
        {
            return 1;
        }

       switch( roman )
       {
           case ROMAN_1:
               return 1;
           case ROMAN_2:
               return 2;
           case ROMAN_3:
               return 3;
           case ROMAN_4:
               return 4;
           default: return -1;
       }
    }

    private static int getPlaylistId(String playlistName) {
        return EPlaylistName.getPlaylistIdByName(playlistName);
    }

    private static String getBaseUrl(int platformId) {
        String returnValue = BASE_URL;
        switch (EPlatform.forId(platformId)) {
            case STEAM:
                returnValue += "steam/";
                break;
            case PSN:
                returnValue += "ps/";
                break;
            case XBOX:
                returnValue += "xbox/";
                break;
            default:
                throw new IllegalArgumentException(String.format("Platform %s (id %d) not supported.", EPlatform.forId(platformId), platformId));
        }

        return returnValue;
    }
}
