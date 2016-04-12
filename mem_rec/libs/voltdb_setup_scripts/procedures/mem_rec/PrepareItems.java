import org.voltdb.*;

public class PrepareItems extends VoltProcedure {

  public final SQLStmt getItems = new SQLStmt(
		  "SELECT userid, movieid, rating, timestamp FROM ratings;" );

  public VoltTable[] run()
      throws VoltAbortException {

          voltQueueSQL( getItems );
          return voltExecuteSQL();

      }
}