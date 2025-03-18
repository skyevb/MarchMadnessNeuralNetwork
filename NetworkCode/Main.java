import java.io.*;
import java.util.Arrays;

public class Main {
    static double error;
    static double prevError = Integer.MAX_VALUE;
    public static void main(String[] args) {
        NeuralNetwork net = new NeuralNetwork(28);
        String filePath = "/Users/skyeb/desktop/NeuralNetwork/training_data.csv";
        double[][] trainingData = new double[1319][28];
        double[] correctOutputs = new double[1319];
        FileReader fr;
        BufferedReader br;
        try {
            fr = new FileReader(filePath);
            br = new BufferedReader(fr);
            br.readLine();
            for (int i = 0; i < 1318; i++) {
                String[] thisLine = br.readLine().split(",");
                for (int j = 0; j < thisLine.length - 1; j++) {
                    trainingData[i][j] = Double.parseDouble(thisLine[j]);
                    correctOutputs[i] = Double.parseDouble(thisLine[thisLine.length - 1]);
                }
            }
            fr.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        trainingData = normalize(trainingData);
        for(double i = 0; i < 75000; i++){
            trainNetwork(net, trainingData, correctOutputs);
            if((i%750   ) == 0){
                System.out.println(((i/7500)*100) + "% complete");
                System.out.println("Error: " + prevError);
            }
        }
        testNetwork(net, trainingData, correctOutputs);
    }

    static double[][] normalize(double[][] data) {
        int numFeatures = data[0].length;
        double[] minValues = new double[numFeatures];
        double[] maxValues = new double[numFeatures];

        // Initialize min/max values
        Arrays.fill(minValues, Double.MAX_VALUE);
        Arrays.fill(maxValues, Double.MIN_VALUE);

        // Find min and max for each feature
        for (double[] row : data) {
            for (int j = 0; j < numFeatures; j++) {
                minValues[j] = Math.min(minValues[j], row[j]);
                maxValues[j] = Math.max(maxValues[j], row[j]);
            }
        }

        // Apply Min-Max Scaling
        for (double[] row : data) {
            for (int j = 0; j < numFeatures; j++) {
                if (maxValues[j] != minValues[j]) {  // Avoid division by zero
                    row[j] = (row[j] - minValues[j]) / (maxValues[j] - minValues[j]);
                } else {
                    row[j] = 0;  // If all values are the same, set to 0
                }
            }
        }
        return data;
    }

    static void testNetwork(NeuralNetwork net, double[][] testData, double[] correctOutputs){
        int correctCount = 0;
        int incorrectCount = 0;

        for(int i = 1001; i < testData.length; i++){
            if(net.output(testData[i]) >= 0.5){
                if(correctOutputs[i] == 1){
                    correctCount++;
                } else {
                    incorrectCount++;
                }
            } else {
                if(correctOutputs[i] == 0){
                    correctCount++;
                } else {
                    incorrectCount++;
                }
            }
        }
        System.out.println("Correctly guessed: " + correctCount + "\nOut of: " + (correctCount + incorrectCount));
    }

    static void trainNetwork(NeuralNetwork net, double[][] data, double[] correctOutputs) {

        File outputFile = new File("/Users/skyeb/desktop/NeuralNetProj/output.bin");
        double[][] firstLayerSaveWeights = net.firstLayer.saveTempWeights();
        double[][] secondLayerSaveWeights = net.secondLayer.saveTempWeights();
        double[][] thirdLayerSaveWeights = net.thirdLayer.saveTempWeights();
        double[] outputNeuronSaveWeights = net.outputNeuron.weights;

        net.tweakWeights();
        double totalError = 0;
        for (int i = 0; i < 1000; i++) {
            double netOutput = net.output(data[i]);
            totalError += Math.abs(correctOutputs[i] - netOutput);
        }
        if (totalError < prevError) {
            prevError = totalError;
            net.write(outputFile);
        } else {
            net.read(outputFile);
        }
    }
}
