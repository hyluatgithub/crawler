package hydata.controller;

import hydata.service.Neo4jService;
import hydata.model.test.Coder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class CoderController {
    @Autowired
    private Neo4jService neo4jService;

    @RequestMapping(value = "hi")
    @ResponseBody
    public String hello(){
        return "hello";
    }

    @GetMapping("/add")
    @ResponseBody
    public Object addCoder(Coder coder) {
        return neo4jService.addCoder(coder);
    }

    @GetMapping("/get")
    @ResponseBody
    public List<Coder> addCoder() {
        List<Coder> coderList = neo4jService.getCoderList();
        System.out.println(coderList.size());
        coderList.forEach(e-> System.out.println(e.toString()));
        return coderList;
    }
}
