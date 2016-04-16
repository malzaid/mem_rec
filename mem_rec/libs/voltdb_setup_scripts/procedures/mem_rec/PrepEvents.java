import org.voltdb.*;

public class PrepEvents extends VoltProcedure {

  public final SQLStmt getDistinctMovies = new SQLStmt(
		  "SELECT DISTINCT movieid FROM ratings;" );

  public VoltTable[] run()
      throws VoltAbortException {

          voltQueueSQL( getDistinctMovies );
          return voltExecuteSQL();

      }
}