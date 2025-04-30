/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

import repository.constant;
import repository.product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;

/**
 *
 * @author imanudd
 */
public class transaction extends javax.swing.JFrame {
    Connection conn;
    ResultSet rs;
    repository.transaction repoTransaction = new repository.transaction();
    repository.product repoProduct = new repository.product();
    repository.category repoCategory = new repository.category();
    repository.cashier repoCashier = new repository.cashier();
    HashMap <String, Integer> categoryMap= new HashMap<String,Integer>();
    HashMap <String, Integer> productMap= new HashMap<String,Integer>();
    HashMap <String, Integer> cashierMap= new HashMap<String,Integer>();
    int transactionIDGenerated;
    int grandTotal;
    int selectedTableRow;
    public transaction() {
        initComponents();
        btn_struck.setEnabled(false);
        connect();
        initCategory();
        btn_update.setEnabled(false);

        int generateID = generateID();
        txt_transaction_id.setText(String.valueOf(generateID));
    }

    private int generateID(){
        SecureRandom random = new SecureRandom();
        transactionIDGenerated = Math.abs(random.nextInt());
        return transactionIDGenerated;
    }

    private void connect(){
        conn = repository.config.initDB();
    }

    private void initCategory(){
        try {
            getCategories();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void getCategories() throws Exception {
        rs = repoCategory.getData(conn);

        while (rs.next()) {
            String categoryName = rs.getString(2);
            Integer categoryID = rs.getInt(1);

            categoryMap.put(categoryName,categoryID);
            cb_category.addItem(categoryName);
        }
    }

    private void getItems(String category) throws Exception{
        rs = repoProduct.getItemsByCategory(conn,categoryMap.get(category));

        while (rs.next()){
            String productName = rs.getString(3);
            Integer productID = rs.getInt(1);

            productMap.put(productName, productID);
            cb_items.addItem(productName);
        }
    }

    private void getCashiers()throws Exception{
        rs = repoCashier.get(conn);

        while (rs.next()){
            String cashierName = rs.getString(2);
            Integer cashierID = rs.getInt(1);

            cashierMap.put(cashierName,cashierID);
            cb_cashier.addItem(cashierName);
        }
    }
    int pricePerUnit = 0;
    private void getQty(String productName) throws Exception{
        rs = repoProduct.getItemsByID(conn,productMap.get(productName));
        int stock = 0;


        while (rs.next()){
            stock = rs.getInt("total");
            pricePerUnit = rs.getInt("price");
        }

        String itemPrice = formatCurrency(pricePerUnit);
        txt_total_items.setText(itemPrice);

        txt_last_stock.setText(String.valueOf(stock));

        for (int i = 1; i <= stock; i++){
            cb_qty.addItem(String.valueOf(i));
        }

    }

    private void updateStock(Integer stockSold, Integer id)throws Exception{
        boolean status = repoProduct.updateSellingStock(conn,stockSold,id);
        if (!status){
            JOptionPane.showMessageDialog(null,"error update stock");
            return;
        }
    }
    HashMap<String, Integer> transactionData = new HashMap<>();
    private HashMap<String, Integer> initTrxData(){
        SecureRandom random = new SecureRandom();
        int detailTransactionIDGenerated = Math.abs(random.nextInt());

        transactionData.put("transactionID",transactionIDGenerated);
        transactionData.put("detailtransactionID",detailTransactionIDGenerated);
        transactionData.put("productID", productMap.get(cb_items.getSelectedItem()));
        transactionData.put("qty", Integer.parseInt(cb_qty.getSelectedItem().toString()));
        transactionData.put("totalPrice",unFormatCurrency(txt_total_price.getText()));
        transactionData.put("grandTotal", grandTotal);
        transactionData.put("cashierID", cashierMap.get(cb_cashier.getSelectedItem()));

        return transactionData;
    }

    private void createTransaction()throws Exception{
        Integer unformat = unFormatCurrency(txt_total_price.getText());
        grandTotal += unformat;

        String grandTotalFormated = formatCurrency(grandTotal);
        txt_grand_total.setText(grandTotalFormated);
        initTrxData();

        boolean status = repoTransaction.upsertTransaction(conn,transactionData);
        if (!status){
            JOptionPane.showMessageDialog(null,"error insert transaction");
            return;
        }

        status = repoTransaction.insertDetailTransaction(conn,transactionData);
        if (!status){
            JOptionPane.showMessageDialog(null,"error insert detail transaction");
            return;
        }

        updateStock(transactionData.get("qty"),transactionData.get("productID"));
    }

    private void loadProductData(String category){
        cb_items.removeAllItems();
        try {
            getItems(category);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadQtyData(String product){
        cb_qty.removeAllItems();

        try {
            getQty(product);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setGrandTotal() throws Exception{
        rs = repoTransaction.getTransaction(conn,Integer.parseInt(txt_transaction_id.getText()));
        while (rs.next()){
            txt_grand_total.setText(formatCurrency(rs.getInt(2)));
        }
    }

    private void deleteTransaction()throws Exception{
        Object detailTrxID = table_transaction.getValueAt(selectedTableRow,0);
        Object productID = table_transaction.getValueAt(selectedTableRow,1);
        Object qty = table_transaction.getValueAt(selectedTableRow,3);
        Object price = table_transaction.getValueAt(selectedTableRow,4);

        Boolean status = repoTransaction.deleteTransaction(
                conn,
                ((Number) detailTrxID).intValue()
        );

        status = repoTransaction.updateGrandTotal(
                conn,
                Integer.parseInt(txt_transaction_id.getText()),
                grandTotal-((Number) price).intValue()
        );

       status = repoProduct.updateReturnStock(
                conn,
                Integer.parseInt(productID.toString()),
               ((Number) qty).intValue()
        );

        if (!status){
            JOptionPane.showMessageDialog(null,"Error when delete transaction");
            return;
        }

        grandTotal -= ((Number) price).intValue();
        String grTotal = formatCurrency(grandTotal);
        txt_grand_total.setText(grTotal);
    }

    NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("id","ID"));
    private String formatCurrency(Integer amount){
        String formated;
        return formated = format.format(amount);
    }

    private Integer unFormatCurrency(String amount){
        Number parsedAmount = null;
        try {
            parsedAmount = format.parse(amount);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        int numericValue = parsedAmount.intValue();
        return numericValue;
    }

    int rowIndex = 0;

    private void showDataOnTable()throws Exception{
        rs = repoTransaction.getDetailTransaction(conn, transactionIDGenerated);

        int q = rs.getMetaData().getColumnCount();

        DefaultTableModel df = (DefaultTableModel)table_transaction.getModel();
        df.setRowCount(0);

        while(rs.next()) {
            Vector v2 = new Vector();
            for (int i = 1; i <= q; i++) {
                v2.add(rs.getInt("id"));
                v2.add(rs.getString("product_id"));
                v2.add(rs.getString("name"));
                v2.add(rs.getInt("quantity"));
                v2.add(rs.getInt("total_price"));
            }
            df.addRow(v2);
        }
    }

//    private void updateTransaction(){
//        HashMap <String,String> mTrx = new HashMap<>();
//        int lastQty = 0;
//        int grandTotal = unFormatCurrency(txt_grand_total.getText());
//
//        mTrx.put("qtyTrx",cb_qty.getSelectedItem().toString());
//        mTrx.put("totalPrice",String.valueOf(unFormatCurrency(txt_total_items.getText())));
//        mTrx.put("idDetailTrx", table_transaction.getModel().getValueAt(selectedTableRow,0).toString());
//        mTrx.put("idTrx",txt_transaction_id.getText());
//        mTrx.put("operandTrx", "+");
//        mTrx.put("operandProduct" ,"-");
//        mTrx.put("idProduct",table_transaction.getModel().getValueAt(selectedTableRow,1).toString());
//
//        rs = repoTransaction.getDetailTransaction(conn, Integer.parseInt(txt_transaction_id.getText()));
//            try {
//                while (rs.next()) {
//                    lastQty = rs.getInt("total");
//                }
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//            int newQty = Integer.parseInt(cb_qty.getSelectedItem().toString());
//
//            Boolean isIncrement = lastQty < newQty;
//            if (!isIncrement){
//                mTrx.put("operandTrx", "-");
//                mTrx.put("operandProduct" ,"+");
//                mTrx.put("qtyProduct",cb_qty.getSelectedItem().toString());
//
//                Boolean status = repoTransaction.update
//
//                Boolean status = repoTransaction.updateTransactionQTY(conn,mTrx);
//                if (!status){
//                    JOptionPane.showMessageDialog(null,"Error when update transaction");
//                    return;
//                }
//                JOptionPane.showMessageDialog(null,"success when update transaction");
//
//                int finalGrandTotal = grandTotal - Integer.parseInt(txt_total_items.getText());
//                txt_grand_total.setText(formatCurrency(finalGrandTotal));
//
//                return;
//            }
//
//            int finalQty = newQty - lastQty;
//
//            mTrx.put("operandTrx", "+");
//            mTrx.put("operandProduct", "-");
//            mTrx.put("qtyProduct", String.valueOf(finalQty));
//            Boolean status = repoTransaction.updateTransactionQTY(conn,mTrx);
//                if (!status){
//                    JOptionPane.showMessageDialog(null,"Error when update transaction");
//                    return;
//                }
//                    JOptionPane.showMessageDialog(null,"success when update transaction");
//
//        int finalGrandTotal = grandTotal + ((Integer.parseInt(txt_total_items.getText())/newQty) * finalQty);
//        txt_grand_total.setText(formatCurrency(finalGrandTotal));
//    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        cashier = new javax.swing.JButton();
        transaction = new javax.swing.JButton();
        inventory = new javax.swing.JButton();
        supplier = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_total_items = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt_total_price = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cb_category = new javax.swing.JComboBox<>();
        cb_items = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_transaction = new javax.swing.JTable();
        btn_add = new javax.swing.JButton();
        btn_update = new javax.swing.JButton();
        btn_struck = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        cb_cashier = new javax.swing.JComboBox<>();
        txt_last_stock = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cb_qty = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        txt_transaction_id = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txt_grand_total = new javax.swing.JLabel();
        btn_delete = new javax.swing.JButton();
        btn_back = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1000, 500));
        setType(java.awt.Window.Type.UTILITY);
        getContentPane().setLayout(null);

        jPanel2.setBackground(new java.awt.Color(52, 73, 94));
        jPanel2.setForeground(new java.awt.Color(102, 204, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(1000, 100));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText("PET");
        jLabel11.setToolTipText("");
        jLabel11.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel11.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel11.setIconTextGap(20);
        jLabel11.setMaximumSize(new java.awt.Dimension(100, 16));
        jLabel11.setPreferredSize(new java.awt.Dimension(100, 100));

        jLabel12.setFont(new java.awt.Font("Segoe UI Light", 0, 36)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText("STOP");
        jLabel12.setToolTipText("");
        jLabel12.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel12.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel12.setIconTextGap(20);
        jLabel12.setMaximumSize(new java.awt.Dimension(100, 16));
        jLabel12.setPreferredSize(new java.awt.Dimension(100, 100));

        cashier.setBackground(new java.awt.Color(52, 73, 94));
        cashier.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        cashier.setForeground(new java.awt.Color(255, 255, 255));
        cashier.setText("Cashiers");
        cashier.setBorder(null);
        cashier.setContentAreaFilled(false);
        cashier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cashierMouseEntered(evt);
            }
        });
        cashier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashierActionPerformed(evt);
            }
        });

