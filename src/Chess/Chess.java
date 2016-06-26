package Chess;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.omg.CORBA.StringHolder;

/**
 * Created by IntelliJ IDEA.
 * By:  Javad Nouri
 * Iran University of Scince and Technology
 * Student Number: 85521487
 * Date: Jul 6, 2007
 * Time: 9:23:21 PM
 * ..............
 * The Main Chess Game.
 */
public class Chess extends JFrame{
    public static final int WHITE_MAIN_ROW = 7;
    public static final int BLACK_MAIN_ROW =0 ;
    public static final int BLACK_PAWN_ROW = 1;
    public static final int WHITE_PAWN_ROW = 6;
    public static final int WHITE_MIDDLE_ROW =5 ;
    public static final int BLACK_MIDDLE_ROW =2 ;
    public static final int QUEEN_COLUMN = 3;
    public static final int KING_COLUMN = 4;
    public static final int QUEEN_SIDE_END = BLACK_MAIN_ROW;
    public static final int KING_SIDE_END = WHITE_MAIN_ROW;
    //The Container:
    Container container=getContentPane();
    //

    //Components
    private ChessBoard pnlChess;
    
    private Cemetery chessCemetery=new Cemetery(); //The cemetery
    private ChessClock cClock;    //The Clock for the game
    private JButton btnNewGame;
    private JTextArea lblMessage;
    private JScrollPane bar;
    private JTextArea txtMoveList;
    private JTextField txtMovement;
    private JButton btnMoveString;

    private JPanel pnlSettings=new JPanel(true);
    /*private JFormattedTextField txtHour = new JFormattedTextField(new DecimalFormat("00"));
    private JFormattedTextField txtMinute = new JFormattedTextField(new DecimalFormat("00"));
    private JFormattedTextField txtSecond = new JFormattedTextField(new DecimalFormat("00"));*/
    private JTextField txtHour = new JTextField("00",1);
    private JTextField txtMinute = new JTextField("10",1);
    private JTextField txtSecond = new JTextField("00",1);
//    private JTextField =new JTextField("00",2),txtMinute=new JTextField("10",2),txtSecond=new JTextField("00",2);
    private JToggleButton tglDrag=new JToggleButton("Move on drag",false);
    private JComboBox cmbChesSets=new JComboBox();
    //End Components

    //Constants:
    //public final static int Piece.getWIDTH()=Piece.Piece.getWIDTH();
    //End Constants

    /**holds the number of moves made. **/
    private int MoveCount=0;

    /**Determines if the user has selected any piece to move.*/
    private boolean IsRaised=false;

    /**True: White's turn, False; Black's turn.**/
    private boolean Turn=true;

    /**the X position of the selected piece (to be moved) on the board.**/
    private int SelectedX=-1;
    /**the Y position of the selected piece (to be moved) on the board.**/
    private int SelectedY=-1;

    /**An 8 by 8 matrix which simulates the game board.**/
    Piece[][] GameMap=new Piece[8][8];
    /**An 8 by 8 matrix which determines how the ij'th cell is going to be painted on the next update.**/
    int[][] GameMapBordered=new int[8][8];

    private static final int ORDINARY = 0;
    private static final int IN_CHECK =1 ;
    private static final int WON = 2;
    private static final int ILLEGAL_MOVE = 3;
    private static final int DRAW = 4;
    private boolean gameEnded=false;
    private Piece theRaised;
    private int dx=0;
    private int dy=0;
    private int RaisedX;
    private int RaisedY;
    private int previousX;
    private int previousY;



    /**Default Constructor of the class Chess:**/
    public Chess(){
        super("Java final project - Spring semester 1385-86 - Iran University of Scince and Technology - Javad Nouri - SN: 85521487");
        InitializeComponents();
        InitializeListeners();
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        NewGame();
    }//

