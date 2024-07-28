package telran.bullscows;

import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import telran.net.Request;
import telran.net.TcpClient;

public class BullsCowsProxy implements BullsCowsService {
	TcpClient tcpClient;

	public BullsCowsProxy(TcpClient tcpClient) {
		this.tcpClient = tcpClient;
	}

	@Override
	public long createNewGame() {
		return Long.parseLong(tcpClient.sendAndReceive(new Request("createNewGame", "")));
	}

	@Override
	public List<MoveResult> getMoveResults(long gameId, Move move) {
		JSONObject requestData = new JSONObject();
		requestData.put("id", gameId);
		requestData.put("sequence", move.sequence());
	
		String moveResultsJSON = tcpClient
				.sendAndReceive(new Request("getMoveResults", requestData.toString()));

		return Arrays.stream(moveResultsJSON.split(";")).map(str -> {
			return setMoveResult(str);
		}).toList();
	}

	private MoveResult setMoveResult(String str) {
		JSONObject jsonObject = new JSONObject(str);
		String sequence = jsonObject.getString("sequence");
		int bulls = jsonObject.getInt("bulls");
		int cows = jsonObject.getInt("cows");
		return new MoveResult(sequence, bulls, cows);
	}

	@Override
	public boolean isGameOver(long gameId) {
		String isGameOver = tcpClient
				.sendAndReceive(new Request("isGameOver", ""+ gameId));
		JSONObject jsonObject = new JSONObject(isGameOver);
		return jsonObject.getBoolean(isGameOver);
	}

}