        transaction.setBackground(new java.awt.Color(52, 73, 94));
        transaction.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        transaction.setForeground(new java.awt.Color(255, 255, 255));
        transaction.setText("Transactions");
        transaction.setBorder(null);
        transaction.setBorderPainted(false);
        transaction.setContentAreaFilled(false);
        transaction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transactionActionPerformed(evt);
            }
        });

        inventory.setBackground(new java.awt.Color(52, 73, 94));
        inventory.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        inventory.setForeground(new java.awt.Color(255, 255, 255));
        inventory.setText("Stocks");
        inventory.setBorder(null);
        inventory.setContentAreaFilled(false);
        inventory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inventoryActionPerformed(evt);
            }
        });

        supplier.setBackground(new java.awt.Color(52, 73, 94));
        supplier.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        supplier.setForeground(new java.awt.Color(255, 255, 255));
        supplier.setText("Suppliers");
        supplier.setBorder(null);
        supplier.setContentAreaFilled(false);
        supplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cashier, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(transaction, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inventory, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(supplier, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cashier, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(transaction, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(inventory, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(supplier, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2);
        jPanel2.setBounds(0, 0, 1000, 100);

        jPanel1.setBackground(new java.awt.Color(149, 165, 166));
        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 500));
        jPanel1.setLayout(null);

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Product Name");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(50, 140, 90, 16);

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Qty");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(250, 80, 60, 16);

        txt_total_items.setBackground(new java.awt.Color(204, 255, 255));
        txt_total_items.setForeground(new java.awt.Color(255, 255, 255));
        txt_total_items.setText("0");
        txt_total_items.setPreferredSize(new java.awt.Dimension(10, 10));
        jPanel1.add(txt_total_items);
        txt_total_items.setBounds(140, 210, 72, 16);

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Total");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(250, 110, 50, 16);

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Price");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(50, 210, 70, 16);

        txt_total_price.setForeground(new java.awt.Color(255, 255, 255));
        txt_total_price.setText("0");
        jPanel1.add(txt_total_price);
        txt_total_price.setBounds(310, 110, 72, 16);

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Product Category");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(50, 80, 130, 16);

        cb_category.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_categoryItemStateChanged(evt);
            }
        });
        cb_category.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cb_categoryMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cb_categoryMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cb_categoryMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                cb_categoryMouseReleased(evt);
            }
        });
        cb_category.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_categoryActionPerformed(evt);
            }
        });
        jPanel1.add(cb_category);
        cb_category.setBounds(50, 100, 180, 22);

        cb_items.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_itemsItemStateChanged(evt);
            }
        });
        cb_items.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                cb_itemsMouseMoved(evt);
            }
        });
        cb_items.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cb_itemsMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cb_itemsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                cb_itemsMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cb_itemsMousePressed(evt);
            }
        });
        cb_items.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_itemsActionPerformed(evt);
            }
        });
        jPanel1.add(cb_items);
        cb_items.setBounds(50, 160, 180, 22);

        table_transaction.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "No. Transaction", "Kode Barang", "Nama Barang", "Qty", "Harga"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_transaction.setGridColor(new java.awt.Color(255, 204, 204));
        table_transaction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_transactionMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(table_transaction);

        jPanel1.add(jScrollPane2);
        jScrollPane2.setBounds(450, 40, 500, 240);

        btn_add.setText("insert");
        btn_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addActionPerformed(evt);
            }
        });
        jPanel1.add(btn_add);
        btn_add.setBounds(240, 210, 170, 23);

        btn_update.setText("update");
        btn_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateActionPerformed(evt);
            }
        });
        jPanel1.add(btn_update);
        btn_update.setBounds(240, 240, 80, 23);

        btn_struck.setText("print invoice");
        btn_struck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_struckActionPerformed(evt);
            }
        });
        jPanel1.add(btn_struck);
        btn_struck.setBounds(240, 290, 170, 23);

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Cashier");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(50, 240, 41, 16);

        cb_cashier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cb_cashierMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cb_cashierMouseEntered(evt);
            }
        });
        cb_cashier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_cashierActionPerformed(evt);
            }
        });
        jPanel1.add(cb_cashier);
        cb_cashier.setBounds(50, 260, 163, 22);

        txt_last_stock.setForeground(new java.awt.Color(255, 255, 255));
        txt_last_stock.setText("0");
        jPanel1.add(txt_last_stock);
        txt_last_stock.setBounds(140, 190, 82, 16);

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Total Stock");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(50, 190, 100, 16);

        cb_qty.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cb_qtyMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cb_qtyMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                cb_qtyMouseExited(evt);
            }
        });
        cb_qty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_qtyActionPerformed(evt);
            }
        });
        jPanel1.add(cb_qty);
        cb_qty.setBounds(310, 80, 88, 22);

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Transaksi id : ");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(50, 50, 90, 16);

        txt_transaction_id.setForeground(new java.awt.Color(255, 255, 255));
        txt_transaction_id.setText("trxid");
        jPanel1.add(txt_transaction_id);
        txt_transaction_id.setBounds(130, 50, 88, 16);

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Grand Total");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(450, 290, 120, 20);

        txt_grand_total.setForeground(new java.awt.Color(255, 255, 255));
        txt_grand_total.setText("0");
        jPanel1.add(txt_grand_total);
        txt_grand_total.setBounds(590, 290, 130, 20);

        btn_delete.setText("delete");
        btn_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteActionPerformed(evt);
            }
        });
        jPanel1.add(btn_delete);
        btn_delete.setBounds(330, 240, 80, 23);

        btn_back.setText("Back");
        btn_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_backActionPerformed(evt);
            }
        });
        jPanel1.add(btn_back);
        btn_back.setBounds(770, 290, 170, 30);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Transactions");
        jPanel1.add(jLabel13);
        jLabel13.setBounds(30, 0, 215, 40);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 100, 1000, 620);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cb_categoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_categoryActionPerformed
    }//GEN-LAST:event_cb_categoryActionPerformed

    private void cb_itemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_itemsActionPerformed
       
    }//GEN-LAST:event_cb_itemsActionPerformed

    private void cb_itemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cb_itemsMouseClicked
    }//GEN-LAST:event_cb_itemsMouseClicked

    private void cb_categoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cb_categoryMouseClicked

    }//GEN-LAST:event_cb_categoryMouseClicked

    private void cb_cashierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cb_cashierMouseClicked


    }//GEN-LAST:event_cb_cashierMouseClicked

    private void cb_cashierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_cashierActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_cashierActionPerformed

    private void btn_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addActionPerformed
        try {
            createTransaction();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please input your transaction first");
        }

        try {
            showDataOnTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please show your data transaction");
        }

        cb_cashier.setEnabled(false);
       
    }//GEN-LAST:event_btn_addActionPerformed

    private void cb_qtyMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cb_qtyMouseEntered
        if (cb_items.getSelectedItem() == null){
            return;
        }
        loadQtyData(cb_items.getSelectedItem().toString());
    }//GEN-LAST:event_cb_qtyMouseEntered

    private void cb_categoryMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cb_categoryMouseEntered
        if (cb_category.getItemCount() != 0){
            return;
        }
    }//GEN-LAST:event_cb_categoryMouseEntered

    private void cb_itemsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cb_itemsMouseEntered
        String selectedCategory = cb_category.getSelectedItem().toString();
        loadProductData(selectedCategory);
    }//GEN-LAST:event_cb_itemsMouseEntered

    private void cb_cashierMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cb_cashierMouseEntered
        if (cb_cashier.getItemCount() != 0){
            return;
        }

        try {
            getCashiers();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }//GEN-LAST:event_cb_cashierMouseEntered

    private void cb_qtyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cb_qtyMouseClicked
    }//GEN-LAST:event_cb_qtyMouseClicked

    private void cb_qtyMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cb_qtyMouseExited

    }//GEN-LAST:event_cb_qtyMouseExited

    private void cb_qtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_qtyActionPerformed
        if (cb_qty.getSelectedItem() == null){
            return;
        }

        int total = Integer.parseInt(cb_qty.getSelectedItem().toString()) * pricePerUnit;
        String totalPrice = formatCurrency(total);
        txt_total_price.setText(totalPrice);


    }//GEN-LAST:event_cb_qtyActionPerformed

    private void cb_itemsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cb_itemsMouseExited
      
    }//GEN-LAST:event_cb_itemsMouseExited

    private void cb_categoryMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cb_categoryMousePressed

    }//GEN-LAST:event_cb_categoryMousePressed

    private void cb_itemsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cb_itemsMousePressed

    }//GEN-LAST:event_cb_itemsMousePressed

    private void cb_categoryMouseReleased(java.awt.event.MouseEvent evt) {                                              
    }                                         

    private void cb_categoryItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_categoryItemStateChanged
        cb_items.removeAllItems();
    }//GEN-LAST:event_cb_categoryItemStateChanged

    private void cb_itemsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_itemsItemStateChanged
  
    }//GEN-LAST:event_cb_itemsItemStateChanged

    private void cb_itemsMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cb_itemsMouseMoved
        
    }//GEN-LAST:event_cb_itemsMouseMoved

    private void table_transactionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_transactionMouseClicked
        int row = table_transaction.getSelectedRow();

        cb_items.setEnabled(false);
        cb_category.setEnabled(false);
    }//GEN-LAST:event_table_transactionMouseClicked

    private void btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteActionPerformed
        DefaultTableModel df = (DefaultTableModel)table_transaction.getModel();
        selectedTableRow = table_transaction.getSelectedRow();

        try {
            deleteTransaction();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        df.removeRow(selectedTableRow);
        cb_items.setEnabled(true);
        cb_category.setEnabled(true);
    }//GEN-LAST:event_btn_deleteActionPerformed

    private void btn_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateActionPerformed
