package com.dauction.domain;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Wallet
{
	private long id;
	private long ownerId;
	private String address;
	private BigDecimal balance = BigDecimal.valueOf(0);
	private int receivingCount = 0;
}
