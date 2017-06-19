package org.wikipedia.activity.detailview;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;

import org.wikipedia.R;
import org.wikipedia.activity.search.MainActivity;
import org.wikipedia.model.WikiData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailImageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailImageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TITLE = "title";
    private static final String IMAGE = "image";
    private View fragmentView;
    public static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 123;
    private static final String TAG = "DetailImageFragment";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String title;
    String image;
    Bitmap bmp;
    ImageView detail_image;
    private OnFragmentInteractionListener mListener;

    public DetailImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailImageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailImageFragment newInstance(String param1, String param2) {
        DetailImageFragment fragment = new DetailImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_detail_image, container, false);
        fragmentView.setTag(TAG);


        title  = getArguments().getString(TITLE);
        image = getArguments().getString(IMAGE);

        TextView detail_tv_title = (TextView) fragmentView.findViewById(R.id.detail_tv_title);
        detail_image = (ImageView) fragmentView.findViewById(R.id.detail_image);

        detail_tv_title.setText(title);
        Glide.with(this).load(image).animate(R.anim.scale).into(detail_image);



        return fragmentView;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void  onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu,inflater);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                getActivity().finish();
                Intent i = new Intent(getActivity(),MainActivity.class);
                startActivity(i);
                return true;

            case R.id.share:

                detail_image.buildDrawingCache();
                bmp = detail_image.getDrawingCache();
                try {

                    if (bmp == null) {
                        Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
                    }
                    final File dir = new File(Environment.getExternalStorageDirectory(),"image");

                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    final File img = new File(dir, title + ".png");
                    if (img.exists()) {
                        img.delete();
                    }


                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);

                    shareIntent.putExtra(Intent.EXTRA_TEXT, title);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(img));
                    shareIntent.setType("image/*");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    getActivity().startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.share)));
                }
                catch(Exception e){

                }
                return true;
            case R.id.download:
                DownloadManager.Request request;

                try {
                    request = new DownloadManager.Request(Uri.parse(image));
                } catch (IllegalArgumentException e) {
                    return false;
                }

                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                request.setTitle(title);
                request.setDescription(getResources().getString(R.string.downloading_image));


                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

                String[] allowedTypes = {"png", "jpg", "jpeg", "gif", "webp"};
                String suffix = image.substring(image.lastIndexOf(".") + 1).toLowerCase();
                if (!Arrays.asList(allowedTypes).contains(suffix)) {

                    for (String s : allowedTypes) {
                    }

                    return false;
                }

                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS +
                        File.separator + "image", "name" + "." + suffix);
                request.allowScanningByMediaScanner();


                boolean result = checkPermission();
                if(result){

                    final DownloadManager dm = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                    Toast.makeText(getActivity(), getResources().getString(R.string.downloading_image), Toast.LENGTH_SHORT).show();
                }


                return true;

            case R.id.copyLink:
                Intent shareLinkIntent = new Intent(Intent.ACTION_SEND);
                shareLinkIntent.setType("text/plain");
                shareLinkIntent.putExtra(Intent.EXTRA_TEXT, image);
                startActivity(Intent.createChooser(shareLinkIntent, getResources().getString(R.string.copylink)));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        File file = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image" + System.currentTimeMillis() + ".png");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bmpUri = Uri.fromFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public boolean checkPermission()
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    new MaterialDialog.Builder(getActivity())
                            .title(getResources().getString(R.string.permission))
                            .content(getString(R.string.permission_message))
                            .positiveText(getResources().getString(R.string.ok))
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    ActivityCompat.requestPermissions((Activity)getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
                                }
                            })
                            .negativeText(getResources().getString(R.string.cancel))
                            .show();

                } else {
                    ActivityCompat.requestPermissions((Activity)getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
