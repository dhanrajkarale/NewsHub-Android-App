package com.dk.newshub

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(), NewsItemClicked {

    private  lateinit var mAdapter: NewsListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager=LinearLayoutManager(this)

        fetchData()
        mAdapter= NewsListAdapter(this)

        recyclerView.adapter=mAdapter
    }
    private fun fetchData() {

//        val url = "https://newsapi.org/v2/top-headlines?country=us&apiKey=your_api_key"
         val url="https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=4da7d471ef434194ae040aafc0cfc624"
        val jsonObjectRequest = object :JsonObjectRequest(Request.Method.GET, url, null,

            { response ->
                val newsJsonArray = response.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for (i in 0 until newsJsonArray.length()) {
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
                    )
                    newsArray.add(news)
                }

                mAdapter.updateNews(newsArray)

            },
            { _ ->

            })

        {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): HashMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"

                return headers
            }
        }


         MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)



    }

    override fun onItemClicked(item: News){
        val builder= CustomTabsIntent.Builder();
        val colorInt: Int = Color.parseColor("#000000") //black

        val defaultColors = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(colorInt)
            .build()
        builder.setDefaultColorSchemeParams(defaultColors)


        val customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(item.url));

    }
}
