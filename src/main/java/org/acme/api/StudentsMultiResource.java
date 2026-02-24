package org.acme.api;

import org.acme.domain.Students;

import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

@Path("/api/students/multi/threads")
@ApplicationScoped
public class StudentsMultiResource {
    @Inject
    io.vertx.mutiny.ext.web.client.WebClient client;

    @GET
    @Path("/reactive")
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<Students> getAllReactive(){
        System.out.println("[REACTIVE] Thread: " + Thread.currentThread().getName());
        return client
            .getAbs("http://localhost:8081/api/students/all")
            .send()
            .onItem().transformToMulti(resp -> {
                System.out.println("[REACTIVE] Response on: " + Thread.currentThread().getName());
                return Multi.createFrom().items(resp.bodyAsJsonArray().stream()
                    .map(obj -> ((io.vertx.core.json.JsonObject) obj).mapTo(Students.class)));
            });
    }

    
}
