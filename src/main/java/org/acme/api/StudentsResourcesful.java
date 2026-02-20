package org.acme.api;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.acme.domain.Students;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonArray;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/students/threads")
@ApplicationScoped
public class StudentsResourcesful {
    @Inject
    io.vertx.mutiny.ext.web.client.WebClient client;

    @GET
    @Path("/reactive")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<Students>> getAllReactive() {
        System.out.println("[REACTIVE] Thread: " + Thread.currentThread().getName());
        return client
            .getAbs("http://localhost:8081/api/students/all")
            .send()
            .onItem().transform(resp -> {
                System.out.println("[REACTIVE] Response on: " + Thread.currentThread().getName());
                return resp.bodyAsJsonArray().stream()
                    .map(obj -> ((io.vertx.core.json.JsonObject) obj).mapTo(Students.class))
                    .collect(Collectors.toList());
            });
    }

    @GET
    @Path("/blocking")
    @Blocking
    @Produces(MediaType.APPLICATION_JSON)
    public List<Students> getAllBlocking() {
        System.out.println("[BLOCKING] Thread: " + Thread.currentThread().getName());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return client
            .getAbs("http://localhost:8081/api/students/all")
            .send()
            .onItem().transform(resp -> {
                System.out.println("[BLOCKING] Response on: " + Thread.currentThread().getName());
                return resp.bodyAsJsonArray().stream()
                    .map(obj -> ((io.vertx.core.json.JsonObject) obj).mapTo(Students.class))
                    .collect(Collectors.toList());
            })
            .await().indefinitely();
    }

    @GET
    @Path("/virtual")
    @RunOnVirtualThread
    @Produces(MediaType.APPLICATION_JSON)
    public List<Students> getAllVirtualThread() {
        System.out.println("[VIRTUAL] Thread: " + Thread.currentThread().getName());
        System.out.println("[VIRTUAL] Is virtual: " + Thread.currentThread().isVirtual());
        return client
            .getAbs("http://localhost:8081/api/students/all")
            .send()
            .onItem().transform(resp -> {
                System.out.println("[VIRTUAL] Response on: " + Thread.currentThread().getName());
                return resp.bodyAsJsonArray().stream()
                    .map(obj -> ((io.vertx.core.json.JsonObject) obj).mapTo(Students.class))
                    .collect(Collectors.toList());
            })
            .await().indefinitely();
    }

    @GET
    @Path("/parallel")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Map<String, Object>> getAllParallel() {
        System.out.println("[PARALLEL] Thread: " + Thread.currentThread().getName());
        long start = System.currentTimeMillis();

        Uni<List<Students>> call1 = client
            .getAbs("http://localhost:8081/api/students/all")
            .send()
            .onItem().transform(resp -> resp.bodyAsJsonArray().stream()
                .map(obj -> ((io.vertx.core.json.JsonObject) obj).mapTo(Students.class))
                .collect(Collectors.toList()));

        Uni<List<Students>> call2 = client
            .getAbs("http://localhost:8081/api/students/all")
            .send()
            .onItem().transform(resp -> resp.bodyAsJsonArray().stream()
                .map(obj -> ((io.vertx.core.json.JsonObject) obj).mapTo(Students.class))
                .collect(Collectors.toList()));

        return Uni.combine().all().unis(call1, call2)
            .asTuple()
            .onItem().transform(tuple -> {
                long elapsed = System.currentTimeMillis() - start;
                System.out.println("[PARALLEL] Done in " + elapsed + "ms on: "
                    + Thread.currentThread().getName());
                return Map.of(
                    "source1_count", tuple.getItem1().size(),
                    "source2_count", tuple.getItem2().size(),
                    "students", tuple.getItem1(),
                    "elapsed_ms", elapsed,
                    "thread", Thread.currentThread().getName()
                );
            });
    }

    @GET
    @Path("/info")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Map<String, String>> threadInfo() {
        String callerThread = Thread.currentThread().getName();
        return Uni.createFrom().item(() -> Map.of(
            "thread_name", callerThread,
            "is_event_loop", String.valueOf(callerThread.contains("vert.x-eventloop")),
            "is_virtual", String.valueOf(Thread.currentThread().isVirtual()),
            "available_processors", String.valueOf(Runtime.getRuntime().availableProcessors())
        ));
    }
}