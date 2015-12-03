package Client;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import Main.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import static java.lang.Thread.sleep;

/**
 * Created by Sasha on 30.09.2015.
 */
public class Client extends Application {


    public static final RequestsParser _parser = new RequestsParser();
    public static boolean isAuth;
    public static Stage _mainStage;
    public static Scene _mainScene;
    public static Parent _lNode;
    public static Parent _mNode;

    private static Socket _sock;
    private static BufferedReader _in;
    private static PrintWriter _out;

    private static boolean termFlag;
    private static int _stage;                           //0 - Notes captions view, 1 - List of versions view

    /* FX Elements */

    public TextField serverName;
    public TextField userName;
    public PasswordField password;
    public TextField tagList;
    public TextField noteCaption;
    public TextArea noteData;
    public ListView listView;
    public Button saveButton;
    public Button undoButton;
    public Button closeButton;
    public Button openButton;
    /* =========== */

    public static ArrayList<Integer> noteIds;       //All user's notes
    public static ArrayList<String> noteCaptions;   //All user's notes captions
    public static ArrayList<Tag> tagsList;          //All tags list
    public static String login;
    public static String pass;
    public static ArrayList<String> versDate;              //List of note's versions
    public static NotePrimitive curVers;                   //Store current edition version of Note
    public static String undoBuff;                         //Buffer which stores last save version of note's text
    public static String currentCaption;                   //Caption of current note
    public static ArrayList<Integer> curNoteTags;          //current note tags


