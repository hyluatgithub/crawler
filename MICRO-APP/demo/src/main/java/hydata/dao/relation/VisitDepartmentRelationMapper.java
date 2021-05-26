package hydata.dao.relation;

import hydata.model.relation.VisitDepartmentRelation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 就诊科室
 *
 * @author lhy13
 */
@Repository
public interface VisitDepartmentRelationMapper extends Neo4jRepository<VisitDepartmentRelation, Long> {

    /**
     * 新建科室从属关系
     *
     * @param symptomCode  疾病名称
     * @param parentName 主科室名称
     * @return
     */
    @Query("MATCH (c:symptom{code:$symptomCode}),(p:department{name:$parentName})" +
            "CREATE (c)-[r:visitDepartment{relation:'就诊科室'}]->(p) RETURN r")
    List<VisitDepartmentRelation> createRel(@Param("symptomCode") String symptomCode,
                                            @Param("parentName") String parentName);
}
