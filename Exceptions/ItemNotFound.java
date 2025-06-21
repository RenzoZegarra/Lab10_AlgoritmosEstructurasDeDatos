package Exceptions;

public class ItemNotFound extends Exception {
    public ItemNotFound(String msg){
        super(msg);
    }
    public ItemNotFound(){
        super("Item no encontrado en el BST.");
    }
}

