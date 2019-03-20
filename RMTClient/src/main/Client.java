package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket;
		boolean loggedIn = false;
		
		try {
			if(args[0] == null || args[1] == null)
				throw new Exception("Argumenti lose uneti");
			
			//socket = new Socket("localhost", 9090);
			socket = new Socket(args[0], Integer.parseInt(args[1]));
			System.out.println("Povezani smo");
			
			
			PrintWriter socketOut = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
			
			String opcija;
			String meni = socketIn.readLine();		
			int pokusaji = 3;
			
			while(true) {
				if(!socket.isClosed())
					System.out.println(meni);
				
				do {
					opcija = console.readLine();
				} while(opcija == null || !Client.proveriOpciju(opcija));
				
				if(loggedIn && (Integer.parseInt(opcija) == 1 || Integer.parseInt(opcija) == 2)) {
					System.out.println("Vec ste se ulogovali");
					continue;
				}
				
				if(Integer.parseInt(opcija) == 3 && !loggedIn) {
					if(pokusaji == 0) {
						opcija = "0";
					}
					pokusaji--;
				}
				
				socketOut.println(opcija);
				String odgovor = socketIn.readLine();
				System.out.println(odgovor);			
				
				
				switch(Integer.parseInt(opcija)) {
				case 0:
					System.out.println("Prekinuta je konekcija sa serverom");
					socket.close();
					
					break;
					
				case 1:
					
					System.out.print("Unesite username:");
					String user = console.readLine();
					socketOut.println(user);
					
					System.out.print("Unesite password:");
					String pass = console.readLine();
					socketOut.println(pass);
					
					if(socketIn.readLine().equals("true")) {
						System.out.println("Uspesno ste se ulogovali");
						loggedIn = true;
						socketOut.println("1");
					} else {
						socketOut.println("0");
						System.out.println("Pogresni podaci");
					}						
					
					break;
					
					
				case 2: // registracija
					String provera;
					String poruka;
					do {
						System.out.println("Unesite username:");
						String noviUser = console.readLine();
						socketOut.println(noviUser);
						provera = socketIn.readLine();
						if(!provera.equals("1")) {
							System.out.println(provera);
						}
					} while(!provera.equals("1"));
					
					do {
						System.out.println("Unesite password:");
						String noviPass = console.readLine();
						socketOut.println(noviPass);
						provera = socketIn.readLine();
						if(!provera.equals("1")) {
							System.out.println(provera);
						}
					} while(!provera.equals("1"));
					
					System.out.println("Uspesno ste se registrovali, sada se ulogujte");
					
					break;
				
					
				case 3:
					String izraz = console.readLine();
					socketOut.println(izraz);
					
					System.out.println( socketIn.readLine() );
					break;
					
				case 4:
					if(loggedIn) {
						loggedIn = false;
						pokusaji = 3;
						System.out.println("Izlogovani ste");	
						socketOut.println("1");
					} else {
						System.out.println("Niste ni ulogovani");
						socketOut.println("0");
					}
					break;
				case 5:
					String history = socketIn.readLine();
					System.out.println(history);
					System.out.println(socketIn.readLine());
					socketOut.println( console.readLine() );
					break;
				}			
				
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static boolean proveriOpciju(String opcija) throws Exception {
		if(opcija == null) {
			throw new Exception("Opcija lose uneta(null)");
		}
		
		if(opcija.length() > 1) 
			return false;
		
		if( !Character.isDigit(opcija.charAt(0)) )
			return false;
		
		if(Integer.parseInt(opcija) < 0 || Integer.parseInt(opcija) > 5)
			return false;
		
		return true;
	}
}
