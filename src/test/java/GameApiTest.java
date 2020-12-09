import com.cotfk.GameState;
import com.cotfk.commands.Actor;
import com.cotfk.maps.GlobalMap;
import com.cotfk.maps.MapLevel;
import com.crown.i18n.I18n;
import com.crown.time.Timeline;
import com.crown.time.VirtualClock;
import org.junit.jupiter.api.*;

import java.time.Instant;
import java.util.*;

import static com.cotfk.commands.CommandParser.parse;
import static org.junit.jupiter.api.Assertions.*;

public class GameApiTest {
    public static final HashMap<String, ResourceBundle> bundles = new HashMap<>();

    @BeforeAll
    public static void setup() {
        // Internationalization is static across any timeline.
        bundles.put("en", ResourceBundle.getBundle("gameMessages", new Locale("en_US")));
        I18n.init(bundles);
        Timeline.init(
            new VirtualClock(24, () -> {
            }),
            null
        );
    }

    @BeforeEach
    private void initStandardTimeline() {
        // Each test runs in isolated timeline.
        Timeline.main = new Timeline(
            new GameState(
                new GlobalMap(
                    "Map",
                    11,
                    11,
                    MapLevel.height
                ).flatTerrain()
            )
        );
        // Player is captured by `new` command automatically,
        // so we need to deselect player from old game state.
        Actor.free();
        // Reset clock
        Timeline.getClock().startAt(Instant.EPOCH);
    }

    private static GameState gs() {
        return (GameState) Timeline.main.getGameState();
    }

    @Test
    public void checkPlayersCountAfterNew() {
        parse("new w a");
        assertEquals(1, gs().players.size());
        parse("new w b");
        assertEquals(2, gs().players.size());
    }

    @Test
    public void checkPlayerNamesAreUnique() {
        parse("new w a");
        var failResult = parse("new m a");
        assertEquals(1, gs().players.size());
        assertNotEquals(I18n.okMessage, failResult);
    }

    @Test
    public void checkPlayerSelect() {
        parse("new w a");
        parse("new w b");
        assertEquals(2, gs().players.size());

        parse("select a");
        assertEquals(2, gs().players.size());
        assertEquals("a", Actor.get().getName().getLocalized());

        parse("select b");
        assertEquals(2, gs().players.size());
        assertEquals("b", Actor.get().getName().getLocalized());
    }

    @Test
    public void checkPlayerKill() {
        parse("new w a");
        parse("new w b");
        assertEquals(2, gs().players.size());

        parse("kill");
        assertEquals(1, gs().players.size());
        assertNull(Actor.get());
    }

    @Test
    public void checkPlayerNameReuseAfterKill() {
        parse("new w a");
        parse("new w b");

        parse("kill");
        assertEquals(1, gs().players.size());
        assertNull(Actor.get());

        parse("new w b");
        assertEquals(2, gs().players.size());
        assertEquals("b", Actor.get().getName().getLocalized());
    }
}
