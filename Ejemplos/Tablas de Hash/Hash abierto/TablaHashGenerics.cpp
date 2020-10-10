#ifndef TABLA_HASH
#define TABLA_HASH
#include <cassert>
#include <string>
#include <iostream>
using namespace std;


// Clase que "empareja" a la clave y al valor, similar a asociacion.
template <class K, class V>
class ClaveValor{
    private:
        K clave;
        V valor;
    public:
        ClaveValor(K &unaClave, V &unValor): clave(unaClave), valor(unValor) {} // Constructor con ambos datos
        ClaveValor(K &unaClave): clave(unaClave) {} // Constructor con solo la clave
        K getClave() {return this->clave;}
        V getValor() {return this->valor;}
        void setClave(K nuevaClave) {this->clave = nuevaClave;}
        void setValor(V nuevoValor) {this->valor = nuevoValor;}
        bool operator==(ClaveValor otraCV) {return this->getClave() == otraCV.getClave(); } // "delego" la igualacion a las claves. Dos ClaveValor son iguales si sus claves son iguales
        bool operator==(K unaClave) {return this->getClave() == unaClave; } // comparador entre ClaveValor y K
};

// Nodo Lista simplemente encadenado
template <class T>
struct NodoLista{
    public:
        T dato; // guarda el dato en si dentro del nodo
        NodoLista<T> *sig; // guarda la referencia al siguiente nodo
        NodoLista(T &unDato, NodoLista<T> *unSiguiente): dato(unDato), sig(unSiguiente) {} // Constructor con ambos datos
        NodoLista(T &unDato): dato(unDato), sig(0) {} // Constructor con solo el dato, por defecto el sig es null
}; 

template <class K, class V>
class TablaHash {
    private:
        // La "Lista" en cada casillero sera un puntero a un NodoLista de ClaveValor
        typedef NodoLista<ClaveValor<K,V>> *NodoPtr;
        // Array principal de la estructura, cada casillero es un puntero a el nodo mencionado anteriormente
        NodoPtr *arrList;
        // Guardamos la funcion hash (ver constructor). Notar que la funcion recibe un K (clave) y retorna un int
        int (*fnHash)(K);
        // tamanio del array
        int tamanio;
        // funcion que dada un clave nos devuelve la posicion en el array donde se esta buscando/insertando/etc
        int posHash(K clave) {
            // usando la funcion fnHash y el tamanio del array se obtiene la posicion.
            // Nota: fnHash puede retornar un numero fuera del array por lo cual se aplica el modulo con el tamanio del array.
            int pos = abs(this->fnHash(clave)) % this->tamanio;
            return pos;
        }
        // funcion que me calcula el siguiente primo.
        int SiguientePrimo(int origen) {
            // TODO
            return origen;
        }
        // funcion recursiva una vez que se encuentra el casillero correspondiente. Observar: void Insertar(K clave,V el)
        void InsertarRecursivo(NodoPtr &nodo, K clave, V valor) {
            if(nodo == 0) {
                ClaveValor<K,V> aInsertar(clave,valor);
                nodo = new NodoLista<ClaveValor<K,V>>(aInsertar);
            }else if(nodo->dato == clave) {
                // Si la clave ya se encuentra dentro de la tabla entonces la sustituyo por el nuevo valor
                nodo->dato.setValor(valor);
            }else {
                // Paso recursivo
                InsertarRecursivo(nodo->sig, clave, valor);
            }
        }
        V RecuperarRecursivo(NodoPtr nodo,K clave) {
            assert(nodo != 0);
            if(nodo->dato == clave) {
                return nodo->dato.getValor();
            }else {
                // Paso recursivo
                return RecuperarRecursivo(nodo->sig, clave);
            }
        }
    public:
        // IMPORTANTE: el constructor del hash recibe dos parametros, el primero es el tamanio y por ultimo la funcion hash.
        // En c++ se pueden pasar funciones como parametros. http://math.hws.edu/bridgeman/courses/331/f05/handouts/c-c++-notes.html
        // Al usar generics/templates no tenemos forma de obtener un int de un tipo K, ya que no sabemos nada de el, por lo cual,
        // pedimos una funcion hash para que sea guardada y ultizada dentro de la tabla de hash.
        TablaHash(int tamanio, int (*f)(K)) {
            int nuevoTamanio = this->SiguientePrimo(tamanio);
            arrList = new NodoPtr[nuevoTamanio]; // instancio el array
            for (int i = 0; i < nuevoTamanio; i++) { arrList[i] = 0; } // inicializo el array
            this->tamanio = nuevoTamanio;
            this->fnHash = f; // IMPORTANTE: me guardo la funcion de hash para luego usar en el resto de las operaciones.
        }
        ~TablaHash() {
            // TODO
        }
        void Insertar(K clave,V valor) {
            int casillero = this->posHash(clave); // Obtengo la posicion inicial, el "casillero"
            this->InsertarRecursivo(arrList[casillero], clave, valor);
        }
        void Eliminar(K clave) {
            // TODO
        }
        V Recuperar(K clave) {
            int casillero = this->posHash(clave); // Obtengo la posicion inicial, el "casillero"
            return RecuperarRecursivo(arrList[casillero], clave);
        }
};

