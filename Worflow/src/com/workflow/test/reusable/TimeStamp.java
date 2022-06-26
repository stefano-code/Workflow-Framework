package com.workflow.test.reusable;

public class TimeStamp
{
	private boolean enabled;
	private long timeStampDisable = 0;
	private long timeStampDeltaDisable = 0;
	private long timeStamp = 0;

	public TimeStamp()
	{
		snapShot();
	}

	public void snapShot()
	{
		enabled = true;
		timeStampDisable = 0;
		timeStampDeltaDisable = 0;
		timeStamp = System.nanoTime()/1000000;	//SS System.currentTimeMillis();
	}

	//SS public long getTimeStamp()
	//SS {
	//SS 	return timeStamp;
	//SS }

	public long getElapsed()
	{
		long diff;
		diff = System.nanoTime()/1000000;	//SS System.currentTimeMillis();
		if ((timeStamp + timeStampDeltaDisable) >= diff)
			return 0;

		diff -= (timeStamp + timeStampDeltaDisable);

		return (diff); // in msec
	}

	public boolean isElapsed(long ulPeriodMSecs)
	{
		boolean ret = false;

		long diff = getElapsed();
		if (diff >= ulPeriodMSecs)
			ret = true;
		else
			ret = false;

		return ret;
	}

	public boolean isEnable()
	{
		return enabled;
	}

	public void pause()
	{
		if (enabled == true)
		{
			long tmp = System.nanoTime()/1000000;	//SS System.currentTimeMillis();
			timeStampDisable = tmp;
			enabled = false;
		}
	}

	public void restart()
	{
		if (enabled == false)
		{
			long tmp = System.nanoTime()/1000000;	//SS System.currentTimeMillis();
			timeStampDeltaDisable += (tmp - timeStampDisable);
			enabled = true;
		}
	}
}
