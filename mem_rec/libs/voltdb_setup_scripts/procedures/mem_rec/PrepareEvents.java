import org.voltdb.*;

public class PrepareItems extends VoltProcedure {

  public final SQLStmt getItems = new SQLStmt(
		  "SELECT DISTINCT movieid FROM ratings;" );

  public VoltTable[] run()
      throws VoltAbortException {

          voltQueueSQL( getItems );
          return voltExecuteSQL();

      }
}