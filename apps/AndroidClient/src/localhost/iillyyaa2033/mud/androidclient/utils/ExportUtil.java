package localhost.iillyyaa2033.mud.androidclient.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Set;

public class ExportUtil {
	
	public static void saveUsercommands(HashMap<String, String> cmds) throws FileNotFoundException {
		File work = new File(GlobalValues.datapath, "scripts/usercommands.txt");

		if (!work.exists()) {
			throw new FileNotFoundException("ImportUtil - saveUsercommands - 404");
		}

		try {
			OutputStream stream = new FileOutputStream(work);
			OutputStreamWriter writer = new OutputStreamWriter(stream, GlobalValues.encoding);
			BufferedWriter bufferedwriter = new BufferedWriter(writer);

			Set<String> set = cmds.keySet();
			for (String cmd : set) {
				writer.write("##### " + cmd + " #####\n");
				writer.write(cmds.get(cmd) + "\n");
			}

			bufferedwriter.close();
			writer.close();
			stream.close();
		} catch (UnsupportedEncodingException e) {
			e(e);
		} catch (IOException e) {
			e(e);
		}
	}
	
	public static void saveFromWorld(){
		
	}
	
	private static void e(Throwable e) {
		ExceptionsStorage.exceptions.add(e);
	}

	static void e(String e){
		ExceptionsStorage.exceptions.add(new Exception(e));
	}
}
