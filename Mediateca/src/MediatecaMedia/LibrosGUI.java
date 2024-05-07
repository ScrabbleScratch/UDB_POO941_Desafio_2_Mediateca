package MediatecaMedia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import MediatecaDB.ConexionDB;

public class LibrosGUI extends JFrame {
    private JPanel panelMenu;
    private JButton btnAgregar, btnModificar, btnListar, btnBorrar, btnBuscar, btnSalir;
    private JTable tablaMateriales;
    private DefaultTableModel modeloTabla;
    private Connection conexion;

    public LibrosGUI() {
        setTitle("Mediateca          **  Sistema de Libros  **");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(1360, 700);

        // Panel de botones
        panelMenu = new JPanel();
        panelMenu.setLayout(new GridLayout(6, 1));

        btnAgregar = new JButton("Agregar Libro");
        btnModificar = new JButton("Modificar Libro");
        btnListar = new JButton("Listar Libros Disponibles");
        btnBorrar = new JButton("Borrar Libro");
        btnBuscar = new JButton("Buscar Libro");
        btnSalir = new JButton("Regresar");

        btnAgregar.addActionListener((ActionEvent e) -> {
            // Abre la ventana para agregar material
            AgregarLibroGUI agregarLibroGUI = new AgregarLibroGUI(LibrosGUI.this);
        });

        btnListar.addActionListener((ActionEvent e) -> {
            try {
                cargarContenido();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        
        btnBuscar.addActionListener((ActionEvent e) -> {
            // Abre la ventana para buscar material
            BuscarLibroGUI buscarLibroGUI = new BuscarLibroGUI(LibrosGUI.this);
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
        modeloTabla.addColumn("Autor");
        modeloTabla.addColumn("Páginas");
        modeloTabla.addColumn("Editorial");
        modeloTabla.addColumn("ISBN");
        modeloTabla.addColumn("Año de publicación");
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
        ResultSet resultSet = statement.executeQuery("SELECT * FROM libros");
        while (resultSet.next()) {
            modeloTabla.addRow(new Object[]{
                    resultSet.getString("codigo"),
                    resultSet.getString("titulo"),
                    resultSet.getString("autor"),
                    resultSet.getString("paginas"),
                    resultSet.getString("editorial"),
                    resultSet.getString("isbn"),
                    resultSet.getString("anio_publicacion"),
                    resultSet.getInt("unidades")
            });
        }
    }

    // Método para agregar un material a la base de datos y actualizar la tabla
    public void agregarContenido(Libro libro) throws SQLException {
        PreparedStatement statement = conexion.prepareStatement(
                "INSERT INTO libros (codigo, titulo, autor, paginas, editorial, isbn, anio_publicacion, unidades) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        statement.setString(1, libro.getCodigo());
        statement.setString(2, libro.getTitulo());
        statement.setString(3, libro.getAutor());
        statement.setInt(4, libro.getPaginas());
        statement.setString(5, libro.getEditorial());
        statement.setString(6, libro.getIsbn());
        statement.setInt(7, libro.getPublicacion());
        statement.setInt(8, libro.getUnidades());
        statement.executeUpdate();
        cargarContenido();
    }

    // Metodo para buscar material en la base de datos
    public void buscarContenido(String busqueda) throws SQLException {
        modeloTabla.setRowCount(0);
        PreparedStatement statement = conexion.prepareStatement("SELECT * FROM libros WHERE titulo = ?");
        statement.setString(1, busqueda);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            modeloTabla.addRow(new Object[]{
                    resultSet.getString("codigo"),
                    resultSet.getString("titulo"),
                    resultSet.getString("autor"),
                    resultSet.getString("paginas"),
                    resultSet.getString("editorial"),
                    resultSet.getString("isbn"),
                    resultSet.getString("anio_publicacion"),
                    resultSet.getInt("unidades")
            });
        }
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

class AgregarLibroGUI extends JFrame {
    private LibrosGUI gui;
    private JTextField txtCodigo, txtTitulo, txtAutor, txtPaginas, txtEditorial, txtIsbn, txtPublicacion, txtUnidades;
    private JButton btnAgregar, btnCancelar;

    public AgregarLibroGUI(LibrosGUI gui) {
        this.gui = gui;
        setTitle("Agregar Libro");
        setSize(600, 300);
        setLayout(new GridLayout(5, 2));

        add(new JLabel("Código:"));
        txtCodigo = new JTextField();
        add(txtCodigo);

        add(new JLabel("Título:"));
        txtTitulo = new JTextField();
        add(txtTitulo);
        
        add(new JLabel("Autor:"));
        txtAutor = new JTextField();
        add(txtAutor);
        
        add(new JLabel("Páginas:"));
        txtPaginas = new JTextField();
        add(txtPaginas);

        add(new JLabel("Editorial:"));
        txtEditorial = new JTextField();
        add(txtEditorial);
        
        add(new JLabel("ISBN:"));
        txtIsbn = new JTextField();
        add(txtIsbn);
        
        add(new JLabel("Año de publicación:"));
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
                String autor = txtAutor.getText();
                int paginas = Integer.parseInt(txtPaginas.getText());
                String editorial = txtEditorial.getText();
                String isbn = txtIsbn.getText();
                int publicacion = Integer.parseInt(txtPublicacion.getText());
                int unidades = Integer.parseInt(txtUnidades.getText());

                Libro libro = new Libro(codigo, titulo, autor, paginas, editorial, isbn, publicacion, unidades);
                try {
                    gui.agregarContenido(libro);
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

class BuscarLibroGUI extends JFrame {
    private LibrosGUI gui;
    private JTextField txtBuscar;
    private JButton btnBuscar, btnCancelar;

    public BuscarLibroGUI(LibrosGUI gui) {
        this.gui = gui;
        setTitle("Buscar Libro");
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

class Libro {
    private String codigo;
    private String titulo;
    private String autor;
    private int paginas;
    private String editorial;
    private String isbn;
    private int publicacion;
    private int unidades;

    public Libro(String codigo, String titulo, String autor, int paginas, String editorial, String isbn, int publicacion, int unidades) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.autor = autor;
        this.paginas = paginas;
        this.editorial = editorial;
        this.isbn = isbn;
        this.publicacion = publicacion;
        this.unidades = unidades;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }
    
    public int getPaginas() {
        return paginas;
    }

    public String getEditorial() {
        return editorial;
    }

    public String getIsbn() {
        return isbn;
    }
    
    public int getPublicacion() {
        return publicacion;
    }

    public int getUnidades() {
        return unidades;
    }
}
