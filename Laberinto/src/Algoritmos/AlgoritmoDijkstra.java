/*
 *  Creado por: David Pérez Sánchez y Fer  :v
 *  Matrícula: 163202
 *  Materia: Estructura de Datos Avanzada
 *  Universidad Politécnica de Chiapas.
 *  Fecha de Creación: 07/04/2018
 */
package Algoritmos;

import Otros.Archivo;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @author David Pérez S.
 */
public class AlgoritmoDijkstra {

    //similar a los defines de C++
    private final int MAX = 10005;  //maximo numero de vértices
    private final int INF = 1<<30;  //definimos un valor grande que represente la distancia infinita inicial, basta conque sea superior al maximo valor del peso en alguna de las aristas
    
    private final List< List< Node > > ady; //lista de adyacencia
    private final int distancia[ ] = new int[ MAX ];          //distancia[ u ] distancia de vértice inicial a vértice con ID = u
    private final boolean visitado[ ] = new boolean[ MAX ];   //para vértices visitados
    private final PriorityQueue< Node > Q = new PriorityQueue<>(); //priority queue propia de Java, usamos el comparador definido para que el de menor valor este en el tope
    private final int V;                                      //numero de vertices
    private final Integer previo[] = new Integer[ MAX ];              //para la impresion de caminos
    private boolean dijkstraEjecutado;
    
    AlgoritmoDijkstra(int V, List< List< Node > > listaAdyacencia){
        this.V = V;
        this.ady= listaAdyacencia;
        dijkstraEjecutado = false;
    }
    
    public void ejecutarAlgoritmo(int verticeDestino) throws FileNotFoundException{
          dijkstra(1);
          //printShortestPath(verticeDestino);
          serializarArchivosDeImportancia();
    }
    
      private void serializarArchivosDeImportancia() throws FileNotFoundException {
            Archivo<Integer[]> archivo2 = new Archivo<>("ShortestPath.bin");
            archivo2.crearArchivoVacio(true);
            archivo2.serializar(this.previo);
      }
    
    //función de inicialización
    private void init(){
        for( int i = 0 ; i <= V ; ++i ){
            distancia[ i ] = INF;  //inicializamos todas las distancias con valor infinito
            visitado[ i ] = false; //inicializamos todos los vértices como no visitados
            previo[ i ] = -1;      //inicializamos el previo del vertice i con -1
        }
    }

    //Paso de relajacion
    private void relajacion( int actual , int adyacente , int peso ){
        //Si la distancia del origen al vertice actual + peso de su arista es menor a la distancia del origen al vertice adyacente
        if( distancia[ actual ] + peso < distancia[ adyacente ] ){
            distancia[ adyacente ] = distancia[ actual ] + peso;  //relajamos el vertice actualizando la distancia
            previo[ adyacente ] = actual;                         //a su vez actualizamos el vertice previo
            Q.add( new Node( adyacente , distancia[ adyacente ] ) ); //agregamos adyacente a la cola de prioridad
        }
    }

    private void dijkstra( int inicial ){
        init(); //inicializamos nuestros arreglos
        Q.add( new Node( inicial , 0 ) ); //Insertamos el vértice inicial en la Cola de Prioridad
        distancia[ inicial ] = 0;      //Este paso es importante, inicializamos la distancia del inicial como 0
        int actual , adyacente , peso;
        while( !Q.isEmpty() ){                   //Mientras cola no este vacia
            actual = Q.element().getFirst();            //Obtengo de la cola el nodo con menor peso, en un comienzo será el inicial
            Q.remove();                           //Sacamos el elemento de la cola
            if( visitado[ actual ] ) continue; //Si el vértice actual ya fue visitado entonces sigo sacando elementos de la cola
            visitado[ actual ] = true;         //Marco como visitado el vértice actual

            for( int i = 0 ; i < ady.get( actual ).size() ; ++i ){ //reviso sus adyacentes del vertice actual
                adyacente = ady.get( actual ).get( i ).getFirst();   //id del vertice adyacente
                peso = ady.get( actual ).get( i ).getSecond();        //peso de la arista que une actual con adyacente ( actual , adyacente )
                if( !visitado[ adyacente ] ){        //si el vertice adyacente no fue visitado
                    relajacion( actual , adyacente , peso ); //realizamos el paso de relajacion
                }
            }
        }
//        System.out.printf( " Distancias más cortas iniciando en vértice %d\n\n" , inicial );
//        for( int i = 1 ; i <= V ; ++i ){
//            System.out.printf(" A vértice %d , distancia más corta = %d\n" , i , distancia[ i ] );
//        }
        dijkstraEjecutado = true;
    }
    
    private void printShortestPath(int verticeDestino){
        if( !dijkstraEjecutado ){
            System.out.println(" Es necesario ejecutar el algorithmo de Dijkstra antes de poder imprimir el camino mas corto");
            return;
        }
          System.out.println("\n Para llegar a la salida tiene que ir por los vértices:");
        print( verticeDestino );
        System.out.printf("\n");
    }
    
    //Impresion del camino mas corto desde el vertice inicial y final ingresados
    private void print( int destino ){
        if( previo[ destino ] != -1 )    //si aun poseo un vertice previo
            print( previo[ destino ] );  //recursivamente sigo explorando
        System.out.printf(" %d, " , destino );        //terminada la recursion imprimo los vertices recorridos
    }
}