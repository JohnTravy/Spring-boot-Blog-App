package org.travy.Springstarter.Util.Constants;

public enum Priviledges {

    RESET_ANY_USER_PASSWORD(1l, "RESET_ANY_USER_PASWWORD"),
    ACCESS_ADMIN_PANEL(2l, "ACCESS_ADMIN_PANEL");

    private Long id;
    private String priviledge;

    private Priviledges(Long id, String priviledge){

        this.id = id;
        this.priviledge = priviledge;

    }

    public Long getid() {
        return id;
    }

    public String getpriviledge() {
        return priviledge;
    }

    

    
}
