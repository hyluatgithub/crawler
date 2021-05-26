package hydata.dao;

import hydata.model.Department;
import hydata.model.Position;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lhy13
 */
@Repository
public interface DepartmentMapper extends Neo4jRepository<Department, Long> {

}
