package alexa.projectcharizard.ViewModel;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

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

        Image spotImage = spot.getImage();
        ImageView image = (ImageView) view.findViewById(R.id.image);
        if (!spotImage.equals(null))
            image.setImageResource(R.drawable.default_smultron);
        



    }

    /**
     * Opens the detailed activity
     * @param view
     */
    public void openDetailActivity(View view) {

    }

    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
