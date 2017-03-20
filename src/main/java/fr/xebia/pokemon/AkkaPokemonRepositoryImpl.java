package fr.xebia.pokemon;

import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;
import akka.stream.ActorMaterializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class AkkaPokemonRepositoryImpl implements AkkaPokemonRepository {

    private ActorMaterializer materializer;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public AkkaPokemonRepositoryImpl(ActorMaterializer materializer) {
        this.materializer = materializer;
    }

    @Override
    public CompletionStage<Optional<Pokemon>> searchPokemon(String name) {
        logger.info("Querying remote service using Akka-Http");
        final String url = String.format("http://pokeapi.co/api/v2/pokemon/%s/", name);
        final HttpRequest request = HttpRequest.GET(url);
        return Http.get(materializer.system())
                .singleRequest(request, materializer)
                .thenCompose(response -> PokemonBuilder.from(name, response, materializer));
    }
}
