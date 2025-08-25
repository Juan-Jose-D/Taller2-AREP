package com.arep.clase;
import java.net.*;
/**
 *
 * @author juan.dgomez
 */
public class URLParser {
    public static void main(String[] args) throws MalformedURLException{
        URL myurl = new URL("http://arep.cursos.escuelaing.edu.co:4200/respuestasexamenes/parcial1.html/?id=200/#Juan");
        System.out.println("Protocol: " + myurl.getProtocol());
        System.out.println("Authority: " + myurl.getAuthority());
        System.out.println("Host: " + myurl.getHost());
        System.out.println("Port: " + myurl.getPort());
        System.out.println("Path: " + myurl.getPath());
        System.out.println("Query: " + myurl.getQuery());
        System.out.println("File: " + myurl.getFile());
        System.out.println("Ref: " + myurl.getRef());
    }
}
