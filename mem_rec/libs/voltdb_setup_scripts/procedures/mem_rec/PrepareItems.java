import org.voltdb.*;

public class PrepareItems extends VoltProcedure {

  public final SQLStmt getEvents = new SQLStmt(
		  "SELECT userid, movieid, rating, timestamp FROM ratings;" );

  public VoltTable[] run()
      throws VoltAbortException {

          voltQueueSQL( getEvents );
          return voltExecuteSQL();

      }
}