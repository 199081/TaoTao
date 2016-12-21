package cn.itcast.bos.service.bc.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.bos.domain.ws.Customer;
import cn.itcast.bos.service.bc.DecidedZoneService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class DecidedZoneServiceTest {
	//注入要测试bean
	@Autowired
	private  DecidedZoneService decidedZoneService;

	@Test
	public void testFindCustomerListNoDecidedZone() {
		List<Customer> list = decidedZoneService.findCustomerListNoDecidedZone();
		System.out.println(list);
	}

	@Test
	public void testFindCustomerListHasDecidedZone() {
		List<Customer> list = decidedZoneService.findCustomerListHasDecidedZone("DQ001");
		System.out.println(list);
	}

	@Test
	public void testAssociateCustomerToDecidedZone() {
		decidedZoneService.associateCustomerToDecidedZone("DQ001", "1,2");
	}

}
