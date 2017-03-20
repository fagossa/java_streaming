package fr.xebia.pokemon;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface AkkaPokemonRepository {

    CompletionStage<Optional<Pokemon>> searchPokemon(String name);

}
