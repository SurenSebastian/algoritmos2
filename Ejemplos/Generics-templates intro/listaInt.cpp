#include <iostream>
#include <cassert>
using namespace std;

class Lista
{
    public:
        virtual void insertar(int el) = 0;
        virtual void eliminar(int el) = 0;
        virtual void imprimir() = 0;
};

struct NodoLista
{
    int dato;
    NodoLista *sig;
    NodoLista(int &unDato) : dato(unDato), sig(0){};
    NodoLista(int &unDato, NodoLista *unSig) : dato(unDato), sig(unSig){};
};

class ListaImp: public Lista
{
private:
    NodoLista *ppio;
    void eliminar(int el, NodoLista *&nodo)
    {
        if (nodo != 0)
        {
            if (nodo->dato == el)
            {
                NodoLista *aEliminar = nodo;
                nodo = nodo->sig;
                delete aEliminar;
            }
            else
            {
                eliminar(el, nodo->sig);
            }
        }
    }

public:
    ListaImp()
    {
        ppio = 0;
    }
    void insertar(int el)
    {
        this->ppio = new NodoLista(el, this->ppio);
    }
    void eliminar(int el)
    {
        eliminar(el, this->ppio);
    }
    void imprimir()
    {
        NodoLista *aux = this->ppio;
        while (aux != 0)
        {
            cout << aux->dato << endl;
            aux = aux->sig;
        }
    }
};

int main()
{
    Lista * listaDeEnteros = new ListaImp();
    listaDeEnteros->insertar(1);
    listaDeEnteros->insertar(2);
    listaDeEnteros->insertar(3);
    listaDeEnteros->insertar(4);
    listaDeEnteros->insertar(5);
    listaDeEnteros->insertar(6);
    listaDeEnteros->insertar(7);

    listaDeEnteros->imprimir();
    return 0;
}
