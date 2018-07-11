package com.metrosoft.arafat.salebook.holder;

/**
 * Created by hp on 10/26/2017.
 */

public class OrderHolder {
    private String sl;
    private String id;
    private String name;
    private int quantity;
    private String PROD_RATE;
    private String PROD_VAT;
    private int array_length;



    public OrderHolder(String sl, String id, String name,int quantity,String PROD_RATE,String PROD_VAT){
        this.sl=sl;
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.PROD_RATE = PROD_RATE;
        this.PROD_VAT = PROD_VAT;

        //this.array_length = array_length;
    }


    public void setsl(String sl){
        this.sl = sl;
    }



    public void setId(String id){
        this.id = id;
    }

    public void setQuantity(int quantity){
        this.quantity=quantity;
    }

    public void setName(String name){
        this.name = name;
    }



    public void setPROD_RATE(String PROD_RATE){
        this.PROD_RATE=PROD_RATE;
    }

    public void setPROD_VAT(String PROD_VAT){
        this.PROD_VAT=PROD_VAT;
    }
    public String getsl() {
        // TODO Auto-generated method stub
        return this.sl;
    }


    public String getId(){
        return this.id;
    }


    public String getName(){
        return this.name;
    }

    public int getQuantity(){
        return this.quantity;
    }


    public String getPROD_RATE(){
        return this.PROD_RATE;
    }
    public String getPROD_VAT(){
        return this.PROD_VAT;
    }









	/*

		public int getArray_length(){
		return this.array_length;
	}
	*/



}

