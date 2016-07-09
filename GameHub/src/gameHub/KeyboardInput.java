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
        
        //get ipaddress
        //add username [username]
        //remove username [username]
        //get online_list
        //get password [username] [admin_password]
        //remove game [username]
        
        /**
         * void gamehub_command {
         *   get_input_from_terminal();
         *   parse();
         *   run_command();
         *   return status; //if it was a success or a failture
         */
    }
    }

}

}