package alexa.projectcharizard.ViewModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

import alexa.projectcharizard.Model.Category;
import alexa.projectcharizard.Model.Spot;
import alexa.projectcharizard.R;

/**
 * InfoWindowAdapter for spot_info_windows.xml
 * Handles the rendering of the small detailed view window that opens
 * when pressing any spot on the map
 * @author Filip Andréasson
 */
public class SpotDetailViewAdapter implements GoogleMap.InfoWindowAdapter {

    private final View detailView;
    private List<Spot> spots;
    private Spot spot;
    private Context context;

    public SpotDetailViewAdapter(Context context, List<Spot> spots) {
        this.context = context;
        detailView = LayoutInflater.from(context).inflate(R.layout.spot_info_window,null);
        this.spots = spots;
    }

    /**
     * Renders the views in spot_info_window.xml
     * @param marker is the marker clicked
     * @param view is the spot_info_window.xml root view
     */
    private void renderWindowText(Marker marker, View view) {

        for (Spot lstSpot : spots) {
            if (lstSpot.getId().equals(marker.getSnippet()))
                spot = lstSpot;

        }

        String spotTitle = spot.getName();
        TextView title = (TextView) view.findViewById(R.id.title);
        if (!spotTitle.equals(""))
            title.setText(marker.getTitle());

        Bitmap spotImage = spot.getImage();
        ImageView image = (ImageView) view.findViewById(R.id.image);
        if (spotImage == null) {
            image.setImageResource(R.drawable.default_smultron);
        } else {
            image.setImageBitmap(spotImage);
        }

        Category spotCategory = spot.getCategory();
        TextView category = (TextView) view.findViewById(R.id.category);
        if (spotCategory != null)
            switch (spotCategory) {
                case OTHER:
                    category.setText("Other");
                    break;

                case APPLE_TREE:
                    category.setText("Apple tree");
            }

    }

    /**
     * Renders the window and returns the view
     * @param marker the marker from which we create the info window
     * @return the info window view
     */
    public View getInfoWindow(Marker marker) {
         renderWindowText(marker, detailView);
         return detailView;
    }


    /**
     * Should return custom content, but without any custom view elements.
     *
     *                   *****Currently not usable*****
     *
     * @param marker
     * @return
     */
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
