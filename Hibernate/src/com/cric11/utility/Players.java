//$Id$
package com.cric11.utility;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Players {
	
	@Id
	private String pid;
	
	private String player_name;
	
	private String player_role;
	
	@Column(nullable=false,unique=true)
	private String player_link;
	
	private String country;
		
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPlayer_name() {
		return player_name;
	}

	public void setPlayer_name(String player_name) {
		this.player_name = player_name;
	}

	public String getPlayer_role() {
		return player_role;
	}

	public void setPlayer_role(String player_role) {
		this.player_role = player_role;
	}

	public String getPlayer_link() {
		return player_link;
	}

	public void setPlayer_link(String player_link) {
		this.player_link = player_link;
	}

}