    /**
     * Creates a new set of pieces an puts them at their appropriate position on the board.
     */
    private void CreatePieces(){
        pnlChess.setVisible(false); //hide the ChessBoard to avoid screen vibratoin while Creating the pieces
        for(int j=0 ; j<8 ; j++){
            for(int i=0 ; i<8 ; i++){
                if(j== WHITE_PAWN_ROW){
                    GameMap[i][j]=new Pawn(true);
                }else if(j== BLACK_PAWN_ROW){
                    GameMap[i][j]=new Pawn(false);
                }else{
                    GameMap[i][j]=null;
                }
                GameMapBordered[i][j]=0;
            }
        }//end for
        GameMap[0][WHITE_MAIN_ROW]=new Rook(true);       //King Side White Rook
        GameMap[7][WHITE_MAIN_ROW]=new Rook(true);       //QueenSide White Rook
        GameMap[0][BLACK_MAIN_ROW]=new Rook(false);      //king Side Black Rook
        GameMap[7][BLACK_MAIN_ROW]=new Rook(false);      //QueenSide Black Rook

        GameMap[2][WHITE_MAIN_ROW]=new Bishop(true);     //King Side White Bishop
        GameMap[5][WHITE_MAIN_ROW]=new Bishop(true);     //QueenSide White Bishop
        GameMap[2][BLACK_MAIN_ROW]=new Bishop(false);    //King Side Black Bishop
        GameMap[5][BLACK_MAIN_ROW]=new Bishop(false);    //QueenSide Black Bishop

        GameMap[1][WHITE_MAIN_ROW]=new Knight(true);     //King Side White Knight
        GameMap[6][WHITE_MAIN_ROW]=new Knight(true);     //QueenSide White Knight
        GameMap[1][BLACK_MAIN_ROW]=new Knight(false);    //King Side Black Knight
        GameMap[6][BLACK_MAIN_ROW]=new Knight(false);    //QueenSide Black Knight

        GameMap[KING_COLUMN][WHITE_MAIN_ROW]=new King(true);       //White King
        GameMap[KING_COLUMN][BLACK_MAIN_ROW]=new King(false);      //Black King

        GameMap[QUEEN_COLUMN][WHITE_MAIN_ROW]=new Queen(true);      //White Queen
        GameMap[QUEEN_COLUMN][BLACK_MAIN_ROW]=new Queen(false);     //Black Queen
        pnlChess.setVisible(true);          //Show the hideen ChessBoard
        repaint();
    }

    /**
     * Moves the piece on [x1][y1] to [x2][y2] if it is valid,
     * otherwise throws an IllegalMoveException.
     *
     * @param x1    X coordinate of the source piece
     * @param y1    Y coordinate of the source piece
     * @param x2    X coordinate of the target cell
     * @param y2    Y coordinate of the target cell
     * @return      the movement in string format (e.g. Nf6)
     * @throws IllegalMoveException     if the move is not valid an IllegalMoveException is thrown.
     */
    String Move(int x1,int y1, int x2, int y2)throws IllegalMoveException{
        if(gameEnded)   return "no game!";
        StringHolder theReason=new StringHolder();
        String Result="";          //Holds the movement
        String Message="";      //The status message
        boolean Captured;       //is true when a piece is captured during the move
        boolean Checked=false;  //is true when the move puts the opponent into check
        boolean Castled=false;  //is true when the plyer castles
        Piece thePiece=GameMap[x1][y1]; //get the Piece which will be moved
        if(thePiece==null){     //if the selected cell is empty
            Message+="You cant move empty cells!!!\t";
            throw new IllegalMoveException(Message);
        }
        if(thePiece.jColor!=Turn){
            throw new IllegalMoveException("You can't move your opponents pieces! It is "+(Turn?"White":"Black")+"'s turn.");
        }
        if(thePiece.IsValidMove(x1,y1,x2,y2,Turn,GameMap,theReason)){     //if the move is valid
            thePiece.hasMoved = true;           //set hsMoved to true
            Captured=(GameMap[x2][y2]!=null);   //if there is a piece in the target cell, it'll be captured
            if(GameMap[x2][y2]!=null){
                chessCemetery.Buerry(GameMap[x2][y2]);
                chessCemetery.repaint();
            }
            //Move the Piece
            GameMap[x2][y2]=thePiece;
            GameMap[x1][y1]=null;

            //Set both the target and the source to be updated next time
            GameMapBordered[x1][y1]=ChessBoard.UPDATEONCE;
            GameMapBordered[x2][y2]=ChessBoard.UPDATEONCE;

            if((y2== BLACK_MAIN_ROW && thePiece.getClass()==Pawn.class && thePiece.jColor) || (y2== WHITE_MAIN_ROW && thePiece.getClass()==Pawn.class && !thePiece.jColor)){
                //a pawn has reached the end of it's way, and it should be promoted
                pnlChess.UpdateBoard();
                GameMap[x2][y2]=Promotion.Promote(GameMap[x2][y2]);     //Promote the pawn
                GameMapBordered[x2][y2]=ChessBoard.UPDATEONCE;          //Update the cell  
            }
            
            if(thePiece.getClass()==King.class && Math.abs(x2-x1)==2){  //The player wants to castle
                Piece theRook;                  //The rook which will take part in castling
                int RookX=x2>x1?7:0;            //the X position of theRook
                int RookY=thePiece.jColor? WHITE_MAIN_ROW : BLACK_MAIN_ROW;  //the Y position of theRook
                theRook=GameMap[RookX][RookY];  //set theRook
                GameMap[x2>x1?x2-1:x2+1][y1]=theRook;   // Castle
                Result=(x2>x1?"O-O":"O-O-O");
                Castled=true;
                GameMapBordered[RookX][RookY]=ChessBoard.UPDATEONCE;
                GameMapBordered[x2>x1?x2-1:x2+1][y1]=ChessBoard.UPDATEONCE;
                GameMap[RookX][RookY]=null;
                Message+=(thePiece.jColor?"White":"Black") + " made a "+(x2>x1?"King":"Queen")+" side castling!\t";
            }
            Turn=!Turn;     //Change the turn
            String checkStatus=Chess.IsChecked(GameMap);    //Determine if the player has put his/her opponent's king into check
            if(checkStatus.equals("w")){    //White is in CHECK
                Message+="White is in CHECK!\t";
                Checked=true;
            }else if(checkStatus.equals("b")){  //Black is in CHECK
                Message+="Black is in CHECK!\t";
                Checked=true;
            }
            Message+=(Turn?"White":"Black") +" to move.\t";
            ControlGame();
        }else{
            throw new IllegalMoveException(theReason.value);   //Invalid move!
        }

        //set The Result:
        if(!Castled)
            Result=GameMap[x2][y2].Abbr+(Captured?"x":"")+CellName(x2,y2);
        if(Checked){
            Result+="+";
            if(gameEnded)
                Result+="+";
        }
        if(!Turn){
            txtMoveList.append(Integer.toString(++MoveCount)+". ");
        }
        txtMoveList.append(Result);
        
        if(!Turn){
            for(int i=0 ; i<4-Result.length() ; i++){
                txtMoveList.append("_");
            }
            txtMoveList.append("_____");
        }else{
            txtMoveList.append("\n");
        }
        if(!gameEnded)  {
            setMessage(Message,(Checked?IN_CHECK:ORDINARY));
            cClock.flip();      //Shut the clock
        }
        return Result;      //
    }

