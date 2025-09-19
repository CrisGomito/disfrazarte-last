
package com.mycompany.views;

import com.mycompany.dsfr.DAOUserCostumesImpl;
import com.mycompany.dsfr.DAOUsersImpl;
import com.mycompany.dsfr.DAODisfracesImpl;
import com.mycompany.dsfr.Dashboard;
import com.mycompany.models.Disfraces;
import com.mycompany.models.UserCostume;
import com.mycompany.models.Users;

import java.awt.Color;
import java.awt.Component;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

/**
 *
 * @author cristian
 */
public class AssignUsers extends javax.swing.JPanel {

    private int disfrazId;

    public AssignUsers(int disfrazId) {
        this.disfrazId = disfrazId;
        initComponents();
        InitStyles();

        try {
            DAODisfracesImpl daoDisfraz = new DAODisfracesImpl();
            Disfraces disfraz = daoDisfraz.getDsfrById(disfrazId);

            if (disfraz != null) {
                String infoDisfraz = String.format(
                        "%d | %s | %s | stock:%d | disp:%d",
                        disfraz.getId(),
                        disfraz.getTitle(),
                        disfraz.getTematica(),
                        disfraz.getStock(),
                        disfraz.getAvailable()
                );
                idCostume.setText(infoDisfraz);
            } else {
                idCostume.setText(String.valueOf(disfrazId));
            }
        } catch (Exception e) {
            idCostume.setText(String.valueOf(disfrazId));
            e.printStackTrace();
        }

        cargarUsuarios();
        mostrarInfoDisfraz();
        marcarUsuariosAsignados();
    }

