package MediatecaMedia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import MediatecaDB.ConexionDB;

public class CDsGUI extends JFrame {
    private JPanel panelMenu;
    private JButton btnAgregar, btnModificar, btnListar, btnBorrar, btnBuscar, btnSalir;
    private JTable tablaMateriales;
    private DefaultTableModel modeloTabla;
    private Connection conexion;

    public CDsGUI() {
        setTitle("Mediateca          **  Sistema de CDs  **");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(1360, 700);

        // Panel de botones
        panelMenu = new JPanel();
        panelMenu.setLayout(new GridLayout(6, 1));

        btnAgregar = new JButton("Agregar CD");
        btnModificar = new JButton("Modificar CD");
        btnListar = new JButton("Listar CDs Disponibles");
        btnBorrar = new JButton("Borrar CD");
        btnBuscar = new JButton("Buscar CD");
        btnSalir = new JButton("Regresar");

        btnAgregar.addActionListener((ActionEvent e) -> {
            // Abre la ventana para agregar material
            AgregarCDGUI agregarCDGUI = new AgregarCDGUI(CDsGUI.this);
        });
        
        btnListar.addActionListener((ActionEvent e) -> {
            try {
                cargarContenido();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        
        btnBorrar.addActionListener((ActionEvent e) -> {
            BorrarCDGUI borrarCDGUI = new BorrarCDGUI(CDsGUI.this);
        });
        
        btnBuscar.addActionListener((ActionEvent e) -> {
            BuscarCDGUI buscarCDGUI = new BuscarCDGUI(CDsGUI.this);
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
        modeloTabla.addColumn("Artista");
        modeloTabla.addColumn("Genero");
        modeloTabla.addColumn("Duración");
        modeloTabla.addColumn("Canciones");
        modeloTabla.addColumn("Unidades");

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
        ResultSet resultSet = statement.executeQuery("SELECT * FROM cds");
        while (resultSet.next()) {
            modeloTabla.addRow(new Object[]{
                    resultSet.getString("codigo"),
                    resultSet.getString("titulo"),
                    resultSet.getString("artista"),
                    resultSet.getString("genero"),
                    resultSet.getInt("duracion"),
                    resultSet.getInt("canciones"),
                    resultSet.getInt("unidades")
            });
        }
    }

    // Método para agregar un material a la base de datos y actualizar la tabla
    public void agregarContenido(CD cd) throws SQLException {
        PreparedStatement statement = conexion.prepareStatement(
                "INSERT INTO cds (codigo, titulo, artista, genero, duracion, canciones, unidades) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)");
        statement.setString(1, cd.getCodigo());
        statement.setString(2, cd.getTitulo());
        statement.setString(3, cd.getArtista());
        statement.setString(4, cd.getGenero());
        statement.setInt(5, cd.getDuracion());
        statement.setInt(6, cd.getCanciones());
        statement.setInt(7, cd.getUnidades());
        statement.executeUpdate();
        cargarContenido();
    }
    
    // Metodo para buscar material en la base de datos
    public void buscarContenido(String busqueda) throws SQLException {
        modeloTabla.setRowCount(0);
        PreparedStatement statement = conexion.prepareStatement("SELECT * FROM cds WHERE titulo = ?");
        statement.setString(1, busqueda);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            modeloTabla.addRow(new Object[]{
                    resultSet.getString("codigo"),
                    resultSet.getString("titulo"),
                    resultSet.getString("artista"),
                    resultSet.getString("genero"),
                    resultSet.getInt("duracion"),
                    resultSet.getInt("canciones"),
                    resultSet.getInt("unidades")
            });
        }
    }
    
    // Metodo para borrar material en la base de datos
    public void borrarContenido(String codigo) throws SQLException {
        modeloTabla.setRowCount(0);
        PreparedStatement statement = conexion.prepareStatement("DELETE FROM cds WHERE codigo = ?");
        statement.setString(1, codigo);
        statement.executeUpdate();
        this.cargarContenido();
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

class AgregarCDGUI extends JFrame {
    private CDsGUI gui;
    private JTextField txtCodigo, txtTitulo, txtArtista, txtGenero, txtDuracion, txtCanciones , txtUnidades;
    private JButton btnAgregar, btnCancelar;

    public AgregarCDGUI(CDsGUI gui) {
        this.gui = gui;
        setTitle("Agregar CD");
        setSize(600, 300);
        setLayout(new GridLayout(4, 2));

        add(new JLabel("Código:"));
        txtCodigo = new JTextField();
        add(txtCodigo);

        add(new JLabel("Título:"));
        txtTitulo = new JTextField();
        add(txtTitulo);

        add(new JLabel("Artista:"));
        txtArtista = new JTextField();
        add(txtArtista);
        
        add(new JLabel("Qué genero es:"));
        txtGenero = new JTextField();
        add(txtGenero);
        
        add(new JLabel("Duración:"));
        txtDuracion = new JTextField();
        add(txtDuracion);
        
        add(new JLabel("Número de canciones:"));
        txtCanciones = new JTextField();
        add(txtCanciones);

        add(new JLabel("Unidades disponibles:"));
        txtUnidades = new JTextField();
        add(txtUnidades);

        btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Validar y agregar el material
                String codigo = txtCodigo.getText();
                String titulo = txtTitulo.getText();
                String artista = txtArtista.getText();
                String genero = txtGenero.getText();
                int duracion = Integer.parseInt(txtDuracion.getText());
                int numero = Integer.parseInt(txtCanciones.getText());
                int unidades = Integer.parseInt(txtUnidades.getText());

                CD cd = new CD(codigo, titulo, artista, genero, duracion, numero, unidades);
                try {
                    gui.agregarContenido(cd);
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

class BuscarCDGUI extends JFrame {
    private CDsGUI gui;
    private JTextField txtBuscar;
    private JButton btnBuscar, btnCancelar;

    public BuscarCDGUI(CDsGUI gui) {
        this.gui = gui;
        setTitle("Buscar CD");
        setSize(600, 100);
        setLayout(new GridLayout(1, 2));

        add(new JLabel("Título:"));
        txtBuscar = new JTextField();
        add(txtBuscar);

        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Validar y agregar el material
                String busqueda = txtBuscar.getText();

                try {
                    gui.buscarContenido(busqueda);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                dispose();
            }
        });
        add(btnBuscar);

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

class BorrarCDGUI extends JFrame {
    private CDsGUI gui;
    private JTextField txtCodigo;
    private JButton btnBuscar, btnCancelar;

    public BorrarCDGUI(CDsGUI gui) {
        this.gui = gui;
        setTitle("Borrar CD");
        setSize(600, 100);
        setLayout(new GridLayout(1, 2));

        add(new JLabel("Código:"));
        txtCodigo = new JTextField();
        add(txtCodigo);

        btnBuscar = new JButton("Borrar");
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Validar y agregar el material
                String codigo = txtCodigo.getText();

                try {
                    gui.borrarContenido(codigo);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                dispose();
            }
        });
        add(btnBuscar);

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

class CD {
    private String codigo;
    private String titulo;
    private String artista;
    private String genero;
    private int duracion;
    private int canciones;
    private int unidades;

    public CD(String codigo, String titulo, String artista, String genero, int duracion, int canciones, int unidades) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.artista = artista;
        this.genero = genero;
        this.duracion = duracion;
        this.canciones = canciones;
        this.unidades = unidades;
        
    }

    public String getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }
    
    public String getArtista() {
        return artista;
    }
    
    public String getGenero() {
        return genero;
    }
    
    public int getDuracion() {
        return duracion;
    }

    public int getCanciones() {
        return canciones;
    }

    public int getUnidades() {
        return unidades;
    }
}
