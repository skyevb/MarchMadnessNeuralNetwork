import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.*;

public class Predict {
    static NeuralNetwork net = new NeuralNetwork(28);
    static File weightsFile = new File("/Users/skyeb/desktop/NeuralNetProj/output.bin");
    public static void main(String[] args){
        net.read(weightsFile);
        double[] data;
        try {
            data = getMatchupInput("/Users/skyeb/desktop/NeuralNetProj/team_profiles.csv", Integer.parseInt(args[0]), args[1], args[2]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        double prediction = net.output(data);
        if(prediction > 0.5){
            System.out.println("Predicted winner is: " + args[1]);
        } else {
            System.out.println("Predicted winner is: " + args[2]);
        }
    }



    public static double[] getMatchupInput(String filePath, int season, String team1Name, String team2Name) throws IOException {
        List<double[]> allStats = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String header = br.readLine(); // Read header
        String[] columns = header.split(",");
        int numFeatures = columns.length - 2; // Exclude Season, TeamName

        double[] team1Stats = null;
        double[] team2Stats = null;
        double[] minValues = new double[numFeatures];
        double[] maxValues = new double[numFeatures];
        Arrays.fill(minValues, Double.MAX_VALUE);
        Arrays.fill(maxValues, Double.MIN_VALUE);

        // Read data and find min/max for normalization
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            String rowTeamName = parts[0];  // Team Name comes first now
            int rowSeason = Integer.parseInt(parts[1]);  // Season is second

            double[] stats = new double[numFeatures];
            for (int i = 0; i < numFeatures; i++) {
                stats[i] = Double.parseDouble(parts[i + 2]);
                minValues[i] = Math.min(minValues[i], stats[i]);
                maxValues[i] = Math.max(maxValues[i], stats[i]);
            }

            if (rowSeason == season && rowTeamName.equals(team1Name)) {
                team1Stats = stats;
            } else if (rowSeason == season && rowTeamName.equals(team2Name)) {
                team2Stats = stats;
            }
            allStats.add(stats);
        }
        br.close();

        if (team1Stats == null || team2Stats == null) {
            throw new IllegalArgumentException("Stats not found for one or both teams in the given season.");
        }

        // Normalize the stats
        for (int i = 0; i < numFeatures; i++) {
            team1Stats[i] = (team1Stats[i] - minValues[i]) / (maxValues[i] - minValues[i]);
            team2Stats[i] = (team2Stats[i] - minValues[i]) / (maxValues[i] - minValues[i]);
        }

        // Combine into one big array [T1_Stat1, T1_Stat2, ..., T2_Stat1, T2_Stat2, ...]
        double[] input = new double[numFeatures * 2];
        System.arraycopy(team1Stats, 0, input, 0, numFeatures);
        System.arraycopy(team2Stats, 0, input, numFeatures, numFeatures);

        return input;
    }


}
