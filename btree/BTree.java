package btree;

import org.w3c.dom.Node;

public class BTree<E extends Comparable<E>> {
    //Se agregan los atributos
    private BNode<E> root;
    private int orden;
    private boolean up;
    private BNode<E> nDes;

    //Constructor de orden y raiz
    public BTree (int orden){
        this.orden = orden;
        this.root = null;
    }

    public boolean isEmpty(){
        return this.root == null;
    }

    public void insert(E cl){
        up = false;
        E mediana;
        BNode<E> pnew;
        mediana = push(this.root, cl);
        if(up){
            pnew = new BNode<E>(this.orden);
            pnew.count = 1;
            pnew.keys.set(0, mediana);
            pnew.childs.set(0, this.root);
            pnew.childs.set(1, nDes);
            this.root = pnew;
        }
    }

    private E push(BNode<E> current, E cl)
    {
        int pos[] = new int[1];
        E mediana;
        if(current == null){
            up = true;
            nDes = null;
            return cl;
        }
        else{
            boolean fl;
            fl = current.searchNode(cl, pos);
            if(fl){
                System.out.println("Item duplicado\n");
                up = false;
                return null;
            }
            mediana = push(current.childs.get(pos[0]),cl);
            if(up){
                if(current.nodeFull(this.orden-1))
                    mediana = divideNode(current, mediana, pos[0]);
                else{
                    up = false;
                    putNode(current,mediana,nDes,pos[0]);
                }
            }
            return mediana;
        }
    }

    private void putNode(BNode<E> current, E cl, BNode<E> rd, int k){
        int i;
        for(i = current.count-1; i >= k; i--){
            current.keys.set(i+1,current.keys.get(i));
            current.childs.set(i+2,current.childs.get(i+1));
        }
        current.keys.set(k,cl);
        current.childs.set(k+1,rd);
        current.count++;
    }

    private E divideNode(BNode<E> current, E cl, int k){
        BNode<E> rd = nDes;
        int i, posMdna;
        posMdna = (k <= this.orden/2) ? this.orden/2 : this.orden/2+1;
        nDes = new BNode<E>(this.orden);
        for(i = posMdna; i < this.orden-1; i++)
        { 
            nDes.keys.set(i-posMdna,current.keys.get(i));
            nDes.childs.set(i-posMdna+1,current.childs.get(i+1));
        }
        nDes.count = (this.orden - 1) - posMdna;
        current.count = posMdna;
        if(k <= this.orden/2)
            putNode(current,cl,rd,k);
        else
            putNode(nDes,cl,rd,k-posMdna);
        E median = current.keys.get(current.count-1);
        nDes.childs.set(0,current.childs.get(current.count));
        current.count--;
        return median;

        
    }

    //EJERCICIO 1: Metodo Search
    public boolean search(E cl){
        //Se busca por la raiz
        if(root == null){
            return false;
        }
        else
        {
            return search(root, cl);
        }
    }
    //EJERCICIO 1: METODO SEARCH
    //Comparacion de las claves del nodo.
    private boolean search(BNode<E> node, E cl)
    {
        int i = 0;
        //Busca la posicion donde podria estar.
        while (i < node.count && cl.compareTo(node.keys.get(i)) > 0) 
        { 
            i++;    
        }
        //Si se encuentra la clave
        if(i < node.count && cl.compareTo(node.keys.get(i)) == 0){
            System.out.println(cl+ " se encuentra en el nodo: " + node.idNode + ", en la posicion: "+ i);
            return true;
        }
        //Si no se encuentra la clave
        if(node.childs.get(i) == null){
            return false;
        }

        //Continua con la busqueda de forma recursiva
        return search(node.childs.get(i), cl);
    }
    

    //EJERCICIO 2: Metodo Remove
    public void remove(E cl){
        if(root == null){
            System.out.println("El arbol esta vacio.");
            return;
        }

        remove(root, cl);

        //Si la raiz esta vacia y tiene un solo hijo
            //Se hace hijo a la nueva raiz.
        if (root.count == 0) {
            if(root.childs.get(0) != null){
                root = root.childs.get(0);
            }
            else{
                root = null;
            }
        }
    }

    //Metodos Axuliares
    private E getPredecessor(BNode<E> node, int index) {
        BNode<E> current = node.childs.get(index);
        while (current.childs.get(current.count) != null) {
            current = current.childs.get(current.count);
        }
        return current.keys.get(current.count - 1);
    }

