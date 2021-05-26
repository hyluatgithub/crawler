package hydata.dao;

import hydata.Application;
import hydata.dao.test.MovieMapper;
import hydata.model.test.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = Application.class)
class MovieMapperTest {

    @Autowired
    private MovieMapper movieMapper;

    @Test
    void getAllList() {
        List<Movie> datas = new ArrayList<>();
        movieMapper.findAll().forEach(e -> datas.add(e));
        datas.forEach(System.out::println);
    }

}