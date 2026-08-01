package com.virtual.cloner;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvApps = findViewById(R.id.rvApps);
        rvApps.setLayoutManager(new LinearLayoutManager(this));

        List<AppItem> installedApps = getInstalledUserApps();
        rvApps.setAdapter(new AppAdapter(installedApps));
    }

    private List<AppItem> getInstalledUserApps() {
        List<AppItem> list = new ArrayList<>();
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            // Filter out system apps, focus on installed user apps
            if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                String name = pm.getApplicationLabel(packageInfo).toString();
                String pkg = packageInfo.packageName;
                Drawable icon = pm.getApplicationIcon(packageInfo);
                list.add(new AppItem(name, pkg, icon));
            }
        }
        return list;
    }

    public static class AppItem {
        String name;
        String packageName;
        Drawable icon;

        public AppItem(String name, String packageName, Drawable icon) {
            this.name = name;
            this.packageName = packageName;
            this.icon = icon;
        }
    }

    private class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {
        private final List<AppItem> apps;

        public AppAdapter(List<AppItem> apps) {
            this.apps = apps;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            AppItem app = apps.get(position);
            holder.tvName.setText(app.name);
            holder.tvPkg.setText(app.packageName);
            holder.imgIcon.setImageDrawable(app.icon);

            holder.btnClone.setOnClickListener(v -> 
                Toast.makeText(MainActivity.this, "Cloning " + app.name + "...", Toast.LENGTH_SHORT).show()
            );
        }

        @Override
        public int getItemCount() {
            return apps.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvPkg;
            ImageView imgIcon;
            MaterialButton btnClone;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvAppName);
                tvPkg = itemView.findViewById(R.id.tvPackageName);
                imgIcon = itemView.findViewById(R.id.imgAppIcon);
                btnClone = itemView.findViewById(R.id.btnClone);
            }
        }
    }
}