    private void InitStyles() {
        title.putClientProperty("FlatLaf.style", "font: light 20 $h0.font;");
        title.setForeground(Color.black);
        idDisfraz.putClientProperty("FlatLaf.styleClass", "large");
        idDisfraz.setForeground(Color.black);
        idsUsuarios.putClientProperty("FlatLaf.styleClass", "large");
        idsUsuarios.setForeground(Color.black);

        statusjComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{"Reservado", "Asignado", "Pagado", "Devuelto"}
        ));
    }

    private void mostrarInfoDisfraz() {
        try {
            DAODisfracesImpl daoDsfr = new DAODisfracesImpl();
            Object[] detalle = daoDsfr.obtenerDetallesPorId(disfrazId);

            if (detalle != null) {
                String info = String.format(
                        "[%d] %s | %s | %s | Stock: %d | Disp: %d",
                        detalle[0],
                        detalle[1],
                        detalle[2],
                        detalle[7],
                        detalle[5],
                        detalle[6]
                );
                idCostume.setText(info);
            } else {
                idCostume.setText("Disfraz no encontrado (ID " + disfrazId + ")");
            }
        } catch (Exception e) {
            idCostume.setText("Error cargando disfraz " + disfrazId);
            e.printStackTrace();
        }
        idCostume.setEditable(false);
    }

    private void cargarUsuarios() {
        try {
            DAOUsersImpl daoUsers = new DAOUsersImpl();
            List<Users> lista = daoUsers.listar("");

            DefaultListModel<ItemUsuario> model = new DefaultListModel<>();
            for (Users u : lista) {
                model.addElement(new ItemUsuario(u.getId(), u.getName(), u.getLast_name_p(), u.getLast_name_m(), u.getEmail()));
            }

            listaUsersjList1.setModel(model);
            listaUsersjList1.setCellRenderer(new CheckBoxListRenderer());
            listaUsersjList1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando usuarios: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void marcarUsuariosAsignados() {
        try {
            DAOUserCostumesImpl daoUC = new DAOUserCostumesImpl();
            List<UserCostume> asignados = daoUC.listarPorDisfraz(disfrazId);

            String estadoDisfraz = null;

            for (int i = 0; i < listaUsersjList1.getModel().getSize(); i++) {
                ItemUsuario item = listaUsersjList1.getModel().getElementAt(i);

                for (UserCostume uc : asignados) {
                    if (uc.getUserId() == item.id) {
                        listaUsersjList1.addSelectionInterval(i, i);
                        if (estadoDisfraz == null) {
                            estadoDisfraz = uc.getStatus();
                        }
                        break;
                    }
                }
            }

            if (estadoDisfraz != null) {
                statusjComboBox1.setSelectedItem(estadoDisfraz);
            } else {
                statusjComboBox1.setSelectedItem("Reservado");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando asignaciones: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static class ItemUsuario {

        final int id;
        final String nombre;
        final String apellidoP;
        final String apellidoM;
        final String email;

        ItemUsuario(int id, String nombre, String apellidoP, String apellidoM, String email) {
            this.id = id;
            this.nombre = nombre;
            this.apellidoP = apellidoP;
            this.apellidoM = apellidoM;
            this.email = email;
        }

        @Override
        public String toString() {
            return String.format("[%d] %s %s %s | %s", id, nombre, apellidoP, apellidoM, email);
        }
    }

    public static class CheckBoxListRenderer extends JCheckBox implements ListCellRenderer<ItemUsuario> {

        @Override
        public Component getListCellRendererComponent(JList<? extends ItemUsuario> list, ItemUsuario value,
                int index, boolean isSelected, boolean cellHasFocus) {
            setText(value.toString());
            setSelected(isSelected);
            setBackground(isSelected ? new Color(220, 240, 255) : Color.WHITE);
            setForeground(Color.BLACK);
            return this;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        idDisfraz = new javax.swing.JLabel();
        idCostume = new javax.swing.JTextField();
        idsUsuarios = new javax.swing.JLabel();
        saveButton = new javax.swing.JButton();
        image = new javax.swing.JLabel();
        returnButton1 = new javax.swing.JButton();
        title = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaUsersjList1 = new javax.swing.JList<>();
        statusjComboBox1 = new javax.swing.JComboBox<>();
        idsUsuarios1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(752, 427));

        bg.setBackground(new java.awt.Color(255, 255, 255));
        bg.setPreferredSize(new java.awt.Dimension(752, 427));

        jSeparator1.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator1.setPreferredSize(new java.awt.Dimension(200, 10));

        idDisfraz.setText("Disfraz:");

        idCostume.setToolTipText("");
        idCostume.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idCostumeActionPerformed(evt);
            }
        });

        idsUsuarios.setText("Usuarios:");

        saveButton.setBackground(new java.awt.Color(18, 90, 173));
        saveButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        saveButton.setForeground(new java.awt.Color(255, 255, 255));
        saveButton.setText("Guardar");
        saveButton.setBorderPainted(false);
        saveButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Sin título-1.png"))); // NOI18N

        returnButton1.setBackground(new java.awt.Color(18, 90, 173));
        returnButton1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        returnButton1.setForeground(new java.awt.Color(255, 255, 255));
        returnButton1.setText("Atrás");
        returnButton1.setBorderPainted(false);
        returnButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        returnButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnButton1ActionPerformed(evt);
            }
        });

        title.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        title.setText("Gestionar Usuarios a un Disfraz");

        jScrollPane1.setBorder(null);

        listaUsersjList1.setModel(new DefaultListModel<>()
        );
        jScrollPane1.setViewportView(listaUsersjList1);

        statusjComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        idsUsuarios1.setText("Estado:");

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addComponent(image, javax.swing.GroupLayout.PREFERRED_SIZE, 366, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(idCostume)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addComponent(saveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(76, 76, 76)
                        .addComponent(returnButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(bgLayout.createSequentialGroup()
                        .addComponent(idsUsuarios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(80, 80, 80)
                        .addComponent(idsUsuarios1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(statusjComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(idDisfraz, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12))
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addComponent(title)
                        .addGap(18, 18, 18)
                        .addComponent(idDisfraz)
                        .addGap(18, 18, 18)
                        .addComponent(idCostume, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bgLayout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(idsUsuarios))
                            .addGroup(bgLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(statusjComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(idsUsuarios1))))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(returnButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(44, 44, 44))
            .addGroup(bgLayout.createSequentialGroup()
                .addComponent(image, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.PREFERRED_SIZE, 410, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        try {
            List<ItemUsuario> seleccionados = listaUsersjList1.getSelectedValuesList();
            String estadoNuevo = (String) statusjComboBox1.getSelectedItem();
            DAOUserCostumesImpl daoUC = new DAOUserCostumesImpl();

            List<UserCostume> actuales = daoUC.listarPorDisfraz(disfrazId);

            if (!actuales.isEmpty() && seleccionados.isEmpty()) {
                for (UserCostume uc : actuales) {
                    if (uc.getStatus().equalsIgnoreCase("Reservado")
                            || uc.getStatus().equalsIgnoreCase("Asignado")
                            || uc.getStatus().equalsIgnoreCase("Pagado")) {
                        daoUC.devolverDisponible(uc.getDsfrId(), uc.getQty());
                    }
                }
            }

            for (UserCostume uc : actuales) {
                boolean sigueSeleccionado = seleccionados.stream().anyMatch(u -> u.id == uc.getUserId());
                if (sigueSeleccionado && estadoNuevo.equalsIgnoreCase("Devuelto")) {
                    if (uc.getStatus().equalsIgnoreCase("Reservado")
                            || uc.getStatus().equalsIgnoreCase("Asignado")
                            || uc.getStatus().equalsIgnoreCase("Pagado")) {
                        daoUC.devolverDisponible(uc.getDsfrId(), uc.getQty());
                    }
                }
            }

            for (UserCostume uc : actuales) {
                daoUC.eliminarAsignacion(uc.getUserId(), uc.getDsfrId(), false);
            }

            for (ItemUsuario user : seleccionados) {
                UserCostume uc = new UserCostume();
                uc.setUserId(user.id);
                uc.setDsfrId(disfrazId);
                uc.setQty(1);
                uc.setStatus(estadoNuevo);
                daoUC.registrar(uc);
            }

            JOptionPane.showMessageDialog(this, "Datos guardados correctamente.", "ÉXITO", JOptionPane.INFORMATION_MESSAGE);
            Dashboard.ShowJPanel(new com.mycompany.views.Costumes());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error gestionando usuarios: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void returnButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnButton1ActionPerformed
        Dashboard.ShowJPanel(new com.mycompany.views.Costumes());
    }//GEN-LAST:event_returnButton1ActionPerformed

    private void idCostumeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idCostumeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idCostumeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
    private javax.swing.JTextField idCostume;
    private javax.swing.JLabel idDisfraz;
    private javax.swing.JLabel idsUsuarios;
    private javax.swing.JLabel idsUsuarios1;
    private javax.swing.JLabel image;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JList<ItemUsuario> listaUsersjList1;
    private javax.swing.JButton returnButton1;
    private javax.swing.JButton saveButton;
    private javax.swing.JComboBox<String> statusjComboBox1;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
