/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package perceptron;

/**
 *
 * @author galle
 */
public class NeuroninVer2 {
    double fDeNet,net,weights[],fPDeNet,Sencibilidad=0,DeltaWeinght[],PrevEp[];
    //MLP familia;
    
    /** 
     Este metodo no es muy util por si mismo, requiere ser interpretado por un FDeNet
     */
    void CalcularNet(NeuroninVer2[] capaAnterior){
        this.net = this.weights[0]*-1;
        for (int numeroNeurona = 0; numeroNeurona<capaAnterior.length; numeroNeurona++){
            this.net += this.weights[numeroNeurona+1]*capaAnterior[numeroNeurona].fDeNet;
        }
    }
    void CalcularFDeNet(NeuroninVer2[] capaAnterior){
        CalcularNet(capaAnterior);
        this.fDeNet = 1 / (1 + Math.exp(-net));
        if (fDeNet>1||fDeNet<0){
        System.out.println("Error critico, hay una fPDeNet mayor de .999 o menor de 0");
        }
    }
    void Debug_Neuronin(){
        for (int i = 0; i<this.weights.length; i++){
            System.out.println("W["+i+"] es: "+weights[i]);
        }
        System.out.println("fDeNet: "+fDeNet);
    }
    /**
     Es importante haber calculado FDeNet antes que este metodo
     */
    void CalcularFPDeNet(){
    this.fPDeNet = this.fDeNet*(1-this.fDeNet);
    if (fPDeNet>.3||fPDeNet<0){
        System.out.println("Error critico, hay una fPDeNet mayor de .3 o menor de 0");
    }
    }
    NeuroninVer2(int Numero_entradas){
        //familia = padre;
        double Pesos[] = new double[Numero_entradas+1];//Aquí se añade W0, el limite que se estimula con -1
        for (int i = 0; i<Pesos.length; i++)
        {
            Pesos[i] = Math.random()/2;
        }
        this.weights = Pesos;
        this.DeltaWeinght = new double[weights.length];
        this.PrevEp = new double[weights.length];
    }
    
    
    
    
    
}
