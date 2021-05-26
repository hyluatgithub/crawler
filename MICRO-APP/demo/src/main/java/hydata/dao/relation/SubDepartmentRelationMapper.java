package hydata.dao.relation;

import hydata.model.relation.SubDepartmentRelation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lhy13
 */
@Repository
public interface SubDepartmentRelationMapper extends Neo4jRepository<SubDepartmentRelation, Long> {

    /**
     * 新建科室从属关系
     *
     * @param childName  子科室名称
     * @param parentName 主科室名称
     * @return
     */
    @Query("MATCH (c:department{name:$childName}),(p:department{name:$parentName})" +
            "MERGE (c)-[r:subDepartment{relation:'从属科室'}]->(p) RETURN r")
    List<SubDepartmentRelation> createRel(@Param("childName") String childName,
                                          @Param("parentName") String parentName);
}
