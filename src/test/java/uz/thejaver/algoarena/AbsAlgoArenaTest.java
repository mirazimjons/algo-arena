package uz.thejaver.algoarena;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
public abstract class AbsAlgoArenaTest {

    @Autowired
    protected MockMvc mvc;

}
