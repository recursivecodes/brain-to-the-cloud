package codes.recursive.repository;

import codes.recursive.model.Brain;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.PageableRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public interface BrainRepository extends PageableRepository<Brain, Long> {
    CompletableFuture<Brain> saveAsync(@NonNull Brain reading);

    @Query("from Brain where (toLocalTimestamp(created_on) between epochToLocalTimestamp(:startTime) and epochToLocalTimestamp(:endTime)) and signal_strength = 100 order by created_on")
    List<Brain> getBrainForMatch(Integer startTime, Integer endTime);

}
