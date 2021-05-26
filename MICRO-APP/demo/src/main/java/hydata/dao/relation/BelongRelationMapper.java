package hydata.dao.relation;

import hydata.model.Position;
import hydata.model.Symptom;
import hydata.model.relation.BelongRelation;
import hydata.model.relation.VisitDepartmentRelation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lhy13
 */
@Repository
public interface BelongRelationMapper extends Neo4jRepository<BelongRelation, Long> {
    /**
     * 新建症状-部位关系
     *
     * @param symptomCode 症状编码
     * @param parentName  部位名称
     * @return
     */
    @Query("MATCH (c:symptom{code:$symptomCode}),(p:position{name:$parentName})" +
            "CREATE (c)-[r:belong{relation:'属于'}]->(p) RETURN r")
    List<BelongRelation> createRel(@Param("symptomCode") String symptomCode,
                                            @Param("parentName") String parentName);

    /**
     * 根据症状查询部位
     * @param symptomName
     * @return
     */
    @Query("MATCH(symptom{name:$symptomName})-[r:belong]-(position) RETURN position")
    Position getBySymptomName(@Param("symptomName") String symptomName);


    /**
     * 根据部位查询症状
     * @param positionName
     * @return
     */
    @Query("MATCH(symptom)-[r:belong]-(position{name:$positionName}) RETURN symptom")
    List<Symptom> getAllByPositionName(@Param("positionName") String positionName);

}
