package server_core;

import java.util.LinkedList;
import java.util.Collection;

import game.Game;
import game.Position;
import game.Tile;

/**
 * Implements the reverse delta frame protocol
 * @author zjwatt
 *
 */
public class StrategyReverseDelta implements Strategy {
	private StrategyReverseDeltaInfo info;
	
	public StrategyReverseDelta(StrategyReverseDeltaInfo info) {
		this.info = info;
	}

	@Override
	public void takeAction(Game game, SocketAdapter socket, Client client) {
		// TODO Auto-generated method stub
		if (client.getKey() == null) {
			System.out.println("This client does not have an identity");
			return;
		}

		Collection<Tile> tiles = new LinkedList<Tile>();
		for (TileInfo tileInfo : info.tiles) {
			tiles.add(
				new Tile(
					new Position(
						tileInfo.getX(),
						tileInfo.getY(),
						client.getKey()
							.getSubscriber(info.subscriptionID)
							.getBounds()
							.getDungeon()
					),
					tileInfo.isWalkable(),
					tileInfo.getTerrainType(),
					tileInfo.getObject(),
					tileInfo.getCharacter()
				)
			);
		}

		client.getKey().getSubscriber(info.subscriptionID).reverseDelta(tiles);
	}

}
