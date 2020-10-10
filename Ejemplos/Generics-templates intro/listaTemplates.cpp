#include <iostream>
#include <cassert>
using namespace std;

template <class T>
class Lista
{
public:
    virtual void insertar(T el) = 0;
    virtual void eliminar(T el) = 0;
    virtual void imprimir() = 0;
};

template <class T>
struct NodoLista
{
    T dato;
    NodoLista<T> *sig;
    NodoLista(T &unDato) : dato(unDato), sig(0){};
    NodoLista(T &unDato, NodoLista<T> *unSig) : dato(unDato), sig(unSig){};
};

template <class T>
class ListaImp : public Lista<T>
{
private:
    NodoLista<T> *ppio;
    void eliminar(T el, NodoLista<T> *&nodo)
    {
        if (nodo != 0)
        {
            if (nodo->dato == el)
            {
                NodoLista<T> *aEliminar = nodo;
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
    void insertar(T el)
    {
        this->ppio = new NodoLista<T>(el, this->ppio);
    }
    void eliminar(T el)
    {
        eliminar(el, this->ppio);
    }
    void imprimir()
    {
        NodoLista<T> *aux = this->ppio;
        while (aux != 0)
        {
            cout << aux->dato << endl;
            aux = aux->sig;
        }
    }
};
struct Tarea
{
public:
    int prioridad;
    string nombre;
};

struct heap
{
private:
    Tarea *tareas;

public:
    int imprimirTareas();
};

class Persona
{
private:
    string nombre;

public:
    Persona(string unNombre)
    {
        nombre = unNombre;
    }

    friend bool operator==(const Persona &lhs, const Persona &rhs);
    friend ostream &operator<<(ostream &out, const Persona &p);
};

ostream &operator<<(ostream &out, const Persona &p)
{
    out << p.nombre;
    return out;
}

bool operator==(const Persona &lhs, const Persona &rhs)
{
    return lhs.nombre.compare(rhs.nombre) == 0;
}

int main()
{
    Lista<string> *listaDeString = new ListaImp<string>();
    listaDeString->insertar("UNO");
    listaDeString->insertar("DOS");
    listaDeString->insertar("TRES");
    listaDeString->insertar("CUATRO");
    listaDeString->insertar("CINCO");
    listaDeString->insertar("SEIS");
    listaDeString->insertar("SIETE");

    listaDeString->eliminar("SEIS");

    listaDeString->imprimir();

    Lista<int> *listaDeEnteros = new ListaImp<int>();
    listaDeEnteros->insertar(1);
    listaDeEnteros->insertar(2);
    listaDeEnteros->insertar(3);
    listaDeEnteros->insertar(4);
    listaDeEnteros->insertar(5);
    listaDeEnteros->insertar(6);
    listaDeEnteros->insertar(7);

    listaDeEnteros->eliminar(6);

    listaDeEnteros->imprimir();

    Lista<Persona> *listaDePersonas = new ListaImp<Persona>();
    Persona per1("Pepe");
    Persona per2("Juan");
    Persona per3("Main");

    listaDePersonas->insertar(per1);
    listaDePersonas->insertar(per2);
    listaDePersonas->insertar(per3);

    Persona per4("Juan");
    listaDePersonas->eliminar(per4);

    listaDePersonas->imprimir();

    return 0;
}
