/* 
 * Copyright (C) 2007, Brian Tanner
 * 
http://rl-glue-ext.googlecode.com/
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 *
 *  $Revision: 489 $
 *  $Date: 2009-01-31 14:34:21 -0700 (Sat, 31 Jan 2009) $
 *  $Author: brian@tannerpages.com $
 *  $HeadURL: http://rl-glue-ext.googlecode.com/svn/trunk/projects/codecs/Java/src/org/rlcommunity/rlglue/codec/types/Observation_action.java $
 *
 */
package edu.bbte.packages.types;

public class Observation extends RLAbstractType {

    public Observation() {
        this(0, 0, 0);
    }

    /**
     * For backwards compatibility with RL-Glue 2.x
     * @param numInts
     * @param numDoubles
     */
    public Observation(int numInts, int numDoubles) {
        this(numInts, numDoubles, 0);
    }

    public Observation(int numInts, int numDoubles, int numChars) {
        super(numInts, numDoubles, numChars);
    }

    public Observation(RLAbstractType src) {
        super(src);
    }

    public Observation duplicate() {
        return new Observation(this);
    }
    
    @Override
    public String toString() {
 
    	return super.toString();
 
    }
}