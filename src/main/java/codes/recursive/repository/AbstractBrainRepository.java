package codes.recursive.repository;

import codes.recursive.model.Brain;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.PageableRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Repository
public abstract class AbstractBrainRepository implements PageableRepository<Brain, Long> {
    private final EntityManager entityManager;

    public AbstractBrainRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void deleteBrainByBrainSessionId(Long brainSessionId) {
        String sql = "delete from Brain b where b.id in (select ib.id from brain ib left outer join brain_session bs on bs.started_at <= ib.created_on and bs.ended_at >= ib.created_on where bs.id = :brainSessionId)";
            entityManager.createNativeQuery(sql)
                .setParameter("brainSessionId", brainSessionId)
                .executeUpdate();
    }

}
