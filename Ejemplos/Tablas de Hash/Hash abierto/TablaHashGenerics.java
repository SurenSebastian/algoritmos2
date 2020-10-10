import java.util.Scanner;
import java.util.function.Function;


class ClaveValor<K,V>{
    private K clave;
    private V valor;
    public ClaveValor(K unaClave, V unValor){
        clave = unaClave;
        valor = unValor;
    }
    public K getClave() { return this.clave; };
    public void setClave(K unaClave) { this.clave = unaClave; };
    public V getValor() { return this.valor; };
    public void setValor(V unValor) { this.valor = unValor; };
    @Override
    public boolean equals(Object otro) {
        try {
            // tratamos de castear a una ClaveValor
            ClaveValor<K,V> otroCV = (ClaveValor<K,V>) otro;
            // de poder hacerlo, comparamos las claves
            return this.getClave().equals(otroCV.getClave());
        }catch(Exception e1) {
            try {
                // tratamos de castear a un K
                K unaClave = (K) otro;
                // de poder hacerlo, comparamos las clave
                return this.getClave().equals(unaClave);
            }catch(Exception e2) {
                return false;
            }
        }
    }
}
// Nodo Lista simplemente encadenado
class NodoLista<E>{
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

class TablaHash<K,V>{
    // Array principal de la estructura, cada casillero es un puntero al primer elemento de la "Lista".
    // IMPORTANTE: java no le gusta los array usando generics, por ende hacemos un array de objetos y lo cateamos cuando lo recuperamos
    private Object array[];

    private Function<K,Integer> fnHash; // Guardamos la funcion hash (ver constructor). Notar que la funcion recibe un K (clave) y retorna un int
    // tamanio del array
    private int tamanio;

    // IMPORTANTE: el constructor del hash recibe dos parametros, el primero es el tamanio y por ultimo la funcion hash.
    // En java se pueden pasar funciones como parametros:
    // https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html
    // https://codereview.stackexchange.com/questions/186972/passing-a-generic-function-as-parameter
    // Al usar generics/templates no tenemos forma de obtener un int de un tipo K, ya que no sabemos nada de el, por lo cual,
    // pedimos una funcion hash para que sea guardada y ultizada dentro de la tabla de hash.
    public TablaHash(int tamanio, Function<K,Integer> unaFuncionHash) { 
        int nuevoTamanio = this.SiguientePrimo(tamanio);
        this.array = new Object[nuevoTamanio]; // instancio el array, en java no se puede usar generics en array, se debe hacer un array de objetos y luego castear
        for (int i = 0; i < nuevoTamanio; i++) { this.array[i] = null; } // inicializo el array
        this.tamanio = nuevoTamanio;
        this.fnHash = unaFuncionHash; // IMPORTANTE: me guardo la funcion de hash para luego usar en el resto de las operaciones.
    }
    // funcion que me calcula el siguiente primo.
    private int SiguientePrimo(int origen) {
        // TODO
        return origen;
    }
    // funcion que dada un clave nos devuelve la posicion en el array donde se esta buscando/insertando/etc
    private int posHash(K clave) {
        // usando la funcion fnHash y el tamanio del array se obtiene la posicion.
        // Nota: fnHash puede retornar un numero fuera del array por lo cual se aplica el modulo con el tamanio del array.
        int pos = java.lang.Math.abs(this.fnHash.apply(clave)) % this.tamanio;
        return pos;
    }
    public void Insertar(K clave,V valor) {
        int casillero = this.posHash(clave); // Obtengo la posicion inicial, el "casillero"
        if(array[casillero] == null) {
            ClaveValor<K,V> aInsertar = new ClaveValor<K,V>(clave, valor);
            NodoLista<ClaveValor<K,V>> nodoAInsertar = new NodoLista<ClaveValor<K,V>>(aInsertar);
            array[casillero] = nodoAInsertar;
        }else {
            NodoLista<ClaveValor<K,V>> primerNodo = (NodoLista<ClaveValor<K,V>>) array[casillero]; // hay que castear
            this.InsertarRecursivo(primerNodo, clave, valor);
        }
    }
    private void InsertarRecursivo(NodoLista<ClaveValor<K,V>> nodo, K clave, V valor) {
        if(nodo.dato.equals(clave)) {
            // Si la clave ya se encuentra dentro de la tabla entonces la sustituyo por el nuevo valor
            nodo.dato.setValor(valor);
        }else if(nodo.sig ==  null){
            ClaveValor<K,V> aInsertar = new ClaveValor<K,V>(clave, valor);
            NodoLista<ClaveValor<K,V>> nodoAInsertar = new NodoLista<ClaveValor<K,V>>(aInsertar);
            nodo.sig = nodoAInsertar;
        }else {
            // Paso recursivo
            InsertarRecursivo(nodo.sig, clave, valor);
        }
    }
    public V Recuperar(K clave) {
        int casillero = this.posHash(clave); // Obtengo la posicion inicial, el "casillero"
        NodoLista<ClaveValor<K,V>> primerNodo = (NodoLista<ClaveValor<K,V>>) array[casillero]; // hay que castear
        return RecuperarRecursivo(primerNodo, clave);
    }
    private V RecuperarRecursivo(NodoLista<ClaveValor<K,V>> nodo,K clave) {
            if(nodo.dato.equals(clave)) {
                return nodo.dato.getValor();
            }else {
                // Paso recursivo
                return RecuperarRecursivo(nodo.sig, clave);
            }
        }
};

// PARTE TRES
class Persona {
    
