package sing;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import sing.f.FragMainTab;


public abstract class SlidingTabPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.OnTabClickListener, PagerSlidingTabStrip.OnTabDoubleTapListener {

	protected final FragMainTab[] fragments;

	protected final Context context;

	public abstract int getCacheCount();

	private int lastPostion = 0;

	public SlidingTabPagerAdapter(FragmentManager fm, int count, Context context) {
		super(fm);
		this.fragments = new FragMainTab[count];
		this.context = context;
		lastPostion = 0;
	}

	@Override
	public FragMainTab getItem(int pos) {
		return fragments[pos];
	}

	@Override
	public abstract int getCount();

	@Override
	public abstract CharSequence getPageTitle(int position);

	public void onPageSelected(int position) {
		FragMainTab fragment = getFragmentByPosition(position);

		// INSTANCE
		if (fragment == null) {
			return;
		}

		fragment.onCurrent();
		onLeave(position);
	}

	private void onLeave(int position) {
		FragMainTab fragment = getFragmentByPosition(lastPostion);
		lastPostion = position;
		// INSTANCE
		if (fragment == null) {
			return;
		}
		fragment.onLeave();
	}

	public void onPageScrolled(int position) {
		FragMainTab fragment = getFragmentByPosition(position);

		// INSTANCE
		if (fragment == null) {
			return;
		}

		fragment.onCurrentScrolled();
		onLeave(position);
	}

	private FragMainTab getFragmentByPosition(int position) {
		// IDX
		if (position < 0 || position >= fragments.length) {
			return null;
		}
		return fragments[position];
	}

	@Override
	public void onCurrentTabClicked(int position) {
		FragMainTab fragment = getFragmentByPosition(position);
		// INSTANCE
		if (fragment == null) {
			return;
		}
		fragment.onCurrentTabClicked();
	}

	@Override
	public void onCurrentTabDoubleTap(int position) {
		FragMainTab fragment = getFragmentByPosition(position);
		// INSTANCE
		if (fragment == null) {
			return;
		}
		fragment.onCurrentTabDoubleTap();
	}
}