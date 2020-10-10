#include <cassert>
#include <climits>
#include <string>
#include <iostream>
using namespace std;

template <class T>
class Cola
{
private:
    struct NodoColaFIFO
    {
        T dato;                                                                                // guarda el dato en si dentro del nodo
        NodoColaFIFO *sig;                                                                     // guarda la referencia al siguiente nodo
        NodoColaFIFO(T &unDato, NodoColaFIFO *unSiguiente) : dato(unDato), sig(unSiguiente) {} // Constructor con ambos datos
        NodoColaFIFO(T &unDato) : dato(unDato), sig(0) {}                                      // Constructor con solo el dato, por defecto el sig es null
    };

    NodoColaFIFO *ppio;
    NodoColaFIFO *fin;

public:
    Cola()
    {
        ppio = fin = 0;
    }
    void encolar(T nuevoDato)
    {
        NodoColaFIFO *nuevoNodo = new NodoColaFIFO(nuevoDato);
        if (ppio == NULL)
        {
            ppio = nuevoNodo;
        }
        else
        {
            fin->sig = nuevoNodo;
        }
        fin = nuevoNodo;
    }
    // pre: no esta vacia
    T desencolar()
    {
        assert(!estaVacia()); // validacion
        T ret = ppio->dato;
        ppio = ppio->sig;
        if (ppio == NULL)
        {
            fin = NULL;
        }
        return ret;
    }
    bool estaVacia()
    {
        return ppio == NULL;
    }
    T tope()
    {
        assert(!estaVacia()); // validacion
        return ppio->dato;
    }
};

template <class T>
struct NodoLista
{
    T dato;
    NodoLista *sig;
    NodoLista(T &unDato) : dato(unDato), sig(0){};
    NodoLista(T &unDato, NodoLista *unSig) : dato(unDato), sig(unSig){};
};

struct Arista
{
    int origen;
    int destino;
    int peso;
    Arista(int unOrigen, int unDestino, int unPeso = 1) : origen(unOrigen), destino(unDestino), peso(unPeso) {}
};

typedef NodoLista<Arista> *ListaAristas;

class Grafo
{
public:
    virtual void aniadirArista(int v, int w, int peso = 1) = 0;
    virtual ListaAristas adyacentesA(int origen) = 0;

    virtual void caminoMasCorto(int origen) = 0;
};

// defino mi Infinito como el maximo inf posible
#define INF INT_MAX;

class GrafoListaAdyImp : public Grafo
{
private:
    ListaAristas *listaAdy;
    int V;
    int A;

    bool esDirigido;
    bool esPonderado;

    int *initCosto(int origen)
    {
        int *ret = new int[V + 1]();
        int inf = INF;
        for (int i = 1; i <= V; i++)
        {
            ret[i] = i != origen ? inf : 0;
        }
        return ret;
    }
    int *initVengo()
    {
        int *ret = new int[V + 1]();
        for (int i = 1; i <= V; i++)
        {
            ret[i] = -1;
        }
        return ret;
    }

public:
    // Crea un grafo con V vertices (del 1 al V)
    // O(v)
    GrafoListaAdyImp(int cantidadDeVertices, bool esDirigido = true, bool esPonderado = true)
    {
        this->V = cantidadDeVertices;
        this->A = 0;
        this->esDirigido = esDirigido;
        this->esPonderado = esPonderado;

        listaAdy = new ListaAristas[V + 1];
        for (int i = 1; i <= V; i++)
        {
            listaAdy[i] = NULL;
        }
    }
    // O(1)
    void aniadirArista(int v, int w, int peso = 1)
    {
        int pesoArista = this->esPonderado ? peso : 1;
        Arista a1(v, w, pesoArista);
        listaAdy[v] = new NodoLista<Arista>(a1, listaAdy[v]);
        if (!esDirigido)
        {
            Arista a2(w, v, pesoArista);
            listaAdy[w] = new NodoLista<Arista>(a2, listaAdy[w]);
        }
    }
    // O(V) pc
    ListaAristas adyacentesA(int origen)
    {
        // copio la lista
        ListaAristas listaRetorno = NULL;
        ListaAristas listaAuxiliar = listaAdy[origen];

        while (listaAuxiliar != NULL)
        {
            Arista aristaAuxiliar = listaAuxiliar->dato;
            listaRetorno = new NodoLista<Arista>(aristaAuxiliar, listaRetorno);
            listaAuxiliar = listaAuxiliar->sig;
        }

        return listaRetorno;
    }
    // O(A + V)
    void imprimirGrafo()
    {
        for (int i = 1; i <= V; i++)
        {
            cout << "Adyacentes al vertice " << i << endl;
            ListaAristas adyacentes = this->adyacentesA(i);
            while (adyacentes != NULL)
            {
                Arista arista = adyacentes->dato;
                cout << i << "-";
                if (this->esPonderado)
                {
                    cout << arista.peso;
                }
                cout << "->" << arista.destino << endl;
                adyacentes = adyacentes->sig;
            }
        }
    }

    void caminoMasCorto(int origen)
    {
        int *costo = initCosto(origen);   // array de V casilleros, todos con valor “INF” a excepción de origen (con 0)
        int *vengo = initVengo();         // array de V casilleros, todos con valor -1

        Cola<int> cola;
        cola.encolar(origen);
        int inf = INF;
        while (!cola.estaVacia())
        {
            int vertice = cola.desencolar();
            ListaAristas adyacentes = this->adyacentesA(vertice);
            while (adyacentes != NULL)
            {
                Arista arista = adyacentes->dato;
                int w = arista.destino;
                if(costo[w] == inf) {
                    cola.encolar(w);
                    costo[w] = costo[vertice] + 1;
                    vengo[w] = vertice;
                }
                adyacentes = adyacentes->sig; 
            }
        }

        // procesar la "tabla" (tabla = costo + vengo)
    }
};

int main()
{
    // GrafoMatrizImp *g = new GrafoMatrizImp(10, false, false);
    // g->aniadirArista(1, 2, 5);

    // g->imprimirGrafo();

    GrafoListaAdyImp *g = new GrafoListaAdyImp(10, true, false);
    g->aniadirArista(1, 3);
    g->aniadirArista(1, 1);
    g->aniadirArista(1, 2);
    g->aniadirArista(4, 1);

    g->imprimirGrafo();

    return 0;
}