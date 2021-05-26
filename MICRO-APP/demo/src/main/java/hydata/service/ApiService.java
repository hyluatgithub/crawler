package hydata.service;

import hydata.dao.relation.BelongRelationMapper;
import hydata.dao.relation.FormRelationMapper;
import hydata.dao.relation.SubDepartmentRelationMapper;
import hydata.dao.relation.VisitDepartmentRelationMapper;
import hydata.model.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 对外查询服务
 */
@Service
public class ApiService {
    @Autowired
    private BelongRelationMapper belongRelationMapper;
    @Autowired
    private FormRelationMapper formRelationMapper;
    @Autowired
    private SubDepartmentRelationMapper subDepartmentRelationMapper;
    @Autowired
    private VisitDepartmentRelationMapper visitDepartmentRelationMapper;


    //// 针对导诊需要的查询api

    /**
     * 根据症状名称 查询科室
     *
     * @param symptomName
     * @return
     */
    public Position getPositionBySymptomName(String symptomName) {
//        belongRelationMapper.
        return null;
    }

}
