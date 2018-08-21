# Sample_Java
This repository contains sample code written in Java
The files included are for a horse race simulator (main class) and a methods class
The predictions are based on a regression equation which estimates a mean speed in ft/sec, and a standard deviation of speed. Individual horse speeds are constant, and the simulation randomizes where within their standard deviation they will run. The horse with the fastest speed is declared the winner. 
For example, horse A has mean time of 40 ft/sec and standard deviation of 2 ft/sec. Horse B has a mean time of 39 ft/sec and standard deviation of 2 ft/sec. Horse A is randomized to a percentile equat to -1 ft/sec and Horse B is randomized at +1.5 ft/sec. Thus, Horse A runs at 40 - 1 = 39 ft/sec and Horse B runs at 39 + 1.5 = 40.5 ft/sec. Horse B wins this race. 10,000 races are simulated and a "win probability" is created for each horse based on the number of races each one wins.
