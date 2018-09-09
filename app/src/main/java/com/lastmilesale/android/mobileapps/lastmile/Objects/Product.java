package com.lastmilesale.android.mobileapps.lastmile.Objects;



public class Product {

    private String id, supplier_id, sku, name, quantity, dozen, caton, price;
    private String product_image = "http://lastmilesale.com/";

    public Product(String id, String supplier_id, String sku, String name, String quantity, String dozen, String caton,String product_image, String price) {
        this.id = id;
        this.supplier_id = supplier_id;
        this.sku = sku;
        this.name = name;
        this.quantity = quantity;
        this.dozen = dozen;
        this.caton = caton;
        this.product_image = this.product_image+product_image;
        this.price = price;
    }

    public String getPrice() {
        return "Tsh "+price+"/-";
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSupplier_id(String supplier_id) {
        this.supplier_id = supplier_id;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setDozen(String dozen) {
        this.dozen = dozen;
    }

    public void setCaton(String caton) {
        this.caton = caton;
    }

    public String getId() {
        return id;
    }

    public String getSupplier_id() {
        return supplier_id;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        if(!dozen.equals("0")){
            if(!caton.equals("0")){
                return dozen+" Dozen(s) & "+caton+" Carton(s) available";
            }else{
                return dozen+" Dozen(s) available";
            }
        }else{
            if(!caton.equals("0")){
                return caton+" Carton(s) available";
            }else{
                return dozen+"0 available";
            }
        }
    }

    public String getDozen() {
        return dozen;
    }

    public String getCaton() {
        return caton;
    }
}
