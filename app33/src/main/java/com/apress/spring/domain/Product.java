package com.apress.spring.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Product{

    @Id
    private Long prod_id;
    private String prod_nm;
    private Long prod_price;
    private String prod_desc;

    @Transient
    private SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

    public Product(Long prod_id, String prod_nm, Long prod_price,  String prod_desc)
            throws ParseException {
        this.prod_id = prod_id;
        this.prod_nm = prod_nm;
        this.prod_price= prod_price;
        this.prod_desc= prod_desc;
    }

    Product() {
    }

    public Long getProd_id() {
        return prod_id;
    }

    public void setProd_id(Long prod_id) {
        this.prod_id = prod_id;
    }

    public String getProd_nm() {
        return prod_nm;
    }

    public void setProd_nm(String prod_nm) {
        this.prod_nm = prod_nm;
    }

    public Long getProd_price() {
        return prod_price;
    }

    public void setProd_price(Long prod_price) {
        this.prod_price = prod_price;
    }

    public String getProd_desc() {
        return prod_desc;
    }

    public void setProd_desc(String prod_desc) {
        this.prod_desc= prod_desc;
    }


    public String toString() {
        StringBuilder value = new StringBuilder("ProductEntry(");
        value.append("prod_id: ");
        value.append(prod_id);
        value.append(", 상품명 : ");
        value.append(prod_nm);
        value.append(", 가격 : ");
        value.append(prod_price);
        value.append(", 상품설명 : ");
        value.append( prod_desc );
        value.append(")");
        return value.toString();
    }
}
