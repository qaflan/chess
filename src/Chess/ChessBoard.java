package Chess;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * By:  Javad Nouri
 * Student Number: 85521487
 * Iran University of Scince and Technology
 * Date: Jul 9, 2007
 * Time: 12:10:56 AM
 *
 */
public class ChessBoard extends JPanel {
    //The Constants: These final static variables determine the style in which the cells will be painted.

    /**The cell is valid to move to but this move puts the players king in check.*/
    public final static int VALIDBUTCHECK=2;

    /**The cell is valid to move to.*/
    public final static int VALIDMOVE=1;

    /**The cell should be updated only on the next update (to repaint only this cell)*/
    public final static int UPDATEONCE=5;


    //The member vars.
    /**The color in which the black cells will be colored in.*/
    final Color BlackCell=new Color(173,182,181);
    /**The color in which the white cells will be colored in.*/
    final Color WhiteCell=new Color(214,215,214);

    /**The Chess object which will be painted on this board.*/
    private Chess chsMain;
    private static final int UPDATEONCE2 = 44;

    /**Default constructor.*/
    public ChessBoard(){
        super();
    }

    /**
     * Constructor.
     * @param theChess  the Chess object which will be painted on this board.
     */
    public ChessBoard(Chess theChess){
        super();
        chsMain=theChess;
    }


    /**
     * Paints the board.
     */
    public void paint(){

        this.paint(this.getGraphics());
    }

   /* public void paint(int x,int y,int width, int height){
        Graphics g=this.getGraphics();
        g.setPaintMode();

        int i=x/Piece.getWIDTH();
        int j=y/Piece.getWIDTH();

        int rX=(i+1)*Piece.getWIDTH();
        int rY=(j+1)*Piece.getWIDTH();
        int rEX=(x+width)-(x+width)%Piece.getWIDTH();
        int rEY=(y+height)-(y+height)%Piece.getWIDTH();
        Color colorUp,colorDown;
        i++;
        colorUp= ((i+j)%2==0?WhiteCell:BlackCell);
        int j2=(y+height)/Piece.getWIDTH();
        colorDown= ((i+j2)%2==0?WhiteCell:BlackCell);
        
        for(int aX=rX ; aX<rEX ; aX+=Piece.getWIDTH()){
            g.setColor(colorUp);
            g.fillRect(aX,y,Piece.getWIDTH(),rY-y);

            g.setColor(colorDown);
            g.fillRect(aX,rEY,Piece.getWIDTH(),height+y-rEY);
            colorUp=(colorUp==WhiteCell?BlackCell:WhiteCell);
            colorDown=(colorDown==WhiteCell?BlackCell:WhiteCell);
        }
        i--;
        j++;
        colorUp= ((i+j)%2==0?WhiteCell:BlackCell);
        int i2=(x+width)/Piece.getWIDTH();
        colorDown= ((i2+j)%2==0?WhiteCell:BlackCell);



        for(int aY=rY ; aY<rEY ; aY+=Piece.getWIDTH()){
            g.setColor(colorUp);
            g.fillRect(x,aY,rX-x,Piece.getWIDTH());
            g.setColor(colorDown);
            g.fillRect(rEX,aY,width+x-rEX,Piece.getWIDTH());
            colorUp=(colorUp==WhiteCell?BlackCell:WhiteCell);
            colorDown=(colorDown==WhiteCell?BlackCell:WhiteCell);
        }

        *//*
        g.setColor( ((i+j)%2==0?WhiteCell:BlackCell));
        g.fillRect(x,y,rX-x,rY-y);
        i++;

        g.setColor( ((i+j)%2==0?WhiteCell:BlackCell));
        g.fillRect(rX,y,Piece.getWIDTH()-rX,rY-y);
        i--;
        j++;
        g.setColor( ((i+j)%2==0?WhiteCell:BlackCell));
        g.fillRect(x,rY,rX-x,Piece.getWIDTH()-rY);
        i++;
        g.setColor( ((i+j)%2==0?WhiteCell:BlackCell));
        g.fillRect(rX,rY,Piece.getWIDTH()-rX,Piece.getWIDTH()-rY);
*//*
        *//*for (int i = x/Piece.getWIDTH()+1; i <= (x+width)/Piece.getWIDTH(); i++) {
            for (int j = y/Piece.getWIDTH()+1; j <= (y+height)/Piece.getWIDTH(); j++) {
                if(i<8&&j<8)    chsMain.GameMapBordered[i][j]=UPDATEONCE;
            }
        }*//*
        //UpdateBoard();
    }*/
    /**
     * Updates the board according to the values of the GameMapBordered[][] matrix.
     * Actually paints only the changed cells.
     */
    public void UpdateBoard(){
        Graphics g=this.getGraphics();  //The Graphics object attached to this panel.
        for(int i=0; i<8 ; i++){
            for(int j=0; j<8; j++){
                if(chsMain.GameMapBordered[i][j]!=0){
                    g.setColor((i+j)%2==0?WhiteCell:BlackCell);
                    g.fillRect(i*Piece.getWIDTH(),j*Piece.getWIDTH(),Piece.getWIDTH(),Piece.getWIDTH());
                    g.drawRect(i*Piece.getWIDTH(),j*Piece.getWIDTH(),Piece.getWIDTH(),Piece.getWIDTH());

                    if(chsMain.GameMapBordered[i][j]==VALIDMOVE){
//                        g.setColor(new Color(96,144,14));
                        g.setColor(Color.PINK);
                        g.fillRect(i*Piece.getWIDTH(),j*Piece.getWIDTH(),Piece.getWIDTH(),Piece.getWIDTH());
                        g.setColor(Color.BLUE);
                        g.drawRect(i*Piece.getWIDTH(),j*Piece.getWIDTH(),Piece.getWIDTH(),Piece.getWIDTH());
                        g.setPaintMode();
                    }else if(chsMain.GameMapBordered[i][j]==UPDATEONCE){
                        chsMain.GameMapBordered[i][j]=0;
                    }else if(chsMain.GameMapBordered[i][j]==VALIDBUTCHECK){
                        g.setColor(Color.RED);
                        g.fillRect(i*Piece.getWIDTH(),j*Piece.getWIDTH(),Piece.getWIDTH(),Piece.getWIDTH());
                        g.setColor(Color.BLUE);
                        g.drawRect(i*Piece.getWIDTH(),j*Piece.getWIDTH(),Piece.getWIDTH(),Piece.getWIDTH());
                        g.setPaintMode();
                    }else if(chsMain.GameMapBordered[i][j]==10){
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(i*Piece.getWIDTH(),j*Piece.getWIDTH(),Piece.getWIDTH(),Piece.getWIDTH());
                        g.setColor(Color.BLUE);
                        g.drawRect(i*Piece.getWIDTH(),j*Piece.getWIDTH(),Piece.getWIDTH(),Piece.getWIDTH());
                        g.setPaintMode();
                    }
                    if(chsMain.GameMapBordered[i][j]==UPDATEONCE2){
                        g.setColor(Color.RED);
                        g.drawRoundRect(i*Piece.getWIDTH(),j*Piece.getWIDTH(),Piece.getWIDTH(),Piece.getWIDTH(),5,5);
                        chsMain.GameMapBordered[i][j]=0;
                    }

                    if (chsMain.GameMap[i][j]!=null)
                    chsMain.GameMap[i][j].Draw(g,this, i*Piece.getWIDTH(),j*Piece.getWIDTH());
                }
            }
        }
    }

