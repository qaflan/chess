package Chess;

import com.sun.java.swing.plaf.motif.MotifBorders;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Javad Nouri    85521487
 * Student Number:  85521487
 * Iran University of Scince and Technology
 * Date: Jul 16, 2007
 * Time: 8:56:57 PM
 */
public class ChessClock extends JPanel {
    /** The chess object.*/
    Chess chsMain;
    /**A PlayerRecords object which will be used to simulate the semi-clock(!) of the White player.*/
    public PlayerRecords WhitePlayer;
    /**A PlayerRecords object which will be used to simulate the semi-clock(!) of the White player.*/
    public PlayerRecords BlackPlayer;

    /**Determines if the Clock is running*/
    private boolean isRunning=false;


    /**
     * Restarts the Clock. This should be invoked when the game is going to be restarted.
     */
    public void restart(int h, int m, int s){

        if(WhitePlayer!=null)
            this.remove(WhitePlayer);   //Remove WhitePlayer to renew it.
        if(BlackPlayer!=null)
            this.remove(BlackPlayer);   //Remove WhitePlayer to renew it.
        //Renew the PlayerRecords:
        WhitePlayer=new PlayerRecords(h,m,s);
        BlackPlayer=new PlayerRecords(h,m,s);
        WhitePlayer.setLocation(0,0);
        BlackPlayer.setLocation(210,0);
        this.add(WhitePlayer);
        this.add(BlackPlayer);

        //The redundant(!) panel
        JPanel pnlTemp=new JPanel();
        this.add(pnlTemp);
        pnlTemp.setVisible(false);

        isRunning=false;    //The Game is hasnot started yet.
    }

    /**
     * Stops the clock.
     */
    public void stop(){
        WhitePlayer.Clock.stop();
        BlackPlayer.Clock.stop();
        WhitePlayer.setForeground(new Color(0,100,0));
        BlackPlayer.setForeground(new Color(0,100,0));
    }

    /**
     * Once a player has performed his/her move, he/she should shut the clock.
     * This operation. is done automatically here and is simulated by this method.
     */
    public void flip(){
        BlackPlayer.shut(); //Black's semi-clock should be shut.
        if(isRunning){
            //if the game has started,
            WhitePlayer.shut();
        }
        isRunning=true;
    }

    /**
     * Default Constructor.
     * @param theGame   The Chess game associated with the clock;
     */
    public ChessClock(Chess theGame) {
        chsMain=theGame;
        restart(0,10,0);
        super.setSize(410,50);
        setVisible(true);
    }



    class PlayerRecords extends JLabel{
        Timer Clock;            //The SemiClock
        boolean Over=false;     //The semiClock has reached 00:00:00
        /**The hour value of the semi clock.*/
        int Hour;
        /**The minute value of the semi clock.*/
        int Minute;
        /**The second value of the semi clock.*/
        int Second;

        /**
         * If the semiClock is working, this method stops it and if it is paused,
         * makes it work.
         * This method also puts a border arround the label if the semi-click is working.
         */
        public void shut(){
            if(Clock.isRunning()){
                super.setForeground(Color.GRAY);
                super.setBorder(null);
                Clock.stop();
            }else{
                super.setForeground(Color.BLUE);
                super.setBorder(new MotifBorders.BevelBorder(true,Color.RED,Color.RED));
                Clock.start();
            }
        }
        public PlayerRecords(int hour,int minute,int second){
            Hour=hour;
            Minute=minute;
            Second=second;
            super.setFont(new Font("Arial",Font.BOLD,20));
            final DecimalFormat twoDigits=new DecimalFormat("00");
            super.setText(twoDigits.format(Hour)+":"+twoDigits.format(Minute)+":"+twoDigits.format(Second));
            super.setSize(200,50);
            Clock = new Timer(1000,new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(Second>0){
                        Second--;
                    }else{
                        if(Minute>0){
                            Minute--;
                            Second=59;
                        }else{
                            if(Hour>0){
                                Hour--;
                                Minute=59;
                                Second=59;
                            }
                        }
                    }
                    if(Second==0 && Minute==0 && Hour==0){
                        Over=true;
                        chsMain.ControlGame();
                        Clock.stop();
                        PlayerRecords.this.setForeground(Color.RED);
                    }
                    PlayerRecords.super.setText(twoDigits.format(Hour)+":"+twoDigits.format(Minute)+":"+twoDigits.format(Second));
                }
            });
        }
        public PlayerRecords() {
            this(0,10,0);
        }
    }

}






