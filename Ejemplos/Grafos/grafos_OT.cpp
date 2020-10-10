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

    // Extras
    virtual void orderTopologico() = 0;
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

    int *initGradoDeEntrada()
    {
        int *ret = new int[V + 1]();
        for (int i = 1; i <= V; i++)
        {
            ListaAristas adyacentes = this->adyacentesA(i);
            while (adyacentes != NULL)
            {
                Arista arista = adyacentes->dato;
                ret[arista.destino]++;
                adyacentes = adyacentes->sig;
            }
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

    void orderTopologico()
    {
        int *gradoEntranda = initGradoDeEntrada(); // inicializa el grado de entrada de cada uno de ls vertices del grafo
        Cola<int> cola;
        int vertice, cont = 0;
        for (int v = 1; v <= V; v++) // encolo todos los vertices de grado de entrada 0
            if (gradoEntranda[v] == 0)
                cola.encolar(v);

        while (!cola.estaVacia())
        {
            vertice = cola.desencolar();
            cont++;
            cout << vertice << endl; // proceso el vertice
            // recorrer los adyecentes al vertice
            ListaAristas adyacentes = this->adyacentesA(vertice);
            while (adyacentes != NULL)
            {
                Arista a = adyacentes->dato;
                int w = a.destino;
                gradoEntranda[w]--; // quito un grado de entrada
                if (gradoEntranda[w] == 0)
                {
                    cola.encolar(w); // el vertice w esta listo para imprimir
                }
                adyacentes = adyacentes->sig;
            }
        }
        if (cont < V)
            cout << "ERROR: el grafo tiene ciclos" << endl;
    }
};

int main()
{
    // GrafoMatrizImp *g = new GrafoMatrizImp(10, false, false);
    // g->aniadirArista(1, 2, 5);

    // g->imprimirGrafo();

    GrafoListaAdyImp *g = new GrafoListaAdyImp(4, true, false);
    g->aniadirArista(1, 3);
    g->aniadirArista(1, 2);
    g->aniadirArista(2, 3);
    g->aniadirArista(2, 4);
    g->aniadirArista(3, 4);
    g->aniadirArista(4, 2);

    g->orderTopologico();

    return 0;
}