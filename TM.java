/*Lakhwinder pal Singh
  CSC 131 Assignment 2 
  Individual Project - Sprint 1 (Design and Coding)
  Prof. Posnett
  this program track the time 
  The application needs to log the time that work began on a particular task.
  The application needs to log the time that work stopped on a particular task.
  The application needs to be able to record a description for a particular task.
  The application needs to provide a summary of the time spent on a particular task.
  The application needs to provide a  summary report for all tasks. */

import java.util.*;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TM{
    FileLinkList list = new FileLinkList();
    public static void main(String[] args){
        new TM().appMain(args);    
    }

    void appMain(String[] args){
        if(args.length == 0){
            usage();    // if no arguments provided
        }
        else if(args.length == 1){
           summary_file("all");    // summary of all the tasks
        }
        else if(args.length == 2){
            switch(args[0]){
                case "start": start_task(args[1]);
                                break;
                case "stop": stop_task(args[1]);
                                break;
                case "summary": summary_file(args[1]);
                                break;
                default: usage();
                        break;
            }
        }
        else if(args.length == 4){
            describe_task(args[1], args[2], args[3]);
        }
        else{
            usage();
        }
    }

    void usage(){
        System.err.println("command can be one of these: start, stop, describe, or summary.\n" +
                        "java TM start <task name>\t\t\t<task name> start time\n" +
                        "java TM stop <task name>\t\t\t<task name> stop time\n"+
                        "java TM describe <task name>\t<description> description of the task <task name>\n"+
                        "java TM summary <task name>\t\t	<task name> summary\n"+
                        "java TM summary\t\t\t\t 	summary of all the tasks");
    }

    void summary_file(String task){
        list.print_summary(task);   //   prints summary of tasks 
    }

    public static String get_time(){
        Date date = new Date(); 
        //https://stackoverflow.com/questions/29060364/java-how-to-save-a-file-with-current-date-and-time
        SimpleDateFormat mydate = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
        String newdate = mydate.format(date);
     return newdate;
    }

    void start_task(String task_name){  
        String date = get_time();
        boolean check =  list.task_checker_start(task_name);    // if task already started ??
        if(check == true){
            System.err.println("Task already running or completed");
        }else  {
         save_log_file(task_name + "," +  date + ".start"); //else log it
        }
    }

    void stop_task(String task_name){
        String date = get_time();
        boolean check_start =  list.task_checker_stop(task_name);   // if task is started or not
        if(check_start == false){       // cant stop a task if not started yet
            System.err.println("Task not started yet/or already finished");
        }else{
         save_log_file(task_name + "," +  date + ".stop");
        }
    }

    void describe_task(String task_name, String description, String size){
        String date = get_time();
        boolean start_flag = true;
        if(start_flag == true){ // task has already been started
            save_log_file(task_name+","+".describe,"+description);
            save_log_file(task_name+",size"+size);
        }else{
            save_log_file(task_name + "," +  date + ".start");
            save_log_file(task_name+","+".describe,"+description);
            save_log_file(task_name+",size"+size);



        }
        














        boolean check =  list.task_checker_start(task_name);    // if task is started or not yet
        if(check == false){
            save_log_file(task_name + "," +  date + ".start");
            save_log_file(task_name + "," +  date + ".describe," + description);
        }else  {
         save_log_file(task_name + "," + ".describe,"+ description);
        }
    }

    void save_log_file(String input){   // writing info to log file
        FileWriter fw = null;
        BufferedWriter bw = null;
        try{
            File file = new File("log.txt");
            if(!file.exists()){
                file.createNewFile();
            }
            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);
            bw.write(input + "\n");
        }catch(IOException e){
            e.printStackTrace();
        }
        finally{
            try{
                if(bw != null) bw.close();
                if(fw != null) fw.close();
            } catch(IOException ex){
                ex.printStackTrace();
            }
        }
    }

}

//https://stackoverflow.com/questions/16247623/how-to-read-from-a-file-and-save-the-content-into-a-linked-list
 class FileLinkList
{
    // creating 3 linked list, 2 for getting data from file, 3rd for sorting for task summary
    LinkedList<String> list = new LinkedList<String>(); // gets deleted after used
    LinkedList<String> new_list = new LinkedList<String>(); // temp list
    LinkedList<String> sorted_list = new LinkedList<String>();  // stores summary info

    public boolean task_checker_start(String task){
        summary_task();

        return true;
       
    }
    public boolean task_checker_stop(String task){
        summary_task();
       
        return true;
    }


