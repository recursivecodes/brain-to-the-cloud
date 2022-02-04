package codes.recursive.repository;

import codes.recursive.model.Game;
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
            value = "select g.id, g.match, g.created_on, g.is_highlighted, g.is_distracted, g.notes from game g where id in (select mvb.\"id\" from mv_game_details_with_brain mvb where mvb.totalAttention is not null) order by g.match.utcStartSeconds desc",
            nativeQuery = true,
            countQuery = "select count(id) from game where id in (select mvb.\"id\" from mv_game_details_with_brain mvb where mvb.totalAttention is not null)"
    )
    Page<Game> findAllWithBrainReadings(Pageable pageable);

    @Query(value = "select * from vw_summary_by_attention_range", nativeQuery = true)
    List<RangeSummaryDTO> getAttentionSummary();

    @Query(value = "select * from vw_summary_by_meditation_range", nativeQuery = true)
    List<RangeSummaryDTO> getMeditationSummary();

    @Query(value = "select * from vw_summary_by_time_moving", nativeQuery = true)
    List<RangeSummaryDTO> getTimeMovingSummary();

    @Query(value = "select * from vw_summary_by_time_moving_with_brain_data", nativeQuery = true)
    List<RangeSummaryDTO> getTimeMovingWithBrainDataSummary();

    @Query(value = "select * from vw_game_summary", nativeQuery = true)
    List<GameSummaryDTO> getGameSummary();

    @Query(value = "select * from vw_game_summary_by_map order by totalEdRatio desc", nativeQuery = true)
    List<GameSummaryDTO> getGameSummaryByMap();

    @Query(value = "select * from vw_game_summary_by_mode order by totalEdRatio desc", nativeQuery = true)
    List<GameSummaryDTO> getGameSummaryByMode();

    @Query(value = "select * from vw_game_summary_by_operator order by totalEdRatio desc", nativeQuery = true)
    List<GameSummaryDTO> getGameSummaryByOperator();

    @Query(value = "select * from vw_game_summary_by_team order by totalEdRatio desc", nativeQuery = true)
    List<GameSummaryDTO> getGameSummaryByTeam();

    @Query(value = "select * from vw_game_summary_by_map_with_brain order by totalEdRatio desc", nativeQuery = true)
    List<GameSummaryDTO> getGameSummaryByMapWithBrain();

    @Query(value = "select * from vw_game_summary_by_mode_with_brain order by totalEdRatio desc", nativeQuery = true)
    List<GameSummaryDTO> getGameSummaryByModeWithBrain();

    @Query(value = "select * from vw_game_summary_by_operator_with_brain order by totalEdRatio desc", nativeQuery = true)
    List<GameSummaryDTO> getGameSummaryByOperatorWithBrain();

    @Query(value = "select * from vw_game_summary_by_team_with_brain order by totalEdRatio desc", nativeQuery = true)
    List<GameSummaryDTO> getGameSummaryByTeamWithBrain();
}
