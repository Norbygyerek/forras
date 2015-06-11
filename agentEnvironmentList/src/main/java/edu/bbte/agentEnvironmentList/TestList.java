package edu.bbte.agentEnvironmentList;

import edu.bbte.robo.Robo;

/**
 * TestList osztály, amely egy saját adatszerkezet, 
 * az adatok elkérdezése érdekében. 4 adattaggal rendelkezik
 * s1 - agentName - Az Agent neve
 * s2 - environmentName - Az Environment neve
 * s3 - date - A teszt kezdésének dátuma
 * robo - egy RoboCommunication objektum
 * 
 * @author Gáll
 *
 */
public class TestList {
	
	private String s1;
	private String s2;
	private String s3;
	private Robo robo;
	
	
	public String getS1() {
		return s1;
	}
	public void setS1(String s1) {
		this.s1 = s1;
	}
	public String getS2() {
		return s2;
	}
	public void setS2(String s2) {
		this.s2 = s2;
	}
	public String getS3() {
		return s3;
	}
	public void setS3(String s3) {
		this.s3 = s3;
	}
	public Robo getRobo() {
		return robo;
	}
	public void setRobo(Robo robo) {
		this.robo = robo;
	}
	
	
}
