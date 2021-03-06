//*********************************************************************************************************************
//  Copyright 2011 by the University of Virginia
//	All Rights Reserved
//
//  Created by Patrick Keith-Hynes
//  Center for Diabetes Technology
//  University of Virginia
//*********************************************************************************************************************
package edu.virginia.dtc.SysMan;

import android.content.Context;
import android.database.Cursor;
import edu.virginia.dtc.Tvector.Pair;
import edu.virginia.dtc.Tvector.Tvector;

public class DiAsSubjectData extends Object	
{
	final static String TAG = "DiAsSubjectData";
	
	// Default remote monitoring URI
	public static final String PROFILE_CHANGE = "edu.virginia.dtc.DIASSETUP_PROFILE_CHANGED";
	
	// Storage for subject session parameters
	//---------------------------------------------
	// - DiAsSetup 1
   	public String subjectName, subjectSession;
   	public int subjectAIT, subjectWeight, subjectHeight, subjectAge;
   	public double subjectTDI;
   	public boolean subjectFemale;
	// - DiAsSetup 2-4
   	public Tvector subjectCR, subjectCF, subjectBasal, subjectSafety;
   	
   	// Field validity flags
   	//---------------------------------------------
	// - DiAsSetup 1
   	public boolean subjectNameValid;
   	public boolean subjectSessionValid;
   	public boolean weightValid;
   	public boolean heightValid;
   	public boolean ageValid;
   	public boolean TDIValid;
   	public boolean sexIsFemale;
   	public boolean AITValid;
   	
	// - DiAsSetup 2-4
   	public boolean subjectCFValid;
   	public boolean subjectCRValid;
   	public boolean subjectBasalValid;
	public boolean subjectTimeRangeValid;

   	public DiAsSubjectData() 
   	{	
   		//Subject Data parameters
   		//------------------------------
   		subjectName = new String("");
   		subjectSession = new String("");
   		
   		subjectAIT=0;
   		subjectWeight=0;
   		subjectHeight=0;
   		subjectAge=0;
   		subjectTDI=0.0;
   		
   		subjectFemale=false;
   		
   		subjectCR = new Tvector(12);
   		subjectCF = new Tvector(12);
   		subjectBasal = new Tvector(12);
   		subjectSafety = new Tvector(12);
   		
   		//Validity parameters
   		//------------------------------
   		subjectNameValid = false;
   		subjectSessionValid = false;
   		weightValid = false;
   		heightValid = false;
   		ageValid = false;
   		TDIValid = false;
   		
   		subjectCFValid = false;
   		subjectCRValid = false;
   		subjectBasalValid = false;
   		subjectTimeRangeValid = false;
   	}
   	
   	public static DiAsSubjectData readDiAsSubjectData(Context ctx) {
		DiAsSubjectData subject_data = new DiAsSubjectData();
		
		Cursor c = ctx.getContentResolver().query(Biometrics.SUBJECT_DATA_URI, null, null, null, null);
		Debug.i(TAG, "readDiAsSubjectData", "Retrieved SUBJECT_DATA_URI with " + c.getCount() + " items");
		
		if (c.moveToLast()) {
			subject_data.subjectName = new String(c.getString(c.getColumnIndex("subjectid")));
			subject_data.subjectSession = new String(c.getString(c.getColumnIndex("session")));
			subject_data.subjectWeight = (c.getInt(c.getColumnIndex("weight")));
			subject_data.subjectHeight = (c.getInt(c.getColumnIndex("height")));
			subject_data.subjectAge = (c.getInt(c.getColumnIndex("age")));
			subject_data.subjectTDI = (c.getInt(c.getColumnIndex("TDI")));
			subject_data.subjectAIT = 4; // Force AIT == 4 for safety

			int isfemale = c.getInt(c.getColumnIndex("isfemale"));
			if (isfemale == 1)
				subject_data.subjectFemale = true;
			else
				subject_data.subjectFemale = false;

			// Set flags
			subject_data.subjectNameValid = true;
			subject_data.subjectSessionValid = true;
			subject_data.weightValid = true;
			subject_data.heightValid = true;
			subject_data.ageValid = true;
			subject_data.TDIValid = true;
			subject_data.AITValid = true;
		}
		else
			return null;
		
		c.close();
		
		if (Tvector.readTvector(subject_data.subjectCF, Biometrics.CF_PROFILE_URI, ctx.getContentResolver()))
			subject_data.subjectCFValid = true;
		else
			return null;
		if (Tvector.readTvector(subject_data.subjectCR, Biometrics.CR_PROFILE_URI, ctx.getContentResolver()))
			subject_data.subjectCRValid = true;
		else
			return null;
		if (Tvector.readTvector(subject_data.subjectBasal, Biometrics.BASAL_PROFILE_URI, ctx.getContentResolver()))
			subject_data.subjectBasalValid = true;
		else
			return null;
		if (Tvector.readTvector(subject_data.subjectSafety, Biometrics.SAFETY_PROFILE_URI, ctx.getContentResolver()))
			subject_data.subjectTimeRangeValid = true;
		
		return subject_data;
	}
   	
