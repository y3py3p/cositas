package com.SafeWebDev.attempt.Models;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Entity
@Table(name = "itemtable")
@Slf4j
public class Item{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long productID ;

    @Column
    private String productName;
    private String productSize="desconocido";
    private String productCondition="desconocido";
    private float productPrice=0;
    private String productOwner;
    private int productStock=0;

    public Item(String productName, String productSize, String state,float productPrice) {
        this.productName = productName;
        this.productSize = productSize;
        this.productCondition = state;
        this.productPrice=productPrice;
    }

    public long getProductID() {
        return productID;
    }

    public Item(){

    }


    public void update(Item item){
        this.productName=item.productName;
        this.productSize=item.productSize;
        this.productCondition=item.productCondition;
        this.productPrice=item.productPrice;
        log.info("{}", productOwner);
        this.productOwner= productOwner;
    }

    public void setId(long productID){
        this.productID=productID;
    }

    public void setOwner(String owner){
        this.productOwner=owner;
    }

    public String getOwner(){
        return this.productOwner;
    }

    public boolean canEdit(String user){
        return this.productOwner.equals(user);
    }

    public String getname() {
        return productName;
    }

    public void setname(String productName) {
        this.productName = productName;
    }

    public String getsize() {
        return productSize;
    }

    public void setsize(String productSize) {
        this.productSize = productSize;
    }

    public String getstate() {
        return productCondition;
    }

    public float getprice() {
        return productPrice;
    }

    public void setprice(float productPrice) {
        this.productPrice = productPrice;
    }

    public void setCondition(String state) {
        this.productCondition = state;
    }

    public void editItem(Item item){
        setname(item.productName);
        setsize(item.productSize);
        setProductCondition(item.productCondition);
        setprice(item.productPrice);
    }

    public void setProductCondition(String productCondition) {
        this.productCondition = productCondition;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id_product=" + productID +
                ", productName='" + productName + '\'' +
                ", productSize='" + productSize + '\'' +
                ", state='" + productCondition + '\'' +
                ", productStock='" + productStock + '\'' +
                '}';
    }

}
/*public class Item{
    private long id_product;
    private String productName;
    private String productSize;
    private String productCondition;
    private float productPrice;
    private int productStock=0;

    public Item(String productName, String productSize, String state,float productPrice) {
        this.productName = productName;
        this.productSize = productSize;
        this.productCondition = state;
        this.productPrice=productPrice;
    }

    public Item(){

    }

    public void setId(long id_product){
        this.id_product=id_product;
    }

    public String getname() {
        return productName;
    }

    public void setname(String productName) {
        this.productName = productName;
    }

    public String getsize() {
        return productSize;
    }

    public void setsize(String productSize) {
        this.productSize = productSize;
    }

    public String getstate() {
        return productCondition;
    }

    public float getprice() {
        return productPrice;
    }

    public void setprice(float productPrice) {
        this.productPrice = productPrice;
    }

    public void setstate(String state) {
        this.productCondition = state;
    }

    public void editItem(Item item){
        setname(item.productName);
        setsize(item.productSize);
        setCondition(item.productCondition);
        setprice(item.productPrice);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id_product=" + id_product +
                ", productName='" + productName + '\'' +
                ", productSize='" + productSize + '\'' +
                ", state='" + productCondition + '\'' +
                ", productStock='" + productStock + '\'' +
                '}';
    }
}*/
