import org.voltdb.*;

public class PrepareUserEvents extends VoltProcedure {

  public final SQLStmt getUserID = new SQLStmt(
		  "SELECT userid, movieid, rating, timestamp FROM ratings WHERE userid = ?" );

  public VoltTable[] run(userid)
      throws VoltAbortException {

          voltQueueSQL( getUserID, userid );
          return voltExecuteSQL();

      }
}