    /**
     *
     * @param movement  The move in string.
     * @return
     * @throws IllegalMoveException
     */
    public String Move(String movement) throws IllegalMoveException {
        if(gameEnded)   return "no game!";
        StringHolder theReason=new StringHolder();
        String onlyMove=movement;
        boolean abbriviated=true;
        char[] chrTemp=onlyMove.toCharArray();
        onlyMove="";
        for(char aChar : chrTemp){
            if(aChar!=':' && aChar!='x' && aChar!='X' && aChar!='!' && aChar!='?'){
                onlyMove+=aChar;
            }
        }
        //Castling Case
        if(onlyMove.equalsIgnoreCase("O-O-O")||onlyMove.equalsIgnoreCase("O-O")){
            int KingX=-1,KingY=-1;
            outer:
            for(int i=0 ; i<8 ; i++){
                for(int j=0 ; j<8 ; j++){
                    if(GameMap[i][j]!=null && GameMap[i][j].getClass()==King.class && GameMap[i][j].jColor==Turn){
                        KingX=i;
                        KingY=j;
                        break outer;
                    }
                }
            }
            if(onlyMove.equalsIgnoreCase("O-O")){
                if( GameMap[KingX][KingY].IsValidMove(KingX,KingY,KingX+2,KingY,Turn,GameMap,theReason) && !GameMap[KingX][KingY].hasMoved)
                    return Move(KingX,KingY,KingX+2,KingY);
                else
                    throw new IllegalMoveException(theReason.value);
            }else{
                if( GameMap[KingX][KingY].IsValidMove(KingX,KingY,KingX-2,KingY,Turn,GameMap,theReason) && !GameMap[KingX][KingY].hasMoved)
                    return Move(KingX,KingY,KingX-2,KingY);
                else
                    throw new IllegalMoveException(theReason.value);
            }
        }//End Castling case

        abbriviated=(onlyMove.length()<=3);
        
        int cStart=1;
        String abbr=Character.toString(onlyMove.charAt(0));
        if(Character.isDigit(onlyMove.charAt(1))){
            cStart=0;
            abbr="";
        }

        int targetX=-1,targetY=-1;
        int sourceX=-1,sourceY=-1;
        if(abbriviated){
            targetX=Character.toLowerCase(onlyMove.charAt(cStart++))-(int)'a';
            targetY=onlyMove.charAt(cStart)-'1';
            targetY=(WHITE_MAIN_ROW ==0?targetY:7-targetY);
            outer2:
            for(int i=0 ; i<8 ; i++){
                for(int j=0 ; j<8 ; j++){
                    if(GameMap[i][j]!=null && GameMap[i][j].Abbr.equalsIgnoreCase(abbr) && GameMap[i][j].jColor==Turn){
                        sourceX=i;
                        sourceY=j;
                        if(GameMap[i][j].IsValidMove(i,j,targetX,targetY,Turn,GameMap,theReason))
                            break outer2;
                    }
                }
            }
            return Move(sourceX,sourceY,targetX,targetY);
        }
        //Not abbriviated
        sourceX=Character.toLowerCase(onlyMove.charAt(cStart++))-(int)'a';
        sourceY=onlyMove.charAt(cStart++)-'1';
        sourceY=(WHITE_MAIN_ROW ==0?sourceY:7-sourceY);
        targetX=Character.toLowerCase(onlyMove.charAt(cStart++))-(int)'a';
        targetY=onlyMove.charAt(cStart)-'1';
        targetY=(WHITE_MAIN_ROW ==0?targetY:7-targetY);
        return Move(sourceX,sourceY,targetX,targetY);
    }
    /**
     * Simulates mouse click at point MouseX, MouseY.
     *
     * If the user has selected a piece to move, this method tries
     * to move that piece, otherwise it selects the clicked piece (if there is any).
     * 
     * if the user has selected a piece before to move but clicks on a piece of the same color,
     * the second one is reselected to be moved on the next click.
     *
     * @param MouseX    The x elemet of the point.
     * @param MouseY    The y elemet of the point.
    **/
    public void ClickMouse(int MouseX, int MouseY){
        StringHolder theReason=new StringHolder();
        int x=MouseX/Piece.getWIDTH();
        int y=MouseY/Piece.getWIDTH();
        if(!IsRaised){
            if(GameMap[x][y]==null) return;
            if(GameMap[x][y].jColor!=Turn){
//                JOptionPane.showMessageDialog(null,"It is "+(Turn?"White":"Black")+"'s turn","Error",JOptionPane.ERROR_MESSAGE);
                String Message="You can't move your opponent's piece!\n";
                Message+="It is "+(Turn?"White":"Black")+"'s turn";
                setMessage(Message,ILLEGAL_MOVE);
                return;
            }
            int AvailableMoves=0;
            for(int i=0; i<8 ; i++){
                for(int j=0 ; j<8 ; j++){
                    boolean BasicValid=GameMap[x][y].IsValidBasicMove(x,y,i,j,GameMap,theReason);
                    boolean GeneralValid=GameMap[x][y].IsValidMove(x,y,i,j,Turn,GameMap,theReason);
                    if(GeneralValid){
                        GameMapBordered[i][j]=1;
                        AvailableMoves++;
                    }else if(BasicValid){    //in Check
                        GameMapBordered[i][j]=2;
                        AvailableMoves++;
                    }
                }
            }
            if(AvailableMoves>0){
                SelectedX=x;
                SelectedY=y;
                IsRaised=true;
            }
        }else{
            IsRaised=false;
            for(int i=0 ; i<8 ; i++){
                    for(int j=0 ; j<8 ; j++){
                        if(GameMapBordered[i][j]!=0)
                            GameMapBordered[i][j]=5;
                    }
            }
            try {
                if(!(SelectedX==x&&SelectedY==y)){
                    Move(SelectedX,SelectedY,x,y);
                }
            } catch (IllegalMoveException e1) {
                if(GameMap[x][y]!=null && GameMap[SelectedX][SelectedY].jColor == GameMap[x][y].jColor){
                    //JOptionPane.showMessageDialog(null,"Rep");
                    ClickMouse(x*Piece.getWIDTH(),y*Piece.getWIDTH());
//                    pnlChess.UpdateBoard();
//                    return;
                }else{
                    theReason.value="SSSSSSSSSSSSSSSSS";
                    //GameMap[SelectedX][SelectedY].IsValidMove(SelectedX, SelectedY, x,y,Turn,GameMap,theReason);
                    setMessage("This is not a valid move because:\t"+e1.getMessage(),ILLEGAL_MOVE);
                }
            }
        }
        pnlChess.UpdateBoard();
    }



