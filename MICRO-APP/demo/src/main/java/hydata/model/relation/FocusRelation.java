package hydata.model.relation;

import hydata.model.Disease;
import hydata.model.Position;
import org.neo4j.ogm.annotation.*;

/**
 * 关联病灶：疾病-关联-部位
 */
@RelationshipEntity(type = "focus")
public class FocusRelation {
    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private Disease startNode;
    @EndNode
    private Position endNode;

    @Property(name = "relation")
    private String relation;

}
