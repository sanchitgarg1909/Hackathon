package org.example.androidsdk.hackathon;

public class User {

    private String id;
    private String email;
    private String token;
    private String name;
    private String profilePic;

    public User(String id, String email, String token, String name, String profilePic) {
        this.id = id;
        this.email = email;
        this.token = token;
        this.name = name;
        this.profilePic = profilePic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
