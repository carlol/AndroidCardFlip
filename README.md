AndroidCardFlip
===============

Simple Compound View to perform flip effect for ALL android versions

CardFlipView is a simple Compound View to perform flip animation for ALL android versions.

Flip animation is performed by single tap on the compound view, but could be triggered using a specific method programmatically:

    public void flipCard();

CardFlipView supplies two xml attributes to refer both side views (front/back).

Event on flip event could be managed by an useful listener:

    public interface OnCardFlipListener {

		public void onFrontFlip(View front);

		public void onBackFlip(View back, boolean isFirstTime);

	}

A simple xml sample:

    <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        xmlns:cardflip="http://schemas.android.com/apk/res/io.carlol.test.cardflip"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity" >
    
        <io.carlol.test.cardflip.ui.CardFlip
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerInParent="true"
            cardflip:cf_animationDuration="500"
            cardflip:cf_back="@+id/main_activity_card_back"
            cardflip:cf_front="@+id/main_activity_card_face" >
    
            <RelativeLayout
                android:id="@id/main_activity_card_face"
                android:layout_width="300dp"
                android:layout_height="407dp"
                android:layout_centerHorizontal="true"
                android:layout_centerVertical="true"
                android:background="@android:color/holo_green_light"
                android:clickable="true"
                android:onClick="onCardClick"
                android:padding="5dp" >
            </RelativeLayout>
    
            <RelativeLayout
                android:id="@id/main_activity_card_back"
                android:layout_width="300dp"
                android:layout_height="407dp"
                android:layout_centerHorizontal="true"
                android:layout_centerVertical="true"
                android:background="@android:color/holo_orange_light"
                android:clickable="true"
                android:onClick="onCardClick"
                android:visibility="gone" >
            </RelativeLayout>
        </io.carlol.test.cardflip.ui.CardFlip>
    
    </RelativeLayout>
