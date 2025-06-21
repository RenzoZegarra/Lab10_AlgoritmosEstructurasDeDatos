package Exceptions;

public class ItemDuplicated extends Exception {
    public ItemDuplicated(String msg){
        super(msg);
    }
    public ItemDuplicated(){
        super("Item ya existe en el BST.");
    }
}

