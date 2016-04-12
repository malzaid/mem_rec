import org.voltdb.*;

public class PrepareItemUsers extends VoltProcedure {

  public final SQLStmt getUser_movieid = new SQLStmt(
		  "SELECT DISTINCT userid FROM ratings WHERE movieid = ?" );

  public VoltTable[] run(int movieid)
      throws VoltAbortException {

          voltQueueSQL( getUser_movieid, movieid );
          return voltExecuteSQL();

      }
}