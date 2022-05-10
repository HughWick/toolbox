package com.github.hugh.db;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.github.hugh.db.mapper.UserMapper;
import com.github.hugh.db.model.UserDO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

//@SpringBootTest
@SpringBootTest(classes = UserMapperTest.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@RunWith(SpringRunner.class) //junit4
@ExtendWith(SpringExtension.class) //junit5
@MybatisPlusTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        UserDO userDO = new UserDO();
        userDO.setId(7777L);
        userDO.setGmtModified(new Date());
        userDO.setGmtCreate(new Date());
        userDO.setRealName("ke");
        userDO.setUserName("ni");
        userMapper.insert(userDO);

        UserDO select = userMapper.selectById(1);
        System.out.println(select);
    }
}
