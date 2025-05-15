package lk.javainstitute.has.util;

import java.util.Random;

public class AccNumbGenerator {
    public static int getAccNumber(){
        Random random = new Random();
        return random.nextInt(10000000);
    }
}
