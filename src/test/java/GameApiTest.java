import com.cotfk.GameState;
import com.cotfk.commands.Actor;
import com.cotfk.commands.CommandParser;
import com.cotfk.maps.GlobalMap;
import com.cotfk.maps.MapLevel;
import com.crown.i18n.I18n;
import com.crown.time.Timeline;
import com.crown.time.VirtualClock;
import org.junit.jupiter.api.*;

import java.util.*;

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
                new GlobalMap("Map", 11, 11, MapLevel.height)
            )
        );
        // Player is captured by `new` command automatically,
        // so we need to deselect player from old game state.
        Actor.free();
    }

    private static GameState gs() {
        return (GameState) Timeline.main.getGameState();
    }

    @Test
    public void checkPlayersCountAfterNew() {
        CommandParser.parse("new w a");
        assertEquals(1, gs().players.size());
    }

    @Test
    public void checkPlayerNamesAreUnique() {
        CommandParser.parse("new w a");
        var failResult = CommandParser.parse("new m a");
        assertEquals(1, gs().players.size());
        assertNotEquals(I18n.okMessage, failResult);
    }

    @Test
    public void checkPlayerSelect() {
        CommandParser.parse("new w a");
        CommandParser.parse("new w b");
        assertEquals(2, gs().players.size());

        CommandParser.parse("select a");
        assertEquals(2, gs().players.size());
        assertEquals("a", Actor.get().getName().getLocalized());

        CommandParser.parse("select b");
        assertEquals(2, gs().players.size());
        assertEquals("b", Actor.get().getName().getLocalized());
    }

    @Test
    public void checkPlayerKill() {
        CommandParser.parse("new w a");
        CommandParser.parse("new w b");
        assertEquals(2, gs().players.size());

        CommandParser.parse("kill");
        assertEquals(1, gs().players.size());
        assertNull(Actor.get());
    }

    @Test
    public void checkPlayerNameReuseAfterKill() {
        CommandParser.parse("new w a");
        CommandParser.parse("new w b");
        assertEquals(2, gs().players.size());

        CommandParser.parse("kill");
        assertEquals(1, gs().players.size());
        assertNull(Actor.get());

        CommandParser.parse("new w b");
        assertEquals(2, gs().players.size());
        assertEquals("b", Actor.get().getName().getLocalized());
    }
}
