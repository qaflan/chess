package Chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by IntelliJ IDEA.
 * User: Javad Nouri    85521487
 * Student Number: 85521487
 * Iran University of Scince and Technology
 * Date: Jul 15, 2007
 * Time: 11:09:55 AM
 */
public class Promotion extends JDialog {
    private final static int WIDTH=70;
//    JPanel pnl=new JPanel();
    PiecePanel[] pnlThePieces=new PiecePanel[4];
    /**The Pawn which wiil be promoted.*/
    Piece thePawn;
    /**The Piece after promotion.*/
    Piece promoted;
    private boolean[] Selections=new boolean[4];
    JButton btnOK=new JButton("OK");
    private JLabel prompt=new JLabel("What do you want the pawn to be promoted to?");
    Container container=this.getContentPane();
    private Promotion(Piece theP,Piece Promoted){
        setTitle( "A "+(theP.jColor?"white":"black")+" pawn reached end of its way!");
        promoted=Promoted;
        thePawn=theP;
        Selections[0]=true;
        final Piece[] c=new Piece[4];
        c[0]=new Queen(thePawn.jColor);
        c[1]=new Rook(thePawn.jColor);
        c[2]=new Knight(thePawn.jColor);
        c[3]=new Bishop(thePawn.jColor);
        for (int i=0 ; i<4; i++) {
            pnlThePieces[i]=new PiecePanel(c[i]);
            pnlThePieces[i].setSize(WIDTH,WIDTH);
            pnlThePieces[i].setLocation(i*(WIDTH+5),20);
            this.add(pnlThePieces[i]);
            pnlThePieces[i].addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                    if(e.getClickCount() ==2){
                        btnOK.doClick();
                    }
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                public void mousePressed(MouseEvent e) {
                    for(int i=0;i<4;i++){
                        pnlThePieces[i].Bordered = (pnlThePieces[i]==e.getSource());
                        Selections[i]=(pnlThePieces[i]==e.getSource());
                        pnlThePieces[i].repaint();
                    }
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                public void mouseReleased(MouseEvent e) {
                }

                public void mouseEntered(MouseEvent e) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                public void mouseExited(MouseEvent e) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }
            });


        }
        pnlThePieces[0].Bordered=true;

        btnOK.addKeyListener(new KeyListener() {
                public void keyTyped(KeyEvent e) {

                }

                public void keyPressed(KeyEvent e) {
                    int current=0,theNext=0,thePrevious=0;
                    for(int i=0;i<4;i++){
                        if(Selections[i]){
                            current=i;
                            break;
                        }
                    }
                    theNext=(current==3?0:current+1);
                    thePrevious=(current==0?3:current-1);
                    switch(e.getKeyCode()){
                        case 39:
                            Selections[theNext]=true;
                            Selections[current]=false;
                            pnlThePieces[theNext].Bordered=true;
                            pnlThePieces[current].Bordered=false;
                            pnlThePieces[theNext].repaint();
                            pnlThePieces[current].repaint();
                            break;
                        case 37:
                            Selections[thePrevious]=true;
                            Selections[current]=false;
                            pnlThePieces[thePrevious].Bordered=true;
                            pnlThePieces[current].Bordered=false;
                            pnlThePieces[thePrevious].repaint();
                            pnlThePieces[current].repaint();
                            break;
                    }
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                public void keyReleased(KeyEvent e) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }
            });

        btnOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i=0;i<4 ; i++) {
                    if(Selections[i]){
                        promoted=c[i];
                        break;
                    }
                }
                Promotion.this.setModal(false);
                Promotion.this.setVisible(false);
            }
        });

        /*pnl.setSize(4*(WIDTH+5)+5,WIDTH);
        pnl.setLocation(20,20);*/
        this.setSize(WIDTH*4+3*5+10,WIDTH+100);
        this.setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()-this.getWidth())/2,(int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()-this.getHeight())/2);
        btnOK.setSize(100,25);
        btnOK.setLocation((getWidth()-btnOK.getWidth())/2,WIDTH+35);
        //prompt.setLocation(0,0);
        //this.setLayout(new GridLayout(2,4));
        prompt.setSize(300,20);
        prompt.setForeground(new Color(0,100,0));
        this.add(prompt);

        this.add(btnOK);
        JPanel x=new JPanel();
        this.add(x);
        x.setVisible(false);
        this.getRootPane().setDefaultButton(btnOK);
        this.setModal(true);
        this.setVisible(true);

    }

    /**
     * Promotes the pawn and transforms it into a Queen, a Rook, a Knight
     *  or a Bishop according to what the user chooses to do.
     * @param p The Pawn which will be promoted.
     * @return  The Promoted Piece which must be replaced by the Pawn.
     */
    public static Piece Promote(Piece p){
        Piece result=new Queen(p.jColor);
        Promotion s=new Promotion(p,result);
        return s.promoted;
    }
    class PiecePanel extends JPanel{
        Piece ThePiece;
        boolean Bordered=false;
        public PiecePanel(Piece thePiece) {
            ThePiece = thePiece;
            if(thePiece==null){
                ThePiece=new Pawn(true);
            }
        }
        public void paint(Graphics g) {
            super.paint(g);
            setBackground(Color.lightGray);
            if(Bordered){
                setForeground(Color.YELLOW);
                g.drawRect(0,0,getWidth()-1,getHeight()-1);
                g.drawRect(1,1,getWidth()-2,getHeight()-2);
                g.drawRect(2,2,getWidth()-4,getHeight()-4);
            }
            ThePiece.Draw(g,this,0,0,Promotion.WIDTH,Promotion.WIDTH);
        }
    }
}
