import static org.junit.jupiter.api.Assertions.*;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RPLSTest {


	Server server;
	GameInfo gameInfo;

	@BeforeEach
	void init(){
		server = new Server(data->{
			Platform.runLater(()->{
			});
		}, 5555);
		gameInfo = new GameInfo();
	}

	// Checking if the points and moves are empty at start
	@Test
	void testP1Point(){
		assertEquals(0, server.client1GM.getP1Points(), "P1 Point doesnt start at 0");
	}
	@Test
	void testP2Point(){
		assertEquals(0, server.client1GM.getP2Points(), "P2 Point doesnt start at 0");
	}
	@Test
	void testP1Play(){
		assertNull(server.client1GM.getP1Plays(), "P1 move doesnt start at null");
	}
	@Test
	void testP2Play(){
		assertNull(server.client1GM.getP2Plays(), "P2 move doesnt start at null");
	}

	// Checking if p1 and p2 points work properly
	@Test
	void testNotZeroP1Point(){
		server.client1GM.setP1Points(1);
		assertEquals(1, server.client1GM.getP1Points(), "P1 point doesnt equal 1");
	}
	@Test
	void testNotZeroP2Point(){
		server.client1GM.setP2Points(1);
		assertEquals(1, server.client1GM.getP2Points(), "P2 point doesnt equal 1");
	}

	// Checking if p1 and p2 moves work with string
	@Test
	void testP1PlayWithString(){
		server.client1GM.setP1Plays("rock");
		assertEquals("rock", server.client1GM.getP1Plays(), "P1 move doesnt equal rock");
	}
	@Test
	void testP2PlayWithString(){
		server.client1GM.setP2Plays("rock");
		assertEquals("rock", server.client1GM.getP2Plays(), "P2 move doesnt equal rock");
	}

	//Check to see if whoWon works
	@Test
	void testWhoWon(){
		server.client1GM.setWhoWon("Player 1 Wins");
		assertEquals("Player 1 Wins", server.client1GM.getWhoWon(), "whoWon doesnt return properly");
	}

	// Check if boolValue starts at false
	@Test
	void testidSet(){
		assertFalse(server.client1GM.idSet, "Doesnt equal false");
	}


}
