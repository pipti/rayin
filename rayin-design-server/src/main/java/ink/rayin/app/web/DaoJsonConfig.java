package ink.rayin.app.web;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DaoJsonConfig implements CommandLineRunner {
    @Override
    public void run(String... args){
        JacksonTypeHandler.setObjectMapper(new ObjectMapper());
    }
}
