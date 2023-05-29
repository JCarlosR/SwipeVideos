# Swipe videos

Exploring `ViewPager2` and `Compose Pager` as libraries to allow swiping videos.

As well as the cache mechanisms offered by `ExoPlayer`, in order to precache next videos.

## Folder structure

- `data`: a hardcoded list of videos to play (first 21 videos are `m3u8` and the next 4 ones are `mp4`).
- `model`: `VideoItem` representing a video.
- `player`: contains a wrapper class around `ExoPlayer` and cache-related classes.
- `ui`: there are 2 variations for this project, one using `ViewPager2` and other with `HorizontalPager`, 
please review the latter.


## Findings

### Preparing vs precaching

- **Preparing** refers to the `prepare` method offered by ExoPlayer to get a video ready to play.
- If a video is prepared, it will start playing almost immediately.
- **Precaching** refers to downloading ahead the first seconds of a video.
  - To cache an mp4 video we use the `CacheWriter` class.
  - To cache an m3u8 we use the `HlsDownloader` class.
  - Both are provided by the ExoPlayer library.
- ExoPlayer can prepare quicker a video if it is precached (will access from disk instead of from network).

### Approaches for Smooth Swiping

**3 players + preparing**
- We can prepare the adjacent videos in their own players, and they will immediately play when swiping. 
- This approach does not require caching at all, as the players take care of the initial buffering.  
- However, if a user goes 2 videos back (to visit a page already disposed) it will need to download the video again.

**1 player + precaching**
- After swiping, there is a small delay (30~200 ms) on preparing the corresponding video, but it will be smooth enough if already precached.
- In order to not show a black screen while preparing, we alleviate showing an image of the 1st frame on top of the video player.
 
### Notes
- The `CacheDataSourceFactory` class tells the player to first read from cache, 
and then continue playback downloading from upstream (network).

## How to run

On Android Studio > app > `Edit configurations` you can define the `Launch Options`.

There, select `Launch: Specified Activity` and then choose whether `ComposePagerActivity` or `ViewPager2Activity`.

## TODO: What's next

Still pending to implement / test:

- Stop precaching as soon as user starts preparing the same video in the player.
- Going background / minimizing the app: do we want to continue precaching on a background job?
