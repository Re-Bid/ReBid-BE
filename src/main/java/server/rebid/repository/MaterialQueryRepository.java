package server.rebid.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import server.rebid.entity.Material;
import server.rebid.entity.QMaterial;

import java.util.List;

@Repository
public class MaterialQueryRepository {

    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;

    public MaterialQueryRepository(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
        this.entityManager = em;
    }


    public List<Material> getTotalMaterial() {
        QMaterial material = QMaterial.material;
        return queryFactory.selectFrom(material)
                .join(material.member).fetchJoin()
                .orderBy(material.createdAt.desc())
                .fetch();
    }
}
