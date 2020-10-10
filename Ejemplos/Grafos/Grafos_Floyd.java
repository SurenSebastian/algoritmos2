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

    int[][] generarMatrizAdy() {
        int[][] ret = new int[V+1][V+1];
        for(int i=1; i<=V; i++) {
            for(int j=1; j<=V; j++) {
                ret[i][j] = i==j ? INF : grafo[i][j];
            }
        }
        return ret;
    }

    int[][] initMatrizVengo() {
        int[][] ret = new int[V+1][V+1];
        for(int i=1; i<=V; i++) {
            for(int j=1; j<=V; j++) {
                ret[i][j] = grafo[i][j] != INF ? i : -1;
            }
        }
        return ret;
    }

    void floyd() {
        int[][] matriz = generarMatrizAdy();
        int[][] vengo =  initMatrizVengo();

        for(int k=1; k<=V; k++) {
            for(int i=1; i<=V; i++) {
                for(int j=1; j<=V; j++) {
                    if(matriz[i][k]!=INF && matriz[k][j]!= INF && matriz[i][j] > matriz[i][k] + matriz[k][j]) {
                        matriz[i][j] = matriz[i][k] + matriz[k][j];
                        vengo[i][j] = k;
                    }
                }
            }
        }

// imprimirCamino(1, 3, matriz[1], vengo[1]);
        for(int i=1; i<=V; i++) {
            System.out.println("Desde origen " + i + "\n");
            for(int j=1; j<=V; j++) {
                if(i != j) {
                    imprimirCamino(i, j, matriz[i], vengo[i]);
                }
            }
            System.out.println("+_+_+_+_+_+_+_+_+_+_+_+_+_\n");
        }
    }
}

public class Grafos_Floyd {
    public static void main(String[] args) {
        Grafo g= new Grafo();
        g.floyd();
    }
}