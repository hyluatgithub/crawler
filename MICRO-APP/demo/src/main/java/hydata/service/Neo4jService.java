package hydata.service;

import hydata.dao.test.CoderRepository;
import hydata.model.test.Coder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Neo4jService {
    @Autowired
    private CoderRepository coderRepository;

    public int addCoder(Coder coder) {
        coderRepository.addCoderList(coder.getName(), coder.getSex());
        return 1;
    }

    public List<Coder> getCoderList() {
        return coderRepository.getCoderList();
    }
}
