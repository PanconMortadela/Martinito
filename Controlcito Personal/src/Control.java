
import com.sun.javafx.sg.prism.NGParallelCamera;
import org.powerbot.script.*;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GeItem;
import java.net.Socket;
import java.net.InetAddress;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.util.Scanner;
import java.io.*;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;

public class Control {
    public static void main(String[] args) throws IOException {
        boolean re=true;
        int count=0;
        int[] contra = {KeyEvent.VK_2, KeyEvent.VK_1,
                KeyEvent.VK_4, KeyEvent.VK_9,
                KeyEvent.VK_9, KeyEvent.VK_2,
                KeyEvent.VK_3, KeyEvent.VK_4,
                KeyEvent.VK_E, KeyEvent.VK_ENTER};

        while (re) {
            if(count==3)break;
            try {
                    clic robot;
                    robot = new clic();
                    Robot teclado = new Robot();
                Thread.sleep(Random.nextInt(2000, 3000));
                    //Seleccionar la ventana 1 170,10;565,26
                robot.mouseMove(Random.nextInt(250 , 270),
                                Random.nextInt(700, 710));
                Thread.sleep(Random.nextInt(2000, 3000));
                robot.click();
                Thread.sleep(Random.nextInt(2000, 3000));
                    //Usuario existente 1 432,330;530,340
                robot.mouseMove(Random.nextInt(460, 500),
                                Random.nextInt(332, 338));
                Thread.sleep(Random.nextInt(2000, 3000));
                robot.click();
                Thread.sleep(Random.nextInt(2000, 3000));
                    //aqui viene teclear pass
                for (int i = 0; i < contra.length; i++) {//Introduce la contra
                        teclado.keyPress(contra[i]);
                        teclado.keyRelease(contra[i]);
                        teclado.delay(Random.nextInt(300, 500));
                }

                	//click para juega   319,360;501,413
                    Thread.sleep(Random.nextInt(5000, 10000));
                    robot.mouseMove(Random.nextInt(350, 450),
                                    Random.nextInt(380, 400));
                    Thread.sleep(Random.nextInt(2000, 3000));
                    robot.click();
                    Thread.sleep(Random.nextInt(2000, 3000));

                	//Script 52,37;76,42
                robot.mouseMove(Random.nextInt(17, 32),
                                Random.nextInt(37, 40));
                Thread.sleep(Random.nextInt(2000, 3000));
                robot.click();
                Thread.sleep(Random.nextInt(2000, 3000));

                    //Start 60,59;125,65
                robot.mouseMove(Random.nextInt(17, 32),
                                Random.nextInt(59, 65));
                Thread.sleep(Random.nextInt(2000, 3000));
                robot.click();
                Thread.sleep(Random.nextInt(2000, 3000));

                	//Play 567,380;606,392
                robot.mouseMove(Random.nextInt(660, 680),
                                Random.nextInt(373, 380));
                Thread.sleep(Random.nextInt(2000, 3000));
                robot.click();
                Thread.sleep(Random.nextInt(2000, 3000));

                    //Minimizar
                robot.mouseMove(Random.nextInt(250 , 270),
                        Random.nextInt(700, 710));
                Thread.sleep(Random.nextInt(2000, 3000));
                robot.click();
                Thread.sleep(Random.nextInt(2000, 3000));
                //////
                //////
                //////

                //Seleccionar la ventana 2
                // ,123;876,138
                robot.mouseMove(Random.nextInt(300, 320),
                        Random.nextInt(700, 710));
                Thread.sleep(Random.nextInt(2000, 3000));
                robot.click();
                Thread.sleep(Random.nextInt(2000, 3000));

                //Usuario existente 1 637,441;738,455
                robot.mouseMove(Random.nextInt(910, 980),
                        Random.nextInt(425, 435));
                Thread.sleep(Random.nextInt(2000, 3000));
                robot.click();
                Thread.sleep(Random.nextInt(2000, 3000));
                //aqui viene teclear pass
                for (int i = 0; i < contra.length; i++) {//Introduce la contra
                    teclado.keyPress(contra[i]);
                    teclado.keyRelease(contra[i]);
                    teclado.delay(Random.nextInt(300, 500));
                }

                //click para press Script   540,475;695,515
                Thread.sleep(Random.nextInt(5000, 10000));
                robot.mouseMove(Random.nextInt(810, 940),
                        Random.nextInt(458, 465));
                Thread.sleep(Random.nextInt(2000, 3000));
                robot.click();
                Thread.sleep(Random.nextInt(2000, 3000));

                //Select Play 260,147;284,155

                robot.mouseMove(Random.nextInt(495, 510),
                        Random.nextInt(126, 130));
                Thread.sleep(Random.nextInt(2000, 3000));
                robot.click();
                Thread.sleep(Random.nextInt(2000, 3000));

                //Start 260,168;345,1782149
                robot.mouseMove(Random.nextInt(495, 510),
                        Random.nextInt(148,158));

                robot.click();
                Thread.sleep(Random.nextInt(2000, 3000));


                //Play 777,490;811,502
                robot.mouseMove(Random.nextInt(1135, 1160),
                        Random.nextInt(465, 470));
                Thread.sleep(Random.nextInt(2000, 3000));
                robot.click();
                Thread.sleep(Random.nextInt(2000, 3000));

                //Minimizar
                robot.mouseMove(Random.nextInt(300, 320),
                        Random.nextInt(700, 710));
                Thread.sleep(Random.nextInt(2000, 3000));
                robot.click();
                Thread.sleep(Random.nextInt(2000, 3000));


            } catch (AWTException e) {

            } catch (InterruptedException e) {

                    e.printStackTrace();
            }

            try {
                    Thread.sleep(14700000);

            } catch (Exception e) {

            }
            count++;
        }

    }
}
