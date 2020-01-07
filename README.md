# FaultyStorage
The purpose of the project is tolerating failures over time during reading and writing of files to a fault-prone storage.  

The faults that are handled are:  
- Corruption of files
- Read failures
- Write failures

The program uses replication with voting as a strategy to detect and correct faults. 
It can recover the original file while handling corruption rates of upto 100% and read and write failures upto 95% of the times for file sizes of at least 80kB.  
