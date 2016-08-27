package localhost.iillyyaa2033.mud.androidclient.utils;
import android.content.Context;
import android.preference.PreferenceManager;

public class GlobalValues{
	
	public static String datapath = "/storage/emulated/0/AppProjects/nmud/content-ru/";
	public static String encoding = "UTF-8";
	public static String encoding_contentarchive = "UTF-8";
	
	public static boolean canScripts = !false;
	public static boolean debug = true;
	public static boolean debug_importer = false;
	public static boolean debug_descr = false;
	public static boolean debug_scripts = false;
	public static boolean debug_graph = false;
	
	public static void updatePrefs(Context c){
		debug = PreferenceManager.getDefaultSharedPreferences(c).getBoolean("FLAG_DEBUG",true);
		debug_importer = PreferenceManager.getDefaultSharedPreferences(c).getBoolean("FLAG_DEBUG_IMPORTER",false);
		debug_descr = PreferenceManager.getDefaultSharedPreferences(c).getBoolean("FLAG_DEBUG_DESCR",false);
		debug_scripts = PreferenceManager.getDefaultSharedPreferences(c).getBoolean("FLAG_DEBUG_SCRIPTS",false);
		debug_graph = PreferenceManager.getDefaultSharedPreferences(c).getBoolean("FLAG_DEBUG_GRAPH",false);	
		
	}
	
}
