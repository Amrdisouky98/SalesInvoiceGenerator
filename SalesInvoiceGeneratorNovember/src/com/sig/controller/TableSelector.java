package com.sig.controller;

import com.sig.model.InvoiceHeader;
import com.sig.model.InvoiceLine;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import static com.sig.controller.invoiceControl.arrayOfHeaders;

public class TableSelector {
    public int tableClick(JTable myTable, JTable myLinesTable) {
        DefaultTableModel myModel = (DefaultTableModel) myTable.getModel();
       int index = (int) myModel.getValueAt(myTable.getSelectedRow(),0);

       DefaultTableModel myLinesModel = (DefaultTableModel) myLinesTable.getModel();
       myLinesModel.setRowCount(0);

       Object[] lineBus = new Object[4];

       for (InvoiceHeader header : arrayOfHeaders){
               for(InvoiceLine line : header.getLines()){
                   if(header.getNum() == index){
                       lineBus[0] = line.getItem();
                       lineBus[1] = line.getPrice();
                       lineBus[2] = line.getCount();
                       lineBus[3] = line.getLineTotal();
                       myLinesModel.addRow(lineBus);
                   }
               }
       }
       return index;
    }
}
