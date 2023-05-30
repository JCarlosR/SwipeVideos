package com.programacionymas.swipevideos.data

import com.programacionymas.swipevideos.model.VideoItem

object VideoItemsList {

    private val videoItems = ArrayList<VideoItem>()

    init {
        add(
            url = "https://d2ufudlfb4rsg4.cloudfront.net/cbssacramento/1kKAnvpnc/adaptive/1kKAnvpnc_master.m3u8",
            title = "Tina Turner, Queen of Rock 'n' Roll, dies at 83",
            firstFrame = "https://img.haystack.tv/v1/dev/480x270/haystack-thumbnails/cbssacramento/1kKAnvpnc/1kKAnvpnc_720.jpg"
        )

        add(
            url ="https://d2ufudlfb4rsg4.cloudfront.net/bloomberg/GUFemrRFc/adaptive/GUFemrRFc_master.m3u8",
            title = "Metallurgical Coal Demand to Outstrip Supply: Coronado Global Resources",
            firstFrame = "https://img.haystack.tv/v1/dev/480x270/haystack-thumbnails/bloomberg/GUFemrRFc/GUFemrRFc_720.jpg"
        )

        add(
            url ="https://d2ufudlfb4rsg4.cloudfront.net/bloomberg/2JzobRIGc/adaptive/2JzobRIGc_master.m3u8",
            title = "Nvidia Market Value Tops $900 Billion",
            firstFrame = "https://img.haystack.tv/v1/dev/480x270/haystack-thumbnails/bloomberg/2JzobRIGc/2JzobRIGc_720.jpg"
        )

        add(
            url ="https://d2ufudlfb4rsg4.cloudfront.net/cnn/MA62KYG4c/adaptive/MA62KYG4c_master.m3u8",
            title = "Target facing backlash following removal of merchandise ahead of Pride Month",
            firstFrame = "https://img.haystack.tv/v1/dev/480x270/haystack-thumbnails/cnn/MA62KYG4c/MA62KYG4c_720.jpg"
        )

        add(
            url ="https://d2ufudlfb4rsg4.cloudfront.net/cnn/R1KrvPW4c/adaptive/R1KrvPW4c_master.m3u8",
            title = "Whistleblower alleges DOJ ‘slow-walked’ Hunter Biden probe",
            firstFrame = "https://img.haystack.tv/v1/dev/480x270/haystack-thumbnails/cnn/R1KrvPW4c/R1KrvPW4c_720.jpg"
        )

        add(
            url ="https://d2ufudlfb4rsg4.cloudfront.net/bloomberg/nDK0mGSGc/adaptive/nDK0mGSGc_master.m3u8",
            title = "Krugman: We're Disinvesting in the Country's Future",
            firstFrame = "https://img.haystack.tv/v1/dev/480x270/haystack-thumbnails/bloomberg/nDK0mGSGc/nDK0mGSGc_720.jpg"
        )

        add(
            url ="https://d2ufudlfb4rsg4.cloudfront.net/bloomberg/o3IgDEkBc/adaptive/o3IgDEkBc_master.m3u8",
            title = "DeSantis Campaign Debut Marred by Twitter Glitches",
            firstFrame = "https://img.haystack.tv/v1/dev/480x270/haystack-thumbnails/bloomberg/o3IgDEkBc/o3IgDEkBc_720.jpg"
        )

        /*
        add(
            url ="https://d2ufudlfb4rsg4.cloudfront.net/yahoofinance/SJVco2yCc/adaptive/SJVco2yCc_master.m3u8",
            title = "Dish Network stock up on plans to sell wireless on Amazon",
            firstFrame = "https://img.haystack.tv/v1/dev/480x270/haystack-thumbnails/yahoofinance/SJVco2yCc/SJVco2yCc_720.jpg"
        )

        add(
            url ="https://d2ufudlfb4rsg4.cloudfront.net/bloomberg/ONig94h5c/adaptive/ONig94h5c_master.m3u8",
            title = "In Germany, Scars of an Energy Crisis",
            firstFrame = "https://img.haystack.tv/v1/dev/480x270/haystack-thumbnails/bloomberg/ONig94h5c/ONig94h5c_720.jpg"
        )

        add(
            url ="https://d2ufudlfb4rsg4.cloudfront.net/reutersvideo/azq1ist2c/adaptive/azq1ist2c_master.m3u8",
            title = "Germany officially enters recession",
            firstFrame = "https://img.haystack.tv/v1/dev/480x270/haystack-thumbnails/reutersvideo/azq1ist2c/azq1ist2c_720.jpg"
        )

        add(
            url ="https://d2ufudlfb4rsg4.cloudfront.net/yahoofinance/HPIDLiB6c/adaptive/HPIDLiB6c_master.m3u8",
            title = "Taiwan Semiconductors, ASML, Intel: Top stocks",
            firstFrame = "https://img.haystack.tv/v1/dev/480x270/haystack-thumbnails/yahoofinance/HPIDLiB6c/HPIDLiB6c_720.jpg"
        )

        add(
            url ="https://d2ufudlfb4rsg4.cloudfront.net/cheddar/3oNf54KDc/adaptive/3oNf54KDc_master.m3u8",
            title = "MoviePass Returns With New Points-Based System",
            firstFrame = "https://img.haystack.tv/v1/dev/480x270/haystack-thumbnails/cheddar/3oNf54KDc/3oNf54KDc_720.jpg"
        )

        add(
            url ="https://d2ufudlfb4rsg4.cloudfront.net/bloomberg/X792SvAAc/adaptive/X792SvAAc_master.m3u8",
            title = "Malek: Oil Is Being Used as a Proxy, and It's Dangerous",
            firstFrame = "https://img.haystack.tv/v1/dev/480x270/haystack-thumbnails/bloomberg/X792SvAAc/X792SvAAc_720.jpg"
        )

        add(
            url ="https://d2ufudlfb4rsg4.cloudfront.net/bloomberg/L4JgXRI9c/adaptive/L4JgXRI9c_master.m3u8",
            title = "Educational Tech Company Byju Founder on Funding, AI",
            firstFrame = "https://img.haystack.tv/v1/dev/480x270/haystack-thumbnails/bloomberg/L4JgXRI9c/L4JgXRI9c_720.jpg"
        )

        add(
            url ="https://d2ufudlfb4rsg4.cloudfront.net/yahoofinance/aW6NTqQDc/adaptive/aW6NTqQDc_master.m3u8",
            title = "Hot real estate markets starting to cool: Redfin",
            firstFrame = "https://img.haystack.tv/v1/dev/480x270/haystack-thumbnails/yahoofinance/aW6NTqQDc/aW6NTqQDc_720.jpg"
        )

        add(
            url ="https://d2ufudlfb4rsg4.cloudfront.net/yahoofinance/X7lKrh5Dc/adaptive/X7lKrh5Dc_master.m3u8",
            title = "Gas prices: Memorial Day weekend 'a dress rehearsal' for summer travel",
            firstFrame = "https://img.haystack.tv/v1/dev/480x270/haystack-thumbnails/yahoofinance/X7lKrh5Dc/X7lKrh5Dc_720.jpg"
        )

        add(
            url ="https://d2ufudlfb4rsg4.cloudfront.net/yahoofinance/SnpKTROCc/adaptive/SnpKTROCc_master.m3u8",
            title = "Tesla overvalued, Ford well positioned in EV market: Strategist",
            firstFrame = "https://img.haystack.tv/v1/dev/480x270/haystack-thumbnails/yahoofinance/SnpKTROCc/SnpKTROCc_720.jpg"
        )

        add(
            url ="https://d2ufudlfb4rsg4.cloudfront.net/bloomberg/04TFvc67c/adaptive/04TFvc67c_master.m3u8",
            title = "Highlights from the 2023 Qatar Economic Forum",
            firstFrame = "https://img.haystack.tv/v1/dev/480x270/haystack-thumbnails/bloomberg/04TFvc67c/04TFvc67c_720.jpg"
        )

        add(
            url ="https://d2ufudlfb4rsg4.cloudfront.net/bloomberg/laGNPSjIc/adaptive/laGNPSjIc_master.m3u8",
            title = "IGamiX's Lee on Macau's Gaming Outlook",
            firstFrame = "https://img.haystack.tv/v1/dev/480x270/haystack-thumbnails/bloomberg/laGNPSjIc/laGNPSjIc_720.jpg"
        )

        add(
            url ="https://d2ufudlfb4rsg4.cloudfront.net/yahoofinance/kPaR3fkjc/adaptive/kPaR3fkjc_master.m3u8",
            title = "Debt ceiling: Recession fears ease as companies shift focus",
            firstFrame = "https://img.haystack.tv/v1/dev/480x270/haystack-thumbnails/yahoofinance/kPaR3fkjc/kPaR3fkjc_720.jpg"
        )

        add(
            url ="https://d2ufudlfb4rsg4.cloudfront.net/bloomberg/hYde3IBBc/adaptive/hYde3IBBc_master.m3u8",
            title = "JPMorgan Cuts 1,000 First Republic Staffers",
            firstFrame = "https://img.haystack.tv/v1/dev/480x270/haystack-thumbnails/bloomberg/hYde3IBBc/hYde3IBBc_720.jpg"
        )*/

        add(
            url = "https://assets.mixkit.co/videos/preview/mixkit-the-spheres-of-a-christmas-tree-2720-large.mp4",
            title = "Christmas",
            firstFrame = "https://i.ibb.co/Yy4ynHT/christmas.jpg"
        )

        add(
            url = "https://assets.mixkit.co/videos/preview/mixkit-tree-with-yellow-flowers-1173-large.mp4",
            title = "Flowers",
            firstFrame = "https://i.ibb.co/Wz88cgs/flowers.jpg"
        )

        add(
            url = "https://assets.mixkit.co/videos/preview/mixkit-waves-in-the-water-1164-large.mp4",
            title = "Waves",
            firstFrame = "https://i.ibb.co/QrhBs1b/waves.jpg"
        )

        add(
            url = "https://v.redd.it/8gl6r3cj1n0b1/DASH_720.mp4",
            title = "TikTok",
            firstFrame = "https://external-preview.redd.it/OidsuuDcHMOxYQVBM5SkrGWifoO47sg8_ROuPuFK7g8.png?format=pjpg&auto=webp&v=enabled&s=ae75797c535dfe21045d85b71ca41c2b10b7dbfe"
        )
    }

    private fun add(url: String, title: String, firstFrame: String) {
        videoItems.add(
            VideoItem(url, title, firstFrame)
        )
    }

    fun get(): List<VideoItem> = videoItems
}