    public void startProcess() {
        /*Thread t = new Thread(_listener);
        t.setDaemon(true);
        t.start();*/

       try {
            _sock = new Socket(CommonData.HOST, CommonData.PORT);
            _in = new BufferedReader(new InputStreamReader(_sock.getInputStream()));
            _out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(_sock.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        termFlag = false;

        Application.launch();

        //CreateUser();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        isAuth = false;
        _mainStage = new Stage();

        _lNode = FXMLLoader.load(getClass().getResource("LoginWindow.fxml"));
        _mNode = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        _mainStage.setTitle(CommonData.LOG_W_CAPTION);
        _mainScene = new Scene(_lNode, 600, 400);
        _mainStage.setScene(_mainScene);
        _mainStage.show();
    }

    public void Login(String _log, String _pass) {
        ArrayList<String> s = new ArrayList<String>();
        s.add(_log);
        s.add(_pass);

        String st = _parser.Build(s, CommonData.O_LOGIN);
        SendToServer(st);
        String str = WaitForServer();

        if (!str.equals(""))
        {
            ArrayList<Integer> buff = _parser.ParseListOfInteger(str);
            if (buff.size() > 1)
                if (buff.get(0) == CommonData.O_RESPOND)
                {
                    if (buff.get(1) == CommonData.SERV_YES)
                    {
                        login = _log;
                        pass = _pass;
                        isAuth = true;
                        _stage = 0;
                        LoadBasicDataFromServer();
                        _mainStage.setTitle(CommonData.MAIN_W_CAPTION);
                        _mainScene = new Scene(_mNode, 600, 400);
                        _mainStage.setScene(_mainScene);
                        _mainStage.show();
                    }
                    //else
                    //   _uiLogin.label.showMessage("User name or password is incorrect!");
                }
        }
    }

    public void Logout() {
        String st = _parser.Build("", CommonData.O_LOGOUT);
        SendToServer(st);
        String str = WaitForServer();
        if (!str.equals(""))
        {
            ArrayList<Integer> buff = _parser.ParseListOfInteger(str);
            if (buff.size() > 1)
                if (buff.get(0) == CommonData.O_RESPOND)
                {
                    if (buff.get(1) == CommonData.SERV_YES)
                    {
                        isAuth = false;
                        _mainStage.setTitle(CommonData.LOG_W_CAPTION);
                        _mainScene = new Scene(_mNode, 600, 400);
                        _mainStage.setScene(_mainScene);
                        _mainStage.show();
                    }
                }
        }
    }

    public void LoadBasicDataFromServer(){
        GetCaptions();
        GetTags();
    }


    public void CreateUser(String _log, String _pass) {
        ArrayList<String> s = new ArrayList<String>();
        s.add(_log);
        boolean add = s.add(_pass);
        String st = _parser.Build(s, CommonData.O_CREATE_U);
        SendToServer(st);
        String str = WaitForServer();
        if (!str.equals("")) {
            ArrayList<Integer> buff = _parser.ParseListOfInteger(str);
            if (buff.size() > 1)
                if (buff.get(0) == CommonData.O_RESPOND) {
                    if (buff.get(1) == CommonData.SERV_YES) {
                        isAuth = true;
                        Login(_log, _pass);
                    }
                }
        }
    }

    public void DeleteUser(int user_id) {
        String st = _parser.Build(user_id, CommonData.O_DELETE_U);
        SendToServer(st);
        String str = WaitForServer();
        if (!str.equals(""))
        {
            ArrayList<Integer> buff = _parser.ParseListOfInteger(str);
            if (buff.size() > 1)
                if (buff.get(0) == CommonData.O_RESPOND)
                {
                    if (buff.get(1) == CommonData.SERV_YES)
                    {
                        isAuth = false;
                        _mainStage.setTitle(CommonData.LOG_W_CAPTION);
                        _mainScene = new Scene(_mNode, 600, 400);
                        _mainStage.setScene(_mainScene);
                        _mainStage.show();
                    }
                }
        }
    }

    public void CreateNote(String note) {
        String st = _parser.Build(note, CommonData.O_CREATE_N);
        SendToServer(st);
        String str = WaitForServer();
        if (!str.equals(""))
        {
            ArrayList<Integer> buff = _parser.ParseListOfInteger(str);
            if (buff.size() > 1)
                if (buff.get(0) == CommonData.O_RESPOND)
                {
                    if (buff.get(1) == CommonData.SERV_YES)
                    {
                        isAuth = true;
                    }
                }
        }
    }

    public void DeleteNote(int note) {
        String st = _parser.Build(note, CommonData.O_DELETE_N);
        SendToServer(st);
        String str = WaitForServer();
        if (!str.equals(""))
        {
            ArrayList<Integer> buff = _parser.ParseListOfInteger(str);
            if (buff.size() > 1)
                if (buff.get(0) == CommonData.O_RESPOND)
                {
                    if (buff.get(1) == CommonData.SERV_YES)
                    {
                        isAuth = true;
                    }
                }
        }
    }

    public void CreateTag(String tag) {
        String st = _parser.Build(tag, CommonData.O_CREATE_T);
        SendToServer(st);
        String str = WaitForServer();
        if (!str.equals(""))
        {
            ArrayList<Integer> buff = _parser.ParseListOfInteger(str);
            if (buff.size() > 1)
                if (buff.get(0) == CommonData.O_RESPOND)
                {
                    if (buff.get(1) == CommonData.SERV_YES)
                    {
                        isAuth = true;
                    }
                }
        }
    }

    public void DeleteTag(String tag) {
        String st = _parser.Build(tag, CommonData.O_DELETE_T);
        SendToServer(st);
        String str = WaitForServer();
        if (!str.isEmpty())
        {
            ArrayList<Integer> buff = _parser.ParseListOfInteger(str);
            if (buff.size() > 1)
                if (buff.get(0) == CommonData.O_RESPOND)
                {
                    if (buff.get(1) == CommonData.SERV_YES)
                    {
                        isAuth = true;
                    }
                }
        }
    }

    public void SaveNote(int note_id) {
        String st = _parser.Build(note_id, CommonData.O_SAVE_N);
        SendToServer(st);
        String str = WaitForServer();
        if (!str.isEmpty())
        {
            ArrayList<Integer> buff = _parser.ParseListOfInteger(str);
            if (buff.size() > 1)
                if (buff.get(0) == CommonData.O_RESPOND)
                {
                    if (buff.get(1) == CommonData.SERV_YES)
                    {
                        isAuth = true;
                    }
                }
        }
    }

    public void DeleteNoteByVersion(int ver) {
        String st = _parser.Build(ver, CommonData.O_DELETE_N_V);
        SendToServer(st);
        String str = WaitForServer();
        if (!str.isEmpty())
        {
            ArrayList<Integer> buff = _parser.ParseListOfInteger(str);
            if (buff.size() > 1)
                if (buff.get(0) == CommonData.O_RESPOND)
                {
                    if (buff.get(1) == CommonData.SERV_YES)
                    {
                        isAuth = true;
                    }
                }
        }
    }

    public void SearchNotes(String title) {
        ArrayList<String> s = new ArrayList<String>();
        s.add(title);
        String st = _parser.Build(s, CommonData.O_SEARCH_N);
        SendToServer(st);
        String str = WaitForServer();
        if (!str.isEmpty())
        {
            ArrayList<Integer> buff = _parser.ParseListOfInteger(str);
            if (buff.size() > 1)
                if (buff.get(0) == CommonData.O_RESPOND)
                {
                    if (buff.get(1) == CommonData.SERV_YES)
                    {
                        isAuth = true;
                    }
                }
        }
    }

    public void GetTags() {
        ArrayList<String> s = new ArrayList<String>();
        String st = _parser.Build(s, CommonData.O_GETTAGS);
        SendToServer(st);
        String str = WaitForServer();
        if (!str.isEmpty())
        {
            ArrayList<String> buff = _parser.ParseListOfString(str);
            if ((buff.size() > 2) && (buff.size() % 2 == 0))
                if (Integer.parseInt(buff.get(0)) == CommonData.O_RESPOND)
                {
                    if (Integer.parseInt(buff.get(1)) == CommonData.SERV_YES)
                    {
                        ArrayList<String> foo = buff;
                        foo.remove(0);
                        foo.remove(0);
                        int tagId=0;
                        String tagData = new String();
                        for (int i =0; i<(foo.size()/2); i++) {
                            tagId = Integer.parseInt(foo.get(i*2));
                            tagData = foo.get(i*2+1);
                        }

                        tagsList.add(new Tag(tagId, tagData));
                    }
                }
        }
    }

    public void GetNoteIds() {
        ArrayList<String> s = new ArrayList<String>();
        String st = _parser.Build(s, CommonData.O_GETNOTEIDS);
        SendToServer(st);
        String str = WaitForServer();
        if (!str.isEmpty())
        {
            ArrayList<Integer> buff = _parser.ParseListOfInteger(str);
            if (buff.size() > 2)
                if (buff.get(0) == CommonData.O_RESPOND)
                {
                    if (buff.get(1) == CommonData.SERV_YES)
                    {
                        buff.remove(0);
                        buff.remove(0);
                        for (int i = 2; i < buff.size(); i++) {
                            noteIds.add(buff.get(i));
                        }
                    }
                }
        }
    }

    public void GetNotePrim(int notePrimId) {
        ArrayList<String> s = new ArrayList<String>();
        s.add(notePrimId+"");
        String st = _parser.Build(s, CommonData.O_GETNOTEPRIM);
        SendToServer(st);
        String str = WaitForServer();
        if (!str.isEmpty())
        {
            ArrayList<String> buff = _parser.ParseListOfString(str);
            if (buff.size() > 4)
                if (Integer.parseInt(buff.get(0)) == CommonData.O_RESPOND)
                {
                    if (Integer.parseInt(buff.get(1)) == CommonData.SERV_YES)
                    {
                        int _id = Integer.parseInt(buff.get(2));
                        LocalDateTime _cdate = LocalDateTime.parse(buff.get(3));
                        String _data = buff.get(4);
                        curVers = new NotePrimitive(_id, _cdate, _data);
                    }
                }
        }
    }

    public void GetVersDate(final int noteId) {
        ArrayList<String> s = new ArrayList<String>();
        s.add(noteId+"");
        String st = _parser.Build(s, CommonData.O_GETVERSDATE);
        SendToServer(st);
        String str = WaitForServer();
        if (!str.isEmpty())
        {
            ArrayList<String> buff = _parser.ParseListOfString(str);
            if (buff.size() > 2)
                if (Integer.parseInt(buff.get(0)) == CommonData.O_RESPOND)
                {
                    if (Integer.parseInt(buff.get(1)) == CommonData.SERV_YES)
                    {
                        versDate.clear();
                        for (int i = 0; i<buff.size(); i++)
                            versDate.add(buff.get(i));
                        FillCaptions(versDate);
                    }
                }
        }
    }

    public boolean SetNoteIds() {
        ArrayList<Integer> s = new ArrayList<Integer>();
        for (int i = 0; i < noteIds.size(); i++) {
            s.add(noteIds.get(i));
        }
        String st = _parser.Build(CommonData.O_SETNOTEIDS, s);
        SendToServer(st);
        String str = WaitForServer();
        if (!str.isEmpty())
        {
            ArrayList<Integer> buff = _parser.ParseListOfInteger(str);
            if (buff.size() > 2)
                if (buff.get(0) == CommonData.O_RESPOND)
                {
                    if (buff.get(1) == CommonData.SERV_YES)
                    {
                        return true;
                    }
                }
        }
        return false;
    }

    public boolean SetNotePrim() {
        ArrayList<String> s = new ArrayList<String>();
        s.add(curVers.GetID()+"");
        s.add(curVers.GetCDate().toString()+"");
        s.add(curVers.GetData());
        String st = _parser.Build(s, CommonData.O_SETNOTEPRIM);
        SendToServer(st);
        String str = WaitForServer();
        if (!str.isEmpty())
        {
            ArrayList<Integer> buff = _parser.ParseListOfInteger(str);
            if (buff.size() > 2)
                if (buff.get(0) == CommonData.O_RESPOND)
                {
                    if (buff.get(1) == CommonData.SERV_YES)
                    {
                        return true;
                    }
                }
        }
        return false;
    }

    public boolean SetTags() {
        ArrayList<String> s = new ArrayList<String>();
        if (tagsList.size()>0) {
            for (int i = 0; i < tagsList.size(); i++) {
                s.add(tagsList.get(i).GetId() + "");
                s.add(tagsList.get(i).GetStrData());
            }
            String st = _parser.Build(s, CommonData.O_SETTAGS);
            SendToServer(st);
            String str = WaitForServer();
            if (!str.isEmpty()) {
                ArrayList<Integer> buff = _parser.ParseListOfInteger(str);
                if (buff.size() > 2)
                    if (buff.get(0) == CommonData.O_RESPOND) {
                        if (buff.get(1) == CommonData.SERV_YES) {
                            return true;
                        }
                    }
            }
        }
        return false;
    }

    public void GetCaptions() {
        ArrayList<String> s = new ArrayList<String>();
        String st = _parser.Build(s, CommonData.O_GETCAPTIONS);
        SendToServer(st);
        String str = WaitForServer();
        if (!str.isEmpty())
        {
            ArrayList<String> buff = _parser.ParseListOfString(str);
            if (buff.size() > 2)
                if (Integer.parseInt(buff.get(0)) == CommonData.O_RESPOND)
                {
                    if (Integer.parseInt(buff.get(1)) == CommonData.SERV_YES)
                    {
                        ArrayList<String> caps = buff;
                        caps.remove(0);
                        caps.remove(0);
                        FillCaptions(caps);
                    }
                }
        }
    }

    private void FillCaptions(final ArrayList<String> arr) {
        if (arr.size()>0) {
            ObservableList<String> lw = FXCollections.observableArrayList();
            for (int i = 0; i < arr.size(); i++){
                //listView.setItems();
                lw.add(arr.get(i));
            }
            listView.setItems(lw);
        }
    }

    public void FilterNoteByTag() { //

    }

    /*private String GetData(String s) {
        String res = new String();
        return res;
    }*/

    private String WaitForServer() {
        int i;
        String str = "";
        for (i = CommonData.RETRIES_COUNT; i > 0 ;i--)
        {
            try {
                sleep(CommonData.SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            str =  ReceiveData();
            if (!str.isEmpty())
            {
                break;
            }
        }
        return str;
    }

    public void CloseNoteButtonClicked(Event event) {

    }

    public void LoginButtonClicked(Event event) {

        Login(userName.getText(), password.getText());
    }


    public void CreateUserButtonClicked(Event event) {

        CreateUser(userName.getText(), password.getText());
    }

  /* public void StopListener() {
        //synchronized (termFlag) {
        termFlag = true;
        //}
    }

    public String ReadBuffer() {
        String str = "";
        synchronized (_buffin) {
            if (_buffin.size() > 0) {
                str = _buffin.get(_buffin.size() - 1);
                _buffin.remove(_buffin.size() - 1);
            }
        }
        return str;
    }

    public void WriteToBuffer(String s) {
        String str = "";

    }

    public synchronized void startThread() {
        _running = true;
        _thread = new Thread(this, "Monitor");

        _thread.setDaemon(true);

        _thread.start();
    }

    public synchronized void stopThread() {
        _running = false;
        try {
            _thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
            String str = "";
            while (_running) {
                try {
                    System.out.println("-");
                    str = _in.readLine();
                    if (str.equals(CommonData.TERMCOMMAND))
                        break;

                    synchronized (_buffin) {
                        if (str.length() > 0) {
                            _buffin.add(str);
                            System.out.println("Client received: " + str);
                        }
                    }

                    synchronized (_buffout) {
                        if (_buffout.size() > 0) {
                            _out.println(_buffout.get(_buffout.size() - 1));
                            System.out.println("Client send: " + _buffout.get(_buffout.size() - 1));
                            _buffout.remove(_buffout.size() - 1);
                        }
                    }

                    if (termFlag)
                        break;


                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("closing...");
                    try {
                        _sock.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        _running = false;
    }
*/
    private  void SendToServer(String str) {
        System.out.println("Client send to server:" + str);
        _out.println(str);
    }

    private String ReceiveData() {
        String str = "";
        try {
            str = _in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Client received: " + str);
        return str;
    }

    public void UndoButtonClicked(Event event) {
    }

    public void SaveButtonClicked(Event event) {
    }

    public void OpenButtonClicked(Event event) {
        int pos = listView.getSelectionModel().getSelectedIndex();
        String text = listView.getSelectionModel().getSelectedItem().toString();
        if (_stage == 0) {
            OpenNote(pos, text);
        }
        else if (_stage == 1){
            OpenNoteVersion(pos);
        }
    }

    private void OpenNote(final int pos, final String text){
        GetVersDate(pos);
        currentCaption = text;
    }

    private void OpenNoteVersion(final int pos) {
        GetNotePrim(pos);
        noteCaption.setText(currentCaption);
        noteData.setText(curVers.GetData());
        tagList.setText(curVers.GetCDate().toString());
    }
}