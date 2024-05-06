package MediatecaMedia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import MediatecaDB.ConexionDB;

public class DVDsGUI extends JFrame {
    private JPanel panelMenu;
    private JButton btnAgregar, btnModificar, btnListar, btnBorrar, btnBuscar, btnSalir;
    private JTable tablaMateriales;
    private DefaultTableModel modeloTabla;
    private Connection conexion;

    public DVDsGUI() {
        setTitle("Mediateca          **  Sistema de DVDs  **");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(1360, 700);

        // Panel de botones
        panelMenu = new JPanel();
        panelMenu.setLayout(new GridLayout(6, 1));

        btnAgregar = new JButton("Agregar DVD");
        btnModificar = new JButton("Modificar DVD");
        btnListar = new JButton("Listar DVDs Disponibles");
        btnBorrar = new JButton("Borrar DVD");
        btnBuscar = new JButton("Buscar DVD");
        btnSalir = new JButton("Salir");

        btnAgregar.addActionListener((ActionEvent e) -> {
            // Abre la ventana para agregar material
            AgregarDVDGUI agregarDVDGUI = new AgregarDVDGUI(DVDsGUI.this);
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
        modeloTabla.addColumn("Director ");
        modeloTabla.addColumn("Genero");
        modeloTabla.addColumn("Duración");

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
        ResultSet resultSet = statement.executeQuery("SELECT * FROM dvds");
        while (resultSet.next()) {
            modeloTabla.addRow(new Object[]{
                    resultSet.getString("codigo"),
                    resultSet.getString("titulo"),
                    resultSet.getString("director"),
                    resultSet.getInt("duracion"),
                    resultSet.getString("genero")
            });
        }
    }

    // Método para agregar un material a la base de datos y actualizar la tabla
    public void agregarContenido(DVD dvd) throws SQLException {
        PreparedStatement statement = conexion.prepareStatement(
                "INSERT INTO dvds (codigo, titulo, director, duracion, genero) " +
                        "VALUES (?, ?, ?, ?, ?)");
        statement.setString(1, dvd.getCodigo());
        statement.setString(2, dvd.getTitulo());
        statement.setString(3, dvd.getDirector());
        statement.setInt(4, dvd.getDuracion());
        statement.setString(5, dvd.getGenero());
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

class AgregarDVDGUI extends JFrame {
    private DVDsGUI gui;
    private JTextField txtCodigo, txtTitulo, txtDirector, txtGenero, txtDuracion;
    private JButton btnAgregar, btnCancelar;

    public AgregarDVDGUI(DVDsGUI gui) {
        this.gui = gui;
        setTitle("Agregar DVD");
        setSize(600, 200);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Código:"));
        txtCodigo = new JTextField();
        add(txtCodigo);

        add(new JLabel("Título:"));
        txtTitulo = new JTextField();
        add(txtTitulo);

        add(new JLabel("Director:"));
        txtDirector = new JTextField();
        add(txtDirector);
        
        add(new JLabel("Qué genero es:"));
        txtGenero = new JTextField();
        add(txtGenero);
        
        add(new JLabel("Duración:"));
        txtDuracion = new JTextField();
        add(txtDuracion);

        btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Validar y agregar el material
                String codigo = txtCodigo.getText();
                String titulo = txtTitulo.getText();
                String artista = txtDirector.getText();
                String genero = txtGenero.getText();
                int duracion = Integer.parseInt(txtDuracion.getText());

                DVD dvd = new DVD(codigo, titulo, artista, duracion, genero);
                try {
                    gui.agregarContenido(dvd);
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

class DVD {
    private String codigo;
    private String titulo;
    private String director;
    private int duracion;
    private String genero;

    public DVD(String codigo, String titulo, String director, int duracion, String genero) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.director = director;
        this.duracion = duracion;
        this.genero = genero;
        
    }

    public String getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }
    
    public String getDirector() {
        return director;
    }
    
    public int getDuracion() {
        return duracion;
    }
    
    public String getGenero() {
        return genero;
    }

}