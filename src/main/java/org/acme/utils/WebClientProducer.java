package org.acme.utils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

@ApplicationScoped
public class WebClientProducer {
    @Inject
    io.vertx.mutiny.core.Vertx vertx;

    @Produces
    @ApplicationScoped
    public io.vertx.mutiny.ext.web.client.WebClient webClient() {
        return io.vertx.mutiny.ext.web.client.WebClient.create(vertx);
    }
}
