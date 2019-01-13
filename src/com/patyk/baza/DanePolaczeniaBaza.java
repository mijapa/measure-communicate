package com.patyk.baza;

public class DanePolaczeniaBaza {
    String adresBazy;
    Integer portBazy;
    String user;
    String password;

    /**
     * @param adresBazy
     * @param portBazy
     * @param user
     * @param password
     */
    public DanePolaczeniaBaza(String adresBazy, Integer portBazy, String user, String password) {
        this.adresBazy = adresBazy;
        this.portBazy = portBazy;
        this.user = user;
        this.password = password;
    }

    public String getAdresBazy() {
        return adresBazy;
    }

    public void setAdresBazy(String adresBazy) {
        this.adresBazy = adresBazy;
    }

    public Integer getPortBazy() {
        return portBazy;
    }

    public void setPortBazy(Integer portBazy) {
        this.portBazy = portBazy;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
