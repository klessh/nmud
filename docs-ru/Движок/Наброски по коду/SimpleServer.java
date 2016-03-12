private void backgroundThreadProcessing() {
		String name = Thread.currentThread().getName();
		Log.d(n, "("+name+") "+"------------------------- \n");
		Log.d(n, "("+name+") "+"Server started");

		BufferedReader in = null;
		PrintWriter    out= null;

		ServerSocket serversocket = null;
		Socket       fromclient = null;

		// create server socket
		try {
			serversocket = new ServerSocket(sPort);
			Log.d(n, "("+name+") "+"ServerSocket port is "+name);
			Log.d(n, "("+name+") "+"new thread name is "+"");
			Log.d(n, "("+name+") "+"Waiting for a client...");
			fromclient= serversocket.accept();
			Log.d(n, "("+name+") "+"Client connected");		

			in  = new BufferedReader(new InputStreamReader(fromclient.getInputStream()));
			out = new PrintWriter(fromclient.getOutputStream(), true);

			String input, output;

			Log.d(n, "("+name+") "+"Wait for messages");
			
			while ((input=in.readLine())!=null) {
				Log.d(n,"("+name+") "+"Recived: "+input);
				if (input.equalsIgnoreCase("exit")) break;
				out.println("Mirrored: "+input);
			}
		} catch (Exception e) {
			Log.e(n, "("+name+") "+null, e);
			stopSelf();
		}

		try {
			out.close();
			in.close();	
			fromclient.close();
			serversocket.close();
			Log.d(n, "("+name+") "+"Server closed");
//			Thread t = Thread.currentThread();
//			t.destroy();
		} catch (Exception e) {
			Log.e(n, "("+name+") "+"Cant close", e);		
		}
	}