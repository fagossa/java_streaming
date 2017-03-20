package fr.xebia.consumer;

import fr.xebia.pokemon.Pokemon;
import fr.xebia.pokemon.RxPokemonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import java.util.Optional;

public class RxObservablePokemonConsumer {

    private RxPokemonRepository pokemonRepository;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public RxObservablePokemonConsumer(RxPokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    public Observable<Optional<Pokemon>> runUsing(String name) {
        logger.info("Querying remote service using RX");
        Observable<Optional<Pokemon>> async = pokemonRepository.searchPokemon(name);
        async.subscribe(
                maybePokemon -> logger.info("onNext: {}", maybePokemon),
                error -> logger.error("onError: {}", error),
                () -> logger.info("done!")
        );
        return async;
    }

}
