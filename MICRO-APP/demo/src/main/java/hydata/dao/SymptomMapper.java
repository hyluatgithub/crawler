package hydata.dao;

import hydata.model.Symptom;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lhy13
 */
@Repository
public interface SymptomMapper extends Neo4jRepository<Symptom, Long> {

    @Query("MATCH (n:symptom) RETURN n ")
    List<Symptom> getList();

    @Query("MATCH (n:symptom{code:$code}) RETURN n ")
    List<Symptom> findByCode(@Param("code") String code);
}
