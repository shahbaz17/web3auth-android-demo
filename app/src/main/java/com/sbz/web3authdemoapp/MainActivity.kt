package com.sbz.web3authdemoapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.web3auth.core.Web3Auth
import com.web3auth.core.types.LoginParams
import com.web3auth.core.types.Provider
import com.web3auth.core.types.Web3AuthOptions
import com.web3auth.core.types.Web3AuthResponse
import com.web3auth.core.isEmailValid
import com.web3auth.core.types.*
import java8.util.concurrent.CompletableFuture


class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private lateinit var web3Auth: Web3Auth

    private val gson = Gson()

    private val verifierList: List<LoginVerifier> = listOf(
        LoginVerifier("Google", Provider.GOOGLE),
        LoginVerifier("Email Passwordless", Provider.EMAIL_PASSWORDLESS),
        LoginVerifier("JWT", Provider.JWT),
        LoginVerifier("Facebook", Provider.FACEBOOK),
        LoginVerifier("Twitch", Provider.TWITCH),
        LoginVerifier("Discord", Provider.DISCORD),
        LoginVerifier("Reddit", Provider.REDDIT),
        LoginVerifier("Apple", Provider.APPLE),
        LoginVerifier("Github", Provider.GITHUB),
        LoginVerifier("LinkedIn", Provider.LINKEDIN),
        LoginVerifier("Twitter", Provider.TWITTER),
        LoginVerifier("Line", Provider.LINE),
        LoginVerifier("WeChat", Provider.WECHAT),
        LoginVerifier("KaKao", Provider.KAKAO),
        LoginVerifier("Weibo", Provider.WEIBO),
    )

    private var selectedLoginProvider: Provider = Provider.GOOGLE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        web3Auth = Web3Auth(
//            Web3AuthOptions(
//                context = this,
//                clientId = getString(R.string.web3auth_project_id),
//                network = Web3Auth.Network.TESTNET,
//                redirectUrl = Uri.parse("com.sbz.web3authdemoapp://auth")
//            )
//        )

        // Handle user signing in when app is not alive

//      Web3Auth Login without Whitelabel

//        web3Auth = Web3Auth(
//            Web3AuthOptions(
//                context = this,
//                clientId = getString(R.string.web3auth_project_id),
//                network = Web3Auth.Network.TESTNET,
//                redirectUrl = Uri.parse("com.sbz.web3authdemoapp://auth")
//            )
//        )

//        // Handle user signing in when app is not alive
//        web3Auth.setResultUrl(intent?.data)

//      Web3Auth Whitelabel

        web3Auth = Web3Auth(
            Web3AuthOptions(
                context = this,
                clientId = getString(R.string.web3auth_project_id), // pass over your Web3Auth Client ID from Developer Dashboard
                network = Web3Auth.Network.TESTNET, // pass over the network you want to use (MAINNET or TESTNET or CYAN)
                redirectUrl = Uri.parse("com.sbz.web3authdemoapp://auth"), // your app's redirect URL
                // Optional parameters
                whiteLabel = WhiteLabelData(
                    "Web3Auth Whitelabel", null, null, "en", true,
                    hashMapOf(
                        "primary" to "#229954"
                    )
                )
            )
        )

//      Web3Auth Custom Authentication + Custom Authentication

//        web3Auth = Web3Auth(
//            Web3AuthOptions(
//                context = this,
//                clientId = getString(R.string.web3auth_project_id), // pass over your Web3Auth Client ID from Developer Dashboard
//                network = Web3Auth.Network.TESTNET, // pass over the network you want to use (MAINNET or TESTNET or CYAN)
//                redirectUrl = Uri.parse("com.sbz.web3authdemoapp://auth"), // your app's redirect URL
//                // Optional parameters
//                whiteLabel = WhiteLabelData(
//                    "Web3Auth dApp Share", null, null, "en", true,
//                    hashMapOf(
//                        "primary" to "#F1C40F"
//                    )
//                ),
//                loginConfig = hashMapOf("google" to LoginConfigItem(
//                    verifier = "web3auth-core-google",
//                    typeOfLogin = TypeOfLogin.GOOGLE,
//                    name = "Custom Google Login",
//                    clientId = getString(R.string.web3auth_google_client_id)
//                ))
//            )
//        )

// Handle user signing in when app is not alive

        web3Auth.setResultUrl(intent?.data)

        // Setup UI and event handlers
        val signInButton = findViewById<Button>(R.id.signInButton)
        signInButton.setOnClickListener { signIn() }

        val signOutButton = findViewById<Button>(R.id.signOutButton)
        signOutButton.setOnClickListener { signOut() }

        val spinner = findViewById<AutoCompleteTextView>(R.id.spinnerTextView)
        val loginVerifierList: List<String> = verifierList.map { item ->
            item.name
        }
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(this, R.layout.item_dropdown, loginVerifierList)
        spinner.setAdapter(adapter)
        spinner.onItemClickListener = this

        reRender(Web3AuthResponse())
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        // Handle user signing in when app is active
        web3Auth.setResultUrl(intent?.data)
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        selectedLoginProvider = verifierList[p2].loginProvider

        val hintEmailEditText = findViewById<EditText>(R.id.etEmailHint)
        if (selectedLoginProvider == Provider.EMAIL_PASSWORDLESS) {
            hintEmailEditText.visibility = View.VISIBLE
        } else {
            hintEmailEditText.visibility = View.GONE
        }

        val hintIdTokenEditText = findViewById<EditText>(R.id.idTokenHint)
        if (selectedLoginProvider == Provider.JWT) {
            hintIdTokenEditText.visibility = View.VISIBLE
        } else {
            hintIdTokenEditText.visibility = View.GONE
        }
    }

