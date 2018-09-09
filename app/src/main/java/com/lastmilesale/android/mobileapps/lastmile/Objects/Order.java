package com.lastmilesale.android.mobileapps.lastmile.Objects;


public class Order {

    private String id, product_id, product_name,supplier_name, user_id, quantity, order_status, price,status,created_at,updated_at;
    private String noOfDozens,noOfPieces,noOfCartons;
    private String productImage = "http://lastmilesale.com/";


    public Order(String id, String product_id, String product_name, String supplier_name,String noOfDozens, String noOfPieces, String noOfCartons,String order_status,String productImage,String price){
        this.id = id;
        this.product_id = product_id;
        this.product_name = product_name;
        this.supplier_name = supplier_name;
        this.noOfDozens = noOfDozens;
        this.noOfPieces = noOfPieces;
        this.noOfCartons = noOfCartons;
        this.order_status = order_status;
        this.productImage = this.productImage + productImage;
        this.price = price;
    }

    public Order(String id, String product_id,  String quantity,String price, String status,String updated_at){
        this.id = id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.updated_at = updated_at;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getNoOfDozens() {
        return noOfDozens;
    }

    public String getNoOfPieces() {
        return noOfPieces;
    }

    public String getNoOfCartons() {
        return noOfCartons;
    }

    public String getPrice() {

        return "Total: Tsh "+price+"/-";
    }

    public int getPriceInInt(){
        return Integer.parseInt(price);
    }

    public int getDozensInInt(){
        return Integer.parseInt(noOfDozens);
    }

    public int getCartonsInInt(){
        return Integer.parseInt(noOfCartons);
    }

    public int getPiecesInInt(){
        return Integer.parseInt(noOfPieces);
    }

    public String getStatus() {
        return order_status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public String getId() {
        return id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }
}