//        updateTransaction();

    }//GEN-LAST:event_btn_updateActionPerformed

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_backActionPerformed
       home home = new home();
       home.setVisible(true);
       
       this.setVisible(false);
    }//GEN-LAST:event_btn_backActionPerformed

    private void cashierMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cashierMouseEntered

    }//GEN-LAST:event_cashierMouseEntered

    private void cashierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashierActionPerformed
        this.setVisible(false);
        new cashier().setVisible(true);
    }//GEN-LAST:event_cashierActionPerformed

    private void transactionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transactionActionPerformed
        this.setVisible(false);
        new transaction().setVisible(true);
    }//GEN-LAST:event_transactionActionPerformed

    private void inventoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inventoryActionPerformed
        product pr = new product();
        this.setVisible(false);
    }//GEN-LAST:event_inventoryActionPerformed

    private void supplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierActionPerformed
        this.setVisible(false);
        new supplier().setVisible(true);
    }//GEN-LAST:event_supplierActionPerformed

    private void btn_struckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_struckActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_struckActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(transaction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(transaction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(transaction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(transaction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new transaction().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_add;
    private javax.swing.JButton btn_back;
    private javax.swing.JButton btn_delete;
    private javax.swing.JButton btn_struck;
    private javax.swing.JButton btn_update;
    private javax.swing.JButton cashier;
    private javax.swing.JComboBox<String> cb_cashier;
    private javax.swing.JComboBox<String> cb_category;
    private javax.swing.JComboBox<String> cb_items;
    private javax.swing.JComboBox<String> cb_qty;
    private javax.swing.JButton inventory;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton supplier;
    private javax.swing.JTable table_transaction;
    private javax.swing.JButton transaction;
    private javax.swing.JLabel txt_grand_total;
    private javax.swing.JLabel txt_last_stock;
    private javax.swing.JLabel txt_total_items;
    private javax.swing.JLabel txt_total_price;
    private javax.swing.JLabel txt_transaction_id;
    // End of variables declaration//GEN-END:variables
}
