package codes.recursive.repository;

import codes.recursive.model.Game;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.PageableRepository;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.math.BigInteger;

@Repository
public abstract class AbstractGameRepository implements PageableRepository<Game, Long> {
    private final EntityManager entityManager;

    public AbstractGameRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Game getGameByMatchID(BigInteger matchID) {
        String sql = "select id, match, created_on as createdOn from game g where g.match.matchID = :matchID";
        return (Game) entityManager.createNativeQuery(sql)
                .unwrap(org.hibernate.query.NativeQuery.class)
                .setParameter("matchID", matchID)
                .addScalar("id", IntegerType.INSTANCE)
                .addScalar("match", StringType.INSTANCE)
                .addScalar("createdOn", DateType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(Game.class))
                .getSingleResult();
    }

    public Boolean matchExists(BigInteger matchID) {
        Game game;
        Boolean exists = true;
        try{
            game = getGameByMatchID(matchID);
        }
        catch(NoResultException e) {
            exists = false;
        }
        return exists;
    }

}
