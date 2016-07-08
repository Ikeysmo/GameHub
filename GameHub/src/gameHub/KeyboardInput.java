package gameHub;

import java.util.Scanner;

public class KeyboardInput extends Thread{
    Scanner sc= new Scanner(System.in);
    @Override
    public void run()
    {
    while(true)
    {
    while(sc.hasNext()) {
        final String line = sc.nextLine();
        System.out.println("Input line: " + line);
        if ("end".equalsIgnoreCase(line)) {
            System.out.println("Ending one thread");
            break;
        }
    }
    }

}

}