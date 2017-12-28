package com.sftc.dom;

import com.sftc.web.dao.jpa.AddressDao;
import com.sftc.web.model.entity.Address;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author ： CatalpaFlat
 * @date ：Create in 17:00 2017/12/28
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestJpa {
    @Resource
    private AddressDao addressDao;
    @Test
    public void test() throws Exception {

        Address address1 = new Address();
        address1.setSupplementary_info("aaa");
        address1.setAddress("aaa");
        address1.setArea("aaa");
        address1.setCity("aaa");
        address1.setProvince("aaa");
        address1.setPhone("aaa");
        address1.setName("aaa");address1.setCreate_time("nmb");

        address1.setLatitude(22.723223);
        address1.setLongitude(22.723223);
        address1.setUser_uuid("aaa");
        addressDao.save(address1);
    }
}
