
package com.mycompany.views;

import com.mycompany.dsfr.DAODisfracesImpl;
import com.mycompany.dsfr.DAOEventosImpl;
import com.mycompany.dsfr.DAOUsersImpl;
import com.mycompany.dsfr.Dashboard;
import com.mycompany.models.Evento;
import com.mycompany.models.Disfraces;

import javax.swing.*;
import java.awt.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.ListCellRenderer;
import javax.swing.JOptionPane;

/**
 *
 * @author cristian
 */
public class EventDetails extends javax.swing.JPanel {

    private Evento eventoDetalle;

    public EventDetails(Evento evento) {
        this.eventoDetalle = evento;
        initComponents();
        InitStyles();
        cargarDetallesEvento();
        cargarDisfracesAsignados();
        cargarUsuariosAsignados();
    }

    private void cargarDetallesEvento() {
        if (eventoDetalle != null) {
            tittleTxt.setText(eventoDetalle.getTitulo());
            tittleTxt.setEditable(false);

            detailsjTextArea1.setText(eventoDetalle.getDetalles());
            detailsjTextArea1.setEditable(false);

            dateTxt.setText(eventoDetalle.getFecha().toString());
            dateTxt.setEditable(false);

            locationTxt.setText(eventoDetalle.getLocation());
            locationTxt.setEditable(false);

            notesjTextArea1.setText(
                    (eventoDetalle.getNotes() == null || eventoDetalle.getNotes().isEmpty())
                    ? "Sin Notas Adicionales"
                    : eventoDetalle.getNotes()
            );
            notesjTextArea1.setEditable(false);

            statusTxt1.setText(
                    (eventoDetalle.getEstado() == null || eventoDetalle.getEstado().isEmpty())
                    ? "Sin Estado"
                    : eventoDetalle.getEstado()
            );
            statusTxt1.setEditable(false);
        }
    }

    private void InitStyles() {
        title.putClientProperty("FlatLaf.styleClass", "h1");
        title.setForeground(Color.black);
    }

    /*
    CLASES INTERNAS
     */
    public static class ItemUser {

        final int id;
        final String nombreCompleto;
        final String email;

        public ItemUser(int id, String nombreCompleto, String email) {
            this.id = id;
            this.nombreCompleto = nombreCompleto;
            this.email = email;
        }

        @Override
        public String toString() {
            if ("No Asignado".equals(nombreCompleto)) {
                return nombreCompleto;
            }
            return "[" + id + "] " + nombreCompleto + " | " + (email != null ? email : "-");
        }
    }

    public static class ItemDisfraz {

        final int id;
        final String nombre;
        final String tematica;
        final String estado;
        final int stock;
        final int disponibles;

        public ItemDisfraz(int id, String nombre, String tematica, String estado, int stock, int disponibles) {
            this.id = id;
            this.nombre = nombre;
            this.tematica = tematica;
            this.estado = estado;
            this.stock = stock;
            this.disponibles = disponibles;
        }

        @Override
        public String toString() {
            return nombre + " | " + tematica + " | " + estado + " | stock:" + stock + " disp:" + disponibles;
        }
    }

    public static class CheckBoxListRenderer<T> extends JCheckBox implements ListCellRenderer<T> {

        @Override
        public java.awt.Component getListCellRendererComponent(javax.swing.JList<? extends T> list, T value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(value.toString());
            setSelected(isSelected);
            setBackground(isSelected ? new Color(18, 90, 173) : Color.WHITE);
            setForeground(isSelected ? Color.WHITE : Color.BLACK);
            return this;
        }
    }

