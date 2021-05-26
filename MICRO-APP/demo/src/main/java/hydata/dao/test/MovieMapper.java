package hydata.dao.test;

import hydata.model.test.Movie;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lhy13
 */
@Repository
public interface MovieMapper extends Neo4jRepository<Movie, Long> {

    @Query("MATCH (n:Movie) WHERE n.title=$title RETURN n")
    List<Movie> findByTitle(@Param("title") String title);
}
