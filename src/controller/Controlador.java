package controller;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.mongodb.MongoClient;
import model.*;
import org.basex.core.BaseXException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jdom2.JDOMException;

import clases.Arma;
import clases.Personaje;
import vista.Vista;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

public class Controlador {
    private Modelo modelo;
    private final Vista vista = new Vista();
    private HashMap<Integer, Personaje> mapa = new HashMap<>();
    private HashMap<Integer, Arma> mapaArma = new HashMap<>();
    private final Scanner scan = new Scanner(System.in);
    private int tipo = -1;
    private int menu = -1;
    private int entidad = -1;
    private Connection conn;
    private Session s;

    public Controlador() {
        elegirAcceso();
        menu();
    }

    private void menu() {
        while (menu != 0) {
            try {
                mapa = modelo.leer();
                if (tipo > 4) {
                    mapaArma = modelo.leerArma();
                } else {
                    mapaArma = modelo.leerArma(mapa);
                }
                menu = vista.preguntar(scan, "Dígame una opción:\n1.Ver todos\t2.Buscar\t3.Insertar\n4.Modificar\t5.Borrar\t6.Transferir\n7.Cambiar archivo\t0.Salir");
                switch (menu) {
                    case 1:
                        entidad = vista.preguntar(scan, "¿Desea leer personaje(1) o arma(2)?");
                        switch (entidad) {
                            case 1:
                                vista.mostrar(mapa);
                                break;
                            case 2:
                                vista.mostrarArmas(mapaArma);
                                break;
                            default:
                                vista.mostrar("Introduzca un número válido.");
                                break;
                        }
                        break;
                    case 2:
                        int idBuscar = -1;
                        entidad = vista.preguntar(scan, "¿Desea buscar personaje(1) o arma(2)?");
                        switch (entidad) {
                            case 1:
                                while (idBuscar != 0) {
                                    idBuscar = vista.preguntar(scan, "Dígame el ID del personaje a buscar (pulse 0 para retroceder): ");
                                    Personaje personajeBuscado;
                                    if (tipo > 4) {
                                        personajeBuscado = modelo.buscar(idBuscar);
                                    } else {
                                        personajeBuscado = modelo.buscar(mapa, idBuscar);
                                    }
                                    vista.mostrar(personajeBuscado);
                                }
                                break;
                            case 2:
                                while (idBuscar != 0) {
                                    idBuscar = vista.preguntar(scan, "Dígame el ID del arma a buscar (pulse 0 para retroceder): ");
                                    Arma armaBuscada;
                                    if (tipo > 4) {
                                        armaBuscada = modelo.buscarArma(idBuscar);
                                    } else {
                                        armaBuscada = modelo.buscarArma(mapaArma, idBuscar);
                                    }
                                    vista.mostrar(armaBuscada);
                                }
                                break;
                            default:
                                vista.mostrar("Introduzca un número válido.");
                        }
                        break;
                    case 3:
                        try {
                            if (tipo > 4) {
                                entidad = vista.preguntar(scan, "¿Desea añadir personaje(1) o arma(2)?");
                                switch (entidad) {
                                    case 1:
                                        Personaje anadir = generarPersonaje();
                                        modelo.insertar(anadir);
                                        break;
                                    case 2:
                                        Arma anadirArma = generarArma(false);
                                        modelo.insertar(anadirArma);
                                        break;
                                }
                            } else {
                                Personaje anadir = generarPersonaje();
                                modelo.insertar(anadir);
                            }
                        } catch (IOException e) {
                            vista.mostrarError("Error tipo IO. Es posible que su fichero se encuentre vacío");
                        } catch (NumberFormatException nfe) {
                            vista.mostrarError("Tipo de formato no permitido, introduzca un número");
                        } catch (ClassNotFoundException cnfe) {
                            vista.mostrarError("Clase no encontrada");
                        } catch (SQLException sql) {
                            vista.mostrarError("Error de tipo SQL. Consulte posibles problemas en claves foráneas");
                        }
                        break;
                    case 4:
                        try {
                            if (tipo > 4) {
                                entidad = vista.preguntar(scan, "¿Desea modificar personaje(1) o arma(2)?");
                                switch (entidad) {
                                    case 1:
                                        vista.mostrar("Estos son los personajes disponibles:");
                                        vista.mostrar(mapa);
                                        int idModificar = vista.preguntar(scan, "Dígame el id del personaje a modificar: ");
                                        Personaje personajeMod = modelo.buscar(mapa, idModificar);
                                        if (personajeMod == null) {
                                            vista.mostrar("Personaje no encontrado");
                                        } else {
                                            mapa.remove(idModificar);
                                            modificarPersonaje(personajeMod);
                                        }
                                        break;
                                    case 2:
                                        vista.mostrar("Estas son las armas disponibles:");
                                        vista.mostrarArmas(mapaArma);
                                        int idModArma = vista.preguntar(scan, "Dígame el id del arma a modificar: ");
                                        Arma armaMod = modelo.buscarArma(mapaArma, idModArma);
                                        if (armaMod == null) {
                                            vista.mostrar("Arma no encontrada");
                                        } else {
                                            modificarArma(armaMod);
                                        }
                                        break;
                                }
                            } else {
                                int idModificar = vista.preguntar(scan, "Dígame el id del personaje a modificar: ");
                                Personaje personajeMod = modelo.buscar(mapa, idModificar);
                                if (personajeMod == null) {
                                    vista.mostrar("Personaje no encontrado");
                                } else {
                                    modificarPersonaje(personajeMod);
                                }
                            }
                        } catch (IOException e) {
                            vista.mostrarError("Error tipo IO. Es posible que su fichero se encuentre vacío");
                        } catch (ClassNotFoundException e) {
                            vista.mostrarError("Clase no encontrada");
                        } catch (NumberFormatException nfe) {
                            vista.mostrarError("Tipo de formato no permitido, introduzca un número");
                        } catch (SQLException sql) {
                            vista.mostrarError("Error de tipo SQL. Consulte posibles problemas en claves foráneas");
                        }
                        break;
                    case 5:
                        try {
                            if (tipo > 3) {
                                entidad = vista.preguntar(scan, "¿Desea borrar personaje(1) o arma(2)?");
                                switch (entidad) {
                                    case 1:
                                        int idElim = vista.preguntar(scan, "Dígame el id del personaje a eliminar: ");
                                        Personaje personajeElim = modelo.buscar(mapa, idElim);
                                        if (personajeElim == null) {
                                            vista.mostrar("Personaje no encontrado");
                                        } else {
                                            modelo.eliminar(personajeElim.getId());
                                        }
                                        break;
                                    case 2:
                                        int idElimArma = vista.preguntar(scan, "Dígame el id del arma a eliminar: ");
                                        Arma armaElim = modelo.buscarArma(mapaArma, idElimArma);
                                        if (armaElim == null) {
                                            vista.mostrar("Arma no encontrada");
                                        } else {
                                            modelo.eliminarArma(armaElim.getId());
                                        }
                                        break;
                                }
                            } else {
                                int idElim = vista.preguntar(scan, "Dígame el id del personaje a eliminar: ");
                                Personaje pElimi = modelo.buscar(mapa, idElim);
                                if (pElimi == null) {
                                    vista.mostrar("Personaje no encontrado");
                                } else {
                                    modelo.eliminar(mapa, pElimi);
                                }
                            }
                        } catch (IOException e) {
                            vista.mostrarError("Error tipo IO. Es posible que su fichero se encuentre vacío");
                        } catch (NumberFormatException nfe) {
                            vista.mostrarError("Tipo de formato no permitido, introduzca un número");
                        } catch (SQLException sql) {
                            vista.mostrarError("Error de tipo SQL. Consulte posibles problemas en claves foráneas");
                        } catch (NullPointerException npe) {
                            vista.mostrarError("Archivo no encontrado");
                        }
                        break;
                    case 6:
                        try {
                            if (tipo > 4) {
                                entidad = vista.preguntar(scan, "¿Desea transferir personaje(1) o arma(2)?");
                                switch (entidad) {
                                    case 1:
                                        exportarPersonajes();
                                        break;
                                    case 2:
                                        exportarArmas();
                                        break;
                                }
                            } else {
                                exportarPersonajes();
                            }
                        } catch (IOException e) {
                            vista.mostrarError("Error tipo IO. Es posible que su fichero se encuentre vacío");
                        } catch (ClassNotFoundException e) {
                            vista.mostrarError("Clase no encontrada");
                        } catch (NumberFormatException nfe) {
                            vista.mostrarError("Tipo de formato no permitido, introduzca un número");
                        } catch (NullPointerException npe) {
                            vista.mostrarError("Archivo no encontrado");
                        } catch (JDOMException jdome) {
                            vista.mostrarError("Error tipo JDOM");
                        } catch (SQLException sql) {
                            vista.mostrarError("Error de tipo SQL. Consulte posibles problemas en claves foráneas");
                        }
                        break;
                    case 7:
                        elegirAcceso();
                        break;
                    case 0:
                        if (conn != null)
                            conn.close();
                        if (s != null)
                            s.close();
                        vista.mostrar("Nos vemos pronto");
                        break;
                    default:
                        vista.mostrar("Introduzca un número válido.");
                        break;
                }
            } catch (NumberFormatException nfe) {
                vista.mostrarError("Tipo de formato no permitido, introduzca un número");
            } catch (SQLException e) {
                vista.mostrarError("Error de tipo SQL. Consulte posibles problemas en claves foráneas");
            } catch (ClassNotFoundException e1) {
                vista.mostrarError("Clase no encontrada");
            } catch (EOFException e1) {
                vista.mostrarError("Error EOFE");
            } catch (IOException e1) {
                vista.mostrarError("Error de tipo IO");
            } catch (JDOMException e1) {
                vista.mostrarError("Error de tipo JDOM");
            } catch (PersistenceException persistenceException) {
                vista.mostrarError("Ha ocurrido un error relacionado con la persistencia");
            }
        }
    }