    public int ci;
    public String nombre;
    public Persona(int unaCi, String unNombre) {
        ci = unaCi;
        nombre = unNombre;
    }
    @Override
    public boolean equals(Object otro) {
        try {
            // tratamos de castear a una Persona
            Persona otraPersona = (Persona) otro;
            // de poder hacerlo, comparamos las ci
            return this.ci == otraPersona.ci;
        }catch(Exception e1) {
            return false;
        }
    }
};

class Empresa {
    public String nombre;
    public Empresa(String unNombre) {
        nombre = unNombre;
    }
    @Override
    public String toString() {
        return nombre;
    }
};

public class TablaHashGenerics {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        // PARTE UNO
        Function<String, Integer> funcionHash = 
            (String palabra)-> {
                int sum = 0;
                for (int k = 0; k < palabra.length(); k++)
                    sum = sum + (int) palabra.charAt(k);
                return sum;};
        TablaHash<String, Integer> miTabla = new TablaHash<String, Integer>(100, funcionHash);
        miTabla.Insertar("UNO", 1);
        miTabla.Insertar("DOS", 2);
        miTabla.Insertar("TRES", 3);
        System.out.println(miTabla.Recuperar("UNO"));
        System.out.println(miTabla.Recuperar("DOS"));
        System.out.println(miTabla.Recuperar("TRES"));

        // PARTE DOS (usando otra funcion hash)
        Function<String, Integer> otraFuncionHash = 
            (String palabra)-> {
                int sum = 0;
                for (int k = 0; k < palabra.length(); k++)
                    sum = sum + (int) palabra.charAt(k) * (k+1); // no solo sumamos la ascii sino que lo multiplicamos por la posicion dentro de la palabra
                return sum;};
        miTabla = new TablaHash<String, Integer>(100, otraFuncionHash);
        miTabla.Insertar("UNO", 1);
        miTabla.Insertar("DOS", 2);
        miTabla.Insertar("TRES", 3);
        System.out.println(miTabla.Recuperar("UNO"));
        System.out.println(miTabla.Recuperar("DOS"));
        System.out.println(miTabla.Recuperar("TRES"));

        // PARTE TRES
         Function<Persona, Integer> funcionHashPersona = 
            (Persona personaAHashear)-> {
                return personaAHashear.ci * (int)personaAHashear.nombre.charAt(0) + 97; // no tiene porque tener sentido, pero jugara un rol importante en la colisiones si no es bien elegida.
                };
        TablaHash<Persona, Empresa> miTabla2 = new TablaHash<Persona, Empresa>(100, funcionHashPersona); // <------
        Persona p1 = new Persona(123, "Juan");
        Persona p2 = new Persona(654, "Jose");
        Persona p3 = new Persona(977, "Pedro");
        Empresa e1 = new Empresa("Google");
        Empresa e2 = new Empresa("Microsoft");
        Empresa e3 = new Empresa("ORT");
        miTabla2.Insertar(p1,e1);
        miTabla2.Insertar(p2,e2);
        miTabla2.Insertar(p3,e3);
        System.out.println(miTabla2.Recuperar(p1));
        System.out.println(miTabla2.Recuperar(p2));
        System.out.println(miTabla2.Recuperar(p3));
    }
}