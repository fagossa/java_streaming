package fr.xebia;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import fr.xebia.consumer.AkkaPokemonConsumer;
import fr.xebia.pokemon.AkkaPokemonRepositoryImpl;
import fr.xebia.pokemon.RxPokemonRepositoryImpl;
import fr.xebia.consumer.RxObservablePokemonConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Executor {

    private static Logger logger = LoggerFactory.getLogger(Executor.class);

    public static void main(String... args) throws IOException {
        logger.info("Running Rx consumer...");
        final String pokemon = "pikachu";

        new RxObservablePokemonConsumer(new RxPokemonRepositoryImpl())
                .run(pokemon);

        logger.info("Running Akka consumer...");
        final ActorSystem system = ActorSystem.create();
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        new AkkaPokemonConsumer(new AkkaPokemonRepositoryImpl(materializer))
                .run(pokemon);
    }

}
