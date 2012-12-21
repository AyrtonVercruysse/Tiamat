/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved.
 * This program and the accompanying materials are made available
 * under the terms of the Common Public License v1.0
 * which accompanies this distribution and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *
 * Contributors:
 *     PARC     initial implementation
 * ******************************************************************/

package tests;

import figures.*;

public class PointPreconditions extends CoreTest {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(PointPreconditions.class);
    }

    public void testTooSmall() {
        Point p1 = new Point(10, 100);
        try {
            p1.setX(-10);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException ea) {
        }
    }

    public void testNotTooSmall() {
        Point p1 = new Point(10, 100);
        p1.setX(0);
    }

    public void testMove() {
        Line l1 = new Line(new Point(10, 100), new Point(20, 200));
        try {
            l1.move(-500, -500);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException ea) {
        }
    }
}