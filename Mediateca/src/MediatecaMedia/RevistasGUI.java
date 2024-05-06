package MediatecaMedia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import MediatecaDB.ConexionDB;

public class RevistasGUI extends JFrame {
    private JPanel panelMenu;
    private JButton btnAgregar, btnModificar, btnListar, btnBorrar, btnBuscar, btnSalir;
    private JTable tablaMateriales;
    private DefaultTableModel modeloTabla;
    private Connection conexion;

    public RevistasGUI() {
        setTitle("Mediateca          **  Sistema de Libros  **");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(1360, 700);

        // Panel de botones
        panelMenu = new JPanel();
        panelMenu.setLayout(new GridLayout(6, 1));

        btnAgregar = new JButton("Agregar Revista");
        btnModificar = new JButton("Modificar Revista");
        btnListar = new JButton("Listar Revistas Disponibles");
        btnBorrar = new JButton("Borrar Revista");
        btnBuscar = new JButton("Buscar Revista");
        btnSalir = new JButton("Salir");

        btnAgregar.addActionListener((ActionEvent e) -> {
            // Abre la ventana para agregar material
            AgregarRevistaGUI agregarRevistaGUI = new AgregarRevistaGUI(RevistasGUI.this);
        });

        // Agrega más ActionListeners para los otros botones según sea necesario

        panelMenu.add(btnAgregar);
        panelMenu.add(btnModificar);
        panelMenu.add(btnListar);
        panelMenu.add(btnBorrar);
        panelMenu.add(btnBuscar);
        panelMenu.add(btnSalir);

        add(panelMenu, BorderLayout.WEST);

        // Tabla para listar materiales
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("Código");
        modeloTabla.addColumn("Título");
        modeloTabla.addColumn("Editorial");
        modeloTabla.addColumn("Periodicidad");
        modeloTabla.addColumn("Fecha de publicacion");
        modeloTabla.addColumn("Unidades disponibles");

        tablaMateriales = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaMateriales);
        add(scrollPane, BorderLayout.CENTER);

        // Conectar a la base de datos
        try {
            conexion = ConexionDB.getConnection();
            cargarContenido();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Agregar ActionListener al botón "Salir"
        btnSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Ocultar ventana
                setVisible(false);
            }
        });

        setVisible(true);
    }

    // Método para cargar materiales desde la base de datos a la tabla
    private void cargarContenido() throws SQLException {
        modeloTabla.setRowCount(0);
        Statement statement = conexion.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM revistas");
        while (resultSet.next()) {
            modeloTabla.addRow(new Object[]{
                    resultSet.getString("codigo"),
                    resultSet.getString("titulo"),
                    resultSet.getString("editorial"),
                    resultSet.getString("periodicidad"),
                    resultSet.getString("fecha_publicacion"),
                    resultSet.getInt("unidades")
            });
        }
    }

    // Método para agregar un material a la base de datos y actualizar la tabla
    public void agregarContenido(Revista revista) throws SQLException {
        PreparedStatement statement = conexion.prepareStatement(
                "INSERT INTO revistas (codigo, titulo, editorial, periodicidad, fecha_publicacion, unidades) " +
                        "VALUES (?, ?, ?, ?, ?, ?)");
        statement.setString(1, revista.getCodigo());
        statement.setString(2, revista.getTitulo());
        statement.setString(3, revista.getEditorial());
        statement.setString(4, revista.getPeriodicidad());
        statement.setString(5, revista.getPublicacion());
        statement.setInt(6, revista.getUnidades());
        statement.executeUpdate();
        cargarContenido();
    }

    // Método para cerrar la conexión a la base de datos
    public void cerrarConexion() {
        try {
            if (conexion != null)
                conexion.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

class AgregarRevistaGUI extends JFrame {
    private RevistasGUI gui;
    private JTextField txtCodigo, txtTitulo, txtEditorial, txtPeriodicidad, txtPublicacion, txtUnidades;
    private JButton btnAgregar, btnCancelar;

    public AgregarRevistaGUI(RevistasGUI gui) {
        this.gui = gui;
        setTitle("Agregar Revista");
        setSize(600, 250);
        setLayout(new GridLayout(4, 2));

        add(new JLabel("Código:"));
        txtCodigo = new JTextField();
        add(txtCodigo);

        add(new JLabel("Título:"));
        txtTitulo = new JTextField();
        add(txtTitulo);

        add(new JLabel("Editorial:"));
        txtEditorial = new JTextField();
        add(txtEditorial);

        add(new JLabel("Periodicidad:"));
        txtPeriodicidad = new JTextField();
        add(txtPeriodicidad);

        add(new JLabel("Fecha de publicación:"));
        txtPublicacion = new JTextField();
        add(txtPublicacion);

        add(new JLabel("Unidades disponibles:"));
        txtUnidades = new JTextField();
        add(txtUnidades);

        btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Validar y agregar el material
                String codigo = txtCodigo.getText();
                String titulo = txtTitulo.getText();
                String editorial = txtEditorial.getText();
                String periodicidad = txtPeriodicidad.getText();
                String publicacion = txtPublicacion.getText();
                int unidades = Integer.parseInt(txtUnidades.getText());

                Revista revista = new Revista(codigo, titulo, editorial, periodicidad, publicacion, unidades);
                try {
                    gui.agregarContenido(revista);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                dispose();
            }
        });
        add(btnAgregar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        add(btnCancelar);

        setVisible(true);
    }
}

class Revista {
    private String codigo;
    private String titulo;
    private String editorial;
    private String periodicidad;
    private String publicacion;
    private int unidades;

    public Revista(String codigo, String titulo, String editorial, String periodicidad, String publicacion, int unidades) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.editorial = editorial;
        this.periodicidad = periodicidad;
        this.publicacion = publicacion;
        this.unidades = unidades;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getEditorial() {
        return editorial;
    }

    public String getPeriodicidad() {
        return periodicidad;
    }

    public String getPublicacion() {
        return publicacion;
    }

    public int getUnidades() {
        return unidades;
    }
}