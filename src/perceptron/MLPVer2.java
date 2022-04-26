/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package perceptron;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author galle
 */
public class MLPVer2 {

    List<NeuroninVer2[]> capasNeuronas = new ArrayList<NeuroninVer2[]>();//Cada array tendria un tamaño distinto
    double error2AcumuladoDeUltimaGen = Double.MAX_VALUE;
    static final int numeroPercibido = 2;
    static final int numeroClases = 3;
    int contador_iteracion = 0;
    //double[] errorCont = new double[iterMax];

    void MostrarDiagrama() {
        int tamanoCapasNeuronas = this.capasNeuronas.size();
        int capaMasNumerosa = 0;
        System.out.println("\n");

        for (int i = 0; i < tamanoCapasNeuronas; i++) {
            if (capasNeuronas.get(i).length > capaMasNumerosa) {
                capaMasNumerosa = capasNeuronas.get(i).length;
            }
        }

        for (int i = 0; i < tamanoCapasNeuronas + 4; i++) {
            System.out.print("-");
        }
        System.out.print("\n");
        for (int fila = 0; fila < capaMasNumerosa; fila++) {
            if (fila < numeroPercibido) {
                System.out.print("D");
            } else {
                System.out.print(" ");
            }
            for (int columna = 0; columna < tamanoCapasNeuronas; columna++) {
                System.out.print("|");
                if (capasNeuronas.get(columna).length > fila) {
                    System.out.print("O");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.print("\n");
        }
        for (int columna = 0; columna < tamanoCapasNeuronas; columna++) {
            System.out.print("|" + capasNeuronas.get(columna)[0].weights.length);
        }

        System.out.println("\n");
        for (int i = 0; i < tamanoCapasNeuronas + 4; i++) {
            System.out.print("-");
        }
    }

    void ImprimirA() {
        int cantidadCapas = this.capasNeuronas.size();
        System.out.println("\nVer de lado inclinando la cabeza a la derecha, A relacionado a forward");
        for (int numeroCapaActual = 0; numeroCapaActual < cantidadCapas; numeroCapaActual++) {
            for (int numeroNeuronaActual = 0; numeroNeuronaActual < this.capasNeuronas.get(numeroCapaActual).length; numeroNeuronaActual++) {
                System.out.print(this.capasNeuronas.get(numeroCapaActual)[numeroNeuronaActual].fDeNet + "|");
            }
            System.out.print("\n");
        }
    }

    void ImprimirS() {
        int cantidadCapas = this.capasNeuronas.size();
        System.out.println("\nVer de lado inclinando la cabeza a la derecha, S relacionado a Backward");
        for (int numeroCapaActual = 0; numeroCapaActual < cantidadCapas; numeroCapaActual++) {
            for (int numeroNeuronaActual = 0; numeroNeuronaActual < this.capasNeuronas.get(numeroCapaActual).length; numeroNeuronaActual++) {
                System.out.print(this.capasNeuronas.get(numeroCapaActual)[numeroNeuronaActual].Sencibilidad + "|");
            }
            System.out.print("\n");
        }
    }

    void AgregarCapa(int numeroNeuronasEnCapa) {
        int numeroEntradasSinW0;
        int tamanoCapasNeuronas = this.capasNeuronas.size();
        if (tamanoCapasNeuronas == 0) {
            System.out.println("\nComo no hay capas, se asumen " + numeroPercibido);
            numeroEntradasSinW0 = numeroPercibido;
        } else {
            numeroEntradasSinW0 = capasNeuronas.get(tamanoCapasNeuronas - 1).length; //Una entrada por cada fDeNet, luego el constructor de neuroninVer2 añade otra para W0
        }

        capasNeuronas.add(new NeuroninVer2[numeroNeuronasEnCapa]);//Se asigna el array, pero no esta inicializado por lo que tampoco hay memoria para cada neuronin
        tamanoCapasNeuronas = this.capasNeuronas.size();

        for (int i = 0; i < numeroNeuronasEnCapa; i++) {//
            capasNeuronas.get(tamanoCapasNeuronas - 1)[i] = new NeuroninVer2(numeroEntradasSinW0);//Ahora si estan inicializados y con su pedazo de memoria. 
        }
        MostrarDiagrama();
    }

    /**
     * Especificamente para puntos double
     */
    void Forward(Point2D.Double Click_acotado) {
        //ImprimirA();

        NeuroninVer2[] CapaAnterior;
        int cantidadCapas = this.capasNeuronas.size();
        //Inicializacion de neuronas de entrada
        CapaAnterior = new NeuroninVer2[2];
        for (int i = 0; i < CapaAnterior.length; i++) {
            CapaAnterior[i] = new NeuroninVer2(0);
        }
        CapaAnterior[0].fDeNet = Click_acotado.getX();
        CapaAnterior[1].fDeNet = Click_acotado.getY();
        //Fin de inicializacion de neuronas de entrada

        for (int numeroCapaActual = 0; numeroCapaActual < cantidadCapas; numeroCapaActual++) {
            for (int numeroNeuronaActual = 0; numeroNeuronaActual < this.capasNeuronas.get(numeroCapaActual).length; numeroNeuronaActual++) {//saca de la lista de capas un array de neuronas
                this.capasNeuronas.get(numeroCapaActual)[numeroNeuronaActual].CalcularFDeNet(CapaAnterior);
            }
            CapaAnterior = this.capasNeuronas.get(numeroCapaActual);
        }

        //ImprimirA();
    }

    /**
     * Antes de hacer un Backward, por como esta el codigo, se tiene que hacer
     * un Forward
     */
    void Backward(List<Point2D.Double> puntos_tipo_2, List<Point2D.Double> puntos_tipo_1, List<Point2D.Double> puntos_tipo_0, Point2D.Double Click_acotado) {
        //ImprimirS();
        NeuroninVer2 capaSuperior[], capaActual[], vistaNeurona;
        int cantidadCapas = this.capasNeuronas.size();
        double error = 0, deseada[];
        deseada = new double[numeroClases];
        //capaSuperior = capaActual;

        capaActual = this.capasNeuronas.get(cantidadCapas - 1);

        if (puntos_tipo_0.contains(Click_acotado)) {//100
            deseada[0] = 1;
            deseada[1] = 0;
            deseada[2] = 0;
        } else if (puntos_tipo_1.contains(Click_acotado)) {//010
            deseada[0] = 0;
            deseada[1] = 1;
            deseada[2] = 0;
        } else if (puntos_tipo_2.contains(Click_acotado)) {//001
            deseada[0] = 0;
            deseada[1] = 0;
            deseada[2] = 1;
        } else {
            System.out.println("\nERROR, en el entrenamiento no se pudo clasificar un dato");
        }
        for (int numeroNeurona = 0; numeroNeurona < capaActual.length; numeroNeurona++) {
            error = deseada[numeroNeurona] - capaActual[numeroNeurona].fDeNet;
            /*if(error>=0){
                System.out.println("Error positivo");
            }else{//ge
                System.out.println("Error negativo");
            }*/
            this.error2AcumuladoDeUltimaGen += Math.pow(error, 2);
            capaActual[numeroNeurona].CalcularFPDeNet();
            capaActual[numeroNeurona].Sencibilidad = -2 * capaActual[numeroNeurona].fPDeNet * error;//-------------------Funcion critica
        }
        capaSuperior = capaActual;

        for (int numeroCapa = 1; numeroCapa < cantidadCapas; numeroCapa++) {
            capaActual = this.capasNeuronas.get(cantidadCapas - 1 - numeroCapa);
            for (int numeroNeurona = 0; numeroNeurona < capaActual.length; numeroNeurona++) {
                vistaNeurona = capaActual[numeroNeurona];
                //primero multiplicamos los pesos de cada W[i+1] por su respectiva sensibilidad en la capa superior. Se guardan en la sensibilidad de numeroNeurona capaActual
                //for (int numeroPeso = 1; numeroPeso<capaSuperior[0].weights.length;numeroPeso++)
                for (int numeroNeuronaCSuperior = 0; numeroNeuronaCSuperior < capaSuperior.length; numeroNeuronaCSuperior++) {//Se ignora el peso 0, podria ser un problema
                    vistaNeurona.Sencibilidad += capaSuperior[numeroNeuronaCSuperior].weights[numeroNeurona + 1] * capaSuperior[numeroNeuronaCSuperior].Sencibilidad;
                }
                vistaNeurona.CalcularFPDeNet();
                vistaNeurona.Sencibilidad = vistaNeurona.Sencibilidad * vistaNeurona.fPDeNet;//luego por la fPDeNet, esto deberia hacer que la matriz capaActual.length * 1 de lo que daria por multiplicar por la matriz con 0s
            }
            capaSuperior = capaActual;
        }
        //ImprimirS();
    }

    void ActualizarPesos(double learninRate, Point2D.Double Click_acotado) {
        int cantidadCapas = this.capasNeuronas.size();
        int cantidadNeuronas;
        NeuroninVer2 capaActual[], capaAnterior[];

        capaAnterior = new NeuroninVer2[2];
        for (int i = 0; i < capaAnterior.length; i++) {
            capaAnterior[i] = new NeuroninVer2(0);
        }
        capaAnterior[0].fDeNet = Click_acotado.getX();
        capaAnterior[1].fDeNet = Click_acotado.getY();

        for (int numeroCapa = 0; numeroCapa < cantidadCapas; numeroCapa++) {
            capaActual = this.capasNeuronas.get(numeroCapa);
            cantidadNeuronas = capaActual.length;

            for (int numeroNeurona = 0; numeroNeurona < cantidadNeuronas; numeroNeurona++) {
                
                capaActual[numeroNeurona].weights[0] += -learninRate * capaActual[numeroNeurona].Sencibilidad * -1;
                /*if (capaActual[numeroNeurona].weights[0]>1){
                    System.out.println("W0: "+capaActual[numeroNeurona].weights[0]+" Se pasó de 1 con -"+learninRate+"*"+capaActual[numeroNeurona].Sencibilidad+"*"+-1+" Que da un incremento de "+-learninRate*capaActual[numeroNeurona].Sencibilidad*-1);
                }*/
                for (int numeroPeso = 1; numeroPeso < capaActual[0].weights.length; numeroPeso++) {//En teoria cualquier neurona de la capa tiene misma cant de pesos
                    //System.out.println("nN: "+numeroNeurona+" nP: "+numeroPeso);
                    capaActual[numeroNeurona].PrevEp[numeroPeso] = capaActual[numeroNeurona].Sencibilidad * capaAnterior[numeroPeso - 1].fDeNet;
                    capaActual[numeroNeurona].DeltaWeinght[numeroPeso] = -learninRate * capaActual[numeroNeurona].PrevEp[numeroPeso];
                    capaActual[numeroNeurona].weights[numeroPeso] += capaActual[numeroNeurona].DeltaWeinght[numeroPeso];
                }
            }
            capaAnterior = capaActual;
        }
    }

    void Debug_MLP() {
        System.out.println("Numero de capas ocultas: " + (this.capasNeuronas.size() - 1));

        for (int capa = 0; capa < this.capasNeuronas.size(); capa++) {//or
            System.out.println("    Capa oculta: " + capa);
            for (int neurona = 0; neurona < capasNeuronas.get(capa).length; neurona++) {
                System.out.println("Neuronin: " + neurona);
                capasNeuronas.get(capa)[neurona].Debug_Neuronin();
            }
        }

    }
    
    
    void ActualizarDeltaPesos(double learninRate, Point2D.Double Click_acotado) {
        int cantidadCapas = this.capasNeuronas.size();
        int cantidadNeuronas;
        NeuroninVer2 capaActual[], capaAnterior[];

        capaAnterior = new NeuroninVer2[2];
        for (int i = 0; i < capaAnterior.length; i++) {
            capaAnterior[i] = new NeuroninVer2(0);
        }
        capaAnterior[0].fDeNet = Click_acotado.getX();
        capaAnterior[1].fDeNet = Click_acotado.getY();

        for (int numeroCapa = 0; numeroCapa < cantidadCapas; numeroCapa++) {
            capaActual = this.capasNeuronas.get(numeroCapa);
            cantidadNeuronas = capaActual.length;

            for (int numeroNeurona = 0; numeroNeurona < cantidadNeuronas; numeroNeurona++) {
                capaActual[numeroNeurona].DeltaWeinght[0] += -learninRate * capaActual[numeroNeurona].Sencibilidad * -1;
                /*if (capaActual[numeroNeurona].weights[0]>1){
                    System.out.println("W0: "+capaActual[numeroNeurona].weights[0]+" Se pasó de 1 con -"+learninRate+"*"+capaActual[numeroNeurona].Sencibilidad+"*"+-1+" Que da un incremento de "+-learninRate*capaActual[numeroNeurona].Sencibilidad*-1);
                }*/
                for (int numeroPeso = 1; numeroPeso < capaActual[0].DeltaWeinght.length; numeroPeso++) {//En teoria cualquier neurona de la capa tiene misma cant de pesos
                    //System.out.println("nN: "+numeroNeurona+" nP: "+numeroPeso);
                    capaActual[numeroNeurona].DeltaWeinght[numeroPeso] += -learninRate * capaActual[numeroNeurona].Sencibilidad * capaAnterior[numeroPeso - 1].fDeNet;
                }
            }
            capaAnterior = capaActual;
        }
    }
    /**
     Lo que este en la variable delta weinght se promedia y se aplica a la cada neurona. Tambien se regresa el deltaWeinght a 0 preparandolo para la proxima gen
     */
    void AplicarDeltaPeso(int tamanoMuestras){
        int cantidadCapas = this.capasNeuronas.size();
        int cantidadNeuronas;
        NeuroninVer2 capaActual[];
        double tamanoDePoblacion = (double)tamanoMuestras;
        for (int numeroCapa = 0; numeroCapa < cantidadCapas; numeroCapa++) {
            capaActual = this.capasNeuronas.get(numeroCapa);
            cantidadNeuronas = capaActual.length;
            for (int numeroNeurona = 0; numeroNeurona < cantidadNeuronas; numeroNeurona++) {
                for (int numeroPeso = 0; numeroPeso < capaActual[0].DeltaWeinght.length; numeroPeso++) {//En teoria cualquier neurona de la capa tiene misma cant de pesos
                    capaActual[numeroNeurona].weights[numeroPeso] += capaActual[numeroNeurona].DeltaWeinght[numeroPeso]/tamanoDePoblacion;
                    capaActual[numeroNeurona].DeltaWeinght[numeroPeso] = 0;
                }
            }
        }
    }
    double Eprima(double sencibilidad, double entradaDelPeso){
        return sencibilidad*entradaDelPeso;
    }
    int ActualizarPesosQuick(double learninRate, Point2D.Double Click_acotado) {
        int salida = 0;
        int cantidadCapas = this.capasNeuronas.size();
        int cantidadNeuronas;
        NeuroninVer2 capaActual[], capaAnterior[];
        double Ep,temporalDiv;

        capaAnterior = new NeuroninVer2[2];
        for (int i = 0; i < capaAnterior.length; i++) {
            capaAnterior[i] = new NeuroninVer2(0);
        }
        capaAnterior[0].fDeNet = Click_acotado.getX();
        capaAnterior[1].fDeNet = Click_acotado.getY();

        for (int numeroCapa = 0; numeroCapa < cantidadCapas; numeroCapa++) {
            capaActual = this.capasNeuronas.get(numeroCapa);
            cantidadNeuronas = capaActual.length;

            for (int numeroNeurona = 0; numeroNeurona < cantidadNeuronas; numeroNeurona++) {
                capaActual[numeroNeurona].weights[0] += -learninRate * capaActual[numeroNeurona].Sencibilidad * -1;
                /*if (capaActual[numeroNeurona].weights[0]>1){
                    System.out.println("W0: "+capaActual[numeroNeurona].weights[0]+" Se pasó de 1 con -"+learninRate+"*"+capaActual[numeroNeurona].Sencibilidad+"*"+-1+" Que da un incremento de "+-learninRate*capaActual[numeroNeurona].Sencibilidad*-1);
                }*/
                for (int numeroPeso = 1; numeroPeso < capaActual[0].weights.length; numeroPeso++) {//En teoria cualquier neurona de la capa tiene misma cant de pesos
                    //capaActual[numeroNeurona].weights[numeroPeso] += -learninRate * capaActual[numeroNeurona].Sencibilidad * capaAnterior[numeroPeso - 1].fDeNet;
                    
                    Ep = Eprima(capaActual[numeroNeurona].Sencibilidad, capaAnterior[numeroPeso - 1].fDeNet);
                    temporalDiv = capaActual[numeroNeurona].PrevEp[numeroPeso]-Ep;
                    if (temporalDiv!=0){
                    capaActual[numeroNeurona].DeltaWeinght[numeroPeso] = (Ep*capaActual[numeroNeurona].DeltaWeinght[numeroPeso])/temporalDiv;
                    capaActual[numeroNeurona].weights[numeroPeso] += capaActual[numeroNeurona].DeltaWeinght[numeroPeso];
                    capaActual[numeroNeurona].PrevEp[numeroPeso] = Ep;
                    }else{
                        ActualizarPesos(learninRate, Click_acotado);
                        salida = 1;//indica la div 0
                        //System.out.println("Divicion entre 0, no se actualiza deltaW");
                    
                    }
                    
                    
                    
                }
            }
            capaAnterior = capaActual;
        }
        return salida;
    }
    
    void EntrenarRedLote(List<Point2D.Double> puntos_tipo_2, List<Point2D.Double> puntos_tipo_1, List<Point2D.Double> puntos_tipo_0, List<Point2D.Double> Click_acotados, int maximasGeneraciones, double error2MaximoAcumulado, double learningRate) {
        int generacionActual = 0;
        Point2D.Double puntoActual;
        double errorTotaldeGeneraciones[] = new double[maximasGeneraciones];
        while (generacionActual < maximasGeneraciones && this.error2AcumuladoDeUltimaGen > error2MaximoAcumulado) {
            this.error2AcumuladoDeUltimaGen = 0;
            for (int numeroClick = 0; numeroClick < Click_acotados.size(); numeroClick++) {
                puntoActual = Click_acotados.get(numeroClick);
                this.Forward(puntoActual);
                this.Backward(puntos_tipo_2, puntos_tipo_1, puntos_tipo_0, puntoActual);//aqui ya se actualizo el error2acumulado a el correspondiente a esta generacion
                this.ActualizarDeltaPesos(learningRate, puntoActual);
                //Calcular los cambios de peso que haria cada punto
            }
            this.error2AcumuladoDeUltimaGen = this.error2AcumuladoDeUltimaGen/Click_acotados.size();
                //Actualizacion de pesos, se usa el size para sacar el promedio, tambien se restablecen a 0 los acumulados
                this.AplicarDeltaPeso(Click_acotados.size());
            System.out.println("\nFin de la generacion: " + generacionActual + " | Con un error2 de: " + this.error2AcumuladoDeUltimaGen);
            //DIBUJAR GRAFICA AL FINAL
            errorTotaldeGeneraciones[generacionActual] = error2AcumuladoDeUltimaGen;
            //errorGraficaArray(generacionActual, error2AcumuladoDeUltimaGen);
            
            //DIBUJAR EN CADA GENERACION
            generacionActual++;
        }
        //ESTA GRAFICA DIBUJA AL FINAL DE TODAS LAS ITERACIONES
        errorGrafica(generacionActual, errorTotaldeGeneraciones);
    }
    void EntrenarRedEstocastico(List<Point2D.Double> puntos_tipo_2, List<Point2D.Double> puntos_tipo_1, List<Point2D.Double> puntos_tipo_0, List<Point2D.Double> Click_acotados, int maximasGeneraciones, double error2MaximoAcumulado, double learningRate) {
        int generacionActual = 0;
        Point2D.Double puntoActual;
        double errorTotaldeGeneraciones[] = new double[maximasGeneraciones];
        while (generacionActual < maximasGeneraciones && this.error2AcumuladoDeUltimaGen > error2MaximoAcumulado) {
            this.error2AcumuladoDeUltimaGen = 0;
            for (int numeroClick = 0; numeroClick < Click_acotados.size(); numeroClick++) {
                puntoActual = Click_acotados.get(numeroClick);
                this.Forward(puntoActual);
                this.Backward(puntos_tipo_2, puntos_tipo_1, puntos_tipo_0, puntoActual);//aqui ya se actualizo el error2acumulado a el correspondiente a esta generacion
                this.ActualizarPesos(learningRate, puntoActual);

                //Actualizacion de pesos    
            }
            this.error2AcumuladoDeUltimaGen = this.error2AcumuladoDeUltimaGen/Click_acotados.size();
            System.out.println("\nFin de la generacion: " + generacionActual + " | Con un error2 de: " + this.error2AcumuladoDeUltimaGen);
            //DIBUJAR GRAFICA AL FINAL
            errorTotaldeGeneraciones[generacionActual] = error2AcumuladoDeUltimaGen;
            //errorGraficaArray(generacionActual, error2AcumuladoDeUltimaGen);
            
            //DIBUJAR EN CADA GENERACION
            generacionActual++;
        }
        //ESTA GRAFICA DIBUJA AL FINAL DE TODAS LAS ITERACIONES
        errorGrafica(generacionActual, errorTotaldeGeneraciones);
    }
void EntrenarRedQuick(List<Point2D.Double> puntos_tipo_2, List<Point2D.Double> puntos_tipo_1, List<Point2D.Double> puntos_tipo_0, List<Point2D.Double> Click_acotados, int maximasGeneraciones, double error2MaximoAcumulado, double learningRate) {
        int generacionActual = 0;
        Point2D.Double puntoActual;
        double errorTotaldeGeneraciones[] = new double[maximasGeneraciones];
        while (generacionActual < maximasGeneraciones && this.error2AcumuladoDeUltimaGen > error2MaximoAcumulado) {
            this.error2AcumuladoDeUltimaGen = 0;
            for (int numeroClick = 0; numeroClick < Click_acotados.size(); numeroClick++) {
                puntoActual = Click_acotados.get(numeroClick);
                this.Forward(puntoActual);
                this.Backward(puntos_tipo_2, puntos_tipo_1, puntos_tipo_0, puntoActual);//aqui ya se actualizo el error2acumulado a el correspondiente a esta generacion
                this.ActualizarPesosQuick(learningRate, puntoActual);

                //Actualizacion de pesos    
            }
            this.error2AcumuladoDeUltimaGen = this.error2AcumuladoDeUltimaGen/Click_acotados.size();
            System.out.println("\nFin de la generacion: " + generacionActual + " | Con un error2 de: " + this.error2AcumuladoDeUltimaGen);
            //DIBUJAR GRAFICA AL FINAL
            errorTotaldeGeneraciones[generacionActual] = error2AcumuladoDeUltimaGen;
            //errorGraficaArray(generacionActual, error2AcumuladoDeUltimaGen);
            
            //DIBUJAR EN CADA GENERACION
            generacionActual++;
        }
        //ESTA GRAFICA DIBUJA AL FINAL DE TODAS LAS ITERACIONES
        errorGrafica(generacionActual, errorTotaldeGeneraciones);
    }
    void Colorear(Point2D.Double Click_acotado, JPanel El_panel) {
        double colores[];
        Graphics g;
        g = El_panel.getGraphics();
        int cantidadCapas = this.capasNeuronas.size();
        colores = new double[this.capasNeuronas.get(cantidadCapas - 1).length];
        if (colores.length != 3) {
            System.out.println("la capa salida no es adecuada para este metodo");
        } else {
            Forward(Click_acotado);
            NeuroninVer2 capaSalida[] = this.capasNeuronas.get(cantidadCapas - 1);
            for (int BRG = 0; BRG < 3; BRG++) {
                colores[BRG] = capaSalida[BRG].fDeNet;
            }
            //colores = Forward(Click_acotado);
            Color color1 = new Color((int) (200 * colores[1]), (int) (200 * colores[2]), (int) (200 * colores[0]));
            g.setColor(color1);
            g.fillOval((int) (Click_acotado.getX() * 250 + 250) - 2, (int) (Click_acotado.getY() * 250 + 250) - 2, 5, 5);
        }
    }

    void ColorearSolido(Point2D.Double Click_acotado, JPanel El_panel) {
        double colores[];
        Graphics g;
        g = El_panel.getGraphics();
        int cantidadCapas = this.capasNeuronas.size();
        colores = new double[this.capasNeuronas.get(cantidadCapas - 1).length];
        if (colores.length != 3) {
            System.out.println("la capa salida no es adecuada para este metodo");
        } else {
            Forward(Click_acotado);
            NeuroninVer2 capaSalida[] = this.capasNeuronas.get(cantidadCapas - 1);
            double max = 0;
            int indexMax = 0;
            for (int BRG = 0; BRG < 3; BRG++) {
                if (max < capaSalida[BRG].fDeNet) {
                    max = capaSalida[BRG].fDeNet;
                    indexMax = BRG;
                }
                colores[BRG] = 0;
            }
            colores[indexMax] = 1;
            //colores = Forward(Click_acotado);
            Color color1 = new Color((int) (255 * colores[1]), (int) (255 * colores[2]), (int) (255 * colores[0]));
            g.setColor(color1);
            g.fillOval((int) (Click_acotado.getX() * 250 + 250) - 2, (int) (Click_acotado.getY() * 250 + 250) - 2, 5, 5);
        }
    }

    public void errorGraficaArray(int contador_iteracion, double errorCont) {
        JFreeChart grafico = null;
        DefaultCategoryDataset datos = new DefaultCategoryDataset();

        //for (int i = 0; i < contador_iteracion; i++) {
        datos.addValue(errorCont, "Error", String.valueOf(contador_iteracion));
        //}
        grafico = ChartFactory.createLineChart("ERRORES", "", "", datos, PlotOrientation.VERTICAL, true, true, false);
        ChartPanel cPanel = new ChartPanel(grafico);
        JFrame informacion = new JFrame("Grafica");
        informacion.getContentPane().add(cPanel);
        informacion.pack();
        informacion.setVisible(true);
    }

    public void errorGrafica(int contador_iteracion, double[] errorCont) {
        JFreeChart grafico = null;
        DefaultCategoryDataset datos = new DefaultCategoryDataset();

        for (int i = 0; i < contador_iteracion; i++) {
            datos.addValue(errorCont[i], "Error", String.valueOf(i));
        }
        grafico = ChartFactory.createLineChart("ERRORES", "", "", datos, PlotOrientation.VERTICAL, true, true, false);
        ChartPanel cPanel = new ChartPanel(grafico);
        JFrame informacion = new JFrame("Grafica");
        informacion.getContentPane().add(cPanel);
        informacion.pack();
        informacion.setVisible(true);
    }

    /*public void prueba() {
        JFreeChart grafico = null;
        DefaultCategoryDataset datos = new DefaultCategoryDataset();
        ChartPanel cPanel = new ChartPanel(grafico);
        JFrame informacion = new JFrame("Grafica");
        for (int i = 0; i < 30; i++) {
            datos.addValue(i, "Error", String.valueOf(i));
            grafico = ChartFactory.createLineChart("ERRORES", "", "", datos, PlotOrientation.VERTICAL, true, true, false);
            informacion.getContentPane().add(cPanel);
            informacion.pack();
            informacion.setVisible(true);
        }

    }*/


}//Fin de clase, or
