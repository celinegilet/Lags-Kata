package com.tof.app;

class Program {

    static final boolean debug = true;

    // ==================
    // fonction principale
    // ===================

    public static void main(String[] args) {
        Console console = new Console();

        String fileName = "ORDRES.CSV";
        FileHandler fileHandler = new FileHandler(console, fileName);

        LagsService service = new LagsService(fileHandler, console);

        boolean flag = false;

        // tant que ce n'est pas la fin du programme
        while (!flag) {
            // met la commande à Z
            char commande = 'Z';
            while (commande != 'A' && commande != 'L' && commande != 'S' && commande != 'Q' && commande != 'C') {
                System.out.println("A)JOUTER UN ORDRE  L)ISTER   C)ALCULER CA  S)UPPRIMER  Q)UITTER");
                try {
                    char keyInfo = (char)System.in.read();
                    commande = Character.toUpperCase(keyInfo);
                    System.out.print(commande);
                } catch (java.io.IOException e) {
                    console.print("IO Exception");
                }
                switch(commande) {
                    case 'Q':
                        {
                            flag = true;
                            break;
                        }
                    case 'L':
                        {
                            service.displayOrders();
                            break;
                        }
                    case 'A':
                        {
                            service.addOrder();
                            break;
                        }
                    case 'S':
                        {
                            service.deleteOrder();
                            break;
                        }
                    case 'C':
                        {
                            console.println("CALCUL CA..");
                            double sales = service.calculateSales(debug);
                            console.format("CA: %10.2f\n", sales);
                            break;
                        }
                }

            }
        }
    }
}
