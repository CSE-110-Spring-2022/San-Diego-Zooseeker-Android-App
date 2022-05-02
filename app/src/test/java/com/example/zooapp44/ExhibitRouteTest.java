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
        List<String> exhibits = Arrays.asList("tiger", "monkey", "goat");
        List<String> Distance = Arrays.asList("300ft", "200ft", "100ft");

        ExhibitRoute exhibitRoute = new ExhibitRoute(exhibits, Distance);
        String encoded = ExhibitRoute.serialize(exhibitRoute);
        ExhibitRoute decoded = ExhibitRoute.deserialize(encoded);

        assertEquals(exhibitRoute.toString(), decoded.toString());
    }
}