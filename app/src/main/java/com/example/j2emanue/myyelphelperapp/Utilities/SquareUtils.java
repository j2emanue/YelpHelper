package com.example.j2emanue.myyelphelperapp.Utilities;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by j2emanue on 9/24/16.
 */

public class SquareUtils {

        private static final Bus BUS = new Bus(ThreadEnforcer.MAIN);

        public static Bus getBusInstance() {
            return BUS;
        }

        private SquareUtils() {
            // No instances.
        }
}
