package com.kb5012.timetable.DataModels;

import java.util.ArrayList;

/**
 * Created by Jordi on 6-1-2016.
 */
public class DummieData {
    public User u1;
    public User u2;
    public User u3;
    public User u4;
    public Group g1;
    public Group g2;
    public ArrayList<Group> groups;

    public void maakDummies(){
        u1.setId(1);
        u1.setFirstName("Luke");
        u1.setLastName("Achternaam");

        u2.setId(2);
        u2.setFirstName("Clarrisa");
        u2.setLastName("Medici");

        u3.setId(3);
        u3.setFirstName("Leo");
        u3.setLastName("de Griek");

        u4.setId(4);
        u4.setFirstName("Evert");
        u4.setLastName("Bax");

        g1.addGroupMember(u1);
        g1.addGroupMember(u2);
        g1.addGroupMember(u3);
        g1.setName("De eerste");
        g1.setBeheerder(u2);

        g2.addGroupMember(u4);
        g2.addGroupMember(u3);
        g2.setName("De schutters");
        g2.setBeheerder(u4);

        groups.add(g1);
        groups.add(g2);
    }

}
