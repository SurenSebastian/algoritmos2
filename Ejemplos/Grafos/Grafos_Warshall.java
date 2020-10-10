import java.util.Scanner;

class Grafo {
    int INF = Integer.MAX_VALUE;
    int V;
    int[][] grafo;

    Grafo(int cantidadDeVertices) {
        V =  cantidadDeVertices;
        grafo = new int[V+1][V+1];
        for(int i=1; i<=V; i++) {
            for(int j=1; j<=V; j++) {
                grafo[i][j] = INF;
            }
        }
    }

    void aniadirArista(int origen, int destino) {
        grafo[origen][destino] = 1;
    }

    private boolean[][] initMatriz(boolean tratarComoDirigida) {
        boolean[][] ret = new boolean[V+1][V+1];
        for(int i=1; i<=V; i++) {
            for(int j=1; j<=V; j++) {
                if(tratarComoDirigida) {
                    ret[i][j] = grafo[i][j] != INF;
                }else{
                    ret[i][j] = grafo[i][j] != INF || grafo[j][i] != INF;
                }
            }
        }
        return ret;
    }

    private void warshall(boolean[][] matriz) {
        // imprimirMatriz(matriz); antes de warshall
        for(int k=1; k<=V; k++) {
            for(int i=1; i<=V; i++) {
                for(int j=1; j<=V; j++) {
                    matriz[i][j] = matriz[i][j] || matriz[i][k] && matriz[k][j];
                }
            }
        }
        // imprimirMatriz(matriz); despues de warshall
    }

    private void imprimirMatriz(boolean[][] matriz) {
        System.out.println("");
        for(int i=1; i<=V; i++) {
            for(int j=1; j<=V; j++) {
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println("");
    }

    private boolean matrizCompleta(boolean[][] matriz) {
        boolean esCompleta = true;
        for(int i=1; i<=V; i++) {
            for(int j=1; j<=V; j++) {
                esCompleta = esCompleta && matriz[i][j];
            }
        }
        return esCompleta;
    }

    void conexidad() {
        boolean[][] matriz = initMatriz(true);
        boolean[][] matrizNoDirigida = initMatriz(false);

        warshall(matriz);
        if(matrizCompleta(matriz)) {
            System.out.println("1"); // fuertemente conexa
        }else{
            warshall(matrizNoDirigida);
            if(matrizCompleta(matrizNoDirigida)) {
                System.out.println("2"); // debilmente conexa
            }else {
                System.out.println("3");
            }
        }
    }
}


public class Grafos_Warshall {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int V = sc.nextInt();
        Grafo g = new Grafo(V);
        int A = sc.nextInt();
        for(int i=0; i<A; i++) {
            int origen = sc.nextInt();
            int destino = sc.nextInt();
            g.aniadirArista(origen, destino);
        }
        g.conexidad();
    }
}