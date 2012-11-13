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

public class GroupPreconditions extends CoreTest {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(GroupPreconditions.class);
    }

    public void testNull() {
        Point p1 = new Point(10, 100);
        Group g  = new Group(p1);

        try {
            g.add(null);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException ea) {
        }
    }

    public void testNonNull() {
        Point p1 = new Point(10, 100);
        Group g  = new Group(p1);
        Point p2 = new Point(20, 200);

        g.add(p2);
    }
    
    public void testSelf() {
        Point p1 = new Point(10, 100);
        Group g  = new Group(p1);

        try {
            g.add(g);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException ea) {
        }
    }

    public void testNotSelf() {
        Point p1 = new Point(10, 100);
        Group g1 = new Group(p1);
        Group g2 = new Group(p1);

        g1.add(g2);
    }
}
