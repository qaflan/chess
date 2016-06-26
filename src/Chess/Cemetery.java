package Chess;

import javax.swing.*;
import java.util.Vector;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Javad Nouri
 * Iran University of Scince and Technology
 * Student No: 85521487
 * Date: Jul 18, 2007
 * Time: 12:11:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class Cemetery extends JPanel {
    Vector<Piece> BlackDead=new Vector<Piece>();
    Vector<Piece> WhiteDead=new Vector<Piece>();

    public Cemetery(){
        super();
        setBackground(Color.LIGHT_GRAY);
        resize();
    }

    public void Clear(){
        WhiteDead.clear();
        BlackDead.clear();
    }

    public void Buerry(Piece theDead){
        if(theDead==null)   return;
        if(theDead.jColor){
            WhiteDead.add(theDead);
        }else{
            BlackDead.add(theDead);
        }
    }

    public void resize(){
        setSize(Piece.getWIDTH()*16/15,Piece.getWIDTH()*8);
        repaint();
    }

    public void paint(Graphics g) {
        super.paint(g);
        int i=0;
        int cWidth=Piece.getWIDTH()*8/15;
        for (Piece aWhiteDead : WhiteDead) {
            g.drawRect(0,i*cWidth,cWidth, cWidth);
            aWhiteDead.Draw(g,this,0,i*cWidth,cWidth, cWidth);
            i++;
        }
        i=0;
        for(Piece aBlackDead : BlackDead){
            g.drawRect(cWidth,i*cWidth,cWidth, cWidth);
            aBlackDead.Draw(g,this,cWidth,i*cWidth,cWidth,cWidth);
            i++;
        }
    }
}
