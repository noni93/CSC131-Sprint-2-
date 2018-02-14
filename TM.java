/*Lakhwinder pal Singh
  CSC 131 Assignment 2 
  Individual Project - Sprint 2 (Design and Coding)
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
            newLog.save_log_file("size"+"\t" + args[1] + "\t" + args[2]);
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
    String total_time = "";
    int occur = 0;
}

class FileLinekedList{
    LinkedList<LogFileEntry> list = new LinkedList<LogFileEntry>();
    void print_summary(String input){
        read();
        sort_list();
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

    void read(){
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
                String[] token = content.split("\t");
                log = new LogFileEntry();
                if(token.length <= 3 ){
                    if(token[0].equals("size")){
                        log.command = token[0];
                        log.task_name = token[1];
                        log.task_size = token[2];
                        list.add(log);
                    }else{
                        log.command = token[0];
                        log.task_name = token[1];
                        log.task_time = token[2];
                        list.add(log);
                    }
                }else if(token.length == 5){
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
                    list.get(i).occur++;
                } 
                for(int j = i+1; j<list.size();j++){
                    // if found two elements with same task name 
                    if(list.get(j).task_name.equals(first)){
                        // if the command at new position is stop, get the stopping time
                        String comm = list.get(j).command;
                        int test = list.get(i).occur % 2;
                        if(comm.contains("stop") && cmd.contains("start") && test != 0){
                            String start_time = list.get(i).task_time;
                            stop_time = list.get(j).task_time;  
                            // get the time difference and assign to the start command element
                            list.get(i).total_time = timeDifference(start_time,stop_time,"diff");
                            list.get(i).occur++;
                            list.remove(j);
                            j--;
                        }
                         if(list.get(j).command.contains("describe")){
                             if(list.get(i).description == ""){
                                list.get(i).description = list.get(j).description;
                             }else{
                                list.get(i).description += "\n\t\t\tNew Description: " + list.get(j).description;
                             }
                            list.get(i).task_size = list.get(j).task_size;
                            list.remove(j);
                            j--;
                        }   
                        if(list.get(j).command.contains("size")){
                            list.get(i).task_size = list.get(j).task_size;
                            list.remove(j);
                            j--;
                            
                        }                        
                    }
                }  
        } 
        // checks if task with same name exists in the list
                    for(int x = 0; x<list.size();x++){
                       for(int y = x+1; y <list.size(); y++){
                           if(list.get(x).task_name.equals(list.get(y).task_name)){
                            if(list.get(y).total_time.equals("")){
                                list.get(x).total_time = "00:00:00";
                                list.remove(y);
                            }
                            else {
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
               seconds = seconds - 60;
           }
           if(minutes >= 60){
               hours++;
               minutes = minutes - 60;
           }
           final_date=  String.format(hours+ ":"+minutes+":"+seconds);
    } 
    return final_date;  
    }
}