// PARTE UNO
int funcionHash(string palabra) {
    int sum = 0;
    for (int k = 0; k < palabra.length(); k++)
        sum = sum + int(palabra[k]);
    return sum;
};

// PARTE DOS
int otraFuncionHash(string palabra) {
    int sum = 0;
    for (int k = 0; k < palabra.length(); k++)
        sum = sum + int(palabra[k]) * (k+1); // no solo sumamos la ascii sino que lo multiplicamos por la posicion dentro de la palabra
    return sum;
};

// PARTE TRES

class Persona {
    public:
        int ci;
        string nombre;
        Persona(int unaCi, string unNombre): ci(unaCi), nombre(unNombre) {}
        bool operator==(Persona otraPersona) {return this->ci == otraPersona.ci; }
};

class Empresa {
    public:
        string nombre;
        Empresa(string unNombre): nombre(unNombre) {}
};

int funcionHashPersona(Persona personaAHashear) {
    return 84760; // no tiene porque tener sentido, pero jugara un rol importante en la colisiones si no es bien elegida.
};


int main () 
{

    // PARTE UNO
    cout<< "Parte uno:" << endl;
    TablaHash<string, int> * miTabla = new TablaHash<string, int>(100, funcionHash);
    miTabla->Insertar("UNO", 1);
    miTabla->Insertar("DOS", 2);
    miTabla->Insertar("TRES", 3);

    cout << miTabla->Recuperar("UNO") << endl;
    cout << miTabla->Recuperar("DOS") << endl;
    cout << miTabla->Recuperar("TRES") << endl;

    // PARTE DOS (usando otra funcion hash)
    cout<< "Parte dos:" << endl;
    miTabla = new TablaHash<string, int>(100, otraFuncionHash); // <------
    miTabla->Insertar("UNO", 1);
    miTabla->Insertar("DOS", 2);
    miTabla->Insertar("TRES", 3);

    cout << miTabla->Recuperar("UNO") << endl;
    cout << miTabla->Recuperar("DOS") << endl;
    cout << miTabla->Recuperar("TRES") << endl;

    // PARTE TRES (clases personalizadas)
    cout<< "Parte tres:" << endl;
    TablaHash<Persona, Empresa> * miTabla2 = new TablaHash<Persona, Empresa>(100, funcionHashPersona); // <------
    Persona p1(123, "Juan");
    Persona p2(654, "Jose");
    Persona p3(977, "Pedro");
    Empresa e1("Google");
    Empresa e2("Microsoft");
    Empresa e3("ORT");
    miTabla2->Insertar(p1, e1);
    miTabla2->Insertar(p2, e2);
    miTabla2->Insertar(p3, e3);

    cout << miTabla2->Recuperar(p1).nombre << endl;
    cout << miTabla2->Recuperar(p2).nombre << endl;
    cout << miTabla2->Recuperar(p3).nombre << endl;
    
    return 0;
}
#endif