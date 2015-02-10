package edu.virginia.dtc.BTLE_G4;

import java.util.Timer;
import java.util.TimerTask;

import edu.virginia.dtc.BTLE_G4.BTLE_G4_Driver.listDevice;
import edu.virginia.dtc.G4DevKit.ReceiverUpdateService;
import edu.virginia.dtc.SysMan.Debug;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class BTLE_G4_UI extends Activity {

	private static final String TAG = "BTLE_G4_UI";
	
	private TextView serial, mac, egv, meter, time;
	
	private Context main;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		final String FUNC_TAG = "onCreate";
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		main = this;
		
		serial = (TextView)this.findViewById(R.id.textSerial);
		mac = (TextView)this.findViewById(R.id.textMac);
		
		egv = (TextView)this.findViewById(R.id.textEgv);
		meter = (TextView)this.findViewById(R.id.textMeter);
		
		time = (TextView)this.findViewById(R.id.TextTime);
		
		final Handler ui = new Handler()
		{
			public void handleMessage(Message msg)
			{
				updateUI();
			}
		};
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask(){
			public void run()
			{
				ui.sendEmptyMessage(0);
			}
		}, 0, 5000);
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
	
	private void updateUI()
	{
		if(BTLE_G4_Driver.mac != null)
		{
			mac.setText("MAC: "+BTLE_G4_Driver.mac.toUpperCase());
			serial.setText("Serial: "+BTLE_G4_Driver.code.toUpperCase());
			
			egv.setText("Last EGV: \n"+BTLE_G4_Driver.egv);
			meter.setText("Last Meter: \n"+BTLE_G4_Driver.meter);
			
			time.setText("Phone Time: "+System.currentTimeMillis()/1000+"\n"+
						 "Recvr Time: "+BTLE_G4_Driver.sysTime+"\n"+
						 "Ready: "+BTLE_G4_Driver.sysTimeReady+"   Offset: "+BTLE_G4_Driver.sysTimeOffset);
		}
	}
}
