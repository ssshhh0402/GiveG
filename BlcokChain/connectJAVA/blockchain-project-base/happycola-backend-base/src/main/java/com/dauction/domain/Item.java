package com.dauction.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Item
{
	private long id;
	private String name;
	private String category;
	private String explanation;
	private String state = "A"; // A(added), S (on sales), D(on delivery), C(confirmed), X(cancelled)
	private long seller;
	private long buyer;
	private LocalDateTime registeredAt;

	@Override
	public String toString()
	{
		return "{ id: " + id +
				"\n\tname: " + name +
				"\n\texplanation: " + explanation +
				"\n\tstate: " + state +
				"\n\tseller: " + seller +
				"\n\tbuyer: " + seller +
				"\n\tregisteredAt: " + registeredAt +
				" }";
	}
}
