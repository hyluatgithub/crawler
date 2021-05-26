package hydata.service;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import hydata.dao.DepartmentMapper;
import hydata.dao.DiseaseMapper;
import hydata.dao.PositionMapper;
import hydata.dao.SymptomMapper;
import hydata.dao.relation.*;
import hydata.model.Department;
import hydata.model.Disease;
import hydata.model.Position;
import hydata.model.Symptom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 初始化数据
 *
 * @author lhy13
 */
@Service
public class ImportDataService {
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private SymptomMapper symptomMapper;
    @Autowired
    private DiseaseMapper diseaseMapper;
    @Autowired
    private PositionMapper positionMapper;
    @Autowired
    private BelongRelationMapper belongRelationMapper;
    @Autowired
    private FormRelationMapper formRelationMapper;
    @Autowired
    private SubDepartmentRelationMapper subDepartmentRelationMapper;
    @Autowired
    private VisitDepartmentRelationMapper visitDepartmentRelationMapper;
    @Autowired
    private FocusRelationMapper focusRelationMapper;
    @Autowired
    private VisitDepartment4JBRelationMapper visitDepartment4JBRelationMapper;
    @Autowired
    private HasSymptomRelationMapper hasSymptomRelationMapper;

    /**
     * 导入科室数据
     */
    public void importDepartment() {
        // 读取二级科室
        ExcelReader reader2 = ExcelUtil.getReader("g:/demo/childkeshi.xls");
        List<List<Object>> readAll2 = reader2.read();
        String childDeptName;
        String parentDeptName;
        String childDeptCode;
        Department department;
        for (List<Object> objects : readAll2) {
            childDeptName = (String) objects.get(0);
            parentDeptName = (String) objects.get(2);
            childDeptCode = objects.get(1).toString().replace("/p/", "").replace(".html", "");

            department = new Department();
            department.setCode(childDeptCode);
            department.setName(childDeptName);
            department.setLevel(1);
            departmentMapper.save(department);

            subDepartmentRelationMapper.createRel(childDeptName, parentDeptName);
        }

    }

    /**
     * 导入部位数据
     */
    public void importPosition() {
        Map<String, String> positionMap = new HashMap<>();
        positionMap.put("头部", "鼻");
        positionMap.put("头部", "耳");
        positionMap.put("头部", "眼");
        positionMap.put("头部", "口");
        positionMap.put("四肢", "下肢");
        positionMap.put("四肢", "上肢");
        positionMap.put("生殖部位", "女性盆骨");
        positionMap.put("生殖部位", "男性股沟");
        // 保存各个部位
        List<Position> positions = initPositionData();
        positions.forEach(e -> {
            positionMapper.save(e);
        });
    }

    /**
     * 关联部位数据
     */
    public void savePositionRelation() {
        Map<String, String> positionMap = new HashMap<>();
        positionMap.put("鼻", "头部");
        positionMap.put("耳", "头部");
        positionMap.put("眼", "头部");
        positionMap.put("口", "头部");
        positionMap.put("下肢", "四肢");
        positionMap.put("上肢", "四肢");
        positionMap.put("女性盆骨", "生殖部位");
        positionMap.put("男性股沟", "生殖部位");
        // 关联部位关系
        positionMap.forEach((k, v) -> {
            formRelationMapper.createRel(k, v);
        });
    }

    /**
     * 导入症状数据
     */
    public void importSymptom() {
        Map<String, String> symptomMap = new HashMap<>();
        List<Symptom> symptoms = new ArrayList<>();
        Map<String, String> symptomAndPositionMap = new HashMap<>();
        Map<String, String> symptomAndDepartmentMap = new HashMap<>();

        // 读取二级科室
        ExcelReader reader = ExcelUtil.getReader("g:/demo/keshiAndZhengzhuang.xlsx");
        List<List<Object>> readAll = reader.read();
        Symptom symptom;

        for (List<Object> objects : readAll) {
            symptom = new Symptom();
            // 症状编码
            symptom.setCode((String) objects.get(2));
            // 名称
            symptom.setName((String) objects.get(3));
            if (!symptomMap.containsKey(symptom.getCode())) {
                symptomMap.put(symptom.getCode(), symptom.getName());
                symptoms.add(symptom);
                symptomAndDepartmentMap.put(symptom.getCode(), (String) objects.get(0));
            }
        }

        // 读取二级科室
        ExcelReader reader2 = ExcelUtil.getReader("g:/demo/buweiAndZhengzhuang.xlsx");
        List<List<Object>> readAll2 = reader2.read();
        for (List<Object> objects : readAll2) {
            symptom = new Symptom();
            // 症状编码
            symptom.setCode((String) objects.get(2));
            // 名称
            symptom.setName((String) objects.get(3));
            if (!symptomMap.containsKey(symptom.getCode())) {
                symptomMap.put(symptom.getCode(), symptom.getName());
                symptoms.add(symptom);
                symptomAndPositionMap.put(symptom.getCode(), (String) objects.get(0));
            }
        }

        // 保存症状
        symptoms.forEach(e -> {
            symptomMapper.save(e);
        });

        // 关联 症状和科室
        symptomAndDepartmentMap.forEach((k, v) -> {
            visitDepartmentRelationMapper.createRel(k, v);
        });

        // 关联 症状和部位
        symptomAndPositionMap.forEach((k, v) -> {
            belongRelationMapper.createRel(k, v);
        });

    }


