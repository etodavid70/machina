package com.example.machina.data.model.onboarding_models
import androidx.annotation.DrawableRes
import com.example.machina.R




sealed class PagerModel(
    @DrawableRes val backgroundImage: Int,
    val mainHeader: String,
    val subHeader: String
)

{
    data object PagerScreen1: PagerModel(R.drawable.presplash1,
        "The Future is Here",
        "Turn Android into Desktop Power . Get Desktop Experience and Mobile Convenience all in one." )

    data object PagerScreen2: PagerModel(R.drawable.presplash2,
        "One Device. Endless Possibilities",
        "Customize your Android to run the operating system you prefer — anytime")

    data object PagerScreen3: PagerModel(R.drawable.presplash3,
        "One Phone. Many Systems",
        "Switch between multiple operating systems — right from your Android device" )
}




