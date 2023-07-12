import java.io.IOException;
import java.util.Scanner;

public class Dbms {
    public static void main(String[] args) throws IOException {

        System.out.println("Java DBMS");

        while(true){
            System.out.println("Enter 1 -> Enter user credentials");
            System.out.println("Enter 2 -> Exit from the program");
            Scanner sc = new Scanner(System.in);
            switch (sc.nextInt()){
                case 1:
                    UserAuthenticationModule auth = new UserAuthenticationModule();
                    if(auth.checkAuthorization()){
                        Console cl = new Console(auth.userName);
                    }
                    else{
                        System.out.println("User authentication failed.. returning to main menu");
                        break;
                    }
                case 2:
                    System.exit(0);
            }
        }








    }
}