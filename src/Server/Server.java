package Server;

import Main.CommonData;
import Main.RequestsParser;
import Main.Tag;

import java.io.*;
import java.net.InterfaceAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Sasha on 30.09.2015.
 */
public class Server extends Thread{
    //private int _port;
    private Socket _socket;
    private BufferedReader _in;
    private PrintWriter _out;
    private RequestsParser _parser;
    private int _userId;

    private void createNewUser() {

    }

    private void createNewNote() {

    }

    Server(Socket s) {
        _socket = s;
        _parser = new RequestsParser();
        //_parser.SetUserId(-1);
        _userId = -1;
        try {
            _in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            _out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(_socket.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try{
            String str = "";
            String resp = "";
            while (true) {
                str = _in.readLine();
                System.out.println("Server received: "+str);
                if (str.equals(CommonData.TERMCOMMAND))
                    break;
                resp = "";
                //Parsing
                if (str.length()>0) {
                    ArrayList<String> buff = _parser.ParseListOfString(str);

                    try {
                        int command = Integer.parseInt(buff.get(0));
                        switch (command) {
                            case CommonData.O_CREATE_U:
                                resp = CreateUser(buff);
                                break;
                            case CommonData.O_CREATE_N:
                                resp = CreateNote(buff);
                                break;
                            case CommonData.O_CREATE_T:
                                resp = CreateTag(buff);
                                break;
                            case CommonData.O_DELETE_N:
                                resp = DeleteNote(buff);
                                break;
                            case CommonData.O_DELETE_N_V:
                                resp = DeleteNoteByVer(buff);
                                break;
                            case CommonData.O_DELETE_T:
                                resp = DeleteTag(buff);
                                break;
                            case CommonData.O_DELETE_U:
                                resp = DeleteUser(buff);
                                break;
                            case CommonData.O_LOGIN:
                                resp = Login(buff);
                                break;
                            case CommonData.O_LOGOUT:
                                resp = Logout(buff);
                                break;
                            case CommonData.O_SAVE_N:
                                resp = SaveNote(buff);
                                break;
                            case CommonData.O_SEARCH_N:
                                resp = SearchNote(buff);
                                break;
                            case CommonData.O_GETCAPTIONS:
                                resp = GetCaptions(buff);
                                break;
                            case CommonData.O_GETTAGS:
                                resp = GetTags(buff);
                                break;
                            case CommonData.O_SETTAGS:
                                resp = SetTags(buff);
                                break;
                            case CommonData.O_GETNOTEIDS:
                                resp = GetNoteIds(buff);
                                break;
                            case CommonData.O_GETNOTEPRIM:
                                resp = GetNotePrimitive(buff);
                                break;
                            case CommonData.O_GETVERSDATE:
                                resp = GetVersionsDate(buff);
                                break;
                            case CommonData.O_SETNOTEIDS:
                                //resp = SetNotePrimitive(buff);
                                break;
                            case CommonData.O_SETNOTEPRIM:
                                resp = SetNotePrimitive(buff);
                                break;
                        }

                    } catch (NumberFormatException e)
                    {
                        System.out.println(e.toString());
                    }


                }

                if (!resp.equals("")) {
                    _out.println(resp);
                    System.out.println("Server send: "+resp);
                }

                try {
                    this.sleep(CommonData.SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("closing...");
            try {
                _socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String GetNotePrimitive(ArrayList<String> buff) {
        String res = "";
        return res;
    }

    public String GetVersionsDate(ArrayList<String> buff) {
        ArrayList<String> res = new ArrayList<String>();
        if (buff.size()>1) {
            res = ServerDaemon.sHelper.GetNoteVersionsListById(_userId, Integer.parseInt(buff.get(1)));
            res.add(CommonData.SERV_YES + "");
        }
        return _parser.Build(res, CommonData.O_RESPOND);
    }

    /*public String SetNotesIds(ArrayList<String> buff) {
        ArrayList<Integer> res = new ArrayList<Integer>();
        if (buff.size()>1)
        for (int i = 0; i < buff.size(); i++) {
            if (b) id = Integer.parseInt(buff.get(i));
            else {
                str = buff.get(i);
                res.add(new Tag(id, str));
            }
            b = !b;
        }
        b = ServerDaemon.sHelper.SetTagList(_userId, res);
        StringBuilder stb = new StringBuilder();
        stb.append(CommonData.SERV_YES + "");
        return _parser.Build(stb.toString(), CommonData.O_RESPOND);
    }*/

    public String SetNotePrimitive(ArrayList<String> buff) {
        String res = new String();
        return res;
    }

    public String GetNoteIds(ArrayList<String> buff) {
        ArrayList<Integer> res = new ArrayList<Integer>();
        res = ServerDaemon.sHelper.GetNotesListByUserId(_userId);
        res.add(CommonData.SERV_YES);
        return _parser.Build(CommonData.O_RESPOND, res);
    }

    public String GetCaptions(ArrayList<String> buff) {
        ArrayList<String> res = new ArrayList<String>();
        res = ServerDaemon.sHelper.GetNotesTitlesById(_userId);
        res.add(CommonData.SERV_YES + "");
        return _parser.Build(res, CommonData.O_RESPOND);
    }

    public String GetTags(ArrayList<String> buff){
        ArrayList<Tag> res = new ArrayList<Tag>();
        res = ServerDaemon.sHelper.GetTagList(_userId);
        StringBuilder stb = new StringBuilder();
        stb.append(CommonData.SERV_YES + "");
        for (int i = 0; i < res.size(); i++) {
            stb.append(res.get(i).GetId());
            stb.append(res.get(i).GetStrData());
        }
        return _parser.Build(stb.toString(), CommonData.O_RESPOND);
    }

    public String SetTags(ArrayList<String> buff){
        ArrayList<Tag> res = new ArrayList<Tag>();
        boolean b = true;
        int id = 0;
        String str = "";
        for (int i = 0; i < buff.size(); i++) {
            if (b) id = Integer.parseInt(buff.get(i));
            else {
                str = buff.get(i);
                res.add(new Tag(id, str));
            }
            b = !b;
        }
        b = ServerDaemon.sHelper.SetTagList(_userId, res);
        StringBuilder stb = new StringBuilder();
        stb.append(CommonData.SERV_YES + "");
        return _parser.Build(stb.toString(), CommonData.O_RESPOND);
    }

    public String Login(ArrayList<String> buff ) {
        ArrayList<String> res = new ArrayList<String>();

        int id = -1;
        if (buff.size()>2) {
            id = ServerDaemon.sHelper.Login(buff.get(1), buff.get(2));
        }
        if (id>=0) {
            res.add(CommonData.SERV_YES + "");
            _userId=id;
        }
        else
            res.add(CommonData.SERV_NO + "");
        //res.add(id+"");
        return _parser.Build(res, CommonData.O_RESPOND);
    }

    public String Logout(ArrayList<String> buff ) {
        StringBuilder res = new StringBuilder();
        boolean suc = false;
        ArrayList<Integer> ar = new ArrayList<Integer>();
        if (buff.size()>3) {
            suc = ServerDaemon.sHelper.Logout(_userId);
        }
        if (suc)
            res.append(CommonData.SERV_YES);
        else
            res.append(CommonData.SERV_NO);
        return _parser.Build(res.toString(), CommonData.O_RESPOND);
    }

    public String CreateUser(ArrayList<String> buff ) {
        ArrayList<String> res = new ArrayList<String>();

        boolean suc = false;
        if (buff.size()>2) {
            suc = ServerDaemon.sHelper.CreateUser(buff.get(1), buff.get(2));
        }
        if (suc)
            res.add(CommonData.SERV_YES + "");
        else
            res.add(CommonData.SERV_NO + "");
        return _parser.Build(res, CommonData.O_RESPOND);
    }

    public String DeleteUser(ArrayList<String> in) {
        //ArrayList<Integer> res = _parser.ParseListOfInteger(in);
        StringBuilder out = new StringBuilder();
        boolean suc = false;
        if (in.size()>2) {
            suc = ServerDaemon.sHelper.DeleteUser(_userId);
        }
        if (suc) {
            out.append(CommonData.SERV_YES);
            _userId = -1;
        }
        else
            out.append(CommonData.SERV_NO);
        return _parser.Build(out.toString(), CommonData.O_RESPOND);
    }

    public String CreateNote(ArrayList<String> buff ) {   //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        StringBuilder res = new StringBuilder();
        boolean suc = false;
        ArrayList<Integer> ar = new ArrayList<Integer>();
        if (buff.size()>3) {
            suc = ServerDaemon.sHelper.CreateNote(0, buff.get(1), buff.get(2));
        }
        if (suc)
            res.append(CommonData.SERV_YES);
        else
            res.append(CommonData.SERV_NO);
        return _parser.Build(res.toString(), CommonData.O_RESPOND);
    }

    public String DeleteNote(ArrayList<String> buff ) {
        String res = new String();
        return res;
    }

    public String DeleteNoteByVer(ArrayList<String> buff ) {
        String res = new String();
        return res;
    }

    public String ChangeUser(ArrayList<String> buff ) {
        String res = new String();
        return res;
    }

    public String SaveNote(ArrayList<String> buff ) {
        StringBuilder res = new StringBuilder();
        boolean suc = false;
        //ArrayList<Integer> ar = new ArrayList<Integer>();
        if (buff.size()>3) {
            suc = ServerDaemon.sHelper.SaveNote(Integer.parseInt(buff.get(1)));
        }
        if (suc)
            res.append(CommonData.SERV_YES);
        else
            res.append(CommonData.SERV_NO);
        return _parser.Build(res.toString(), CommonData.O_RESPOND);
    }

    public String SearchNote(ArrayList<String> buff ) {
        String res = new String();
        return res;
    }
    /*public String CreateRequest(ArrayList<String> buff ) {
        String res = new String();
        return res;
    }

    public String ChangeRequest(ArrayList<String> buff ) {
        String res = new String();
        return res;
    }

    public String DeleteRequest(ArrayList<String> buff ) {
        String res = new String();
        return res;
    }*/

    public String CreateTag(ArrayList<String> buff ) {
        String res = new String();
        return res;
    }

    public String DeleteTag(ArrayList<String> buff ) {
        String res = new String();
        return res;
    }

    public String AddTagToRequest(ArrayList<String> buff ) {
        String res = new String();
        return res;
    }

    public String GetRequestListByTags(ArrayList<String> buff ) {
        String res = new String();
        return res;
    }

    public String GetTagList(ArrayList<String> buff ) {
        String res = new String();
        return res;
    }

    public String HandleRequest(ArrayList<String> buff ) {
        String res = new String();
        return res;
    }

    public String GetNoteTitleList(ArrayList<String> buff ) {
        String res = new String();
        return res;
    }
}
