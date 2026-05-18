package view;

import dao.ClienteDAOImpl;
import dao.EmpleadoDAOImpl;
import model.Cliente;
import model.Empleado;

import javax.swing.*;
import java.awt.*;

public class Registro extends JFrame {

    // Componentes comunes
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtEmail;
    private JTextField txtNombre;
    private JTextField txtApellidos;
    private JTextField txtDni;
    private JComboBox<String> cmbRol;

    // Componentes exclusivos de Cliente
    private JTextField txtTelefono;
    private JTextField txtDireccion;
    private JPanel     panelCliente;

    // Componentes exclusivos de Empleado
    private JComboBox<String> cmbCargo;
    private JTextField        txtSalario;
    private JPanel            panelEmpleado;

    // Botones
    private JButton btnRegistrar;
    private JButton btnVolver;

    // DAOs
    private final ClienteDAOImpl  clienteDAO  = new ClienteDAOImpl();
    private final EmpleadoDAOImpl empleadoDAO = new EmpleadoDAOImpl();

    // Constructor
    public Registro() {
        initUI();
    }

    // Construcción de la interfaz
    private void initUI() {
        setTitle("Jero Pepperoni — Registro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 620);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(30, 30, 30));

        // Título
        JLabel lblTitulo = new JLabel("Crear cuenta nueva", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(255, 100, 0));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Panel formulario con scroll
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(new Color(45, 45, 45));
        panelForm.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 5, 6, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Campos comunes
        txtUsername  = addCampo(panelForm, gbc, "Usuario:",     0);
        txtPassword  = new JPasswordField();
        addCampoCustom(panelForm, gbc, "Contraseña:", txtPassword, 1);
        txtEmail     = addCampo(panelForm, gbc, "Email:",        2);
        txtNombre    = addCampo(panelForm, gbc, "Nombre:",       3);
        txtApellidos = addCampo(panelForm, gbc, "Apellidos:",    4);
        txtDni       = addCampo(panelForm, gbc, "DNI:",          5);

        // Selector de rol
        JLabel lblRol = crearLabel("Rol:");
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 1;
        panelForm.add(lblRol, gbc);

        cmbRol = new JComboBox<>(new String[]{"cliente", "empleado"});
        cmbRol.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.gridy = 6;
        panelForm.add(cmbRol, gbc);

        // Panel dinámico: campos de Cliente
        panelCliente = new JPanel(new GridBagLayout());
        panelCliente.setBackground(new Color(45, 45, 45));
        GridBagConstraints gbcC = new GridBagConstraints();
        gbcC.insets = new Insets(6, 5, 6, 5);
        gbcC.fill = GridBagConstraints.HORIZONTAL;
        gbcC.weightx = 1.0;

        txtTelefono = addCampo(panelCliente, gbcC, "Teléfono:", 0);
        txtDireccion = addCampo(panelCliente, gbcC, "Dirección:", 1);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        panelForm.add(panelCliente, gbc);

        // Panel dinámico: campos de Empleado
        panelEmpleado = new JPanel(new GridBagLayout());
        panelEmpleado.setBackground(new Color(45, 45, 45));
        GridBagConstraints gbcE = new GridBagConstraints();
        gbcE.insets = new Insets(6, 5, 6, 5);
        gbcE.fill = GridBagConstraints.HORIZONTAL;
        gbcE.weightx = 1.0;

        JLabel lblCargo = crearLabel("Cargo:");
        gbcE.gridx = 0; gbcE.gridy = 0; gbcE.gridwidth = 1;
        panelEmpleado.add(lblCargo, gbcE);

        cmbCargo = new JComboBox<>(new String[]{"camarero", "cocinero", "repartidor", "admin"});
        cmbCargo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbcE.gridx = 1; gbcE.gridy = 0;
        panelEmpleado.add(cmbCargo, gbcE);

        txtSalario = addCampo(panelEmpleado, gbcE, "Salario (€):", 1);

        panelEmpleado.setVisible(false); // oculto por defecto

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        panelForm.add(panelEmpleado, gbc);

        // Botones
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 0));
        panelBotones.setBackground(new Color(45, 45, 45));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRegistrar.setBackground(new Color(255, 100, 0));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setBorderPainted(false);
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnVolver = new JButton("Volver al Login");
        btnVolver.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnVolver.setBackground(new Color(80, 80, 80));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnVolver);

        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        panelForm.add(panelBotones, gbc);

        JScrollPane scroll = new JScrollPane(panelForm);
        scroll.setBorder(null);
        panelPrincipal.add(scroll, BorderLayout.CENTER);
        add(panelPrincipal);

        // Eventos

        // Cambio de rol: muestra/oculta paneles dinámicos
        cmbRol.addActionListener(e -> {
            String rolSeleccionado = (String) cmbRol.getSelectedItem();
            panelCliente.setVisible("cliente".equals(rolSeleccionado));
            panelEmpleado.setVisible("empleado".equals(rolSeleccionado));
            pack();
            revalidate();
        });

        btnRegistrar.addActionListener(e -> registrar());
        btnVolver.addActionListener(e -> {
            dispose();
            new Login().setVisible(true);
        });
    }

    // Lógica de registro
    private void registrar() {
        // Validar campos comunes vacíos
        if (txtUsername.getText().trim().isEmpty() ||
            txtEmail.getText().trim().isEmpty()    ||
            txtNombre.getText().trim().isEmpty()   ||
            txtApellidos.getText().trim().isEmpty()||
            txtDni.getText().trim().isEmpty()      ||
            new String(txtPassword.getPassword()).trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                "Por favor, rellena todos los campos obligatorios.",
                "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String rol = (String) cmbRol.getSelectedItem();
        boolean exito;

        if ("cliente".equals(rol)) {
            Cliente cliente = new Cliente();
            cliente.setUsername(txtUsername.getText().trim());
            cliente.setPassword(new String(txtPassword.getPassword()).trim());
            cliente.setEmail(txtEmail.getText().trim());
            cliente.setNombre(txtNombre.getText().trim());
            cliente.setApellidos(txtApellidos.getText().trim());
            cliente.setDni(txtDni.getText().trim());
            cliente.setActivo(true);
            cliente.setTelefono(txtTelefono.getText().trim());
            cliente.setDireccion(txtDireccion.getText().trim());
            exito = clienteDAO.insertar(cliente);

        } else {
            Empleado empleado = new Empleado();
            empleado.setUsername(txtUsername.getText().trim());
            empleado.setPassword(new String(txtPassword.getPassword()).trim());
            empleado.setEmail(txtEmail.getText().trim());
            empleado.setNombre(txtNombre.getText().trim());
            empleado.setApellidos(txtApellidos.getText().trim());
            empleado.setDni(txtDni.getText().trim());
            empleado.setActivo(true);
            empleado.setCargo((String) cmbCargo.getSelectedItem());
            try {
                empleado.setSalario(Double.parseDouble(txtSalario.getText().trim()));
            } catch (NumberFormatException e) {
                empleado.setSalario(1200.00);
            }
            exito = empleadoDAO.insertar(empleado);
        }

        if (exito) {
            JOptionPane.showMessageDialog(this,
                "¡Cuenta creada correctamente! Ya puedes iniciar sesión.",
                "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new Login().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                "Error al registrar. El usuario o DNI ya existe.",
                "Error de registro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Métodos auxiliares para crear campos
    private JTextField addCampo(JPanel panel, GridBagConstraints gbc,
                                 String label, int fila) {
        JLabel lbl = crearLabel(label);
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        panel.add(lbl, gbc);

        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.gridy = fila;
        panel.add(txt, gbc);
        return txt;
    }

    private void addCampoCustom(JPanel panel, GridBagConstraints gbc,
                                 String label, JComponent campo, int fila) {
        JLabel lbl = crearLabel(label);
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        panel.add(lbl, gbc);
        gbc.gridx = 1; gbc.gridy = fila;
        panel.add(campo, gbc);
    }

    private JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return lbl;
    }
}