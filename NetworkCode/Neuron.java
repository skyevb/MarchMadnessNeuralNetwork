//File Name: Neuron.java
//Creates abstract class Neuron, and child classes RELUNeuron and LogisticNeuron
//@author Skye Bertain
import java.io.*;
import java.util.Random;

abstract class Neuron {
    double[] weights;
    int inputs;

    Neuron(int argInputs) {//sets all weights to random double -1 to 1 and bias as well
        inputs = argInputs;
        weights = new double[inputs];
        Random rand = new Random();
        for (int i = 0; i < weights.length; i++) { weights[i] = -1.0 + (rand.nextDouble() * 2.0);}
        weights[inputs - 1] = -1.0 + (rand.nextDouble() * 2.0);
    }


    Neuron(File file, int argInputs) throws FileNotFoundException { //second constructor to create neuron from weights file
        FileInputStream fis = new FileInputStream(file);
        DataInputStream dis = new DataInputStream(fis);
        inputs = argInputs;
        try{this.read(dis);}
        catch (Exception e) {e.printStackTrace();}
    }

    void read(DataInputStream dis) throws FileNotFoundException, IOException{//makes a new array of 500 items, initialize data input stream, read the weights from file and assign them to the array, retrieves the weights from the file, and assigns the weights to these weights
        weights = new double[inputs];
        for(int i = 0; i<inputs; i ++) {
            this.weights[i] = dis.readDouble();
        }
    }

    abstract double activation(double x);

    double output(double[] inputs) { //output function takes in an array out inputs, goes through each one, multiplies it by its respective weight, adds the bias, then returns the activation of that sum
        double retVal = 0;
        for (int i = 0; i < inputs.length - 1; i ++) {retVal += weights[i]*inputs[i];}
        retVal += weights[weights.length - 1];

        return activation(retVal);
    }

    void write(DataOutputStream dos) throws IOException{ //for every weight, write the weight to the data output stream, then write the bias
        //System.out.println("Writing weights to file!");
        for (int i = 0; i < weights.length; i ++) {
            dos.writeDouble(weights[i]);
        }
    }

    void tweakWeight() {
        Random rand = new Random();
        int randIndex = rand.nextInt(weights.length);
        double randomValue = -0.1 + (rand.nextDouble() * 0.2);
        this.weights[randIndex] += randomValue;
    }
}

class RELUNeuron extends Neuron{
    RELUNeuron(int argInputs){super(argInputs);}
    RELUNeuron(File file, int argInputs) throws FileNotFoundException{super(file, argInputs);}
    double activation(double x) {
        x /= 20.0;
        return x > 0 ? x : 0.0;
    }
}

class LogisticNeuron extends Neuron{
    LogisticNeuron(int inputs){super(inputs);}
    LogisticNeuron(File file, int argInputs) throws FileNotFoundException{super(file, argInputs);}
    double activation(double input) {
        double expon = Math.exp(input * 4.0);
        return expon / (1.0 + expon);
    }
}
