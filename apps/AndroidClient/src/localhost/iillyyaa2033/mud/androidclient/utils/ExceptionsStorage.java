package localhost.iillyyaa2033.mud.androidclient.utils;
import java.util.ArrayList;

public class ExceptionsStorage{
	
	public static ArrayList<Throwable> exceptions = new ArrayList<Throwable>();
	
	public static synchronized void addException(Throwable t){
		exceptions.add(t);
	}
}
