/*Lakhwinder pal Singh
  CSC 131 Assignment 2  
  Individual Project - Sprint 2 (Design and Coding)
  Task Manager v1.2
  Prof. Posnett
  this program track the time  spent on particular task
 -------------------------------------------------------------------------------
  v1.1
  The application needs to log the time that work began on a particular task.
  The application needs to log the time that work stopped on a particular task.
  The application needs to be able to record a description for a particular task.
  The application needs to provide a summary of the time spent on a particular task.
  The application needs to provide a  summary report for all tasks. 
  ----------------------------------------------------------------------------------
  v1.2 updates
  added task size functionality to the program.
  -----------------------------------------------------------------------------------*/

import java.util.*;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;

public class TM{
    WriteLog newLog = new WriteLog();
    public static void main(String[] args){
        new TM().appMain(args);    
    }
    void appMain(String[] args){
        if(args.length == 0){
            usage();    // if no arguments provided
        }
        else if(args.length == 1 && args[0].equals("summary")){
           summary_file("all");    // summary of all the tasks
        }
        else if(args.length == 2){
            String date = null;
            switch(args[0]){ 
                case "start": date = get_time();
                              newLog.save_log_file("start" + "\t" + args[1] + "\t" + date);
                              break;
                case "stop":  date = get_time();
                             newLog.save_log_file("stop" + "\t" + args[1] + "\t" + date);
                                break;
                case "summary": summary_file(args[1]);
                                break;
                default: usage();
                        break;
            }
        }
        else if(args.length == 3){
            String date = null;
            switch(args[0]){
                case "size" :  newLog.save_log_file("size"+"\t" + args[1] + "\t" + args[2]);
                            break;
                case "describe": date = get_time();
                                newLog.save_log_file("describe" + "\t" + args[1] + "\t" + date + "\t"+ args[2]);
                                break; 
                default: usage();
            }
        
        }
        else if(args.length == 4){
            String date = get_time();
            newLog.save_log_file("describe" + "\t" + args[1] + "\t" + date + "\t"+ args[2] + "\t" + args[3]);   
        }
        else{
            usage();
        }
    }

    void usage(){
        System.err.println("command can be one of these: start, stop, describe, or summary.\n" +
                        "java TM start <task name>\t\t\t\t<task name> start time\n" +
                        "java TM stop <task name>\t\t\t\t<task name> stop time\n"+
                        "java TM size <task name> <task size>\t\t\tadds size of the task\n"+
                        "java TM describe <task name> <description> <size> \tdescription of the task\n"+
                        "java TM summary <task name>\t\t\t	<task name> summary\n"+
                        "java TM summary\t\t\t\t\t 	summary of all the tasks");
    }

    void summary_file(String task){
       FileLinekedList newlist = new FileLinekedList();
       newlist.print_summary(task);;   //   prints summary of tasks 
    }

    public static String get_time(){
        Date date = new Date(); 
        //https://stackoverflow.com/questions/29060364/java-how-to-save-a-file-with-current-date-and-time
        SimpleDateFormat mydate = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
        String newdate = mydate.format(date);
     return newdate;
    }
}

class WriteLog{
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

class LogFileEntry{
    String command = "";
    String task_name = "";
    String description = "";
    String task_size = "";
    String task_time ="";
    String total_time = "00:00:00";
    int occur = 0;      // saves occurances of particular task 
}

class FileLinekedList{
    LinkedList<LogFileEntry> list = new LinkedList<LogFileEntry>();
    void print_summary(String input){
        read(); // read the log file to linked list
        sort_list();    // sort the linked list
        if(list.isEmpty()){
            System.out.println("Log file empty");
        }
        if(input.equals("all")){
            for(int k = 0; k< list.size(); k++){
                System.out.println("\nTask Name: " + list.get(k).task_name  +"\tDescription: "  + list.get(k).description + " \n"
                                        +"Total Time: " + list.get(k).total_time +  "\tTask Size: " +list.get(k).task_size);
            }
        }else{
            for(int i = 0; i<list.size();i++){
                String task = list.get(i).task_name;
                if(task.equals(input)){
                    System.out.println("\nTask Name: " + list.get(i).task_name  +"\tDescription: "  + list.get(i).description + " \n"
                                        +"Total Time: " + list.get(i).total_time +  "\tTask Size: " +list.get(i).task_size);
                }
            }
        }
    }

