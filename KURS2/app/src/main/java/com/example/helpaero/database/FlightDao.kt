import androidx.room.*
import com.example.helpaero.database.FlightDB

@Dao
interface FlightDao {
    @Insert
    suspend fun insert(flightDB: FlightDB)

    @Query("SELECT * FROM flights")
    suspend fun getAllFlights(): List<FlightDB>

    @Query("SELECT * FROM flights WHERE userId = :userId")
    suspend fun getFlightsForUser(userId: Int): List<FlightDB>
}