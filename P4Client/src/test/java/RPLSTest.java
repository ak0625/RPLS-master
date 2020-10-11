import static org.junit.jupiter.api.Assertions.*;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RPLSTest {

	Client client;
	GameInfo gameInfo;

	@BeforeEach
	void init(){
		gameInfo = new GameInfo();
		client = new Client(data->{
			Platform.runLater(()->{
			});
		}, 5555, "127.0.0.1");
	}

	// Checking if the points and moves are empty at start
	@Test
	void testP1Point(){
		assertEquals(0, client.client.getP1Points(), "P1 Point doesnt start at 0");
	}
	@Test
	void testP2Point(){
		assertEquals(0, client.client.getP2Points(), "P2 Point doesnt start at 0");
	}
	@Test
	void testP1Play(){
		assertNull(client.client.getP1Plays(), "P1 move doesnt start at null");
	}
	@Test
	void testP2Play(){
		assertNull(client.client.getP2Plays(), "P2 move doesnt start at null");
	}

	// Checking if p1 and p2 points work properly
	@Test
	void testNotZeroP1Point(){
		client.client.setP1Points(1);
		assertEquals(1, client.client.getP1Points(), "P1 point doesnt equal 1");
	}
	@Test
	void testNotZeroP2Point(){
		client.client.setP2Points(1);
		assertEquals(1, client.client.getP2Points(), "P2 point doesnt equal 1");
	}

	// Checking if p1 and p2 moves work with string
	@Test
	void testP1PlayWithString(){
		client.client.setP1Plays("rock");
		assertEquals("rock", client.client.getP1Plays(), "P1 move doesnt equal rock");
	}
	@Test
	void testP2PlayWithString(){
		client.client.setP2Plays("rock");
		assertEquals("rock", client.client.getP2Plays(), "P2 move doesnt equal rock");
	}

	//Check to see if whoWon works
	@Test
	void testWhoWon(){
		client.client.setWhoWon("Player 1 Wins");
		assertEquals("Player 1 Wins", client.client.getWhoWon(), "whoWon doesnt return properly");
	}

	// Check if boolValue starts at false
	@Test
	void testidSet(){
		assertFalse(client.client.idSet, "Doesnt equal false");
	}
}
