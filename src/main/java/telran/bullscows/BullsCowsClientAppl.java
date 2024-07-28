package telran.bullscows;

import java.util.List;

import telran.net.TcpClient;
import telran.view.Item;
import telran.view.Menu;
import telran.view.SystemInputOutput;

public class BullsCowsClientAppl {
	private static final int PORT = 5000;
	
	public static void main(String[] args) {
		TcpClient tcpClient = new TcpClient("localhost", PORT);
		BullsCowsProxy bullsCowsProxy = new BullsCowsProxy(tcpClient);
		
		List<Item> bullsCowsItems =
				BullsCowsApplItems.getItems(bullsCowsProxy);
		bullsCowsItems.add(Item.of("Exit & connection close", io -> {
			try {
				tcpClient.close();
			} catch (Exception e) {
				
			}
		}, true));
	
		Menu menu = new Menu("BullsCows Game (viaTCP)",
				bullsCowsItems.toArray(Item[]::new));
		menu.perform(new SystemInputOutput());

	}

}
