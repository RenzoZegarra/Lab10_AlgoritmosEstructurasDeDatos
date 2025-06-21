import btree.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Ingrese el orden del árbol B:");
        int orden = sc.nextInt();
        sc.nextLine(); 

        BTree<Integer> arbol = new BTree<>(orden);

        boolean salir = false;

        while (!salir) {
            System.out.println("\n--- Menú ---");
            System.out.println("1. Insertar elemento");
            System.out.println("2. Mostrar árbol");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            String opcion = sc.nextLine();

            switch (opcion) {
                case "1":
                    System.out.print("Ingrese un número a insertar: ");
                    try {
                        int num = Integer.parseInt(sc.nextLine());
                        arbol.insert(num);
                        System.out.println("Elemento insertado.");
                    } catch (NumberFormatException e) 
                    {
                        System.out.println("Entrada inválida. Ingrese un número.");
                    }
                    break;

                case "2":
                    System.out.println("\nContenido del árbol B:");
                    System.out.println(arbol.toString());
                    break;

                case "3":
                    salir = true;
                    System.out.println("Saliendo del programa.");
                    break;

                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
                    break;
            }
        }

        sc.close();
    }
}
