package com.lastmilesale.android.mobileapps.lastmile.Objects;



public class Supplier {

    private String id,name, logo="http://lastmilesale.com/", status;

    public Supplier(String id, String name, String logo, String status){
        this.id = id;
        this.name = name;
        this.logo = this.logo + logo;
        this.status = status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLogo(String logo) {
        this.logo = this.logo+logo;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogo() {
        return logo;
    }

    public String getStatus() {
        return status;
    }
}
