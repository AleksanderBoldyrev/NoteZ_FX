package Server;

import Main.Note;
import Main.Tag;

import java.util.ArrayList;

/**
 * Created by Sasha on 08.10.2015.
 */
public class SecurityHelper {

    private static BaseWorker _dataBase;
    private static ArrayList<Integer> _activeUsers;

    SecurityHelper() {
        _dataBase = new BaseWorker();
        _activeUsers = new ArrayList<Integer>();
    }

    public synchronized int Login(String name, String pass) {
        int res = _dataBase.CheckUser(name, pass);  //*** NARROW!. ***
        if (res >= 0) {
            if (_activeUsers.contains(res))
                return -1;
            else {
                _activeUsers.add(res);
                return res;
            }
        }
        return -1;
    }

    public synchronized boolean Logout(int userId) {
        if (_activeUsers.contains(userId))
            for (int i = 0; i < _activeUsers.size(); i++) {
                if (_activeUsers.get(i) == userId)
                    _activeUsers.remove(i);
                    return true;
            }
        return false;
    }

    public synchronized  int GetNotesCount(int userId) {
        if (_activeUsers.contains(userId)) return _dataBase.GetNotesCountByUser(userId);
        return 0;
    }

    public synchronized  ArrayList<Integer> GetNotesListByUserId(int userId) {
        ArrayList<Integer> res = new ArrayList<Integer>();
        if (_activeUsers.contains(userId))
            return _dataBase.GetNotesByUserId(userId);
        return res;
    }

    public synchronized String GetNoteVersionById(int userId, int noteId, int verId) {
        String res = "";
        if (_activeUsers.contains(userId)) {
            return _dataBase.GetNoteVerById(userId, noteId, verId);
        }
        return res;
    }

    public synchronized String GetNoteVersionDateById(int userId, int noteId, int verId) {
        String res = "";
        if (_activeUsers.contains(userId)) {
            return _dataBase.GetNoteVerDateById(userId, noteId, verId);
        }
        return res;
    }

    public synchronized ArrayList<String> GetNotesTitlesById(int userId) {
        ArrayList<String> res = new ArrayList<String>();
        if (_activeUsers.contains(userId))
            return _dataBase.GetNotesTitles(userId);
        return res;
    }

    public synchronized int GetNoteVersCount(int userId, int noteId) {//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        int res = 0;
        if (_activeUsers.contains(userId))
            return _dataBase.GetNoteVersCount(userId,noteId);
        return 0;

    }

    public synchronized ArrayList<String> GetNoteVersionsListById(int userId, int noteId) {
        ArrayList<String> res = new ArrayList<String>();

        if (_activeUsers.contains(userId)){
            ArrayList<Integer> arr = _dataBase.GetNotesByUserId(userId);
            boolean flag = false;
            for (int i = 0; i<arr.size(); i++) {
                if (arr.get(i)==noteId){
                    res =  _dataBase.GetNoteVersionsDatesById(noteId);
                }
            }
        }

        return res;
    }

    public synchronized boolean CreateNote(int userId, String data, String title) { //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if (_activeUsers.contains(userId)) {
            _dataBase.AddNote(userId, data, title);
            return true;
        }
        return false;
    }

    public synchronized void DeleteNote(int noteId, int userId) { //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if (_activeUsers.contains(userId))
            if (HaveUserNote(userId, noteId))
                _dataBase.DeleteNote(noteId, userId);
    }

    public synchronized void GetUserNoteHeaderList() {

    }

    public synchronized boolean CreateUser(String name, String pass) { //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        boolean res = _dataBase.DoesUserExist(name);  //*** NARROW!. ***
        if (!res)
            _dataBase.AddUser(name, pass);
        return !res;
    }

    public synchronized boolean DeleteUser(int userId) {//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if (_activeUsers.contains(userId)) {
            _dataBase.DeleteUser(userId);
            return true;
        }

        return false;
    }

    public synchronized void AddTagToNote(String t) {
        _dataBase.AddTag(t);
    }

    public synchronized void ChangeUser() {

    }

    public void DeleteTagFromNote() {

    }

    /*public void GetNoteListByTag() {

    }*/

    public ArrayList<Tag> GetTagList(final int userId) {
        ArrayList<Tag> art = new ArrayList<Tag>();
        if (_activeUsers.contains(userId)) {
            return _dataBase.GetTagList();
        }
        return art;
    }
    public boolean SetTagList(int userId, ArrayList<Tag> art) {
        if (_activeUsers.contains(userId)) {
            _dataBase.SetTagList(art);
        }
        return true;
    }

    public void GetTitleNoteList() {

    }

    public boolean HaveUserNote(final int userId, final int noteId) {
        ArrayList<Integer> buff = _dataBase.GetNotesByUserId(userId);
        for (Integer aBuff : buff)
            if (aBuff == noteId)
                return true;
        return false;
    }

}
