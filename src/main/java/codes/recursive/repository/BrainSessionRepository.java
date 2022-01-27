package codes.recursive.repository;

import codes.recursive.model.BrainDetailsDTO;
import codes.recursive.model.BrainSession;
import codes.recursive.model.BrainSummaryDTO;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.PageableRepository;

import java.util.List;

@Repository
public interface BrainSessionRepository extends PageableRepository<BrainSession, Long> {

    @Query(value = "select * from vw_brain_session_details where id = :sessionId", nativeQuery = true)
    List<BrainDetailsDTO> listBrainSessionDetails(Long sessionId);

    @Query(value = "select * from vw_brain_session_summary", nativeQuery = true)
    List<BrainSummaryDTO> listBrainSessionSummaries();

}
