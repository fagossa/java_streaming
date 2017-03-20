package fr.xebia.pokemon;

import rx.Observable;

import java.util.Optional;

public interface PokemonRepository {

    Observable<Optional<Pokemon>> searchPokemon(String name);

}
