package hydata.model.relation;

import hydata.model.Position;
import hydata.model.Symptom;
import org.neo4j.ogm.annotation.*;

/**
 * 关联：部位组成
 */
@RelationshipEntity(type = "form")
public class FormRelation {
    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private Position startNode;
    @EndNode
    private Position endNode;

    @Property(name = "relation")
    private String relation;

}
