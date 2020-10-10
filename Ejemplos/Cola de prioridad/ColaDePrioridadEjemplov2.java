import java.util.Scanner;
import java.util.function.BiFunction;

abstract class ColaDePrioridad<T> {
    abstract void insertar(T el);
    abstract T desencolar();
    abstract boolean esVacia();
    abstract boolean estaLlena();
};

class NodoLista<T> {
    public NodoLista sig;
    public NodoLista ant;
    public T dato;
    public NodoLista() {};
    public NodoLista(T unDato, NodoLista unSig, NodoLista unAnt) {
        dato = unDato;
        sig = unSig;
        ant = unAnt;
    };
    public NodoLista(T unDato) {
        dato = unDato;
        sig = null;
        ant = null;
    };
};

class Lista<T> {
    private NodoLista<T> ppio;
    private BiFunction<T,T,Integer> fnComparacion;

    public Lista(BiFunction<T,T,Integer> unaFnComparacion) {
        ppio = null;
        fnComparacion = unaFnComparacion;
    }

    public void insertar(T dato) {
        NodoLista<T> nuevoNodo =  new NodoLista<T>(dato, this.ppio, null);
        if(this.ppio != null) {
            this.ppio.ant = nuevoNodo;
        }
        ppio = nuevoNodo;
    }
    public T obtenerMenor() {
        NodoLista<T> menor = this.ppio;
        NodoLista<T> aux = this.ppio;
        while(aux != null) {
            if(this.fnComparacion.apply(menor.dato, aux.dato) > 0) {
                menor = aux;
            }
            aux = aux.sig;
        }
        return menor.dato;
    }

    public void eliminarMenor() {
        NodoLista<T> menor = this.ppio;
        NodoLista<T> aux = this.ppio;
        while(aux != null) {
            if(this.fnComparacion.apply(menor.dato, aux.dato) > 0) {
                menor = aux;
            }
            aux = aux.sig;
        }

        // elimino el nodo que contiene el menor dato
        if(menor.ant != null) {
            menor.ant.sig = menor.sig;
        }else {
            ppio = menor.sig;
        }
        if(menor.sig != null) {
            menor.sig.ant = menor.ant;
        }
    }

    public boolean esVacia() {
        return ppio == null;
    }
};

class ColaDePrioridadImpLista<T> extends ColaDePrioridad<T> {
    private Lista<T> miLista;

    public ColaDePrioridadImpLista(BiFunction<T,T,Integer> unaFnComparacion) {
        miLista = new Lista<T>(unaFnComparacion);
    }

    public void insertar(T el) {
        miLista.insertar(el);
    }
    public T desencolar() {
        T ret = miLista.obtenerMenor();
        miLista.eliminarMenor();
        return ret;
    }
    public boolean esVacia() {
        return miLista.esVacia();
    }
    public boolean estaLlena() {
        return false;
    }
};

class Estudiante {
    public String nombre;
    public int edad;
    public int pac;
    public int cantidadDeMateriasAprobadas;
    public Estudiante(String unNombre, int unaEdad, int unPac, int unaCantidadDeMaterias) {
        this.nombre = unNombre;
        this.edad = unaEdad;
        this.pac = unPac;
        this.cantidadDeMateriasAprobadas = unaCantidadDeMaterias;
    }
    @Override
    public String toString() {
        return nombre;
    }
}

public class ColaDePrioridadEjemplov2 {
    public static void main(String[] args) {
        BiFunction<Integer,Integer,Integer> fnComparacion = (
            (Integer x,Integer y) -> {
                return x - y;
            }
        );

        ColaDePrioridad<Integer> cp = new ColaDePrioridadImpLista<Integer>(fnComparacion);
        cp.insertar(12);
        cp.insertar(1);
        cp.insertar(44);
        cp.insertar(100);
        cp.insertar(56);
        cp.insertar(122);

        while(!cp.esVacia()) {
            System.out.println(cp.desencolar());
        }

        BiFunction<Estudiante, Estudiante, Integer> fnComparacionEstudiante = 
            (Estudiante e1, Estudiante e2)-> {
                if(e1.pac != e2.pac) {
                    return e2.pac - e1.pac;
                }
                if(e1.cantidadDeMateriasAprobadas != e2.cantidadDeMateriasAprobadas) {
                    return e2.cantidadDeMateriasAprobadas - e1.cantidadDeMateriasAprobadas;
                }
                if(e1.edad != e2.edad) {
                    return e1.edad - e2.edad;
                }
                return 0;
            };

        ColaDePrioridad<Estudiante> cp2 = new ColaDePrioridadImpLista<Estudiante>(fnComparacionEstudiante);
        cp2.insertar(new Estudiante("Pepe", 23, 90, 10));
        cp2.insertar(new Estudiante("Juan", 20, 82, 15));
        cp2.insertar(new Estudiante("Florencia", 34, 99, 1));
        cp2.insertar(new Estudiante("Anastacio", 43, 82, 22));
        cp2.insertar(new Estudiante("Joaquin", 28, 50, 32));
        cp2.insertar(new Estudiante("Jose", 23, 50, 32));

        // while(!cp2.esVacia()) {
        //     System.out.println(cp2.desencolar());
        // }


        BiFunction<Estudiante, Estudiante, Integer> fnComparacionEstudiante2 = 
            (Estudiante e1, Estudiante e2)-> {
                if(e1.edad != e2.edad) {
                    return e2.edad - e1.edad;
                }
                return 0;
            };

        ColaDePrioridad<Estudiante> cp3 = new ColaDePrioridadImpLista<Estudiante>(fnComparacionEstudiante2);
        cp3.insertar(new Estudiante("Pepe", 23, 90, 10));
        cp3.insertar(new Estudiante("Juan", 20, 82, 15));
        cp3.insertar(new Estudiante("Florencia", 34, 99, 1));
        cp3.insertar(new Estudiante("Anastacio", 43, 82, 22));
        cp3.insertar(new Estudiante("Joaquin", 28, 50, 32));
        cp3.insertar(new Estudiante("Jose", 23, 50, 32));

        while(!cp3.esVacia()) {
            System.out.println(cp3.desencolar());
        }
    }
}