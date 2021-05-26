package hydata.dao.relation;

import hydata.model.relation.HasSymptomRelation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 疾病症状
 *
 * @author lhy13
 */
@Repository
public interface HasSymptomRelationMapper extends Neo4jRepository<HasSymptomRelation, Long> {

    /**
     * 新建疾病症状从属关系
     *
     * @param diseaseCode 疾病编码
     * @param symptomCode 症状编码
     * @return
     */
    @Query("MATCH (c:disease{code:$diseaseCode}),(p:symptom{code:$symptomCode})" +
            "CREATE (c)-[r:hasSymptom{relation:'相关症状'}]->(p) RETURN r")
    List<HasSymptomRelation> createRel(@Param("diseaseCode") String diseaseCode,
                                       @Param("symptomCode") String symptomCode);
}
