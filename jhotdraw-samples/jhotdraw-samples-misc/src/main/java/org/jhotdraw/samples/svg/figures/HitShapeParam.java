package org.jhotdraw.samples.svg.figures;

import java.awt.*;

public class HitShapeParam<T,S> {
    private Shape cachedHitShape;
    private final T fillColor;
    private final S fillGradient;
    private final Shape transformedShape;

    public HitShapeParam(Shape cachedHitShape, T fillColor, S fillGradient, Shape transformedShape) {
        this.cachedHitShape = cachedHitShape;
        this.fillColor = fillColor;
        this.fillGradient = fillGradient;
        this.transformedShape = transformedShape;
    }

    public Shape getCachedHitShape() {
        return cachedHitShape;
    }

    public T getFillColor() {
        return fillColor;
    }

    public S getFillGradient() {
        return fillGradient;
    }

    public Shape getTransformedShape() {
        return transformedShape;
    }

    public void setCachedHitShape(Shape cachedHitShape) {
        this.cachedHitShape = cachedHitShape;
    }
}
