package hydata.dao.relation;

import hydata.model.relation.FormRelation;
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
public interface FormRelationMapper extends Neo4jRepository<FormRelation, Long> {
    /**
     * 新建部位-部位关系
     *
     * @param childName  部位名称
     * @param parentName 部位名称
     * @return
     */
    @Query("MATCH (c:position{name:$childName}),(p:position{name:$parentName})" +
            "CREATE (c)-[r:form{relation:'组成'}]->(p) RETURN r")
    List<FormRelation> createRel(@Param("childName") String childName,
                                 @Param("parentName") String parentName);
}