    void summary_task(){
        main(); // call to read the file into linked list
        String line = null, task_name = null, start_time = null, describe_task = null, temp = null;
        int index_comma = 0, index_dot = 0, second_comma = 0;
        String stop_time = "00:00:00";
        String stop_task = "";
        String describe = "";
        int list_size = list.size();
        int size = list.size();

        for (int j = 0; j <list_size; j++) {
            line = list.get(j); // get one line at a time
            index_comma = line.indexOf(",");    
            index_dot = line.indexOf(".");
            task_name = line.substring(0,index_comma);  // task name is saved between index 0 to index of first comma
            if(line.contains("start")){
                start_time = line.substring(index_comma+1, index_dot);  // start time is daved betweeen first comma and a dot
            }
            // for comparing task with other elements in list
            for(int i = j+1; i <=size;i++){
                if(i == list.size()){
                    temp = list.getLast();
                }else{
                temp = list.get(i); }
                if(temp.contains(task_name) && temp.contains("stop")){
                    stop_task = temp;
                    stop_time = temp.substring(index_comma+1, index_dot);   // stop time saved between first comma and a dot
                    if(i == list.size()){   // checks if last node of the list
                    list.addLast("end");}   // add something to the end so delete the current node
                    list.remove(i);
                }
                else if(temp.contains(task_name) && temp.contains("describe")){
                    second_comma = temp.indexOf(",",index_comma + 3);   
                    describe_task = temp.substring(second_comma+1); // description of task from comma to end of line
                    describe = describe_task;
                    if(i == list.size()){
                        list.addLast("end");}
                        list.remove(i);
                }
                size = list.size();     // updating size of the list
            }
            String total_time = get_time_int(start_time, stop_time);    // finction to get total time spent on a task
            sorted_list.add("Task: "+ task_name + "\tDescription: "+ describe + "\nTotal Time: " +total_time ); // add element to task in sorted order
            stop_task = "";
            describe = "";
            stop_time= "00:00:00";
            list_size = list.size();
        }        
    }

    String get_time_int(String start, String stop){ // returns total time spent by a task, if task still running returns 00:00:00
        String final_time = null;
        int start_day, start_month, start_year = 0;
        int stop_day, stop_month, stop_year;
        int day, month, year = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date date_start = null;
        Date date_stop = null;
        //https://stackoverflow.com/questions/18604408/convert-java-string-to-time-not-date
        if(stop == "00:00:00"){ // if stop is not yet entered by user
            final_time = stop;
        }else{
        try {
            date_start = sdf.parse(start.substring(9,17));  // convert date in string format to date format
            date_stop = sdf.parse(stop.substring(9,17));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        start_month = Integer.parseInt(start.substring(0,2));   // gets starting month of task
        start_day =  Integer.parseInt(start.substring(3,5));    // gets starting day of task
        start_year = Integer.parseInt(start.substring(6,8));    // gets starting year of task
        stop_month = Integer.parseInt(stop.substring(0,2));     // gets ending month of task
        stop_day =  Integer.parseInt(stop.substring(3,5));      // gets ending day of task
        stop_year = Integer.parseInt(stop.substring(6,8));      // gets ending year of task
        day = stop_day - start_day; 
        month = stop_month - start_month;
        year = stop_year - start_year;
        // used the following code from 
        //https://stackoverflow.com/questions/5911387/difference-in-time-between-two-dates-in-java
        long difference = date_stop.getTime() - date_start.getTime();   //differecne between the date, using java built in function
        difference = difference/1000;   // convert to seconds
        long hours = difference / 3600; // convert seconds to hours
        long minutes = (difference % 3600) / 60;
        long seconds = difference % 60;
        if(day>1){  // if task takes more than an hour, save how many days,months or years it tool
            final_time = String.format("Year: "  + year + "Month: " + month + "Days: " + day + hours+":"+minutes+":"+seconds);

        }else{  // else just save time upto hours
            final_time = String.format(hours+":"+minutes+":"+seconds);
        }
    }
     return final_time;
    }

    void print_summary(String task_name){
        summary_task();
        boolean flag = false;
        int position = 0;
        if(task_name == "all"){ // if user wants summary of all the tasks
            if (sorted_list.isEmpty() == true){ // if list is empty
                System.out.println("Log File Empty");
            }else{
                 Iterator i = sorted_list.iterator();   // else print he sorted list
                 while (i.hasNext()) {
                    System.out.println(i.next());
                    System.out.println();
                }
            }
    
        }else{          // if user requested summary of certain task
            String line = null;
            for(int i = 0; i<sorted_list.size(); i++){
                line = sorted_list.get(i);  
                if(line.contains(task_name)){
                    flag = true;    // if found the task 
                    position = i;   // save the index of the element in list
                }
            }
            if(flag == true){
                    System.out.println(sorted_list.get(position));  // print the task info
                }
                else System.err.println("Please enter a valid task");

    }
}

    public void main(){ // reads log file to linked lists
        String content = new String();
        File file = new File("log.txt");
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            Scanner sc = new Scanner(new FileInputStream(file));
            while (sc.hasNextLine()){
                content = sc.nextLine();
                list.add(content); 
                new_list.add(content);  
            }
            sc.close();
        }catch(FileNotFoundException fnf){
            fnf.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("\nProgram terminated Safely...");
        }
    }       
}
