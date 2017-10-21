package br.com.mobiclub.quantoprima.facebook;

import java.util.Comparator;

public class FacebookFriend implements Comparator<FacebookFriend> {
  
	private String id;
	private String name;
	private String profilePhoto;
	
	public FacebookFriend(String id, String name) {
		this.id = id;
		this.name = name;
		this.profilePhoto = "http://graph.facebook.com/" + id + "/picture?type=square";
	}

	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public String getProfilePhoto() {
		return profilePhoto;
	}
	
	@Override
    public int compare(FacebookFriend o1, FacebookFriend o2) {
		return o1.getName().compareTo(o2.getName());
    }
}
