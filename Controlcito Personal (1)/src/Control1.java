import org.powerbot.script.Random;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class Control1 {
    public static void main(String[] args) throws IOException {
        boolean re=true;
        int count=0;
        int[] contra = {KeyEvent.VK_2, KeyEvent.VK_1,
                KeyEvent.VK_4, KeyEvent.VK_9,
                KeyEvent.VK_9, KeyEvent.VK_2,
                KeyEvent.VK_3, KeyEvent.VK_4,
                KeyEvent.VK_E, KeyEvent.VK_ENTER};

        while (re) {

            try {
                    clic robot;
                    robot = new clic();
                    Robot teclado = new Robot();
                Thread.sleep(Random.nextInt(2000, 3000));
                                        //Seleccionar la ventana 1 170,10;565,26
                robot.mouseMove(Random.nextInt(255 , 275),
                                Random.nextInt(700, 710));
                Thread.sleep(Random.nextInt(2000, 3000));
                robot.click();
                Thread.sleep(Random.nextInt(2000, 3000));

                if(count>0){
                    //Seleccionar ventana para desconectar
                    robot.mouseMove(Random.nextInt(360 , 440),
                            Random.nextInt(345, 360));
                    Thread.sleep(Random.nextInt(2000, 3000));
                    robot.click();
                    Thread.sleep(Random.nextInt(2000, 3000));
                }else {

                    //Usuario existente 1 432,330;530,340
                    robot.mouseMove(Random.nextInt(460, 500),
                            Random.nextInt(335, 345));
                    Thread.sleep(Random.nextInt(2000, 3000));
                    robot.click();
                    Thread.sleep(Random.nextInt(2000, 3000));
                }
                    //aqui viene teclear pass
                for (int i = 0; i < contra.length; i++) {//Introduce la contra
                        teclado.keyPress(contra[i]);
                        teclado.keyRelease(contra[i]);
                        teclado.delay(Random.nextInt(300, 500));
                }

                	//click boton rojo   319,360;501,413
                    Thread.sleep(Random.nextInt(5000, 10000));
                    robot.mouseMove(Random.nextInt(350, 450),
                                    Random.nextInt(370, 400));
                    Thread.sleep(Random.nextInt(2000, 3000));
                    robot.click();
                    Thread.sleep(Random.nextInt(2000, 3000));

                	//Script 52,37;76,42
                robot.mouseMove(Random.nextInt(16, 33),
                                Random.nextInt(37, 41));
                Thread.sleep(Random.nextInt(2000, 3000));
                robot.click();
                Thread.sleep(Random.nextInt(2000, 3000));

                    //Start 60,59;125,65
                robot.mouseMove(Random.nextInt(23, 75),
                                Random.nextInt(60, 64));
                Thread.sleep(Random.nextInt(2000, 3000));
                robot.click();
                Thread.sleep(Random.nextInt(2000, 3000));

                	//Play 567,380;606,392
                robot.mouseMove(Random.nextInt(655, 680),
                                Random.nextInt(375, 380));
                Thread.sleep(Random.nextInt(2000, 3000));
                robot.click();
                Thread.sleep(Random.nextInt(2000, 3000));

                //Minimizar

                robot.mouseMove(Random.nextInt(255 , 275),
                        Random.nextInt(700, 710));
                Thread.sleep(Random.nextInt(2000, 3000));
                robot.click();
                Thread.sleep(Random.nextInt(2000, 3000));
                //////
                //////
                //////

                /*
                //Seleccionar la ventana 2
                // ,123;876,138
                robot.mouseMove(Random.nextInt(845, 940),
                        Random.nextInt(69, 71));
                Thread.sleep(Random.nextInt(2000, 3000));
                robot.click();
                Thread.sleep(Random.nextInt(2000, 3000));

                //Usuario existente 1 637,441;738,455
                robot.mouseMove(Random.nextInt(910, 980),
                        Random.nextInt(393, 396));
                Thread.sleep(Random.nextInt(2000, 3000));
                robot.click();
                Thread.sleep(Random.nextInt(2000, 3000));
                //aqui viene teclear pass
                for (int i = 0; i < contra.length; i++) {//Introduce la contra
                    teclado.keyPress(contra[i]);
                    teclado.keyRelease(contra[i]);
                    teclado.delay(Random.nextInt(300, 500));
                }

                //Cuadradito rojo para entrar   540,475;695,515
                Thread.sleep(Random.nextInt(5000, 10000));
                robot.mouseMove(Random.nextInt(810, 940),
                        Random.nextInt(458, 465));
                Thread.sleep(Random.nextInt(2000, 3000));
                robot.click();
                Thread.sleep(Random.nextInt(2000, 3000));

                //Presiona Script

                robot.mouseMove(Random.nextInt(525, 535),
                        Random.nextInt(90, 92));
                Thread.sleep(Random.nextInt(2000, 3000));
                robot.click();
                Thread.sleep(Random.nextInt(2000, 3000));


                //Presiona Play 260,168;345,1782149
                robot.mouseMove(Random.nextInt(535, 555),
                        Random.nextInt(113, 116));
                Thread.sleep(Random.nextInt(2000, 3000));
                robot.click();
                Thread.sleep(Random.nextInt(2000, 3000));

                //Select Script para correr
                robot.mouseMove(Random.nextInt(660, 700),
                        Random.nextInt(366, 370));
                Thread.sleep(Random.nextInt(2000, 3000));
                robot.click();
                Thread.sleep(Random.nextInt(2000, 3000));

                //Play 777,490;811,502
                robot.mouseMove(Random.nextInt(1036, 1062),
                        Random.nextInt(445, 450));
                Thread.sleep(Random.nextInt(2000, 3000));
                robot.click();
                Thread.sleep(Random.nextInt(2000, 3000));

*/
                count++;
                if(count==5){
                    re=false;
                }
            } catch (AWTException e) {

            } catch (InterruptedException e) {
                    e.printStackTrace();
            }

            try {
                Thread.sleep(14700000);//Thread.sleep(8400000);//14700000

            } catch (Exception e) {

            }

        }

    }
}
