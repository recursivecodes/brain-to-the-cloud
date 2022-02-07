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

    @Query(value = "select * from mv_brain_details_with_game where game_id = :gameId order by created_on asc", nativeQuery = true)
    List<Brain> getBrainForMatch(Long gameId);

}
