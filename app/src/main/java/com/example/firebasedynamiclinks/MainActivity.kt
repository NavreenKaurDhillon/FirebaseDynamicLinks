package com.example.firebasedynamiclinks

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import java.util.SplittableRandom

class MainActivity : AppCompatActivity() {

    lateinit var share : Button
    override fun onStart() {
        super.onStart()
        checkDynamicLinks()

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        share = findViewById(R.id.shareBT);
        share.setOnClickListener {
            sharePage()
        }

        //dynamic link creation


    }

    fun checkDynamicLinks() {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData: PendingDynamicLinkData? ->
                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                    var currentPage = deepLink?.getQueryParameter("currentPage");
                    Log.d(TAG, "onCreate: /// dynamic link " + currentPage)
                }

                // Handle the deep link. For example, open the linked
                // content, or apply promotional credit to the user's
                // account.

            }
            .addOnFailureListener(this) { e -> Log.w(TAG, "getDynamicLink:onFailure", e) }
    }

        fun sharePage()
        {
            val pageNumber = 9;
            // Generate the dynamic link
            val dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(PREFIX+"/page="+pageNumber))
                .setDomainUriPrefix(PREFIX)
                .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
                .buildDynamicLink()

            // Get the generated link as a string
            val dynamicLinkUri = dynamicLink.uri.toString()

            // Share the link using any sharing method you prefer
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, dynamicLinkUri)
            startActivity(Intent.createChooser(shareIntent, "Share Dynamic Link"))
    }

    companion object {
        const val PREFIX = "https://fbdynamiclinkk.page.link"
    }
}