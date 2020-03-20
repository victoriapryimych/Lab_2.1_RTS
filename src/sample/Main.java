package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import java.util.Random;
import static java.lang.Math.*;

public class Main extends Application {

    public static int wMax = 1800;
    public static int nGarm = 14;
    public static int nDiscr = 256;

    //signal genetation
    public static double[] signalGeneration() {
        Random rand = new Random();
        int[] Phi = new int[nGarm];
        int[] Amp = new int[nGarm];
        for (int i = 0; i < nGarm; i++) {
            Phi[i] = rand.nextInt(360) + 1;
            Amp[i] = rand.nextInt(10) + 1;
        }
        double[] x = new double[nDiscr];
        for (int t = 0; t < nDiscr; t++) {
            for (int j = 0; j < nGarm; j++) {
                x[t] += Amp[j] * sin((double) (wMax / nGarm) * (j + 1) * t + Phi[j]);
            }
        }
        return x;
    }

    //Fourier transform
    public static double[] fourierTransform(double[] x) {
        double[] XRe = new double[nDiscr];
        double[] XIm = new double[nDiscr];
        double[] X = new double[nDiscr];
        long start = System.nanoTime();
        for (int p = 0; p < nDiscr; p++)
            for (int k = 0; k < nDiscr; k++) {
                XRe[p] += x[k] * cos(2 * PI * p * k / nDiscr);
                XIm[p] += x[k] * sin(2 * PI * p * k / nDiscr);
            }
        long finish = System.nanoTime();
        long timeElapsed = finish - start;
        System.out.println("Elapsed time: " + timeElapsed/pow(10,6) + " milliseconds");
        for (int p = 0; p < nDiscr; p++) {
            X[p] = sqrt(pow(XRe[p], 2) + pow(XIm[p], 2));
        }
        return X;
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Лабораторна робота №2.1");
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("t");
        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        XYChart.Series series = new XYChart.Series();
        series.setName("Графік X(p)");//Амплітудний спектр

        double[] x = signalGeneration();
        double[] X = fourierTransform(x);
        for (int p = 0; p < nDiscr; p++)
            series.getData().add(new XYChart.Data(p, X[p]));

        Scene scene = new Scene(lineChart, 1000, 600);
        lineChart.getData().add(series);
        lineChart.setCreateSymbols(false);
        stage.setScene(scene);
        stage.show();
    }
}
