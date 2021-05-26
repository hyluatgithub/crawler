package hydata.model.relation;

import hydata.model.Department;
import org.neo4j.ogm.annotation.*;

/**
 * 关联：子科室-关联-上级科室
 *
 * @author lhy13
 */
@RelationshipEntity(type = "subDepartment")
public class SubDepartmentRelation {
    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private Department startNode;
    @EndNode
    private Department endNode;

    @Property(name = "relation")
    private String relation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Department getStartNode() {
        return startNode;
    }

    public void setStartNode(Department startNode) {
        this.startNode = startNode;
    }

    public Department getEndNode() {
        return endNode;
    }

    public void setEndNode(Department endNode) {
        this.endNode = endNode;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    @Override
    public String toString() {
        return "SubDepartmentRelation{" +
                "id=" + id +
                ", startNode=" + startNode +
                ", endNode=" + endNode +
                ", relation='" + relation + '\'' +
                '}';
    }
}
