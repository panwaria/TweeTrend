/**
 * Class to store application wide constants.
 */
public class AppConstants
{
	// DEBUGGING Related
	public static final int PROCESSING_TWEET_LIMIT = 1000;
	public static final boolean PRINT_TO_CONSOLE_ALLOWED = false;
	
	// TAXONOMY Related
	public static final String TAXONOMY_SOURCE_XML = "src\\Taxonomy_Movies.xml";
	public static final String TAXONOMY_DELIMITER_STRING = "\\s+|:|,|\\.|&|-";
	public static final String GO_WORDS_SOURCE_FILE = "src\\go_words.dat";
	
	// TWEETS Related
	public static final String TWITTER_DATA_FILE = "C:\\Tweets\\twitter_20110731-12.log";
	public static final String TWEET_DELIMITER_STRING = "\\s+|:|,|\\.|&|-|\\?";
	
	// DEBUGGING Related
	public static final String LOGS_DIRECTORY_NAME = "logs";
	public static final int TO_NONE = 0;
	public static final int TO_CONSOLE = 1;
	public static final int TO_FILE = 2;
	public static final int PRINT_DEST = TO_FILE;
	
	public static final String ENGLISH_WORDS_FILE = "src\\words.dat";
	
	public static final String TMDB_API_KEY = "68493e79cf1639dadb814fcc797ab58d";
	
	
	public static final String OUTPUT_HTML_FILE_BASE_NAME = "tagcloud_"; 
	
