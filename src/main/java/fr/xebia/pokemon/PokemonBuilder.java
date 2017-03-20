package fr.xebia.pokemon;

import akka.http.javadsl.model.HttpEntity;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.StatusCodes;
import akka.stream.Materializer;
import com.jayway.jsonpath.JsonPath;
import javaslang.control.Try;

import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class PokemonBuilder {

    public static Optional<Pokemon> buildFrom(String name, Response response) {
        final Optional<String> maybeJson = Optional.of(response)
                .filter(resp -> resp.getStatus() == 200)
                .map(resp -> response.readEntity(String.class));

        return parseContentIntoPokemon(name, maybeJson);
    }

    public static CompletionStage<Optional<Pokemon>> from(String name, HttpResponse response, Materializer materializer) {
        final Optional<CompletionStage<String>> completionStage = Optional.of(response)
                .filter(resp -> StatusCodes.OK.equals(resp.status()) || StatusCodes.MOVED_PERMANENTLY.equals(resp.status()))
                .map(HttpResponse::entity)
                .map(responseEntity ->
                        responseEntity.toStrict(5_000, materializer))
                .map(PokemonBuilder::decodeStringContent);
        return completionStage
                .map(stage ->
                        stage.thenApply(content ->
                                parseContentIntoPokemon(name, Optional.ofNullable(content))))
                .orElseGet(() ->
                        CompletableFuture.supplyAsync(Optional::empty));
    }

    private static Optional<Pokemon> parseContentIntoPokemon(String name, Optional<String> maybeJsonXX) {
        return Try
                .of(() -> maybeJsonXX)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(json ->
                        Optional.of(new Pokemon(name, JsonPath.read(json, "$.abilities[*].ability.name"))))
                .recover((ex) -> Optional.empty()).get();
    }

    private static CompletionStage<String> decodeStringContent(CompletionStage<HttpEntity.Strict> eventual) {
        return eventual
                .thenApply(HttpEntity.Strict::getData)
                .thenApply(byteString -> byteString.decodeString("UTF-8"));
    }
}
