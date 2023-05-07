package me.bright.mbingo.games;

public enum GameMode {
    SOLO("Одиночный"),
    TEAM("Командный");

    private String name;
    GameMode(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }
}