    /**
     * Checks whether the kings on the recieved GameBoard(here GameMap) are in check or not.
     * @param GameMap   The game board which the checking will be performed on.
     * @return          "w" if White is in check, "b" if Black is in check and "wb" if both the kings are in check (well the latter is added to avoid this situation!!).
     *
     */
    public static String IsChecked(Piece[][] GameMap){
        StringHolder theReason=new StringHolder();
        int WhiteKingX=-1,WhiteKingY=-1,BlackKingX=-1,BlackKingY=-1;    //These four integers hold the position of the Kings on the board.

        boolean WhiteChecked=false;     //true if White is in check
        boolean BlackChecked=false;     //true if Black is in check

        //Get the position of the kings on the board:
        for(int i=0 ; i<8 ; i++){
            for(int j=0 ; j<8 ; j++){
                if(GameMap[i][j]!=null && GameMap[i][j].getClass()==King.class){
                    if(GameMap[i][j].jColor){
                        WhiteKingX=i;
                        WhiteKingY=j;
                    }else{
                        BlackKingX=i;
                        BlackKingY=j;
                    }
                }
            }
        }//

        //Check to see if the kings can be catured in the next move (actually the Check status
        for(int i=0 ; i<8 ; i++){
            for(int j=0 ; j<8 ; j++){
                if(GameMap[i][j]==null){
                    //There is no piece on the cell
                    continue;
                }
                if(GameMap[i][j].IsValidBasicMove(i,j,WhiteKingX, WhiteKingY,GameMap,theReason)){
                    //The white king can be captured
                    WhiteChecked=true;
                }
                if(GameMap[i][j].IsValidBasicMove(i,j,BlackKingX,BlackKingY,GameMap,theReason)){
                    //The black king can be captured
                    BlackChecked=true;
                    
                }
            }
        }

        
        GameMap[WhiteKingX][WhiteKingY].IsInCheck=WhiteChecked;
        GameMap[BlackKingX][BlackKingY].IsInCheck=BlackChecked;

        String theResult="";    //Will be returned
        if(WhiteChecked){
            theResult+="w";     //White is in check
        }
        if(BlackChecked){
            theResult+="b";     //White is in check
        }

        
        return theResult;
    }


