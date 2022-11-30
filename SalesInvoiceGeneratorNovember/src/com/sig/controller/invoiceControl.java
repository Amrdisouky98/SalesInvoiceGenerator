package com.sig.controller;

import com.sig.model.InvoiceHeader;
import com.sig.model.InvoiceLine;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.sig.view.InvoiceFrame;

public class invoiceControl {
    public static ArrayList<InvoiceHeader> arrayOfHeaders = new ArrayList<>();
    public void openFile(JTable myTable) {
        getHeadersFile();
        getLinesFile();
        showData(myTable);
        test();
    }


    private void getHeadersFile() {
        JFileChooser loadFile = new JFileChooser();
        int actionPerformed = loadFile.showOpenDialog(null);
        String path = loadFile.getSelectedFile().getPath();
        if (actionPerformed == JFileChooser.APPROVE_OPTION) {
            BufferedReader csvReader = null;
            try {
                csvReader = new BufferedReader(new FileReader(path));
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null,
                        e.getMessage(),
                        "Sales Invoice Generator",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            while (true) {
                try {
                    if (!((path = csvReader.readLine()) != null)) break;
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null,
                            e.getMessage(),
                            "Sales Invoice Generator",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                String[] lineBus = path.split(",", 3);
                InvoiceHeader newHeader = null;
                try {
                    newHeader = new InvoiceHeader(Integer.parseInt(lineBus[0]), lineBus[2], new SimpleDateFormat("dd-MM-yyyy").parse(lineBus[1]));
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(null,
                            e.getMessage(),
                            "Sales Invoice Generator",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                arrayOfHeaders.add(newHeader);
            }
        }
    }

    private void getLinesFile() {
        JFileChooser loadLines = new JFileChooser();
        int actionPerformer = loadLines.showOpenDialog(null);
        String path = loadLines.getSelectedFile().getPath();
        if (actionPerformer == JFileChooser.APPROVE_OPTION) {
            BufferedReader linesReader = null;
            try {
                linesReader = new BufferedReader(new FileReader(path));
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null,
                        e.getMessage(),
                        "Sales Invoice Generator",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            while (true) {
                try {
                    if (!((path = linesReader.readLine()) != null)) break;
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null,
                            e.getMessage(),
                            "Sales Invoice Generator",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                String[] lineTransporter = path.split(",", 4);
                for (InvoiceHeader header : arrayOfHeaders) {
                    if (header.getNum() == Integer.parseInt(lineTransporter[0])) {
                        header.getLines().add(new InvoiceLine(lineTransporter[1], Double.parseDouble(lineTransporter[2]), Integer.parseInt(lineTransporter[3]), Integer.parseInt(lineTransporter[0])));
                    }
                }
            }
        }

    }

    private void showData(JTable myTable) {

        DefaultTableModel myTableModel = (DefaultTableModel) myTable.getModel();
        myTableModel.setRowCount(0);
        Object[] headerTable = new Object[4];
        for (InvoiceHeader header : arrayOfHeaders) {
            headerTable[0] = header.getNum();
            headerTable[2] = header.getCustomer();
            headerTable[1] = header.getInvDate();
            headerTable[3] = header.getInvoiceTotal();
            myTableModel.addRow(headerTable);
        }
    }

    public void deleteInvoiceButton(JTable myTable, JLabel theLabel) {
        int selectedRow = myTable.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) myTable.getModel();

        if (myTable.getSelectionModel().isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select an invoice", "SIG", JOptionPane.INFORMATION_MESSAGE);
        } else {
            int result = JOptionPane.showOptionDialog(new JFrame(), "Are you sure?", "SIG", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Yes", "No"}, JOptionPane.YES_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                model.removeRow(selectedRow);
                arrayOfHeaders.removeIf(item -> item.getNum() == Integer.parseInt(theLabel.getText()));
                JOptionPane.showMessageDialog(null, "The invoice is deleted", "SIG", JOptionPane.INFORMATION_MESSAGE);
            }
        }

    }

    public void deleteInvoiceLine(JTable linesTable, JLabel invoiceTotal, JLabel invoiceNumber) {

        int theSelectedRowIndex = linesTable.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) linesTable.getModel();

        double itemValue = Double.parseDouble(model.getValueAt(theSelectedRowIndex,3).toString());
        String itemName = model.getValueAt(theSelectedRowIndex,0).toString();

        if (linesTable.getSelectionModel().isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Please select the Invoice!",
                    "Sales Invoice Generator",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            int feedBack = JOptionPane.showOptionDialog(new JFrame(), "Do you want to delete the item?", "Sales Invoice Generator",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                    new Object[]{"Yes", "No, Cancel!"}, JOptionPane.YES_OPTION);
            if (feedBack == JOptionPane.YES_OPTION) {
                model.removeRow(theSelectedRowIndex);

                for (InvoiceHeader invoiceHeader : arrayOfHeaders) {
                    invoiceHeader.getLines().removeIf(item -> item.getItem().equals(itemName));
                }

                invoiceTotal.setText(String.valueOf(Double.parseDouble(invoiceTotal.getText())- itemValue));

                InvoiceFrame.getInvHTbl().getModel().setValueAt(Double.parseDouble(invoiceTotal.getText()),InvoiceFrame.getInvHTbl().getSelectedRow(),3);

                JOptionPane.showMessageDialog(null,
                        "The Invoice is deleted successfully",
                        "Sales Invoice Generator",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    public void SaveFies() {

        JFileChooser myFileChooser = new JFileChooser();
        myFileChooser.setSelectedFile(new File("InvoiceHeader"));
        int feedBack = myFileChooser.showSaveDialog(null);
        myFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        String theHeadersPath = myFileChooser.getCurrentDirectory()+"\\"+"InvoiceHeader"+".csv";
        myFileChooser.setDialogTitle("Please select the folder");
        myFileChooser.setAcceptAllFileFilterUsed(false);
        if (feedBack == JFileChooser.APPROVE_OPTION){
            FileWriter file = null;
            try {
                file = new FileWriter(theHeadersPath);
                for (InvoiceHeader inv : arrayOfHeaders) {
                    file.append(String.valueOf(inv.getNum()));
                    file.append(",");
                    file.append(dateFormatter(inv.getInvDate()));
                    file.append(",");
                    file.append(inv.getCustomer());
                    file.append("\n");
                }
                file.close();
                SavingInvoices();
                JOptionPane.showMessageDialog(null,
                        "File Saved Successfully",
                        "Sales Invoice Generator",
                        JOptionPane.INFORMATION_MESSAGE);

            }catch (Exception e){
                JOptionPane.showMessageDialog(null,
                        e.getMessage(),
                        "Sales Invoice Generator",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "HEADERS path is not selected!",
                    "Sales Invoice Generator",
                    JOptionPane.WARNING_MESSAGE);
        }



    }
    private void SavingInvoices(){
        JOptionPane.showMessageDialog(null, "Kindly select the folder to save LINES","SIG (Attention)", JOptionPane.WARNING_MESSAGE);
        JFileChooser myFileChooser = new JFileChooser();
        myFileChooser.setSelectedFile(new File("InvoiceLine"));
        int feedBack = myFileChooser.showSaveDialog(null);
        myFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        String theLinesPath = myFileChooser.getCurrentDirectory()+"\\"+"InvoiceLine"+".csv";
        myFileChooser.setDialogTitle("Please select the folder");
        myFileChooser.setAcceptAllFileFilterUsed(false);
        if (feedBack == JFileChooser.APPROVE_OPTION){
            FileWriter file = null;
            try {
                file = new FileWriter(theLinesPath);
                for (InvoiceHeader invoicesHeader : arrayOfHeaders) {
                    for (InvoiceLine line : invoicesHeader.getLines()){
                        file.append(String.valueOf(invoicesHeader.getNum()));
                        file.append(",");
                        file.append(line.getItem());
                        file.append(",");
                        file.append(String.valueOf(line.getPrice()));
                        file.append(",");
                        file.append(String.valueOf(line.getCount()));
                        file.append("\n");
                    }
                }
                file.close();
            }catch (Exception e){
                JOptionPane.showMessageDialog(null,
                        e.getMessage(),
                        "SIG",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }else {
            JOptionPane.showMessageDialog(null,
                    "LINES path is not selected!",
                    "SIG",
                    JOptionPane.WARNING_MESSAGE);
        }


    }
    public String dateFormatter(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = formatter.format(date);
        return strDate;
    }

    private void test(){
        for(InvoiceHeader invoice : arrayOfHeaders){
            System.out.println(invoice.getNum() + "      " + invoice.getInvDate() + "      " + invoice.getCustomer() + "      " + invoice.getInvoiceTotal());
            for (InvoiceLine lines : invoice.getLines()){
                System.out.println(lines.getItem() + "      " + lines.getPrice() + "      " + lines.getCount() + "      " + lines.getLineTotal());
            }
            System.out.println("__________________________________________________");
        }
    }
}

