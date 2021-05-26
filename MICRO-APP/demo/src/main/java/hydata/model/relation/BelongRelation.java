package hydata.model.relation;

import hydata.model.Position;
import hydata.model.Symptom;
import org.neo4j.ogm.annotation.*;

/**
 * 关联：症状-关联-部位
 */
@RelationshipEntity(type = "belong")
public class BelongRelation {
    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private Symptom startNode;
    @EndNode
    private Position endNode;

    @Property(name = "relation")
    private String relation;

}
