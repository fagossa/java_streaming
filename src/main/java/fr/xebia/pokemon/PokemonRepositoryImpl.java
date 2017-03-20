package fr.xebia.pokemon;

import org.glassfish.jersey.client.rx.Rx;
import org.glassfish.jersey.client.rx.RxClient;
import org.glassfish.jersey.client.rx.rxjava.RxObservableInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import java.util.Optional;

public class PokemonRepositoryImpl implements PokemonRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private RxClient<RxObservableInvoker> newRxClient = Rx.newClient(RxObservableInvoker.class);

    @Override
    public Observable<Optional<Pokemon>> searchPokemon(String name) {
        return newRxClient
                .target("http://pokeapi.co/api/v2/pokemon/" + name)
                .request()
                .rx()
                .get()
                .map(response -> Pokemon.buildFrom(name, response))
                .onErrorReturn(throwable -> {
                    logger.warn("Error: {}, getting pokemon: {}", throwable.getMessage(), name);
                    return Optional.empty();
                });
    }
}
