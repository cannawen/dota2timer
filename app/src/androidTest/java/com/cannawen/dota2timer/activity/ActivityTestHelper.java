package com.cannawen.dota2timer.activity;

import android.support.annotation.NonNull;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static android.support.test.espresso.core.internal.deps.guava.base.Preconditions.checkNotNull;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class ActivityTestHelper {
    public static ViewAssertion isVisible() {
        return (view, noView) -> assertThat(view, new VisibilityMatcher(View.VISIBLE));
    }

    public static ViewAssertion isGone() {
        return (view, noView) -> assertThat(view, new VisibilityMatcher(View.GONE));
    }

    public static ViewAssertion isInvisible() {
        return (view, noView) -> assertThat(view, new VisibilityMatcher(View.INVISIBLE));
    }

    public static ViewAssertion itemCount(int count) {
        return (view, noViewFoundException) -> {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            assertThat(adapter.getItemCount(), is(count));
        };
    }

    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

    private static class VisibilityMatcher extends BaseMatcher<View> {
        private int visibility;

        public VisibilityMatcher(int visibility) {
            this.visibility = visibility;
        }

        @Override
        public void describeTo(Description description) {
            String visibilityName;
            if (visibility == View.GONE) visibilityName = "GONE";
            else if (visibility == View.VISIBLE) visibilityName = "VISIBLE";
            else visibilityName = "INVISIBLE";
            description.appendText("View visibility must has equals " + visibilityName);
        }

        @Override
        public boolean matches(Object o) {
            if (o == null) {
                if (visibility == View.GONE || visibility == View.INVISIBLE) return true;
                else if (visibility == View.VISIBLE) return false;
            }

            if (!(o instanceof View))
                throw new IllegalArgumentException("Object must be instance of View. Object is instance of " + o);
            return ((View) o).getVisibility() == visibility;
        }
    }
}