    private void modificarPersonaje(Personaje personajeAux) throws IOException, ClassNotFoundException, SQLException {
        int campo = vista.preguntar(scan, "¿Qué desea cambiar?\n1. nombre\t2. rareza\n3. arma\t4. elemento");
        String valor = "";
        Personaje auxiliar = personajeAux;
        int idPrimera = personajeAux.getId();
        switch (campo) {
            case 1:
                boolean repetido = true;
                String nombre = "";
                if (mapa != null) {
                    while (repetido) {
                        nombre = vista.preguntarS(scan, "Dígame el nombre: ");
                        repetido = modelo.comprobarNombre(nombre, mapa);
                        if (repetido) {
                            vista.mostrar("Su nombre  '" + nombre + "' ya está siendo utilizado.");
                        }
                    }
                }
                valor = nombre;
                personajeAux.setNombre(nombre);
                break;
            case 2:
                int rareza = vista.preguntar(scan, "Nueva rareza: ");
                valor = String.valueOf(rareza);
                personajeAux.setRareza(rareza);
                break;
            case 3:
                Arma arma = generarArma(true);
                personajeAux.setArma(arma);
                assert arma != null;
                valor = String.valueOf(arma.getId());
                break;
            case 4:
                String elemento = vista.preguntarS(scan, "Nuevo elemento: ");
                personajeAux.setElemento(elemento);
                valor = elemento;
                break;
        }
        if (tipo == 6) {
            modelo.modificar(auxiliar, personajeAux);
            return;
        }
        if (tipo > 3) {
            modelo.modificar(idPrimera, campo, valor);
        } else {
            modelo.modificar(mapa, personajeAux);
        }
    }

