package fr.xebia;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import fr.xebia.consumer.AkkaPokemonConsumer;
import fr.xebia.consumer.RxObservablePokemonConsumer;
import fr.xebia.pokemon.AkkaPokemonRepositoryImpl;
import fr.xebia.pokemon.Pokemon;
import fr.xebia.pokemon.RxPokemonRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Executor {

    private static Logger rxLogger = LoggerFactory.getLogger(RxObservablePokemonConsumer.class);
    private static Logger akkaLogger = LoggerFactory.getLogger(AkkaPokemonConsumer.class);

    public static void main(String... args) throws IOException {
        // Rx
        new RxObservablePokemonConsumer(new RxPokemonRepositoryImpl())
                .runUsing("pikachu", "bulbasaur")
                .subscribe(
                        maybePokemon -> maybePokemon.stream()
                                .forEach(e -> rxLogger.info("onNext : {}", e)),
                        error -> rxLogger.error("onError: {}", error),
                        () -> rxLogger.info("done!")
                );

        // Akka
        final ActorSystem system = ActorSystem.create();
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        new AkkaPokemonConsumer(new AkkaPokemonRepositoryImpl(materializer), materializer)
                .runUsing("pikachu", "bulbasaur")
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
