import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Duration;


import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;


public class KeyTabController implements Initializable {

    @FXML
    private TextField TFCurrentDefaultKey;
    @FXML
    private Button BCopyDefaultKey;
    @FXML
    private Button BRefresh;

    @FXML
    private TextField TFNewDefaultKey;
    @FXML
    private Button BSetNewDefaultKey;
    @FXML
    private Label LNewDefaultKey;

    @FXML
    private Button BGenerateCustomKey;
    @FXML
    private CheckBox CBShuffleCustomKey;
    @FXML
    private TextField TFCustomKey;
    @FXML
    private TextField TFRemovedCharacter;
    @FXML
    private TextField TFAddCharacter;
    @FXML
    private Button BCopyCustomKey;

    @FXML
    private TextArea TAPlainText;
    @FXML
    private Button BGenerateCustomFitKey;
    @FXML
    private TextField TFCustomFitKey;
    @FXML
    private Button BCopyCustomFitKey;
    @FXML
    private CheckBox CBShuffleCustomFitKey;


    private String Key;
    private FileManager file;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshKey();

        LNewDefaultKey.setVisible(false);
        TFCustomKey.setText(Key);

    }

    public void refreshKey()
    {
        Key = FileManager.ReadKey();
        TFCurrentDefaultKey.setText(Key);
    }



    public void CopyDefaultKey()
    {
        StringSelection stringSelection = new StringSelection(Key);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    public void SetNewDefaultKey() throws IOException
    {
        if(!TFNewDefaultKey.getText().isEmpty())
        {

            //LNewDefaultKey.setVisible(true);
            if(CheckNewKey())
            {
                FileManager.SetKey(TFNewDefaultKey.getText());
                TFNewDefaultKey.setText("");
                LNewDefaultKey.setVisible(false);
                refreshKey();
            }else
            {
                LNewDefaultKey.setVisible(true);
            }
        }
    }

    public boolean CheckNewKey()
    {
        ArrayList<Character> key1 = new ArrayList<>();
        String temp = TFNewDefaultKey.getText().toLowerCase();
        for(int x=0;x<temp.length();x++)
        {
            key1.add(temp.charAt(x));
        }

        for(int x = 0; x< key1.size(); x++)
        {
            if (Collections.frequency(key1, key1.get(x)) != 1)
            {
                System.out.println(key1.get(x));
                return false;

            }
        }
        return true;
    }

    public void CustomKey()
    {
        char[] remove = TFRemovedCharacter.getText().toLowerCase().toCharArray();
        char[] add = TFAddCharacter.getText().toLowerCase().toCharArray();

        ArrayList<Character> key1 = new ArrayList<>();
        String temp = Key;
        for(int x=0;x<temp.length();x++)
        {
            key1.add(temp.charAt(x));
        }

        if(remove.length!=0)
        {
            for (char c : remove) {
                key1.remove((Character) c);
            }
        }

        if(add.length!=0)
        {
            for (char c : add) {
                if (!key1.contains(c)) {
                    key1.add(c);
                }
            }
        }



        if(!CBShuffleCustomKey.isSelected())
        {
            String output = "";
            for (Character character : key1) {
                output += character;
                TFCustomKey.setText(output);
            }
        }else
        {
            Collections.shuffle(key1);
            String output = "";
            for (Character character : key1) {
                output += character;
                TFCustomKey.setText(output);
            }
        }

    }


    public void CopyCustomKey()
    {
        if(!TFCustomKey.getText().isEmpty())
        {
            StringSelection stringSelection = new StringSelection(TFCustomKey.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }
    }


    public void GenerateCustomFitKey()
    {
        if(!TAPlainText.getText().isEmpty())
        {
            String text = TAPlainText.getText();
            String key = "";
            text = text.toLowerCase();
            while(!text.isEmpty())
            {
                char temp = text.charAt(0);
                text = text.replaceAll(String.valueOf(temp), "");
                key+=temp;
            }
            startTextAnimation(key);
        }else
        {
            TFCustomFitKey.clear();
        }
    }


    public void CopyCustomFitKey()
    {
        if(!TFCustomFitKey.getText().isEmpty())
        {
            StringSelection stringSelection = new StringSelection(TFCustomFitKey.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }
    }

    private void startTextAnimation(String key) {
        AtomicInteger currentIndex = new AtomicInteger(); // Reset index for each button press
        TFCustomFitKey.clear();

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(15), // Adjust the delay between each letter appearance
                event -> {
                    if (currentIndex.get() < key.length()) {
                        TFCustomFitKey.appendText(String.valueOf(key.charAt(currentIndex.get())));
                        currentIndex.getAndIncrement();
                    }
                }
        ));

        // Set the number of cycles based on the length of the text
        timeline.setCycleCount(key.length());
        timeline.play();
    }

}