//    private fun signIn() {
//        val selectedLoginProvider = Provider.GOOGLE   // Can be GOOGLE, FACEBOOK, TWITCH etc.
////        val selectedLoginProvider = Provider.EMAIL_PASSWORDLESS
////        val loginCompletableFuture: CompletableFuture<Web3AuthResponse> = web3Auth.login(LoginParams(selectedLoginProvider, extraLoginOptions = ExtraLoginOptions(login_hint = "shahbaz.web3@gmail.com")))
//        val loginCompletableFuture: CompletableFuture<Web3AuthResponse> = web3Auth.login(LoginParams(selectedLoginProvider))
////        val loginCompletableFuture: CompletableFuture<Web3AuthResponse> = web3Auth.login(LoginParams(selectedLoginProvider, mfaLevel = MFALevel.MANDATORY ))
////        val loginCompletableFuture: CompletableFuture<Web3AuthResponse> = web3Auth.login(LoginParams(selectedLoginProvider, dappShare = "afford join neutral spoon bike glue ahead floor giant match primary style cycle front address gossip embark rose boy muscle tuition melt left question" ))
//        loginCompletableFuture.whenComplete { loginResponse, error ->
//            if (error == null) {
//                println(loginResponse)
//                reRender(loginResponse)
//            } else {
//                Log.d("MainActivity_Web3Auth", error.message ?: "Something went wrong" )
//            }
//        }
//    }

    private fun signIn() {
        val hintEmailEditText = findViewById<EditText>(R.id.etEmailHint)
        val hintIdTokenEditText = findViewById<EditText>(R.id.idTokenHint)
        var extraLoginOptions: ExtraLoginOptions? = null

        if (selectedLoginProvider == Provider.EMAIL_PASSWORDLESS) {
            val hintEmail = hintEmailEditText.text.toString()
            if (hintEmail.isBlank() || !hintEmail.isEmailValid()) {
                Toast.makeText(this, "Please enter a valid Email.", Toast.LENGTH_LONG).show()
                return
            }
            extraLoginOptions = ExtraLoginOptions(login_hint = hintEmail)
        }

        if (selectedLoginProvider == Provider.JWT) {
            val hintToken = hintIdTokenEditText.text.toString()
            if (hintToken.isBlank()) {
                Toast.makeText(this, "Please enter your JWT.", Toast.LENGTH_LONG).show()
                return
            }
            extraLoginOptions = ExtraLoginOptions(id_token_hint = hintToken)
        }

        val loginCompletableFuture: CompletableFuture<Web3AuthResponse> = web3Auth.login(
            LoginParams(selectedLoginProvider, extraLoginOptions = extraLoginOptions)
        )

        loginCompletableFuture.whenComplete { loginResponse, error ->
            if (error == null) {
                reRender(loginResponse)
            } else {
                Log.d("MainActivity_Web3Auth", error.message ?: "Something went wrong" )
            }
        }
    }

    private fun signOut() {
        val logoutCompletableFuture =  web3Auth.logout()
        logoutCompletableFuture.whenComplete { _, error ->
            if (error == null) {
                reRender(Web3AuthResponse())
            } else {
                Log.d("MainActivity_Web3Auth", error.message ?: "Something went wrong" )
            }
        }
        recreate()
    }

    private fun reRender(web3AuthResponse: Web3AuthResponse) {
        val contentTextView = findViewById<TextView>(R.id.contentTextView)
        val signInButton = findViewById<Button>(R.id.signInButton)
        val signOutButton = findViewById<Button>(R.id.signOutButton)
        val spinner = findViewById<TextInputLayout>(R.id.verifierList)
        val hintEmailEditText = findViewById<EditText>(R.id.etEmailHint)
        val hintIdTokenEditText = findViewById<EditText>(R.id.idTokenHint)

        val key = web3AuthResponse.privKey
        val userInfo = web3AuthResponse.userInfo
        println(userInfo)
        if (key is String && key.isNotEmpty()) {
            contentTextView.text = gson.toJson(web3AuthResponse)
            contentTextView.visibility = View.VISIBLE
            signInButton.visibility = View.GONE
            signOutButton.visibility = View.VISIBLE
            spinner.visibility = View.GONE
            hintEmailEditText.visibility = View.GONE
            hintIdTokenEditText.visibility = View.GONE
        } else {
            contentTextView.text = getString(R.string.not_logged_in)
            contentTextView.visibility = View.GONE
            signInButton.visibility = View.VISIBLE
            signOutButton.visibility = View.GONE
            spinner.visibility = View.VISIBLE
        }
    }


}