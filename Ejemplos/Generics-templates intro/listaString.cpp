#include <iostream>
#include <cassert>
using namespace std;

class Lista
{
    public:
        virtual void insertar(string el) = 0;
        virtual void eliminar(string el) = 0;
        virtual void imprimir() = 0;
};

struct NodoLista
{
    string dato;
    NodoLista *sig;
    NodoLista(string &unDato) : dato(unDato), sig(0){};
    NodoLista(string &unDato, NodoLista *unSig) : dato(unDato), sig(unSig){};
};

class ListaImp: public Lista
{
private:
    NodoLista *ppio;
    void eliminar(string el, NodoLista *&nodo)
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
    void insertar(string el)
    {
        this->ppio = new NodoLista(el, this->ppio);
    }
    void eliminar(string el)
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
    Lista * listaDeString = new ListaImp();
    listaDeString->insertar("UNO");
    listaDeString->insertar("DOS");
    listaDeString->insertar("TRES");
    listaDeString->insertar("CUATRO");
    listaDeString->insertar("CINCO");
    listaDeString->insertar("SEIS");
    listaDeString->insertar("SIETE");

    listaDeString->imprimir();
    return 0;
}