    private void modificarArma(Arma arma) throws IOException, ClassNotFoundException, SQLException {
        int campo = vista.preguntar(scan, "¿Qué desea cambiar?\n1. nombre\t2. rareza");
        String valor = "";
        int idPrimera = arma.getId();
        switch (campo) {
            case 1:
                boolean repetido = true;
                String nombre = "";
                while (repetido) {
                    nombre = vista.preguntarS(scan, "Dígame el nombre del arma: ");
                    repetido = modelo.comprobarNombre(nombre, mapa);
                    if (repetido) {
                        vista.mostrar("El nombre  '" + nombre + "' ya está siendo utilizado.");
                    }
                }
                valor = nombre;
                break;
            case 2:
                int rareza = vista.preguntar(scan, "Introduzca la rareza: ");
                valor = String.valueOf(rareza);
                break;
        }
        modelo.modificarArma(idPrimera, campo, valor);
    }

    private void exportarPersonajes() throws IOException, ClassNotFoundException, JDOMException, SQLException {
        int tipo2 = vista.preguntar(scan, "Diga el tipo de archivo al que desea exportar:\n1.Texto\t2.Binario\t3.XML\t4.XQuery\n5.MySQL\t6.SQLite\t7.Hibernate\n8.PHP\t9.Objetos\t10.Mongo");
        Modelo modelo2;
        File archivo;
        switch (tipo2) {
            case 1:
                archivo = new File("ficheros\\personajes.txt");
                archivo.createNewFile();
                modelo2 = new ModeloTXT(archivo);
                modelo2.escribir(mapa);
                break;
            case 2:
                modelo2 = new ModeloBIN(new File("ficheros\\personajes.dat"));
                modelo2.escribir(mapa);
                break;
            case 3:
                modelo2 = new ModeloXML(new File("ficheros\\personajes.xml"));
                modelo2.escribir(mapa);
                break;
            case 4:
                archivo = new File("ficheros\\personajes.xml");
                archivo.createNewFile();
                modelo2 = new ModeloXQuery("ficheros/personajes.xml");
                modelo2.escribir(mapa);
                break;
            case 5:
                String database = "bdpersonajes";
                String hostname = "localhost";
                String port = "3306";
                String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=false";
                String username = "root";
                String password = "root";
                conn = DriverManager.getConnection(url, username, password);
                conn.setClientInfo("Tipo", "MySQL");
                modelo2 = new ModeloBD(conn);
                modelo2.escribir(mapa, mapaArma);
                break;
            case 6:
                conn = DriverManager.getConnection("jdbc:sqlite:ficheros\\bdpersonajes.db");
                modelo2 = new ModeloSQLite(conn);
                modelo2.escribir(mapa, mapaArma);
                break;
            case 7:
                SessionFactory sf = new Configuration().configure().buildSessionFactory();
                s = sf.openSession();
                modelo2 = new ModeloHB(s);
                modelo2.escribir(mapa, mapaArma);
                break;
            case 8:
                modelo2 = new ModeloPHP();
                modelo2.escribir(mapa, mapaArma);
            case 10:
                MongoClient mongoClient = new MongoClient("localhost", 27017);
                modelo2 = new ModeloMongo(mongoClient);
                modelo2.escribir(mapa, mapaArma);
                break;
            default:
                vista.mostrar("Introduzca un número válido.");
                break;
        }
    }

