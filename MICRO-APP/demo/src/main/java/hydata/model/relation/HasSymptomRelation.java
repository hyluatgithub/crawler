package hydata.model.relation;

import hydata.model.Disease;
import hydata.model.Position;
import hydata.model.Symptom;
import org.neo4j.ogm.annotation.*;

/**
 * 关联：疾病-关联-症状
 */
@RelationshipEntity(type = "hasSymptom")
public class HasSymptomRelation {
    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private Disease startNode;
    @EndNode
    private Symptom endNode;

    @Property(name = "relation")
    private String relation;

}
