package com.dauction.application;

import com.dauction.domain.FabricRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FabricCCServiceTest
{
	private static final Logger log = LoggerFactory.getLogger(FabricCCServiceTest.class);
	@Autowired
	private IFabricCCService fabricCCService;

	@Test
	public void testRegisterItem(){
		boolean result = this.fabricCCService.registerItem(1001, 2000);
		assert result != false;
	}

	@Test
	public void testDeregisterItem(){
		boolean result = this.fabricCCService.deregisterItem(1000, 2000);
		assert result != false;
	}

	@Test
	public void testStartAuctionSuccess(){
		boolean result = this.fabricCCService.startAuction(1000, 2000);
		assert result != false;
	}

	@Test
	public void testStartAuctionFailure(){
		boolean result = this.fabricCCService.startAuction(1000, 2001);
		assert result != true;
	}

	@Test
	public void testEndAuctionSuccess(){
		boolean result = this.fabricCCService.endAuction(1000, 2000);
		assert result != false;
	}

	@Test
	public void testEndAuctionFailure(){
		boolean result = this.fabricCCService.endAuction(1000, 2001);
		assert result != true;
	}

	@Test
	public void testCancelAuctionSuccess(){
		boolean result = this.fabricCCService.cancelAuction(1000, 2000);
		assert result != false;
	}

	@Test
	public void testCancelAuctionFailure(){
		boolean result = this.fabricCCService.cancelAuction(1000, 2001);
		assert result != true;
	}

	@Test
	public void testConfirmItem(){
		boolean result = this.fabricCCService.confirmItem(1000, 3000);
		assert result != false;
	}

	@Test
	public void testStartDelivery() {
		boolean result = this.fabricCCService.startDelivery(1000, 100000);
		assert result != false;
	}

	@Test
	public void testQuery(){
		FabricRecord recentRecord = this.fabricCCService.query(1000);
		log.debug(recentRecord.toString());
		assert recentRecord.getAssetId().equals("1000");
		assert recentRecord.getCreatedAt() != null;
		assert recentRecord.getState() != null;
		assert recentRecord.getRecorder() != null;
	}

	@Test
	public void testGetItemHistory(){
		List<FabricRecord> fr = this.fabricCCService.getItemHistory(1001);

		assert fr.size() > 0;

		fr.forEach(fabricRecord -> log.debug(fabricRecord.toString()));
	}
}
