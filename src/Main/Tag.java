package Main;

import java.util.ArrayList;

/**
 * Created by Alex on 19.10.2015.
 */
public class Tag {
    /*
        private ArrayList <String> Tag;
        private class TagType {
            int id;
            String data;
        }

        public void LoadTagsFromBase() {

        }

        public void SaveInBase() {

        }

        public void DeleteTag() {

        }

        public void AddTag() {

        }

        public void DeleteUnusedTags() {

        }
    */
    private int id;
    private String strData;

    public Tag (int _id, String _str) {
        id = _id;
        strData = _str;
    }

    public int GetId() {
        return this.id;
    }

    public String GetStrData() {
        return this.strData;
    }

}
