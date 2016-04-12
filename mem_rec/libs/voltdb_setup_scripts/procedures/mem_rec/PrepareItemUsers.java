import org.voltdb.*;

public class PrepareItemEvents extends VoltProcedure {

  public final SQLStmt getMovieID = new SQLStmt(
		  "SELECT userid, movieid, rating, timestamp FROM ratings WHERE movieid = ?" );

  public VoltTable[] run(int movieid)
      throws VoltAbortException {

          voltQueueSQL( getMovieID, movieid );
          return voltExecuteSQL();

      }
}