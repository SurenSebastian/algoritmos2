import java.util.Scanner;

abstract class Lista {
    abstract void insertar(int el);
    abstract void eliminar(int el);
    abstract void imprimir();
};

class NodoLista {
    public int dato;
    public NodoLista sig;
    public NodoLista() {};
    public NodoLista(int unDato, NodoLista unSig) {
        dato = unDato;
        sig = unSig;
    };
    public NodoLista(int unDato) {
        dato = unDato;
        sig = null;
    };
};

class ListaImp extends Lista {
    private NodoLista ppio;

    public ListaImp() {
        ppio = null;
    }

    public void insertar(int el) {
        this.ppio = new NodoLista(el, this.ppio);
    }
    public void eliminar(int el) {
        if(ppio != null) {
            if(ppio.dato == el) {
                ppio = ppio.sig;
            }else {
                NodoLista aux = ppio;
                while(aux.sig != null) {
                    if(aux.sig.dato == el) {
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

public class ListaInt {
    public static void main(String[] args) {
        Lista listaDeEnteros = new ListaImp();
        listaDeEnteros.insertar(1);
        listaDeEnteros.insertar(2);
        listaDeEnteros.insertar(3);
        listaDeEnteros.insertar(4);
        listaDeEnteros.insertar(5);
        listaDeEnteros.insertar(6);
        listaDeEnteros.insertar(7);

        listaDeEnteros.eliminar(6);

        listaDeEnteros.imprimir();
    }
}