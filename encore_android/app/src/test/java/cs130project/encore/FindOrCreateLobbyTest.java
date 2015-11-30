package cs130project.encore;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by Shubham on 11/30/2015.
 */
public class FindOrCreateLobbyTest extends ActivityInstrumentationTestCase2<FindOrCreateLobby> {
    private FindOrCreateLobby test;
    public FindOrCreateLobbyTest(){
        super(FindOrCreateLobby.class);
    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        test = getActivity();
        testPreconditions();
    }

    public void testPreconditions(){
        assertNotNull(test);
    }
}
