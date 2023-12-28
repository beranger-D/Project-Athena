import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CypherV2 {
    private String encodedMessage;

    private String decodedMessage;

    private final ArrayList<Character> key1;

    private final ArrayList<Character> key2;

    public CypherV2() throws CloneNotSupportedException {
        key1 = new ArrayList<>();
        key2 = new ArrayList<>();
        //String temp1 = "abcdefghijklmnopqrstuvwxyz., ?;'‘\"%!=+-*/()1234567890\n";
        String temp1 = "abcdefghijklmnopqrstuvwxyz., ?;'‘\"%!=+-*/(){}\n";
        for(int x=0;x<temp1.length();x++)
        {
            key1.add(temp1.charAt(x));
        }

        /*
        String temp2 = reverse(temp1);
        for(int x=0;x<temp2.length();x++)
        {
            key2.add(temp2.charAt(x));
        }
        */


        String temp2 = temp1;
        key2.add(temp2.charAt(temp2.length()-1));
        for(int x=1;x<temp2.length()-1;x++)
        {
            key2.add(temp2.charAt(x));
        }


        for(int x = 0; x< key1.size(); x++)
        {
            if(Collections.frequency(key1, key1.get(x))!=1)
            {
                System.out.println(key1.get(x));
                throw new CloneNotSupportedException();
            }
        }

        for(int x = 0; x< key2.size(); x++)
        {
            if(Collections.frequency(key2, key2.get(x))!=1)
            {
                System.out.println(key2.get(x));
                throw new CloneNotSupportedException();
            }
        }

        //System.out.println(key1.toString());
        System.out.println(key2.toString());



        //System.out.println(list1.toString());
        //System.out.println(list2.toString());


        encodedMessage = "No message has been encoded yet";
        decodedMessage = "No message has been decoded yet";
    }

    public String Encode(String text)
    {
        char[] message = text.toLowerCase().toCharArray();
        int CurrentPosition=0;
        int NextPosition=0;
        encodedMessage = "";
        for(int x=0;x<message.length;x++)
        {
            if(getIndexOf(message[x],1)!=-1)
            {
                if (x % 2 == 0)
                {
                    NextPosition = getIndexOf(message[x],1);
                    if (NextPosition < CurrentPosition) {
                        encodedMessage += ("" + ((key1.size() - CurrentPosition) + NextPosition) + ".");
                    } else {
                        encodedMessage +=("" + (NextPosition-CurrentPosition) + ".");
                    }
                    CurrentPosition = NextPosition;

                } else
                {
                    NextPosition = getIndexOf(message[x],2);
                    if (NextPosition < CurrentPosition) {
                        encodedMessage += ("" + ((key2.size() - CurrentPosition) + NextPosition) + ".");
                    } else {
                        encodedMessage +=("" + (NextPosition-CurrentPosition) + ".");
                    }
                    CurrentPosition = NextPosition;
                }
            }else
            {
                System.out.println(message[x]+" at location "+x);
                throw new IllegalArgumentException();
            }

        }

        return encodedMessage;
    }

    public String Decode(String text)
    {
        String[] message = text.split("\\.");
        System.out.println(Arrays.toString(message));
        //System.out.println(Arrays.toString(message));

        int CurrentPosition = 0;
        int NextPosition = 0;
        decodedMessage = "";
        for(int x=0;x<message.length;x++)
        {
            if (Integer.parseInt(message[x])!=-1)
            {
                if(x % 2 == 0)
                {
                    NextPosition = Integer.parseInt(message[x]);
                    decodedMessage += getValueOf((NextPosition+CurrentPosition)% key1.size(),1);
                    CurrentPosition = (NextPosition+CurrentPosition)% key1.size();
                }else
                {

                    NextPosition = Integer.parseInt(message[x]);
                    decodedMessage += getValueOf((NextPosition+CurrentPosition)% key2.size(),2);
                    CurrentPosition = (NextPosition+CurrentPosition)% key2.size();
                }
            }else
            {
                System.out.println(message[x]+" at location "+x+"  "+message.length);
                System.out.println(CurrentPosition+"  "+NextPosition);
                System.out.println(decodedMessage);
                throw new IllegalArgumentException();
            }
        }


        return decodedMessage;
    }




    private String reverse(String l)
    {
        StringBuilder temp = new StringBuilder();
        for(int x=l.length()-1;x>0;x--)
        {
            temp.append(l.charAt(x));
        }


        return temp.toString();
    }


    private int getIndexOf(Character letter, int list)
    {
        if(list==1)
        {
            for(int x = 0; x< key1.size(); x++){
                if(key1.get(x)==letter)
                {
                    return x;
                }
            }
            return -1;
        }else if(list==2)
        {
            for(int x = 0; x< key2.size(); x++){
                if(key2.get(x)==letter)
                {
                    return x;
                }
            }
            return -1;
        }else
        {
            return -1;
        }

    }

    private Character getValueOf(int index, int list) {
        if (list == 1)
        {
            return key1.get(index);
        }else if (list==2)
        {
            return key2.get(index);
        }else
        {
            return '*';
        }
    }
}
