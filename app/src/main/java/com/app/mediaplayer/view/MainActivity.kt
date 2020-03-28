package com.app.mediaplayer.view

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.mediaplayer.R
import com.app.mediaplayer.model.TeamsAdapter
import com.app.mediaplayer.viewmodel.TeamsViewModel
import com.google.android.exoplayer2.PlaybackPreparer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_content.*


class MainActivity : AppCompatActivity(), Player.EventListener {
    private val POSITION: String = "POSITION"
    private var show: Boolean = true
    private lateinit var mHomeTeamAdapter: TeamsAdapter
    private lateinit var mAwayTeamAdapter: TeamsAdapter
    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var playbackPosition: Long = 0

    private val teamsViewModel by lazy {
        ViewModelProviders.of(this).get(TeamsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mHomeTeamAdapter = TeamsAdapter()
        mAwayTeamAdapter = TeamsAdapter()

        val homeLayoutManager = LinearLayoutManager(this)
        rc_home_team.layoutManager = homeLayoutManager
        rc_home_team.itemAnimator = DefaultItemAnimator()
        rc_home_team.adapter = mHomeTeamAdapter

        val awayLayoutManager = LinearLayoutManager(this)
        rc_away_team.layoutManager = awayLayoutManager
        rc_away_team.itemAnimator = DefaultItemAnimator()
        rc_away_team.adapter = mAwayTeamAdapter

        img_close.setOnClickListener(View.OnClickListener {
            finish()
        })
        img_show_hide.setOnClickListener(View.OnClickListener {
            if (show) {
                show = false
                rc_away_team.visibility = View.VISIBLE
                rc_home_team.visibility = View.VISIBLE
                img_show_hide.setBackgroundResource(R.drawable.show)
            } else {
                show = true
                rc_away_team.visibility = View.GONE
                rc_home_team.visibility = View.GONE
                img_show_hide.setBackgroundResource(R.drawable.hide)

            }
        })

        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong(POSITION);
        }

        initObserver()
    }

    private fun initObserver() {

        teamsViewModel.teamsLiveData.observe(this, Observer {
            mHomeTeamAdapter.updateAdapter(it!!.lineups.data!!.homeTeam!!.players)
            mAwayTeamAdapter.updateAdapter(it!!.lineups.data!!.awayTeam!!.players)

        })

        if (networkConnection()) {
            teamsViewModel.fetchAllTeamsData()
        }
    }

    private fun initializePlayer() {

        player = SimpleExoPlayer.Builder(this)
            .build()

        player_view.player = player
        player_view.setOnTouchListener(OnTouchListener { _, _ ->
            if (!show) {
                show = true
                rc_away_team.visibility = View.GONE
                rc_home_team.visibility = View.GONE
                img_show_hide.setBackgroundResource(R.drawable.hide)

            }

            true

        })

        val uri = Uri.parse(getString(R.string.media_url))
        val mediaSource = buildMediaSource(uri)

        player!!.playWhenReady = playWhenReady
        player!!.seekTo(0, playbackPosition)
        mediaSource?.let { player!!.prepare(it, false, false) }

        player_view.setShutterBackgroundColor(Color.TRANSPARENT)
        player!!.playWhenReady = true
    }


    // Build the video MediaSource.
    private fun buildMediaSource(uri: Uri): MediaSource? {

        val dataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(this, "user-agent")

        return HlsMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)

    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putLong(POSITION, playbackPosition);
    }

    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player!!.playWhenReady
            playbackPosition = player!!.currentPosition
            player!!.release()
            player = null
        }
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
            if (player_view != null) {
                player_view.onResume()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer()
            if (player_view != null) {
                player_view.onResume()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            if (player_view != null) {
                player_view.onPause()
            }
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            if (player_view != null) {
                player_view.onPause()
            }
            releasePlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        teamsViewModel.cancelAllRequests()
    }

    private fun networkConnection(): Boolean { // get Connectivity Manager object to check connection
        val cm =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting
        // Check for network connections
        return if (isConnected) {
            true
        } else {
            Toast.makeText(this, " No Internet Connection available ", Toast.LENGTH_LONG).show()
            false
        }
    }
}
