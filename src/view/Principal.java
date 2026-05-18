package view;
 
import dao.*;
import model.*;
 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
 

/**
 * Ventana principal (Dashboard) de la Pizzería Jero Pepperoni.
 * Contiene:
 *   - JMenuBar con opciones de sesión y tema
 *   - Panel lateral de navegación con módulos
 *   - JTable central que cambia según el módulo activo
 *   - Panel de operaciones adaptativo (Nuevo / Guardar / Eliminar)
 */


public class Principal extends JFrame {
 
    // Usuario en sesión
    private final Usuario usuarioActual;

    // DAOs
    private final UsuarioDAOImpl usuarioDAO  = new UsuarioDAOImpl();
    private final ClienteDAOImpl clienteDAO  = new ClienteDAOImpl();
    private final EmpleadoDAOImpl empleadoDAO = new EmpleadoDAOImpl();
    private final ProductoDAOImpl productoDAO = new ProductoDAOImpl();
    private final PedidoDAOImpl pedidoDAO   = new PedidoDAOImpl();
 
    // Componentes principales 
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JPanel panelOps;
    private JLabel lblModulo;
    private String moduloActivo = "clientes";
 
    // Campos del panel de operaciones
    private JTextField  txtCampo1, txtCampo2, txtCampo3, txtCampo4, txtCampo5, txtCampo6;
    private JLabel      lblCampo1, lblCampo2, lblCampo3, lblCampo4, lblCampo5, lblCampo6;
    private JComboBox<String> cmbCampo1;
    private int         idSeleccionado = -1;
 
    // Colores del tema
    

    private static Color getFondo()   { return temaOscuro ? new Color(30,30,30)   : new Color(240,240,240); }
    private static Color getPanel()   { return temaOscuro ? new Color(45,45,45)   : new Color(220,220,220); }
    private static Color getLateral() { return temaOscuro ? new Color(25,25,25)   : new Color(200,200,200); }
    private static Color getTexto()   { return temaOscuro ? Color.WHITE            : Color.BLACK; }
    private static final Color COLOR_ACENTO   = new Color(255, 100, 0);
    
    
    private static boolean temaOscuro = true; // tema por defecto

    // Constructor
    public Principal(Usuario usuario) {
    this.usuarioActual = usuario;
    try {
        if (temaOscuro) {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } else {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }
    } catch (Exception e) {
        System.err.println("Error aplicando tema: " + e.getMessage());
    }
    initUI();
    cargarModulo("clientes");
}
 
