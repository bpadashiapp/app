package ir.tahasystem.music.app.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ir.tahasystem.music.app.Values;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		context.startService(new Intent(context,
				Services.class));

		context.startService(new Intent(context,
				ServicesSocket.class));

		context.startService(new Intent(context,
				ServicesSocketManager.class));

		if(Values.appId==3 && Values.isService)  {

			context.startService(new Intent(context,
					ServiceGPS.class));

			context.startService(new Intent(context,
					ServicesSocketMap.class));

		}
	}
}