package hydata.dao.relation;

import hydata.model.Disease;
import hydata.model.Position;
import hydata.model.relation.FocusRelation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author lhy13
 */
@Repository
public interface FocusRelationMapper extends Neo4jRepository<FocusRelation, Long> {
    /**
     * 新建症状-部位关系
     *
     * @param diseaseCode 症状编码
     * @param parentName  部位名称
     * @return
     */
    @Query("MATCH (c:disease{code:$diseaseCode}),(p:position{name:$parentName})" +
            "CREATE (c)-[r:focus{relation:'病变'}]->(p) RETURN r")
    List<FocusRelation> createRel(@Param("diseaseCode") String diseaseCode,
                                  @Param("parentName") String parentName);

    /**
     * 根据疾病查询部位
     *
     * @param diseaseName
     * @return
     */
    @Query("MATCH(disease{name:$diseaseName})-[r:focus]-(position) RETURN position")
    Position getByDiseaseName(@Param("diseaseName") String diseaseName);


    /**
     * 根据部位查询疾病
     *
     * @param positionName
     * @return
     */
    @Query("MATCH(disease)-[r:focus]-(position{name:$positionName}) RETURN disease")
    List<Disease> getAllByPositionName(@Param("positionName") String positionName);

}
