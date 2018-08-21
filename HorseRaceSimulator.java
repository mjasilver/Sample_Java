import java.net.*;
import java.text.*;
import java.io.*;
import java.util.Scanner;
import java.util.*;
import org.apache.pdfbox.*;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
//import org.apache.pdfbox.pdfparser.BaseParser;
//import org.apache.pdfbox.pdfparser.COSParser;
import org.apache.pdfbox.io.RandomAccessRead;

public class HorseRaceSimulator{  
  public static void main (String[] args) throws Exception, IOException { 
  
  Random randomNum = new Random();
  
  String raceLine="";
  String raceSet="";
  String raceSetPrev="";
  String dateLine="";
  double raceTime = 0;
 // double meanVal = 0;
 // double stdDev = 0;
  int tokenCount=0;
  int lineCount=0;
  int raceCount=0;
  int dateCount=0;
  int numSims = 10000;
  int intDate = 0;
  int raceDateInt=0;
  boolean simulateRace=false;
  int progressCounter=0;
  
  HorseRaceSimulatorMethods xxxxOBJECT = new HorseRaceSimulatorMethods(0,0,0,false); 

  String[] dateAnalysisArray = new String[20];
  for (int n=0; n<dateAnalysisArray.length; n++)
   {dateAnalysisArray[n]= new String("");}
  
  String[][] raceTimeArray = new String[300000][17];   //This array will hold the information from the STATA output and pertinent information 
    for (int n = 0; n<raceTimeArray.length; n++)
     {for(int m=0; m<raceTimeArray[n].length; m++)
       {raceTimeArray[n][m] = new String("");}
     }
   //[0]:Horse Name   [1]: Track   [2]: Race Number   [3]: Date   [4]:Mean Time   [5]:Std DevPos   [6]: Actual Time  [7]: WinDummy [8]: StdDevNeg   [9]: meanCenter  [10]: estRacePaceMean (mean pace of the race, estimated. Used as a center for simulation)  [11]: paceStDev  [12]: indexAVE (Speed index for horse)  [13]: Average Speed of Field, ACTUAL  [14]: beta coefficient of Pace  [15]: Betting odds [16]: space
      
   Double[][] raceDoubleArray = new Double[300000][16];  //This array performs the calculations
    for (int n = 0; n<raceDoubleArray.length; n++)
     {for(int m=0; m<raceDoubleArray[n].length; m++)
       {raceDoubleArray[n][m] = new Double(0);}
     }
   //[0]: Mean Time  [1]: Std Dev Pos  [2]: Race Count  [3]: Estimated Time in Simulated Race   [4]: Number of Wins in Simulation  [5]: Win Percentage  [6]: Actual Time  [7]: WinDummy  [8]:Std Dev Neg  [9]: Mean Center  [10]: estRacePaceMean (mean pace of the race, estimated. Used as a center for simulation)  [11]: paceStDev  [12]: indexAVE (Speed index for horse)  [13]: Average Speed of Field, ACTUAL  [14]: beta coefficient of Pace   [15]: Betting odds

    
//This section of code keeps the races being run today and deletes the rest. This is to ensure that the next file produces only the probabilities of the active races.
//Change the date of the section if you want to analyze all past history races, or a certain range of the history.

    String raceTimeFileAll = "c:\\Users\\User\\Desktop\\racePredictions.txt";
    Scanner raceTimeScannerAll = new Scanner(new File(raceTimeFileAll));    
    String todaysRaces = "c:\\Users\\User\\Desktop\\racePredictionsTodayOnly.txt";
    PrintWriter writerResultsToday = new PrintWriter(todaysRaces);   

    Date date;
    date = Calendar.getInstance().getTime();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
    String stringDate = dateFormat.format(date);
    System.out.println(stringDate);
//*******BE CAREFUL with this line. The line below is a trouble-shooting line. Delete this line when you're running live
stringDate="161001";
intDate = Integer.parseInt(stringDate);
    while(raceTimeScannerAll.hasNext()){
      dateLine=raceTimeScannerAll.nextLine();
      xxxxOBJECT.clearArray_StringOneDim(dateAnalysisArray);
      xxxxOBJECT.tokenizeString(dateAnalysisArray, dateLine, dateCount);
//Re-insert the commented IF here when in real-time to select for only the current date's races
if(!dateAnalysisArray[3].equals("datereformat") && !dateAnalysisArray[3].equals("")){raceDateInt=Integer.parseInt(dateAnalysisArray[3]);} 
      if(raceDateInt>intDate) //Can also have this set against stringDate and use dateAnalysisArray[3] and test equality to select just for today's races
       {
        writerResultsToday.print(dateLine);
        writerResultsToday.println();
       }
      dateCount=dateCount+1;
    }
    System.out.println("Out of the WHILE loop");
    raceTimeScannerAll.close();
    writerResultsToday.close();

//END SECTION. Now, only today's races are represented in the racePredictionsTodayOnly.txt file    
    
    
    System.out.println("got here");
//   String raceTimeFile = "d:\\Equibase\\raceTimeEstimates.txt";
    String raceTimeFile = "c:\\Users\\User\\Desktop\\racePredictionsTodayOnly.txt";
//   String raceTimeFile = "d:\\Equibase\\raceTimeEstimatesTrial.txt";
    Scanner raceTimeScanner = new Scanner(new File(raceTimeFile));

    raceCount=0;
    while(raceTimeScanner.hasNext()){     
      //here, we are looking for the name of the PDF file in the TXT file. If the PDF name is located in the TXT folder, you've already parsed it, so you skip the processing of the file and move to the next line
      simulateRace=false;
      raceLine=raceTimeScanner.nextLine();
      progressCounter=progressCounter+1;
 if(progressCounter%1000==0)
  {System.out.println(progressCounter);}     
      if(lineCount==0) //This bit is important because STATA produces headers in its .txt output. So, this lets you skip that first line.
       {raceLine=raceTimeScanner.nextLine(); 
        //lineCount=1; Something about how the methods were designed means you don't increment this first one forward
       }   
//      System.out.println(raceLine);
      xxxxOBJECT.tokenizeLine(raceTimeArray, raceLine, lineCount, raceDoubleArray);
      if(xxxxOBJECT.getSimulateBoolean()==true && lineCount!=0)  //Make sure that lineCount!=0 because this would trigger a false-positive 
       {
//        System.out.println("gets here");
        xxxxOBJECT.setSimulateBooleanFalse();        
        xxxxOBJECT.simulateRace(raceTimeArray, numSims, raceDoubleArray);
        xxxxOBJECT.setFirstHorseAt(lineCount); //The method works when you identify a new race. You can't tell you're looking at a new race until you encounter a line that has a new race in it, so the line you're looking at -- lineCount -- is not part of the old race, but the first line in the new race. So, firstHorseAt gets assigned lineCount.
//System.out.println("New Horse Set Correctly? First Horse at: "+xxxxOBJECT.getFirstHorseAt());
      }
      lineCount=lineCount+1;
 //*******BE CAREFUL with this line. This terminates the system at 50000 because it was stalling at 100,000. This just lets you get some of the sample as opposed to all.
      if(progressCounter==50000)//This should normally be commented out. Java runs out of heap space, so pick a cutoff when you're analyzing a large set
      {break;}//This should normally be commented out. Java runs out of heap space, so pick a cutoff when you're analyzing a large set
    }
    
//System.out.println("Remember that the last race won't get triggered to simulate if there isn't a differing track-race combo on the next line, so this will have to be corrected.");   
//   System.out.println("LineCount: "+lineCount);
   //Need to trigger a simulation one more time because the simulateRace() method relies on identifying a new set of horses. However, the new set of horses doesn't get recognized because the last line of the file is a blank, thus there is no new set of horses. So, if the last three lines are Race 3, Race 3, Race 3 -- you don't see "Race 4" that would trigger a simulation because it changed. Therefore, you need to run one more simulation here to get this last race to simulate. 
   xxxxOBJECT.setLastHorseAt(lineCount-1); 
   xxxxOBJECT.simulateRace(raceTimeArray, numSims, raceDoubleArray);
System.out.println("Remember that the last race won't get triggered to simulate if there isn't a differing track-race combo on the next line, so this will have to be corrected.");   

   raceTimeScanner.close();
   
    String raceResultsEstimates = "d:\\racedata\\raceResultsEstimates.txt";
    PrintWriter writerResults = new PrintWriter(raceResultsEstimates);
//Now, print out the results to an external file   
   for(int j=0; j<=lineCount; j++)
   {
     for(int q=0; q<=3; q++)
      {writerResults.print(raceTimeArray[j][q]+",");}
     for(int k=0; k<16; k++)  //only need to print the results from [0] to [10]. The ones after that aren't needed.
      {if(k!=3 && k!=10 && k!=11 && k!=12 && k!=13 && k!=14){writerResults.print(raceDoubleArray[j][k]+",");}} //Don't include [k][3], because this one just stores the simulated racetime for each race. At the time of printing, it just holds the last simulated racetime and thus, isn't needed.
     writerResults.println();
   }
   writerResults.close();
  }
}