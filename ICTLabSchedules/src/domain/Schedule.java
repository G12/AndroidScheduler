package domain;

import java.util.HashMap;

import com.algonquincollege.mad9132.ictlabschedules.DOW;
import com.algonquincollege.mad9132.ictlabschedules.Periods;

/**
 * A schedule has periods by DOW.
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 * @Version 1.0
 */
public class Schedule {

	private HashMap<String, String> schedule;

	public Schedule() {
		super();

		schedule = new HashMap<String, String>();
	}

	public void atPut( Periods period, DOW dow, String scheduledClass ) {
		schedule.put( period.name() + dow.name(), scheduledClass );
	}

	public String getScheduledClassAt( Periods period, DOW dow ) {
		return schedule.get( period.name() + dow.name() );
	}
}
