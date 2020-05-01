package com.dauction.application;

import com.dauction.domain.FabricRecord;

import java.util.List;

public interface IFabricCCService
{
	boolean registerItem(final long iid, final long uid);
    boolean deregisterItem(final long iid, final long uid);
    boolean startAuction(final long iid, final long uid);
    boolean endAuction(final long iid, final long uid);
    boolean cancelAuction(final long iid, final long uid);
    boolean startDelivery(final long iid, final long shippingAdmin);
    boolean confirmItem(final long iid, final long uid);
	List<FabricRecord> getItemHistory(final long iid);
    FabricRecord query(final long iid);
}
