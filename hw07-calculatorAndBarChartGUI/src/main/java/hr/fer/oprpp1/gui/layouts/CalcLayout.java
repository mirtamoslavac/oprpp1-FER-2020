package hr.fer.oprpp1.gui.layouts;

import java.util.*;
import java.awt.*;
import java.util.function.Function;

/**
 * The {@code CalcLayout} class represents a layout used for displaying calculator's elements. It implements the {@link LayoutManager2} interface.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class CalcLayout implements LayoutManager2 {
    /**
     * Number of rows within the grid of the layout.
     */
    private static final int ROWS = 5;
    /**
     * Number of columns within the grid of the layout.
     */
    private static final int COLUMNS = 7;
    /**
     * Map storing {@link RCPosition}s and their respective {@link Component}s within the current layout.
     */
    private final Map<RCPosition, Component> components;
    /**
     * Fixed gap size between rows and columns of the current layout.
     */
    private final int gapSize;

    /**
     * Creates a new {@code CalcLayout} instance with a defined {@code gapSize}.
     *
     * @param gapSize fixed gap size between rows and columns.
     */
    public CalcLayout(int gapSize) {
        if (gapSize < 0) throw new CalcLayoutException("The given gap size cannot be negative!");
        this.gapSize = gapSize;
        this.components = new TreeMap<>((a, b) -> {
            if (a.getRow() < b.getRow()) return -1;
            else if (a.getRow() > b.getRow()) return 1;
            else return Integer.compare(a.getColumn(), b.getColumn());
        });
    }

    /**
     * Creates a new {@code CalcLayout} instance with a fixed gap size of 0.
     */
    public CalcLayout() {
        this(0);
    }

    /**
     * {@inheritDoc}
     *
     * @throws CalcLayoutException when the given row and column numbers within the given {@code constraints} are out of defined bounds
     * or when attempting to add a new component to a position already containing another component.
     * @throws IllegalArgumentException when the given {@code constraints} is not of the String or {@link RCPosition} type or when it cannot be parsed into a {@link RCPosition}.
     * @throws NullPointerException when the given {@code comp} or {@code constraints} are {@code null}.
     */
    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        Objects.requireNonNull(comp, "The given component cannot be null!");
        Objects.requireNonNull(constraints, "The given constraints cannot be null!");
        if(!(constraints instanceof RCPosition) && !(constraints instanceof String))
            throw new IllegalArgumentException("The given constraints are not of the RCPosition or String type!");

        RCPosition position;
        if (constraints instanceof RCPosition) position = (RCPosition)constraints;
        else {
            try {
                 position = RCPosition.parse((String)constraints);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("The given constraint cannot be parsed as the RCPosition type!");
            }
        }

        int row = position.getRow();
        int column = position.getColumn();

        if (row < 1 || row > ROWS) throw new CalcLayoutException("Invalid row number! Expected a number between 1 and 5, got " + row + "!");
        if (column < 1 || column > COLUMNS) throw new CalcLayoutException("Invalid column number! Expected a number between 1 and 7, got " + column + "!");

        if (row == 1 && column > 1 && column < 6)
            throw new CalcLayoutException("Invalid column number for the first row! Expected either a 6 or 7, got " + column + "!");

        if (this.components.containsKey(position)) throw new CalcLayoutException("There is already a component assigned to this position!");

        this.components.put(position, comp);
    }

    /**
     * {@inheritDoc}
     *
     * @throws UnsupportedOperationException since this method should not be called.
     */
    @Override
    public void addLayoutComponent(String name, Component comp) {
        throw new UnsupportedOperationException("This method should not be invoked!");
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException when the given {@code comp} is {@code null}.
     */
    @Override
    public void removeLayoutComponent(Component comp) {
        this.components.values().remove(Objects.requireNonNull(comp, "The given component cannot be null!"));
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException when the given {@code comp} is {@code null} or when the wanted {@link Dimension} instance cannot be calculated.
     */
    @Override
    public Dimension maximumLayoutSize(Container target) {
        return calculateLayoutSize(Objects.requireNonNull(target, "The given target container cannot be null!"), LayoutSizeType.MAXIMUM);
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException when the given {@code comp} is {@code null} or when the wanted {@link Dimension} instance cannot be calculated.
     */
    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return calculateLayoutSize(Objects.requireNonNull(parent, "The given parent container cannot be null!"), LayoutSizeType.PREFERRED);
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException when the given {@code comp} is {@code null} or when the wanted {@link Dimension} instance cannot be calculated.
     */
    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return calculateLayoutSize(Objects.requireNonNull(parent, "The given target container cannot be null!"), LayoutSizeType.MINIMUM);
    }

    /**
     * Calculates the specified layout size.
     *
     * @param container container used for the calculation.
     * @param layoutSizeType type of layout size to calculate.
     * @return {@link Dimension} instance that is the wanted layout size.
     */
    private Dimension calculateLayoutSize(Container container, LayoutSizeType layoutSizeType) {
        final Function<Component, Dimension> action;

        switch (layoutSizeType) {
            case MAXIMUM -> action = Component::getMaximumSize;
            case MINIMUM -> action = Component::getMinimumSize;
            default -> action = Component::getPreferredSize;
        }

        OptionalInt maxParentWidth = this.components.entrySet().stream().filter(Objects::nonNull)
                .mapToInt(mapEntry -> {
                    if (mapEntry.getKey().equals(new RCPosition(1, 1))) return (action.apply(mapEntry.getValue()).width - (ROWS - 1) * this.gapSize) / ROWS;
                    return action.apply(mapEntry.getValue()).width;})
                .max();
        OptionalInt maxParentHeight = this.components.values().stream().filter(Objects::nonNull).map(action).mapToInt(dimension -> dimension.height).max();

        if (maxParentHeight.isEmpty() || maxParentWidth.isEmpty()) return null;

        Insets insets = container.getInsets();
        int dimensionWidth = (maxParentWidth.getAsInt() * COLUMNS) + (this.gapSize * (COLUMNS - 1)) + insets.left + insets.right;
        int dimensionHeight = (maxParentHeight.getAsInt() * ROWS) + (this.gapSize * (ROWS - 1)) + insets.top + insets.bottom;

        return new Dimension(dimensionWidth, dimensionHeight);
    }

    /**
     * The {@code LayoutSizeType} enum lists possible types of possible layout size calculations.
     *
     * @author mirtamoslavac
     * @version 1.0
     */
    private enum LayoutSizeType {
        /**
         * Selected when wanting to determine the the maximum size of the container.
         */
        MAXIMUM,
        /**
         * Selected when wanting to determine the the preferred size of the container.
         */
        PREFERRED,
        /**
         * Selected when wanting to determine the the minimum size of the container.
         */
        MINIMUM
    }

    /**
     * {@inheritDoc}
     *
     * @return 0 always.
     */
    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    /**
     * {@inheritDoc}
     *
     * @return 0 always.
     */
    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    @Override
    public void invalidateLayout(Container target) {
        Objects.requireNonNull(target, "The given target container cannot be null!");
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException when the given {@code parent} is {@code null}.
     */
    @Override
    public void layoutContainer(Container parent) {
        Objects.requireNonNull(parent, "The given parent container cannot be null!");

        Insets insets = parent.getInsets();

        int[] componentWidths = calculateComponentArray(parent.getWidth(), insets.left, insets.right, COLUMNS);
        int[] componentHeights = calculateComponentArray(parent.getHeight(), insets.top, insets.bottom, ROWS);

        int calculatorDisplayWidth = 0;
        for (int i = 1; i <= ROWS; i++) calculatorDisplayWidth += componentWidths[i];

        int heightOffset = insets.top;
        int widthOffset = insets.left;

        for (Map.Entry<RCPosition, Component> componentWithPosition : this.components.entrySet()) {

            RCPosition position = componentWithPosition.getKey();
            Component component = componentWithPosition.getValue();
            int row = position.getRow(), column = position.getColumn();

            if (row == 1 && column == 1) {
                component.setBounds(widthOffset + this.gapSize / 2, heightOffset + this.gapSize / 2, calculatorDisplayWidth + this.gapSize * 4, componentHeights[row]);
                for (int skipped = 2; skipped < ROWS + 1; skipped++) widthOffset += componentWidths[skipped] + (double)this.gapSize;
            } else {
                component.setBounds(widthOffset + this.gapSize / 2, heightOffset + this.gapSize / 2, componentWidths[column], componentHeights[row]);
            }
            if (column == COLUMNS) {
                heightOffset += componentHeights[row] + (double)this.gapSize;
                widthOffset = insets.left;
            } else widthOffset += componentWidths[column] + (double)this.gapSize;
        }

    }

    /**
     * Calculates and uniformly distributes the sizes for a certain dimension of the calculator's components.
     *
     * @param parentLength total length of the wanted category.
     * @param inset1 first inset to be taken into consideration during calculation.
     * @param inset2 second inset to be taken into consideration during calculation.
     * @param elementAmount total number of elements within that category.
     * @return array with the values.
     */
    private int[] calculateComponentArray(int parentLength, int inset1, int inset2, int elementAmount) {
        int[] result = new int[elementAmount + 1];
        int strippedParentLength = parentLength - inset1 - inset2 - this.gapSize * (elementAmount);

        if (strippedParentLength % elementAmount == 0) {
            Arrays.fill(result, strippedParentLength / elementAmount);
            return result;
        }

        for (int i = 1; i <= elementAmount; i++) {
            result[i] = (int)Math.round(strippedParentLength * i * 1. / elementAmount);
            for (int j = i - 1; j >= 1; j--) result[i] -= result[j];
        }

        return result;
    }
}
