package fr.xebia.pokemon;

import akka.http.javadsl.Http;
import akka.http.javadsl.OutgoingConnection;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class AkkaPokemonRepositoryImpl implements AkkaPokemonRepository {

    private ActorMaterializer materializer;

    public AkkaPokemonRepositoryImpl(ActorMaterializer materializer) {
        this.materializer = materializer;
    }

    @Override
    public CompletionStage<Optional<Pokemon>> searchPokemon(String name) {
        //import akka.http.javadsl.ConnectHttp;
        /*final HttpRequest request = HttpRequest.GET("http://pokeapi.co/api/v2/pokemon/" + name);*/
        /*return Http.get(materializer.system())
                .singleRequest(request, materializer)
                .thenCompose(response -> PokemonBuilder.from(name, response, materializer));*/

        final HttpRequest request = HttpRequest.GET("/api/v2/pokemon/" + name);

        Flow<HttpRequest, HttpResponse, CompletionStage<OutgoingConnection>> connectionFlow =
                Http.get(materializer.system())
                        .outgoingConnection("pokeapi.co");

        return Source
                .single(request)
                .via(connectionFlow)
                .runWith(Sink.head(), materializer)
                .thenCompose(response -> PokemonBuilder.from(name, response, materializer));
    }
}
