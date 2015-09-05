package murad.affandy.simpletwitter;

import android.app.ListActivity;
import android.app.ListFragment;
import android.widget.ArrayAdapter;
import android.os.AsyncTask;
import android.widget.ListView;



import java.util.ArrayList;

public class TwitterAsyncTask extends AsyncTask<Object, Void, ArrayList<TwitterTweet>> {
    ListActivity callerActivity;

    final static String TWITTER_API_KEY = "NCCVETxTTdUXywwamRsoIh3Ag";
    final static String TWITTER_API_SECRET = "rnPfKTYct9vEVoz7yhwf3iHwirtweDcMIhnGsJ1daGk9vktStW";

    @Override
    protected ArrayList<TwitterTweet> doInBackground(Object... params) {
        ArrayList<TwitterTweet> twitterTweets = null;
        callerActivity = (ListActivity) params[1];
        if (params.length > 0) {

            TwitterAPI twitterAPI = new TwitterAPI(TWITTER_API_KEY,TWITTER_API_SECRET);
            twitterTweets = twitterAPI.getTwitterTweets(params[0].toString());

        }


        return twitterTweets;
    }

    @Override
    protected void onPostExecute(ArrayList<TwitterTweet> twitterTweets) {
        ArrayAdapter<TwitterTweet> adapter =
                new ArrayAdapter<TwitterTweet>(callerActivity, R.layout.twitter_tweets_list,
                        R.id.listTextView, twitterTweets);
        callerActivity.setListAdapter(adapter);

    }
}