    public void paint(Graphics g){
        super.paint(g);
        for(int i=0 ; i<8 ; i++){
            for(int j=0 ; j<8 ; j++){
                g.setColor((i+j)%2==0?WhiteCell:BlackCell);
                //g.drawRect(i*Piece.getWIDTH(),j*Piece.getWIDTH(),(i+1)*Piece.getWIDTH(),(j+1)*Piece.getWIDTH());
                g.fillRect(i*Piece.getWIDTH(),j*Piece.getWIDTH(),Piece.getWIDTH(),Piece.getWIDTH());
                g.drawRect(i*Piece.getWIDTH(),j*Piece.getWIDTH(),Piece.getWIDTH(),Piece.getWIDTH());
                if(chsMain.GameMapBordered[i][j]==VALIDMOVE){
//                    g.setColor(new Color(96,144,14));
                    g.setColor(Color.PINK);
                    g.fillRect(i*Piece.getWIDTH(),j*Piece.getWIDTH(),Piece.getWIDTH(),Piece.getWIDTH());
                    g.setColor(Color.BLUE);
                    g.drawRect(i*Piece.getWIDTH(),j*Piece.getWIDTH(),Piece.getWIDTH(),Piece.getWIDTH());
                    g.setPaintMode();
                }else if(chsMain.GameMapBordered[i][j]==VALIDBUTCHECK){
                        g.setColor(Color.RED);
                        g.fillRect(i*Piece.getWIDTH(),j*Piece.getWIDTH(),Piece.getWIDTH(),Piece.getWIDTH());
                        g.setColor(Color.BLUE);
                        g.drawRect(i*Piece.getWIDTH(),j*Piece.getWIDTH(),Piece.getWIDTH(),Piece.getWIDTH());
                        g.setPaintMode();
                }else if(chsMain.GameMapBordered[i][j]==10){
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(i*Piece.getWIDTH(),j*Piece.getWIDTH(),Piece.getWIDTH(),Piece.getWIDTH());
                        g.setColor(Color.BLUE);
                        g.drawRect(i*Piece.getWIDTH(),j*Piece.getWIDTH(),Piece.getWIDTH(),Piece.getWIDTH());
                        g.setPaintMode();
                }
                if (chsMain.GameMap[i][j]!=null)
                    chsMain.GameMap[i][j].Draw(g,this, i*Piece.getWIDTH(),j*Piece.getWIDTH());

            }
        }
    }
}
