package hydata.service;

import cn.hutool.core.util.StrUtil;
import hydata.Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest(classes = Application.class)
class ImportDataServiceTest {

    @Autowired
    private ImportDataService importDataService;

    /**
     * 导入科室，并关联科室
     */
    @Test
    void importDepartment() {
        importDataService.importDepartment();
    }

    @Test
    void importPosition() {
        importDataService.importPosition();
    }

    @Test
    void savePositionRelation() {
        importDataService.savePositionRelation();
    }

    @Test
    void importSymptom() {
        importDataService.importSymptom();
    }


    @Test
    void importDisease() {
        importDataService.importDisease();
    }

    @Test
    void importDiseasePositionRelation() {
        importDataService.importDiseasePositionRelation();
    }

    @Test
    void importDiseaseDepartRelation() {
        importDataService.importDiseaseDepartRelation();
    }

    @Test
    void importDiseaseSymptomRelation(){
        importDataService.importDiseaseSymptomRelation();
    }

}