package codes.recursive.repository;

import codes.recursive.model.BrainSession;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.PageableRepository;

@Repository
public interface BrainSessionRepository extends PageableRepository<BrainSession, Long> {}
