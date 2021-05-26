package hydata.dao;

import hydata.model.Disease;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 疾病
 *
 * @author lhy13
 */
@Repository
public interface DiseaseMapper extends Neo4jRepository<Disease, Long> {

    @Query("MATCH (n:disease) RETURN n ")
    List<Disease> getList();

    @Query("MATCH (n:disease{code:$code}) RETURN n ")
    List<Disease> findByCode(@Param("code") String code);
}
