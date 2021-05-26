package hydata.service;

import hydata.dao.SymptomMapper;
import hydata.model.Symptom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SymptomService {
    @Autowired
    private SymptomMapper symptomMapper;

    /**
     * 获取全部症状
     *
     * @return
     */
    public List<Symptom> getAll() {
        List<Symptom> datas = new ArrayList<>();
        symptomMapper.findAll().forEach(e -> datas.add(e));
        return datas;
    }

    /**
     * 保存症状
     *
     * @param symptom
     * @return
     */
    public Symptom saveSym(Symptom symptom) {
        return symptomMapper.save(symptom);
    }
}
