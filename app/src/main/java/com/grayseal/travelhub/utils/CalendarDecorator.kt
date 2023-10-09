package com.grayseal.travelhub.utils

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

/**
 * CalendarDecorator is a class that defines how to decorate specific dates in a calendar view.
 *
 * @param color The color to be used for decorating the specified dates.
 * @param dates A collection of CalendarDay objects representing the dates to be decorated.
 */
class CalendarDecorator(private val color: Int, private val dates: Collection<CalendarDay>) :
    DayViewDecorator {

    /**
     * Determines whether a given date should be decorated.
     *
     * @param day The CalendarDay to be checked for decoration.
     * @return true if the provided date should be decorated, false otherwise.
     */
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dates.contains(day)
    }

    /**
     * Decorates a specific date in the calendar view.
     *
     * @param view The DayViewFacade associated with the date to be decorated.
     */
    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(5f, color)) // Adds a colored dot to the date.
        view?.setDaysDisabled(true) // Disables interaction with the date.
    }
}
