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
import localhost.iillyyaa2033.mud.androidclient.logic.model.World;
import localhost.iillyyaa2033.mud.androidclient.logic.model.Zone;
import localhost.iillyyaa2033.mud.androidclient.logic.model.WorldObject;

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

	public static void saveFromWorld() {
		StringBuilder sb = new StringBuilder();
		for (Zone z : World.zones) {
			sb.append("<zone");
			for (String key : z.params.keySet()) {
				sb.append("\n").append(key).append("=\"").append(z.params.get(key)).append("\"");
			}
			sb.append(" >");
			for (WorldObject o : z.objects) {
				sb.append("\n<object");
				for (String key : o.params.keySet()) {
					sb.append("\n").append(key).append("=\"").append(o.params.get(key)).append("\"");
				}
				sb.append(" />");
			}
			sb.append("\n</zone>\n");
		}
		// TODO: save obiects without zones
		try {
			File f= new File(GlobalValues.datapath + "/maps/root.xml");
			if (!f.exists()) {
				f.createNewFile();
			}
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(f),"UTF-8");
			writer.write(sb.toString());
			writer.close();
		} catch (IOException e) {
			e(e);
		}
	}

	private static void e(Throwable e) {
		ExceptionsStorage.exceptions.add(e);
	}

	static void e(String e) {
		ExceptionsStorage.exceptions.add(new Exception(e));
	}
}
