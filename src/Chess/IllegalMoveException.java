package Chess;
/**
 * Created by IntelliJ IDEA.
 * By:  Javad Nouri
 * Student Number: 85521487
 * Iran University of Scince and Technology
 *
 */

public class IllegalMoveException extends Exception {
    private String reason="";
    public String getMessage(){
        return reason;
    }
    public IllegalMoveException(String theReason){
        reason=theReason;
    }
}
