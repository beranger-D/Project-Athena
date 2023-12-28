import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

import static javafx.scene.paint.Color.RED;

public class EncodeController implements Initializable {

    //FXML imports
    @FXML
    private TextArea TAPlainText;
    @FXML
    private Button BEncrypt;
    @FXML
    private TextField TFCustomKey;
    @FXML
    private ToggleButton TBCustomKey;
    @FXML
    private TextArea TAEncryptedText;
    @FXML
    private Button BCopy;
    @FXML
    private Label LNoEncryptedText;
    @FXML
    private Label LErrorPlainText;
    @FXML
    private Label LNoCustomKey;
    @FXML
    private Button BCopyKey;
    @FXML
    private ProgressIndicator PIEncoding;
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
    public void initialize(URL location, ResourceBundle resources)
    {

        TBCustomKey.setSelected(false);
        TFCustomKey.setVisible(false);
        TAEncryptedText.setEditable(false);
        LErrorPlainText.setVisible(false);
        LNoCustomKey.setVisible(false);
        LNoEncryptedText.setVisible(false);
        TAPlainText.setWrapText(true);
        TAEncryptedText.setWrapText(true);
        BCopyKey.setVisible(false);

        file = new FileManager();
        refreshKey();

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


    public void EncryptPlainText() throws InterruptedException, IOException {
        if((TBCustomKey.isSelected() && !TFCustomKey.getText().isEmpty()) || (!TBCustomKey.isSelected()))
        {
            LNoCustomKey.setVisible(false);
            PIEncoding.setProgress(0);
            if (!TAPlainText.getText().isEmpty()) {
                LNoCustomKey.setText("Error in custom key");
                LNoCustomKey.setTextFill(RED);
                LErrorPlainText.setVisible(false);
                LNoEncryptedText.setVisible(false);

                if (TBCustomKey.isSelected()) {
                    SetKey(TFCustomKey.getText());
                } else {
                    refreshKey();
                    SetKey(defaultKey);
                }

                PlainText = TAPlainText.getText().toLowerCase();
                PlainText = PlainText.replaceAll("”", "\"");
                PlainText = PlainText.replaceAll("“", "\"");
                PlainText = PlainText.replaceAll("’", "'");


                // find shift
                int start = PlainText.indexOf("[");
                int finish = PlainText.indexOf("]");
                if(start==0)
                {
                    shift = Integer.parseInt(PlainText.substring(start+1,finish));
                }else
                {
                    shift=0;
                }
                PlainText = PlainText.substring(finish+1);
                //System.out.println(PlainText);
                //

                // add shift to final message
                EncodedText = "";
                if(CBShift.isSelected())
                {
                    EncodedText+="["+shift+"]";
                }
                //



                int CurrentPosition = 0;
                int NextPosition = 0;
                TAEncryptedText.clear();
                char[] message = PlainText.toLowerCase().toCharArray();
                for (int x = 0; x < message.length; x++)
                {
                    if(message[x]=='\n')
                    {
                        EncodedText+='\n';
                    } else if (getIndexOf(message[x]) != -1)
                    {
                        NextPosition = getIndexOf(message[x]);
                        if (NextPosition < CurrentPosition) {
                            EncodedText += ("" + ((key1.size() - CurrentPosition) + NextPosition) + ".");
                            //TAEncryptedText.appendText(((key1.size() - CurrentPosition) + NextPosition) + ".");
                        } else {
                            EncodedText += ("" + (NextPosition - CurrentPosition) + ".");
                            //TAEncryptedText.appendText((NextPosition - CurrentPosition) + ".");
                        }
                        //System.out.println("shifting "+x);

                        CurrentPosition = getIndexOf(message[x]);
                        ShiftKey();
                    } else {
                        System.out.println(message[x] + " at location " + x);
                        LErrorPlainText.setText("Character  "+message[x]+"   not in key");
                        LErrorPlainText.setVisible(true);
                        break;
                    }
                }
                startTextAnimation();
            } else {
                LErrorPlainText.setVisible(true);
                TAEncryptedText.setText("");
                PIEncoding.setProgress(0);
            }
        }else
        {
            LNoCustomKey.setVisible(true);
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
        TFCustomKey.setPromptText(defaultKey);
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
        if(!TAEncryptedText.getText().isEmpty())
        {
            LNoEncryptedText.setVisible(false);
            StringSelection stringSelection = new StringSelection(EncodedText);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }else
        {
            LNoEncryptedText.setVisible(true);
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
        currentIndex=0; // Reset index for each button press
        PIEncoding.setProgress(0);
        TAEncryptedText.clear();

         timeline = new Timeline(new KeyFrame(
                Duration.millis(0.4), // Adjust the delay between each letter appearance
                event -> {
                    if (currentIndex < EncodedText.length()) {
                        TAEncryptedText.appendText(String.valueOf(EncodedText.charAt((int)currentIndex)));
                        currentIndex++;

                        double progress = (double) (currentIndex / EncodedText.length());
                        PIEncoding.setProgress(progress);
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
