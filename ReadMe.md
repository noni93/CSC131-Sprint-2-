# **Lakhwinder Singh**
##### Task Manager v1.2
##### Individual Project - Sprint 2 (Some new features and tools)
##### Feb 14, 2017

### Design
---
The program is updated version of Individual project- Sprint 1. The program added following tools and features for the user: 

* added a command size that takes a task name and task size : java TM size \<task name> \<task size>   
* modify the describe command to take an additional optional size parameter : java TM describe \<task name> \<task description> \<task size>
* user will be able to give a deeper description to each task

Program consists of four classes: **main class, logfileEntry, writeLog, filelinkedlist**. WriteLog class saves the command provided by the user to the file like start, stop, describe, size. LogFileEntry class consists of variables as taskname, command, tasksize, description, occurances, tasktime. FileLinkedList class reads from the log file and saves the information to linked list that is created on based of Abstract Data Type class LogFileEntry. Each line is split into tokens and tokens are assigned to correct class variables and stored into lilnked list. After that the list is sorted by comparing and getting the nodes that have same task name, and combined into one element.

#### Design updates and choices
---
1. The choice how program reads data from the file and stores had been modified completely. Last version _v1.1_ used 3 linked lists with String data type, and separaters like commas, period, but this version has been updated to use only 1 linked list using Abstract Data Type and separaters have been removed, using tokens by splitting the String line, and saving info into correct slots. 
2. User is now able to add more detailed information for the task, as _v1.2_ appends the new description to older description and prints all the description.  
3. Time functionality has been improved, _v1.2_ allows users to add multiple instances of one task, and keeps track of total time spent, and adding up the time of all the instances. 
4. Use of multiple classes, and function calls within functions has been removed to improve speed and not to complicate the logic. 

__choices for better sorting the linked list__  
For sorting the list TreeSet could have been used but lack of information and how to use it was a road block in implementation, so use of comparison by for loops, and if statements have been used.   

#### Application Limitation
---
* The program has the limitation that it depends on the user for entering the commands in correct order. i.e. **\<start>  ---> \<stop>/\<describe>/\<size>**

#### Conclusion
---
The program performs all the tasks required by the instructor. 


