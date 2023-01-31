package Model;

public interface Venue
{
	void draw(Universe universe);
	void finalize(Universe universe);
	void initialize(Universe universe);
	void updateForTimerTick(Universe universe);
}
