package tinkerbell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CONSTANTS {
    // SCORING POSITIONS
    public static final Map<String, List<Number>> coralScoreLocations = new HashMap<>();

    // Add CORAL REEF scoring locations with ApriTag ID
    static {
        // RED SIDE
        coralScoreLocations.put("6L", List.of(534, 206, 240)); // 22L
        coralScoreLocations.put("6R", List.of(546, 199, 240)); // 22R
        coralScoreLocations.put("7L", List.of(565, 166, 180)); // 21L
        coralScoreLocations.put("7R", List.of(565, 152, 180)); // 21R
        coralScoreLocations.put("8L", List.of(546, 105, 120)); // 20L
        coralScoreLocations.put("8R", List.of(534, 98, 120)); // 20R
        coralScoreLocations.put("9L", List.of(534, 98, 60)); // 19L
        coralScoreLocations.put("9R", List.of(578, 105, 60)); // 19R
        coralScoreLocations.put("10L", List.of(464, 152, 0)); // 18L
        coralScoreLocations.put("10R", List.of(464, 166, 0)); // 18R
        coralScoreLocations.put("11L", List.of(534, 199, 300)); // 17L
        coralScoreLocations.put("11R", List.of(495, 206, 300)); // 17R
        // BLUE SIDE
        coralScoreLocations.put("17L", List.of(196, 199, 300));
        coralScoreLocations.put("17R", List.of(157, 206, 300));
        coralScoreLocations.put("18L", List.of(126, 152, 0));
        coralScoreLocations.put("18R", List.of(126, 166, 0));
        coralScoreLocations.put("19L", List.of(152, 98, 60));
        coralScoreLocations.put("19R", List.of(196, 105, 60));
        coralScoreLocations.put("20L", List.of(208, 105, 120));
        coralScoreLocations.put("20R", List.of(196, 98, 120));
        coralScoreLocations.put("21L", List.of(227, 166, 180));
        coralScoreLocations.put("21R", List.of(227, 152, 180));
        coralScoreLocations.put("22L", List.of(196, 206, 240));
        coralScoreLocations.put("22R", List.of(208, 199, 240));

    }
}
