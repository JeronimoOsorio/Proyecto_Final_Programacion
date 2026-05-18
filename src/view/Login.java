package view;

import dao.UsuarioDAOImpl;
import model.Usuario;
 
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Login extends JFrame{
    
    // Componentes de la ventana
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton        btnEntrar;
    private JLabel         lblRegistro;

    // DAO para validar credenciales
    private final UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();

    // Constructor Login
    public Login(){
        initUI();
    }

    // ── Construcción de la interfaz ──────────────────────────────────
    private void initUI() {
        setTitle("Jero Pepperoni — Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 340);
        setLocationRelativeTo(null); // centra en pantalla
        setResizable(false);
 
        // Panel principal con fondo oscuro
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setBackground(new Color(30, 30, 30));
 
        // Panel superior: logo/título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(30, 30, 30));
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0));
 
        JLabel lblTitulo = new JLabel("Jero Pepperoni");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(255, 100, 0));
        panelTitulo.add(lblTitulo);
 
        JLabel lblSubtitulo = new JLabel("Sistema de Gestión");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(Color.GRAY);
 
        JPanel panelSubtitulo = new JPanel();
        panelSubtitulo.setBackground(new Color(30, 30, 30));
        panelSubtitulo.add(lblSubtitulo);
 
        JPanel panelHeader = new JPanel(new GridLayout(2, 1));
        panelHeader.setBackground(new Color(30, 30, 30));
        panelHeader.add(panelTitulo);
        panelHeader.add(panelSubtitulo);
 
        // Panel central: formulario
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(new Color(45, 45, 45));
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
 
        // Label y campo username
        JLabel lblUsername = new JLabel("Usuario:");
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        panelForm.add(lblUsername, gbc);
 
        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtUsername.setPreferredSize(new Dimension(200, 32));
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.7;
        panelForm.add(txtUsername, gbc);
 
        // Label y campo password
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        panelForm.add(lblPassword, gbc);
 
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPassword.setPreferredSize(new Dimension(200, 32));
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.7;
        panelForm.add(txtPassword, gbc);
 
        // Botón entrar
        btnEntrar = new JButton("Entrar");
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEntrar.setBackground(new Color(255, 100, 0));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEntrar.setBorderPainted(false);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        panelForm.add(btnEntrar, gbc);
 
        // Enlace a registro
        lblRegistro = new JLabel("¿No tienes cuenta? Regístrate aquí");
        lblRegistro.setForeground(new Color(100, 150, 255));
        lblRegistro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblRegistro.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 5, 5);
        panelForm.add(lblRegistro, gbc);
 
        // ── Ensamblar paneles ────────────────────────────────────────
        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(panelForm, BorderLayout.CENTER);
        add(panelPrincipal);
 
        // ── Eventos ──────────────────────────────────────────────────
        btnEntrar.addActionListener(e -> login());
 
        // Permite hacer login pulsando Enter en cualquier campo
        txtUsername.addActionListener(e -> login());
        txtPassword.addActionListener(e -> login());
 
        // Enlace a ventana de registro
        lblRegistro.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirRegistro();
            }
        });
    }

    // Lógica de login
    private void login(){
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        // Validación de campos vacíos
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor, introduzca el usuario y contraseña.",
                "Campos vacíos",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar contra la base de datos
        Usuario usuario = usuarioDAO.validar(username, password);

        if (usuario != null) {
            //Credenciales correctas abre dashboard
            dispose(); // esto cierra ventana
            new Principal(usuario).setVisible(true);
        } else {
            // Credenciales incorrectas muestra el error
            JOptionPane.showMessageDialog(this,
                "Usuario o contraseña incorrectos.\nInténtalo de nuevo.",
                "Error de acceso",
                JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            txtUsername.requestFocus();
        }
    }

    // Abrir ventana de registro

    private void abrirRegistro(){
        dispose();
        new Registro().setVisible(true);
    }
}
