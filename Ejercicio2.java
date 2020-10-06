import java.util.Scanner;
import java.util.function.Function;

class ClaveValor<K, V> {
    private K clave;
    private V valor;

    public ClaveValor(K unaClave, V unValor) {
        clave = unaClave;
        valor = unValor;
    }

    public K getClave() {
        return this.clave;
    };

    public void setClave(K unaClave) {
        this.clave = unaClave;
    };

    public V getValor() {
        return this.valor;
    };

    public void setValor(V unValor) {
        this.valor = unValor;
    };

    @Override
    public boolean equals(Object otro) {
        try {
            // tratamos de castear a una ClaveValor
            ClaveValor<K, V> otroCV = (ClaveValor<K, V>) otro;
            // de poder hacerlo, comparamos las claves
            return this.getClave().equals(otroCV.getClave());
        } catch (Exception e1) {
            try {
                // tratamos de castear a un K
                K unaClave = (K) otro;
                // de poder hacerlo, comparamos las clave
                return this.getClave().equals(unaClave);
            } catch (Exception e2) {
                return false;
            }
        }
    }
}

// Nodo Lista simplemente encadenado
class NodoLista<E> {
    public E dato; // guarda el dato en si dentro del nodo
    public NodoLista<E> sig; // guarda la referencia al siguiente nodo

    public NodoLista(E unDato) { // Constructor con solo el dato, por defecto el sig es null
        this.dato = unDato;
        this.sig = null;

    }

    public NodoLista(E unDato, NodoLista<E> unSig) { // Constructor con ambos datos
        this.dato = unDato;
        this.sig = unSig;
    }
}

class TablaHash<K, V> {
    // Array principal de la estructura, cada casillero es un puntero al primer
    // elemento de la "Lista".
    // IMPORTANTE: java no le gusta los array usando generics, por ende hacemos un
    // array de objetos y lo cateamos cuando lo recuperamos
    private Object array[];

    private Function<K, Integer> fnHash; // Guardamos la funcion hash (ver constructor). Notar que la funcion recibe un
                                         // K (clave) y retorna un int
    // tamanio del array
    private int tamanio;

    // IMPORTANTE: el constructor del hash recibe dos parametros, el primero es el
    // tamanio y por ultimo la funcion hash.
    // En java se pueden pasar funciones como parametros:
    // https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html
    // https://codereview.stackexchange.com/questions/186972/passing-a-generic-function-as-parameter
    // Al usar generics/templates no tenemos forma de obtener un int de un tipo K,
    // ya que no sabemos nada de el, por lo cual,
    // pedimos una funcion hash para que sea guardada y ultizada dentro de la tabla
    // de hash.
    public TablaHash(int tamanio, Function<K, Integer> unaFuncionHash) {
        int nuevoTamanio = this.SiguientePrimo(tamanio);
        this.array = new Object[nuevoTamanio]; // instancio el array, en java no se puede usar generics en array, se
                                               // debe hacer un array de objetos y luego castear
        for (int i = 0; i < nuevoTamanio; i++) {
            this.array[i] = null;
        } // inicializo el array
        this.tamanio = nuevoTamanio;
        this.fnHash = unaFuncionHash; // IMPORTANTE: me guardo la funcion de hash para luego usar en el resto de las
                                      // operaciones.
    }

    // funcion que me calcula el siguiente primo.
    private int SiguientePrimo(int origen) {
        // TODO
        return origen;
    }

    // funcion que dada un clave nos devuelve la posicion en el array donde se esta
    // buscando/insertando/etc
    private int posHash(K clave) {
        // usando la funcion fnHash y el tamanio del array se obtiene la posicion.
        // Nota: fnHash puede retornar un numero fuera del array por lo cual se aplica
        // el modulo con el tamanio del array.
        int pos = java.lang.Math.abs(this.fnHash.apply(clave)) % this.tamanio;
        return pos;
    }

