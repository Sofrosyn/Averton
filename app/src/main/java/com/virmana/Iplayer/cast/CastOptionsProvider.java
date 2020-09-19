package com.virmana.Iplayer.cast;

import android.content.Context;

import com.virmana.Iplayer.ui.activities.HomeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tamim on 28/09/2019.

 */

public class CastOptionsProvider {
/*
    @Override
    public CastOptions getCastOptions(Context context) {
        // Cast Coach staging : CC1AD845
        CastMediaOptions mediaOptions = new CastMediaOptions.Builder()
                .setImagePicker(new ImagePickerImpl())
                .setNotificationOptions(getNotificationOptions())
                .setExpandedControllerActivityClassName(ExpandedControlsActivity.class.getName())
                .build();

        CastOptions castOptions = new CastOptions.Builder()
                .setReceiverApplicationId(CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID)
                .setCastMediaOptions(mediaOptions)
                .build();
        return castOptions;
    }

    @Override
    public List<SessionProvider> getAdditionalSessionProviders(Context context) {
        return null;
    }

    private List<String> getButtonActions() {
        List<String> buttonActions = new ArrayList<>();
        buttonActions.add(MediaIntentReceiver.ACTION_REWIND);
        buttonActions.add(MediaIntentReceiver.ACTION_TOGGLE_PLAYBACK);
        buttonActions.add(MediaIntentReceiver.ACTION_STOP_CASTING);
        return buttonActions;
    }

    private NotificationOptions getNotificationOptions() {
        int[] compatButtonActionsIndicies = new int[]{ 1, 2 };

        NotificationOptions notificationOptions = new NotificationOptions.Builder()
                .setActions(getButtonActions(), compatButtonActionsIndicies)
                .setTargetActivityClassName(HomeActivity.class.getName())
                .build();

        return notificationOptions;
    }
    private static class ImagePickerImpl extends ImagePicker {

        @Override
        public WebImage onPickImage(MediaMetadata mediaMetadata, int type) {
            if ((mediaMetadata == null) || !mediaMetadata.hasImages()) {
                return null;
            }
            List<WebImage> images = mediaMetadata.getImages();
            if (images.size() == 1) {
                return images.get(0);
            } else {
                if (type == ImagePicker.IMAGE_TYPE_MEDIA_ROUTE_CONTROLLER_DIALOG_BACKGROUND) {
                    return images.get(0);
                } else {
                    return images.get(1);
                }
            }
        }
    }*/
}
