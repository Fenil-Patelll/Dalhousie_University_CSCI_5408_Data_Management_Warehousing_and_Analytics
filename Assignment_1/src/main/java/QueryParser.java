import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class QueryParser {

    void queryParsing(String userName,String query) throws IOException {


        String filePath = System.getProperty("user.dir")+"\\allTextfiles\\";
        Pattern create= Pattern.compile("create\\s*table\\s*[a-z]+\\s*\\((\\s*[a-z_]+\\s*[\\w()]+,?)+\\s*\\);",Pattern.CASE_INSENSITIVE);
        Pattern select = Pattern.compile("^SELECT\\s+(\\*|[a-zA-Z]+(\\s*,\\s*[a-zA-Z]+)*)\\s+FROM\\s+[a-zA-Z]+\\s*(WHERE\\s+.+)?;",Pattern.CASE_INSENSITIVE);
        Pattern insert = Pattern.compile("insert\\s*into\\s*[A-Za-z]+\\s*\\((\\s*[a-zA-Z]+\\s*,?)+\\)\\s*values\\s*\\((\\s*'(\\s*\\w+\\s*)+'\\s*,?)+\\)\\s*;",Pattern.CASE_INSENSITIVE);
        Pattern update = Pattern.compile("update\\s*[a-zA-Z]+\\s*set(\\s*[a-zA-Z_]+\\s*=\\s*'(\\s*\\w+\\s*)+'\\s*,?)+(where\\s*[a-zA-Z_]+\\s*=(\\s*\\d+\\s*|\\s*'(\\s*\\w+\\s*)+'\\s*))?;",Pattern.CASE_INSENSITIVE);
        Pattern delete = Pattern.compile("delete\\s*from\\s*[a-zA-Z_]+\\s*(where\\s*[a-zA-Z_]+\\s*=(\\s*\\d+\\s*|\\s*'(\\s*\\w+\\s*)+'\\s*))?;",Pattern.CASE_INSENSITIVE);
        Pattern update_new = Pattern.compile("\"update\\s+\\w+\\s+set\\s+\\w+\\s*=\\s*('[^']*'|\\w+)(?:\\s*,\\s*\\w+\\s*=\\s*('[^']*'|\\w+))*\\s*(?:where\\s+\\w+\\s*(?:=\\s*('[^']*'|\\w+)))?\\s*;",Pattern.CASE_INSENSITIVE);

        boolean createflag = create.matcher(query).matches();
        boolean selectflag = select.matcher(query).matches();
        boolean insertflag = insert.matcher(query).matches();
        boolean updateflag = update.matcher(query).matches();
        boolean deleteflag = delete.matcher(query).matches();

        if(createflag){
            String[] list= query.split("[,()\\d;]+");
            String[] firstPart = list[0].trim().split(" ");
            String tableName = firstPart[2];
            String headerName = "";
            System.out.println(tableName);
            for(int i= 1; i<list.length; i++  ){
                String[] secondPart = list[i].trim().split(" ");
                headerName+=secondPart[0]+"\t\t\t";
            }

            File myObj = new File(filePath+userName+"\\"+tableName+".txt");
            if(myObj.createNewFile()){
                FileWriter myWriter = new FileWriter(myObj,true);
                BufferedWriter writer = new BufferedWriter(myWriter);
                myWriter.write(headerName);
                writer.newLine();
                writer.close();
                myWriter.close();
            }
            else{
                System.out.println("table is already present");
                System.exit(0);
            }
        }

        else if (insertflag) {
            LinkedHashMap<String, String> data = new LinkedHashMap<>();
            String[] list = query.split("(?i)\\bvalues\\b");
            String[] sublist= list[0].trim().split("[,();]+");
            String[] firstPart  = sublist[0].trim().split(" ");
            String tableName = firstPart[2];
            File f = new File(filePath+userName+"\\"+tableName+".txt");
            if(!f.exists()){
                System.out.println("There is no table named "+ tableName);
                System.exit(0);
            }
            List<String> ColumnName = new ArrayList<>();
            for(int i= 1; i<sublist.length; i++  ){
                String[] secondPart = sublist[i].trim().split(" ");
                ColumnName.add(secondPart[0].trim());
            }
            String[] values = list[1].trim().split("'");
            List<String> ColumnValues = new ArrayList<>();
            for (int i = 1; i < values.length; i += 2) {
                ColumnValues.add(values[i].trim());
            }
            if(ColumnName.size() == ColumnValues.size() ){
                for(int i=0; i<ColumnName.size();i++){
                    data.put(ColumnName.get(i), ColumnValues.get(i));
                }
            }
            else{
                System.out.println("Column Count and values count not matching");
            }

            for (Map.Entry<String, String> entry : data.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }

            BufferedReader reader = new BufferedReader(new FileReader(filePath+userName+"\\"+tableName+".txt"));
            String headerLine = reader.readLine();
            String[] headline = (headerLine.trim().split("\t\t\t"));
            reader.close();


            FileWriter writer = new FileWriter(filePath+userName+"\\"+tableName+".txt", true);
            String inputStirng = "";
            for(String temp : headline){
                if(data.containsKey(temp)){
                    inputStirng += data.get(temp) + "\t\t\t";
                }
                else {
                    inputStirng += "null" + "\t\t\t";
                }
            }
            writer.write(inputStirng);
            writer.write("\r\n");
            writer.close();

        }

        else if (selectflag)
        {
            Pattern pattern = Pattern.compile(".*where.*",Pattern.CASE_INSENSITIVE);
            boolean whereCheck = pattern.matcher(query).matches();
            if(whereCheck){
                String[] list = query.split("(?i)\\bwhere\\b");
                if(Pattern.compile("\s*and\s*",Pattern.CASE_INSENSITIVE).matcher(list[1]).find()){

                    String regexPattern = "(\\w+)\\s*=\\s*('[^']*'|\\d+)";
                    Pattern pat = Pattern.compile(regexPattern);
                    Matcher matcher = pat.matcher(list[1]);
                    //where conditions catch
                    LinkedHashMap<String,String> whereConditions = new LinkedHashMap<>();
                    while (matcher.find()) {
                        String key = matcher.group(1).trim();
                        String value = matcher.group(2).trim();
                        if (value.startsWith("'") && value.endsWith("'")) {
                            value = value.substring(1, value.length() - 1);
                        }
                        whereConditions.put(key,value);
                    }
                    String tableName = list[0].trim().split("(?i)\\s*from\\s*")[1].trim();
                    //getting headline of the text file
                    BufferedReader reader = new BufferedReader(new FileReader(filePath+userName+"\\"+tableName+".txt"));
                    String headerLine = reader.readLine();
                    String[] headline = (headerLine.trim().split("\t\t\t"));
                    ArrayList<String> strList = new ArrayList<String>(
                            Arrays.asList(headline));

                    ArrayList<Integer> indexOfwhere = new ArrayList<>();
                    for(String item : whereConditions.keySet()){
                        indexOfwhere.add(strList.indexOf(item));
                    }


//                    if(Pattern.compile("\\bSELECT\\b\\s*\\*\\s*",Pattern.CASE_INSENSITIVE).matcher(list[0]).matches()){
//                        File file = new File(filePath+userName+"\\"+tableName+".txt");
//                        Scanner sc = new Scanner(file);
//                        while (sc.hasNextLine())
//                            System.out.println(sc.nextLine());
//                    }
                    //for select *
                    if(Pattern.compile("\\bSELECT\\b\\s*\\*\\s*",Pattern.CASE_INSENSITIVE).matcher(list[0]).find()){
                        String line;
                        System.out.println(headerLine);
                        while ((line = reader.readLine()) != null) {
                            String[] readedText  = (line.trim().split("\t\t\t"));
                            ArrayList<String> tempText = new ArrayList<String>(
                                    Arrays.asList(readedText));
                            int count= 0;
                            for(Integer item : indexOfwhere){


                                if(tempText.get(item).equals(whereConditions.get(strList.get(item)))){
                                    count++;
                                }
                                if(count==2){
                                    System.out.println(line);
                                }
                            }
                        }
                    }

                    //for select columnNames ....
                    else {
                        //getting all column names and storing them into map
                        ArrayList<String> columnName = new ArrayList<>();
                        Pattern pattern2 = Pattern.compile("\\b\\w+\\b(?=\\s*(?:,|\\bFROM\\b|;|$))");
                        Matcher matcher2 = pattern2.matcher(list[0]);
                        while(matcher2.find())
                        {
                            columnName.add(matcher2.group());
                        }

                        //storing index of the matched columnNames
                        BufferedReader reader2 = new BufferedReader(new FileReader(filePath+userName+"\\"+tableName+".txt"));
                        ArrayList<Integer> index = new ArrayList<>();
                        for(String item : columnName){
                            index.add(strList.indexOf(item));
                        }

                        for(String column : columnName){
                            System.out.print(column+"\t\t\t");
                        }
                        System.out.println("");
                        String line;
                        while ((line = reader2.readLine()) != null) {
                            String[] readedText  = (line.trim().split("\t\t\t"));
                            ArrayList<String> tempText = new ArrayList<String>(
                                    Arrays.asList(readedText));
                            int count = 0;
                            for(Integer item : indexOfwhere){
                                if(count==2){
                                    String tempLine = "";
                                    for(Integer i : index){
                                        tempLine += tempText.get(i) + "\t\t\t";
                                    }
                                    System.out.println(tempLine);
                                }
                                if(tempText.get(item).equals(whereConditions.get(strList.get(item)))){
                                    count++;
                                }

                            }

                        }
                    }


                }
                else{
                    String regexPattern = "(\\w+)\\s*=\\s*('[^']*'|\\d+)";
                    Pattern pat = Pattern.compile(regexPattern);
                    Matcher matcher = pat.matcher(list[1]);

                    LinkedHashMap<String,String> whereConditions = new LinkedHashMap<>();
                    while (matcher.find()) {
                        String key = matcher.group(1).trim();
                        String value = matcher.group(2).trim();
                        whereConditions.put(key,value);
                    }

                    String tableName = list[0].trim().split("(?i)\\s*from\\s*")[1].trim();
                    BufferedReader reader = new BufferedReader(new FileReader(filePath+userName+"\\"+tableName+".txt"));
                    String headerLine = reader.readLine();
                    String[] headline = (headerLine.trim().split("\t\t\t"));
                    ArrayList<String> strList = new ArrayList<String>(
                            Arrays.asList(headline));

                    ArrayList<Integer> indexOfwhere = new ArrayList<>();
                    for(String item : whereConditions.keySet()){
                        indexOfwhere.add(strList.indexOf(item));
                    }

                    //for select *
                    if(Pattern.compile("\\bSELECT\\b\\s*\\*\\s*",Pattern.CASE_INSENSITIVE).matcher(list[0]).matches()){
                        String line;
                        System.out.println(headline);
                        while ((line = reader.readLine()) != null) {
                            String[] readedText  = (line.trim().split("\t\t\t"));
                            ArrayList<String> tempText = new ArrayList<String>(
                                    Arrays.asList(readedText));
                            int count= 0;
                            for(Integer item : indexOfwhere){

                                if(tempText.get(item).equals(whereConditions.get(strList.get(item)))){
                                    count++;
                                }
                                if(count==1){
                                    System.out.println(line);
                                }
                            }
                        }
                    }

                    //for select columnNames ....
                    else {
                        ArrayList<String> columnName = new ArrayList<>();
                        Pattern pattern2 = Pattern.compile("\\b\\w+\\b(?=\\s*(?:,|\\bFROM\\b|;|$))",Pattern.CASE_INSENSITIVE);
                        Matcher matcher2 = pattern2.matcher(list[0].trim().split("(?i)\\bfrom\\b")[0]+";");
                        while(matcher2.find())
                        {
                            columnName.add(matcher2.group());
                        }

                        ArrayList<Integer> index = new ArrayList<>();
                        for(String item : columnName){
                            index.add(strList.indexOf(item));
                        }

                        for(String column : columnName){
                            System.out.print(column+"\t\t\t");
                        }
                        System.out.println("");
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] readedText  = (line.trim().split("\t\t\t"));
                            ArrayList<String> tempText = new ArrayList<String>(
                                    Arrays.asList(readedText));
                            int count = 0;
                            for(Integer item : indexOfwhere){

                                if(tempText.get(item).equals(whereConditions.get(strList.get(item)))){
                                    count++;
                                }
                                if(count==1){
                                    String tempLine = "";
                                    for(Integer i : index){
                                        tempLine += tempText.get(i) + "\t\t\t";
                                    }
                                    System.out.println(tempLine);
                                }

                            }

                        }
                    }


                }

            }
            else{
                String[] list = query.split("(?i)\\bfrom\\b");
                if(Pattern.compile("\\bSELECT\\b\\s*\\*\\s*",Pattern.CASE_INSENSITIVE).matcher(list[0]).matches()){
                    String tableName = list[1].trim().split(";")[0];
                    File file = new File(filePath+userName+"\\"+tableName+".txt");
                    Scanner sc = new Scanner(file);
                    while (sc.hasNextLine())
                        System.out.println(sc.nextLine());
                }
                else{
                    ArrayList<String> columnName = new ArrayList<>();

                    Pattern pattern2 = Pattern.compile("\\b\\w+\\b(?=\\s*(?:,|\\bFROM\\b|;|$))",Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern2.matcher(list[0]);
                    String tableName = list[1].trim().split(";")[0];
                    while(matcher.find())
                    {
                        columnName.add(matcher.group());
                    }

                    BufferedReader reader = new BufferedReader(new FileReader(filePath+userName+"\\"+tableName+".txt"));
                    String headerLine = reader.readLine();
                    String[] headline = (headerLine.trim().split("\t\t\t"));
                    ArrayList<String> strList = new ArrayList<String>(
                            Arrays.asList(headline));

                    ArrayList<Integer> index = new ArrayList<>();
                    for(String item : columnName){
                         index.add(strList.indexOf(item));
                    }
                    for(String column : columnName){
                        System.out.print(column+"\t\t\t");
                    }
                    System.out.println("");
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] readedText  = (line.trim().split("\t\t\t"));
                        ArrayList<String> tempText = new ArrayList<String>(
                                Arrays.asList(readedText));
                        String tempLine = "";
                        for(Integer item : index){
                            tempLine += tempText.get(item) + "\t\t\t";
                        }
                        System.out.println(tempLine);
                    }

                }
            }

        }
        else if (updateflag) {
            Pattern pattern = Pattern.compile("(?i)\\bset\\b");
            String[] list = pattern.split(query);
            String tableName = list[0].trim().split("(?i)\\s*update\\s*")[1].trim();

            pattern = Pattern.compile("(?i)\\bwhere\\b");
            list = pattern.split(list[1]);
            String updateValues = list[0].trim();

            // Parsing update values
            LinkedHashMap<String,String> updateValuesMap = new LinkedHashMap<>();
            String regexPattern = "(\\w+)\\s*=\\s*('[^']*'|\\d+)";
            Pattern pat = Pattern.compile(regexPattern);
            Matcher matcher = pat.matcher(updateValues);
            while (matcher.find()) {
                String key = matcher.group(1).trim();
                String value = matcher.group(2).trim();
                if (value.startsWith("'") && value.endsWith("'")) {
                    value = value.substring(1, value.length() - 1);
                }
                updateValuesMap.put(key,value);
            }

            // Parsing where conditions
            LinkedHashMap<String,String> whereConditions = new LinkedHashMap<>();
            matcher = pat.matcher(list[1]);
            while (matcher.find()) {
                String key = matcher.group(1).trim();
                String value = matcher.group(2).trim();
                if (value.startsWith("'") && value.endsWith("'")) {
                    value = value.substring(1, value.length() - 1);
                }
                whereConditions.put(key,value);
            }

            // Open the file for reading and writing

            BufferedReader reader = new BufferedReader(new FileReader(filePath +"\\"+userName+"\\"+ tableName + ".txt"));
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath +"\\"+userName+"\\"+ tableName + ".txt.temp"));

            // Process the header line
            String headerLine = reader.readLine();
            writer.write(headerLine + System.lineSeparator());

            // Process the data lines
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\t\t\t");

                // Check if the row matches the where conditions
                boolean match = true;
                for (Map.Entry<String,String> entry : whereConditions.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    int index = Arrays.asList(headerLine.split("\t\t\t")).indexOf(key);
                    if (!data[index].equals(value)) {
                        match = false;
                        break;
                    }
                }

                // Update the row if it matches the where conditions
                if (match) {
                    for (Map.Entry<String,String> entry : updateValuesMap.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        int index = Arrays.asList(headerLine.split("\t\t\t")).indexOf(key);
                        data[index] = value;
                    }
                }

                // Write the updated row to the new file
                String updatedLine = String.join("\t\t\t", data);
                writer.write(updatedLine + System.lineSeparator());
            }

            // Close the file reader and writer
            reader.close();
            writer.close();

            // Rename the temporary file to the original file
            File file = new File(filePath + "\\"+userName+"\\"+tableName + ".txt");
            File tempFile = new File(filePath + "\\"+userName+"\\"+tableName + ".txt.temp");
            file.delete();
            tempFile.renameTo(file);
        }

        else if (deleteflag) {
            Pattern pattern = Pattern.compile("(?i)\\bfrom\\b");
            String[] list = pattern.split(query);
            String tableName = list[1].trim().split(" ")[0];

            pattern = Pattern.compile("(?i)\\bwhere\\b");
            list = pattern.split(list[1]);
            String whereConditions = list[1].trim();

            // Open the file for reading and writing
            BufferedReader reader = new BufferedReader(new FileReader(filePath + "\\" + userName + "\\" + tableName + ".txt"));
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + "\\" + userName + "\\" + tableName + ".txt.temp"));

            // Process the header line
            String headerLine = reader.readLine();
            writer.write(headerLine + System.lineSeparator());

            // Process the data lines
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\t\t\t");

                // Check if the row matches the where conditions
                boolean match = true;
                for (String whereCondition : whereConditions.split("\\bAND\\b")) {
                    String[] condition = whereCondition.split("\\s*=\\s*");
                    String key = condition[0].trim();
                    String value = condition[1].trim().replaceAll("'","").replaceAll(";","");
                    int index = Arrays.asList(headerLine.split("\t\t\t")).indexOf(key);
                    if (!data[index].equals(value)) {
                        match = false;
                        break;
                    }
                }

                // Write the row to the new file if it does not match the where conditions
                if (!match) {
                    String updatedLine = String.join("\t\t\t", data);
                    writer.write(updatedLine + System.lineSeparator());
                }
            }

            // Close the file reader and writer
            reader.close();
            writer.close();

            // Rename the temporary file to the original file
            File file = new File(filePath + "\\" + userName + "\\" + tableName + ".txt");
            File tempFile = new File(filePath + "\\" + userName + "\\" + tableName + ".txt.temp");
            file.delete();
            tempFile.renameTo(file);
        }





    }



    }

