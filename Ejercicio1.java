import java.util.Scanner;

class NodoAVL<T> {
    T dato;
    int cantRepeticiones; // CUENTO LA CANTIDAD DE VECES QUE INGRESO EL DATO
    int altura;
    NodoAVL<T> izq, der;

    NodoAVL(T d) {
        dato = d;
        cantRepeticiones = 1;
        altura = 1;
    }
}

// Exijo que el T que me pasen se sepa "comparar"
class AVL<T extends Comparable<T>> {
    NodoAVL<T> raiz;

    // returna la altura de un nodo
    private int altura(NodoAVL<T> N) {
        if (N == null)
            return 0;
        return N.altura;
    }

    private int max(int a, int b) {
        return a > b ? a : b;
    }

    private NodoAVL<T> rotacionDerecha(NodoAVL<T> y) {
        NodoAVL<T> x = y.izq;
        NodoAVL<T> T2 = x.der;
        x.der = y;
        y.izq = T2;

        // actualizar alturas
        y.altura = max(altura(y.izq), altura(y.der)) + 1;
        x.altura = max(altura(x.izq), altura(x.der)) + 1;

        // retorna el nuevo padre
        return x;
    }

    private NodoAVL<T> rotacionIzquierda(NodoAVL<T> x) {
        NodoAVL<T> y = x.der;
        NodoAVL<T> T2 = y.izq;
        y.izq = x;
        x.der = T2;

        // actualizar alturas
        x.altura = max(altura(x.izq), altura(x.der)) + 1;
        y.altura = max(altura(y.izq), altura(y.der)) + 1;

        // retorna el nuevo padre
        return y;
    }

    // obtiene el factor de equilibro de un nodo
    private int getBalance(NodoAVL<T> N) {
        if (N == null)
            return 0;

        return altura(N.der) - altura(N.izq);
    }

    // funcion que inserta un dato
    // retorna la nueva raiz (si es que cambia)
    private NodoAVL<T> insertar(NodoAVL<T> nodo, T dato) {

        /* 1. Se realiza una insersion tal cual como un ABB */
        if (nodo == null) {
            return (new NodoAVL<T>(dato));
        }
        if (dato.compareTo(nodo.dato) < 0)
            nodo.izq = insertar(nodo.izq, dato);
        else if (dato.compareTo(nodo.dato) > 0)
            nodo.der = insertar(nodo.der, dato);
        else { // Duplicate keys not allowed
            nodo.cantRepeticiones++; // SI ESTOY INSERTANDO UN DATO REPETIDO
                                     // AUMENTO EL CONTADOR DE REPETICIONES DE ESE DATO
                                     // CREO QUE NO DEBERIA VARIAR EL ORDEN o(log N)
            return nodo;
        }

        /* 2. Actualizamos la nueva altura */
        nodo.altura = 1 + max(altura(nodo.izq), altura(nodo.der));

        /*
         * 3. Obtenemos el factor de equilibro para saber si se desbalanceo y hay que
         * realizar las rotaciones
         */
        int balance = getBalance(nodo);

        // Rotacion derecha simple
        if (balance < -1 && dato.compareTo((T) nodo.izq.dato) < 0)
            return rotacionDerecha(nodo);

        // Rotacion derecha simple
        if (balance > 1 && dato.compareTo((T) nodo.der.dato) > 0)
            return rotacionIzquierda(nodo);

        // Rotacion Izquierda doble (izq-der)
        if (balance < -1 && dato.compareTo((T) nodo.izq.dato) > 0) {
            nodo.izq = rotacionIzquierda(nodo.izq);
            return rotacionDerecha(nodo);
        }

        // Rotacion Derecha doble (der-izq)
        if (balance > 1 && dato.compareTo((T) nodo.der.dato) < 0) {
            nodo.der = rotacionDerecha(nodo.der);
            return rotacionIzquierda(nodo);
        }
        return nodo;
    }

    public void insertar(T dato) {
        this.raiz = insertar(this.raiz, dato);
    }

    private void inOrden(NodoAVL<T> nodo) {
        if (nodo != null) {
            inOrden(nodo.izq);
            for (int i = 1; i <= nodo.cantRepeticiones; i++) {
                System.out.println(nodo.dato);
            }
            inOrden(nodo.der);
        }
    }

    public void inOrden() {
        inOrden(this.raiz);
    }
};

class Sorting{
    AVL<Integer> avl;

    public Sorting(){
        avl = new AVL<Integer>();
    }

    public void agregar(int numero){
        avl.insertar(numero);
    }

    public void mostrarOrdenado(){
        avl.inOrden();
    }
}

// CON LA PRUEBA MAS GRANDE (10.000.000) DEMORA 62 SEGS APROX
public class Ejercicio1 {
    public static void main(String[] args) {
        long tiempoInicial = System.currentTimeMillis();
        Sorting sorting = new Sorting();
        
        Scanner sc = new Scanner(System.in);
        int cantNumeros = sc.nextInt();

        for (int i = 0; i < cantNumeros; i++) {
            sorting.agregar(sc.nextInt());
        }
        sc.close();
        sorting.mostrarOrdenado();

        long tiempoFinal = System.currentTimeMillis();
        System.out.println("Tiempo total: " + (tiempoFinal - tiempoInicial) / 1000 + " segs");
    }
}