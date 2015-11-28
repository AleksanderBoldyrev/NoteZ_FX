package Client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

import Main.*;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static java.lang.Thread.sleep;

/**
 * Created by Sasha on 30.09.2015.
 */
public class Client extends Application{

    public final SocketListener _listener = new SocketListener(CommonData.PORT, CommonData.HOST);
    public final RequestsParser _parser = new RequestsParser();
    public boolean isAuth;

    /* FX Elements */

    public TextField serverName;
    public TextField userName;
    public PasswordField password;

    /* =========== */

    /*public Client(int port, String host) {

        _listener = new SocketListener(port, host);
        _parser = new RequestsParser();
        isAuth = false;
    }*/

    public void startProcess() {

        Application.launch();

        //CreateUser();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        //this._listener = new SocketListener(CommonData.PORT, CommonData.HOST);
        //this._parser = new RequestsParser();
        isAuth = false;

        Thread t = new Thread(_listener);
        t.start();
        Parent root = FXMLLoader.load(getClass().getResource("LoginWindow.fxml"));
        primaryStage.setTitle(CommonData.LOG_W_CAPTION);
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public void stopListener() {
        _listener.StopListener();
    }

    public void Login(String _log, String _pass) {
        ArrayList<String> s = new ArrayList<String>();
        s.add(_log);
        s.add(_pass);
        String st = _parser.Build(s, CommonData.O_LOGIN);
        _listener.WriteToBuffer(st);
        String str = WaitForServer();
        if (!Objects.equals(str, ""))
        {
            ArrayList<Integer> buff = _parser.ParseListOfInteger(str);
            if (buff.size() > 1)
                if (buff.get(0) == CommonData.O_RESPOND)
                {
                    if (buff.get(1) == CommonData.SERV_YES)
                    {
                        isAuth = true;

                    }
                    //else
                    //   _uiLogin.label.showMessage("User name or password is incorrect!");
                }
        }
    }

    public void Logout() {
        String st = _parser.Build("", CommonData.O_LOGOUT);
        _listener.WriteToBuffer(st);
        String str = WaitForServer();
        if (!Objects.equals(str, ""))
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

    public void CreateUser(String _log, String _pass) {
        ArrayList<String> s = new ArrayList<String>();
        s.add(_log);
        s.add(_pass);
        String st = _parser.Build(s, CommonData.O_CREATE_U);
        _listener.WriteToBuffer(st);
        String str = WaitForServer();
        if (!Objects.equals(str, "")) {
            ArrayList<Integer> buff = _parser.ParseListOfInteger(str);
            if (buff.size() > 1)
                if (buff.get(0) == CommonData.O_RESPOND) {
                    if (buff.get(1) == CommonData.SERV_YES) {
                        isAuth = true;
                    }
                }
        }
    }

    public void DeleteUser(int user_id) {
        String st = _parser.Build(user_id, CommonData.O_DELETE_U);
        _listener.WriteToBuffer(st);
        String str = WaitForServer();
        if (!Objects.equals(str, ""))
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

    public void CreateNote(String note) {
        String st = _parser.Build(note, CommonData.O_CREATE_N);
        _listener.WriteToBuffer(st);
        String str = WaitForServer();
        if (!Objects.equals(str, ""))
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
        _listener.WriteToBuffer(st);
        String str = WaitForServer();
        if (!Objects.equals(str, ""))
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
        _listener.WriteToBuffer(st);
        String str = WaitForServer();
        if (!Objects.equals(str, ""))
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
        _listener.WriteToBuffer(st);
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
        _listener.WriteToBuffer(st);
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
        _listener.WriteToBuffer(st);
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
        _listener.WriteToBuffer(st);
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

    public void GetTagList() {

    }

    public void GetNoteByTag() { //

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
            str =  _listener.ReadBuffer();
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
}