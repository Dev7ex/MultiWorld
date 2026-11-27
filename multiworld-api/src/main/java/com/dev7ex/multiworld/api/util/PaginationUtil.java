package com.dev7ex.multiworld.api.util;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Utility class for handling list pagination, including splitting a list into pages
 * and calculating total page count.
 *
 * @author Dev7ex
 * @since 11.06.2025
 */
public final class PaginationUtil {

    private PaginationUtil() {
        // Utility class; prevent instantiation
    }

    /**
     * Splits the given list into a sublist representing a specific page.
     *
     * @param list           the original list to split
     * @param entriesPerPage number of entries per page (must be > 0)
     * @param page           page number (1-based)
     * @param <T>            the type of list element
     * @return a sublist representing the specified page; empty if page is out of range
     */
    public static <T> List<T> splitPage(@NotNull final List<T> list, final int entriesPerPage, final int page) {
        if (entriesPerPage <= 0 || page <= 0) {
            return Collections.emptyList();
        }

        final int startIndex = entriesPerPage * (page - 1);
        if (startIndex >= list.size()) {
            return Collections.emptyList();
        }

        final int endIndex = Math.min(startIndex + entriesPerPage, list.size());
        return list.subList(startIndex, endIndex);
    }

    /**
     * Calculates the total number of pages based on the list size and entries per page.
     *
     * @param list           the list to paginate
     * @param entriesPerPage number of entries per page (must be > 0)
     * @return total number of pages; 0 if list is empty or entriesPerPage is invalid
     */
    public static int getPageCount(@NotNull final List<?> list, final int entriesPerPage) {
        if (entriesPerPage <= 0 || list.isEmpty()) {
            return 0;
        }
        return (int) Math.ceil(list.size() / (double) entriesPerPage);
    }

    /**
     * Checks whether a specific page number is valid for the given list.
     *
     * @param list           the list to check
     * @param entriesPerPage number of entries per page
     * @param page           page number (1-based)
     * @return true if the page exists; false otherwise
     */
    public static boolean isValidPage(@NotNull final List<?> list, final int entriesPerPage, final int page) {
        return page > 0 && page <= PaginationUtil.getPageCount(list, entriesPerPage);
    }

    /**
     * Returns the index of the first item on the given page.
     *
     * @param entriesPerPage number of entries per page
     * @param page           page number (1-based)
     * @return the zero-based index of the first item; -1 if invalid input
     */
    public static int getStartIndex(final int entriesPerPage, final int page) {
        if (entriesPerPage <= 0 || page <= 0) {
            return -1;
        }
        return entriesPerPage * (page - 1);
    }

    /**
     * Returns the index of the last item on the given page (exclusive upper bound).
     *
     * @param entriesPerPage number of entries per page
     * @param page           page number (1-based)
     * @param totalSize      total number of items
     * @return the index after the last item on the page; -1 if invalid input
     */
    public static int getEndIndex(final int entriesPerPage, final int page, final int totalSize) {
        if (entriesPerPage <= 0 || page <= 0 || totalSize < 0) {
            return -1;
        }
        return Math.min(totalSize, PaginationUtil.getStartIndex(entriesPerPage, page) + entriesPerPage);
    }

}
