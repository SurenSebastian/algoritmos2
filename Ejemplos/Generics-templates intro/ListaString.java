import java.util.Scanner;

abstract class Lista {
    abstract void insertar(String el);
    abstract void eliminar(String el);
    abstract void imprimir();
};

class NodoLista {
    public String dato;
    public NodoLista sig;
    public NodoLista() {};
    public NodoLista(String unDato, NodoLista unSig) {
        dato = unDato;
        sig = unSig;
    };
    public NodoLista(String unDato) {
        dato = unDato;
        sig = null;
    };
};

class ListaImp extends Lista {
    private NodoLista ppio;

    public ListaImp() {
        ppio = null;
    }

    public void insertar(String el) {
        this.ppio = new NodoLista(el, this.ppio);
    }
    public void eliminar(String el) {
        if(ppio != null) {
            if(ppio.dato == el) {
                ppio = ppio.sig;
            }else {
                NodoLista aux = ppio;
                while(aux.sig != null) {
                    if(aux.sig.dato.equals(el)) {
                        aux.sig = aux.sig.sig;
                    }
                    aux = aux.sig;
                }
            }
        }
    }
    public void imprimir() {
        NodoLista aux = ppio;
        while(aux != null) {
            System.out.println(aux.dato);
            aux = aux.sig;
        }
    }
};

public class ListaString {
    public static void main(String[] args) {
        Lista listaDeString = new ListaImp();
        listaDeString.insertar("UNO");
        listaDeString.insertar("DOS");
        listaDeString.insertar("TRES");
        listaDeString.insertar("CUATRO");
        listaDeString.insertar("CINCO");
        listaDeString.insertar("SEIS");
        listaDeString.insertar("SIETE");

        listaDeString.eliminar("SEIS");

        listaDeString.imprimir();
    }
}