    // Construcción de la interfaz
    private void initUI() {
        setTitle("Jero Pepperoni — Panel de gestión | " + usuarioActual.getNombre());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 680);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 550));
 
        // JMenuBar
        JMenuBar menuBar = new JMenuBar() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(25, 25, 25));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        menuBar.setOpaque(true);
        menuBar.setBackground(new Color(25, 25, 25));

        JMenu menuSesion = new JMenu("👤 Sesión");
        menuSesion.setForeground(getTexto());
        menuSesion.setOpaque(false);

        JMenu menuTema = new JMenu("🎨 Tema");
        menuTema.setForeground(getTexto());
        menuTema.setOpaque(false);
 
        menuSesion.getPopupMenu().setBackground(new Color(45, 45, 45));
        menuSesion.getPopupMenu().setOpaque(true); 
        menuTema.getPopupMenu().setBackground(new Color(45, 45, 45));
        menuTema.getPopupMenu().setOpaque(true);
        JMenuItem itemPassword = new JMenuItem("Cambiar contraseña");
        itemPassword.addActionListener(e -> cambiarPassword());

        JMenuItem itemCerrar = new JMenuItem("Cerrar sesión");
        itemCerrar.addActionListener(e -> cerrarSesion());
 
        menuSesion.add(itemPassword);
        menuSesion.addSeparator();
        menuSesion.add(itemCerrar);

        JMenuItem itemOscuro = new JMenuItem("Modo oscuro");
        itemOscuro.addActionListener(e -> aplicarTema(true));
        
        JMenuItem itemClaro = new JMenuItem("Modo claro");
        itemClaro.addActionListener(e -> aplicarTema(false));
        
        itemOscuro.setBackground(new Color(45, 45, 45));
        itemOscuro.setForeground(Color.WHITE);

        itemClaro.setBackground(new Color(45, 45, 45));
        itemClaro.setForeground(Color.WHITE);

        itemPassword.setBackground(new Color(45, 45, 45));
        itemPassword.setForeground(Color.WHITE);

        itemCerrar.setBackground(new Color(45, 45, 45));
        itemCerrar.setForeground(Color.WHITE);
        
        itemPassword.setOpaque(true);
        itemCerrar.setOpaque(true);
        itemOscuro.setOpaque(true);
        itemClaro.setOpaque(true);
        
        menuTema.add(itemOscuro);
        menuTema.add(itemClaro);

 
        JLabel lblUsuario = new JLabel("  " + usuarioActual.getNombre() + " (" + usuarioActual.getRol() + ")  ");
        lblUsuario.setForeground(COLOR_ACENTO);
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
 
        menuBar.add(menuSesion);
        menuBar.add(menuTema);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(lblUsuario);
        setJMenuBar(menuBar);
 
        // Layout principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(getFondo());
 
        // Panel lateral de navegación
        JPanel panelLateral = new JPanel();
        panelLateral.setLayout(new BoxLayout(panelLateral, BoxLayout.Y_AXIS));
        panelLateral.setBackground(getLateral());
        panelLateral.setPreferredSize(new Dimension(180, 0));
        panelLateral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
 
        JLabel lblMenu = new JLabel("MENÚ");
        lblMenu.setForeground(Color.GRAY);
        lblMenu.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelLateral.add(lblMenu);
        panelLateral.add(Box.createVerticalStrut(10));
 
        // Botones de navegación
        panelLateral.add(crearBotonNav("Clientes",   "clientes"));
        panelLateral.add(Box.createVerticalStrut(5));
        panelLateral.add(crearBotonNav("Empleados",  "empleados"));
        panelLateral.add(Box.createVerticalStrut(5));
        panelLateral.add(crearBotonNav("Productos",  "productos"));
        panelLateral.add(Box.createVerticalStrut(5));
        panelLateral.add(crearBotonNav("Pedidos",    "pedidos"));
        panelLateral.add(Box.createVerticalStrut(5));
        panelLateral.add(crearBotonNav("Usuarios",   "usuarios"));
        panelLateral.add(Box.createVerticalGlue());
 
        // Panel central
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(getFondo());
 
        // Cabecera del módulo activo
        lblModulo = new JLabel(" Clientes", SwingConstants.LEFT);
        lblModulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblModulo.setForeground(COLOR_ACENTO);
        lblModulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 0));
        panelCentral.add(lblModulo, BorderLayout.NORTH);
 
        // JTable
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // tabla de solo lectura
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setRowHeight(28);
        tabla.setBackground(getPanel());
        tabla.setForeground(getTexto());
        tabla.setGridColor(new Color(60, 60, 60));
        tabla.getTableHeader().setBackground(COLOR_ACENTO);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setOpaque(true);
        tabla.getTableHeader().setPreferredSize(new Dimension(0, 35));
        tabla.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setBackground(COLOR_ACENTO);
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
                lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(200, 80, 0)));
                return lbl;
            }
        });

        tabla.setSelectionBackground(new Color(255, 100, 0, 80));
        tabla.setSelectionForeground(getTexto());
 
        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBackground(getFondo());
        scrollTabla.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        panelCentral.add(scrollTabla, BorderLayout.CENTER);
        
        scrollTabla.getViewport().setBackground(new Color(45, 45, 45));

        // Panel de operaciones (derecha)
        panelOps = new JPanel();
        panelOps.setLayout(new BoxLayout(panelOps, BoxLayout.Y_AXIS));
        panelOps.setBackground(getPanel());
        panelOps.setPreferredSize(new Dimension(220, 0));
        panelOps.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
 
        // Ensamblar
        panelPrincipal.add(panelLateral, BorderLayout.WEST);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        panelPrincipal.add(panelOps, BorderLayout.EAST);
        add(panelPrincipal);
 
        // Evento selección en tabla
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                cargarFilaEnFormulario(tabla.getSelectedRow());
            }
        });
    }
 
    // Cargar módulo activo
    private void cargarModulo(String modulo) {
        this.moduloActivo = modulo;
        idSeleccionado = -1;
        panelOps.removeAll();
 
        switch (modulo) {
            case "clientes" -> cargarClientes();
            case "empleados" -> cargarEmpleados();
            case "productos" -> cargarProductos();
            case "pedidos" -> cargarPedidos();
            case "usuarios" -> cargarUsuarios();
        }
 
        panelOps.revalidate();
        panelOps.repaint();
    }
 
    // MÓDULO CLIENTES
    private void cargarClientes() {
        lblModulo.setText("Clientes");
        modeloTabla.setRowCount(0);
        modeloTabla.setColumnIdentifiers(new String[]{
            "ID", "Username", "Nombre", "Apellidos", "Email", "DNI", "Teléfono", "Dirección"
        });
 
        List<Cliente> clientes = clienteDAO.listarTodos();
        for (Cliente c : clientes) {
            modeloTabla.addRow(new Object[]{
                c.getId(), c.getUsername(), c.getNombre(), c.getApellidos(),
                c.getEmail(), c.getDni(), c.getTelefono(), c.getDireccion()
            });
        }
 
        // Panel de operaciones para clientes
        lblCampo1 = ops_label("Username:"); txtCampo1 = ops_field();
        lblCampo2 = ops_label("Nombre:"); txtCampo2 = ops_field();
        lblCampo3 = ops_label("Apellidos:"); txtCampo3 = ops_field();
        lblCampo4 = ops_label("Email:"); txtCampo4 = ops_field();
        lblCampo5 = ops_label("Teléfono:"); txtCampo5 = ops_field();
        lblCampo6 = ops_label("Dirección:"); txtCampo6 = ops_field();
 
        ops_titulo("Operaciones - Clientes");
        ops_add(lblCampo1, txtCampo1); ops_add(lblCampo2, txtCampo2);
        ops_add(lblCampo3, txtCampo3); ops_add(lblCampo4, txtCampo4);
        ops_add(lblCampo5, txtCampo5); ops_add(lblCampo6, txtCampo6);
        ops_botones();
    }
 
    // MÓDULO EMPLEADOS
    private void cargarEmpleados() {
        lblModulo.setText("Empleados");
        modeloTabla.setRowCount(0);
        modeloTabla.setColumnIdentifiers(new String[]{
            "ID", "Username", "Nombre", "Apellidos", "Email", "DNI", "Cargo", "Salario"
        });
 
        List<Empleado> empleados = empleadoDAO.listarTodos();
        for (Empleado e : empleados) {
            modeloTabla.addRow(new Object[]{
                e.getId(), e.getUsername(), e.getNombre(), e.getApellidos(),
                e.getEmail(), e.getDni(), e.getCargo(), e.getSalario()
            });
        }
 
        lblCampo1 = ops_label("Username:");  txtCampo1 = ops_field();
        lblCampo2 = ops_label("Nombre:");    txtCampo2 = ops_field();
        lblCampo3 = ops_label("Apellidos:"); txtCampo3 = ops_field();
        lblCampo4 = ops_label("Email:");     txtCampo4 = ops_field();
        lblCampo5 = ops_label("Cargo:");
        cmbCampo1 = new JComboBox<>(new String[]{"camarero","cocinero","repartidor","admin"});
        lblCampo6 = ops_label("Salario:");   txtCampo6 = ops_field();
 
        ops_titulo("Operaciones - Empleados");
        ops_add(lblCampo1, txtCampo1); ops_add(lblCampo2, txtCampo2);
        ops_add(lblCampo3, txtCampo3); ops_add(lblCampo4, txtCampo4);
        ops_addCombo(lblCampo5, cmbCampo1);
        ops_add(lblCampo6, txtCampo6);
        ops_botones();
    }
 
    // MÓDULO PRODUCTOS
    private void cargarProductos() {
        lblModulo.setText("Productos");
        modeloTabla.setRowCount(0);
        modeloTabla.setColumnIdentifiers(new String[]{
            "ID", "Nombre", "Categoría", "Precio (€)", "Disponible", "Descripción"
        });
 
        List<Producto> productos = productoDAO.listarTodos();
        for (Producto p : productos) {
            modeloTabla.addRow(new Object[]{
                p.getId(), p.getNombre(), p.getCategoria(),
                String.format("%.2f", p.getPrecio()),
                p.isDisponible() ? "Sí" : "No",
                p.getDescripcion()
            });
        }
 
        lblCampo1 = ops_label("Nombre:");      txtCampo1 = ops_field();
        lblCampo2 = ops_label("Descripción:"); txtCampo2 = ops_field();
        lblCampo3 = ops_label("Precio (€):");  txtCampo3 = ops_field();
        lblCampo4 = ops_label("Categoría:");
        cmbCampo1 = new JComboBox<>(new String[]{"pizza","pasta","entrante","postre","bebida"});
 
        ops_titulo("Operaciones - Productos");
        ops_add(lblCampo1, txtCampo1); ops_add(lblCampo2, txtCampo2);
        ops_add(lblCampo3, txtCampo3); ops_addCombo(lblCampo4, cmbCampo1);
        ops_botones();
    }
 
    // MÓDULO PEDIDOS
    private void cargarPedidos() {
        lblModulo.setText("Pedidos");
        modeloTabla.setRowCount(0);
        modeloTabla.setColumnIdentifiers(new String[]{
            "ID", "Fecha", "Cliente ID", "Empleado ID", "Estado", "Tipo", "Total (€)"
        });
 
        List<Pedido> pedidos = pedidoDAO.listarTodos();
        for (Pedido p : pedidos) {
            modeloTabla.addRow(new Object[]{
                p.getId(), p.getFecha(), p.getClienteId(), p.getEmpleadoId(),
                p.getEstado(), p.getTipo(), String.format("%.2f", p.getTotal())
            });
        }
 
        lblCampo1 = ops_label("Cliente ID:"); txtCampo1 = ops_field();
        lblCampo2 = ops_label("Estado:");
        cmbCampo1 = new JComboBox<>(new String[]{
            "pendiente","en_cocina","listo","entregado","cancelado"
        });
        lblCampo3 = ops_label("Tipo:");
        lblCampo4 = ops_label("Observaciones:"); txtCampo4 = ops_field();
 
        ops_titulo("Operaciones - Pedidos");
        ops_add(lblCampo1, txtCampo1);
        ops_addCombo(lblCampo2, cmbCampo1);
        ops_add(lblCampo4, txtCampo4);
        ops_botones();
    }
 
    //MÓDULO USUARIOS
    private void cargarUsuarios() {
        lblModulo.setText("Usuarios");
        modeloTabla.setRowCount(0);
        modeloTabla.setColumnIdentifiers(new String[]{
            "ID", "Username", "Email", "Nombre", "Apellidos", "DNI", "Rol", "Activo"
        });
 
        List<Usuario> usuarios = usuarioDAO.listarTodos();
        for (Usuario u : usuarios) {
            modeloTabla.addRow(new Object[]{
                u.getId(), u.getUsername(), u.getEmail(), u.getNombre(),
                u.getApellidos(), u.getDni(), u.getRol(),
                u.isActivo() ? "Sí" : "No"
            });
        }
 
        lblCampo1 = ops_label("Username:"); txtCampo1 = ops_field();
        lblCampo2 = ops_label("Email:");    txtCampo2 = ops_field();
        lblCampo3 = ops_label("Nombre:");   txtCampo3 = ops_field();
        lblCampo4 = ops_label("Apellidos:");txtCampo4 = ops_field();
 
        ops_titulo("Operaciones - Usuarios");
        ops_add(lblCampo1, txtCampo1); ops_add(lblCampo2, txtCampo2);
        ops_add(lblCampo3, txtCampo3); ops_add(lblCampo4, txtCampo4);
        ops_botones();
    }
 
    // Cargar fila seleccionada en el formulario
    private void cargarFilaEnFormulario(int fila) {
        idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
 
        switch (moduloActivo) {
            case "clientes" -> {
                txtCampo1.setText(modeloTabla.getValueAt(fila, 1).toString());
                txtCampo2.setText(modeloTabla.getValueAt(fila, 2).toString());
                txtCampo3.setText(modeloTabla.getValueAt(fila, 3).toString());
                txtCampo4.setText(modeloTabla.getValueAt(fila, 4).toString());
                txtCampo5.setText(modeloTabla.getValueAt(fila, 6).toString());
                txtCampo6.setText(modeloTabla.getValueAt(fila, 7).toString());
            }
            case "empleados" -> {
                txtCampo1.setText(modeloTabla.getValueAt(fila, 1).toString());
                txtCampo2.setText(modeloTabla.getValueAt(fila, 2).toString());
                txtCampo3.setText(modeloTabla.getValueAt(fila, 3).toString());
                txtCampo4.setText(modeloTabla.getValueAt(fila, 4).toString());
                cmbCampo1.setSelectedItem(modeloTabla.getValueAt(fila, 6).toString());
                txtCampo6.setText(modeloTabla.getValueAt(fila, 7).toString());
            }
            case "productos" -> {
                txtCampo1.setText(modeloTabla.getValueAt(fila, 1).toString());
                txtCampo2.setText(modeloTabla.getValueAt(fila, 5).toString());
                txtCampo3.setText(modeloTabla.getValueAt(fila, 3).toString());
                cmbCampo1.setSelectedItem(modeloTabla.getValueAt(fila, 2).toString());
            }
            case "pedidos" -> {
                txtCampo1.setText(modeloTabla.getValueAt(fila, 2).toString());
                cmbCampo1.setSelectedItem(modeloTabla.getValueAt(fila, 4).toString());
                if (txtCampo4 != null) txtCampo4.setText("");
            }
            case "usuarios" -> {
                txtCampo1.setText(modeloTabla.getValueAt(fila, 1).toString());
                txtCampo2.setText(modeloTabla.getValueAt(fila, 2).toString());
                txtCampo3.setText(modeloTabla.getValueAt(fila, 3).toString());
                txtCampo4.setText(modeloTabla.getValueAt(fila, 4).toString());
            }
        }
    }
 
    // Acción NUEVO
    private void accionNuevo() {
        idSeleccionado = -1;
        limpiarFormulario();
        tabla.clearSelection();
    }
 
    // Acción GUARDAR
    private void accionGuardar() {
        boolean exito = false;
 
        switch (moduloActivo) {
            case "clientes" -> {
                Cliente c = new Cliente();
                c.setUsername(txtCampo1.getText().trim());
                c.setPassword("cliente123");
                c.setEmail(txtCampo4.getText().trim());
                c.setNombre(txtCampo2.getText().trim());
                c.setApellidos(txtCampo3.getText().trim());
                c.setDni("");
                c.setActivo(true);
                c.setTelefono(txtCampo5.getText().trim());
                c.setDireccion(txtCampo6.getText().trim());
 
                if (idSeleccionado == -1) {
                    exito = clienteDAO.insertar(c);
                } else {
                    c.setId(idSeleccionado);
                    exito = clienteDAO.actualizar(c);
                }
                cargarClientes();
            }
            case "empleados" -> {
                Empleado e = new Empleado();
                e.setUsername(txtCampo1.getText().trim());
                e.setPassword("empleado123");
                e.setEmail(txtCampo4.getText().trim());
                e.setNombre(txtCampo2.getText().trim());
                e.setApellidos(txtCampo3.getText().trim());
                e.setDni("");
                e.setActivo(true);
                e.setCargo((String) cmbCampo1.getSelectedItem());
                try { e.setSalario(Double.parseDouble(txtCampo6.getText().trim())); }
                catch (NumberFormatException ex) { e.setSalario(1200.00); }
 
                if (idSeleccionado == -1) {
                    exito = empleadoDAO.insertar(e);
                } else {
                    e.setId(idSeleccionado);
                    exito = empleadoDAO.actualizar(e);
                }
                cargarEmpleados();
            }
            case "productos" -> {
                Producto p = new Producto();
                p.setNombre(txtCampo1.getText().trim());
                p.setDescripcion(txtCampo2.getText().trim());
                try { p.setPrecio(Double.parseDouble(txtCampo3.getText().trim())); }
                catch (NumberFormatException ex) { p.setPrecio(0.0); }
                p.setCategoria((String) cmbCampo1.getSelectedItem());
                p.setDisponible(true);
 
                if (idSeleccionado == -1) {
                    exito = productoDAO.insertar(p);
                } else {
                    p.setId(idSeleccionado);
                    exito = productoDAO.actualizar(p);
                }
                cargarProductos();
            }
            case "pedidos" -> {
                if (idSeleccionado != -1) {
                    String nuevoEstado = (String) cmbCampo1.getSelectedItem();
                    exito = pedidoDAO.actualizarEstado(idSeleccionado, nuevoEstado);
                    cargarPedidos();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Selecciona un pedido para cambiar su estado.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            case "usuarios" -> {
                if (idSeleccionado != -1) {
                    Usuario u = usuarioDAO.listarTodos().stream()
                        .filter(x -> x.getId() == idSeleccionado)
                        .findFirst().orElse(null);
                    if (u != null) {
                        u.setUsername(txtCampo1.getText().trim());
                        u.setEmail(txtCampo2.getText().trim());
                        u.setNombre(txtCampo3.getText().trim());
                        u.setApellidos(txtCampo4.getText().trim());
                        exito = usuarioDAO.actualizar(u);
                        cargarUsuarios();
                    }
                }
            }
        }
 
        if (exito) {
            JOptionPane.showMessageDialog(this, "Operación realizada correctamente.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar. Revisa los datos.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    // Acción ELIMINAR
    private void accionEliminar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un registro para eliminar.",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Seguro que quieres eliminar este registro?",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
 
        if (confirm != JOptionPane.YES_OPTION) return;
 
        boolean exito = switch (moduloActivo) {
            case "clientes"  -> clienteDAO.eliminar(idSeleccionado);
            case "empleados" -> empleadoDAO.eliminar(idSeleccionado);
            case "productos" -> productoDAO.eliminar(idSeleccionado);
            case "pedidos"   -> pedidoDAO.eliminar(idSeleccionado);
            case "usuarios"  -> usuarioDAO.eliminar(idSeleccionado);
            default          -> false;
        };
 
        if (exito) {
            JOptionPane.showMessageDialog(this, "Registro eliminado correctamente.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarModulo(moduloActivo);
        } else {
            JOptionPane.showMessageDialog(this, "Error al eliminar.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    // Cambiar contraseña
    private void cambiarPassword() {
        JPasswordField txtNueva = new JPasswordField();
        int result = JOptionPane.showConfirmDialog(this, txtNueva,
            "Nueva contraseña:", JOptionPane.OK_CANCEL_OPTION);
 
        if (result == JOptionPane.OK_OPTION) {
            String nueva = new String(txtNueva.getPassword()).trim();
            if (!nueva.isEmpty()) {
                boolean ok = usuarioDAO.cambiarPassword(usuarioActual.getId(), nueva);
                JOptionPane.showMessageDialog(this,
                    ok ? "✅ Contraseña cambiada correctamente." : "❌ Error al cambiar la contraseña.",
                    "Cambiar contraseña", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
 
    // Cerrar sesión
    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Seguro que quieres cerrar sesión?",
            "Cerrar sesión", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new Login().setVisible(true);
        }
    }
 
    // Aplicar tema
    private void aplicarTema(boolean oscuro) {
        temaOscuro = oscuro;
        System.out.println("Tema oscuro: " + temaOscuro);
        dispose();
        new Principal(usuarioActual).setVisible(true);
    }
 
    // Limpiar formulario
    private void limpiarFormulario() {
        if (txtCampo1 != null) txtCampo1.setText("");
        if (txtCampo2 != null) txtCampo2.setText("");
        if (txtCampo3 != null) txtCampo3.setText("");
        if (txtCampo4 != null) txtCampo4.setText("");
        if (txtCampo5 != null) txtCampo5.setText("");
        if (txtCampo6 != null) txtCampo6.setText("");
    }
 
    // Helpers para construir el panel de operaciones
    private JButton crearBotonNav(String texto, String modulo) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setBackground(getPanel());
        btn.setForeground(getTexto());
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(e -> cargarModulo(modulo));
        return btn;
    }
 
    private void ops_titulo(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(COLOR_ACENTO);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panelOps.add(lbl);
    }
 
    private void ops_add(JLabel lbl, JTextField txt) {
        lbl.setForeground(getTexto());
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        panelOps.add(lbl);
        panelOps.add(Box.createVerticalStrut(2));
        panelOps.add(txt);
        panelOps.add(Box.createVerticalStrut(6));
    }
 
    private void ops_addCombo(JLabel lbl, JComboBox<String> cmb) {
        lbl.setForeground(getTexto());
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmb.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        panelOps.add(lbl);
        panelOps.add(Box.createVerticalStrut(2));
        panelOps.add(cmb);
        panelOps.add(Box.createVerticalStrut(6));
    }
 
    private void ops_botones() {
        panelOps.add(Box.createVerticalGlue());
 
        JButton btnNuevo    = crearBotonOp("Nuevo",    new Color(60, 120, 60));
        JButton btnGuardar  = crearBotonOp("Guardar",  new Color(255, 100, 0));
        JButton btnEliminar = crearBotonOp("Eliminar", new Color(180, 40, 40));
 
        btnNuevo.addActionListener(e    -> accionNuevo());
        btnGuardar.addActionListener(e  -> accionGuardar());
        btnEliminar.addActionListener(e -> accionEliminar());
 
        panelOps.add(btnNuevo);
        panelOps.add(Box.createVerticalStrut(5));
        panelOps.add(btnGuardar);
        panelOps.add(Box.createVerticalStrut(5));
        panelOps.add(btnEliminar);
    }
 
    private JButton crearBotonOp(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }
 
    private JLabel ops_label(String texto) {
        return new JLabel(texto);
    }
 
    private JTextField ops_field() {
        return new JTextField();
    }
}
