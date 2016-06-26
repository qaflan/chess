package Chess;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import org.omg.CORBA.StringHolder;

/**
 * Created by IntelliJ IDEA.
 * By:  Javad Nouri
 * Student Number: 85521487
 * Iran University of Scince and Technology
 * Date: Jul 6, 2007
 * Time: 9:22:24 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Piece {
    protected final String PATH="ChessSets/Classic";
    String Name="",Abbr="";
    boolean IsInCheck=false;
    boolean hasMoved=false;
    private static int WIDTH=80;


    public static void setWIDTH(int w){
        WIDTH=Math.abs(w);
    }
    public static int getWIDTH() {
        return WIDTH;
    }

    boolean jColor;  //True: White, False: Black
    /*public Piece(Piece p){
        Name= p.Name;
        Abbr= p.Abbr;
        jColor=p.jColor;
        image=(BufferedImage)p.image.;
    }*/
    protected BufferedImage image=null;

    public void Draw(Graphics g,JPanel pnl, int x, int y){
        g.drawImage(image,x,y,WIDTH,WIDTH,pnl);
    }
    public void Draw(Graphics g,JPanel pnl, int x, int y,int width,int height){
        g.drawImage(image,x,y,width, height, pnl);
    }

    public Piece(boolean nColor){
        jColor=nColor;
        setImage();
    }

    private void setImage() {
        image=LoadImage(PATH+"/"+Name+(this.jColor?"W":"B")+".gif");
    }

    public Piece(String name,String abbr,boolean color){
        Name=name;
        Abbr=abbr;
        jColor=color;
        setImage();
    }
    public Piece(){
        this(true);
    }

    protected abstract boolean IsValidBasicMove(int x1, int y1, int x2, int y2, Piece[][] GameMap, StringHolder Reason);

    public boolean IsValidMove(int x1, int y1, int x2, int y2, boolean turn,Piece[][] GameMap, StringHolder Reason){
        String strReason="";
        Chess.IsChecked(GameMap);
        if(!IsValidBasicMove(x1,y1,x2,y2,GameMap,Reason)){
            return false;
        }
        //String CurrentCheckStatus=Chess.IsChecked(GameMap);
        
        Piece temp=GameMap[x2][y2];
        GameMap[x2][y2]=GameMap[x1][y1];
        GameMap[x1][y1]=null;
        boolean theResult=true;
        if((turn && Chess.IsChecked(GameMap).equals("w")) || (!turn && Chess.IsChecked(GameMap).equals("b")) ){
            strReason+="This move will put your("+(turn?"White":"Black")+"'s) king into check!";
            theResult=false;
        }
        if(Chess.IsChecked(GameMap).equals("wb")){
            strReason+="You can't put your opponent into check when you yourself are in check!";
            theResult=false;
        }
        GameMap[x1][y1]=GameMap[x2][y2];
        GameMap[x2][y2]=temp;
        Reason.value = strReason;
        return theResult;
    }
    protected BufferedImage LoadImage(String FileName){
        try {
            return ImageIO.read(new File(FileName));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"File Not Found!!","ERROR",JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}



class Pawn extends Piece {

    /*public Pawn(Piece p){
        super(p);
    }*/
    public Pawn(){
        this(true);
    }

    protected boolean IsValidBasicMove(int x1, int y1, int x2, int y2, Piece[][] GameMap, StringHolder Reason) {
        boolean color=GameMap[x1][y1].jColor;
        int dx=x2-x1,dy=y2-y1;
        Piece Target=GameMap[x2][y2];
        int charpi=(Chess.WHITE_PAWN_ROW ==1?1:-1);
        dy=(color ? charpi*dy : -charpi*dy);
        boolean YLegal=(dy==1 || (dy==2 && y1==(color? Chess.WHITE_PAWN_ROW : Chess.BLACK_PAWN_ROW) && GameMap[x1][(color? Chess.WHITE_MIDDLE_ROW : Chess.BLACK_MIDDLE_ROW)]==null));
            if(!YLegal){
                if(dy<0){
                    Reason.value = "A pawn can't move backwards!";
                }else if(dy==2 && y1!=(color? Chess.WHITE_PAWN_ROW : Chess.BLACK_PAWN_ROW)){
                    Reason.value = "A pawn can move 2 cells forward only on its first move!";
                }else if(Math.abs(dy)>2){
                    Reason.value="A pawn can only move one step forward or 2 steps on its first move!";

                }else if(Target!=null){
                    if(Target.jColor!=color){
			Reason.value = "A pawn can only capture one step diagonally!";
                    }else{
			Reason.value = "You can't get on your own piece!";
                    }
                }
            }

        boolean XLegal=(Math.abs(dx)==1 && Target!=null && dy==1 && Target.jColor!=color) || (dx==0 && Target==null);
        if(!XLegal){
            if(Math.abs(dx)>1){
                Reason.value = "A pawn can only capture diagonally!";
            }else if(Math.abs(dx)==1){
                if(Target==null){
                    Reason.value = "A pawn can only  move diagonally while capturing!";
                }else if(Target.jColor == color){
                    Reason.value = "You can't capture your own piece!";
                }else if(dy!=1){
                    Reason.value =  "A pawn can capture only one step diagonally!";
                }
            }
        }
        return (YLegal && XLegal);
    }

    public Pawn(boolean nColor){
        super("Pawn","",nColor);
    }
}
class Bishop extends Piece {


  /*  public Bishop(Piece p){
        super(p);
    }*/
    public Bishop(){
        this(true);
        //image=LoadImage("C:\\Bishop"+(this.jColor?"W":"B")+".gif");
    }

    public static boolean StaticIsValidBasicMove(int x1, int y1, int x2, int y2, Piece[][] GameMap, StringHolder Reason) {
        boolean color=GameMap[x1][y1].jColor;
        int dx=x2-x1,dy=y2-y1;
        if(Math.abs(dx)!=Math.abs(dy)){
            Reason.value = "A bishop can only move diagonally!";
            return false;
        }
        Piece Target=GameMap[x2][y2];
        for(int x=x1,y=y1 ; x!=x2 && y!=y2 ; x+=(dx>0?1:-1),y+=(dy>0?1:-1)){
            if(x!=x1 && GameMap[x][y]!=null){
                Reason.value="A bishop can't jump over other pieces!";
                return false;
            }
        }
        if(Target!=null && Target.jColor==color){
            Reason.value="You can't capture your own pieces!!";
        }
        return Target == null || Target.jColor != color;
    }


    protected boolean IsValidBasicMove(int x1, int y1, int x2, int y2, Piece[][] GameMap, StringHolder Reason) {
        return StaticIsValidBasicMove(x1,  y1,  x2,  y2, GameMap,Reason);
    }

    public Bishop(boolean nColor){
        super("Bishop","B",nColor);
        //image=LoadImage("C:\\Bishop"+(this.jColor?"W":"B")+".gif");
    }
}
class Rook extends Piece {


 /*   public Rook(Piece p){
        super(p);
    }*/
    public Rook(){
        this(true);
    }
    public Rook(boolean nColor){
        super("Rook","R",nColor);
    }
    public static boolean StaticIsValidBasicMove(int x1, int y1, int x2, int y2, Piece[][] GameMap, StringHolder Reason){
        boolean color=GameMap[x1][y1].jColor;
        int dx=x2-x1,dy=y2-y1;
        Piece Target=GameMap[x2][y2];
        if(dx==0 ^ dy==0){
                if(dx==0){
                    int minVal=(y1<y2?y1:y2),maxVal=(y1>y2?y1:y2);
                    for(int i=minVal+1 ; i<maxVal; i++){
                        if(GameMap[x1][i]!=null){
                            Reason.value = "A rook can't jump over other pieces!";
                            return false;
                        }
                    }
                    if(Target!=null && Target.jColor == color){
                        Reason.value = "You can't capture your own pieces!";
                    }
                    return Target == null || (Target.jColor != color);
                }else{
                    int minVal=(x1<x2?x1:x2),maxVal=(x1>x2?x1:x2);
                    for(int i=minVal+1 ; i<maxVal; i++){
                        if(GameMap[i][y1]!=null){
                            Reason.value = "A rook can't jump over other pieces!";
                            return false;

                        }
                    }
                    if(Target!=null && Target.jColor == color){
                        Reason.value = "You can't capture your own pieces!";
                    }
                    return Target == null || (Target.jColor != color);
                }
        }else{
            Reason.value = "A rook can only move along ranks of files!";
            return false;
        }
    }
    protected boolean IsValidBasicMove(int x1, int y1, int x2, int y2, Piece[][] GameMap, StringHolder Reason){
        return StaticIsValidBasicMove(x1,  y1,  x2,  y2, GameMap,Reason);
    }
}
class King extends Piece {


 /*   public King(Piece p){
        super(p);
    }*/
    public King(){
        this(true);
    }

    protected boolean IsValidBasicMove(int x1, int y1, int x2, int y2, Piece[][] GameMap, StringHolder Reason) {
        boolean color=GameMap[x1][y1].jColor;
        int dx=Math.abs(x2-x1),dy=Math.abs(y2-y1);
        Piece Target=GameMap[x2][y2];
        //Chess.IsChecked(GameMap);
        if(!IsInCheck){
            if(dy==0 && dx==2 && !hasMoved){     //May be able to castle
                if(x2>x1){  //KingSide castling
                    Piece theRook=GameMap[Chess.KING_SIDE_END][jColor? Chess.WHITE_MAIN_ROW : Chess.BLACK_MAIN_ROW];
                    if(theRook!=null && !theRook.hasMoved){ //The rook hasn't moved
                        if(GameMap[x1 + 1][y1] == null && GameMap[x1 + 2][y1] == null){
                            return  true;
                        }else{
                            Reason.value = "While castling, there should be no pieces between the king and the rook!";
                            return false;
                        }
                    }else{
                        Reason.value="Once your rook has moved, you can't castle anymore!";
                        return false;
                    }
                }else if(x2<x1){    //KingSide Castling
                    Piece theRook=GameMap[Chess.QUEEN_SIDE_END][jColor? Chess.WHITE_MAIN_ROW : Chess.BLACK_MAIN_ROW];
                    if(theRook!=null && !theRook.hasMoved){ //The rook hasn't moved
                        if(GameMap[x1 - 1][y1] == null && GameMap[x1 - 2][y1] == null&& GameMap[x1 - 3][y1] == null){
                            return true;
                        }else{
                            Reason.value = "While castling, there should be no pieces between the king and the rook!";
                            return false;
                        }
                    }else{
                        Reason.value="Once your rook has moved, you can't castle anymore!";
                        return false;
                    }
                }
            }else if(dy==0 && dx==2 && hasMoved){
                Reason.value="Once your king has moved, you can't castle anymore!";
                return false;
            }
        }else if(dx==2){
            Reason.value = "You can't castle when you are in check!";
            return false;
        }

        if( !((dx==0&&dy==1)||(dx==1&&dy==0)||(dx==1&&dy==1)) ){
            Reason.value = "A king can only move one cell away!";
            return false;
        }
        return Target == null || Target.jColor != color;
    }

    public King(boolean nColor){
        super("King","K",nColor);
    }
}
class Queen extends Piece {

    /*   public Queen(Piece p){
        super(p);
    }*/
    public Queen(){
        this(true);
    }


    protected boolean IsValidBasicMove(int x1, int y1, int x2, int y2, Piece[][] GameMap, StringHolder Reason) {
        boolean BishopValid=Bishop.StaticIsValidBasicMove(x1,y1,x2,y2,GameMap,Reason);
        boolean RookValid=Rook.StaticIsValidBasicMove(x1,y1,x2,y2,GameMap,Reason);
        if(!RookValid && !BishopValid){
            Reason.value = "A queen can only move diagonally, along ranks or files and can't jump over other pieces!";
        }
        return RookValid || BishopValid;
    }

    public Queen(boolean nColor){
        super("Queen","Q",nColor);
    }
}
class Knight extends Piece {


  /*  public Knight(Piece p){
        super(p);
    }*/
    public Knight(){
        this(true);
    }

    protected boolean IsValidBasicMove(int x1, int y1, int x2, int y2, Piece GameMap[][], StringHolder Reason) {
        boolean color=GameMap[x1][y1].jColor;
        int dx=x2-x1,dy=y2-y1;
        dx=Math.abs(dx);
        dy=Math.abs(dy);
        Piece Target=GameMap[x2][y2];
        if((dx==1 && dy==2)||(dx==2 && dy==1) ){
            return Target == null || (Target.jColor != color);
        }else{
            Reason.value = "A knight can only move in L-shaped paths!";
            return false;
        }
    }

    public Knight(boolean nColor){
        super("Knight","N",nColor);
    }
}

