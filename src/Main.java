import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class Main {

    public static void main(String[] args) throws CloneNotSupportedException {


        CypherV1 cypher = new CypherV1();
       // String message = "My next idea is that depending on the message, certain letters could be removed from the lists used. Such as if the message doesn't contain a “U”, then the “U” is removed from the list\n" +"This has 2 benefits, reduces memory used and increases the strength of the encryption as the person trying to break it should have a certain knowledge about the message itself to try and find the key. It can also help when giving the key to another person, as less information needs to be transmitted.\n";
        String message = "test";
        System.out.println(cypher.Encode(message));
        String encryptedmessage = "";
        System.out.println(cypher.Decode("0."));


        /*
        CypherV2 cypher = new CypherV2();
        String message = "The good thing with this type of encryption is that patterns are very difficult to find, as the only pattern visible is when two of the same characters are next to each other. Because the space between the words is also in the key, words cannot be distinguished from each other when the message is encrypted.\n";
        String encryptedmessage=cypher.Encode(message);
        System.out.println(encryptedmessage);
        //String encryptedmessage = "19.44.53.24.34.8.0.45.25.47.44.1.5.49.22.50.42.11.44.21.47.44.1.10.10.47.5.47.45.24.42.47.23.32.9.45.15.7.47.4.45.6.55.15.36.10.10.47.44.49.19.9.43.41.19.0.41.13.52.5.10.28.17.43.24.49.39.13.7.4.31.5.53.0.3.50.18.47.8.9.47.51.14.33.3.5.46.24.1.28.18.10.47.44.53.24.42.55.54.13.4.43.41.19.0.41.13.52.15.49.43.10.46.49.10.49.24.36.10.10.50.41.53.9.15.47.3.48.14.42.47.23.47.44.53.24.46.38.12.48.24.30.5.49.17.39.2.17.41.13.1.10.28.17.43.24.41.47.19.52.9.47.51.14.32.52.2.5.21.42.5.44.53.13.9.2.29.3.54.54.20.54.42.24.47.44.53.24.46.53.41.2.2.24.29.3.15.3.38.0.9.15.47.44.53.24.50.48.3.42.15.10.36.10.10.28.11.7.52.14.36.5.15.47.44.53.24.38.50.20.3.1.50.48.3.42.15.10.30.54.13.0.1.5.9.29.3.24.31.5.10.1.45.5.49.14.44.10.45.53.55.25.33.12.53.54.16.32.52.2.5.21.42.5.44.53.13.11.50.41.53.9.15.47.44.53.24.40.48.14.0.38.6.54.24.36.10.10.32.9.45.15.7.47.4.41.55.23.";
        System.out.println(Arrays.toString(cypher.Decode(encryptedmessage).toCharArray()));




        /*
        ArrayList<Character> list1 = new ArrayList<>();
        String temp = "abcdefghijklmnopqrstuvwxyz. ?";
        for(int x=0;x<temp.length();x++)
        {
            list1.add(temp.charAt(x));
        }
        Collections.shuffle(list1);
        System.out.println(list1);
        String list = list1.toString();
        System.out.println(list);
        }
     */


    }
}