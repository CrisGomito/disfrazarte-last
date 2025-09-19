/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.views;

import com.mycompany.dsfr.DAOEventoAsignacionesImpl;
import com.mycompany.dsfr.DAOEventosImpl;
import com.mycompany.dsfr.Dashboard;
import com.mycompany.interfaces.DAOEventos;
import com.mycompany.models.Evento;
import com.mycompany.models.EventoItem;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.mycompany.models.Disfraces;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author cristian
 */
public class UpEvent extends javax.swing.JPanel {

    /**
     * Creates new form UpEvent
     */

    boolean fromEventDetails = false;
    boolean fromEventInfo = false;
    boolean fromCalendar = false;

// solo si se viene desde Calendar
    Evento eventOrigen;
    Date fechaOrigen;

    boolean isEdition = false;
    Evento eventEdition;
    private Date fechaSeleccionada;

    /**
     * @param evento Evento existente y puede ser null si es nuevo
     * @param fecha Fecha seleccionada para nuevos eventos desde Calendar
     * @param origen EventDetails, EventInfo o Calendar
     */
    public UpEvent(Evento evento, Date fecha, String origen) {
        initComponents();

        isEdition = evento != null;
        eventEdition = evento;

        if (isEdition) {
            fechaSeleccionada = Date.from(eventEdition.getFecha().atStartOfDay(ZoneId.systemDefault()).toInstant());
        } else if (fecha != null) {
            fechaSeleccionada = fecha;
        } else {
            fechaSeleccionada = new Date();
        }

        fromEventDetails = "EventDetails".equals(origen);
        fromEventInfo = "EventInfo".equals(origen);
        fromCalendar = "Calendar".equals(origen);

        InitStyles();
        cargarDisfracesDisponibles();
    }

    private void InitStyles() {
        title.putClientProperty("FlatLaf.styleClass", "h1");
        title.setForeground(Color.black);
        tituloTxt.putClientProperty("JTextField.placeholderText", "Ingrese el título del evento");
        detallesjTextArea1.putClientProperty("JTextField.placeholderText", "Ingrese los detalles del evento");
        ubicacionTxt.putClientProperty("JTextField.placeholderText", "Ingrese la ubicación del evento");
        notesjTextArea1.putClientProperty("JTextField.placeholderText", "Notas adicionales");

        fechaTxt.setEditable(false);
        fechaTxt.setText(fechaSeleccionada.toString());

        if (isEdition && eventEdition != null) {
            title.setText("Editar Evento");
            button.setText("Modificar Evento");

            tituloTxt.setText(eventEdition.getTitulo());
            detallesjTextArea1.setText(eventEdition.getDetalles());
            ubicacionTxt.setText(eventEdition.getLocation());
            notesjTextArea1.setText(eventEdition.getNotes());
        }

        statusjComboBox1.setModel(new DefaultComboBoxModel<>(new String[]{
            "Programado", "Cancelado", "Finalizado"
        }));

        if (isEdition && eventEdition != null) {
            statusjComboBox1.setSelectedItem(eventEdition.getEstado());
            statusjComboBox1.setFocusable(true);
            statusjComboBox1.setOpaque(true);
        } else {
            if (isEdition && eventEdition != null) {
                statusjComboBox1.setSelectedItem(eventEdition.getEstado());
                statusjComboBox1.setEnabled(true);
            } else {
                statusjComboBox1.setSelectedItem("Programado");
                statusjComboBox1.setEnabled(true);

                statusjComboBox1.addActionListener(e -> {
                    String seleccionado = (String) statusjComboBox1.getSelectedItem();
                    if (!"Programado".equals(seleccionado)) {
                        JOptionPane.showMessageDialog(this,
                                "El evento debe iniciar como Programado obligatoriamente.",
                                "AVISO", JOptionPane.WARNING_MESSAGE);
                        statusjComboBox1.setSelectedItem("Programado");
                    }
                });
            }
        }
    }

