package model;

import clases.Arma;
import clases.Personaje;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.Open;
import org.basex.core.cmd.XQuery;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ModeloXQuery extends Modelo {
    private final Context context;

    public ModeloXQuery(String rutaArchivo) throws BaseXException {
        context = new Context();
        try {
            new CreateDB("personajes", rutaArchivo).execute(context);
            new Open("personajes").execute(context);
        } catch (Exception e) {
            new CreateDB("personajes", rutaArchivo).execute(context);
            new Open("personajes").execute(context);
        }
    }

    public HashMap<Integer, Personaje> leer() throws IOException, JDOMException {
        String query = "<personajes> {//personaje} </personajes>";
        String datosConsulta = new XQuery(query).execute(context);
        HashMap<Integer, Personaje> mapa = new HashMap<>();
        SAXBuilder saxBuilder = new SAXBuilder();
        InputStream fichero = new ByteArrayInputStream(datosConsulta.getBytes());
        if (fichero.available() != 0) {
            Document document = saxBuilder.build(fichero);
            Element classElement = document.getRootElement();
            List<Element> lista = classElement.getChildren();
            for (Element personaje : lista) {
                Arma arma = new Arma(Integer.parseInt(personaje.getChild("arma").getChildText("id")), personaje.getChild("arma").getChildText("nombre"), Integer.parseInt(personaje.getChild("arma").getChildText("rareza")));
                mapa.put(Integer.parseInt(personaje.getAttributeValue("id")),
                        new Personaje(Integer.parseInt(personaje.getAttributeValue("id")), personaje.getChildText("nombre"),
                                Integer.parseInt(personaje.getChildText("rareza")), arma, personaje.getChildText("elemento")));
            }
        }
        return mapa;
    }

    public void insertar(Personaje p) throws IOException {
        Attribute id = new Attribute("id", String.valueOf(p.getId()));
        Element nombre = new Element("nombre").addContent(p.getNombre());
        Element rareza = new Element("rareza").addContent(String.valueOf(p.getRareza()));
        Element elemento = new Element("elemento").addContent(p.getElemento());
        Element arma = new Element("arma");
        Element idArma = new Element("id").addContent(String.valueOf(p.getArma().getId()));
        Element nombreArma = new Element("nombre").addContent(p.getArma().getNombre());
        Element rarezaArma = new Element("rareza").addContent(String.valueOf(p.getArma().getRareza()));
        arma.addContent(idArma);
        arma.addContent(nombreArma);
        arma.addContent(rarezaArma);
        Element personaje = new Element("personaje");
        personaje.setAttribute(id);
        personaje.addContent(nombre);
        personaje.addContent(rareza);
        personaje.addContent(arma);
        personaje.addContent(elemento);

        XMLOutputter xmlOut = new XMLOutputter();
        String formateado = xmlOut.outputString(personaje);

        String queryInsert = "insert node " + formateado + " into /personajes";
        new XQuery(queryInsert).execute(context);

    }

    public void modificar(int id, int campo, String valor) throws BaseXException {
        String campoModificar = switch (campo) {
            case 1 -> "nombre";
            case 2 -> "rareza";
            case 3 -> "arma";
            case 4 -> "elemento";
            default -> "";
        };
        String query = "replace value of node //personaje[@id = " + id + "]/ " + campoModificar + " with '" + valor + "'";
        new XQuery(query).execute(context);
    }

    public void eliminar(int id) throws IOException {
        String query = "delete node //personaje[@id = " + id + "]";
        new XQuery(query).execute(context);
    }

    public void escribir(HashMap<Integer, Personaje> mapa) throws IOException {
        new XQuery("delete node personajes").execute(context);
        Personaje p;
        Element personajes = new Element("personajes");
        Element personaje = null;
        for (Map.Entry<Integer, Personaje> entrada : mapa.entrySet()) {
            int clave = entrada.getKey();
            p = entrada.getValue();
            personaje = new Element("personaje");
            personajes.addContent(personaje);
            Attribute attr = new Attribute("id", String.valueOf(clave));
            personaje.setAttribute(attr);
            Element nombre = new Element("nombre");
            nombre.setText(p.getNombre());
            personaje.addContent(nombre);
            Element rareza = new Element("rareza");
            personaje.addContent(rareza);
            rareza.setText(String.valueOf(p.getRareza()));
            //para escribir arma
            Element arma = new Element("arma");
            Element idArma = new Element("id");
            idArma.setText(String.valueOf(p.getArma().getId()));
            arma.addContent(idArma);
            Element nombreArma = new Element("nombre");
            nombreArma.setText(p.getArma().getNombre());
            arma.addContent(nombreArma);
            Element rarezaArma = new Element("rareza");
            rarezaArma.setText(String.valueOf(p.getArma().getRareza()));
            arma.addContent(rarezaArma);
            personaje.addContent(arma);
            Element element = new Element("elemento");
            element.setText(String.valueOf(p.getElemento()));
            personaje.addContent(element);
        }

        XMLOutputter xmlOut = new XMLOutputter();
        String formateado = xmlOut.outputString(personaje);

        String queryInsert = "insert node " + formateado + " into /personajes";
        new XQuery(queryInsert).execute(context);
    }


}
