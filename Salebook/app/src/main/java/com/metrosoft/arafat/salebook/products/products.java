package com.metrosoft.arafat.salebook.products;

/**
 * Created by hp on 10/17/2017.
 */

public class products {
    public String prod_sl;
    public String prod_code;
    public String product_name;
    public String product_desc;
    public String prod_qnty;
    public String prod_uom;
    public String prod_price;
    public String prod_discount;
    public String prod_net;
    public String prod_prg_price;
    public String total_price;

    boolean ShowName;
    public products(String psl, String pcode,String pqnty, String pprice, String Name, String Desc, String UOM) {
        this.prod_sl = psl;
        this.prod_code = pcode;
        prod_qnty="";
        prod_price =pprice;
        this.product_name=Name;
        product_desc=Desc;
        prod_uom =UOM;
        prod_discount="";
        prod_net    ="";
        prod_prg_price ="";
        total_price ="";
    }
   /* public boolean isShowName() {
        return ShowName;
    }
    public void setShowName(boolean showName) {
        ShowName = showName;
    }*/
    //sl
    public String getSl() {         return prod_sl;     }
    public void setSl(String psl) {         this.prod_sl = psl;     }
//product code
    public String getCode() {         return prod_code;     }
    public void setCode(String pcode) {         this.prod_code = pcode;     }
    //product Name
    public String getName() {         return product_name;     }
    public void setName(String product_name) {         this.product_name = product_name;     }
    //product desc
    public String getDesc() {         return product_desc;     }
    public void setDesc(String product_desc) {         this.product_desc = product_desc;     }
    //product UOM
    public String getUom() {         return prod_uom;     }
    public void setUom(String prod_uom) {         this.prod_uom = prod_uom;     }
    // product quantity

    public String getQnty() {
        return prod_qnty;     }
    public void setQnty(String prod_qnty) {
        this.prod_qnty = prod_qnty;     }
//product price
    public String getPrice() {
        return prod_price;     }
    public void setPrice(String prod_price) {
        this.prod_price = prod_price;     }

    //product discont
    public String getDiscount() {
        return prod_discount;     }
    public void setDiscont(String prod_discount) {
        this.prod_discount = prod_discount;     }
    //product Net
    public String getNet() {
        return prod_net;     }
    public void setNet(String prod_net) {
        this.prod_net = prod_net;     }
    //product Program price
    public String getProgPrice() {
        return prod_prg_price;     }
    public void setProgPrice(String prod_prg_price) {
        this.prod_prg_price = prod_prg_price;     }
    //product total price
    public String getTotal_price() {
        return total_price;     }
    public void setTotal_price(String total_price) {
        this.total_price = total_price;     }

}

