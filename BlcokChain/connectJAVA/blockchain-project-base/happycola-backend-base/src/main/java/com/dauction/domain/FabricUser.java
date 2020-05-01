package com.dauction.domain;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import java.io.Serializable;
import java.util.Set;

/**
 * 패브릭 네트워크 초기 세팅을 위해
 * org.hyperledger.fabric.sdk.User를 implements한 클래스가 필요하다.
 *
 * 예를 들어 다음과 같은 클래스를 작성해야 한다.
 * public class FabricUser implements User, Serializable
 * {
 * 	  private static final long serialVersionUID = 8077132186383604355L;
 * 	  ...
 * }
 */
public class FabricUser implements User, Serializable
{
	private static final long serialVersionUID = 8077132186383604355L;

	private String name;
	private Set<String> roles;
	private String account;
	private String affiliation;
	private String organization;
	private String enrollmentSecret;
	private String mspId;
	Enrollment enrollment = null;

	private transient CryptoSuite cryptoSuite;

	public FabricUser(String name, String org, CryptoSuite cryptoSuite) {
		this.name = name;
		this.cryptoSuite = cryptoSuite;
		this.organization = org;
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public Set<String> getRoles()
	{
		return this.roles;
	}

	@Override
	public String getAccount()
	{
		return this.account;
	}

	@Override
	public String getAffiliation()
	{
		return this.affiliation;
	}

	@Override
	public Enrollment getEnrollment()
	{
		return this.enrollment;
	}

	public void setEnrollment(final Enrollment enrollment){
		this.enrollment = enrollment;
	}

	@Override
	public String getMspId()
	{
		return this.mspId;
	}

	public void setMspId(final String mspId){
		this.mspId = mspId;
	}
}
