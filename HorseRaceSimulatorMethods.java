import java.util.Scanner;
import java.util.Random;
import java.io.*;
public class HorseRaceSimulatorMethods {
 
 String raceLine="";
 String raceSet="";
 String raceSetPrev="";
 String line="";

 int numberOfHorses=0;
 int lineCount=0;
 int firstHorseAt=0;
 int lastHorseAt=0;
 int first=0;
 int last=0;
 int numSims=0;
 int topHorse=0;
 int tokenCount=0;

 double raceCount=0;
 double time=0;
 double mean=0;
 double stDev=0;
 double topTime=0;
 double winPct=0;
 double racePaceMean=0; 
 double racePaceStDev=0;
 double simulatedPace=0;
 
 boolean simulateTriggerBoolean=false;

 Random randomNumber = new Random();
 
  //constructor
public HorseRaceSimulatorMethods(int a, int b, int c, boolean d)
    {
     firstHorseAt=a;
     lastHorseAt=b;
     numberOfHorses=c;
     simulateTriggerBoolean=false;
    }



public int getFirstHorseAt()
 {return firstHorseAt;}
public void setFirstHorseAt(int first)
 {firstHorseAt=first;}
public int getLastHorseAt()
 {return lastHorseAt;}
public void setLastHorseAt(int last)
 {lastHorseAt=last;}
public boolean getSimulateBoolean()
 {return simulateTriggerBoolean;}
public void setSimulateBooleanFalse()
 {simulateTriggerBoolean=false;}
//action methods here

public void tokenizeString(String array[], String li, int da)
{
  line=li;
  lineCount=da;
  tokenCount=0;
  
    for(int j=0; j<line.length(); j++)
     {
      if(line.charAt(j)==',')
       {tokenCount=tokenCount+1;}
       if(line.charAt(j)!=',')
        {array[tokenCount]=array[tokenCount]+line.charAt(j);}
      } //
    
}//END METHOD

public void clearArray_StringOneDim(String array[])
{
  for (int j=0; j<array.length; j++)
   {array[j]="";}
}//END METHOD

public void tokenizeLine(String raceTimeArray[][], String line, int liCount, Double raceDoubleArray[][])
{
    lineCount=liCount;
    raceLine=line;
    tokenCount=0;
    raceSet="";
    simulateTriggerBoolean=false;
//    System.out.println(line);
    if(lineCount==0)
    {
      raceCount=1; 
      raceSet=raceTimeArray[lineCount][2]+raceTimeArray[lineCount][3];
      raceSetPrev=raceSet;
    }
//    if(lineCount!=0){raceCount=raceDoubleArray[lineCount-1][2];}
    
    for(int j=0; j<raceLine.length(); j++)
     {
      if(raceLine.charAt(j)==',')
       {
        tokenCount=tokenCount+1;
        if(tokenCount==3)
         {
          raceSet=raceTimeArray[lineCount][2]+raceTimeArray[lineCount][3]; //This is a combination of the race number and the track. 
// System.out.println(raceSet);
          if(raceSet.equals(raceSetPrev)) //This is where you show which race the horses belong to. If its the same raceSet, you don't increment up the raceCount. If they differ, you'll increment up the raceCount and store it.
           {
// System.out.println("passes boolean");           
            raceDoubleArray[lineCount][2]=raceCount;}
          else{
            raceCount=raceCount+1;
            raceSetPrev=raceTimeArray[lineCount][2]+raceTimeArray[lineCount][3];
            raceDoubleArray[lineCount][2]=raceCount;
            lastHorseAt=lineCount-1; 
            simulateTriggerBoolean=true;
// System.out.println("boolean hits true");           
              }
          }
        }
       if(raceLine.charAt(j)!=',')
        {raceTimeArray[lineCount][tokenCount]=raceTimeArray[lineCount][tokenCount]+raceLine.charAt(j);}
      } //

    //Now, place the numbers into the doubleArray so we don't have to do tons of String-to-Integer/Double things later
//System.out.println(raceTimeArray[lineCount][0]+" "+raceTimeArray[lineCount][1]+" "+raceTimeArray[lineCount][2]+" "+raceTimeArray[lineCount][3]+" ");
    raceDoubleArray[lineCount][0]=Double.parseDouble(raceTimeArray[lineCount][4]); //prediction
    raceDoubleArray[lineCount][1]=Double.parseDouble(raceTimeArray[lineCount][5]); //stdDevPos of prediction
    raceDoubleArray[lineCount][6]=Double.parseDouble(raceTimeArray[lineCount][6]); //actual time
//System.out.println(raceTimeArray[lineCount][6]);
    raceDoubleArray[lineCount][7]=Double.parseDouble(raceTimeArray[lineCount][7]); //winDummy
//System.out.println(raceTimeArray[lineCount][7]);
    raceDoubleArray[lineCount][8]=Double.parseDouble(raceTimeArray[lineCount][8]); //stdDevNeg of prediction
//System.out.println(raceTimeArray[lineCount][8]);
    raceDoubleArray[lineCount][9]=Double.parseDouble(raceTimeArray[lineCount][9]); //meanCenter of prediction
//System.out.println(raceTimeArray[lineCount][9]); 
    raceDoubleArray[lineCount][10]=Double.parseDouble(raceTimeArray[lineCount][10]); //estRacePaceMean. This is the estimate of the mean pace that the race will be run at. This will form the basis for the pace-variable that will be simulated, along with its standard deviation
    raceDoubleArray[lineCount][11]=Double.parseDouble(raceTimeArray[lineCount][11]); //paceStDev. For Pace simulations, this is the standard deviation for the simulation.
    raceDoubleArray[lineCount][12]=Double.parseDouble(raceTimeArray[lineCount][12]); //indexAVE. This is the average speed index for each horse, if the horse has a race history. If the horse doesn't have a history, this is given a value of 1, which will come into play later when we augment the regression.
    raceDoubleArray[lineCount][13]=Double.parseDouble(raceTimeArray[lineCount][13]); //average speed of field, actual. This is used to back out a variable from the regression. Later we're going to simulate the pace, so we need to remove the ACTUAL speed of the field so we can replace it with the simulated speed of the field.
    raceDoubleArray[lineCount][14]=Double.parseDouble(raceTimeArray[lineCount][14]); //betaPACE. This will be used in the calculation of the race speed. This is the coefficient from the regression.
    
    if(raceTimeArray[lineCount][15].equals(""))
      {raceTimeArray[lineCount][15]="-1";} //This deals with NULL responses, which happens occasionally. Since -1 is an invalid odds measure, put it in as a negative so it's obvious what the NULLs were
    raceDoubleArray[lineCount][15]=Double.parseDouble(raceTimeArray[lineCount][15]); //odds. This will be used in the calculation of the race speed. This is the coefficient from the regression.
    
    
//    for(int j=0; j<10; j++)
//     {System.out.print(raceTimeArray[lineCount][j]+",");}
//    System.out.println();
//    for(int j=0; j<10; j++)
//     {System.out.print(raceDoubleArray[lineCount][j]+",");}
//    System.out.println();


}//END METHOD


public void simulateRace(String raceTimeArray[][], int nS, Double raceDoubleArray[][])
{
  numSims=nS;
//System.out.println("Race Simulating. First Horse: "+raceTimeArray[firstHorseAt][0]+"   Last Horse: "+raceTimeArray[lastHorseAt][0]);  
  racePaceMean=0;
  racePaceStDev=0;
  simulatedPace=0;
  
 for(int j=0; j<numSims; j++){
  topTime=0;
  topHorse=0;
  
  //Simulating the Pace here
  racePaceMean=raceDoubleArray[firstHorseAt][10]; //You can use firstHorseAt (or any horse here) because all the horses in the same race have the same value here.
  racePaceStDev=raceDoubleArray[firstHorseAt][11];

  simulatedPace=racePaceMean+randomNumber.nextGaussian()*racePaceStDev; //This is the simulated pace for this race.
  
  for(int k=firstHorseAt; k<=lastHorseAt; k++) //Getting times for each of the horses here
  {
   //First, we need to make a correction to the estimated mean for the horse.
     //The regression uses a variable that is Beta*speedIndex*ACTUALaveragespeedOfTheField. The theory here is that we need to know the effect that paces have on the outcome of the race, which is very influential. An assumption is violated, however, because we do not actually know the ACTUAL pace of the race until after the race is over.
       //To get around this, we have a regression that estimates the pace of the race, and then we simulate around that mean based on the standard deviation. Thus, we can run the gamut of possible paces that could be run, and we know the relative affect that it would have on each horse in the race.
       //However, the value at raceDoubleArray[k][0] (which is the estimated time that the horse will run) includes the ACTUAL average speed of the field, multiplied by speed Index, multiplied by Beta. So, we must remove this Beta*ACTUALpace*speedIndex and replace it with Beta*SIMULATEDpace*speedIndex to get the new mean for the horse.
         //This editing process begins below.
   //Backing out the ACTUAL-versus-SIMULATED pace effect in this next section, as described above.
    mean=raceDoubleArray[k][0]; //This is the original estimated mean for the horse, as imported from STATA. This will be augmented below.
      
//*****EVENTUALLY PUT THIS BACK IN, ONCE MORE RESEARCH HAS BEEN DONE. This is still a cool idea, that there is a pace to the race that affects horses differently, and should be accounted for
        //However, it needs to be developed more before implementation
         
         mean = mean-raceDoubleArray[k][14]*raceDoubleArray[k][13]*raceDoubleArray[k][12]; //MEAN - (Beta*ACTUALpace*speedIndex). Here, you remove the effect of the ACTUAL pace, so that you can replace it with the simulated pace underneath.
         mean = mean+raceDoubleArray[k][14]*simulatedPace*raceDoubleArray[k][12]; //MEAN + (Beta*SIMULATEDpace*speedIndex). Now, you have the mean time the horse is expected to run given the simulated pace.
         
    
  //Now that the horse has the correct mean speed (according to the simulated pace), you simulate the standard deviation about that mean 
   if(randomNumber.nextDouble()<raceDoubleArray[k][9]) //If the randomNumber is greater than the meanCenter, it is on the "positive" side of the distribution. Therefore, calculate the positive standard deviation. Otherwise, use the negative standard deviation. Note that the way this is defined in STATA is that the meanCenter is the number of observations that are on the "right of the mean" (positive side) in the distribution. Most of the buckets have a "positive" side of the mean that is over 50%. So, most of the races are run at higher than their mean value. So, this will get programmed at like 51%. So, [k][9] will equal like .51. So, to get the positive side of the mean, you'd have to pull a number that is LESS than .51 (because that is also 51% of the probabilties).
    {stDev=raceDoubleArray[k][1];} //This is the positive-sided standard deviation
   else{stDev=-1*raceDoubleArray[k][8];}  //This is the negative-sided standard deviation
   time=mean+randomNumber.nextGaussian()*stDev;
   raceDoubleArray[k][3]=time;
//System.out.println("Horse "+k+" : "+time);   
   if(time>topTime)
    {
     topHorse=k; 
     topTime=time;
    } //This tracks the top time for the race as each horse comes through. When the simulations end, whichever horse is the topHorse earns credit for the win 
  }
//  System.out.println("Winning Horse: "+topHorse);


  //Adding wins to the horse who won
  raceDoubleArray[topHorse][4]=raceDoubleArray[topHorse][4]+1; //Adds one to the win total of the horse
  
 }//End of simulation
 
 //Now, assign a win percentage to each horse
 for(int z=firstHorseAt; z<=lastHorseAt; z++)
 {
  winPct=0;
  winPct=(double)raceDoubleArray[z][4]/(double)numSims;
  raceDoubleArray[z][5]=winPct; 
//  System.out.println(raceTimeArray[z][0]+": "+raceDoubleArray[z][5]);
 }
 
}//END METHOD

}