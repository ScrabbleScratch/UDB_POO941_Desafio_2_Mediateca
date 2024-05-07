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
        setTitle("Mediateca          **  Sistema de Revistas  **");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1360, 700);

        // Panel de botones
        panelMenu = new JPanel();
        panelMenu.setLayout(new GridLayout(6, 1));

        btnAgregar = new JButton("Agregar Revista");
        btnModificar = new JButton("Modificar Revista");
        btnListar = new JButton("Listar Revistas Disponibles");
        btnBorrar = new JButton("Borrar Revista");
        btnBuscar = new JButton("Buscar Revista");
        btnSalir = new JButton("Regresar");

        btnAgregar.addActionListener((ActionEvent e) -> {
            // Abre la ventana para agregar material
            AgregarRevistaGUI agregarRevistaGUI = new AgregarRevistaGUI(RevistasGUI.this);
        });
        
        btnModificar.addActionListener((ActionEvent e) -> {
            // Abre la ventana para agregar material
            ModificarRevistaGUI modificarRevistaGUI = new ModificarRevistaGUI(RevistasGUI.this);
        });
        
        btnListar.addActionListener((ActionEvent e) -> {
            try {
                cargarContenido();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        
        btnBorrar.addActionListener((ActionEvent e) -> {
            BorrarRevistaGUI borrarRevistaGUI = new BorrarRevistaGUI(RevistasGUI.this);
        });
        
        btnBuscar.addActionListener((ActionEvent e) -> {
            // Abre la ventana para buscar material
            BuscarRevistaGUI buscarRevistaGUI = new BuscarRevistaGUI(RevistasGUI.this);
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

    // Método para modificar un material a la base de datos y actualizar la tabla
    public void modificarContenido(Revista revista) throws SQLException {
        PreparedStatement statement = conexion.prepareStatement(
            "UPDATE revistas SET titulo = ?, editorial = ?, periodicidad = ?, fecha_publicacion = ?, unidades = ? " +
                "WHERE codigo = ? LIMIT 1");
        statement.setString(1, revista.getTitulo());
        statement.setString(2, revista.getEditorial());
        statement.setString(3, revista.getPeriodicidad());
        statement.setString(4, revista.getPublicacion());
        statement.setInt(5, revista.getUnidades());
        statement.setString(6, revista.getCodigo());
        statement.executeUpdate();
        cargarContenido();
    }

    // Metodo para buscar material en la base de datos
    public void buscarContenido(String busqueda) throws SQLException {
        modeloTabla.setRowCount(0);
        PreparedStatement statement = conexion.prepareStatement("SELECT * FROM revistas WHERE titulo = ?");
        statement.setString(1, busqueda);
        ResultSet resultSet = statement.executeQuery();
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
    
    // Metodo para borrar material en la base de datos
    public void borrarContenido(String codigo) throws SQLException {
        modeloTabla.setRowCount(0);
        PreparedStatement statement = conexion.prepareStatement("DELETE FROM revistas WHERE codigo = ?");
        statement.setString(1, codigo);
        statement.executeUpdate();
        this.cargarContenido();
    }
    
    // Metodo para obtener data de material en la base de datos
    public ResultSet getContenido(String codigo) throws SQLException {
        PreparedStatement statement = conexion.prepareStatement("SELECT * FROM revistas WHERE codigo = ?");
        statement.setString(1, codigo);
        ResultSet resultSet = statement.executeQuery();
        return resultSet;
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

class ModificarRevistaGUI extends JFrame {
    private RevistasGUI gui;
    private JTextField txtCodigo, txtTitulo, txtEditorial, txtPeriodicidad, txtPublicacion, txtUnidades;
    private JButton btnBuscar, btnModificar, btnCancelar;

    public ModificarRevistaGUI(RevistasGUI gui, Revista revista) {
        setTitle("Editar Revista");
        setSize(600, 250);
        setLayout(new GridLayout(4, 2));

        add(new JLabel("Código:"));
        add(new JLabel(revista.getCodigo()));

        add(new JLabel("Título:"));
        txtTitulo = new JTextField(revista.getTitulo());
        add(txtTitulo);

        add(new JLabel("Editorial:"));
        txtEditorial = new JTextField(revista.getEditorial());
        add(txtEditorial);

        add(new JLabel("Periodicidad:"));
        txtPeriodicidad = new JTextField(revista.getPeriodicidad());
        add(txtPeriodicidad);

        add(new JLabel("Fecha de publicación:"));
        txtPublicacion = new JTextField(revista.getPublicacion());
        add(txtPublicacion);

        add(new JLabel("Unidades disponibles:"));
        txtUnidades = new JTextField(Integer.toString(revista.getUnidades()));
        add(txtUnidades);

        btnModificar = new JButton("Modificar");
        btnModificar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Validar y agregar el material
                String codigo = revista.getCodigo();
                String titulo = txtTitulo.getText();
                String editorial = txtEditorial.getText();
                String periodicidad = txtPeriodicidad.getText();
                String publicacion = txtPublicacion.getText();
                int unidades = Integer.parseInt(txtUnidades.getText());

                Revista revista = new Revista(codigo, titulo, editorial, periodicidad, publicacion, unidades);
                try {
                    gui.modificarContenido(revista);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                dispose();
            }
        });
        add(btnModificar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        add(btnCancelar);

        setVisible(true);
    }
    
    public ModificarRevistaGUI(RevistasGUI gui) {
        this.gui = gui;
        setTitle("Buscar Revista");
        setSize(600, 100);
        setLayout(new GridLayout(1, 2));

        add(new JLabel("Código:"));
        txtCodigo = new JTextField();
        add(txtCodigo);

        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Validar y agregar el material
                String codigo = txtCodigo.getText();

                try {
                    ResultSet results = gui.getContenido(codigo);
                    if (results.next()) {
                        Revista revista = new Revista(
                            results.getString("codigo"),
                            results.getString("titulo"),
                            results.getString("editorial"),
                            results.getString("periodicidad"),
                            results.getString("fecha_publicacion"),
                            results.getInt("unidades")
                        );
                        new ModificarRevistaGUI(gui, revista);
                    }
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

class BuscarRevistaGUI extends JFrame {
    private RevistasGUI gui;
    private JTextField txtBuscar;
    private JButton btnBuscar, btnCancelar;

    public BuscarRevistaGUI(RevistasGUI gui) {
        this.gui = gui;
        setTitle("Buscar Revista");
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

class BorrarRevistaGUI extends JFrame {
    private RevistasGUI gui;
    private JTextField txtBorrar;
    private JButton btnBuscar, btnCancelar;

    public BorrarRevistaGUI(RevistasGUI gui) {
        this.gui = gui;
        setTitle("Borrar Revista");
        setSize(600, 100);
        setLayout(new GridLayout(1, 2));

        add(new JLabel("Código:"));
        txtBorrar = new JTextField();
        add(txtBorrar);

        btnBuscar = new JButton("Borrar");
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Validar y agregar el material
                String codigo = txtBorrar.getText();

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