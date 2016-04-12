import org.voltdb.*;

public class PrepareUsers extends VoltProcedure {

  public final SQLStmt getUsers = new SQLStmt(
		  "SELECT DISTINCT userid FROM ratings;" );

  public VoltTable[] run()
      throws VoltAbortException {

          voltQueueSQL( getUsers );
          return voltExecuteSQL();

      }
}