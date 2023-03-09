package com.lentimosystems.swipevideos.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.lentimosystems.swipevideos.R;
import com.lentimosystems.swipevideos.model.VideoItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewPager2 videosViewPager = findViewById(R.id.viewPagerVideos);

        // By default the ViewPager binds only position 0, then position 1 after swiping
        // With page limit 1, it will bind position 0 and 1 at the beginning
        // Do we need to bind in advance? Or just precache the next video?
        // videosViewPager.setOffscreenPageLimit(1);

        VideosAdapter adapter = new VideosAdapter();
        adapter.setVideoItems(getVideoItems());

        videosViewPager.setAdapter(adapter);
    }

    private List<VideoItem> getVideoItems() {
        List<VideoItem> videoItems = new ArrayList<>();

        VideoItem item = new VideoItem();
        item.videoURL = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
        item.videoTitle = "Big Buck Bunny";
        item.videoDesc = "Big Buck Bunny tells the story of a giant rabbit with a heart bigger than himself.";
        videoItems.add(item);

        VideoItem item2 = new VideoItem();
        item2.videoURL = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4";
        item2.videoTitle = "We Are Going On Bullrun";
        item2.videoDesc = "The Smoking Tire is going on the 2010 Bullrun Live Rally in a 2011 Shelby GT500, and posting a video from the road every single day!";
        videoItems.add(item2);

        VideoItem item3 = new VideoItem();
        item3.videoURL = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4";
        item3.videoTitle = "Tears of Steel";
        item3.videoDesc = "Tears of Steel was realized with crowd-funding by users of the open source 3D creation tool Blender.";
        videoItems.add(item3);

        return videoItems;
    }
}