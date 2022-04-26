/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package perceptron;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author galle
 */
public class MLP {
    
    Neuronin Capas_ocultas[][];
    Neuronin Capa_salida[];
    int CantidadNeuronasPcapa, CantidadDeocultas;
    
    public static double[][] multiply(double[][] a, double[][] b) {
    double[][] c = new double[a.length][b[0].length];
    // se comprueba si las matrices se pueden multiplicar
    if (a[0].length == b.length) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                for (int k = 0; k < a[0].length; k++) {
                    // aquí se multiplica la matriz
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }
    }
    /**
     * si no se cumple la condición se retorna una matriz vacía
     */
    return c;
}
    
    
    
    MLP(int Ccapas_ocultas, int Cneuronas_Pcapa){//Para determinar los parametros de las matrices
        Capa_salida = new Neuronin[3];
        Capas_ocultas = new Neuronin[Ccapas_ocultas][Cneuronas_Pcapa];
        //Para facilitar ciclos despues
        CantidadDeocultas = Ccapas_ocultas;
        CantidadNeuronasPcapa = Cneuronas_Pcapa;
        //Aqui cambiaria si no fueran puntos X,Y
        if (CantidadDeocultas > 0)
        {
            for (int neurona = 0; neurona<CantidadNeuronasPcapa;neurona++){
                //0 por la primera oculta, 2 por la cantidad de entradas que son coord X,Y
                Capas_ocultas[0][neurona]= new Neuronin(2);
            }
            //esto son las ocultas, la primera se evalua primero con tamaño segun entrada
            for (int capa = 1; capa<CantidadDeocultas; capa++){
                for (int neurona = 0; neurona<CantidadNeuronasPcapa;neurona++){
                    Capas_ocultas[capa][neurona]= new Neuronin(CantidadNeuronasPcapa);
                }
            }    
            //luego se inicializa la salida
            for (int neurona = 0; neurona<3;neurona++){
                //0 por la primera oculta, 2 por la cantidad de entradas que son coord X,Y
                Capa_salida[neurona]= new Neuronin(CantidadNeuronasPcapa);
            }
        }
        else{
            for (int neurona = 0; neurona<3;neurona++){
                //0 por la primera oculta, 2 por la cantidad de entradas que son coord X,Y
                System.out.println("Sin capas ocultas se ignora la cantidad de neuronas por capa");
                Capa_salida[neurona]= new Neuronin(2);
            }
        }
    }
    
    void Debug_MLP(){
        System.out.println("Numero de capas ocultas: "+ CantidadDeocultas);
        System.out.println("Las capas ocultas tienen: "+ CantidadNeuronasPcapa + " neuronas por capa");
        for (int capa = 0; capa<CantidadDeocultas; capa++){
            System.out.println("    Capa oculta: "+capa);
            for (int neurona = 0; neurona<CantidadNeuronasPcapa;neurona++){
                System.out.println("Neuronin: "+neurona);
                Capas_ocultas[capa][neurona].Debug_Neuronin();
            }
        }
        System.out.println("    Capa salida ");
            for (int neurona = 0; neurona<3;neurona++){
                System.out.println("Neuronin: "+neurona);
                Capa_salida[neurona].Debug_Neuronin();
            }
    }
    
    double[] Calcular_A(Neuronin[] laCapa, double AsAnterior[]){
        //en neuronas ocultas, laCapa.lenght termina siendo igual que this.CantidadNeuronasPcapa
        double A[] = new double[laCapa.length];
        //la fantasma se introduce en Resultado_neuronin
        for (int i = 0; i < A.length; i++){
            A[i]=laCapa[i].Resultado_neuronin(AsAnterior);
        }
        
        return A;
    } 
    
    //sin terminar
    double[] Calcular_Fderivada(Neuronin[] laCapa, double AsAnterior[]){
        //en neuronas ocultas, laCapa.lenght termina siendo igual que this.CantidadNeuronasPcapa
        double Salida[] = new double[laCapa.length];
        //la fantasma se introduce en Resultado_neuronin
        for (int i = 0; i < Salida.length; i++){
            Salida[i]=(laCapa[i].Derivada_resultado_neuronin(AsAnterior));
        }
        return Salida;
    } 
    
    double[] Clasificar (Point2D.Double Click_acotado){
        double Respuesta[] = new double[Capa_salida.length];
        
        double primerA[] = new double[2];
        primerA[0] = Click_acotado.getX();
        primerA[1] = Click_acotado.getY();
        if (CantidadDeocultas > 0)
        {
            double a_CapaAnterior[] = new double[this.CantidadNeuronasPcapa];
            a_CapaAnterior = Calcular_A(Capas_ocultas[0], primerA);
            for(int capa = 1; capa < CantidadDeocultas; capa++){
            a_CapaAnterior = Calcular_A(Capas_ocultas[capa], a_CapaAnterior);
            }
            Respuesta = Calcular_A(Capa_salida, a_CapaAnterior);
        }
        else
        {
            Respuesta = Calcular_A(Capa_salida, primerA);
        }
        System.out.println("\n\n  Resultaods de la capa de salida\n"+Respuesta[0]+" "+Respuesta[1]+" "+Respuesta[2]+" Con lengt: "+Respuesta.length);
        return Respuesta;
    }
    
    void Colorear(Point2D.Double Click_acotado, JPanel El_panel){
        double colores[];
        Graphics g;
        g = El_panel.getGraphics();
        colores = new double[Capa_salida.length];
        if(colores.length != 3){
            System.out.println("la capa salida no es adecuada para este metodo");
        }else{
            colores = Clasificar(Click_acotado);
            Color color1 = new Color((int)(255*colores[1]),(int)(255*colores[2]),(int)(255*colores[0]));
            g.setColor(color1);
            g.fillOval((int) (Click_acotado.getX() * 250 + 250) - 2, (int) (Click_acotado.getY() * 250 + 250) - 2, 5, 5);
        }
    }
    
    
    double[][] meterOs(double Calculadas[]){
        double respuesta[][] = new double[Calculadas.length][Calculadas.length];
        for (int x = 0; x < Calculadas.length; x++){
            for (int y = 0; y < Calculadas.length; y++){
                if (x==y){
                    respuesta[x][y] = Calculadas[x];
                }else{
                    respuesta[x][y] = 0;
                }
            }
        }
        return respuesta;
    }
    
    void Entrenar(List<Point2D.Double> puntos_tipo_2, List<Point2D.Double> puntos_tipo_1, List<Point2D.Double> puntos_tipo_0, List<Point2D.Double> clicks, JTextField learning_rateTXT, JTextField iterationsTXT, JPanel JPanel_principal, JTextField jTextField_Error_cuadratico){
        int generacionesMax = Integer.parseInt(iterationsTXT.getText());
        double errorMin = Double.parseDouble(jTextField_Error_cuadratico.getText());
        double learningRate = Double.parseDouble(learning_rateTXT.getText());
        
        
        
        double primerA[] = new double[2];
        double InternasA[][] = new double[this.CantidadDeocultas][this.CantidadNeuronasPcapa];
        //Esto se podria sacar del metodo clasificar vvv, pero calcularlo aqui ahorraria redudandia de calculo de A 
        double salidaA[] = new double[3];
        
        double deseada[] = {0,0,0};
        double salidaS[] = new double[3];
        double InternasS[][] = new double[this.CantidadDeocultas][this.CantidadNeuronasPcapa];
        double WsinWO[] = new double[this.CantidadNeuronasPcapa];
        double InternasS_antesTransformacion[][] = new double[this.CantidadDeocultas][this.CantidadNeuronasPcapa];
        double InternasSEnmatrizO[][] = new double[this.CantidadNeuronasPcapa][this.CantidadNeuronasPcapa];
        int capaActual;
        //recordatorio de no modificar ningun peso hasta calcular todas las matrices que se usaran para el ajuste
        int generacionActual = 0;
        while (generacionActual<generacionesMax){
            System.out.println("\nGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG La generacion actual es: "+generacionActual);
            for(int numeroPunto = 0; numeroPunto < clicks.size(); numeroPunto++){
                Point2D.Double puntoActual = clicks.get(numeroPunto);
                //Aqui empieza el calculo de las A
                primerA[0] = puntoActual.getX();
                primerA[1] = puntoActual.getY();
                //el +1 es para que se pase y detecte en un if de abajo que toca la salida
                for(int numeroCapaOculta = 0; numeroCapaOculta<this.CantidadDeocultas+1; numeroCapaOculta++){
                    if (numeroCapaOculta == 0){//La primer oculta tiene entrada diferente
                        //obtiene un array tamano this.CantidadNeuronasPcapa, pero la primer capa oculta no tiene ese numero de entradas, sino que tiene 2 (la fantasma se introduce en Resultado_neuronin)
                        InternasA[numeroCapaOculta] = Calcular_A(this.Capas_ocultas[numeroCapaOculta], primerA);
                    }else{
                        if(numeroCapaOculta == this.CantidadDeocultas){//para cuando se llega a la capa de salida, la entrada no es diferente pero la salida y neuronas usadas si
                            salidaA = Calcular_A(this.Capa_salida,InternasA[numeroCapaOculta-1]);
                        }else{//las capas internas nienen las mismas entradas que salidas, pero solo se entra a este si hay 2 o más
                          //usa la capa actual, pero las salidas de la anterior como entrada
                            System.out.println("UNA CAPA INTERNA GENERICA (solo deberia salir si hay 2 o más ocultas");
                          InternasA[numeroCapaOculta] = Calcular_A(this.Capas_ocultas[numeroCapaOculta], InternasA[numeroCapaOculta-1]);   
                        }  
                    }
                }
                //Aqui termina el calculo de las A
                
                //Aqui empieza el calculo de las S, sensibilidades
                
                
                if (puntos_tipo_0.contains(puntoActual)) {//100
                    deseada[0]=1;
                    deseada[1]=0;
                    deseada[2]=0;
                }else
                    if (puntos_tipo_1.contains(puntoActual)) {//010
                        deseada[0]=0;
                        deseada[1]=1;
                        deseada[2]=0;
                    }else
                        if (puntos_tipo_2.contains(puntoActual)) {//001
                        deseada[0]=0;
                        deseada[1]=0;
                        deseada[2]=1;
                        }else{
                            System.out.println("\nERROR, en el entrenamiento no se pudo clasificar un dato");
                            }
                
                
                
                    //esto son las s de la capa de salida
                salidaS = Calcular_Fderivada(this.Capa_salida, InternasA[InternasA.length-1]);//la ultima a que no es de salida es la entrada de la salida
                //distinta configuracion de fors, por que para esta no se ha implementado un metodo parecido a Calcular_A
                for (int numeroNeurona = 0; numeroNeurona<salidaS.length;numeroNeurona++){
                    salidaS[numeroNeurona] = salidaS[numeroNeurona]*2*(deseada[numeroNeurona]-salidaA[numeroNeurona]);
                }
                   //estp dejo de ser las s de la capa de salida
                   //esto es la sencibilidad de las capas ocultas
                  
                  //primer oculta
                if (this.CantidadDeocultas>0)
                {
                   
                  
                  
                    System.out.println("\nEstas son ya en capas ocultas");
                    for (int NumeroRetrocedido = 0; NumeroRetrocedido<this.CantidadDeocultas; NumeroRetrocedido++)
                    {
                        capaActual = this.CantidadDeocultas-1-NumeroRetrocedido;
                            if (capaActual == 0){//el ultimo, osea el que recibe x,y
                                InternasS_antesTransformacion[capaActual] = Calcular_Fderivada(this.Capas_ocultas[capaActual],primerA);
                            }else{
                                InternasS_antesTransformacion[capaActual] = Calcular_Fderivada(this.Capas_ocultas[capaActual],InternasA[capaActual-1]); 
                            }
                            InternasSEnmatrizO = meterOs(InternasS_antesTransformacion[capaActual]);
                        for (int numeroNeurona = 0; numeroNeurona<this.CantidadNeuronasPcapa; numeroNeurona++){
                            //if 
                            for (int peso=0; peso<this.CantidadNeuronasPcapa; peso++){
                                //WsinWO[peso] = this.
                            }
                            //InternasS[capaActual] = multiply(InternasSEnmatrizO, InternasS)
                        }
                    
                    }
                }   
                   //esto es el fin de la sencibilidad en las capas ocultas
                //Aqui termina el calculo de las S
                
                
                
                
               ///* Esto es para ver la matriz A generada, no se imprime en cuadrado pero se entiende que cada internas lenght separa una fila
                System.out.println("\n");
                for(int i = 0; i<primerA.length;i++){
                    System.out.print(primerA[i]+"  ");
                }
                System.out.println("\n"+"internas length: "+ InternasA.length);
                for (int ii = 0; ii<InternasA.length ;ii++){
                    System.out.println("internas length fila: "+ InternasA[ii].length);
                    for(int i = 0; i<InternasA[ii].length;i++){
                        System.out.print(InternasA[ii][i]+"  ");
                    }
                }
                
                System.out.println("\nsalida");
                for(int i = 0; i<salidaA.length;i++){
                    System.out.print(salidaA[i]+"  ");
                }
                System.out.println("\nooooooooooooooooooooooooooo\n");
                
               //*/ 
   
            }
            
            System.out.println("\nGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG La generacion actual que termina: "+generacionActual);
            generacionActual++;
        }
        
        
        
        
    }
    
    
}
