package hydata.dao;

import hydata.Application;
import hydata.model.Symptom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = Application.class)
class SymptomMapperTest {

    @Autowired
    private SymptomMapper symptomMapper;

    @Test
    void getAllList() {
        List<Symptom> datas = new ArrayList<>();
        symptomMapper.findAll().forEach(e -> datas.add(e));
        datas.forEach(System.out::println);
    }

    @Test
    void getList() {
        List<Symptom> datas = new ArrayList<>();
        symptomMapper.getList().forEach(e -> datas.add(e));
        datas.forEach(System.out::println);
    }

    @Test
    void findByCode() {
        List<Symptom> datas = new ArrayList<>();
        symptomMapper.findByCode("1").forEach(e -> datas.add(e));
        datas.forEach(System.out::println);
    }

    @Test
    void save() {
        List<Symptom> symptoms = initData();
        symptoms.forEach(e -> {
            symptomMapper.save(e);
        });
    }

    @Test
    void deleteObj() {
        Symptom symptom = new Symptom();
        symptom.setCode("5");
        symptomMapper.delete(symptom);

    }

    private List<Symptom> testData() {
        List<Symptom> symptoms = new ArrayList<>();
        Symptom symptom;
        for (int i = 10; i < 15; i++) {
            symptom = new Symptom();
            symptom.setCode("" + i);
            symptom.setName("name" + i);
//            symptom.setTitle("title" + i);
//            symptom.setCheck("check" + i);
//            symptom.setPrevention("prevention" + i);
            symptoms.add(symptom);
        }
        return symptoms;
    }

    private List<Symptom> initData() {
        List<Symptom> symptoms = new ArrayList<>();
        Symptom symptom;

        symptom = new Symptom();
        symptom.setCode("6485");
        symptom.setName("扁桃体发炎");
//        symptom.setTitle("扁桃腺炎发病的原因不外是感冒、过度疲劳、季节变化及体质不好。急性扁桃腺炎的症状是喉咙痛、发烧。其特征是易转变慢性，症状会较为稳定，但疲倦时，就会喉咙痛，容易发烧。有进，也会造成中耳炎或肾炎等并发症。剧烈疼痛持续2-3开仍未退烧时，最好接受医生的诊断。");
//        symptom.setCheck("鼻咽部MRI检查 血常规");
        symptoms.add(symptom);

        symptom = new Symptom();
        symptom.setCode("2346");
        symptom.setName("口腔溃疡");
//        symptom.setTitle("口腔溃疡：也称复发性口疮，是—种反复发作的口腔粘膜疾病。它的特点是反复发作、灼痛难忍，同时能引起多种并发症。中医所辩证论述：复发性口腔溃疡是因七情内伤，素体虚弱，外感六淫之邪，致使肝郁气滞，郁热化火，心火炽盛，胃火上攻，心肾不交，虚火上炎熏蒸于口而发病。欲治愈此病，必须从根本上进行治疗。医院专家组，历经长期的临床治疗与研究，科学地采用“祛腐清胃，祛腐生肌”等系列疗法为广大患者解除病痛，让复发性口腔溃疡患者不再承受溃疡的折磨和恶变的危险。");
//        symptom.setCheck("耳、鼻、咽拭子细菌培养 维生素B1 牙髓电活力测定 牙髓温度试验（冷热诊） 维生素B12");
        symptoms.add(symptom);

        return symptoms;
    }
}