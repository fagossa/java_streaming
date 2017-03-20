package fr.xebia.consumer;

import fr.xebia.pokemon.AkkaPokemonRepository;
import fr.xebia.pokemon.Pokemon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class AkkaPokemonConsumer {

    private AkkaPokemonRepository pokemonRepository;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public AkkaPokemonConsumer(AkkaPokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    public void run(String name) {
        final CompletionStage<Optional<Pokemon>> stage = pokemonRepository.searchPokemon(name);
        stage.whenComplete((maybePokemon, error) -> {
            if (error == null) {
                logger.info("onNext : {}", maybePokemon);
                logger.info("done!");
            } else {
                logger.error("onError : " + error);
            }
        });
    }
}
