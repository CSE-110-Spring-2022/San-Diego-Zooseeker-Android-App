package com.example.zooapp44;

import org.junit.Test;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExhibitRouteTest {
    @Test
    public void addition_isCorrect() {
        List<String> exhibits = Arrays.asList("tiger", "monkey", "fish");
        //List<String> Distance = Arrays.asList("300ft", "200ft", "100ft");
        ZooGraph.Vertex one=null;
        /*
        one.id="tiger";
        one.kind="exhibit";
        one.name="Big tiger";
        one.selected=true;
        */

        ZooGraph.Vertex two=null ;
        /*
        two.id="monkey";
        two.kind="exhibit";
        two.name="Victor Zhen";
        two.selected=true;
        */

        ZooGraph.Vertex three = null;
        /*
        three.id="fish";
        three.kind="exhibit";
        three.name="Your mother";
        three.selected=true;

         */
        List<ZooGraph.Vertex> vertices= Arrays.asList(one,two,three);

        ZooGraph.Edge oneToTwo=null;
        /*
        oneToTwo.id="edge_1";
        oneToTwo.street="Tragic Road";

         */
        ZooGraph.Edge twoToThree=null;
        /*
        twoToThree.id="edge_2";
        twoToThree.street="Depression Road";

         */
        List<ZooGraph.Edge> edges= Arrays.asList(oneToTwo,twoToThree);

        List<Double> weights= Arrays.asList(300.0,200.0);

        ExhibitRoute exhibitRoute = new ExhibitRoute(vertices,edges,weights,exhibits);
        String encoded = ExhibitRoute.serialize(exhibitRoute);
        ExhibitRoute decoded = ExhibitRoute.deserialize(encoded);

        assertEquals(exhibitRoute.toString(), decoded.toString());
    }
}