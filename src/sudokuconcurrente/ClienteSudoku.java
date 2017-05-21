/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudokuconcurrente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SAMAEL
 */
public class ClienteSudoku implements Runnable {

    private String hostname = "";
    private int port = 0;
    private String encoding = "";
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private Thread theThread = null;
    private String[] aux;
    private FrameSudoku frame;

    public ClienteSudoku(String hostname, int port, String encoding) {
        this.hostname = hostname;
        this.port = port;
        this.encoding = encoding;
        open();
    }

    public void open() {
        try {
            socket = new Socket(this.hostname, this.port);
            out = new PrintWriter(this.socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), this.encoding));
            theThread = new Thread(this);
            theThread.start();
        } catch (IOException ex) {
            Logger.getLogger(ClienteSudoku.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void close() {
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ClienteSudoku.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writer(String message) {
        getOut().println(message);
    }

    @Override
    public void run() {
        String redline = "";
        boolean quit = true;
        while (quit) {
            try {

                //aca recibo las respuestas del servidor para el metodo 1
                redline = getIn().readLine() + "\n";
                System.out.println("ReadLine" + redline);

                String aux[] = redline.split("\n");
                
                for (int i = 0; i < aux.length; i++) {
                    if (aux[i].startsWith("<OK>")) {
                        System.out.println("" + redline);

                        String[] cadena;
                        cadena = aux[i].substring(4).split(",");
                        int fila = Integer.parseInt(cadena[0]);
                        int columna = Integer.parseInt(cadena[1]);
                        int valor = -1;
                        try {
                            valor = Integer.parseInt(cadena[2].trim());
                        } catch (Exception e) {
                            System.out.println("exception" + e);
                        }

                        if (valor != -1) {
                            frame.agregarAMatriz(fila, columna, valor);
                        }
                    }

                }

            } catch (IOException e) {
                Logger.getLogger(ClienteSudoku.class.getName()).log(Level.SEVERE, null, e);

                if (redline == null) {
                    quit = false;
                }
            }

        }

    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public Thread getTheThread() {
        return theThread;
    }

    public void setTheThread(Thread theThread) {
        this.theThread = theThread;
    }

    public String[] getAux() {
        return aux;
    }

    public void setAux(String[] aux) {
        this.aux = aux;
    }

    void recibirFrame(FrameSudoku frame) {
        this.frame = frame;
    }

}
