/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author imanudd
 */
public class product extends javax.swing.JFrame {

    Connection conn;
    ResultSet rs;
    repository.category repoCategory = new repository.category();
    repository.supplier repoSupplier = new repository.supplier();
    repository.product repoProduct = new repository.product();
    HashMap<String, Integer> categoryMap= new HashMap<String,Integer>();
    HashMap<String, Integer> supplierMap= new HashMap<>();
    dto.product product = new dto.product();

    public product() {
        initComponents();

        txt_productID.setEnabled(false);

        connect();
        try {
            getCategories();
            getSuppliers();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void connect(){
        conn = repository.config.initDB();
    }
    private void getCategories() throws Exception {
        rs = repoCategory.getData(conn);

        while (rs.next()) {
            String categoryName = rs.getString(2);
            Integer categoryID = rs.getInt(1);

            categoryMap.put(categoryName,categoryID);
            cb_categorySearch.addItem(categoryName);
            cb_category.addItem(categoryName);
        }
    }
    private void getSuppliers() throws Exception {
        rs = repoSupplier.getData(conn);

        while (rs.next()) {
            Integer supplierID = rs.getInt(1);
            String supplierName = rs.getString(2);

            supplierMap.put(supplierName,supplierID);
            cb_supplier.addItem(supplierName);
        }
    }

    private void getProductByCategory(String categoryName){
        int q = 0;
        try {
            rs = repoProduct.getProductData(conn,categoryMap.get(categoryName));

            q = rs.getMetaData().getColumnCount();
            DefaultTableModel df = (DefaultTableModel)tb_product.getModel();
            df.setRowCount(0);

            while(rs.next()){
                Vector v2 = new Vector();
                for (int i = 1; i<=q; i++){
                    v2.add(rs.getString("id"));
                    v2.add(rs.getString("product_name"));
                    v2.add(rs.getString("last_stock"));
                    v2.add(rs.getString("price"));
                    v2.add(rs.getString("supplier_name"));
                }
                df.addRow(v2);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private dto.product initDataProduct(String categoryName, String supplierName){
        SecureRandom random = new SecureRandom();
        int transactionIDGenerated = Math.abs(random.nextInt());

        dto.product product = new dto.product(
                transactionIDGenerated,
                categoryMap.get(categoryName),
                Integer.parseInt(txt_stock.getText()),
                Integer.parseInt(txt_price.getText()),
                supplierMap.get(supplierName),
                txt_productName.getText()
        );

        return product;
    }

    private void addNewProduct(String categoryName, String supplierName){
        Boolean isSuccess = repoProduct.addNewProduct(conn,initDataProduct(categoryName, supplierName));

        if (!isSuccess){
            JOptionPane.showMessageDialog(null,"error add product");
            return;
        }
        JOptionPane.showMessageDialog(null,"success add product");
    }

    private void updateProduct(){
        product.setId(Integer.parseInt(txt_productID.getText()));
        product.setProductName(txt_productName.getText());
        product.setPrice(Integer.parseInt(txt_price.getText()));
        product.setTotal(Integer.parseInt(txt_stock.getText()));

        Boolean isSuccess = repoProduct.updateProductInfo(conn,product);
        if (!isSuccess){
            JOptionPane.showMessageDialog(null,"Failed to update product");
            return;
        }

        JOptionPane.showMessageDialog(null,"success update product");
    }

    private void deleteProduct(){
        Boolean isSuccess = repoProduct.delete(conn,Integer.parseInt(txt_productID.getText()));
        if (!isSuccess){
            JOptionPane.showMessageDialog(null,"Failed to delete product");
            return;
        }

        JOptionPane.showMessageDialog(null,"success delete product");
    }

    private void clearData(){
        txt_productID.setText("");
        txt_productName.setText("");
        txt_price.setText("");
        txt_stock.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cashier = new javax.swing.JButton();
        transaction = new javax.swing.JButton();
        inventory = new javax.swing.JButton();
        supplier = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tb_product = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        cb_categorySearch = new javax.swing.JComboBox<>();
        btn_search = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txt_stock = new javax.swing.JTextField();
        txt_productID = new javax.swing.JTextField();
        cb_supplier = new javax.swing.JComboBox<>();
        txt_productName = new javax.swing.JTextField();
        txt_price = new javax.swing.JTextField();
        btn_delete = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        cb_category = new javax.swing.JComboBox<>();
        btn_add = new javax.swing.JButton();
        btn_update = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1000, 451));
        setPreferredSize(new java.awt.Dimension(1000, 500));
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(52, 73, 94));
        jPanel1.setForeground(new java.awt.Color(102, 204, 255));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("PET");
        jLabel8.setToolTipText("");
        jLabel8.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel8.setIconTextGap(20);
        jLabel8.setMaximumSize(new java.awt.Dimension(100, 16));
        jLabel8.setPreferredSize(new java.awt.Dimension(100, 100));

        jLabel9.setFont(new java.awt.Font("Segoe UI Light", 0, 36)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("STOP");
        jLabel9.setToolTipText("");
        jLabel9.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel9.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel9.setIconTextGap(20);
        jLabel9.setMaximumSize(new java.awt.Dimension(100, 16));
        jLabel9.setPreferredSize(new java.awt.Dimension(100, 100));

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cashier, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(transaction, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(inventory, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(supplier, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cashier, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(transaction, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inventory, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(supplier, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31))
        );

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 1000, 100);

        jPanel2.setBackground(new java.awt.Color(149, 165, 166));
        jPanel2.setLayout(null);

        tb_product.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Product ID", "Product Name", "last Stock", "Price", "Supplier"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_product.setColumnSelectionAllowed(true);
        tb_product.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_productMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tb_productMouseEntered(evt);
            }
        });
        jScrollPane2.setViewportView(tb_product);

        jPanel2.add(jScrollPane2);
        jScrollPane2.setBounds(20, 50, 600, 300);

        jLabel1.setText("Category : ");
        jPanel2.add(jLabel1);
        jLabel1.setBounds(60, 20, 60, 16);

        cb_categorySearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cb_categorySearchMouseEntered(evt);
            }
        });
        jPanel2.add(cb_categorySearch);
        cb_categorySearch.setBounds(120, 20, 140, 22);

        btn_search.setText("Search");
        btn_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchActionPerformed(evt);
            }
        });
        jPanel2.add(btn_search);
        btn_search.setBounds(270, 20, 75, 23);

        jLabel2.setText("ProductID");
        jPanel2.add(jLabel2);
        jLabel2.setBounds(700, 50, 110, 16);

        jLabel3.setText("Price");
        jPanel2.add(jLabel3);
        jLabel3.setBounds(700, 170, 37, 16);

        jLabel4.setText("Product Name");
        jPanel2.add(jLabel4);
        jLabel4.setBounds(700, 80, 110, 16);

        jLabel5.setText("Supplier ");
        jPanel2.add(jLabel5);
        jLabel5.setBounds(700, 140, 100, 16);

        jLabel6.setText("Stock");
        jPanel2.add(jLabel6);
        jLabel6.setBounds(700, 200, 37, 16);
        jPanel2.add(txt_stock);
        txt_stock.setBounds(810, 200, 50, 22);
        jPanel2.add(txt_productID);
        txt_productID.setBounds(810, 50, 130, 22);

        jPanel2.add(cb_supplier);
        cb_supplier.setBounds(810, 140, 130, 22);
        jPanel2.add(txt_productName);
        txt_productName.setBounds(810, 80, 130, 22);
        jPanel2.add(txt_price);
        txt_price.setBounds(810, 170, 130, 22);

        btn_delete.setText("Delete");
        btn_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteActionPerformed(evt);
            }
        });
        jPanel2.add(btn_delete);
        btn_delete.setBounds(700, 320, 250, 30);

        jLabel7.setText("Category");
        jPanel2.add(jLabel7);
        jLabel7.setBounds(700, 110, 100, 16);

        jPanel2.add(cb_category);
        cb_category.setBounds(810, 110, 130, 22);

        btn_add.setText("Add Product");
        btn_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addActionPerformed(evt);
            }
        });
        jPanel2.add(btn_add);
        btn_add.setBounds(700, 240, 250, 30);

        btn_update.setText("Update");
        btn_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateActionPerformed(evt);
            }
        });
        jPanel2.add(btn_update);
        btn_update.setBounds(700, 280, 250, 30);

        getContentPane().add(jPanel2);
        jPanel2.setBounds(0, 100, 1000, 410);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cb_categorySearchMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cb_categorySearchMouseEntered
        if (cb_categorySearch.getItemCount() != 0) {
            return;
        }
    }//GEN-LAST:event_cb_categorySearchMouseEntered

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
        product productPage = new product();
        productPage.setVisible(true);

        this.setVisible(false);
    }//GEN-LAST:event_inventoryActionPerformed

    private void supplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierActionPerformed
        this.setVisible(false);
        new supplier().setVisible(true);
    }//GEN-LAST:event_supplierActionPerformed

    private void btn_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchActionPerformed
       String categoryName = cb_categorySearch.getSelectedItem().toString();

       getProductByCategory(categoryName);
       clearData();
       cb_category.setEnabled(true);
       cb_supplier.setEnabled(true);
    }//GEN-LAST:event_btn_searchActionPerformed

    private void btn_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addActionPerformed
        String categoryName = cb_category.getSelectedItem().toString();
        String supplierName = cb_supplier.getSelectedItem().toString();

        addNewProduct(categoryName, supplierName);
        clearData();
    }//GEN-LAST:event_btn_addActionPerformed

    private void btn_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateActionPerformed
        updateProduct();
        clearData();
    }//GEN-LAST:event_btn_updateActionPerformed

    private void btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteActionPerformed
        cb_supplier.setEnabled(true);
        cb_category.setEnabled(true);

        deleteProduct();
        clearData();
    }//GEN-LAST:event_btn_deleteActionPerformed

    private void tb_productMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_productMouseClicked
      int row = tb_product.getSelectedRow();
      txt_productID.setText(tb_product.getModel().getValueAt(row, 0).toString());
      txt_productName.setText(tb_product.getModel().getValueAt(row, 1).toString());
      txt_stock.setText(tb_product.getModel().getValueAt(row, 2).toString());
      txt_price.setText(tb_product.getModel().getValueAt(row, 3).toString());

      cb_supplier.setEnabled(false);
      cb_category.setEnabled(false);
    }//GEN-LAST:event_tb_productMouseClicked

    private void tb_productMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_productMouseEntered
        
    }//GEN-LAST:event_tb_productMouseEntered

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
            java.util.logging.Logger.getLogger(product.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(product.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(product.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(product.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new product().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_add;
    private javax.swing.JButton btn_delete;
    private javax.swing.JButton btn_search;
    private javax.swing.JButton btn_update;
    private javax.swing.JButton cashier;
    private javax.swing.JComboBox<String> cb_category;
    private javax.swing.JComboBox<String> cb_categorySearch;
    private javax.swing.JComboBox<String> cb_supplier;
    private javax.swing.JButton inventory;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton supplier;
    private javax.swing.JTable tb_product;
    private javax.swing.JButton transaction;
    private javax.swing.JTextField txt_price;
    private javax.swing.JTextField txt_productID;
    private javax.swing.JTextField txt_productName;
    private javax.swing.JTextField txt_stock;
    // End of variables declaration//GEN-END:variables
}