    /**
     *  Shows theMessage on the Screen.
     * @param theMessage    The text which will be displayed on the status shower.
     * @param messageType   Describes how the text is going to be pained.
     */
    private void setMessage(String theMessage,int messageType){
        Color messageColor=null;
        switch(messageType){
            case IN_CHECK:
                messageColor=Color.RED;
                break;
            case WON:
                messageColor=new Color(0,100,0);
                break;
            case DRAW:
                messageColor=new Color(100,100,0);
                break;
            case ILLEGAL_MOVE:
                messageColor=new Color(100,20,30);
                break;
            default:
                messageColor=Color.BLUE;
        }
        lblMessage.setForeground(messageColor);
        lblMessage.setText(theMessage); //Set the text of the JTextArea
    }

    //Starting point of the program
    public static void main(String[] args) {
        new Chess();
    }

    /**
     * Returns the name of the xy'th chess board cell (e.g. c3 , e7 , ...).
     * @param x     the X coordinate of the cell.
     * @param y     the Y coordinate of the cell.
     * @return      the popular 'name' of the cell.
     */
    String CellName(int x, int y){
        String[] Files=new String[8];   //Holds the names of the files
        String[] Ranks=new String[8];   //Holds the names of the ranks
        for(int i=0 ; i<8 ; i++){
            Files[i]=Character.toString((char)('a'+(char)i));   //set the value of the Files : 'a' to 'h'
            Ranks[i]=Integer.toString(8-i);                     //set the value of the Ranks : 1 to 8
        }
        return Files[x]+Ranks[y];   //return the result
    }

