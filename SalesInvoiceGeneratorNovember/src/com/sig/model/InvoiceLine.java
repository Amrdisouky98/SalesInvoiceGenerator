/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sig.model;


public class InvoiceLine {
    private String item;
    private int headerNum;
    private double price;
    private int count;
    private InvoiceHeader header;

    public InvoiceLine(String item, double price, int count, int headerNum) {
        this.item = item;
        this.price = price;
        this.count = count;
        this.headerNum = headerNum;
    }

    public InvoiceHeader getHeader() {
        return header;
    }

    public void setHeader(InvoiceHeader header) {
        this.header = header;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
    public double getLineTotal(){
        return price * count;
    }
    
    

    
    
    
}
