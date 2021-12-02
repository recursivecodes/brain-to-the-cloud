package codes.recursive.repository;

import codes.recursive.model.Brain;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.PageableRepository;

import java.util.concurrent.CompletableFuture;

@Repository
public interface BrainRepository extends PageableRepository<Brain, Long> {
    CompletableFuture<Brain> saveAsync(@NonNull Brain reading);
}
