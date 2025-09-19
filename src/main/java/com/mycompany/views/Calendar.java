
package com.mycompany.views;

import com.mycompany.dsfr.DAOEventosImpl;
import com.mycompany.interfaces.DAOEventos;
import com.mycompany.models.Evento;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 *
 * @author cristian
 */
public class Calendar extends javax.swing.JPanel {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Set<String> fechasConEventos = new HashSet<>();
    private Map<String, Color> coloresPorFecha = new HashMap<>();

    public Calendar() {
        initComponents();
        InitStyles();
        cargarFechasEventos();
        attachDayButtonListeners();
        colorearFechasConEventos();

        jCalendar1.addPropertyChangeListener("calendar", evt -> {
            SwingUtilities.invokeLater(() -> {
                attachDayButtonListeners();
                colorearFechasConEventos();
            });
        });
    }

    private void InitStyles() {
        title.putClientProperty("FlatLaf.styleClass", "h1");
        title.setForeground(Color.black);
    }

    private void cargarFechasEventos() {
        try {
            DAOEventosImpl dao = new DAOEventosImpl();
            fechasConEventos.clear();

            List<Evento> eventos = dao.listar();

            for (Evento e : eventos) {
                String fechaStr = sdf.format(java.sql.Date.valueOf(e.getFecha()));
                fechasConEventos.add(fechaStr);

                if (!coloresPorFecha.containsKey(fechaStr)) {
                    String colorHex = dao.obtenerColorPorFecha(e.getFecha());
                    if (colorHex != null && !colorHex.isEmpty()) {
                        coloresPorFecha.put(fechaStr, Color.decode(colorHex));
                    } else {
                        coloresPorFecha.put(fechaStr, generarColorAleatorio());
                        dao.insertarColorFecha(e.getFecha(), coloresPorFecha.get(fechaStr));
                    }
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar fechas de eventos.\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Color generarColorAleatorio() {
        Random rnd = new Random();
        return new Color(rnd.nextInt(156) + 100, rnd.nextInt(156) + 100, rnd.nextInt(156) + 100);
    }

    private void colorearFechasConEventos() {
        Date fechaHoy = new Date();
        String fechaHoyStr = sdf.format(fechaHoy);

        Container dayChooser = jCalendar1.getDayChooser();
        for (Component comp : dayChooser.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel weekPanel = (JPanel) comp;
                for (Component btnComp : weekPanel.getComponents()) {
                    if (btnComp instanceof JButton) {
                        JButton dayButton = (JButton) btnComp;
                        String text = dayButton.getText();
                        if (text != null && text.matches("\\d+")) {
                            int dia = Integer.parseInt(text);
                            java.util.Calendar cal = java.util.Calendar.getInstance();
                            cal.setTime(jCalendar1.getDate());
                            cal.set(java.util.Calendar.DAY_OF_MONTH, dia);
                            String fechaStr = sdf.format(cal.getTime());

                            if (fechasConEventos.contains(fechaStr)) {
                                dayButton.setBackground(coloresPorFecha.get(fechaStr));
                                dayButton.setForeground(Color.BLACK);
                            } else {
                                dayButton.setBackground(null);
                                dayButton.setForeground(Color.BLACK);
                            }
                            if (fechaStr.equals(fechaHoyStr)) {
                                dayButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                            } else {
                                dayButton.setBorder(UIManager.getBorder("Button.border"));
                            }
                        }
                    }
                }
            }
        }
    }

    private void attachDayButtonListeners() {
        Container dayChooser = jCalendar1.getDayChooser();
        addMouseListenersRecursive(dayChooser);
    }

    private void addMouseListenersRecursive(Component comp) {
        if (comp instanceof JButton) {
            JButton btn = (JButton) comp;
            if (!Boolean.TRUE.equals(btn.getClientProperty("calendarMouseListener"))) {
                btn.putClientProperty("calendarMouseListener", true);
                btn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            Date fechaSeleccionada = jCalendar1.getDate();
                            mostrarPanelSegunFecha(fechaSeleccionada);
                        }
                    }
                });
            }
        } else if (comp instanceof Container) {
            for (Component c : ((Container) comp).getComponents()) {
                addMouseListenersRecursive(c);
            }
        }
    }

    private void mostrarPanelSegunFecha(Date fechaSeleccionada) {
        try {
            LocalDate fecha = fechaSeleccionada.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            DAOEventos dao = new DAOEventosImpl();
            List<Evento> eventos = dao.listarPorFecha(fecha);

            JPanel panel;
            if (eventos == null || eventos.isEmpty()) {
                panel = new UpEvent(null, fechaSeleccionada, "Calendar");
            } else {
                panel = new EventInfo(fechaSeleccionada);
            }

            com.mycompany.dsfr.Dashboard.ShowJPanel(panel);
            revalidate();
            repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar eventos de la fecha.\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
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
        jCalendar1 = new com.toedter.calendar.JCalendar();
        title = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(752, 427));

        bg.setBackground(new java.awt.Color(255, 255, 255));
        bg.setPreferredSize(new java.awt.Dimension(752, 427));

        title.setText("Calendario de Eventos");

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(22, 22, 22))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jCalendar1, javax.swing.GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE)
                .addGap(14, 14, 14))
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(title)
                .addGap(18, 18, 18)
                .addComponent(jCalendar1, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                .addGap(27, 27, 27))
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
    private com.toedter.calendar.JCalendar jCalendar1;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