    /**
     * Initializes and puts all the GUI components on the Chess
     */
    private void InitializeComponents(){
        //Initialize the label:
        lblMessage = new JTextArea();
        lblMessage.setBackground(this.getBackground());
        lblMessage.setFocusable(false);
        lblMessage.setEditable(false);

        lblMessage.setFont(new Font("Times New Roman",Font.BOLD,15));
        lblMessage.setSize(300,100);
        lblMessage.setLocation(Piece.getWIDTH()*9,0);
        //
        //Initialize txtMovement
        txtMovement=new JTextField(10);
        //
        //Initialize btnMoveString
        btnMoveString=new JButton("Move!");
        //
        
        //Initialize txtMoveList and put it in a JScrollPane:
        txtMoveList=new JTextArea();
        bar= new JScrollPane(txtMoveList);
        bar.setSize(120,Piece.getWIDTH()*9-160-30);
        bar.setLocation(Piece.getWIDTH()*9,160);
        txtMoveList.setEditable(false);
        //
        //Initialize the Clock
        cClock=new ChessClock(this);
        cClock.setSize(100,80);
        cClock.setLocation(Piece.getWIDTH()*9,50);
        //
        //Initialize the ChessBoard pnlChess
        pnlChess=new ChessBoard(this);
        pnlChess.setSize(Piece.getWIDTH()*8,Piece.getWIDTH()*8);
        pnlChess.setLocation(15,5);
        //
        //Initialize the cemetery
        
        chessCemetery.setLocation(100,100);
        //
        //Initialize btnNewGame
        btnNewGame=new JButton("New Game");
        btnNewGame.setSize(100,30);
        btnNewGame.setLocation(720,10);

        //Initialize Settings part:
        pnlSettings.setBorder(new BevelBorder(0));
        pnlSettings.setLayout(new FlowLayout());
        pnlSettings.add(new JLabel("Chess Set:"));
        pnlSettings.add(cmbChesSets);
        pnlSettings.add(new JLabel("Time:"));
        txtHour.setText("00");
        pnlSettings.add(txtHour);
        pnlSettings.add(new JLabel(":"));
        txtMinute.setText("10");
        pnlSettings.add(txtMinute);
        pnlSettings.add(new JLabel(":"));
        txtSecond.setText("00");
        pnlSettings.add(txtSecond);
        pnlSettings.add(tglDrag);
        //
        //Add the Components to the container
        container.add(pnlChess);
        container.add(cClock);
        container.add(chessCemetery);
        container.add(btnNewGame);
        container.add(lblMessage);
        container.add(bar);
        container.add(txtMovement);
        container.add(btnMoveString);
        container.add(pnlSettings);

        //The temperory one
        JPanel pnlTemp=new JPanel();
        container.add(pnlTemp);
        pnlTemp.setVisible(false);
        SetPositions();
    }

