package hydata.service;

import hydata.dao.PositionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 部位
 */
@Service
public class PositionService {
    @Autowired
    private PositionMapper positionMapper;

}
