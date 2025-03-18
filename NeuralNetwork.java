import java.io.*;

public class NeuralNetwork {
    NeuralLayer firstLayer;
    NeuralLayer secondLayer;
    NeuralLayer thirdLayer;
    Neuron outputNeuron;

    NeuralNetwork(int inputs){
        firstLayer = new NeuralLayer(inputs, 1000);
        secondLayer = new NeuralLayer(1000, 100);
        thirdLayer = new NeuralLayer(100, 10);
        outputNeuron = new LogisticNeuron(10);
    }

    void tweakWeights(){
        firstLayer.tweakWeights();
        secondLayer.tweakWeights();
        thirdLayer.tweakWeights();
        outputNeuron.tweakWeight();
    }

    double output(double[] input){
        return outputNeuron.output(thirdLayer.output(secondLayer.output(firstLayer.output(input))));

    }

    void write(File file) {//writes all neuron weights to given file
        try {
            FileOutputStream fos = new FileOutputStream(file);
            DataOutputStream dos = new DataOutputStream(fos);
            firstLayer.write(dos);
            secondLayer.write(dos);
            thirdLayer.write(dos);
            outputNeuron.write(dos);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    void read(File file) {//sets all neurons in network to weights in given file
        DataInputStream dis = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            dis = new DataInputStream(fis);

            for(int i = 0; i<firstLayer.neuronArray.length; i ++) { //reading for first layer
                firstLayer.neuronArray[i].read(dis);
            }

            for(int i = 0; i<secondLayer.neuronArray.length; i ++) { //reading for second layer
                secondLayer.neuronArray[i].read(dis);
            }

            for(int i = 0; i<thirdLayer.neuronArray.length; i ++) { //reading for third layer
                thirdLayer.neuronArray[i].read(dis);
            }

            for (int j = 0; j < outputNeuron.weights.length; j++) {
                outputNeuron.weights[j] = dis.readDouble();
            }
            dis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