    /**
     * Creates a new chess game.
     */
    public void NewGame(){
        try{
            int hour=Integer.parseInt(txtHour.getText());
            int minute=Integer.parseInt(txtMinute.getText());
            int second=Integer.parseInt(txtSecond.getText());
            if(hour<0 || minute<0 || second<0){
                throw new NumberFormatException();
            }
            minute+=(second/60);
            second%=60;
            hour+=(minute/60);
            minute%=60;
            if(hour==0 && minute<=5){
                JOptionPane.showMessageDialog(null,"The time you have entered is not so wise to play!\nThe default time (10 minutes) is assumed.","Too short time!",JOptionPane.WARNING_MESSAGE);
                minute=10;
                second=0;
            }
            txtHour.setText(Integer.toString(hour));
            txtMinute.setText(Integer.toString(minute));
            txtSecond.setText(Integer.toString(second));
            
            cClock.restart(hour,minute,second);       //Restart the clock.
            Turn=true;      //It is white's turn.
            IsRaised=false; //no piece is selected to move
            CreatePieces();
            MoveCount=0;    //the game has started so the re are no moves yet.
            txtMoveList.setText("");    //Clear the move list.
            chessCemetery.Clear();
            setMessage("Start the game, White should play.",ORDINARY);   //Let the user know the game has started.
            gameEnded=false;
            pnlChess.setEnabled(true);
            txtMovement.setEnabled(true);
            btnMoveString.setEnabled(true);
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null,"Please enter valid integers in the desired time fields.","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Sets all the event handlers for the components.
     */
    private void InitializeListeners(){
        //MouseListener for pnlChess : the ChessBoard
        pnlChess.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                if(pnlChess.isEnabled() && !tglDrag.isSelected())    ClickMouse(e.getX(),e.getY());
            }
            //The following method have empty bodies!
            public void mousePressed(MouseEvent e) {
                
                if(!tglDrag.isSelected())   return;
                int xIndex=e.getX()/Piece.getWIDTH();
                int yIndex=e.getY()/Piece.getWIDTH();
                theRaised=GameMap[xIndex][yIndex];
                if(theRaised!=null){
                    dx=e.getX()-xIndex*Piece.getWIDTH();
                    dy=e.getY()-yIndex*Piece.getWIDTH();
                    Chess.this.setCursor(Cursor.MOVE_CURSOR);
                    RaisedX=xIndex;
                    RaisedY=yIndex;
                    previousX=xIndex;
                    previousY=yIndex;
                }
            }
            public void mouseReleased(MouseEvent e) {
                if(!tglDrag.isSelected())   return;
                if(theRaised!=null){

                    int xIndex=e.getX()/Piece.getWIDTH();
                    int yIndex=e.getY()/Piece.getWIDTH();
                    try {
                        if(RaisedX!=xIndex||RaisedY!=yIndex)
                            Move(RaisedX,RaisedY,xIndex,yIndex);
                    } catch (IllegalMoveException e1) {
                        setMessage("This is not a valid move because:\t"+e1.getMessage(),ILLEGAL_MOVE);
                    }finally{
                        GameMapBordered[RaisedX][RaisedY]=ChessBoard.UPDATEONCE;
                        Chess.this.setCursor(Cursor.DEFAULT_CURSOR);
                        theRaised=null;
                        pnlChess.repaint();
                    }
                }
            }
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}

        });
        pnlChess.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
                if(!tglDrag.isSelected())   return;
                if(theRaised!=null && Turn==theRaised.jColor){

                    int x1=(e.getX()-dx)/Piece.getWIDTH();
                    int y1=(e.getY()-dy)/Piece.getWIDTH();
                    int x2;
                    int y2;
                    x1=(previousX<x1?previousX:x1);
                    y1=(previousY<y1?previousY:y1);
                    x2=(previousX>(e.getX()-dx)/Piece.getWIDTH()?previousX:(e.getX()-dx)/Piece.getWIDTH());
                    y2=(previousY>(e.getY()-dy)/Piece.getWIDTH()?previousY:(e.getX()-dy)/Piece.getWIDTH());

                    x2++;
                    y2++;
//                    x2+=Piece.getWIDTH();
//                    y2+=Piece.getWIDTH();
                    for(int x=x1;x<x2;x++){
                        for(int y=y1; y<y2;y++){
                            System.out.println(x+" , "+y);
                            GameMapBordered[x][y]=ChessBoard.UPDATEONCE;
                        }
                    }
                    GameMapBordered[previousX][previousY]=ChessBoard.UPDATEONCE;/*
                    GameMapBordered[previousX+1][previousY]=ChessBoard.UPDATEONCE;
                    GameMapBordered[previousX][previousY+1]=ChessBoard.UPDATEONCE;
                    GameMapBordered[previousX+1][previousY+1]=ChessBoard.UPDATEONCE;*/

                    previousX=e.getX()/Piece.getWIDTH();
                    previousY=e.getY()/Piece.getWIDTH();


                    boolean BasicValid=theRaised.IsValidBasicMove(RaisedX,RaisedY,previousX,previousY,GameMap,new StringHolder());
                    boolean GenericValid=theRaised.IsValidMove(RaisedX,RaisedY,previousX,previousY,Turn,GameMap,new StringHolder());

                    if(BasicValid ){
                        if(GenericValid)
                            GameMapBordered[previousX][previousY]=ChessBoard.VALIDMOVE;
                        else
                            GameMapBordered[previousX][previousY]=ChessBoard.VALIDBUTCHECK;
                    }else{
                        GameMapBordered[previousX][previousY]=0;
                    }
                    pnlChess.UpdateBoard();
                    //pnlChess.repaint();
                    theRaised.Draw(pnlChess.getGraphics(),pnlChess,e.getX()-dx,e.getY()-dy);
                }
            }

            public void mouseMoved(MouseEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        //ActionListener for btnNewGame
        btnNewGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                NewGame();
            }
        });
        
        //MouseWheelListener for pnlChess
        pnlChess.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                //Resize the game board
                double charpi=1+e.getWheelRotation()*0.1;
                int newWidth=(int)(Piece.getWIDTH()*charpi);
                if(newWidth>=30 && newWidth<=83){
                    Piece.setWIDTH(newWidth);
                    pnlChess.setSize(Piece.getWIDTH()*8,Piece.getWIDTH()*8);
                    SetPositions();
                    repaint();
                }
            }
        });
        //Action Listener for btnMoveString
        btnMoveString.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!gameEnded){
                    try {
                        Move(txtMovement.getText());
                        pnlChess.repaint();
                        chessCemetery.repaint();
                        txtMovement.selectAll();
                        txtMovement.grabFocus();
                    } catch (IllegalMoveException e1) {
                        setMessage("\""+txtMovement.getText()+"\" is not a valid move because:\t"+e1.getMessage(),ILLEGAL_MOVE);
                    }catch (Exception e2){
                        setMessage("Please enter a valid move in the text field.",ILLEGAL_MOVE);
                    }
                }
            }
        });
        //Action Listener for txtMovement
        txtMovement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnMoveString.doClick();
            }
        });

        tglDrag.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if(tglDrag.isSelected() && IsRaised){
                    ClickMouse(SelectedX*Piece.getWIDTH(),SelectedY*Piece.getWIDTH());
                }
            }
        });
    }

    /**
     * Sets the positions of the components on the JFrame.
     */
    private void SetPositions(){
        int cWidth=130;
        int dx=5,dy=5;
        int x=0;
        int y=0;
        x+=pnlChess.getLocation().getX();
        lblMessage.setSize(pnlChess.getWidth()+cWidth+dx,40);
        lblMessage.setLocation(x,(int) pnlChess.getLocation().getY()+pnlChess.getHeight()+3*dy);
        
        x+=pnlChess.getWidth();
        x+=dx;
        y+=pnlChess.getLocation().getY();
        chessCemetery.setLocation(x,y);
        chessCemetery.resize();
        x+=chessCemetery.getWidth();
        x+=dx;
        btnNewGame.setSize(cWidth,25);
        btnNewGame.setLocation(x,y);
        y+=btnNewGame.getHeight();
        y+=dy;
        cClock.setSize(cWidth,cClock.getHeight());
        cClock.setLocation(x,y);
        y+=cClock.getHeight();
        y+=dy;

        bar.setSize(cWidth,pnlChess.getHeight()+(int)pnlChess.getLocation().getY()-y);
        bar.setLocation(x,y);
        y+=bar.getSize().getHeight();
        y+=dy;
        y=(int) pnlChess.getLocation().getY();
        x+=cWidth;
        x+=dy;
        txtMovement.setSize(cWidth,25);
        txtMovement.setLocation(x,y);
        y+=txtMovement.getHeight();
        y+=dy;
        btnMoveString.setSize(cWidth,25);
        btnMoveString.setLocation(x,y);
        y+=btnMoveString.getHeight();
        y+=dy;
        pnlSettings.setSize(cWidth,100);
        pnlSettings.setLocation(x,y);
        y+=pnlSettings.getHeight();
        y+=dy;

    }

    public void paint(Graphics g) {
        super.paint(g);
        char theFile;   //Holds the names of the file
        char theRank;    //Holds the names of the rank
        for(int i=0 ; i<8 ; i++){
            theFile=(char)('a'+(char)i);   //set the value of the Files : 'a' to 'h'
            theRank=(char)((int)'1'+7-i);    //set the value of the Ranks : 1 to 8
            int drawX=(int) pnlChess.getLocation().getX()+Piece.getWIDTH()/2+i*Piece.getWIDTH();

            //pnlChess.setVisible(false);
            int drawY=(int) pnlChess.getLocation().getY();
            drawY+=pnlChess.getHeight();
            drawY+=40;
            g.drawString(Character.toString(theFile),drawX,drawY);
            drawX=9;
            drawY=(int) pnlChess.getLocation().getY()+30+Piece.getWIDTH()/2+i*Piece.getWIDTH();
            g.drawString(Character.toString(theRank),drawX,drawY);
        }
    }
    private static boolean ThereAreMoreMoves(Piece[][] gameMap, boolean turn){
        for(int i=0 ; i<8 ; i++){
            for(int j=0 ; j<8 ; j++){
                if(gameMap[i][j]!=null && gameMap[i][j].jColor==turn){
                    for(int k=0 ; k<8 ; k++){
                        for(int l=0 ; l<8 ; l++){
                            if(gameMap[i][j].IsValidMove(i,j,k,l,turn,gameMap,new StringHolder())){
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    void ControlGame(){
        if(cClock.WhitePlayer.Over){
            //White has lost
            setMessage("Black won the game because white's time is up!!",WON);
            EndGame();
            return;
        }else if(cClock.BlackPlayer.Over){
            //Black has lost
            setMessage("White won the game because black's time is up!!",WON);
            EndGame();
            return;
        }
        boolean CanBeDraw=!ThereAreMoreMoves(GameMap,Turn);
        boolean GameIsInCheck=(IsChecked(GameMap).equals(Turn?"w":"b"));
        if(CanBeDraw && !GameIsInCheck){
            //Draw;
            setMessage("Draw because there are no possible moves for "+(Turn?"white":"black")+"!!",DRAW);
            EndGame();
        }else if(CanBeDraw && GameIsInCheck){
            EndGame();
            setMessage((!Turn?"White":"Black")+" won the game! "+(Turn?"White":"Black")+" is checkmated!",WON);
            //CheckMate
        }
        int pieceCount=(Turn?chessCemetery.WhiteDead.size():chessCemetery.BlackDead.size());
        pieceCount=16-pieceCount;
        if(pieceCount==1){
            EndGame();
            setMessage("Draw because there is only one piece left for "+(Turn?"white":"black")+"!!",DRAW);
            //Draw
        }
    }
    private void EndGame(){
        gameEnded=true;
        txtMovement.setEnabled(false);
        btnMoveString.setEnabled(false);
        pnlChess.setEnabled(false);
        cClock.stop();
    }
}