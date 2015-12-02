package Main;

/**
 * Created by Alex on 04.11.2015.
 */
public final class CommonData {
    public static final int PORT = 36000;
    public static final String HOST = "localhost";
    public static final char SEP = '|';
    public static final char SEPID = '.';
    public static final String TERMCOMMAND = "***";
    public static final int SLEEP_TIME = 50;
    public static final int RETRIES_COUNT = 200;
    public static final int SERV_YES = 1;
    public static final int SERV_NO = 0;

    public static final String LOG_W_CAPTION = "Welcome to NoteZ app";
    public static final String MAIN_W_CAPTION = "NoteZ app";

    /*Client - server commands*/
    public static final int O_IS_SERVER_ALIVE = 1111;
    public static final int O_RESPOND = 0;
    public static final int O_LOGIN = 1;
    public static final int O_CREATE_U = 2;
    public static final int O_DELETE_U = 3;
    public static final int O_LOGOUT = 4;
    public static final int O_CREATE_N = 5;
    public static final int O_DELETE_N = 6;
    public static final int O_CREATE_T = 7;
    public static final int O_DELETE_T = 8;
    public static final int O_SAVE_N = 9;
    public static final int O_DELETE_N_V = 10;
    public static final int O_SEARCH_N = 11;
    public static final int O_GETCAPTIONS = 12;
    public static final int O_GETTAGS = 13;
    public static final int O_SETTAGS = 14;
    public static final int O_GETNOTEIDS = 15;
    public static final int O_GETNOTEPRIM = 16;
    public static final int O_GETVERSDATE = 17;
    public static final int O_SETNOTEIDS = 19;
    public static final int O_SETNOTEPRIM = 20;

}
