package jcollado.pw.pimpam.utils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.controller.AddComicFragment;
import jcollado.pw.pimpam.controller.AuthActivity;
import jcollado.pw.pimpam.controller.CollectionFragment;
import jcollado.pw.pimpam.controller.MainActivity;
import jcollado.pw.pimpam.controller.SettingsFragment;

/**
 * Created by colla on 31/05/2017.
 */

public class NavigationDrawer {

    private static NavigationDrawer drawerBuilerClass;
    private static Drawer drawerBuilder;
    private static CollectionFragment collection;


    private NavigationDrawer(final MainActivity activity, final Toolbar toolbar) {


        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
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
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(item1,item2, new DividerDrawerItem(),ajustes,signout)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        BaseFragment fragment;

                        switch (position) {
                            case 1:
                                fragment = CollectionFragment.newInstance();
                                toolbar.setTitle(activity.getString(R.string.seeCollection));

                                activity.getSupportActionBar().hide();
                                activity.openFragment(fragment,"");
                                break;

                            case 2:
                                fragment = AddComicFragment.newInstance();
                                activity.getSupportActionBar().hide();
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
                                        Intent i = new Intent(activity.getApplicationContext(), AuthActivity.class);
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

    public static NavigationDrawer getInstance(final MainActivity activity, final Toolbar toolbar) {

            drawerBuilerClass = new NavigationDrawer(activity,toolbar);

        return drawerBuilerClass;
    }

    public Drawer getDrawerBuilder() {
        return drawerBuilder;
    }
}
