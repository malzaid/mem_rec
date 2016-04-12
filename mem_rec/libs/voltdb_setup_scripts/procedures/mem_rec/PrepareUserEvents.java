import org.voltdb.*;

public class PrepareUserEvents extends VoltProcedure {

  public final SQLStmt getUserEvents = new SQLStmt(
		  "SELECT userid, movieid, rating, timestamp FROM ratings WHERE userid = ?" );

  public VoltTable[] run(int userid)
      throws VoltAbortException {

          voltQueueSQL( getUserEvents, userid );
          return voltExecuteSQL();

      }
}