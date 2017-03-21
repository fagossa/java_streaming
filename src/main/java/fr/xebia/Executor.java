package fr.xebia;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import fr.xebia.consumer.AkkaPokemonService;
import fr.xebia.consumer.RxObservablePokemonService;
import fr.xebia.pokemon.AkkaPokemonRepositoryImpl;
import fr.xebia.pokemon.Pokemon;
import fr.xebia.pokemon.RxPokemonRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Executor {

    private static Logger rxLogger = LoggerFactory.getLogger(RxObservablePokemonService.class);
    private static Logger akkaLogger = LoggerFactory.getLogger(AkkaPokemonService.class);

    public static void main(String... args) throws IOException {
        // Rx
        new RxObservablePokemonService(new RxPokemonRepositoryImpl())
                .fetchPokemons("pikachu", "bulbasaur")
                .subscribe(
                        maybePokemon -> maybePokemon.stream()
                                .forEach(e -> rxLogger.info("onNext : {}", e)),
                        error -> rxLogger.error("onError: {}", error),
                        () -> rxLogger.info("done!")
                );

        // Akka
        final ActorSystem system = ActorSystem.create();
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        new AkkaPokemonService(new AkkaPokemonRepositoryImpl(materializer), materializer)
                .fetchPokemons("pikachu", "bulbasaur")
                .whenComplete((maybePokemon, error) -> {
                    system.shutdown();
                    if (error == null) {
                        maybePokemon.stream()
                                .map(Pokemon::toString)
                                .forEach(e -> akkaLogger.info("onNext : {}", e));
                        akkaLogger.info("done!");
                    } else {
                        akkaLogger.error("onError : " + error);
                    }
                });
    }

}
