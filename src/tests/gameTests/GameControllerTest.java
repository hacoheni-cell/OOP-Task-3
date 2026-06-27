package tests.unitsTest;

import Game.GameController;
import Game.Level;
import Units.Unit;
import businessLayer.InputCallback;
import businessLayer.MessageCallback;
import combat.CombatSystem;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    private CombatSystem createPassthroughCombatSystem() {
        return new CombatSystem() {
            @Override
            public int Combat(Unit attacker, Unit defender) {
                return 0;
            }
            @Override
            public boolean Attack(Unit otherUnit, double damage, String attackerName) {
                otherUnit.SetHealthAmount(otherUnit.getHealthAmount() - (int) damage);
                return true;
            }
        };
    }

    private InputCallback createScriptedInput(List<String> inputs) {
        Iterator<String> iterator = inputs.iterator();
        return () -> iterator.hasNext() ? iterator.next() : "q";
    }

    private Level createEmptyLevel(CombatSystem combatSystem, MessageCallback messageCallback) {
        List<String> levelData = new ArrayList<>();
        levelData.add("###");
        levelData.add("#@#");
        levelData.add("###");
        return new Level(levelData, combatSystem, messageCallback);
    }

    private Level createLevelWithOneWeakMonster(CombatSystem combatSystem, MessageCallback messageCallback) {
        List<String> levelData = new ArrayList<>();
        levelData.add("#####");
        levelData.add("#@.s#");
        levelData.add("#####");
        return new Level(levelData, combatSystem, messageCallback);
    }

    @Test
    void testConstructor_InitializesSevenAvailablePlayers() {
        List<String> sentMessages = new ArrayList<>();
        MessageCallback messageCallback = sentMessages::add;
        CombatSystem combatSystem = createPassthroughCombatSystem();
        InputCallback inputCallback = createScriptedInput(List.of("1"));

        List<Level> levels = new ArrayList<>();
        levels.add(createEmptyLevel(combatSystem, messageCallback));

        GameController controller = new GameController(messageCallback, inputCallback, levels, combatSystem);
        controller.start();

        long playerListingMessages = sentMessages.stream()
                .filter(m -> m.matches("^[1-7]\\. .*"))
                .count();
        assertEquals(7, playerListingMessages);
    }

    @Test
    void testSelectPlayer_InvalidThenValidChoice_PromptsUntilValid() {
        List<String> sentMessages = new ArrayList<>();
        MessageCallback messageCallback = sentMessages::add;
        CombatSystem combatSystem = createPassthroughCombatSystem();
        InputCallback inputCallback = createScriptedInput(List.of("not-a-number", "99", "1"));

        List<Level> levels = new ArrayList<>();
        levels.add(createEmptyLevel(combatSystem, messageCallback));

        GameController controller = new GameController(messageCallback, inputCallback, levels, combatSystem);
        controller.start();

        assertTrue(sentMessages.stream().anyMatch(m -> m.contains("Invalid input")));
        assertTrue(sentMessages.stream().anyMatch(m -> m.contains("Invalid choice")));
        assertTrue(sentMessages.stream().anyMatch(m -> m.contains("You have selected: Jon Snow")));
    }

    @Test
    void testStart_EmptyLevel_DeclaresVictoryWithoutAnyMoves() {
        List<String> sentMessages = new ArrayList<>();
        MessageCallback messageCallback = sentMessages::add;
        CombatSystem combatSystem = createPassthroughCombatSystem();
        InputCallback inputCallback = createScriptedInput(List.of("1"));

        List<Level> levels = new ArrayList<>();
        levels.add(createEmptyLevel(combatSystem, messageCallback));

        GameController controller = new GameController(messageCallback, inputCallback, levels, combatSystem);
        controller.start();

        assertTrue(sentMessages.stream().anyMatch(m -> m.equals("You won!")));
    }

    @Test
    void testStart_LevelWithMonster_PlayerAttacksAndWins() {
        List<String> sentMessages = new ArrayList<>();
        MessageCallback messageCallback = sentMessages::add;
        CombatSystem combatSystem = createPassthroughCombatSystem();

        List<String> inputs = new ArrayList<>();
        inputs.add("1");
        for (int i = 0; i < 15; i++) {
            inputs.add("e");
        }
        InputCallback inputCallback = createScriptedInput(inputs);

        List<Level> levels = new ArrayList<>();
        levels.add(createLevelWithOneWeakMonster(combatSystem, messageCallback));

        GameController controller = new GameController(messageCallback, inputCallback, levels, combatSystem);
        controller.start();

        assertTrue(sentMessages.stream().anyMatch(m -> m.contains("died")));
        assertTrue(sentMessages.stream().anyMatch(m -> m.equals("You won!")));
    }

    @Test
    void testStart_PlayerDies_SendsGameOverMessage() {
        List<String> sentMessages = new ArrayList<>();
        MessageCallback messageCallback = sentMessages::add;

        CombatSystem lethalCombatSystem = new CombatSystem() {
            @Override
            public int Combat(Unit attacker, Unit defender) {
                defender.SetHealthAmount(0);
                return 0;
            }
            @Override
            public boolean Attack(Unit otherUnit, double damage, String attackerName) {
                otherUnit.SetHealthAmount(otherUnit.getHealthAmount() - (int) damage);
                return true;
            }
        };

        List<String> inputs = new ArrayList<>();
        inputs.add("1");
        for (int i = 0; i < 5; i++) {
            inputs.add("d");
        }
        InputCallback inputCallback = createScriptedInput(inputs);

        List<Level> levels = new ArrayList<>();
        levels.add(createLevelWithOneWeakMonster(lethalCombatSystem, messageCallback));

        GameController controller = new GameController(messageCallback, inputCallback, levels, lethalCombatSystem);
        controller.start();

        assertTrue(sentMessages.stream().anyMatch(m -> m.contains("Game Over")));
        assertFalse(sentMessages.stream().anyMatch(m -> m.equals("You won!")));
    }

    @Test
    void testStart_MultipleLevels_ProgressesThroughBothAndWins() {
        List<String> sentMessages = new ArrayList<>();
        MessageCallback messageCallback = sentMessages::add;
        CombatSystem combatSystem = createPassthroughCombatSystem();

        List<String> inputs = new ArrayList<>();
        inputs.add("1");
        for (int i = 0; i < 15; i++) {
            inputs.add("e");
        }
        for (int i = 0; i < 15; i++) {
            inputs.add("e");
        }
        InputCallback inputCallback = createScriptedInput(inputs);

        List<Level> levels = new ArrayList<>();
        levels.add(createLevelWithOneWeakMonster(combatSystem, messageCallback));
        levels.add(createLevelWithOneWeakMonster(combatSystem, messageCallback));

        GameController controller = new GameController(messageCallback, inputCallback, levels, combatSystem);
        controller.start();

        long levelCompleteMessages = sentMessages.stream()
                .filter(m -> m.equals("Level Complete!"))
                .count();

        assertEquals(2, levelCompleteMessages);
        assertTrue(sentMessages.stream().anyMatch(m -> m.equals("You won!")));
    }

    @Test
    void testStart_MultipleLevels_PlayerDiesInSecondLevel() {
        List<String> sentMessages = new ArrayList<>();
        MessageCallback messageCallback = sentMessages::add;

        CombatSystem progressiveCombatSystem = new CombatSystem() {
            private int levelHits = 0;
            @Override
            public int Combat(Unit attacker, Unit defender) {
                defender.SetHealthAmount(0);
                return 0;
            }
            @Override
            public boolean Attack(Unit otherUnit, double damage, String attackerName) {
                otherUnit.SetHealthAmount(otherUnit.getHealthAmount() - (int) damage);
                levelHits++;
                return true;
            }
        };

        List<String> inputs = new ArrayList<>();
        inputs.add("1");
        for (int i = 0; i < 15; i++) {
            inputs.add("e");
        }
        for (int i = 0; i < 5; i++) {
            inputs.add("d");
        }
        InputCallback inputCallback = createScriptedInput(inputs);

        List<Level> levels = new ArrayList<>();
        levels.add(createLevelWithOneWeakMonster(progressiveCombatSystem, messageCallback));
        levels.add(createLevelWithOneWeakMonster(progressiveCombatSystem, messageCallback));

        GameController controller = new GameController(messageCallback, inputCallback, levels, progressiveCombatSystem);
        controller.start();

        long levelCompleteMessages = sentMessages.stream()
                .filter(m -> m.equals("Level Complete!"))
                .count();

        assertEquals(1, levelCompleteMessages);
        assertTrue(sentMessages.stream().anyMatch(m -> m.contains("Game Over")));
        assertFalse(sentMessages.stream().anyMatch(m -> m.equals("You won!")));
    }
}