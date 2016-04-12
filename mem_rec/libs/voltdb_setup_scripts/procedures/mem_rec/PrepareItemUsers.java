import org.voltdb.*;

public class PrepareItemUsers extends VoltProcedure {
  public final SQLStmt getItemUsers = new SQLStmt(
		  "SELECT DISTINCT userid FROM ratings WHERE movieid = ?" );

  public VoltTable[] run(int movieid)
      throws VoltAbortException {

          voltQueueSQL( getItemUsers, movieid );
          return voltExecuteSQL();

      }
}