    void read(){    // read the log file and save it in linked list of ADT
        String content = null;
        LogFileEntry log;
        File file = new File("log.txt");
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            Scanner sc = new Scanner(new FileInputStream(file));
            while (sc.hasNextLine()){
                content = sc.nextLine();
                String[] token = content.split("\t"); // split string based on tab 
                log = new LogFileEntry();
                if(token.length <= 3 ){ // for size
                    if(token[0].equals("size")){
                        log.command = token[0];
                        log.task_name = token[1];
                        log.task_size = token[2];
                        list.add(log);
                    }else{ // save for start,stop
                        log.command = token[0];
                        log.task_name = token[1];
                        log.task_time = token[2];
                        list.add(log);
                    }
                }else if(token.length == 4){ // for describe
                    log.command = token[0];
                    log.task_name = token[1];
                    log.task_time = token[2];
                    log.description = token[3];
                    list.add(log); 
                }
                else if(token.length == 5){ // for describe with size
                    log.command = token[0];
                    log.task_name = token[1];
                    log.task_time = token[2];
                    log.description = token[3];
                    log.task_size = token[4];
                    list.add(log); 
                }
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

    void sort_list(){
        int i = 0;
        for(i = 0; i<list.size();i++){
                String first = list.get(i).task_name;
                String stop_time = "00:00:00";
                String cmd = list.get(i).command;   // get the command
                if(cmd.contains("start")){
                    list.get(i).occur++; // increment occurance to check if another node has same task name with start command
                } 
                if(cmd.contains("describe")){   // if task is started by describe function
                cmd = "start";      // change the command to start for it to behave as task has started
                list.get(i).occur++;    // increment the occurance of task
                }
                for(int j = i+1; j<list.size();j++){  
                    if(list.get(j).task_name.equals(first)){  // if found two elements with same task name 
                        String comm = list.get(j).command;  // gets command from next node
                        int test = list.get(i).occur % 2;   // ge the occurance of task 
                        if(comm.contains("stop") && cmd.contains("start") && test != 0){// if the command at new position is stop, get the stopping time
                            String start_time = list.get(i).task_time;
                            stop_time = list.get(j).task_time;  
                            list.get(i).total_time = timeDifference(start_time,stop_time,"diff");// get the time difference and assign to the start command element
                            list.get(i).occur++; // increment occurance of current task
                            list.remove(j);
                            j--;
                        }
                         if(list.get(j).command.contains("describe") && j != i && (list.get(j).task_name.equals(first))){  // if more descriptions are found
                             if(list.get(i).description == ""){ // if initial description is empty
                                list.get(i).description = list.get(j).description;
                             }else{ // if initial description is previously described then add new description instead of overwriting
                                list.get(i).description += "\n\t\t\tNew Description: " + list.get(j).description;
                                if(!list.get(j).task_size.isEmpty()){   // to get size task, and not overwritten by null when using decribe without size
                                    list.get(i).task_size = list.get(j).task_size;
                                }
                             }
                            list.remove(j); // remove current node
                            j--;    // point to previous node as current node is removed and next node points to current position
                        }   
                       if(list.get(j).command.contains("size") && j != i && (list.get(j).task_name.equals(first)) ){
                            list.get(i).task_size = list.get(j).task_size; // get the size and update to the latest provided
                            list.remove(j);
                            j--;
                        }                       
                    }
                }  
        } 
        // checks if task with same name exists in the list
                    for(int x = 0; x<list.size();x++){
                       for(int y = x+1; y <list.size(); y++){
                           if(list.get(x).task_name.equals(list.get(y).task_name)){ // if same task name found at another location
                                if(list.get(y).total_time.equals("00:00:00")){  // if no stop time is given
                                    list.get(x).total_time = "00:00:00";    
                                    list.remove(y);
                                }
                            else {  // if total time is given then add the two times
                                list.get(x).total_time = timeDifference(list.get(x).total_time,list.get(y).total_time, "add");
                                list.remove(y);
                            }
                        }
                    }
                }
    }
    String timeDifference(String start, String stop, String command)
    { 
        String final_date = null;
        if(command.equals("diff")){
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
            Date date_start = null;
            Date date_stop = null;
        try {
            date_start = sdf.parse(start);
            date_stop = sdf.parse(stop);         
        } catch (ParseException e) {
            e.printStackTrace();
        }  
            long difference = date_stop.getTime() - date_start.getTime();   //differecne between the date, using java built in function
            difference = difference/1000;   // convert to seconds
            long hours = difference / 3600; // convert seconds to hours
            long minutes = (difference % 3600) / 60;
            long seconds = difference % 60;
           final_date=  String.format(hours+ ":"+minutes+":"+seconds);
        }else if(command.equals("add")){    
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date date_start = null;
            Date date_stop = null;
            try {
                date_start = sdf.parse(start);
                date_stop = sdf.parse(stop);         
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long start1 = date_start.getTime()/1000;
            long start2 = date_stop.getTime()/1000;
            long seconds = (start1 % 60) + (start2 % 60);
            long minutes = (start1 %3600 / 60) + (start2 %3600 / 60);
            long hours = ((start1/3600)-8) + ((start2/3600)-8);
            if(seconds >= 60){
                minutes++;
                seconds = seconds - 60; }
            if(minutes >= 60){
                hours++;
                minutes = minutes - 60; }
            final_date=  String.format(hours+ ":"+minutes+":"+seconds);
        } 
     return final_date;  
    }
}