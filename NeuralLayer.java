import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class NeuralLayer {
    Neuron[] neuronArray;
    NeuralLayer(int numInputs, int numNeurons){
        neuronArray = new Neuron[numNeurons];
        for(int i = 0; i < numNeurons; i++) {
            neuronArray[i] = new RELUNeuron(numInputs);
        }
    }

    double[] output(double[] inputs) {
        double[] retVal = new double[neuronArray.length];
        for(int i = 0; i < neuronArray.length; i++) {
            retVal[i] = neuronArray[i].output(inputs);
        }
        return retVal;
    }

    double[][] saveTempWeights(){
        double[][] retVal = new double[neuronArray.length][neuronArray[0].weights.length];
        for(int i = 0; i < neuronArray.length; i++){
            retVal[i] = neuronArray[i].weights;
        }
        return retVal;
    }

    void tweakWeights(){
        for(Neuron neu : neuronArray){
            neu.tweakWeight();
        }
    }

    void write(DataOutputStream dos) throws IOException {
        for(int i = 0; i < neuronArray.length; i++) {
            neuronArray[i].write(dos);
        }
    }
}
