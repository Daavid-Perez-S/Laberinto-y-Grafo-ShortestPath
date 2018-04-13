/*
 *  Creado por: David Pérez Sánchez y Fer  :v
 *  Matrícula: 163202
 *  Materia: Estructura de Datos Avanzada
 *  Universidad Politécnica de Chiapas.
 *  Fecha de Creación: 08/04/2017
 */

package Algoritmos;

import java.io.Serializable;

/**
 * @author David Pérez S.
 */
public class Node implements Comparable<Node>, Serializable {
      
        private final int first;
        private final int second;
        
        Node( int d , int p ){      //constructor
            this.first = d;
            this.second = p;
        }
        public int getFirst(){
              return this.first;
        }
        public int getSecond(){
              return this.second;
        }
        @Override
        public int compareTo(Node other){              // Es necesario definir un comparador para el correcto funcionamiento del PriorityQueue
            if( second > other.second ) return 1;
            if( second == other.second ) return 0;
            return -1;
        }
        @Override
        public String toString(){
              return " Vertice  " +  Integer.toString(this.first) + " - Peso " + Integer.toString(this.second);
        }
}