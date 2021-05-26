package hydata.dao;

import hydata.Application;
import hydata.model.Department;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = Application.class)
class DepartmentMapperTest {
    @Autowired
    private DepartmentMapper departmentMapper;

    @Test
    void save() {
        List<Department> departments = initData();
        departments.forEach(e -> {
            departmentMapper.save(e);
        });
    }

    @Test
    void saveChild() {
        List<Department> departments = initChildData();
        departments.forEach(e -> {
            departmentMapper.save(e);
        });
    }

    private List<Department> initData() {
        List<Department> departments = new ArrayList<>();
        Department department;

        department = new Department();
        department.setCode("neike");
        department.setName("内科");
        department.setLevel(0);
        departments.add(department);

        department = new Department();
        department.setCode("waike");
        department.setName("外科");
        department.setLevel(0);
        departments.add(department);

        department = new Department();
        department.setCode("fuchanke");
        department.setName("妇产科");
        department.setLevel(0);
        departments.add(department);

        department = new Department();
        department.setCode("chuanranke");
        department.setName("传染科");
        department.setLevel(0);
        departments.add(department);

        department = new Department();
        department.setCode("shengzhijiankang");
        department.setName("生殖健康");
        department.setLevel(0);
        departments.add(department);

        department = new Department();
        department.setCode("nanke");
        department.setName("男科");
        department.setLevel(0);
        departments.add(department);

        department = new Department();
        department.setCode("pifuxingbingke");
        department.setName("皮肤性病科");
        department.setLevel(0);
        departments.add(department);

        department = new Department();
        department.setCode("zhongyike");
        department.setName("中医科");
        department.setLevel(0);
        departments.add(department);

        department = new Department();
        department.setCode("zhongxiyijieheke");
        department.setName("中西医结合科");
        department.setLevel(0);
        departments.add(department);

        department = new Department();
        department.setCode("wuguanke");
        department.setName("五官科");
        department.setLevel(0);
        departments.add(department);

        department = new Department();
        department.setCode("jingshenke");
        department.setName("精神科");
        department.setLevel(0);
        departments.add(department);

        department = new Department();
        department.setCode("xinlike");
        department.setName("心理科");
        department.setLevel(0);
        departments.add(department);

        department = new Department();
        department.setCode("erke");
        department.setName("儿科");
        department.setLevel(0);
        departments.add(department);

        department = new Department();
        department.setCode("yingyangke");
        department.setName("营养科");
        department.setLevel(0);
        departments.add(department);

        department = new Department();
        department.setCode("zhongliuke");
        department.setName("肿瘤科");
        department.setLevel(0);
        departments.add(department);

        department = new Department();
        department.setCode("qitakeshi");
        department.setName("其他科室");
        department.setLevel(0);
        departments.add(department);

        department = new Department();
        department.setCode("jizhenke");
        department.setName("急诊科");
        department.setLevel(0);
        departments.add(department);

        department = new Department();
        department.setCode("tijianke");
        department.setName("体检科");
        department.setLevel(0);
        departments.add(department);


        return departments;
    }

    private List<Department> initChildData() {
        List<Department> departments = new ArrayList<>();
        Department department;

        department = new Department();
        department.setCode("huxineike");
        department.setName("呼吸内科");
        department.setLevel(1);
        departments.add(department);

        department = new Department();
        department.setCode("xiaohuaneike");
        department.setName("消化内科");
        department.setLevel(1);
        departments.add(department);

        department = new Department();
        department.setCode("miniaoneike");
        department.setName("泌尿内科");
        department.setLevel(1);
        departments.add(department);

        department = new Department();
        department.setCode("xinneike");
        department.setName("心内科");
        department.setLevel(1);
        departments.add(department);

        department = new Department();
        department.setCode("xueyeke");
        department.setName("血液科");
        department.setLevel(1);
        departments.add(department);

        department = new Department();
        department.setCode("neifenmike");
        department.setName("内分泌科");
        department.setLevel(1);
        departments.add(department);

        department = new Department();
        department.setCode("shenjingneike");
        department.setName("神经内科");
        department.setLevel(1);
        departments.add(department);

        department = new Department();
        department.setCode("shenneike");
        department.setName("肾内科");
        department.setLevel(1);
        departments.add(department);

        department = new Department();
        department.setCode("yichuanbingke");
        department.setName("遗传病科");
        department.setLevel(1);
        departments.add(department);

        department = new Department();
        department.setCode("fengshimianyike");
        department.setName("风湿免疫科");
        department.setLevel(1);
        departments.add(department);
        return departments;
    }
}