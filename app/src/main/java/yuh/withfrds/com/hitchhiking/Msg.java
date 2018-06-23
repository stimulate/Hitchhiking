package yuh.withfrds.com.hitchhiking;

/**
 * Created by a_yu_ on 2018/6/23.
 */
public class Msg {
    public String dep;
    public String dest;
    public String l1;
    public String l2;

 public Msg(String moffer_dep, String moffer_dest, String l1, String l2){
     this.dep = moffer_dep;
     this.dest = moffer_dest;
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

}
