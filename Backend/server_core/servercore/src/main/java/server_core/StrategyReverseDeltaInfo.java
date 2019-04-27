package server_core;

import java.util.Collection;

/**
 * Dummy class encapsulating the information in
 * the client's JSON message
 * @author Zachariah Watt
 *
 */
public class StrategyReverseDeltaInfo {
	public String requestType;
	public Collection<TileInfo> tiles;
}
