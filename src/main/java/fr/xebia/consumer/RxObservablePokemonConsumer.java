package fr.xebia.consumer;

import fr.xebia.pokemon.Pokemon;
import fr.xebia.pokemon.RxPokemonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RxObservablePokemonConsumer {

    private RxPokemonRepository pokemonRepository;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public RxObservablePokemonConsumer(RxPokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    public Observable<List<Pokemon>> runUsing(String... names) {
        logger.info("Querying remote service using RX");

        return Observable
                .from(Arrays.asList(names))
                .flatMap(name -> pokemonRepository.searchPokemon(name))
                .toList()
                .map(list -> list.stream()
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList()));
    }

}
