# Swipe videos

Exploring `ViewPager2` and `Compose Pager` as libraries to allow swiping videos.

As well as the cache mechanism offered by `ExoPlayer`, in order to precache next videos.

The original project was using `VideoView`, but it has been replaced with `ExoPlayer`.

## Folder structure

- `data`: a hardcoded list of videos to play.
- `model`: `VideoItem` representing a video.
- `player`: contains a wrapper class around `ExoPlayer` and cache-related classes.
- `ui`: there are 2 variations for this project, one using `ViewPager2` and other for `Pager`, 
please review one at a time.


## Findings

### Caching

- Without the `PreCacher` we can still prepare the next video ahead and it will immediately play
when swiping. However, if a user goes 2 videos back (to visit a page already disposed) it will need to
download the video again from the beginning.
- Adding a `CacheDataSourceFactory` to the player doesn't magically cache the videos. It will just
read from cache before downloading from upstream (online).