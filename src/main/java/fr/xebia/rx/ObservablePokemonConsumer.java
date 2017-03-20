package fr.xebia.rx;

import fr.xebia.pokemon.Pokemon;
import fr.xebia.pokemon.PokemonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import java.util.Optional;

public class ObservablePokemonConsumer {

    private PokemonRepository pokemonRepository;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public ObservablePokemonConsumer(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    public void run(String name) {
        Observable<Optional<Pokemon>> async = pokemonRepository.searchPokemon(name);
        async.subscribe(
                result -> logger.info("onNext : " + result),
                error -> logger.error("onError : " + error),
                () -> logger.info("done!")
        );
    }

}
