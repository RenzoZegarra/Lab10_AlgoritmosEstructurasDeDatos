package btree;

import java.util.ArrayList;

public class BNode<E extends Comparable<E>> {
    //Creacion de las llaves, hijos y contador
    protected ArrayList<E> keys;
    protected ArrayList<BNode<E>> childs;
    protected int count;
    //Agregacion del atributo idNode que aumenta cada que
        //se crea un nuevo nodo
    protected int idNode;
    //Variable estatica que cuenta los nodos creados
    private static int countNode = 0;

    //Constructor
    public BNode(int n){
        this.keys = new ArrayList<E> (n);
        this.childs = new ArrayList<BNode<E>> (n);
        this.count = 0;
        //Incrementa el ID unico para cada nodo
        this.idNode = ++countNode;
        for(int i = 0; i <= n; i++){ //Se agrega el <= en vez de <
            this.keys.add(null);
            this.childs.add(null);
        }
    }
    //Verifica si el nodo actual existe
    public boolean nodeEmpty(){
        return count == 0;
    }

    //Busca una llave para el nodo actual
        //Si lo encuentra devuelve true
        //Si no devuelve false y la posicion donde el hijo deberia descender
    public boolean searchNode(E key, int[] pos)
    {
        int i = 0;
        while(i < count && keys.get(i).compareTo(key) < 0){
            i++;
        }
        if (i < count && keys.get(i).compareTo(key) == 0) {
            pos[0] = i;
            return true;
        }
        else {
            pos[0] = i; // Posición donde debería descender
            return false;
        }
        
    }

    // Verifica si el nodo está lleno con respecto al número máximo de claves
    public boolean nodeFull(int max) {
        return count >= max;
    }


    //Devuelve la llave encontrada por el nodo
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Nodo ID: ").append(idNode).append(" | Claves: ");
        for (int i = 0; i < count; i++) 
        {
            sb.append(keys.get(i)).append(" ");
        }
        return sb.toString().trim();
    }
}