    private void cargarDisfracesDisponibles() {
        try {
            com.mycompany.interfaces.DAODisfraces daoD = new com.mycompany.dsfr.DAODisfracesImpl();

            int eventoId = isEdition ? eventEdition.getId() : 0;
            List<Disfraces> lista = daoD.listarDisponiblesPorFecha(
                    fechaSeleccionada.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate(),
                    eventoId
            );

            DefaultListModel<ItemDisfraz> model = new DefaultListModel<>();
            for (com.mycompany.models.Disfraces d : lista) {
                model.addElement(new ItemDisfraz(
                        d.getId(),
                        d.getTitle(),
                        d.getTematica(),
                        d.getEstado(),
                        d.getStock(),
                        d.getAvailable()
                ));
            }

            listaDisfraces.setModel(model);
            listaDisfraces.setCellRenderer(new CheckBoxListRenderer());
            listaDisfraces.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            if (isEdition && eventEdition != null) {
                com.mycompany.interfaces.DAOEventoItems daoItems = new com.mycompany.dsfr.DAOEventoItemsImpl();
                List<EventoItem> itemsAsignados = daoItems.listarPorEvento(eventEdition.getId());

                java.util.List<Integer> indicesList = new java.util.ArrayList<>();
                for (int i = 0; i < model.size(); i++) {
                    ItemDisfraz item = model.get(i);
                    for (EventoItem ei : itemsAsignados) {
                        if (ei.getDisfrazId() == item.id) {
                            indicesList.add(i);
                            break;
                        }
                    }
                }

                int[] indicesSeleccionados = indicesList.stream().mapToInt(Integer::intValue).toArray();
                listaDisfraces.setSelectedIndices(indicesSeleccionados);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cargando disfraces: " + ex.getMessage());
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
            return nombre + " | " + tematica + " | " + estado + " | stock:" + stock + " disp:" + disponibles;
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

    private Color generarColorAleatorio() {
        Random rnd = new Random();
        return new Color(rnd.nextInt(156) + 100, rnd.nextInt(156) + 100, rnd.nextInt(156) + 100);
    }

    private String colorToHex(Color color) {
        return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
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
        dsfrdisp = new javax.swing.JLabel();
        nameLbl = new javax.swing.JLabel();
        notas = new javax.swing.JLabel();
        title = new javax.swing.JLabel();
        button = new javax.swing.JButton();
        ubicacionTxt = new javax.swing.JTextField();
        domLbl = new javax.swing.JLabel();
        jSeparator = new javax.swing.JSeparator();
        fechaTxt = new javax.swing.JTextField();
        apMLbl = new javax.swing.JLabel();
        apPLbl = new javax.swing.JLabel();
        tituloTxt = new javax.swing.JTextField();
        listaDisfracesScroll = new javax.swing.JScrollPane();
        listaDisfraces = new javax.swing.JList<>();
        cancelbutton1 = new javax.swing.JButton();
        statusLbl1 = new javax.swing.JLabel();
        statusjComboBox1 = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        detallesjTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        notesjTextArea1 = new javax.swing.JTextArea();

        bg.setBackground(new java.awt.Color(255, 255, 255));

        dsfrdisp.setText("Asignar Disfraces *");

        nameLbl.setText("Título del Evento *");

        notas.setText("Notas Adicionales");

        title.setText("Crear nuevo Evento");

        button.setBackground(new java.awt.Color(18, 90, 173));
        button.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        button.setForeground(new java.awt.Color(255, 255, 255));
        button.setText("Crear Evento");
        button.setBorderPainted(false);
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonActionPerformed(evt);
            }
        });

        ubicacionTxt.setToolTipText("");

        domLbl.setText("Ubicacion *");

        jSeparator.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator.setPreferredSize(new java.awt.Dimension(200, 10));

        apMLbl.setText("Fecha:");

        apPLbl.setText("Detalles del Evento *");

        tituloTxt.setActionCommand("<Not Set>");
        tituloTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tituloTxtActionPerformed(evt);
            }
        });

        listaDisfracesScroll.setBorder(null);

        listaDisfracesScroll.setViewportView(listaDisfraces);

        cancelbutton1.setBackground(new java.awt.Color(18, 90, 173));
        cancelbutton1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cancelbutton1.setForeground(new java.awt.Color(255, 255, 255));
        cancelbutton1.setText("Cancelar");
        cancelbutton1.setBorderPainted(false);
        cancelbutton1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cancelbutton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelbutton1ActionPerformed(evt);
            }
        });

        statusLbl1.setText("Estado:");

        statusjComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        detallesjTextArea1.setColumns(20);
        detallesjTextArea1.setRows(5);
        detallesjTextArea1.setLineWrap(true);
        detallesjTextArea1.setWrapStyleWord(true);
        jScrollPane1.setViewportView(detallesjTextArea1);

        notesjTextArea1.setColumns(20);
        notesjTextArea1.setRows(5);
        notesjTextArea1.setLineWrap(true);
        notesjTextArea1.setWrapStyleWord(true);
        jScrollPane2.setViewportView(notesjTextArea1);

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(71, 71, 71))
            .addGroup(bgLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addComponent(cancelbutton1, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                        .addGap(227, 227, 227))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameLbl, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tituloTxt, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, bgLayout.createSequentialGroup()
                                .addComponent(apPLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(106, 106, 106))
                            .addComponent(notas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(46, 46, 46)))
                .addComponent(jSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bgLayout.createSequentialGroup()
                                .addComponent(domLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(150, 150, 150))
                            .addComponent(listaDisfracesScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(apMLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(fechaTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dsfrdisp, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE))
                                .addGap(24, 24, 24)
                                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(statusLbl1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(statusjComboBox1, 0, 87, Short.MAX_VALUE)))
                            .addComponent(ubicacionTxt))
                        .addGap(21, 21, 21))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addComponent(button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(116, 116, 116))))
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(title)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bgLayout.createSequentialGroup()
                                .addComponent(nameLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tituloTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(apPLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(notas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cancelbutton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(button))
                                .addGap(12, 12, 12))
                            .addGroup(bgLayout.createSequentialGroup()
                                .addComponent(domLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(14, 14, 14)
                                .addComponent(ubicacionTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(19, 19, 19)
                                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(apMLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(statusLbl1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(14, 14, 14)
                                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(fechaTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(statusjComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dsfrdisp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(listaDisfracesScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(60, 60, 60))))
                    .addGroup(bgLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(60, 60, 60))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonActionPerformed
        String titulo = tituloTxt.getText().trim();
        String detalles = detallesjTextArea1.getText().trim();
        String ubicacion = ubicacionTxt.getText().trim();
        String notas = notesjTextArea1.getText().trim();
        LocalDate fecha = fechaSeleccionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (titulo.isEmpty() || detalles.isEmpty() || ubicacion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe llenar los campos obligatorios.", "AVISO", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String estado = (String) statusjComboBox1.getSelectedItem();
        if (estado == null || estado.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un estado para el evento.",
                    "AVISO", JOptionPane.ERROR_MESSAGE);
            statusjComboBox1.requestFocus();
            return;
        }

        List<ItemDisfraz> seleccionados = listaDisfraces.getSelectedValuesList();
        if (seleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar al menos un disfraz para el evento.",
                    "AVISO", JOptionPane.ERROR_MESSAGE);

            if (listaDisfraces.getModel().getSize() > 0) {
                listaDisfraces.ensureIndexIsVisible(0);
            }
            listaDisfraces.requestFocusInWindow();
            return;
        }

        Evento ev = isEdition ? eventEdition : new Evento();
        ev.setTitulo(titulo);
        ev.setDetalles(detalles);
        ev.setFecha(fecha);
        ev.setLocation(ubicacion);
        ev.setNotes(notas);
        ev.setEstado(estado);

        if (!isEdition && (ev.getColor() == null || ev.getColor().isEmpty())) {
            Color colorAleatorio = generarColorAleatorio();
            String colorHex = colorToHex(colorAleatorio);
            ev.setColor(colorHex);
        }

        try {
            DAOEventos dao = new DAOEventosImpl();
            if (!isEdition) {
                dao.insertar(ev);
            } else {
                dao.modificar(ev);
            }

            com.mycompany.interfaces.DAOEventoItems daoItems = new com.mycompany.dsfr.DAOEventoItemsImpl();

            DAOEventoAsignacionesImpl daoAsignaciones = new DAOEventoAsignacionesImpl();

            Set<Integer> nuevosIds = seleccionados.stream()
                    .map(d -> d.id)
                    .collect(Collectors.toSet());

            List<EventoItem> actuales = daoItems.listarPorEvento(ev.getId());
            Set<Integer> actualesIds = actuales.stream()
                    .map(EventoItem::getDisfrazId)
                    .collect(Collectors.toSet());

            for (EventoItem existente : actuales) {
                if (!nuevosIds.contains(existente.getDisfrazId())) {

                    daoAsignaciones.eliminarPorEventoYDisfraz(ev.getId(), existente.getDisfrazId());

                    daoItems.eliminar(existente.getId());
                }
            }
            //insertar nuevos disfraces que no existían
            for (ItemDisfraz nuevo : seleccionados) {
                if (!actualesIds.contains(nuevo.id)) {
                    EventoItem item = new EventoItem();
                    item.setEventoId(ev.getId());
                    item.setDisfrazId(nuevo.id);
                    item.setQtyReserved(0);
                    item.setQtyAssigned(0);
                    daoItems.insertar(item);
                }
            }

            JOptionPane.showMessageDialog(this,
                    "Evento " + (isEdition ? "modificado" : "registrado") + " exitosamente.",
                    "AVISO", JOptionPane.INFORMATION_MESSAGE);

            if (fromEventDetails && isEdition) {
                Dashboard.ShowJPanel(new EventDetails(ev));
            } else if (fromEventInfo) {
                Dashboard.ShowJPanel(new EventInfo(fechaSeleccionada));
            } else if (fromCalendar) {
                if (!isEdition) {

                    Dashboard.ShowJPanel(new EventInfo(fechaSeleccionada));
                } else {
                    Dashboard.ShowJPanel(new Calendar());
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error guardando evento: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_buttonActionPerformed

    private void tituloTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tituloTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tituloTxtActionPerformed

    private void cancelbutton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelbutton1ActionPerformed
        try {
            if (fromEventDetails && eventEdition != null) {
                Dashboard.ShowJPanel(new EventDetails(eventEdition));
            } else if (fromEventInfo) {
                Dashboard.ShowJPanel(new EventInfo(fechaSeleccionada));
            } else if (fromCalendar && fechaSeleccionada != null) {
                Dashboard.ShowJPanel(new Calendar());
            } else {
                Dashboard.ShowJPanel(new Calendar());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al regresar: " + e.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_cancelbutton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel apMLbl;
    private javax.swing.JLabel apPLbl;
    private javax.swing.JPanel bg;
    private javax.swing.JButton button;
    private javax.swing.JButton cancelbutton1;
    private javax.swing.JTextArea detallesjTextArea1;
    private javax.swing.JLabel domLbl;
    private javax.swing.JLabel dsfrdisp;
    private javax.swing.JTextField fechaTxt;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator;
    private javax.swing.JList<ItemDisfraz> listaDisfraces;
    private javax.swing.JScrollPane listaDisfracesScroll;
    private javax.swing.JLabel nameLbl;
    private javax.swing.JLabel notas;
    private javax.swing.JTextArea notesjTextArea1;
    private javax.swing.JLabel statusLbl1;
    private javax.swing.JComboBox<String> statusjComboBox1;
    private javax.swing.JLabel title;
    private javax.swing.JTextField tituloTxt;
    private javax.swing.JTextField ubicacionTxt;
    // End of variables declaration//GEN-END:variables
}