   	public static void print(String LOCAL_TAG, DiAsSubjectData base)
   	{
   		final String FUNC_TAG = "print";
   		
   		Debug.i(LOCAL_TAG, FUNC_TAG, "Subject Name: "+base.subjectName+" Valid: "+base.subjectNameValid);
   		Debug.i(LOCAL_TAG, FUNC_TAG, "Subject ID: "+base.subjectSession+" Valid: "+base.subjectSessionValid);
   		
   		Debug.i(LOCAL_TAG, FUNC_TAG, "AIT: "+base.subjectAIT);
   		Debug.i(LOCAL_TAG, FUNC_TAG, "Weight: "+base.subjectWeight+" Valid: "+base.weightValid);
   		Debug.i(LOCAL_TAG, FUNC_TAG, "Height: "+base.subjectHeight+" Valid: "+base.heightValid);
   		Debug.i(LOCAL_TAG, FUNC_TAG, "Age: "+base.subjectAge+" Valid: "+base.ageValid);
   		Debug.i(LOCAL_TAG, FUNC_TAG, "TDI: "+base.subjectTDI+" Valid: "+base.TDIValid);
   		
   		Debug.i(LOCAL_TAG, FUNC_TAG, "Female: "+base.subjectFemale);
   	}
   	
   	public static boolean isChanged(DiAsSubjectData base, DiAsSubjectData test)
   	{
   		final String FUNC_TAG = "isChanged";
   		
   		Debug.i(TAG, FUNC_TAG, "Base: "+base.subjectName+" Test: "+test.subjectName);
   		Debug.i(TAG, FUNC_TAG, "Base: "+base.subjectSession+" Test: "+test.subjectSession);
   		
   		Debug.i(TAG, FUNC_TAG, "Base: "+base.subjectAIT+" Test: "+test.subjectAIT);
   		Debug.i(TAG, FUNC_TAG, "Base: "+base.subjectWeight+" Test: "+test.subjectWeight);
   		Debug.i(TAG, FUNC_TAG, "Base: "+base.subjectHeight+" Test: "+test.subjectHeight);
   		Debug.i(TAG, FUNC_TAG, "Base: "+base.subjectAge+" Test: "+test.subjectAge);
   		Debug.i(TAG, FUNC_TAG, "Base: "+base.subjectTDI+" Test: "+test.subjectTDI);
   		
   		Debug.i(TAG, FUNC_TAG, "Base: "+base.subjectFemale+" Test: "+test.subjectFemale);
   		
   		if(!base.subjectName.equals(test.subjectName))
   			return true;
   		if(!base.subjectSession.equals(test.subjectSession))
   			return true;
   		
   		if(base.subjectAIT != test.subjectAIT)
   			return true;
   		if(base.subjectWeight != test.subjectWeight)
   			return true;
   		if(base.subjectHeight != test.subjectHeight)
   			return true;
   		if(base.subjectAge != test.subjectAge)
   			return true;
   		if(base.subjectTDI != test.subjectTDI)
   			return true;
   		if(base.subjectFemale != test.subjectFemale)
   			return true;
   		
   		if(base.subjectCR.count() != test.subjectCR.count())
   			return true;
   		else	//Then the counts are at least the same
   		{
   			if(profileDifferent(base.subjectCR, test.subjectCR))
   			{
   				Debug.i(TAG, FUNC_TAG, "Difference in CR profile!");
   				return true;
   			}
   		}
   		Debug.i(TAG, FUNC_TAG, "No differences in CR profile!");
   		
   		if(base.subjectCF.count() != test.subjectCF.count())
   			return true;
   		else	//Then the counts are at least the same
   		{
   			if(profileDifferent(base.subjectCF, test.subjectCF))
   			{
   				Debug.i(TAG, FUNC_TAG, "Difference in CF profile!");
   				return true;
   			}
   		}
   		Debug.i(TAG, FUNC_TAG, "No differences in CF profile!");
   		
   		if(base.subjectBasal.count() != test.subjectBasal.count())
   			return true;
   		else	//Then the counts are at least the same
   		{
   			if(profileDifferent(base.subjectBasal, test.subjectBasal))
   			{
   				Debug.i(TAG, FUNC_TAG, "Difference in Basal profile!");
   				return true;
   			}
   		}
   		Debug.i(TAG, FUNC_TAG, "No differences in Basal profile!");
   		
   		if(base.subjectSafety.count() != test.subjectSafety.count())
   			return true;
   		else	//Then the counts are at least the same
   		{
   			if(profileDifferent(base.subjectSafety, test.subjectSafety))
   			{
   				Debug.i(TAG, FUNC_TAG, "Difference in Safety profile!");
   				return true;
   			}
   		}
   		Debug.i(TAG, FUNC_TAG, "No differences in Safety profile!");
   		
   		return false;
   	}
   	
   	private static boolean profileDifferent(Tvector base, Tvector test)
   	{
   		final String FUNC_TAG = "profileDifferent";
   		
   		//Iterate through the profile and return
   		//true if there are any differences
   		for(int i = 0;i<base.count();i++)
   		{
   			Pair b = base.get(i);
   			Pair t = test.get(i);
   			
   			Debug.i(TAG, FUNC_TAG, "Base: "+b.value()+", "+b.time()+", "+b.endTime()+" Test: "+t.value()+", "+t.time()+", "+t.endTime());
   			
   			if(b.value() != t.value())
   				return true;
   			if(b.time() != t.time())
   				return true;
   			if(b.endTime() != t.endTime())
   				return true;
   		}
   		
   		return false;
   	}
   	
}
