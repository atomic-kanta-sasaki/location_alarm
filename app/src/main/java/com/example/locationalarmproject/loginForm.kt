import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.locationalarmproject.R
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_action)

        auth = FirebaseAuth.getInstance()

        val buttonSignUp = findViewById<Button>(R.id.SignUpButton)
        val buttonLogin = findViewById<Button>(R.id.LoginButton)



        buttonSignUp.setOnClickListener {

            val emailEditText = findViewById<EditText>(R.id.emailEditText)
            val emailText = emailEditText.text.toString()

            val passEditText = findViewById<EditText>(R.id.passEditText)
            val passText = passEditText.text.toString()


            auth.createUserWithEmailAndPassword(emailText, passText)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            baseContext, "SignUp 成功",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            baseContext, "SignUp 失敗",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        buttonLogin.setOnClickListener {

            val emailEditText = findViewById<EditText>(R.id.emailEditText)
            val emailText = emailEditText.text.toString()

            val passEditText = findViewById<EditText>(R.id.passEditText)
            val passText = passEditText.text.toString()

            auth.signInWithEmailAndPassword(emailText, passText)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            baseContext, "Login 成功",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            baseContext, "Login 失敗",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

        }
    }
}