    private void exportarArmas()
            throws IOException, ClassNotFoundException, JDOMException, SQLException {
        int tipo2 = vista.preguntar(scan, "Dígame la bd a donde quiera transferir sus armas:\n1.MySQL\t2.SQLite\t3.Hibernate\n4.PHP\t5.Mongo");
        Modelo modelo2;
        switch (tipo2) {
            case 1:
                String database = "bdpersonajes";
                String hostname = "localhost";
                String port = "3306";
                String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=false";
                String username = "root";
                String password = "root";
                conn = DriverManager.getConnection(url, username, password);
                conn.setClientInfo("Tipo", "MySQL");
                modelo2 = new ModeloBD(conn);
                modelo2.escribirArma(mapaArma);
            case 2:
                conn = DriverManager.getConnection("jdbc:sqlite:ficheros\\bdpersonajes.db");
                modelo2 = new ModeloSQLite(conn);
                modelo2.escribirArma(mapaArma);
            case 3:
                SessionFactory sf = new Configuration().configure().buildSessionFactory();
                s = sf.openSession();
                modelo2 = new ModeloHB(s);
                modelo2.escribirArma(mapaArma);
            case 4:
                break;
            case 5:
                MongoClient mongoClient = new MongoClient("localhost", 27017);
                modelo2 = new ModeloMongo(mongoClient);
                modelo2.escribir(mapa);
                break;
            default:
                vista.mostrar("Introduzca un número válido.");
        }
    }

    private void elegirAcceso() throws NumberFormatException, NullPointerException {
        tipo = -1;
        File archivo;
        while (tipo > 10 || tipo < 1) {
            try {
                tipo = vista.preguntar(scan, "Diga el tipo de archivo que desee manipular:\n1.Texto\t2.Binario\t3.XML\t4.XQuery\n5.MySQL\t6.SQLite\t7.Hibernate\n8.PHP\t9.Objetos\t10.Mongo");
                switch (tipo) {
                    case 1:
                        archivo = new File("ficheros\\personajes.txt");
                        archivo.createNewFile();
                        modelo = new ModeloTXT(archivo);
                        break;
                    case 2:
                        archivo = new File("ficheros\\personajes.dat");
                        archivo.createNewFile();
                        modelo = new ModeloBIN(archivo);
                        break;
                    case 3:
                        archivo = new File("ficheros\\personajes.xml");
                        archivo.createNewFile();
                        modelo = new ModeloXML(archivo);
                        break;
                    case 4:
                        archivo = new File("ficheros\\personajes.xml");
                        archivo.createNewFile();
                        modelo = new ModeloXQuery("ficheros/personajes.xml");
                        break;
                    case 5:
                        String database = "bdpersonajes";
                        String hostname = "localhost";
                        String port = "3306";
                        String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=false";
                        String username = "root";
                        String password = "root";
                        conn = DriverManager.getConnection(url, username, password);
                        modelo = new ModeloBD(conn);
                        System.out.println("Creado modelo BD");
                        break;
                    case 6:
                        conn = DriverManager.getConnection("jdbc:sqlite:ficheros\\bdpersonajes.db");
                        modelo = new ModeloBD(conn);
                        break;
                    case 7:
                        SessionFactory sf = new Configuration().configure().buildSessionFactory();
                        s = sf.openSession();
                        modelo = new ModeloHB(s);
                        break;
                    case 8:
                        modelo = new ModeloPHP();
                        break;
                    case 9:
                        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ficheros/personajes.odb");
                        EntityManager em = emf.createEntityManager();
                        modelo = new ModeloOBJ(em);
                        break;
                    case 10:
                        MongoClient mongoClient = new MongoClient("localhost", 27017);
                        modelo = new ModeloMongo(mongoClient);
                        break;
                    default:
                        vista.mostrar("Introduzca un número válido.");
                        break;
                }
            } catch (NumberFormatException e) {
                vista.mostrarError("Tipo de formato no permitido, introduzca un número");
            } catch (NullPointerException ioe) {
                vista.mostrarError("Error tipo IO. Es posible que su fichero se encuentre vacío");
            } catch (SQLException sqle) {
                vista.mostrarError("Error de tipo SQL. Consulte posibles problemas en claves foráneas");
            } catch (BaseXException bx) {
                vista.mostrarError("Error con BaseX");
            } catch (IOException e) {
                vista.mostrarError("Erorr tipo IO");
            }
        }
    }

