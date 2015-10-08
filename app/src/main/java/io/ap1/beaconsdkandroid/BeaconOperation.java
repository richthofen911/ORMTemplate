package io.ap1.beaconsdkandroid;

/**
 * This class contains all beacon operations definition.
 */
public class BeaconOperation{

    public boolean equals (Beacon beacon1, Beacon beacon2){
        return ((beacon1.getUuid().equals(beacon2.getUuid())) &&
        beacon1.getMajor().equals(beacon2.getMajor()) &&
        beacon1.getMinor().equals(beacon2.getMinor()));
    }

    public boolean isLessThan(Beacon beacon1, Beacon beacon2){
        if(beacon1.equals(beacon2))
            return false;
        if(beacon1.getUuid().equals(beacon2.getUuid())){
            if(beacon1.getMajor().equals(beacon2.getMajor())){
                return (Integer.parseInt(beacon1.getMinor()) < Integer.parseInt(beacon2.getMinor()));
            }
            return (Integer.parseInt(beacon1.getMajor()) < Integer.parseInt(beacon2.getMajor()));
        }
        return (Integer.parseInt(beacon1.getUuid()) < Integer.parseInt(beacon2.getUuid()));
    }
}
