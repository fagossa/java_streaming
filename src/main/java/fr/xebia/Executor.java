package fr.xebia;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import fr.xebia.consumer.AkkaPokemonConsumer;
import fr.xebia.consumer.RxObservablePokemonConsumer;
import fr.xebia.pokemon.AkkaPokemonRepositoryImpl;
import fr.xebia.pokemon.RxPokemonRepositoryImpl;

import java.io.IOException;

public class Executor {

    public static void main(String... args) throws IOException {
        final String pokemon = "pikachu";

        // Rx
        new RxObservablePokemonConsumer(new RxPokemonRepositoryImpl())
                .runUsing(pokemon);

        // Akka
        final ActorSystem system = ActorSystem.create();
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        new AkkaPokemonConsumer(new AkkaPokemonRepositoryImpl(materializer))
                .runUsing(pokemon)
                .whenComplete((maybePokemon, error) -> system.shutdown());
    }

}
