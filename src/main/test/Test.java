import com.sftc.web.model.Address;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

/**
 * Created by Administrator on 2017/4/3.
 */
public class Test {
    public static void main(String[] args){
        String resource = "mybatis/sqlMapConfig.xml";
        InputStream is = null;


        try {
            is = Test.class.getResourceAsStream(resource);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession session = sessionFactory.openSession(true);
        String statement = "com.sftc.ssm.mapper.UserMapper.addAddress";
        Address address = new Address("广东省","深圳市","五和","66","134574411","杨啟源",
                "收",1);

       int i = session.insert(statement);

        System.out.print(address.getId());
    }
}
