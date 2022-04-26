/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package perceptron;

/**
 *
 * @author galle
 */
public class Neuronin {
    double W[];
    //double A[];
    
    Neuronin(int Numero_entradas){
        double Pesos[] = new double[Numero_entradas+1];//Aqui se añade el weight
        for (int i = 0; i<Pesos.length; i++)
        {
            Pesos[i] = Math.random()/2-.25;
        }
        this.W = Pesos;
    }
    /*boolean mismoSize(double a[],double b[]){
        return a.length==b.length;
    }*/
    double NetNeuronin(double[] As){
        //-(W[0] * -1 + W[1] * coordenadas.getX() + W[2] * coordenadas.getY())   
        double salidaNet = 0;
        //Aqui se contempla la entrada fantasma especificamente para el peso 0
        salidaNet = W[0]*-1;
        for(int i = 1; i<W.length; i++){
            salidaNet += W[i]*As[i-1];
        }
        System.out.println("La net da: "+salidaNet);
        return salidaNet;
    }
    
    double Resultado_neuronin(double[] As){
        //Te creas, la entrada fantasma esta en NetNeuronin, luego por que tardamos tanto encontrando errores
        double resultado;
        resultado= 1 / (1 + Math.exp(-(NetNeuronin(As))));
        
        /*if (NetNeuronin(As)>=0) {
            resultado = 1;
        } else {
            resultado = 0;
        }*/
        
        
        
        System.out.println("La evaluacion f da: "+resultado);
        return resultado;
    }
    double Derivada_resultado_neuronin(double[] As){
        //Te creas, la entrada fantasma esta en NetNeuronin, luego por que tardamos tanto encontrando errores
        double resultado;
        double net = NetNeuronin(As);
        resultado = net * (1.0 - net);
        
        /*if (NetNeuronin(As)>=0) {
            resultado = 1;
        } else {
            resultado = 0;
        }*/
        
        
        
        System.out.println("La evaluacion f' da: "+resultado);
        return resultado;
    }
    void Debug_Neuronin(){
        for (int i = 0; i<this.W.length; i++){
            System.out.println("W["+i+"] es: "+W[i]);
        }
    }
    
    
    
}
