package codes.recursive.repository;

import codes.recursive.model.Game;
import codes.recursive.model.GameDTO;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.PageableRepository;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

@Repository
public interface GameRepository extends PageableRepository<Game, Long> {
    CompletableFuture<Game> saveAsync(@NonNull Game reading);

    @Query("FROM Game g where g.match.matchID = :matchID")
    Game getGameByMatchID(BigInteger matchID);

    Page<Game> findAllByIsHighlightedEqual(Boolean isHighlighted, Pageable pageable);

    void updateIsHighlighted(@Id Long id, Boolean isHighlighted);

    void updateIsDistracted(@Id Long id, Boolean isDistracted);

    @Query(
            value = "select * FROM Game g order by g.match.utcStartSeconds desc",
            nativeQuery = true,
            countQuery = "select count(g.id) FROM Game g"
    )
    Page<Game> findAll(Pageable pageable);

    @Query(
            value = "select g.id, to_char(g.match) as match, g.is_highlighted, g.is_distracted, g.created_on from game g left outer join brain b on toLocalTimestamp(b.created_on) >= epochToLocalTimestamp(g.match.utcStartSeconds) and toLocalTimestamp(b.created_on) <= epochToLocalTimestamp(g.match.utcEndSeconds) group by g.id, to_char(g.match), g.is_highlighted, g.is_distracted, g.match.utcStartSeconds, g.created_on having case when count(b.id) > 0 then 1 else 0 end > 0 order by g.match.utcStartSeconds desc",
            nativeQuery = true,
            countQuery = "select count(distinct g.id) from game g left outer join brain b on toLocalTimestamp(b.created_on) >= epochToLocalTimestamp(g.match.utcStartSeconds) and toLocalTimestamp(b.created_on) <= epochToLocalTimestamp(g.match.utcEndSeconds) group by g.id, to_char(g.match), g.is_highlighted, g.match.utcStartSeconds, g.created_on having case when count(b.id) > 0 then 1 else 0 end > 0"
    )
    Page<Game> findAllWithBrainReadings(Pageable pageable);

}
