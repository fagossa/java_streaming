package fr.xebia;

import fr.xebia.pokemon.PokemonRepositoryImpl;
import fr.xebia.rx.ObservablePokemonConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Executor {

    private static Logger logger = LoggerFactory.getLogger(Executor.class);

    public static void main(String... args) throws IOException {
        logger.info("Running service...");
        new ObservablePokemonConsumer(new PokemonRepositoryImpl())
                .run("pikachu");
    }

}
