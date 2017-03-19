package test.java;
import com.example.Tests.Dao_Testers.DAO_TESTER;

import org.junit.runner.JUnitCore;

/**
 * Created by Michael on 2/23/2017.
 */

public class TestDriverMain {
    public static void main(String[] args){
        org.junit.runner.JUnitCore.main(
                "com.example.Tests.Dao_Testers.DAO_TESTER",
                "com.example.Tests.Dao_Testers.RegisterServiceTester",
                "com.example.Tests.Dao_Testers.ProxyTest"
        );
    }
}
