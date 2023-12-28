import java.util.ArrayList;
import java.util.Collections;

public class CypherV1 {

    private String encodedMessage;

    private String decodedMessage;

    private final ArrayList<Character> key1;

    public CypherV1() throws CloneNotSupportedException {
        key1 = new ArrayList<>();
        String temp = "abcdefghijklmnopqrstuvwxyz., ?;\'\"%!=+-*/(){}1234567890\n";
        temp = "tes";
        for(int x=0;x<temp.length();x++)
        {
            key1.add(temp.charAt(x));
        }

        for(int x = 0; x< key1.size(); x++)
        {
            if(Collections.frequency(key1, key1.get(x))!=1)
            {
                System.out.println(key1.get(x));
                throw new CloneNotSupportedException();
            }
        }

        encodedMessage = "No message has been encoded yet";
        decodedMessage = "No message has been decoded yet";
    }

    public String Encode(String text) {
        if(!text.isEmpty())
        {
            text = text.replaceAll("”","\"");
            text = text.replaceAll("“","\"");
            text = text.replaceAll("’","'");
            char[] message = text.toLowerCase().toCharArray();


            int CurrentPosition = 0;
            int NextPosition = 0;
            encodedMessage = "";
            for (int x = 0; x < message.length; x++) {
                if (getIndexOf(message[x]) != -1) {
                    NextPosition = getIndexOf(message[x]);
                    if (NextPosition < CurrentPosition) {
                        encodedMessage += ("" + ((key1.size() - CurrentPosition) + NextPosition) + ".");
                    } else {
                        encodedMessage += ("" + (NextPosition - CurrentPosition) + ".");
                    }
                    CurrentPosition = NextPosition;
                } else {
                    System.out.println(message[x] + " at location " + x);
                    throw new IllegalArgumentException();
                }
            }

            return encodedMessage;
        }else
        {
            throw new IllegalArgumentException();
        }

    }

    public String Decode(String text)
    {
        if(!text.isEmpty())
        {
            String[] message = text.split("\\.");
            //System.out.println(Arrays.toString(message));

            int CurrentPosition = 0;
            int NextPosition = 0;
            decodedMessage = "";
            for (int x = 0; x < message.length; x++) {
                if (Integer.parseInt(message[x]) != -1) {
                    NextPosition = Integer.parseInt(message[x]);
                    decodedMessage += getValueOf((NextPosition + CurrentPosition) % key1.size());
                    CurrentPosition = (NextPosition + CurrentPosition) % key1.size();
                } else {
                    throw new IllegalArgumentException();
                }
            }


            return decodedMessage;
        }else
        {
            throw new IllegalArgumentException();
        }
    }


    private int getIndexOf(Character letter)
    {
        for(int x = 0; x< key1.size(); x++){
            if(key1.get(x)==letter)
            {
                return x;
            }
        }
        return -1;
    }

    private Character getValueOf(int index)
    {
        return key1.get(index);
    }
}