	public static final String OUTPUT_HTML_START_STRING = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n" + 
			"<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n" + 
			"<head>\r\n" + 
			"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\r\n" + 
			"<title>TweetTrend</title>\r\n" + 
			"<style>\r\n" + 
			"\r\n" + 
			"body {\r\n" + 
			"	margin:0;\r\n" + 
			"	padding:0;\r\n" + 
			"	background:#e1e1e1;\r\n" + 
			"	font:80% Trebuchet MS, Arial, Helvetica, sans-serif;\r\n" + 
			"	color:#555;\r\n" + 
			"	line-height:180%;\r\n" + 
			"}\r\n" + 
			"a{color:#3c70d0;}\r\n" + 
			"h1{\r\n" + 
			"	font-size:180%;\r\n" + 
			"	font-weight:normal;\r\n" + 
			"	margin:0 20px;\r\n" + 
			"	padding:1em 0;\r\n" + 
			"	}\r\n" + 
			"h2{\r\n" + 
			"	font-size:160%;\r\n" + 
			"	font-weight:normal;\r\n" + 
			"	}	\r\n" + 
			"h3{\r\n" + 
			"	font-size:140%;\r\n" + 
			"	font-weight:normal;\r\n" + 
			"	}	\r\n" + 
			"img{border:none;}\r\n" + 
			"pre{\r\n" + 
			"	display:block;\r\n" + 
			"	font:12px \"Courier New\", Courier, monospace;\r\n" + 
			"	padding:10px;\r\n" + 
			"	border:1px solid #bae2f0;\r\n" + 
			"	background:#e3f4f9;	\r\n" + 
			"	margin:.5em 0;\r\n" + 
			"	width:500px;\r\n" + 
			"	}	\r\n" + 
			"	\r\n" + 
			"#container{\r\n" + 
			"	margin:0 auto;\r\n" + 
			"	text-align:left;\r\n" + 
			"	width:700px;\r\n" + 
			"	background:#fff;\r\n" + 
			"	}\r\n" + 
			"#main{\r\n" + 
			"	float:left;\r\n" + 
			"	display:inline;\r\n" + 
			"	width:380px;\r\n" + 
			"	margin-left:20px;	\r\n" + 
			"	}\r\n" + 
			"#side{\r\n" + 
			"	float:left;\r\n" + 
			"	display:inline;\r\n" + 
			"	width:260px;\r\n" + 
			"	margin-left:20px;\r\n" + 
			"	}	\r\n" + 
			"#footer{\r\n" + 
			"	clear:both;\r\n" + 
			"	padding:1em 0;\r\n" + 
			"	margin:0 20px;\r\n" + 
			"	}\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"/* Tag cloud */\r\n" + 
			"\r\n" + 
			"	#tags ul{\r\n" + 
			"		margin:1em 0;\r\n" + 
			"		padding:.5em 10px;\r\n" + 
			"		text-align:center;\r\n" + 
			"		background:#71b5e9 url(bg_tags.gif) repeat-x;		\r\n" + 
			"		}\r\n" + 
			"	#tags li{\r\n" + 
			"		margin:0;\r\n" + 
			"		padding:0;\r\n" + 
			"		list-style:none;\r\n" + 
			"		display:inline;\r\n" + 
			"		}\r\n" + 
			"	#tags li a{\r\n" + 
			"		text-decoration:none;\r\n" + 
			"		color:#fff;\r\n" + 
			"		padding:0 2px;	\r\n" + 
			"		}\r\n" + 
			"	#tags li a:hover{	\r\n" + 
			"		color:#cff400;\r\n" + 
			"		}		\r\n" + 
			"	\r\n" + 
			"	.tag1{font-size:100%;}\r\n" + 
			"	.tag2{font-size:120%;}\r\n" + 
			"	.tag3{font-size:140%;}\r\n" + 
			"	.tag4{font-size:160%;}\r\n" + 
			"	.tag5{font-size:180%;}\r\n" + 
			"	.tag6{font-size:200%;}\r\n" + 
			"	.tag7{font-size:220%;}\r\n" + 
			"	.tag8{font-size:240%;}\r\n" + 
			"	.tag9{font-size:260%;}\r\n" + 
			"	.tag10{font-size:280%;}\r\n" + 
			"\r\n" + 
			"	\r\n" + 
			"	/* alternative layout */\r\n" + 
			"\r\n" + 
			"	#tags .alt{\r\n" + 
			"		text-align:left;\r\n" + 
			"		padding:0;\r\n" + 
			"		background:none;\r\n" + 
			"		}\r\n" + 
			"	#tags .alt li{\r\n" + 
			"		padding:2px 10px;\r\n" + 
			"		background:#efefef;\r\n" + 
			"		display:block;\r\n" + 
			"		}\r\n" + 
			"	#tags .alt .tag1, \r\n" + 
			"	#tags .alt .tag2, \r\n" + 
			"	#tags .alt .tag3, \r\n" + 
			"	#tags .alt .tag4, \r\n" + 
			"	#tags .alt .tag5{font-size:100%;}\r\n" + 
			"	#tags .alt .tag1{background:#7cc0f4;}\r\n" + 
			"	#tags .alt .tag2{background:#67abe0;}\r\n" + 
			"	#tags .alt .tag3{background:#4d92c7;}\r\n" + 
			"	#tags .alt .tag4{background:#3277ad;}\r\n" + 
			"	#tags .alt .tag5{background:#266ca2;}\r\n" + 
			"	#tags .alt .tag6{background:#000000;}\r\n" + 
			"	\r\n" + 
			"/* // Tag cloud */\r\n" + 
			"\r\n" + 
			"</style>\r\n" + 
			"\r\n" + 
			"<script type=\"text/javascript\" src=\"js/jquery.js\"></script>\r\n" + 
			"\r\n" + 
			"</head>\r\n" + 
			"\r\n" + 
			"<body>\r\n" + 
			"\r\n" + 
			"<div id=\"container\">	\r\n" + 
			"	\r\n" + 
			"	<h1 align='center'>Movies Tag Cloud</h1>\r\n" + 
			"	\r\n" + 
			"		<div id=\"tags\">\r\n" + 
			"			<ul>";

	public static final String OUTPUT_HTML_PART1 = "<li class=\"tag";
	public static final String OUTPUT_HTML_PART2 = "\"><a href=\"#\">";
	public static final String OUTPUT_HTML_PART3 = "</a></li>\n";
	
	public static final String OUTPUT_HTML_END_STRING = "</ul>\r\n" + 
			"		</div>\r\n" + 
			"\r\n" + 
			"</div>\r\n" + 
			"\r\n" + 
			"</body>\r\n" + 
			"</html>";
}
