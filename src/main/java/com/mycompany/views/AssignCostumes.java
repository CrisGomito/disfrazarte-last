
package com.mycompany.views;

import com.mycompany.dsfr.DAODisfracesImpl;
import com.mycompany.dsfr.DAOUserCostumesImpl;
import com.mycompany.dsfr.DAOUsersImpl;
import com.mycompany.dsfr.Dashboard;
import com.mycompany.interfaces.DAODisfraces;
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
public class AssignCostumes extends javax.swing.JPanel {

    public AssignCostumes() {
        initComponents();
        InitStyles();
        cargarDisfraces();
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

    public AssignCostumes(int userId) {
        initComponents();
        InitStyles();
        cargarDisfraces();

        try {
            DAOUsersImpl daoUsers = new DAOUsersImpl();
            Users user = daoUsers.getUserById(userId);

            if (user != null) {
                String infoUsuario = String.format(
                        "[%d] %s %s %s | %s",
                        user.getId(),
                        user.getName(),
                        user.getLast_name_p(),
                        user.getLast_name_m(),
                        user.getEmail() != null ? user.getEmail() : "-"
                );
                idUser.setText(infoUsuario);
            } else {
                idUser.setText(String.valueOf(userId));
            }
        } catch (Exception e) {
            idUser.setText(String.valueOf(userId));
            e.printStackTrace();
        }

        idUser.setEditable(false);
        marcarDisfracesAsignados(userId);
    }

    private void cargarDisfraces() {
        try {
            DAODisfraces dao = new DAODisfracesImpl();
            List<Disfraces> lista = dao.listar("");

            DefaultListModel<ItemDisfraz> model = new DefaultListModel<>();
            for (Disfraces d : lista) {
                model.addElement(new ItemDisfraz(
                        d.getId(),
                        d.getTitle(),
                        d.getTematica(),
                        d.getEstado(),
                        d.getStock(),
                        d.getAvailable()
                ));
            }

            listaDisfracesjList1.setModel(model);
            listaDisfracesjList1.setCellRenderer(new CheckBoxListRenderer());
            listaDisfracesjList1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando disfraces: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void marcarDisfracesAsignados(int userId) {
        try {
            DAOUserCostumesImpl daoUC = new DAOUserCostumesImpl();
            List<UserCostume> asignados = daoUC.listarPorUsuario(userId);

            String estadoUsuario = null;

            for (int i = 0; i < listaDisfracesjList1.getModel().getSize(); i++) {
                ItemDisfraz item = listaDisfracesjList1.getModel().getElementAt(i);

                for (UserCostume uc : asignados) {
                    if (uc.getDsfrId() == item.id) {

                        listaDisfracesjList1.addSelectionInterval(i, i);

                        if (estadoUsuario == null) {
                            estadoUsuario = uc.getStatus();
                        }
                        break;
                    }
                }
            }

            String[] estados = {"Reservado", "Asignado", "Pagado", "Devuelto"};
            statusjComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(estados));
            if (estadoUsuario != null && !estadoUsuario.trim().isEmpty()) {
                statusjComboBox1.setSelectedItem(estadoUsuario);
            } else {
                statusjComboBox1.setSelectedItem("Reservado");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando asignaciones: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static class ItemDisfraz {

        final int id;
        final String nombre;
        final String tematica;
        final String estado;
        final int stock;
        final int disponibles;

        ItemDisfraz(int id, String nombre, String tematica, String estado, int stock, int disponibles) {
            this.id = id;
            this.nombre = nombre;
            this.tematica = tematica;
            this.estado = estado;
            this.stock = stock;
            this.disponibles = disponibles;
        }

        @Override
        public String toString() {
            return String.format("[%d] %s | %s | %s | stock:%d disp:%d",
                    id, nombre, tematica, estado, stock, disponibles);
        }
    }

    public static class CheckBoxListRenderer extends JCheckBox implements ListCellRenderer<ItemDisfraz> {

        @Override
        public Component getListCellRendererComponent(JList<? extends ItemDisfraz> list, ItemDisfraz value,
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
        idUser = new javax.swing.JTextField();
        idsUsuarios = new javax.swing.JLabel();
        saveButton = new javax.swing.JButton();
        image = new javax.swing.JLabel();
        returnButton1 = new javax.swing.JButton();
        title = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaDisfracesjList1 = new javax.swing.JList<>();
        statusjComboBox1 = new javax.swing.JComboBox<>();
        idsUsuarios1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(752, 427));

        bg.setBackground(new java.awt.Color(255, 255, 255));
        bg.setPreferredSize(new java.awt.Dimension(752, 427));

        jSeparator1.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator1.setPreferredSize(new java.awt.Dimension(200, 10));

        idDisfraz.setText("Usuario:");

        idUser.setToolTipText("");

        idsUsuarios.setText("Disfraces:");

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

        image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Sin título-2.png"))); // NOI18N

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
        title.setText("Gestionar Disfraces a un Usuario");

        jScrollPane1.setBorder(null);

        listaDisfracesjList1.setModel(new DefaultListModel<>()
        );
        jScrollPane1.setViewportView(listaDisfracesjList1);

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addComponent(saveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(76, 76, 76)
                        .addComponent(returnButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1)
                    .addComponent(idUser)
                    .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addComponent(idsUsuarios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(126, 126, 126)
                        .addComponent(idsUsuarios1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(statusjComboBox1, 0, 108, Short.MAX_VALUE))
                    .addComponent(idDisfraz, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12))
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(image, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(32, 32, 32))
                    .addGroup(bgLayout.createSequentialGroup()
                        .addComponent(title)
                        .addGap(18, 18, 18)
                        .addComponent(idDisfraz)
                        .addGap(18, 18, 18)
                        .addComponent(idUser, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(statusjComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(idsUsuarios1)
                            .addComponent(idsUsuarios))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(returnButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.PREFERRED_SIZE, 428, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        try {
            String idText = idUser.getText();
            int userId = -1;
            java.util.regex.Matcher m = java.util.regex.Pattern.compile("\\[(\\d+)\\]").matcher(idText);
            if (m.find()) {
                userId = Integer.parseInt(m.group(1));
            } else {
                throw new Exception("No se pudo extraer el ID del usuario.");
            }

            List<ItemDisfraz> seleccionados = listaDisfracesjList1.getSelectedValuesList();
            String estadoNuevo = (String) statusjComboBox1.getSelectedItem();
            DAOUserCostumesImpl daoUC = new DAOUserCostumesImpl();

            List<UserCostume> actuales = daoUC.listarPorUsuario(userId);

            if (actuales.size() > 0 && seleccionados.isEmpty()) {
                for (UserCostume uc : actuales) {
                    if (uc.getStatus().equalsIgnoreCase("Reservado")
                            || uc.getStatus().equalsIgnoreCase("Asignado")
                            || uc.getStatus().equalsIgnoreCase("Pagado")) {
                        daoUC.devolverDisponible(uc.getDsfrId(), uc.getQty());
                    }
                }
            }

            for (UserCostume uc : actuales) {
                boolean sigueSeleccionado = seleccionados.stream().anyMatch(d -> d.id == uc.getDsfrId());

                if (sigueSeleccionado && estadoNuevo.equalsIgnoreCase("Devuelto")) {
                    if (uc.getStatus().equalsIgnoreCase("Reservado")
                            || uc.getStatus().equalsIgnoreCase("Asignado")
                            || uc.getStatus().equalsIgnoreCase("Pagado")) {
                        daoUC.devolverDisponible(uc.getDsfrId(), uc.getQty());
                    }
                }
            }

            for (UserCostume uc : actuales) {
                daoUC.eliminarAsignacion(userId, uc.getDsfrId(), false);
            }

            for (ItemDisfraz disfraz : seleccionados) {
                UserCostume uc = new UserCostume();
                uc.setUserId(userId);
                uc.setDsfrId(disfraz.id);
                uc.setQty(1);
                uc.setStatus(estadoNuevo);
                daoUC.registrar(uc);
            }

            JOptionPane.showMessageDialog(this, "Datos guardados correctamente.", "ÉXITO", JOptionPane.INFORMATION_MESSAGE);
            Dashboard.ShowJPanel(new com.mycompany.views.Usersview());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error gestionando disfraces: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }//GEN-LAST:event_saveButtonActionPerformed

    private void returnButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnButton1ActionPerformed
        Dashboard.ShowJPanel(new com.mycompany.views.Usersview());
    }//GEN-LAST:event_returnButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
    private javax.swing.JLabel idDisfraz;
    private javax.swing.JTextField idUser;
    private javax.swing.JLabel idsUsuarios;
    private javax.swing.JLabel idsUsuarios1;
    private javax.swing.JLabel image;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JList<ItemDisfraz> listaDisfracesjList1;
    private javax.swing.JButton returnButton1;
    private javax.swing.JButton saveButton;
    private javax.swing.JComboBox<String> statusjComboBox1;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
