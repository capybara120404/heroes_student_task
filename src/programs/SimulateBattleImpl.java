package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;
import java.util.*;

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog;

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        while (true) {
            List<Unit> alive = new ArrayList<>();
            for (Unit u : playerArmy.getUnits()) {
                if (u.isAlive()) {
                    alive.add(u);
                }
            }
            for (Unit u : computerArmy.getUnits()) {
                if (u.isAlive()) {
                    alive.add(u);
                }
            }
            if (alive.isEmpty()) {
                break;
            }
            alive.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());

            if (playerArmy.getUnits().stream().noneMatch(Unit::isAlive) ||
                    computerArmy.getUnits().stream().noneMatch(Unit::isAlive)) {
                break;
            }

            for (Unit unit : alive) {
                if (!unit.isAlive()) {
                    continue;
                }

                Unit target = unit.getProgram().attack();
                if (target != null && printBattleLog != null) {
                    printBattleLog.printBattleLog(unit, target);
                }
            }
        }
    }
}
