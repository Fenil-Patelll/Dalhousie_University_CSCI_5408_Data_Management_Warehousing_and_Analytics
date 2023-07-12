import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.util.Scanner;

public class Console {
   Console(String userName) throws IOException {
      System.out.println("Welcome " +userName);
      File folder = new File("C:\\Users\\fenil\\OneDrive\\Desktop\\Data\\CSCI_5408_Assignment_1\\allTextfiles\\"+userName);
      if(folder.mkdir()){
         System.out.println("Folder "+userName+ " has been created");
      }
      else {
         System.out.println("in "+userName+" Folder");
      }

      Scanner scan = new Scanner(System.in);
      System.out.println("Write the query");
      String query = scan.nextLine();
      QueryParser qp = new QueryParser();
      qp.queryParsing(userName,query);

   }
}
