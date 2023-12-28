import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class DecodeController implements Initializable {
    //FXML imports
    @FXML
    private TextArea TAEncryptedText;
    @FXML
    private Button BDecrypt;
    @FXML
    private TextField TFCustomKey;
    @FXML
    private ToggleButton TBCustomKey;
    @FXML
    private TextArea TAPlainText;
    @FXML
    private Button BCopy;
    @FXML
    private Label LNoPlainText;
    @FXML
    private Label LErrorEncryptedText;
    @FXML
    private Label LNoCustomKey;
    @FXML
    private Button BCopyKey;
    @FXML
    private ProgressIndicator PIDecoding;
    @FXML
    private CheckBox CBShift;


    //Variables
    private String PlainText;
    private String EncodedText;
    private ArrayList<Character> key1;
    private FileManager file;
    private String defaultKey;
    private double currentIndex;
    private Timeline timeline;
    private int shift;
    private boolean first;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TBCustomKey.setSelected(false);
        TFCustomKey.setVisible(false);
        TAPlainText.setEditable(false);
        LErrorEncryptedText.setVisible(false);
        LNoCustomKey.setVisible(false);
        LNoPlainText.setVisible(false);
        TAPlainText.setWrapText(true);
        TAEncryptedText.setWrapText(true);
        BCopyKey.setVisible(false);

        file = new FileManager();
        refreshKey();
        TFCustomKey.setPromptText(defaultKey);

        PlainText = "";
        EncodedText = "";

        currentIndex = 0;

        shift = 0;
        first=true;
    }

    private void refreshKey()
    {
        defaultKey = file.getKey();
        SetKey(defaultKey);
    }

    public void setKey()
    {
        TFCustomKey.setPromptText(defaultKey);
        if (!TFCustomKey.getText().isEmpty()) {
            SetKey(TFCustomKey.getText());
        } else {
            refreshKey();
            SetKey(defaultKey);
        }
    }

    public void DecryptEncryptedText() throws InterruptedException {
        if((TBCustomKey.isSelected() && !TFCustomKey.getText().isEmpty()) || (!TBCustomKey.isSelected()))
        {
            LNoCustomKey.setVisible(false);
            PIDecoding.setProgress(0);
            if (!TAEncryptedText.getText().isEmpty())
            {

                LErrorEncryptedText.setVisible(false);
                EncodedText = TAEncryptedText.getText();


                if (TBCustomKey.isSelected()) {
                    SetKey(TFCustomKey.getText());
                } else {
                    refreshKey();
                    SetKey(defaultKey);
                }


                // find shift
                int start = EncodedText.indexOf("[");
                int finish = EncodedText.indexOf("]");
                if(start==0)
                {
                    shift = Integer.parseInt(EncodedText.substring(start+1,finish));
                }else
                {
                    shift=0;
                }
                EncodedText = EncodedText.substring(finish+1);
                //System.out.println(EncodedText);
                //

                // add shift to final message
                PlainText = "";
                if(CBShift.isSelected())
                {
                    PlainText+="["+shift+"]";
                }
                //




                int CurrentPosition = 0;
                int NextPosition = 0;
                TAPlainText.clear();
                String[] encoded = EncodedText.split("\n");
                int test = 0;
                for(int y=0;y<encoded.length;y++)
                {
                    String[] message = encoded[y].split("\\.");
                    for (int x = 0; x < message.length; x++) {

                        if (Integer.parseInt(message[x]) != -1) {
                            NextPosition = Integer.parseInt(message[x]);
                            int t = (NextPosition + CurrentPosition) % key1.size();

                            PlainText += getValueOf(t);
                            CurrentPosition = t;
                            ShiftKey();
                        } else {
                            throw new IllegalArgumentException();
                        }
                        test++;

                    }
                    PlainText+="\n";
                }
                startTextAnimation();

            }else {
                LErrorEncryptedText.setVisible(true);
                TAPlainText.setText("");
                PIDecoding.setProgress(0);
            }
        }else
        {
            LNoCustomKey.setVisible(true);
        }
    }


    private Character getValueOf(int index)
    {
        return key1.get(index);
    }

    private void ShiftKey()
    {
        for(int x=0;x<shift;x++)
        {
            if (key1.size() % 2 == 0) {
                int middle = key1.size() / 2;
                if (first) {
                    char temp = key1.get(key1.size() - 1);
                    key1.add(middle - 1, temp);
                    key1.remove(key1.size() - 1);
                    first = false;
                } else {
                    char temp = key1.get(0);
                    key1.add(middle, temp);
                    key1.remove(0);
                    first = true;
                }
            } else {
                if (first) {
                    char temp = key1.get(key1.size() - 1);
                    key1.remove(key1.size() - 1);
                    int middle = key1.size() / 2;
                    key1.add(middle - 1, temp);
                    first = false;
                } else {
                    char temp = key1.get(0);
                    key1.remove(0);
                    int middle = key1.size() / 2;
                    key1.add(middle, temp);
                    first = true;
                }
            }
        }
        //System.out.println(key1.toString());
    }

    public void TBPressed()
    {
        if(TBCustomKey.isSelected())
        {
            TFCustomKey.setVisible(true);
            BCopyKey.setVisible(true);
        }else {
            TFCustomKey.setVisible(false);
            BCopyKey.setVisible(false);
        }
    }

    private void SetKey(String key)
    {
        LNoCustomKey.setVisible(false);
        key1 = new ArrayList<>();
        String temp = key;
        for(int x=0;x<temp.length();x++)
        {
            key1.add(temp.charAt(x));
        }

        for(int x = 0; x< key1.size(); x++)
        {
            if(Collections.frequency(key1, key1.get(x))!=1)
            {
                System.out.println(key1.get(x));
                try {
                    LNoCustomKey.setVisible(true);
                    throw new CloneNotSupportedException();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void CopyText()
    {
        if(!TAPlainText.getText().isEmpty())
        {
            LNoPlainText.setVisible(false);
            StringSelection stringSelection = new StringSelection(PlainText);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }else
        {
            LNoPlainText.setVisible(true);
        }
    }

    public void CopyKey()
    {
        if(!TFCustomKey.getText().isEmpty())
        {
            LNoCustomKey.setVisible(false);
            StringSelection stringSelection = new StringSelection(TFCustomKey.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }else
        {
            LNoCustomKey.setVisible(true);
        }
    }

    private void startTextAnimation() {
        currentIndex = 0; // Reset index for each button press
        PIDecoding.setProgress(0);
        TAPlainText.clear();
        timeline = new Timeline(new KeyFrame(
                Duration.millis((0.6)), // Adjust the delay between each letter appearance
                event -> {
                    if (currentIndex < PlainText.length()) {
                        TAPlainText.appendText(String.valueOf(PlainText.charAt((int)currentIndex)));
                        currentIndex++;

                        double progress = (double) (currentIndex / PlainText.length());
                        PIDecoding.setProgress(progress);
                    }else
                    {
                        timeline.stop();
                    }
                }
        ));

        // Set the number of cycles based on the length of the text
        timeline.setCycleCount(EncodedText.length());
        timeline.play();
    }
}