    /**
     * 导入疾病数据
     */
    public void importDisease() {
        Map<String, String> diseasesMap = new HashMap<>();
        List<Disease> diseases = new ArrayList<>();
        // 读取疾病
        ExcelReader reader = ExcelUtil.getReader("g:/demo/jibing.xlsx");
        List<List<Object>> readAll = reader.read();
        Disease disease;

        for (List<Object> objects : readAll) {
            disease = new Disease();
            //疾病编码
            disease.setCode((String) objects.get(0));
            //疾病名称
            disease.setName((String) objects.get(1));
            //疾病简介
            disease.setDesc((String) objects.get(2));
            //疾病病因
            disease.setCause((String) objects.get(3));
            // 预防措施
            disease.setPrevent((String) objects.get(4));
            // 治疗周期
            disease.setCureWay((String) objects.get(5));
            // 治疗方式
            disease.setCureLastTime((String) objects.get(6));
            // 治愈概率
            disease.setCuredProb((String) objects.get(7));
            // 疾病易感人群
            disease.setEasyGet((String) objects.get(8));
            // 宜吃食物
            disease.setDietGood((String) objects.get(9));
            // 宜吃食物列表
            disease.setGoodFoods((String) objects.get(10));
            // 忌吃食物
            disease.setDietBad((String) objects.get(11));
            // 忌吃食物列表
            disease.setBadFoods((String) objects.get(12));

            if (!diseasesMap.containsKey(disease.getCode())) {
                diseasesMap.put(disease.getCode(), disease.getName());
                diseases.add(disease);
            }
        }

        // 保存症状
        diseases.forEach(e -> {
            diseaseMapper.save(e);
        });

    }


    /**
     * 导入症状关联关系数据
     */
    public void importDiseasePositionRelation() {
        Map<String, String> diseaseAndPositionMap = new HashMap<>();

        // 读取二级科室
        ExcelReader reader = ExcelUtil.getReader("g:/demo/buweiAndJB.xlsx");
        List<List<Object>> readAll = reader.read();
        String diseaseCode;
        for (List<Object> objects : readAll) {
            diseaseCode = (String) objects.get(2);
            if (!diseaseAndPositionMap.containsKey(diseaseCode)) {
                diseaseAndPositionMap.put(diseaseCode, (String) objects.get(0));
            }
        }

        // 关联 症状和部位
        diseaseAndPositionMap.forEach((k, v) -> {
            focusRelationMapper.createRel(k, v);
        });

    }

    /**
     * 导入症状关联关系数据
     */
    public void importDiseaseDepartRelation() {
        Map<String, String> diseaseAndDepartmentMap = new HashMap<>();

        // 读取二级科室
        ExcelReader reader = ExcelUtil.getReader("g:/demo/keshiAndJB.xlsx");
        List<List<Object>> readAll = reader.read();
        String diseaseCode;
        for (List<Object> objects : readAll) {
            diseaseCode = (String) objects.get(2);
            if (!diseaseAndDepartmentMap.containsKey(diseaseCode)) {
                diseaseAndDepartmentMap.put(diseaseCode, (String) objects.get(0));
            }
        }

        // 关联 症状和部位
        diseaseAndDepartmentMap.forEach((k, v) -> {
            visitDepartment4JBRelationMapper.createRel(k, v);
        });

    }

    /**
     * 导入疾病与症状关联关系数据
     */
    public void importDiseaseSymptomRelation() {
        // 读取二级科室
        ExcelReader reader = ExcelUtil.getReader("g:/demo/jibingAndZhengzhuang.xlsx");
        List<List<Object>> readAll = reader.read();
        String diseaseCode;
        for (List<Object> objects : readAll) {
            // 疾病编码-症状编码（多个逗号分隔）
            diseaseCode = (String) objects.get(0);
            if (objects.get(1) == null) {
                continue;
            }
            String[] split = objects.get(1).toString().split(",");
            for (String id : split) {
                // 关联 疾病和症状
                hasSymptomRelationMapper.createRel(diseaseCode, id);
            }
        }


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

    String[] parentPositionStr = {"头部", "颈部", "四肢", "胸部", "腹部", "腰部", "生殖部位", "皮肤", "全身", "排泄部位"};
    String[] childPositionStr = {"鼻", "耳", "眼", "口", "下肢", "上肢", "女性盆骨", "男性股沟"};

    private List<Position> initPositionData() {
        List<Position> positions = new ArrayList<>();
        Position position;
        for (String s : parentPositionStr) {
            position = new Position();
            position.setName(s);
            position.setLevel(0);
            positions.add(position);
        }
        for (String s : childPositionStr) {
            position = new Position();
            position.setName(s);
            position.setLevel(1);
            positions.add(position);
        }
        return positions;
    }
}
