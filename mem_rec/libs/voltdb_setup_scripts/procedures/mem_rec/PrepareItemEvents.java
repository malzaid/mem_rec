import org.voltdb.*;

public class PrepareItemEvents extends VoltProcedure {

  public final SQLStmt getItemEvents = new SQLStmt(
		  "SELECT userid, movieid, rating, timestamp FROM ratings WHERE movieid = ?" );

  public VoltTable[] run(int movieid)
      throws VoltAbortException {

          voltQueueSQL( getItemEvents, movieid );
          return voltExecuteSQL();

      }
}