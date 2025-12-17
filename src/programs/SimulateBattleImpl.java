package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog;

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        while (armyHasAlive(playerArmy) && armyHasAlive(computerArmy)) {
            List<Unit> allUnits = collectAliveUnits(playerArmy, computerArmy);
            allUnits.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());

            for (Unit unit : allUnits) {
                if (!unit.isAlive()) {
                    continue;
                }

                Unit target = unit.getProgram().attack();
                logBattle(unit, target);

                if (!armyHasAlive(playerArmy) || !armyHasAlive(computerArmy)) {
                    return;
                }
            }
        }
    }

    private boolean armyHasAlive(Army army) {
        return army.getUnits().stream().anyMatch(Unit::isAlive);
    }

    private List<Unit> collectAliveUnits(Army playerArmy, Army computerArmy) {
        List<Unit> aliveUnits = new ArrayList<>();
        aliveUnits.addAll(playerArmy.getUnits().stream().filter(Unit::isAlive).toList());
        aliveUnits.addAll(computerArmy.getUnits().stream().filter(Unit::isAlive).toList());

        return aliveUnits;
    }

    private void logBattle(Unit attacker, Unit target) {
        if (printBattleLog != null && target != null) {
            printBattleLog.printBattleLog(attacker, target);
        }
    }
}
