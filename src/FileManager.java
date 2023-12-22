import javafx.fxml.Initializable;

import java.io.*;

public class FileManager implements Serializable{
    private static String defaultKey;

    public FileManager()
    {
        defaultKey = ReadKey();
    }

    public static void SetKey(String newKey) throws IOException {
        defaultKey = newKey;
        KeyToDisk();
    }

    public static String getKey()
    {
        return defaultKey;
    }

    public void saveKey() throws IOException {
        KeyToDisk();
    }


    public static void KeyToDisk() throws IOException {
        File keyfile = new File("Key.bin");
        FileOutputStream fileoutput = new FileOutputStream("Key.bin");
        ObjectOutputStream out = new ObjectOutputStream(fileoutput);
        out.writeObject(defaultKey);
        out.close();
        fileoutput.close();
        System.out.println("Key saved to file");
    }

    public static String ReadKey()
    {
        try
        {
            String temp;
            FileInputStream fileinput = new FileInputStream("Key.bin");
            ObjectInputStream objectinput = new ObjectInputStream(fileinput);
            temp = (String) objectinput.readObject();
            objectinput.close();
            fileinput.close();
            return temp;
        }catch(IOException | ClassNotFoundException e)
        {
            System.out.println("no file");
            return "abcdefghijklmnopqrstuvwxyz., ?:;\'\"%!=+-*/\\(){}1234567890";
        }
    }
}
