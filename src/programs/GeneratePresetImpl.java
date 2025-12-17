package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {
    private static final int MAX_UNITS_PER_TYPE = 11;
    private static final int MIN_X = 0;
    private static final int MAX_X = 2;
    private static final int MIN_Y = 0;
    private static final int MAX_Y = 20;
    private final Random random = new Random();

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        List<Unit> result = new ArrayList<>();
        Map<String, Integer> typeCount = new HashMap<>();
        Set<String> occupiedCells = new HashSet<>();
        for (Unit unit : unitList) {
            typeCount.put(unit.getUnitType(), 0);
        }
        int points = 0;
        List<Unit> shuffled = new ArrayList<>(unitList);
        while (true) {
            Collections.shuffle(shuffled, random);
            boolean added = false;
            for (Unit proto : shuffled) {
                int count = typeCount.get(proto.getUnitType());
                if (count >= MAX_UNITS_PER_TYPE) {
                    continue;
                }
                if (points + proto.getCost() > maxPoints) {
                    continue;
                }
                int attempts = 0;
                int x, y;
                String key;
                do {
                    x = random.nextInt(MAX_X - MIN_X + 1) + MIN_X;
                    y = random.nextInt(MAX_Y - MIN_Y + 1) + MIN_Y;
                    key = x + ":" + y;
                    attempts++;
                } while (occupiedCells.contains(key) && attempts < 50);
                if (occupiedCells.contains(key)) {
                    continue;
                }
                Unit unit = new Unit(proto.getUnitType() + " " + (count + 1), proto.getUnitType(), proto.getHealth(), proto.getBaseAttack(), proto.getCost(), proto.getAttackType(), proto.getAttackBonuses(), proto.getDefenceBonuses(), x, y);
                result.add(unit);
                occupiedCells.add(key);
                typeCount.put(proto.getUnitType(), count + 1);
                points += proto.getCost();
                added = true;
                break;
            }
            if (!added) {
                break;
            }
        }
        Army army = new Army();
        army.setUnits(result);
        army.setPoints(points);

        return army;
    }
}
