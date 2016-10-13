package localhost.iillyyaa2033.mud.androidclient.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import localhost.iillyyaa2033.mud.androidclient.exceptions.IncorrectPositionException;
import localhost.iillyyaa2033.mud.androidclient.logic.dictionary.WordScript;
import localhost.iillyyaa2033.mud.androidclient.logic.model.World;
import localhost.iillyyaa2033.mud.androidclient.logic.model.WorldObject;
import localhost.iillyyaa2033.mud.androidclient.logic.model.Zone;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class ImportUtil {

	public static HashMap<String, String> loadUsercommands() throws FileNotFoundException {
		File work = new File(GlobalValues.datapath, "scripts/usercommands.txt");

		if (!work.exists()) {
			throw new FileNotFoundException("ImportUtil - loadUsercommands - 404");
		}

		HashMap<String, String> cmds = new HashMap<String, String>();

		try {
			InputStream inputstream = new FileInputStream(work);
			InputStreamReader reader = new InputStreamReader(inputstream, GlobalValues.encoding);
			BufferedReader bufferedreader = new BufferedReader(reader);

			String buffer = null;
			String currName = "";
			StringBuffer currscript = new StringBuffer();
			boolean n = false;
			while ((buffer = bufferedreader.readLine()) != null) {
				if (buffer.startsWith("##### ") && buffer.endsWith(" #####")) {
					if (!currName.equals(""))
						cmds.put(currName, currscript.toString());

					currscript = new StringBuffer();
					currName = buffer.substring(6, buffer.length() - 6);
					n = false;
				} else {
					currscript.append((n ? "\n" : "") + buffer);
					if (!n) n = !n;
				}
			}
			cmds.put(currName, currscript.toString());

			bufferedreader.close();
			reader.close();
			inputstream.close();
		} catch (FileNotFoundException e) {
			e(e);
		} catch (UnsupportedEncodingException e) {
			e(e);
		} catch (IOException e) {
			e(e);
		}

		return cmds;
	}

	public static ArrayList<WordScript> importWords() {
		return null;
	}

	public static void importToWorld(File file) {

		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(new FileInputStream(file), "UTF-8");
			
			Zone currentZone=null;
			WorldObject currentObject=null;

			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				
				if (eventType == XmlPullParser.START_DOCUMENT) {
					// do nothing
				} else if (eventType == XmlPullParser.START_TAG) {
					String name = xpp.getName();	// имя тэга

					if (name.equals("zone")) {
						currentZone = new Zone();
						World.zones.add(currentZone);

						int attribs = xpp.getAttributeCount();
						for (int i =0; i < attribs; i++) {
							currentZone.params.put(xpp.getAttributeName(i),xpp.getAttributeValue(i));
						}
					}
					
					if (name.equals("object")) {
						currentObject = new WorldObject();
						if (currentZone != null){
							currentZone.addObject(currentObject);

							int attribs = xpp.getAttributeCount();
							for (int i =0; i < attribs; i++) {
								currentObject.params.put(xpp.getAttributeName(i),xpp.getAttributeValue(i));
							}
						}
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					xpp.getName();	// имя тэга
				} else if (eventType == XmlPullParser.TEXT) {
					//	UNUSED
					xpp.getText();
				} else {
					e("In unknown place");
				}
				eventType = xpp.next();
			}
			
		} catch (XmlPullParserException e) {
			e(e);
		} catch (FileNotFoundException e) {
			e(e);
		} catch (IOException e) {
			e(e);
		} catch (IncorrectPositionException e){
			e(e);
		}
	}

	private static void e(Throwable e) {
		ExceptionsStorage.exceptions.add(e);
	}
	
	static void e(String e){
		ExceptionsStorage.exceptions.add(new Exception(e));
	}
}
