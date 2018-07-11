package com.metrosoft.arafat.salebook.holder;

/**
 * Created by hp on 12/21/2017.
 */

public class HashMapColumn {

    public   String FIRST_COLUMN;
    public   String SECOND_COLUMN="PROD_NAME";
    public   String THIRD_COLUMN="UOM";
    public   String FOURTH_COLUMN="QNTY";
    public   String FIFTH_COLUMN="PROD_PRICE";
    public   String GROSS_PRICE="GROSS_PRICE";
    public   String SIXTH_COLUMN="PROD_CODE";
    public   String SEVENTH_COLUMN="PROD_DESC";
    public   String STYPE="STYPE";
    public   String PCAT="PCAT";
    public  String  UNITTOTAL="unittotal";

    public HashMapColumn(String psl, String pcode,String pqnty,String grossprice, String pprice, String Name, String Desc, String UOM,String prod_sale_type,String prod_parent_cat,String unittotal) {
        this.FIRST_COLUMN = psl;
        SIXTH_COLUMN = pcode;
        FOURTH_COLUMN=pqnty;
        GROSS_PRICE =grossprice;
        FIFTH_COLUMN =pprice;
        SECOND_COLUMN=Name;
        SEVENTH_COLUMN=Desc;
        THIRD_COLUMN =UOM;
        STYPE   =prod_sale_type;
        PCAT    = prod_parent_cat;
        UNITTOTAL =unittotal;
    }
    //sl
    public String getSl() {         return FIRST_COLUMN;     }
    public void setSl(String psl) {         this.FIRST_COLUMN = psl;     }
    //product Name
    public String getName() {         return SECOND_COLUMN;     }
    public void setName(String product_name) {         this.SECOND_COLUMN = product_name;     }
// product code
    public String getCode() {         return SIXTH_COLUMN;     }
    public void setCode(String pcode) {         this.SIXTH_COLUMN = pcode;     }

    //product desc
    public String getDesc() {         return SEVENTH_COLUMN;     }
    public void setDesc(String product_desc) {         this.SEVENTH_COLUMN = product_desc;     }

    //product UOM
    public String getUom() {         return THIRD_COLUMN;     }
    public void setUom(String prod_uom) {         this.THIRD_COLUMN = prod_uom;     }
    // product quantity

    public String getQnty() {
        return FOURTH_COLUMN;     }
    public void setQnty(String prod_qnty) {
        this.FOURTH_COLUMN = prod_qnty;     }
    //product price
    public String getPrice() {
        return FIFTH_COLUMN;     }
    public void setPrice(String prod_price) {
        this.FIFTH_COLUMN = prod_price;     }

    //product parent cat
    public String getParentCat() {
        return PCAT;     }
    public void setParentCat(String parentCat) {
        this.PCAT = parentCat;     }

    //product sale cat
    public String getSaleType() {
        return STYPE;     }
    public void setSaleType(String psaletype) {
        this.STYPE = psaletype;     }

    //product gross price
    public String getGROSS_PRICE() {
        return GROSS_PRICE;     }
    public void setGROSS_PRICE(String gross_price) {
        this.GROSS_PRICE = gross_price;     }

    //product total price
    public String getTotal_price() {
        return UNITTOTAL;     }
    public void setTotal_price(String unittotal) {
        this.UNITTOTAL = UNITTOTAL;     }


}

