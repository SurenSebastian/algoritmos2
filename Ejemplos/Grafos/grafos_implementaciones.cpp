#include <climits>
#include <string>
#include <iostream>
using namespace std;

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
};

// defino mi Infinito como el maximo inf posible
#define INF INT_MAX;

class GrafoMatrizImp : public Grafo
{
private:
    int **matriz;
    int V;
    int A;

    bool esDirigido;
    bool esPonderado;

public:
    // Crea un grafo con V vertices (del 1 al V)
    // O(v^2)
    GrafoMatrizImp(int cantidadDeVertices, bool esDirigido = true, bool esPonderado = true)
    {
        this->V = cantidadDeVertices;
        this->A = 0;
        this->esDirigido = esDirigido;
        this->esPonderado = esPonderado;

        matriz = new int *[V + 1];
        for (int i = 1; i <= V; i++)
        {
            matriz[i] = new int[V + 1];
            for (int j = 1; j <= V; j++)
            {
                matriz[i][j] = INF;
            }
        }
    }
    // O(1)
    void aniadirArista(int v, int w, int peso = 1)
    {
        matriz[v][w] = esPonderado ? peso : 1;
        if (!esDirigido)
        {
            matriz[w][v] = esPonderado ? peso : 1;
        }
        this->A++;
    }
    // O(V)
    ListaAristas adyacentesA(int origen)
    {
        int inf = INF;
        ListaAristas listaRetorno = NULL;
        for (int i = 1; i <= V; i++)
        {
            if (matriz[origen][i] != inf)
            {
                Arista aux(origen, i, matriz[origen][i]);
                listaRetorno = new NodoLista<Arista>(aux, listaRetorno);
            }
        }
        return listaRetorno;
    }
    // O(V^2)
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
};

class GrafoListaAdyImp : public Grafo
{
private:
    ListaAristas * listaAdy;
    int V;
    int A;

    bool esDirigido;
    bool esPonderado;

public:
    // Crea un grafo con V vertices (del 1 al V)
    // O(v)
    GrafoListaAdyImp(int cantidadDeVertices, bool esDirigido = true, bool esPonderado = true)
    {
        this->V = cantidadDeVertices;
        this->A = 0;
        this->esDirigido = esDirigido;
        this->esPonderado = esPonderado;

        listaAdy = new ListaAristas[V+1];
        for (int i = 1; i <= V; i++)
        {
            listaAdy[i] = NULL;
        }
        
    }
    // O(1)
    void aniadirArista(int v, int w, int peso = 1)
    {
        int pesoArista = this->esPonderado ? peso : 1;
        Arista a1(v,w,pesoArista);
        listaAdy[v] = new NodoLista<Arista>(a1,listaAdy[v]);
        if (!esDirigido)
        {
            Arista a2(w,v,pesoArista);
            listaAdy[w] = new NodoLista<Arista>(a2,listaAdy[w]);
        }
    }
    // O(V) pc
    ListaAristas adyacentesA(int origen)
    {
        // copio la lista
        ListaAristas listaRetorno = NULL;
        ListaAristas listaAuxiliar = listaAdy[origen];

        while(listaAuxiliar != NULL) {
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