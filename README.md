# FaultyStorage
The aim is tolerating failures over time during reading and writing of files to a fault-prone storage.  

The faults that are handled are:  
- Corruption of files
- Read failures
- Write failures

Our client program (say an image capture program in a satellite) relies on the FaultyFileSystem (faults induced due to radiation, impact, debris, etc) to read and write to storage. The FaultyFileSystem is prone to failures and may cause corruption of data. Our goal is to survive these faults and recover original file after some time t.

The program uses replication with voting as a strategy to detect and correct faults. 
It can recover the original file while handling corruption rates of upto 100% and read and write failures upto 95% of the times for file sizes of at least 80kB.  
