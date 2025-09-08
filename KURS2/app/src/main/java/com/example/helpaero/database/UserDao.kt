import androidx.room.*
import com.example.helpaero.database.UserDB

@Dao
interface UserDao {
    @Insert
    suspend fun insert(userDB: UserDB)

    @Query("SELECT * FROM users WHERE login = :login AND password = :password LIMIT 1")
    suspend fun getUser(login: String, password: String): UserDB?
}