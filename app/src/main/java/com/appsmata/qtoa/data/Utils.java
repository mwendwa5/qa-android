package com.appsmata.qtoa.data;

public class Utils {

    //DATABASE NAME
    public static final String DATABASE_NAME = "question2answer";
    public static final int DATABASE_VERSION = 3;

	//DATABASE STRINGS
    public static final String TBL_CATEGORIES = "qa_categories";
    public static final String TBL_POSTS = "qa_posts";
    public static final String TBL_USERS = "qa_users";
	
    public static final String CATEGORYID = "categoryid";
    public static final String PARENTID = "parentid";
    public static final String TITLE = "title";
    public static final String TAGS = "tags";
    public static final String QCOUNT = "qcount";
    public static final String POSITION = "position";
    public static final String CHILDCOUNT = "childcount";
    public static final String CONTENT = "content";
    public static final String BACKPATH = "backpath";
    public static final String ITEMORDER = "itemorder";

    public static final String POSTID = "postid";
    public static final String TYPE = "type";
    public static final String BASETYPE = "basetype";
    public static final String HIDDEN = "hidden";
    public static final String QUEUED = "queued";
    public static final String ACOUNT = "acount";
    public static final String SELCHILDID = "selchildid";
    public static final String CLOSEDBYID = "closedbyid";
    public static final String UPVOTES = "upvotes";
    public static final String DOWNVOTES = "downvotes";
    public static final String NETVOTES = "netvotes";
    public static final String VIEWS = "views";
    public static final String HOTNESS = "hotness";
    public static final String FLAGCOUNT = "flagcount";
    public static final String CREATED = "created";
    public static final String NAME = "name";
    public static final String CATEGORYNAME = "categoryname";
    public static final String CATEGORYBACKPATH = "categorybackpath";
    public static final String CATEGORYIDS = "categoryids";
    public static final String USERVOTE = "uservote";
    public static final String USERFLAG = "userflag";
    public static final String USERFAVORITEQ = "userfavoriteq";
    public static final String USERID = "userid";
    public static final String COOKIEID = "cookieid";
    public static final String CREATEIP = "createip";
    public static final String POINTS = "points";
    public static final String FLAGS = "flags";
    public static final String LEVEL = "level";
    public static final String EMAIL = "email";
    public static final String HANDLE = "handle";
    public static final String AVATARBLOBID = "avatarblobid";
    public static final String AVATARWIDTH = "avatarwidth";
    public static final String AVATARHEIGHT = "avatarheight";
	
	//CREATE CATEGORIES TABLE SQL
    public static final String CREATE_CATEGORIES_TABLE_SQL =
            "CREATE TABLE " + TBL_CATEGORIES + " ("
            + CATEGORYID + " INTEGER PRIMARY KEY, "
            + PARENTID + " TEXT, "
            + TITLE + " TEXT, "
            + TAGS + " TEXT, "
            + QCOUNT + " TEXT, "
            + POSITION + " TEXT ,"
            + CHILDCOUNT + " TEXT ,"
            + CONTENT + " TEXT ,"
            + BACKPATH + " TEXT ,"
            + ITEMORDER + " TEXT);";
			
    //CREATE POSTS TABLE SQL
    public static final String CREATE_POSTS_TABLE_SQL =
            "CREATE TABLE " + TBL_POSTS + " ("
            + POSTID + " INTEGER PRIMARY KEY, "
            + CATEGORYID + " TEXT, "
            + TYPE + " TEXT, "
            + BASETYPE + " TEXT, "
            + HIDDEN + " TEXT, "
            + QUEUED + " TEXT ,"
            + ACOUNT + " TEXT ,"
            + SELCHILDID + " TEXT ,"
            + CLOSEDBYID + " TEXT ,"
            + UPVOTES + " TEXT ,"
            + DOWNVOTES + " TEXT ,"
            + NETVOTES + " TEXT ,"
            + VIEWS + " TEXT ,"
            + HOTNESS + " TEXT ,"
            + FLAGCOUNT + " TEXT ,"
            + TITLE + " TEXT ,"
            + TAGS + " TEXT ,"
            + CREATED + " TEXT ,"
            + NAME + " TEXT ,"
            + CATEGORYNAME + " TEXT ,"
            + CATEGORYBACKPATH + " TEXT ,"
            + CATEGORYIDS + " TEXT ,"
            + USERVOTE + " TEXT ,"
            + USERFLAG + " TEXT ,"
            + USERFAVORITEQ + " TEXT ,"
            + USERID + " TEXT ,"
            + COOKIEID + " TEXT ,"
            + CREATEIP + " TEXT ,"
            + POINTS + " TEXT ,"
            + FLAGS + " TEXT ,"
            + LEVEL + " TEXT ,"
            + EMAIL + " TEXT ,"
            + HANDLE + " TEXT ,"
            + AVATARBLOBID + " TEXT ,"
            + AVATARWIDTH + " TEXT ,"
            + AVATARHEIGHT + " TEXT ,"
            + ITEMORDER + " TEXT);";
			
	//CREATE USERS TABLE SQL
    public static final String CREATE_USERS_TABLE_SQL =
            "CREATE TABLE " + TBL_USERS + " ("
            + USERID + " INTEGER PRIMARY KEY, "
            + HANDLE + " TEXT, "
            + POINTS + " TEXT, "
            + FLAGS + " TEXT, "
            + EMAIL + " TEXT, "
            + AVATARBLOBID + " TEXT ,"
            + AVATARWIDTH + " TEXT ,"
            + AVATARHEIGHT + " TEXT ,"
            + ITEMORDER + " TEXT);";
		
	
}