// Multiplayer.java

import java.applet.Applet;
import java.awt.*; 
import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.String;

/* This applet is designed to communicate and query a central perl script about
 * the environment outside each individual java script.
 * author (many modifications): mikaels@it.kth.se 
 * original code author: Ronald.Tschalaer@psi.ch (comp.lang.java)
 */
public class MultiPlayer extends Applet
{
        private final String script = "/cgi/mikaels/battleship-test";
        private final String ctype  = "application/x-www-form-urlencoded";
        private       String sdata  = "playersquery=?";
        private       String rdata  = "";
        private       String home;
        private       int    port;

	// init gets the extra data necessary to find my cgi script on the
        // media server. Getting home and port. (80 of course being default
        // for web port numbers).

	public void init()
	{
		// White background looks better		

		setBackground(Color.white);

		home = getDocumentBase().getHost();
		port = getDocumentBase().getPort();
		if (port == -1)  port = 80;
	}

	// start does the actual work. It sends the data out to the media 
        // server and gets additional info as an exception into our rdata
        // variable.

	public void start()
	{
		Socket           sock;
		OutputStream     outp;
		InputStream      inp;
		DataOutputStream dataout;
		DataInputStream  datain;

		rdata = "";

		// create a client socket 

		try
			sock = new Socket(home, port);
		catch (Exception e)
		{
			rdata = e+" (socket: Host: "+home+"\tPort: "+port+")";
			return;
		}

		// Obtain output stream to communicate with the server
		System.out.println("Trying to contact: " + home + ":" + port);
		try
		{
			outp = sock.getOutputStream();
			inp  = sock.getInputStream();
		} 
		catch (Exception e)
		{
			rdata = e+" (getstream)";
			try
				sock.close();
			catch (IOException ee) ;
			return;
		}    

		try
		{
			dataout = new DataOutputStream(outp);
			datain  = new DataInputStream(inp);
		}
		catch (Exception e)
		{
			rdata = e+" (Dstream)";
			try
				sock.close();
			catch (IOException ee) ;
			return;
		}

		// Send http request to server and get return data
		System.out.println("Trying to send out HTTP header and POST data");
		try
		{
			// HTTP header
			dataout.writeBytes("POST " + script + " HTTP/1.0\r\n");
			dataout.writeBytes("Content-type: " + ctype + "\r\n");
			dataout.writeBytes("Content-length: " + sdata.length() + "\r\n");
			dataout.writeBytes("\r\n");         // end of header
             	
			// POST data
			dataout.writeBytes(sdata);
			dataout.writeBytes("\r\n");
			boolean body = false;
			String line;
			while ((line = datain.readLine()) != null)
			{
            			if (body)
                   			rdata += "\n" + line;
                     		else if (line.equals(""))       // end of header
                         		body = true;
              		}
             	}
             	catch (Exception e)
             	{
                 	rdata = e+" (write)";
                 	try
                     		sock.close();
                	catch (IOException ee) ;
                 	return;
             	}

		// close up shop

		try
		{
			dataout.close();
			datain.close();
		} catch (IOException e) ;
		try
			sock.close();
		catch (IOException e) ;
	}

	// since we are really only interested in immediate results from this
        // java class, stop has no real purpose for us.

	public void stop() {}

	// display is designed to show an ongoing status display of what is
	// actually going on with the communication process. It is basically going to
	// end up being a graphical applet System.out.println.

	public void display (String disp)
	{
	}

	// paint is used to show the interaction taking place after
        // communicating with the perl script. It also attempts to analyze our
	// hard-coded test data to show that it can be done. (More to give me
	// confidence that it can be done than anything else.)
    
	public void paint(Graphics g)
	{
		int 	line    = 1;
                int 	line_sp = getFontMetrics(getFont()).getHeight()+1;
		String 	foo 	= rdata.trim();
		int 	foo2;

		g.drawString("Received data from the CGI Script.", 5, line*line_sp);
            	line++;
		g.drawString("CGI Output: " + foo + ".", 5, line*line_sp);
            	line++;
		g.drawString("Attempting to see if # of Players = 1...", 5, line*line_sp);
            	line++;
		
		// Let's try converting the number in foo into an integer. Cross your fingers...
		foo2 = Integer.parseInt(foo);

		if (foo2 == 1) {
			g.drawString("Yep! Sure does. Multiplayer Interface Test successful.", 5, line*line_sp);
            		line++;
			System.out.println("Reached the if loop!");
		} else {
			g.drawString("Nope doesn't work.", 5, line*line_sp);
            		line++;
		}
	}
}

//End of MultiPlayer.java
