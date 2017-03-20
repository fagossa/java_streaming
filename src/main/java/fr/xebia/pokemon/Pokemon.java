package fr.xebia.pokemon;

import java.util.List;

public class Pokemon {

    private String name;
    private List<String> abilities;

    public Pokemon(String name, List<String> abilities) {
        this.name = name;
        this.abilities = abilities;
    }

    public String getName() {
        return name;
    }

    public List<String> getAbilities() {
        return abilities;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "name='" + name + '\'' +
                ", abilities=" + abilities +
                '}';
    }
}
