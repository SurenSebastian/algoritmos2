import java.util.Scanner;

abstract class Lista<T> {
    abstract void insertar(T el);
    abstract void eliminar(T el);
    abstract void imprimir();
};

class NodoLista<T> {
    public T dato;
    public NodoLista<T> sig;
    public NodoLista() {};
    public NodoLista(T unDato, NodoLista<T> unSig) {
        dato = unDato;
        sig = unSig;
    };
    public NodoLista(T unDato) {
        dato = unDato;
        sig = null;
    };
};

class ListaImp<T> extends Lista<T> {
    private NodoLista<T> ppio;

    public ListaImp() {
        ppio = null;
    }

    public void insertar(T el) {
        this.ppio = new NodoLista<T>(el, this.ppio);
    }
    public void eliminar(T el) {
        if(ppio != null) {
            if(ppio.dato == el) {
                ppio = ppio.sig;
            }else {
                NodoLista<T> aux = ppio;
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
        NodoLista<T> aux = ppio;
        while(aux != null) {
            System.out.println(aux.dato);
            aux = aux.sig;
        }
    }
};

class Persona {
    private String nombre;
    public Persona(String unNombre) { this.nombre = unNombre; }
    @Override
    public String toString() { return this.nombre; }
    @Override
    public boolean equals(Object p) {
        Persona otraPersona =  (Persona) p;
        return this.nombre.equals(otraPersona.nombre);
    }
};

public class ListaTemplates {
    public static void main(String[] args) {

        Lista<Integer> listaDeEnteros = new ListaImp<Integer>();
        listaDeEnteros.insertar(1);
        listaDeEnteros.insertar(2);
        listaDeEnteros.insertar(3);
        listaDeEnteros.insertar(4);
        listaDeEnteros.insertar(5);
        listaDeEnteros.insertar(6);
        listaDeEnteros.insertar(7);

        listaDeEnteros.eliminar(6);

        listaDeEnteros.imprimir();

        Lista<String> listaDeString = new ListaImp<String>();
        listaDeString.insertar("UNO");
        listaDeString.insertar("DOS");
        listaDeString.insertar("TRES");
        listaDeString.insertar("CUATRO");
        listaDeString.insertar("CINCO");
        listaDeString.insertar("SEIS");
        listaDeString.insertar("SIETE");

        listaDeString.eliminar("SEIS");

        listaDeString.imprimir();


        Lista<Persona> listaDePersonas = new ListaImp<Persona>();
        listaDePersonas.insertar(new Persona("Pepe"));
        listaDePersonas.insertar(new Persona("Juan"));
        listaDePersonas.insertar(new Persona("Main"));

        listaDePersonas.eliminar(new Persona("Juan"));

        listaDePersonas.imprimir();
    }
}