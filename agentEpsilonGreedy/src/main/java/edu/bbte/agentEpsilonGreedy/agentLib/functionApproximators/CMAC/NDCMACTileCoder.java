/*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package edu.bbte.agentEpsilonGreedy.agentLib.functionApproximators.CMAC;

import edu.bbte.packages.ParameterHolder;
import edu.bbte.packages.taskSpec.TaskSpec;
import edu.bbte.packages.types.Observation;

/**
 * NOT THREAD SAFE
 * @author Brian Tanner
 */
public class NDCMACTileCoder implements FeatureBasedFunctionApproximatorInterface {
double[] theWeights=null;
double theGridSize=1.0d;
TileCoder theTC=null;
int numTilings=1;
int[] activeTiles=null;


public static void addToParameterHolder(ParameterHolder p){
        p.addIntegerParam("cmac-gridSize", 16);
        p.addIntegerParam("cmac-numTilings", 16);
        p.addIntegerParam("cmac-memorySize", 50000);
}

public NDCMACTileCoder(TaskSpec tso,ParameterHolder p){
        int memorySize=p.getIntegerParam("cmac-memorySize");
        this.theGridSize=p.getIntegerParam("cmac-gridSize");
        this.numTilings=p.getIntegerParam("cmac-numTilings");

        theWeights=new double[memorySize];
        theTC=new TileCoder();
        activeTiles=new int[numTilings];

    }
    public void init() {

    }
/**
 * 
 * @param normalizedObservation
 * @param delta Assume that delta is pre-alpha reduced
 */
    public void update(Observation normalizedObservation, double delta) {
        fillTiles(normalizedObservation,-1);
        
        double splitWeight=delta/(double)activeTiles.length;
        for (int thisTile : activeTiles)theWeights[thisTile]+=splitWeight;
    }

    private void fillTiles(Observation normalizedObservation,long uniqueId){
        fillTiles(activeTiles,normalizedObservation,uniqueId);
    }
    
    private void fillTiles(int[] tilesToFill,Observation normalizedObservation,long uniqueId){
        //This could surely be a member
        int tileStartOffset=0;
        int memorySize=theWeights.length;
        int[] noInts=new int[0];
        
        double[] scaledDoubles=new double[normalizedObservation.doubleArray.length];
        for(int i=0;i<scaledDoubles.length;i++)scaledDoubles[i]=normalizedObservation.doubleArray[i]*theGridSize;

        theTC.tiles(tilesToFill, tileStartOffset, numTilings,memorySize,scaledDoubles,noInts);
        
    }

    public double query(Observation normalizedObservation) {
        return query(normalizedObservation,-1);
    }

    
    public double query(Observation normalizedObservation, long uniqueId) {
        //Check if uniqueId is valid or not
        double valueEstimate=0.0d;

        fillTiles(normalizedObservation,uniqueId);
        for (int thisTile : activeTiles)valueEstimate+=theWeights[thisTile];
        
        return valueEstimate;
            
    }

    
    
/*
 The next few functions are because we implement FeatureBasedFunctionApproximatorInterface
 */
    public int getMemorySize() {
        return theWeights.length;
    }

    public int getActiveFeatureCount() {
        return numTilings;
    }

    public void setActiveFeatures(Observation normalizedObservation,int[] theFeatures) {
        fillTiles(theFeatures,normalizedObservation,-1);
    }

    public void incrementFeatureByAmount(int whatFeature, double delta) {
        double splitWeight=delta/(double)activeTiles.length;
        theWeights[whatFeature]+=splitWeight;
    }

    public void cleanup() {
        this.activeTiles=null;
        this.theTC=null;
        this.theWeights=null;
    }

}
