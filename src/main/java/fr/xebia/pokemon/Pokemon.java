package fr.xebia.pokemon;

import com.jayway.jsonpath.JsonPath;
import javaslang.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

public class Pokemon {

    private static Logger logger = LoggerFactory.getLogger(Pokemon.class);

    private String name;

    private List<String> abilities;

    static Optional<Pokemon> buildFrom(String name, Response response) {
        final Optional<String> maybeJson = Optional.of(response)
                .filter(resp -> resp.getStatus() == 200)
                .map(resp -> response.readEntity(String.class));

        return Try.of(() -> maybeJson.map(json -> {
            final Pokemon pokemon = new Pokemon();
            pokemon.name = name;
            pokemon.abilities = JsonPath.read(json, "$.abilities[*].ability.name");
            return pokemon;
        })).recover((ex) -> {
            logger.error("Error parsing response", ex);
            return Optional.empty();
        }).get();
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
