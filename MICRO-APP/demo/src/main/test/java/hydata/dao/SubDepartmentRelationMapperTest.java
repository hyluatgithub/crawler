package hydata.dao;

import hydata.Application;
import hydata.dao.relation.SubDepartmentRelationMapper;
import hydata.model.Department;
import hydata.model.relation.SubDepartmentRelation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = Application.class)
class SubDepartmentRelationMapperTest {

    @Autowired
    private SubDepartmentRelationMapper subDepartmentRelationMapper;

    @Test
    void getAll() {
        Iterable<SubDepartmentRelation> all = subDepartmentRelationMapper.findAll();
        all.forEach(System.out::println);
    }

    @Test
    void save() {
        Department department = new Department();
        department.setCode("huxineike");

        Department department2 = new Department();
        department2.setCode("neike");


        SubDepartmentRelation subDepartmentRelation = new SubDepartmentRelation();
        subDepartmentRelation.setStartNode(department);
        subDepartmentRelation.setEndNode(department2);
        subDepartmentRelation.setRelation("从属科室");
        subDepartmentRelationMapper.save(subDepartmentRelation);
    }

    @Test
    void add() {
        String childName = "呼吸内科";
        String parentName = "内科";
        List<SubDepartmentRelation> rel = subDepartmentRelationMapper.createRel(childName, parentName);
        rel.forEach(System.out::println);

    }

}