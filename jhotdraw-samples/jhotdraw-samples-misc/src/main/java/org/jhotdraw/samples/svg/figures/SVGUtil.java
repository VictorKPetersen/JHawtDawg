package org.jhotdraw.samples.svg.figures;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.geom.GrowStroke;

import java.awt.*;

public class SVGUtil {

    private SVGUtil(){}

    public static <T,S> Shape getHitShapeUtil(Figure figure, HitShapeParam<T,S> hitShapeParam){
        if (hitShapeParam.getCachedHitShape() == null) {
            if (hitShapeParam.getFillColor() != null || hitShapeParam.getFillGradient() != null) {
                hitShapeParam.setCachedHitShape(new GrowStroke(
                        (float) AttributeKeys.getStrokeTotalWidth(figure, 1.0) / 2f,
                        (float) AttributeKeys.getStrokeTotalMiterLimit(figure, 1.0))
                        .createStrokedShape(hitShapeParam.getTransformedShape()));
            } else {
                hitShapeParam.setCachedHitShape(AttributeKeys.getHitStroke(figure, 1.0).createStrokedShape(hitShapeParam.getTransformedShape()));
            }
        }
        return hitShapeParam.getCachedHitShape();
    }
}



