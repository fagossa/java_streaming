package fr.xebia.consumer;

import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import fr.xebia.pokemon.AkkaPokemonRepository;
import fr.xebia.pokemon.Pokemon;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class AkkaPokemonConsumer {

    private AkkaPokemonRepository pokemonRepository;
    private ActorMaterializer materializer;

    public AkkaPokemonConsumer(AkkaPokemonRepository pokemonRepository, ActorMaterializer materializer) {
        this.pokemonRepository = pokemonRepository;
        this.materializer = materializer;
    }

    public CompletionStage<List<Pokemon>> runUsing(String... names) {
        return Source
                .from(Arrays.asList(names))
                .mapAsync(2, name -> pokemonRepository.searchPokemon(name))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toMat(Sink.seq(), Keep.right())
                .run(materializer);
    }

}
