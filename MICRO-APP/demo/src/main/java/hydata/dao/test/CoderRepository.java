package hydata.dao.test;

import hydata.model.test.Coder;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoderRepository extends Neo4jRepository<Coder, Long> {

    @Query("MATCH (n:Coder) RETURN n ")
    List<Coder> getCoderList();

    @Query("CREATE (n:Coder{name:{name},sex:{sex}}) RETURN n")
    List<Coder> addCoderList(@Param("name") String name, @Param("sex") String sex);
}
