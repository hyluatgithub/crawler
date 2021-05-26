package hydata.dao;

import hydata.model.Position;
import hydata.model.Symptom;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 部位
 *
 * @author lhy13
 */
@Repository
public interface PositionMapper extends Neo4jRepository<Position, Long> {

}
