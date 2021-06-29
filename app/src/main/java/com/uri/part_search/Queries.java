package com.uri.part_search;

/**
 * Created by kstanoev on 1/23/2015.
 */
public class Queries {

    String catNo, description, category, voltage, current, power, voutMin;
    String url = "https://docs.google.com/spreadsheets/d/14pcekUttPmUraLYwq0dQumA0Ro7ecxc59f0Ye49LLZw/gviz/tq?tq=";
    String query [] = new String[7];
    String queryString = "";

    public void queryString() {
        query[0] = catNo == "" ? "" : "SELECT%20*%20WHERE%20A%20CONTAINS%20" + catNo;
        query[1] = description == "" ? "" : "SELECT%20*%20WHERE%20A%20CONTAINS%20" + description;
        query[2] = category == "" ? "" : "SELECT%20*%20WHERE%20C%20%3D%20" + category;
        query[3] = voltage == "" ? "" : "SELECT%20*%20WHERE%20D%20%3D%20" + voltage;
        query[4] = current == "" ? "" : "SELECT%20*%20WHERE%20E%20%3D%20" + current;
        query[5] = power == "" ? "" : "SELECT%20*%20WHERE%20F%20%3D%20" + power;
        query[6] = voutMin == "" ? "" : "SELECT%20*%20WHERE%20G%20%3D%20" + voutMin;
        for (int c = 1; c == 7; c++) {
            queryString = queryString == "" ? queryString + query[c] : queryString + "and" + query[c];
        }
    }
    // select all matches:
    // SQL:
    // SELECT *  WHERE A = "AD1024-12F"
    public static String selectAll = "https://docs.google.com/spreadsheets/d/14pcekUttPmUraLYwq0dQumA0Ro7ecxc59f0Ye49LLZw/gviz/tq?tq=select%20*%20where%20A%20%3D%20%22AD1024-12F%22";
    // select all matches before 1900
    // SQL:
    // SELECT * WHERE D = 12
    public static String selectAllBefore1900 = "https://docs.google.com/spreadsheets/d/14pcekUttPmUraLYwq0dQumA0Ro7ecxc59f0Ye49LLZw/gviz/tq?tq=select%20*%20WHERE%20D%20%3D%2012";
    // select all the wins of Aston Villa between 1990 and 2000
    // SQL:
    // SELECT * WHERE A CONTAINS "AD"
    public static String selectWinsAstonVilla1900_2000 = "https://docs.google.com/spreadsheets/d/14pcekUttPmUraLYwq0dQumA0Ro7ecxc59f0Ye49LLZw/gviz/tq?tq=SELECT%20*%20WHERE%20A%20CONTAINS%20%22AD%22";
    // select all the wins of Liverpool since 1888
    // SQL:
    // select * where ((D contains 'Liverpool' and E>G) or (F contains 'Liverpool' and G > E))
    public static String selectWinsLiverpool = "https://spreadsheets.google.com/tq?tqx=out:JSON&tq=select%20%2A%20where%20%28%28D%20contains%20%27Liverpool%27%20and%20E%3EG%29%20or%20%28F%20contains%20%27Liverpool%27%20and%20G%20%3E%20E%29%29&key=1o728ZECFa8A05nqlN50e9aPicnYheb26SS2gMwVMxsE";
    // select all matches of season 2013-2014
    // SQL:
    // select * where (A=2013 and B>7) or (A=2014 and B<7)
    // A season in the English Premier League is considered the period between September, YEAR and May, YEAR+1
    public static String selectSeason2013_2014 = "https://spreadsheets.google.com/tq?tqx=out:JSON&tq=select%20%2A%20where%20%28A%3D2013%20and%20B%3E7%29%20or%20%28A%3D2014%20and%20B%3C7%29&key=1o728ZECFa8A05nqlN50e9aPicnYheb26SS2gMwVMxsE";
}