    private void fixChild(BNode<E> parent, int idx) {
        int minKeys = (orden + 1) / 2 - 1;

        // Intentar redistribuir con hermano izquierdo
        if (idx > 0 && parent.childs.get(idx - 1).count > minKeys) {
            redistributeLeft(parent, idx);
        }
        // Intentar redistribuir con hermano derecho
        else if (idx < parent.count && parent.childs.get(idx + 1).count > minKeys) {
            redistributeRight(parent, idx);
        }
        // Fusionar con hermano izquierdo
        else if (idx > 0) {
            merge(parent, idx - 1);
        }
        // Fusionar con hermano derecho
        else {
            merge(parent, idx);
        }
    }
    private void merge(BNode<E> parent, int idx) {
        BNode<E> left = parent.childs.get(idx);
        BNode<E> right = parent.childs.get(idx + 1);

        // Agregar la clave del padre entre los dos
        left.keys.set(left.count, parent.keys.get(idx));
        left.count++;

        // Mover claves y hijos del nodo derecho al izquierdo
        for (int i = 0; i < right.count; i++) {
            left.keys.set(left.count, right.keys.get(i));
            left.childs.set(left.count, right.childs.get(i));
            left.count++;
        }
        // No olvidar el último hijo
        left.childs.set(left.count, right.childs.get(right.count));

        // Eliminar clave del padre
        for (int i = idx; i < parent.count - 1; i++) {
            parent.keys.set(i, parent.keys.get(i + 1));
            parent.childs.set(i + 1, parent.childs.get(i + 2));
        }
        parent.keys.set(parent.count - 1, null);
        parent.childs.set(parent.count, null);
        parent.count--;

        // Si el padre queda vacío y es la raíz
        if (parent == root && parent.count == 0) {
            root = left;
        }
    }

    private void redistributeLeft(BNode<E> parent, int idx) {
        BNode<E> left = parent.childs.get(idx - 1);
        BNode<E> current = parent.childs.get(idx);

        // Mover claves/hijos una posición hacia la derecha
        for (int i = current.count; i > 0; i--) {
            current.keys.set(i, current.keys.get(i - 1));
            current.childs.set(i + 1, current.childs.get(i));
        }
        current.childs.set(1, current.childs.get(0));

        // Insertar clave del padre
        current.keys.set(0, parent.keys.get(idx - 1));
        current.childs.set(0, left.childs.get(left.count));

        current.count++;

        // Mover la última clave del hermano izquierdo al padre
        parent.keys.set(idx - 1, left.keys.get(left.count - 1));
        left.keys.set(left.count - 1, null);
        left.childs.set(left.count, null);
        left.count--;
    }

    private void redistributeRight(BNode<E> parent, int idx) {
        BNode<E> right = parent.childs.get(idx + 1);
        BNode<E> current = parent.childs.get(idx);

        // Agregar clave del padre
        current.keys.set(current.count, parent.keys.get(idx));
        current.childs.set(current.count + 1, right.childs.get(0));
        current.count++;

        // Mover la primera clave del hermano derecho al padre
        parent.keys.set(idx, right.keys.get(0));

        // Desplazar claves e hijos a la izquierda en el hermano derecho
        for (int i = 0; i < right.count - 1; i++) {
            right.keys.set(i, right.keys.get(i + 1));
            right.childs.set(i, right.childs.get(i + 1));
        }
        right.childs.set(right.count - 1, right.childs.get(right.count));
        right.keys.set(right.count - 1, null);
        right.childs.set(right.count, null);
        right.count--;
    }




    //EJERCICIO 2:
    private void remove(BNode<E> node, E cl) {
    int i = 0;
    while (i < node.count && cl.compareTo(node.keys.get(i)) > 0) {
        i++;
    }

    if (i < node.count && cl.compareTo(node.keys.get(i)) == 0) {
        // Caso 1: Clave está en este nodo
        if (node.childs.get(i) == null) {
            // Caso 1a: Es hoja
            for (int j = i; j < node.count - 1; j++) {
                node.keys.set(j, node.keys.get(j + 1));
            }
            node.keys.set(node.count - 1, null);
            node.count--;
        } else {
            // Caso 1b: Nodo interno
            E pred = getPredecessor(node, i);
            node.keys.set(i, pred);
            remove(node.childs.get(i), pred);
        }
    } else {
        // Caso 2: Clave no está en este nodo
        if (node.childs.get(i) == null) {
            System.out.println("La clave no se encuentra en el árbol.");
            return;
        }

        // Antes de continuar, asegurar que el hijo tiene el mínimo necesario
        if (node.childs.get(i).count < (orden / 2)) {
            fixChild(node, i);
        }

        remove(node.childs.get(i), cl);
    }
}


    @Override
    public String toString() {
        if (isEmpty()) {
            return "Árbol B vacío.";
        } else {
            return writeTree(this.root);
        }
    }

    private String writeTree(BNode<E> current) {
        if (current == null) return "";
    
        StringBuilder sb = new StringBuilder();
        sb.append("Nodo ID: ").append(current.idNode).append(" | Claves: ");
        for (int i = 0; i < current.count; i++) {
            sb.append(current.keys.get(i)).append(" ");
        }
        sb.append("\n");

        for (int i = 0; i <= current.count; i++) {
            BNode<E> child = current.childs.get(i);
            if (child != null) {
                sb.append(writeTree(child));
            }
        }
        return sb.toString();
    }



    
}
