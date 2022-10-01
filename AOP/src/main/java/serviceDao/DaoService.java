package serviceDao;
import com.itheima.dao.DaoImpl.DaoImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import com.itheima.dao.Dao;

import javax.annotation.PostConstruct;


@Service
public class DaoService {
    @Autowired
    private DaoImpl di;


    public void testAll(){
        System.out.println(di);
        di.save();
        di.delete();
    }


}
