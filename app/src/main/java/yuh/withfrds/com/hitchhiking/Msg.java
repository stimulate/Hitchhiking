package yuh.withfrds.com.hitchhiking;

import android.location.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by a_yu_ on 2018/6/23.
 */
    public class Msg {
        public String dep;
        public String dest;
        public ArrayList<Tuple> path;
        public String l1;
        public String l2;


        public Msg(String moffer_dep, String moffer_dest, String l1, String l2, ArrayList<Tuple> mpath){
         this.dep = moffer_dep;
         this.dest = moffer_dest;
         this.path = mpath;
         this.l1 = l1;
         this.l2 = l2;
     }

    public String getDep(){
         return dep;
     }
     public String getDest(){
        return dest;
    }
    public String getl1(){
            return l1;
        }
    public String getl2(){
        return l2;
    }



    private Location getLocation(String loc){

        String[] parts = loc.split(",");
        double lat = Double.parseDouble(parts[0]);
        double lon = Double.parseDouble(parts[1]);

        Location targetLocation = new Location("");//provider name is unnecessary
        targetLocation.setLatitude(lat);//your coords of course
        targetLocation.setLongitude(lon);
        return targetLocation;
    }
    // added by Tim
    public Location getDepLocation(){
        return getLocation(l1);

    }
    public Location getDestLocation(){
        return getLocation(l2);
    }
    public ArrayList<Tuple> getPath(){
            return path;
    }
}
