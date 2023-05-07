package me.bright.mbingo.database;

public enum LDbType {
    HOSTPORT("hostport"),
    PLAYERS_COUNT("players"),
    GAME_MODE("mode"),
    STATUS("status"),
    GAMENAME("gamename"),
    UUID("uuid"),
    KIILS("kills"),
    DEATHS("deaths"),
    GAMES("games"),
    WINS("wins");


    private String dbStringName;
    LDbType(String dbStringName) {
        this.dbStringName = dbStringName;
    }

    public String getDbStringName() {
        return dbStringName;
    }
}
