package com.github.poke.tweeplusreader;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Row-based layout that fills a single line as long as possible and
 * automatically wraps into the next row if required.
 * 
 */
public class WrapLayout extends ViewGroup
{
	private static final int DEFAULT_HORIZONTAL_SPACING = 2;
	private static final int DEFAULT_VERTICAL_SPACING = 2;
	private int horizontalSpacing;
	private int verticalSpacing;
	
	/**
	 * Construct a WrapLayout.
	 * 
	 * @param context the application context.
	 */
	public WrapLayout ( Context context )
	{
		super( context );
		horizontalSpacing = DEFAULT_HORIZONTAL_SPACING;
		verticalSpacing = DEFAULT_VERTICAL_SPACING;
	}
	
	/**
	 * Construct a WrapLayout with the given attributes.
	 * 
	 * @param context the application context.
	 * @param attrs the attribute set.
	 */
	public WrapLayout ( Context context, AttributeSet attrs )
	{
		super( context, attrs );
		TypedArray styledAttributes = context.obtainStyledAttributes( attrs, R.styleable.WrapLayout );
		horizontalSpacing = styledAttributes.getDimensionPixelSize( R.styleable.WrapLayout_android_horizontalSpacing,
				DEFAULT_HORIZONTAL_SPACING );
		verticalSpacing = styledAttributes.getDimensionPixelSize( R.styleable.WrapLayout_android_verticalSpacing,
				DEFAULT_VERTICAL_SPACING );
		styledAttributes.recycle();
	}
	
	/**
	 * Return the horizontal spacing between child views within a single row.
	 * 
	 * @return the horizontal spacing.
	 */
	public int getHorizontalSpacing ()
	{
		return horizontalSpacing;
	}
	
	/**
	 * Set the horizontal spacing between child views within a single row.
	 * 
	 * @param horizontalSpacing the new spacing.
	 */
	public void setHorizontalSpacing ( int horizontalSpacing )
	{
		this.horizontalSpacing = horizontalSpacing;
	}
	
	/**
	 * Return the vertical spacing between rows.
	 * 
	 * @return the vertical spacing.
	 */
	public int getVerticalSpacing ()
	{
		return verticalSpacing;
	}
	
	/**
	 * St the vertical spacing between rows.
	 * 
	 * @param verticalSpacing the new spacing.
	 */
	public void setVerticalSpacing ( int verticalSpacing )
	{
		this.verticalSpacing = verticalSpacing;
	}
	
	@Override
	protected void onMeasure ( int widthMeasureSpec, int heightMeasureSpec )
	{
		final int widthMode = MeasureSpec.getMode( widthMeasureSpec );
		final int widthSize = MeasureSpec.getSize( widthMeasureSpec );
		final int heightMode = MeasureSpec.getMode( heightMeasureSpec );
		final int heightSize = MeasureSpec.getSize( heightMeasureSpec );
		
		// if exact measurement is specified, just take a shortcut
		if ( widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY )
		{
			setMeasuredDimension( widthSize, heightSize );
			return;
		}
		
		// calculate dimensions
		final int availableWidth = widthSize - getPaddingRight() - getPaddingLeft();
		final int availableHeight = heightSize - getPaddingTop() - getPaddingBottom();
		int maxWidth = Integer.MIN_VALUE;
		int rowHeight = Integer.MIN_VALUE;
		int x = 0, y = 0;
		
		for ( int i = 0; i < getChildCount(); i++ )
		{
			final View child = getChildAt( i );
			if ( child.getVisibility() == GONE )
				continue;
			
			// measure child
			LayoutParams layoutParams = child.getLayoutParams();
			child.measure( createMeasureSpec( layoutParams.width, availableWidth, widthMode ),
					createMeasureSpec( layoutParams.height, availableHeight, heightMode ) );
			
			final int width = child.getMeasuredWidth();
			final int height = child.getMeasuredHeight();
			
			// new row required
			if ( x + width > availableWidth )
			{
				maxWidth = Math.max( maxWidth, x );
				x = 0;
				y += rowHeight + verticalSpacing;
				rowHeight = Integer.MIN_VALUE;
			}
			
			// update values
			rowHeight = Math.max( rowHeight, height );
			x += width + horizontalSpacing;
		}
		
		// finish calculations on last row and set measured dimension
		x += getPaddingLeft() + getPaddingRight();
		y += getPaddingTop() + getPaddingBottom();
		setMeasuredDimension( widthMode == MeasureSpec.EXACTLY ? widthSize : Math.max( maxWidth, x ),
				heightMode == MeasureSpec.EXACTLY ? heightSize : y + rowHeight );
	}
	
	@Override
	protected void onLayout ( boolean changed, int l, int t, int r, int b )
	{
		final int availableWidth = r - l - getPaddingRight();
		int rowHeight = Integer.MIN_VALUE;
		int x = getPaddingLeft();
		int y = getPaddingTop();
		
		for ( int i = 0; i < getChildCount(); i++ )
		{
			final View child = getChildAt( i );
			if ( child.getVisibility() == GONE )
				continue;
			
			final int width = child.getMeasuredWidth();
			final int height = child.getMeasuredHeight();
			
			// new row required
			if ( x + width > availableWidth )
			{
				x = getPaddingLeft();
				y += rowHeight + verticalSpacing;
				rowHeight = Integer.MIN_VALUE;
			}
			
			// layout child and update values
			child.layout( x, y, x + width, y + height );
			rowHeight = Math.max( rowHeight, height );
			x += width + horizontalSpacing;
		}
	}
	
	/**
	 * Create a measure specification for a child.
	 * 
	 * @param size Supplied size of the child's layout parameters.
	 * @param maxSize Maximum available size.
	 * @param parentMode Parent's measure specification mode.
	 * @return created measure specification.
	 */
	private int createMeasureSpec ( int size, int maxSize, int parentMode )
	{
		switch ( size )
		{
			case LayoutParams.WRAP_CONTENT:
				return MeasureSpec.makeMeasureSpec( maxSize,
						parentMode == MeasureSpec.UNSPECIFIED ? MeasureSpec.UNSPECIFIED : MeasureSpec.AT_MOST );
				
			case LayoutParams.FILL_PARENT:
				return MeasureSpec.makeMeasureSpec( maxSize, MeasureSpec.EXACTLY );
				
			default:
				return MeasureSpec.makeMeasureSpec( size, MeasureSpec.EXACTLY );
		}
	}
}