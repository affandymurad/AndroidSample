package murad.affandy.simpletwitter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity  implements SwipeRefreshLayout.OnRefreshListener {

    final static String twitterScreenName = "CNN";
    final static String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        MainActivity.this.setTitle("@CNN Tweets");
        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(MainActivity.this);
        swipeLayout.setColorScheme(android.R.color.holo_purple,
                android.R.color.holo_blue_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.black);

        AndroidNetworkUtility androidNetworkUtility = new AndroidNetworkUtility();
        if (androidNetworkUtility.isConnected(this)) {
            Toast.makeText(getApplicationContext(), "Congrats, Success to open CNN Twitter Timeline", Toast.LENGTH_LONG).show();
            new TwitterAsyncTasksi().execute(twitterScreenName,this);
        } else {
            Toast.makeText(getApplicationContext(), "Unfortunately, can't be connected to Internet. Please check your connection", Toast.LENGTH_LONG).show();
            Log.v(TAG, "Network not Available!");
        }
    }


    public void onBackPressed()
    {
        doExit();
    }

    @Override
    public void onRefresh(){
    AndroidNetworkUtility androidNetworkUtility = new AndroidNetworkUtility();
    if (androidNetworkUtility.isConnected(this)) {
        Toast.makeText(getApplicationContext(), "Congrats, Success to open CNN Twitter Timeline", Toast.LENGTH_LONG).show();
        new TwitterAsyncTasksi().execute(twitterScreenName,this);
    } else {
        Toast.makeText(getApplicationContext(), "Unfortunately, can't be connected to Internet. Please check your connection", Toast.LENGTH_LONG).show();
        Log.v(TAG, "Network not Available!");
        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(false);
            }
        });
    }
    }

    private void doExit() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        alertDialog.setNegativeButton("No", null);
        alertDialog.setMessage("Do you want to quit?");
        alertDialog.show();
    }

    public class TwitterAsyncTasksi extends AsyncTask<Object, Void, ArrayList<TwitterTweet>> {
        ListView callerActivity = (ListView) findViewById(R.id.listTweet);

        final static String TWITTER_API_KEY = "NCCVETxTTdUXywwamRsoIh3Ag";
        final static String TWITTER_API_SECRET = "rnPfKTYct9vEVoz7yhwf3iHwirtweDcMIhnGsJ1daGk9vktStW";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
            swipeLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeLayout.setRefreshing(true);
                }
            });
        }

        @Override
        protected ArrayList<TwitterTweet> doInBackground(Object... params) {
            ArrayList<TwitterTweet> twitterTweets = null;
            if (params.length > 0) {

                TwitterAPI twitterAPI = new TwitterAPI(TWITTER_API_KEY,TWITTER_API_SECRET);
                twitterTweets = twitterAPI.getTwitterTweets(params[0].toString());

            }


            return twitterTweets;
        }

        @Override
        protected void onPostExecute(ArrayList<TwitterTweet> twitterTweets) {
            ArrayAdapter<TwitterTweet> adapter =
                    new ArrayAdapter<TwitterTweet>(getApplicationContext(), R.layout.twitter_tweets_list,
                            R.id.listTextView, twitterTweets);
            callerActivity.setAdapter(adapter);
            final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
            swipeLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeLayout.setRefreshing(false);
                }
            });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        if (id == R.id.action_settings)
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.about, null);
            alertDialog.setView(dialogView);
            ImageView img = (ImageView) dialogView.findViewById(R.id.imageView2);
            img.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("http://affandymurad.blogspot.com/"));
                    startActivity(intent);
                }
            });
            AlertDialog dialogue = alertDialog.create();
            dialogue.show();
        }
        // Activate the navigation drawer toggle
        return super.onOptionsItemSelected(item);

    }

}