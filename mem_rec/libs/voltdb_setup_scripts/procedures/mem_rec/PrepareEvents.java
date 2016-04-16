import org.voltdb.*;

public class PrepareEvents extends VoltProcedure {

  public final SQLStmt getEvents = new SQLStmt(
		  "SELECT DISTINCT movieid FROM ratings ORDER BY movieid;" );

  public VoltTable[] run()
      throws VoltAbortException {

          voltQueueSQL( getEvents );
          return voltExecuteSQL();

      }
}