    public void insertar(K clave, V valor) {
        int casillero = this.posHash(clave); // Obtengo la posicion inicial, el "casillero"
        ClaveValor<K, V> aInsertar = new ClaveValor<K, V>(clave, valor);
        NodoLista<ClaveValor<K, V>> nodoAInsertar = new NodoLista<ClaveValor<K, V>>(aInsertar);
        if (array[casillero] == null) {
            array[casillero] = nodoAInsertar;
        } else {
            NodoLista<ClaveValor<K, V>> primerNodo = (NodoLista<ClaveValor<K, V>>) array[casillero]; // hay que castear
            this.insertarRecursivo(primerNodo, clave, valor);
        }
    }

    private void insertarRecursivo(NodoLista<ClaveValor<K, V>> nodo, K clave, V valor) {
        if (nodo.sig == null) {
            nodo.sig = new NodoLista<ClaveValor<K, V>>(new ClaveValor<K, V>(clave, valor));
        } else {
            insertarRecursivo(nodo.sig, clave, valor);
        }
    }

    public V recuperar(K clave) {
        int casillero = this.posHash(clave); // Obtengo la posicion inicial, el "casillero"
        NodoLista<ClaveValor<K, V>> primerNodo = (NodoLista<ClaveValor<K, V>>) array[casillero]; // hay que castear
        return recuperarRecursivo(primerNodo, clave);
    }

    private V recuperarRecursivo(NodoLista<ClaveValor<K, V>> nodo, K clave) {
        if (nodo != null) {
            if (nodo.dato.equals(clave)) {
                return nodo.dato.getValor();
            } else {
                return recuperarRecursivo(nodo.sig, clave);
            }
        } else {
            return null;
        }
    }

    // USADO PARA HACER DEBUG
    public void imprimir() {
        NodoLista<ClaveValor<K, V>> nodo;
        for (int i = 0; i < array.length; i++) {
            System.out.print("[" + i + "] -> ");
            nodo = (NodoLista<ClaveValor<K, V>>) array[i];
            while (nodo != null) {
                System.out.print("[" + nodo.dato.getClave() + "] ");
                nodo = nodo.sig;
            }
            System.out.println();
        }
    }
}

class Diccionario {
    TablaHash<String, String> tablaHashPalabras;
    Function<String, Integer> unaFuncionHash = (palabras) -> {
        // https://github.com/kfricilone/OpenRS/blob/master/source/net/openrs/util/crypto/Djb2.java
        // http://www.cse.yorku.ca/~oz/hash.html
        int hash = 0;
        for (int i = 0; i < palabras.length(); i++) {
            hash = palabras.charAt(i) + ((hash << 5) - hash);
        }
        return hash;
    };

    public Diccionario(int cantPalabras) {
        tablaHashPalabras = new TablaHash<String, String>(cantPalabras * 2, unaFuncionHash);
    }

    public void agregarPalabra(String palabra) {
        tablaHashPalabras.insertar(palabra, palabra);
    }

    public String existePalabra(String palabra) {
        if (tablaHashPalabras.recuperar(palabra) != null) {
            return "1";
        }
        return "0";
    }

}

// CON LA PRUEBA MAS GRANDE (1.000.000) DEMORA 7 SEGS APROX
public class Ejercicio2 {
    public static void main(String[] args) {
        long tiempoInicial = System.currentTimeMillis();

        Scanner sc = new Scanner(System.in);
        String palabra;
        
        int cantPalabras = Integer.parseInt(sc.nextLine());
        Diccionario diccionario = new Diccionario(cantPalabras);

        while (cantPalabras > 0) {
            palabra = sc.nextLine();
            diccionario.agregarPalabra(palabra);
            cantPalabras--;
        }

        int cantPalabrasABuscar = Integer.parseInt(sc.nextLine());

        while (cantPalabrasABuscar > 0) {
            palabra = sc.nextLine();
            System.out.println(diccionario.existePalabra(palabra));
            cantPalabrasABuscar--;
        }
        sc.close();

        long tiempoFinal = System.currentTimeMillis();
        System.out.println("Tiempo total: " + (tiempoFinal - tiempoInicial) / 1000 + " segs");
    }
}