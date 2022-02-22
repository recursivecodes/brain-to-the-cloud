package codes.recursive.repository;

import codes.recursive.model.Game;
import codes.recursive.model.GameDetailDTO;
import codes.recursive.model.GameSummaryDTO;
import codes.recursive.model.RangeSummaryDTO;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.PageableRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public interface GameRepository extends PageableRepository<Game, Long> {
    CompletableFuture<Game> saveAsync(@NonNull Game reading);

    @Query("FROM Game g where g.match.matchID = :matchID")
    Game getGameByMatchID(BigInteger matchID);

    Page<Game> findAllByIsHighlightedEqual(Boolean isHighlighted, Pageable pageable);

    void updateIsHighlighted(@Id Long id, Boolean isHighlighted);

    void updateIsDistracted(@Id Long id, Boolean isDistracted);

    void updateNotes(@Id Long id, String notes);

    @Query(
            value = "select * FROM Game g order by g.match.utcStartSeconds desc",
            nativeQuery = true,
            countQuery = "select count(g.id) FROM Game g"
    )
    Page<Game> findAll(Pageable pageable);

    @Query(
            value = "select g.id, g.match, g.created_on, g.is_highlighted, g.is_distracted, g.notes from game g where id in (select mvb.\"id\" from mv_game_details_with_brain mvb where mvb.brainRecords > 0) order by g.match.utcStartSeconds desc",
            nativeQuery = true,
            countQuery = "select count(id) from game where id in (select mvb.\"id\" from mv_game_details_with_brain mvb where mvb.brainRecords > 0)"
    )
    Page<Game> findAllWithBrainReadings(Pageable pageable);

    @Query(
            value = "select * from mv_game_details_with_brain where isHighlighted = 1 order by matchStart desc",
            nativeQuery = true,
            countQuery = "select count(1) from mv_game_details_with_brain where isHighlighted = 1"
    )
    Page<GameDetailDTO> listHighlightedGameDetails(Pageable pageable);

    @Query(
            value = "select * from mv_game_details_with_brain where brainRecords > 0 order by matchStart desc",
            nativeQuery = true,
            countQuery = "select count(1) from mv_game_details_with_brain where brainRecords > 0"
    )
    Page<GameDetailDTO> listGameDetailsWithBrain(Pageable pageable);

    @Query(
            value = "select * from mv_game_details_with_brain order by matchStart desc",
            nativeQuery = true,
            countQuery = "select count(1) from mv_game_details_with_brain"
    )
    Page<GameDetailDTO> listGameDetails(Pageable pageable);

    @Query(value = "select * from rangeSummary(:type)", nativeQuery = true)
    List<RangeSummaryDTO> getBrainSummary(String type);

    @Query(value = "select * from timeMovingSummary(:withBrain)", nativeQuery = true)
    List<RangeSummaryDTO> getTimeMovingSummary(Integer withBrain);

    @Query(value = "select * from gameSummary(:withBrain)", nativeQuery = true)
    List<GameSummaryDTO> getGameSummary(Integer withBrain);

    @Query(value = "select * from summaryByType(:type, :withBrain) order by totalEdRatio desc", nativeQuery = true)
    List<GameSummaryDTO> getGameSummaryByType(String type, Integer withBrain);

    @Query(value = "select * from summaryByType('map', 0) order by totalEdRatio desc", nativeQuery = true)
    List<GameSummaryDTO> getGameSummaryByMap();

    @Query(value = "select * from summaryByType('mode', 0) order by totalEdRatio desc", nativeQuery = true)
    List<GameSummaryDTO> getGameSummaryByMode();

    @Query(value = "select * from summaryByType('operator', 0) order by totalEdRatio desc", nativeQuery = true)
    List<GameSummaryDTO> getGameSummaryByOperator();

    @Query(value = "select * from summaryByType('team', 0) order by totalEdRatio desc", nativeQuery = true)
    List<GameSummaryDTO> getGameSummaryByTeam();

    @Query(value = "select * from summaryByType('map', 1) order by totalEdRatio desc", nativeQuery = true)
    List<GameSummaryDTO> getGameSummaryByMapWithBrain();

    @Query(value = "select * from summaryByType('mode', 1) order by totalEdRatio desc", nativeQuery = true)
    List<GameSummaryDTO> getGameSummaryByModeWithBrain();

    @Query(value = "select * from summaryByType('operator', 1) order by totalEdRatio desc", nativeQuery = true)
    List<GameSummaryDTO> getGameSummaryByOperatorWithBrain();

    @Query(value = "select * from summaryByType('team', 1) order by totalEdRatio desc", nativeQuery = true)
    List<GameSummaryDTO> getGameSummaryByTeamWithBrain();
}
