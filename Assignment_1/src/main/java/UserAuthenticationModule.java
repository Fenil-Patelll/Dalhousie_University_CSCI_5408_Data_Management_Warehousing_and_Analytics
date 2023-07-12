import java.io.*;
import java.util.Scanner;

public class UserAuthenticationModule{

    String userName;
    private String password;

    private String securityQuestion;

    private String answer;

    boolean authentication;

    boolean checkAuthorization(){

        System.out.println("hii");
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your username");
        this.userName = sc.next();
        System.out.println("Enter your password");
        this.password = sc.next();
        String filePath = System.getProperty("user.dir")+"\\allTextfiles\\";
        try {
            File myObj = new File(filePath+"\\userDetails.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
                System.out.println("Enter a Security Question");
                this.securityQuestion = sc.nextLine();
                System.out.println("Security Answer");
                this.answer = sc.nextLine();
                FileWriter myWriter = new FileWriter(filePath+"\\userDetails.txt", true);
                BufferedWriter out = new BufferedWriter(myWriter);
                myWriter.write(userName+'\t'+password+'\t'+securityQuestion+'\t'+answer);
                myWriter.write("\r\n");
                myWriter.flush();
                myWriter.close();
                System.out.println("New user created");
                return true;


            } else {
                try {
                    Scanner scanner = new Scanner(new File(filePath+"\\userDetails.txt"));
                    boolean flag = false;
                    while (scanner.hasNextLine()) {
                        String userdetails[] =  scanner.nextLine().split("\t");
                        if (userdetails[0].equals(this.userName)){
                            if(userdetails[1].equals(this.password)){
                                System.out.println("Security Question - "+ userdetails[2]);
                                Scanner answerinput = new Scanner(System.in);
                                String userAnswer = answerinput.nextLine();
                                if(userAnswer.toLowerCase().equals(userdetails[3].toLowerCase())){
                                    System.out.println("user authentication successfull");
                                    authentication = true;
                                }
                                else{
                                    System.out.println("incorrect security answer");
                                    authentication = false;
                                }
                            }
                            else {
                                System.out.println("incorrect password");
                                authentication = false;
                            }
                            flag = true;
                            break;
                        }
                    }
                    if(flag==false){
                        System.out.println("can not find user with " +this.userName);
                        System.out.println("to register a new user select 2 options");
                        System.out.println("Input 1 -> to register a new user");
                        System.out.println("Input 2 -> to exit from the program");
                        Scanner in = new Scanner(System.in);
                        switch (in.nextInt()){
                            case 1:
                            {
                                Scanner scan = new Scanner(System.in);
                                System.out.println("Enter a Security Question");
                                String que = scan.nextLine();
                                System.out.println("Enter a Security Answer");
                                String ans = scan.nextLine();
                                FileWriter myWriter = new FileWriter(filePath+"\\userDetails.txt", true);
                                BufferedWriter out = new BufferedWriter(myWriter);
                                myWriter.write(userName+'\t'+password+'\t'+que+'\t'+ans);
                                myWriter.write("\r\n");
                                myWriter.flush();
                                out.close();
                                myWriter.close();
                                authentication = true;
                                break;
                            }
                            case 2:
                                System.exit(0);

                        }

                    }

                    scanner.close();
                    return authentication;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return false;
    }
}
