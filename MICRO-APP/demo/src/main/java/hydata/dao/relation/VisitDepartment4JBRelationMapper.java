package hydata.dao.relation;

import hydata.model.relation.VisitDepartment4JBRelation;
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
public interface VisitDepartment4JBRelationMapper extends Neo4jRepository<VisitDepartment4JBRelation, Long> {

    /**
     * 新建科室从属关系
     *
     * @param diseaseCode 疾病名称
     * @param parentName  主科室名称
     * @return
     */
    @Query("MATCH (c:disease{code:$diseaseCode}),(p:department{name:$parentName})" +
            "CREATE (c)-[r:visitDepartment4jb{relation:'就诊科室'}]->(p) RETURN r")
    List<VisitDepartment4JBRelation> createRel(@Param("diseaseCode") String diseaseCode,
                                            @Param("parentName") String parentName);
}
