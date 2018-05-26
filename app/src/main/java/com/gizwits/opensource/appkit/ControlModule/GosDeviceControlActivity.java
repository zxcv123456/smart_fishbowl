package com.gizwits.opensource.appkit.ControlModule;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;
import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;
import com.gizwits.opensource.appkit.R;

import java.util.concurrent.ConcurrentHashMap;

public class GosDeviceControlActivity extends GosBaseActivity {

	private Button heat_on;
	private Button light_On;
	private Button supplyoxygen_on;
	private Button water_on;
	private Button food_on;
	private TextView temperate_values;
	private TextView ligh_values;
	private Double status1;
	private Integer status2;

	public static final String heater="heater";
	public static final String Supplyoxygen="Supplyoxygen";
	public static final String lighting="lighting";
	public static final String food="food";
	public static final String water="water";

	public static final String temperate="temperate";
	public static final String ligh="ligh";




	/** The GizWifiDevice device */
	private GizWifiDevice device;

	/** The ActionBar actionBar */
	ActionBar actionBar;

	private GizWifiDeviceListener deviceListener = new GizWifiDeviceListener() {
		@Override
		public void didReceiveData(GizWifiErrorCode result, GizWifiDevice device, ConcurrentHashMap<String, Object> dataMap, int sn) {
			if (dataMap.get("data") != null) {
				ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String,Object>) dataMap.get("data");
				if (map.get(lighting) != null) {
					Boolean status = (Boolean) map.get(lighting);
					light_On.setSelected(status);
				}
				if (map.get(heater) != null) {
					Boolean status = (Boolean) map.get(heater);
					heat_on.setSelected(status);
				}
				if (map.get(Supplyoxygen) != null) {
					Boolean status = (Boolean) map.get(Supplyoxygen);
					supplyoxygen_on.setSelected(status);
				}
				if (map.get(food) != null) {
					Boolean status = (Boolean) map.get(food);
					food_on.setSelected(status);
				}
				if (map.get(water) != null) {
					Boolean status = (Boolean) map.get(water);
					water_on.setSelected(status);
				}
				if (map.get(temperate)!=null){
					status1= (Double) map.get(temperate);
					temperate_values.setText(status1.toString()+" ℃");
				}
				if (map.get(ligh)!=null){
					status2= (Integer) map.get(ligh);
					ligh_values.setText(status2.toString()+" LUX");
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gos_device_control);
		initDevice();
		setActionBar(true, true, device.getProductName());
		initView();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		device.setSubscribe(false);
	}

	private void initView() {
		light_On= (Button) findViewById(R.id.light_on);
		light_On.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                contorl(light_On,lighting);
			}
		});

		heat_on= (Button) findViewById(R.id.heat_on);
		heat_on.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				contorl(heat_on,heater);
			}
		});

		supplyoxygen_on= (Button) findViewById(R.id.supplyoxygen_on);
		supplyoxygen_on.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				contorl(supplyoxygen_on,Supplyoxygen);
			}
		});

		water_on= (Button) findViewById(R.id.water_on);
		water_on.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				contorl(water_on,water);
			}
		});

		food_on= (Button) findViewById(R.id.food_on);
		food_on.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				contorl(food_on,food);
			}
		});

		temperate_values= (TextView) findViewById(R.id.temperate_values);
		ligh_values= (TextView) findViewById(R.id.ligh_values);

	}

	private void initDevice() {
		Intent intent = getIntent();
		device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
		device.setListener(deviceListener);//这里初始化才能监控设备，记住，这里
		Log.i("Apptest", device.getDid());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void contorl(Button a,String dt){
		if (a.isSelected()){
			sendCommand(dt,false);
			a.setSelected(false);
		}else {
			sendCommand(dt,true);
			a.setSelected(true);
		}

	}

	private void sendCommand(String dt, boolean onOff){
		int sn=5;
		ConcurrentHashMap<String,Object> command=new ConcurrentHashMap<String, Object>();
		command.put(dt,onOff);
		device.write(command,sn);
	}


}
