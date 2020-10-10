import java.util.Scanner;

class Grafo {
    int INF = Integer.MAX_VALUE;
    int V = 7;
    int[][] grafo = {
        //0  1   2   3   4   5   6   7
        {INF,INF,INF,INF,INF,INF,INF,INF}, // 0
        {INF,INF,  2,INF,  1,INF,INF,INF}, // 1
        {INF,INF,INF,INF,  3, 10,INF,INF}, // 2
        {INF,  4,INF,INF,INF,INF,  5,INF}, // 3
        {INF,INF,INF,  2,INF,  2,  8,  4}, // 4
        {INF,INF,INF,INF,INF,INF,INF,  6}, // 5
        {INF,INF,INF,INF,INF,INF,INF,INF}, // 6
        {INF,INF,INF,INF,INF,INF,  1,INF}  // 7
    };

    boolean[] initVisitados() {
        boolean[] ret = new boolean[V+1];
        for(int i=1; i<=V; i++){
            ret[i] = false;
        }
        return ret;
    }

    int[] initCostos(int origen){
        int[] ret = new int[V+1];
        for(int i=1; i<=V; i++){
            ret[i] = i == origen ? 0 : INF;
        }
        return ret;
    }

    int[] initVengo(){
        int[] ret = new int[V+1];
        for(int i=1; i<=V; i++){
            ret[i] = -1;
        }
        return ret;
    }

    int verticesDesconocidoDeMenorCosto(boolean[] vistados, int[] costos) {
        int menorVerticeNoConocido = -1;
        int menorCosto = INF;
        for(int i=1; i<=V; i++) {
            if(!vistados[i] && costos[i] < menorCosto) {
                menorVerticeNoConocido = i;
                menorCosto = costos[i];
            }
        }
        return menorVerticeNoConocido;
    }

    void icRec(int origen, int destino, int[] vengo) {
        if(origen == destino) {
            System.out.print(origen + "->");
        }else {
            icRec(origen, vengo[destino], vengo);
            System.out.print(destino + "->");
        }
    }

    void imprimirCamino(int origen, int destino, int[] costos, int[] vengo) {
        if(costos[destino] == INF) {
            System.out.println("No existe camino desde " + origen + " a " + destino);
        }else {
            System.out.println("El camino + corto desde " + origen + " a " + destino + " tiene peso " + costos[destino]);
            icRec(origen, destino, vengo);
            System.out.println("");
            System.out.println("");
        }
    }

    void dijkstra(int origen) {
        boolean[] visitados = initVisitados();
        int[] costos = initCostos(origen);
        int[] vengo = initVengo();

        for(int i=1; i<=V; i++) {
            int v = verticesDesconocidoDeMenorCosto(visitados, costos);
            if(v != -1) {
                visitados[v] = true;
                for(int w=1; w<=V; w++) {
                    if(grafo[v][w] != INF) {
                        if(costos[w] > costos[v] + grafo[v][w]) {
                            costos[w] = costos[v] + grafo[v][w];
                            vengo[w] = v;
                        }
                    }
                }
            }
        }

        for(int i=1; i<=V; i++) {
            imprimirCamino(origen, i, costos, vengo);
        }
    }
}

public class Grafos_Dijkstra {
    public static void main(String[] args) {
        Grafo g= new Grafo();
        g.dijkstra(3);
    }
}