    private Personaje generarPersonaje() throws IOException, ClassNotFoundException, SQLException {
        int id = 1, rareza;
        for (Map.Entry<Integer, Personaje> entry : mapa.entrySet()) {
            if (entry.getKey() != id) {
                break;
            }
            id++;
        }
        String nombre = "", elemento = "";
        boolean repetido = true;
        while (repetido) {
            nombre = vista.preguntarS(scan, "Dígame el nombre: ");
            repetido = modelo.comprobarNombre(nombre, mapa);
            if (repetido) {
                vista.mostrar("Su nombre  '" + nombre + "' ya está siendo utilizado.");
            }
        }
        rareza = vista.preguntar(scan, "Introduzca la rareza: ");
        boolean salir = true;
        while (salir) {
            switch (vista.preguntar(scan, "Dígame el elemento: \n1.Pyro 2.Hydro 3.Electro 4.Cryo\n\t5.Geo 6.Dendro 7.Anemo")) {
                case 1:
                    elemento = "Pyro";
                    salir = false;
                    break;
                case 2:
                    elemento = "Hydro";
                    salir = false;
                    break;
                case 3:
                    elemento = "Electro";
                    salir = false;
                    break;
                case 4:
                    elemento = "Cryo";
                    salir = false;
                    break;
                case 5:
                    elemento = "Geo";
                    salir = false;
                    break;
                case 6:
                    elemento = "Dendro";
                    salir = false;
                    break;
                case 7:
                    elemento = "Anemo";
                    salir = false;
                    break;
                default:
                    vista.mostrar("Seleccione un número del 1 al 7");
                    break;
            }
        }
        Arma arma = generarArma(true);
        return new Personaje(id, nombre, rareza, arma, elemento);
    }

    private Arma generarArma(boolean preguntar) throws IOException, ClassNotFoundException, SQLException {
        Arma arma = null;
        int idArma = 1, rareza;
        for (Map.Entry<Integer, Personaje> entry : mapa.entrySet()) {
            if (entry.getKey() != idArma) {
                break;
            }
            idArma++;
        }
        String nombre = "";
        boolean repetido = true;
        int pArma;
        if (preguntar) {
            pArma = vista.preguntar(scan, "¿Desea añadir una nueva arma(1) o usar una existente(2)?");
        } else {
            pArma = 1;
        }
        switch (pArma) {
            case 1:
                if (mapaArma != null) {
                    while (repetido) {
                        nombre = vista.preguntarS(scan, "Dígame el nombre: ");
                        repetido = modelo.comprobarNombreArma(nombre, mapaArma);
                        if (repetido) {
                            vista.mostrar("Su nombre  '" + nombre + "' ya está siendo utilizado.");
                        }
                    }
                } else {
                    nombre = vista.preguntarS(scan, "Dígame el nombre: ");
                }
                rareza = vista.preguntar(scan, "Introduzca la rareza: ");
                arma = new Arma(idArma, nombre, rareza);
                modelo.insertar(arma);
                return arma;
            case 2:
                while (true) {
                    vista.mostrar("Estas son las armas disponibles: ");
                    vista.mostrarArmas(mapaArma);
                    idArma = vista.preguntar(scan, "Dígame la ID del arma:");
                    arma = modelo.buscarArma(mapaArma, idArma);
                    if (arma == null) {
                        vista.mostrar("Esa ID no está disponible");
                    } else {
                        return arma;
                    }
                }
            default:
                vista.mostrar("Introduzca un número válido.");
                break;
        }
        return null;
    }
}
