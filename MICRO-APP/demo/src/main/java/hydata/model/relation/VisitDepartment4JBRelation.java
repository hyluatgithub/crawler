package hydata.model.relation;

import hydata.model.Department;
import hydata.model.Disease;
import hydata.model.Symptom;
import org.neo4j.ogm.annotation.*;

/**
 * 关联：症状-就诊科室-科室
 */
@RelationshipEntity(type = "visitDepartment4jb")
public class VisitDepartment4JBRelation {
    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private Disease startNode;
    @EndNode
    private Department endNode;

    @Property(name = "relation")
    private String relation;

}
