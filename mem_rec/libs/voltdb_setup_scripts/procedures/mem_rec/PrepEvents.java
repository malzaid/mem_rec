import org.voltdb.*;

public class PrepEvents extends VoltProcedure {

  public final SQLStmt getEvents = new SQLStmt(
		  "SELECT DISTINCT movieid FROM ratings;" );

  public VoltTable[] run()
      throws VoltAbortException {

          voltQueueSQL( getEvents );
          return voltExecuteSQL();

      }
}