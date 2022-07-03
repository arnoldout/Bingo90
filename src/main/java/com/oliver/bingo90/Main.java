package com.oliver.bingo90;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

        private static final int TOTAL_REQUIRED_STRIPS = 10000;

        public static void main(String[] args) {
                List<String> allStrips = new ArrayList<>();
                for (int stripCounter = 0; stripCounter < TOTAL_REQUIRED_STRIPS; stripCounter++) {
                        List<int []> strip = TicketService.getInstance().generateTicket();
                        allStrips.add("-----New ticket strip "+(stripCounter+1)+"-----");
                        int totalColumns = 6;
                        for (int columnNo = 0; columnNo < totalColumns; columnNo++) {
                                StringBuilder ticketBuilder = new StringBuilder();
                                int totalRows = 9;
                                for (int rowNo = 0; rowNo < totalRows; rowNo++) {
                                        //data up until now has been sorted by column. User will need data sorted by row to read it.
                                        int nextItem = columnNo + (rowNo * 6);
                                        ticketBuilder.append(Arrays.toString(strip.get(nextItem)));
                                }
                                allStrips.add(ticketBuilder.toString());
                        }
                }
                try {
                        FileService.getInstance().saveTextToFile(allStrips);
                } catch (IOException e) {
                        System.out.println("ERROR - Error writing tickets to output because of: "+e.getLocalizedMessage());
                }
        }

}
