package Main;

import sun.nio.cs.ext.IBM037;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

/**
 * Created by Alex on 04.11.2015.
 */
public class RequestsParser {


   /*public String Build(Tag t) {
       String res = new String();
       res += oId;
       res += CommonData.SEP;
       for (int i = 0; i < ar.size(); i++) {
           res += ar.get(i);
           res += CommonData.SEP;
       }
   }

    public String Build(User u) {
        String res = new String();
        res += oId;
        res += CommonData.SEP;
        for (int i = 0; i < ar.size(); i++) {
            res += ar.get(i);
            res += CommonData.SEP;
        }
    }

    public String Build(Note n) {
        String res = new String();
        res += oId;
        res += CommonData.SEP;
        for (int i = 0; i < ar.size(); i++) {
            res += ar.get(i);
            res += CommonData.SEP;
        }
    }

    public String Build(NotePrimitive np) {
        String res = new String();
        res += oId;
        res += CommonData.SEP;
        for (int i = 0; i < ar.size(); i++) {
            res += ar.get(i);
            res += CommonData.SEP;
        }
    }*/

    public String Build(ArrayList<String> ar, int oId) {
        String res = new String();
        res+=oId;
        res+=CommonData.SEP;
        for (String anAr : ar) {
            res += anAr;
            res += CommonData.SEP;
        }

        return res;
    }

    public String Build(int oId, ArrayList<Integer> ar) {
        String res = "";
        res+=oId;
        res+=CommonData.SEPID;
        for (Integer anAr : ar) {
            res += anAr;
            res += CommonData.SEPID;
        }
        return res;
    }

    public String Build(String buff, int oId) {
        String res = oId + CommonData.SEP + buff;
        return res;
    }

    public String Build(int buff, int oId) {
        StringBuilder res = new StringBuilder();
        res.append(oId);
        res.append(CommonData.SEP);
        res.append(buff);
        return res.toString();
    }

    public ArrayList<String> ParseListOfString(String str) {
        ArrayList<String> s = new ArrayList<String>();

        StringBuilder buff = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == CommonData.SEP) {
                s.add(buff.toString());
                buff.delete(0, buff.length());
            }
            else buff.append(str.charAt(i));
        }

        return s;
    }

    public ArrayList<Integer> ParseListOfInteger(String str) {
        ArrayList<Integer> n = new ArrayList<Integer>();

        StringBuilder buff = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == CommonData.SEP) {
                n.add(Integer.parseInt(buff.toString()));
                buff.delete(0, buff.length());
            }
            else buff.append(str.charAt(i));
        }

        return n;
    }

    public NotePrimitive ParseNotePrimitive(String str, int id) {
        //NotePrimitive np = new NotePrimitive();

        byte stage = 0;
        StringBuilder buff = new StringBuilder();

        LocalDateTime dtbuff = LocalDateTime.now();
        String ss = "";

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == CommonData.SEP) {
                switch (stage) {
                    case 0:         //*** Read creation date. ***
                        stage++;
                        dtbuff = LocalDateTime.parse(buff.toString());
                        break;
                    case 1:         //*** Read text. ***
                        stage++;
                        ss = buff.toString();
                        break;
                }
                buff.delete(0, buff.length());
            } else buff.append(str.charAt(i));
        }

        NotePrimitive np = new NotePrimitive(id, dtbuff, ss);
        return np;
    }

}