    /*
    MÉTODOS DE CARGA
     */
    private void cargarDisfracesAsignados() {
        DefaultListModel<ItemDisfraz> model = new DefaultListModel<>();
        try {
            DAOEventosImpl daoEventos = new DAOEventosImpl();
            eventoDetalle = daoEventos.obtenerDetallesPorId(eventoDetalle.getId());

            DAODisfracesImpl daoDisfraces = new DAODisfracesImpl();
            List<Disfraces> todosDisfraces = daoDisfraces.listar("");

            for (String nombreAsignado : eventoDetalle.getDisfracesAsignados()) {
                for (Disfraces d : todosDisfraces) {
                    if (d.getTitle().equals(nombreAsignado)) {
                        model.addElement(new ItemDisfraz(
                                d.getId(),
                                "[" + d.getId() + "] " + d.getTitle(),
                                d.getTematica(),
                                d.getEstado(),
                                d.getStock(),
                                d.getAvailable()
                        ));
                        break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error cargando disfraces: " + e.getMessage());
        }
        dsfrsAssignjList1.setModel(model);
        dsfrsAssignjList1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    private void cargarUsuariosAsignados() {
        DefaultListModel<ItemUser> model = new DefaultListModel<>();
        try {
            DAOUsersImpl daoUsers = new DAOUsersImpl();
            List<com.mycompany.models.Users> todosUsuarios = daoUsers.listar("");

            List<String> usuariosAsignados = eventoDetalle.getUsuariosAsignados();

            if (usuariosAsignados == null || usuariosAsignados.isEmpty()) {
                model.addElement(new ItemUser(-1, "No Asignado", "-"));
            } else {
                for (String nombreAsignado : usuariosAsignados) {
                    todosUsuarios.stream()
                            .filter(u -> (u.getName() + " " + u.getLast_name_p() + " " + u.getLast_name_m()).equals(nombreAsignado))
                            .findFirst()
                            .ifPresent(u -> model.addElement(new ItemUser(
                            u.getId(),
                            nombreAsignado,
                            u.getEmail() != null ? u.getEmail() : "-"
                    )));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error cargando usuarios asignados: " + e.getMessage());
        }

        usersAssignjList1.setModel(model);
        usersAssignjList1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public static class CheckBoxUserRenderer extends JCheckBox implements ListCellRenderer<ItemUser> {

        @Override
        public Component getListCellRendererComponent(JList<? extends ItemUser> list, ItemUser value,
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
        title = new javax.swing.JLabel();
        assignButton = new javax.swing.JButton();
        returnButton1 = new javax.swing.JButton();
        editEventButton = new javax.swing.JButton();
        tittleLbl = new javax.swing.JLabel();
        tittleTxt = new javax.swing.JTextField();
        detailsLbl = new javax.swing.JLabel();
        dsfrAssignLbl = new javax.swing.JLabel();
        locationTxt = new javax.swing.JTextField();
        locationLbl = new javax.swing.JLabel();
        dateTxt = new javax.swing.JTextField();
        dateLbl = new javax.swing.JLabel();
        notesLbl = new javax.swing.JLabel();
        editCostumesButton = new javax.swing.JButton();
        usersAssignLbl = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        usersAssignjList1 = new javax.swing.JList<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        dsfrsAssignjList1 = new javax.swing.JList<>();
        jSeparator1 = new javax.swing.JSeparator();
        statusLbl1 = new javax.swing.JLabel();
        statusTxt1 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        detailsjTextArea1 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        notesjTextArea1 = new javax.swing.JTextArea();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        jTextField1 = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(752, 427));

        bg.setBackground(new java.awt.Color(255, 255, 255));
        bg.setForeground(new java.awt.Color(239, 239, 239));
        bg.setPreferredSize(new java.awt.Dimension(752, 427));

        title.setText("Detalles del Evento");

        assignButton.setBackground(new java.awt.Color(18, 90, 173));
        assignButton.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        assignButton.setForeground(new java.awt.Color(255, 255, 255));
        assignButton.setText("Asignar");
        assignButton.setBorderPainted(false);
        assignButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        assignButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                assignButtonActionPerformed(evt);
            }
        });

        returnButton1.setBackground(new java.awt.Color(18, 90, 173));
        returnButton1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        returnButton1.setForeground(new java.awt.Color(255, 255, 255));
        returnButton1.setText("Atrás");
        returnButton1.setBorderPainted(false);
        returnButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        returnButton1.setMaximumSize(new java.awt.Dimension(90, 32));
        returnButton1.setMinimumSize(new java.awt.Dimension(90, 32));
        returnButton1.setPreferredSize(new java.awt.Dimension(90, 32));
        returnButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnButton1ActionPerformed(evt);
            }
        });

        editEventButton.setBackground(new java.awt.Color(18, 90, 173));
        editEventButton.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        editEventButton.setForeground(new java.awt.Color(255, 255, 255));
        editEventButton.setText("Editar");
        editEventButton.setBorderPainted(false);
        editEventButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        editEventButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editEventButtonActionPerformed(evt);
            }
        });

        tittleLbl.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tittleLbl.setText("Título del Evento");

        tittleTxt.setBorder(null);
        tittleTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tittleTxtActionPerformed(evt);
            }
        });

        detailsLbl.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        detailsLbl.setText("Detalles del Evento");

        dsfrAssignLbl.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        dsfrAssignLbl.setText("Disfraces Asignados");

        locationTxt.setBorder(null);
        locationTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locationTxtActionPerformed(evt);
            }
        });

        locationLbl.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        locationLbl.setText("Ubicación");

        dateTxt.setBorder(null);
        dateTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateTxtActionPerformed(evt);
            }
        });

        dateLbl.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        dateLbl.setText("Fecha");

        notesLbl.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        notesLbl.setText("Notas Adicionales");

        editCostumesButton.setBackground(new java.awt.Color(18, 90, 173));
        editCostumesButton.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        editCostumesButton.setForeground(new java.awt.Color(255, 255, 255));
        editCostumesButton.setText("Editar");
        editCostumesButton.setBorderPainted(false);
        editCostumesButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        editCostumesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editCostumesButtonActionPerformed(evt);
            }
        });

        usersAssignLbl.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        usersAssignLbl.setText("Usuarios Asignados");

        usersAssignjList1.setModel(new DefaultListModel<ItemUser>()
        );
        jScrollPane1.setViewportView(usersAssignjList1);

        dsfrsAssignjList1.setModel(new javax.swing.AbstractListModel<ItemDisfraz>() {
            List<ItemDisfraz> items = new ArrayList<>();

            @Override
            public int getSize() {
                return items.size();
            }

            @Override
            public ItemDisfraz getElementAt(int i) {
                return items.get(i);
            }
        });
        jScrollPane4.setViewportView(dsfrsAssignjList1);

        jSeparator1.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator1.setPreferredSize(new java.awt.Dimension(200, 10));

        statusLbl1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        statusLbl1.setText("Estado");

        statusTxt1.setBorder(null);
        statusTxt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusTxt1ActionPerformed(evt);
            }
        });

        jScrollPane2.setBorder(null);

        detailsjTextArea1.setColumns(20);
        detailsjTextArea1.setRows(5);
        detailsjTextArea1.setBorder(null);
        detailsjTextArea1.setLineWrap(true);
        detailsjTextArea1.setWrapStyleWord(true);
        jScrollPane2.setViewportView(detailsjTextArea1);

        jScrollPane3.setBorder(null);

        notesjTextArea1.setColumns(20);
        notesjTextArea1.setRows(5);
        notesjTextArea1.setBorder(null);
        notesjTextArea1.setLineWrap(true);
        notesjTextArea1.setWrapStyleWord(true);
        jScrollPane3.setViewportView(notesjTextArea1);

        jSeparator2.setForeground(new java.awt.Color(239, 239, 239));
        jSeparator2.setPreferredSize(new java.awt.Dimension(200, 10));

        jSeparator3.setForeground(new java.awt.Color(239, 239, 239));
        jSeparator3.setPreferredSize(new java.awt.Dimension(200, 10));

        jSeparator4.setForeground(new java.awt.Color(239, 239, 239));
        jSeparator4.setPreferredSize(new java.awt.Dimension(200, 10));

        jSeparator5.setForeground(new java.awt.Color(239, 239, 239));
        jSeparator5.setPreferredSize(new java.awt.Dimension(200, 10));

        jSeparator6.setForeground(new java.awt.Color(239, 239, 239));
        jSeparator6.setPreferredSize(new java.awt.Dimension(200, 10));

        jSeparator7.setForeground(new java.awt.Color(239, 239, 239));
        jSeparator7.setPreferredSize(new java.awt.Dimension(200, 10));

        jTextField1.setBackground(new java.awt.Color(255, 255, 255));
        jTextField1.setBorder(null);

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(392, 392, 392))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(bgLayout.createSequentialGroup()
                                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 622, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(bgLayout.createSequentialGroup()
                                        .addComponent(jSeparator7, javax.swing.GroupLayout.DEFAULT_SIZE, 603, Short.MAX_VALUE)
                                        .addGap(25, 25, 25)))
                                .addComponent(returnButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(bgLayout.createSequentialGroup()
                                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(bgLayout.createSequentialGroup()
                                        .addComponent(tittleLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(142, 142, 142)
                                        .addComponent(editEventButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(tittleTxt)
                                    .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(detailsLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jScrollPane2)
                                    .addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(locationLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(locationTxt)
                                    .addComponent(jSeparator4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(notesLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jScrollPane3))
                                .addGap(24, 24, 24)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(bgLayout.createSequentialGroup()
                                        .addComponent(dsfrAssignLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(123, 123, 123)
                                        .addComponent(editCostumesButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jScrollPane4)
                                    .addGroup(bgLayout.createSequentialGroup()
                                        .addComponent(usersAssignLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(134, 134, 134)
                                        .addComponent(assignButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jSeparator6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jSeparator5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jScrollPane1)
                                    .addGroup(bgLayout.createSequentialGroup()
                                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(dateLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(dateTxt))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(statusTxt1)
                                            .addComponent(statusLbl1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(84, 84, 84)))))
                        .addGap(17, 17, 17))))
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(title)
                .addGap(4, 4, 4)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bgLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(dsfrAssignLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(6, 6, 6))
                            .addComponent(editCostumesButton))
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bgLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(usersAssignLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(6, 6, 6))
                            .addComponent(assignButton))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(statusLbl1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dateLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(6, 6, 6)
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(statusTxt1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12))
                    .addGroup(bgLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(bgLayout.createSequentialGroup()
                                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(bgLayout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(tittleLbl, javax.swing.GroupLayout.DEFAULT_SIZE, 17, Short.MAX_VALUE)
                                        .addGap(6, 6, 6))
                                    .addComponent(editEventButton))
                                .addGap(6, 6, 6)
                                .addComponent(tittleTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(7, 7, 7)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)
                                .addComponent(detailsLbl, javax.swing.GroupLayout.DEFAULT_SIZE, 17, Short.MAX_VALUE)
                                .addGap(6, 6, 6)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
                                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(locationLbl, javax.swing.GroupLayout.DEFAULT_SIZE, 17, Short.MAX_VALUE)
                                .addGap(6, 6, 6)
                                .addComponent(locationTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9)
                                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(notesLbl, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(returnButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, 769, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void assignButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_assignButtonActionPerformed
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Asignar Usuarios", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLocationRelativeTo(this);

        //Construcción del modelo
        DefaultListModel<ItemUser> listModel = new DefaultListModel<>();
        try {
            DAOUsersImpl daoUsers = new DAOUsersImpl();
            List<com.mycompany.models.Users> usuariosModel = daoUsers.listar("");

            for (com.mycompany.models.Users u : usuariosModel) {
                String nombreCompleto = u.getName() + " " + u.getLast_name_p() + " " + u.getLast_name_m();
                String email = u.getEmail() != null ? u.getEmail() : "";
                listModel.addElement(new ItemUser(u.getId(), nombreCompleto, email));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error cargando lista de usuarios: " + e.getMessage());
        }

        JList<ItemUser> dialogList = new JList<>(listModel);
        dialogList.setCellRenderer(new CheckBoxUserRenderer());
        dialogList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        List<String> usuariosAsignados = eventoDetalle.getUsuariosAsignados();
        if (usuariosAsignados != null && !usuariosAsignados.isEmpty()) {
            List<Integer> indicesSeleccionados = new ArrayList<>();
            for (int i = 0; i < listModel.size(); i++) {
                ItemUser u = listModel.get(i);
                if (usuariosAsignados.contains(u.nombreCompleto)) {
                    indicesSeleccionados.add(i);
                }
            }
            if (!indicesSeleccionados.isEmpty()) {
                int[] sel = indicesSeleccionados.stream().mapToInt(Integer::intValue).toArray();
                dialogList.setSelectedIndices(sel);
            }
        }

        //Construcción del UI
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        JLabel lblMensaje = new JLabel("Seleccione los Usuarios a Asignar:");
        lblMensaje.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(lblMensaje, BorderLayout.NORTH);

        // Definición del tamaño
        JScrollPane scrollPane = new JScrollPane(dialogList);
        scrollPane.setPreferredSize(new Dimension(500, 320));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAsignar = new JButton("Asignar");
        JButton btnCancelar = new JButton("Cancelar");

        btnAsignar.setBackground(new Color(18, 90, 173));
        btnAsignar.setForeground(Color.WHITE);
        btnAsignar.setFocusPainted(false);
        btnAsignar.setOpaque(true);
        btnAsignar.setBorderPainted(false);

        btnCancelar.setBackground(new Color(200, 50, 50));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setOpaque(true);
        btnCancelar.setBorderPainted(false);

        btnAsignar.addActionListener(e -> {
            try {
                List<ItemUser> seleccionados = dialogList.getSelectedValuesList();
                List<Integer> userIds = new ArrayList<>();
                for (ItemUser iu : seleccionados) {
                    if (iu.id > 0) {
                        userIds.add(iu.id);
                    }
                }

                DAOEventosImpl daoEventos = new DAOEventosImpl();
                daoEventos.eliminarUsuariosAsignados(eventoDetalle.getId());
                daoEventos.asignarUsuariosAEvento(eventoDetalle.getId(), userIds);

                eventoDetalle = daoEventos.obtenerDetallesPorId(eventoDetalle.getId());
                cargarUsuariosAsignados();

                JOptionPane.showMessageDialog(dialog, "Usuarios asignados correctamente.");
                dialog.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Error asignando usuarios: " + ex.getMessage());
            }
        });

        btnCancelar.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(btnCancelar);
        buttonsPanel.add(btnAsignar);

        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        dialog.setContentPane(mainPanel);

        dialog.pack();

        //Tamaño mínimo
        dialog.setResizable(true);
        dialog.setMinimumSize(new Dimension(300, 300));

        dialog.setLocationRelativeTo(this);

        dialog.setVisible(true);
    }//GEN-LAST:event_assignButtonActionPerformed

    private void returnButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnButton1ActionPerformed
        try {
            if (eventoDetalle != null) {
                Dashboard.ShowJPanel(new EventInfo(
                        java.util.Date.from(
                                eventoDetalle.getFecha()
                                        .atStartOfDay(java.time.ZoneId.systemDefault())
                                        .toInstant()
                        )
                ));
            } else {
                Dashboard.ShowJPanel(new Calendar());
            }
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error al regresar: " + e.getMessage(),
                    "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_returnButton1ActionPerformed

    private void editEventButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editEventButtonActionPerformed
        if (eventoDetalle != null) {
            UpEvent panel = new UpEvent(eventoDetalle, null, "EventDetails");
            Dashboard.ShowJPanel(panel);
        }
    }//GEN-LAST:event_editEventButtonActionPerformed

    private void tittleTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tittleTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tittleTxtActionPerformed

    private void locationTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_locationTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_locationTxtActionPerformed

    private void dateTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dateTxtActionPerformed

    private void editCostumesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editCostumesButtonActionPerformed
        if (dsfrsAssignjList1.getSelectedValue() != null) {
            try {
                ItemDisfraz itemSel = dsfrsAssignjList1.getSelectedValue();

                DAODisfracesImpl dao = new DAODisfracesImpl();
                com.mycompany.models.Disfraces dsfr = dao.getDsfrById(itemSel.id);

                if (dsfr != null) {

                    Dashboard.ShowJPanel(new UpDisfraces(dsfr, eventoDetalle));
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se encontró el disfraz seleccionado.",
                            "AVISO", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error cargando disfraz: " + e.getMessage(),
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Debes seleccionar un disfraz de la lista para editar.\n",
                    "AVISO", JOptionPane.WARNING_MESSAGE);
            dsfrsAssignjList1.requestFocusInWindow();
        }
    }//GEN-LAST:event_editCostumesButtonActionPerformed

    private void statusTxt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusTxt1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_statusTxt1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton assignButton;
    private javax.swing.JPanel bg;
    private javax.swing.JLabel dateLbl;
    private javax.swing.JTextField dateTxt;
    private javax.swing.JLabel detailsLbl;
    private javax.swing.JTextArea detailsjTextArea1;
    private javax.swing.JLabel dsfrAssignLbl;
    private javax.swing.JList<ItemDisfraz> dsfrsAssignjList1;
    private javax.swing.JButton editCostumesButton;
    private javax.swing.JButton editEventButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel locationLbl;
    private javax.swing.JTextField locationTxt;
    private javax.swing.JLabel notesLbl;
    private javax.swing.JTextArea notesjTextArea1;
    private javax.swing.JButton returnButton1;
    private javax.swing.JLabel statusLbl1;
    private javax.swing.JTextField statusTxt1;
    private javax.swing.JLabel title;
    private javax.swing.JLabel tittleLbl;
    private javax.swing.JTextField tittleTxt;
    private javax.swing.JLabel usersAssignLbl;
    private javax.swing.JList<ItemUser> usersAssignjList1;
    // End of variables declaration//GEN-END:variables
}
