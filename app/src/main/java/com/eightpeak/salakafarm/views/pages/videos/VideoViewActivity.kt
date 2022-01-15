package com.eightpeak.salakafarm.views.pages.videos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.eightpeak.salakafarm.databinding.ActivityVideoViewBinding
import com.eightpeak.salakafarm.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.widget.Toast

import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import coil.api.load
import com.eightpeak.salakafarm.serverconfig.ApiInterface
import com.eightpeak.salakafarm.utils.Constants.Companion.BASE_URL_VIDEO
import com.eightpeak.salakafarm.utils.EndPoints
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.URI
import com.google.gson.GsonBuilder

import com.google.gson.Gson
import kotlinx.android.synthetic.main.product_item.view.*
import okhttp3.OkHttpClient

import okhttp3.Interceptor
import okhttp3.Request
import android.content.Intent
import android.net.Uri
import androidx.cardview.widget.CardView
import com.eightpeak.salakafarm.R
import kotlinx.android.synthetic.main.activity_login.*


class VideoViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVideoViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.header.text=getString(R.string.our_videoes)
        getVideoLink()
//        retrofit.
      }
    private fun getVideoLink() {
        showProgressBar()
          val interceptor: Interceptor = object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                val newRequest: Request =
                    chain.request().newBuilder().addHeader("User-Agent", "Retrofit-Sample-App")
                        .build()
                return chain.proceed(newRequest)
            }
        }

        val builder = OkHttpClient.Builder()
        builder.interceptors().add(interceptor)
        val client: OkHttpClient = builder.build()


        val gson = GsonBuilder()
            .create()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_VIDEO)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()


        val post: ApiInterface = retrofit.create(ApiInterface::class.java)
        val call: Call<YoutubeVideoModel?>? = post.getVideoList()
        call?.enqueue(object : Callback<YoutubeVideoModel?> {
            override fun onResponse(
                call: Call<YoutubeVideoModel?>,
                response: Response<YoutubeVideoModel?>
            ) {
                hideProgressBar()
                if (response.isSuccessful()) {
                    Log.i("TAG", "onResponse: 1")
                    if (response.body() != null) {
                       displayVideos(response.body()!!)
                    }
                }else{    Log.i("TAG", "onResponse: 2")
                }
            }

            override fun onFailure(call: Call<YoutubeVideoModel?>, t: Throwable) {
                Log.i("TAG", "onResponse: 3")
                hideProgressBar()

            }

        })
    }

    private fun displayVideos(body: YoutubeVideoModel) {
        Log.i("TAG", "displayVideos: "+body.items)
        if(body!=null){
            for (i in body.items.indices) {
                val itemView: View =
                    LayoutInflater.from(this)
                        .inflate(
                            com.eightpeak.salakafarm.R.layout.youtube_video_items,
                            binding.videoList,
                            false
                        )

                val videoName =
                    itemView.findViewById<TextView>(com.eightpeak.salakafarm.R.id.video_title)
                val videoThumbnail =
                    itemView.findViewById<ImageView>(com.eightpeak.salakafarm.R.id.video_thumbnail)
                val videoUploadedDate =
                    itemView.findViewById<TextView>(com.eightpeak.salakafarm.R.id.video_date)
              val videoUploadedCard =
                    itemView.findViewById<CardView>(com.eightpeak.salakafarm.R.id.video_cart)
           if(body.items[i].snippet.publishTime != "2020-06-28T11:21:30Z"){
               var date=body.items[i].snippet.publishTime
               videoName.text=body.items[i].snippet.title
               videoUploadedDate.text=date.substring(0,  10)
               videoThumbnail.load(body.items[i].snippet.thumbnails.default.url)
               binding.videoList.addView(itemView)
               videoUploadedCard.setOnClickListener {
                   startActivity(
                       Intent(
                           Intent.ACTION_VIEW,
                           Uri.parse("http://www.youtube.com/watch?v=${body.items[i].id.videoId}")
                       )
                   )
                   Log.i("Video", "Video Playing....")

               }

                }
            }
            }
    }

    private fun hideProgressBar() {
        progress.visibility = View.GONE
    }

    private fun showProgressBar() {
        progress.visibility = View.VISIBLE
    }
}