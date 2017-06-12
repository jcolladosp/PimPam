package jcollado.pw.pimpam.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;

import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.controller.AddComicFragment;
import jcollado.pw.pimpam.login.LoginActivity;
import jcollado.pw.pimpam.controller.CollectionFragment;
import jcollado.pw.pimpam.controller.MainActivity;
import jcollado.pw.pimpam.settings.SettingsFragment;

/**
 * Created by colla on 31/05/2017.
 */

public class NavigationDrawer {

    private static NavigationDrawer drawerBuilerClass;
    private static Drawer drawerBuilder;


    private NavigationDrawer(final MainActivity activity) {


//        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.header)
                .withSelectionListEnabledForSingleProfile(false)
                .withOnlyMainProfileImageVisible(true)
                .addProfiles(
                        new ProfileDrawerItem().withName(UserInfo.getDisplayName()).withEmail(UserInfo.getUserEmail()).withIcon(UserInfo.getProfilePictureURL())
                )

                .build();
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(activity.getString(R.string.seeCollection)).withIcon(FontAwesome.Icon.faw_book);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(activity.getString(R.string.addComic)).withIcon(FontAwesome.Icon.faw_plus_circle);
        SecondaryDrawerItem ajustes = new SecondaryDrawerItem().withIdentifier(3).withName(activity.getString(R.string.settings)).withIcon(FontAwesome.Icon.faw_cog);
        SecondaryDrawerItem signout = new SecondaryDrawerItem().withIdentifier(4).withName(activity.getString(R.string.logout_button)).withIcon(FontAwesome.Icon.faw_sign_out);



        //create the drawer and remember the `Drawer` result object
        drawerBuilder = new DrawerBuilder()
                .withActivity(activity)

                .withAccountHeader(headerResult)
                .addDrawerItems(item1,item2, new DividerDrawerItem(),ajustes,signout)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        BaseFragment fragment;

                        switch (position) {
                            case 1:
                                fragment = CollectionFragment.newInstance();

                                activity.openFragment(fragment,"");
                                break;

                            case 2:
                                fragment = AddComicFragment.newInstance();
                                activity.openFragment(fragment,"add");
                                break;

                            case 4:
                                fragment = SettingsFragment.newInstance();
                                activity.openFragment(fragment,"settings");
                                break;


                            case 5:
                                //logout code
                                AlertDialog.Builder logOutBuilder = Functions.getModalLogOut(activity);
                                logOutBuilder.setPositiveButton((activity.getString(R.string.ok)), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        FirebaseModule.getInstance().getmAuth().signOut();
                                        Intent i = new Intent(activity.getApplicationContext(), LoginActivity.class);
                                        activity.startActivity(i);
                                    }
                                });
                                logOutBuilder.show();
                                break;

                        }
                        return true;
                    }
                }).build();

    }

    public static NavigationDrawer getInstance(final MainActivity activity) {

            drawerBuilerClass = new NavigationDrawer(activity);

        return drawerBuilerClass;
    }

    public Drawer getDrawerBuilder() {
        return drawerBuilder;
    }

    public static void loadProfileImage(){
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder, String tag) {
                Glide.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Glide.clear(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx, String tag) {
                //define different placeholders for different imageView targets
                //default tags are accessible via the DrawerImageLoader.Tags
                //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
                if (DrawerImageLoader.Tags.PROFILE.name().equals(tag)) {
                    return DrawerUIUtils.getPlaceHolder(ctx);
                } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name().equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(com.mikepenz.materialdrawer.R.color.primary).sizeDp(56);
                } else if ("customUrlItem".equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(R.color.md_red_500).sizeDp(56);
                }

                //we use the default one for
                //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()

                return super.placeholder(ctx, tag);
            }
        });
    }
}
