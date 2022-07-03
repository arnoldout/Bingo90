package com.oliver.bingo90;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TicketService {

        private final int BUFFER_FOR_UPPER_RANGE = 1;
        private static TicketService ticketService;

        private TicketService()
        {
        }

        public static TicketService getInstance()
        {
                if(ticketService==null)
                {
                        ticketService = new TicketService();
                }
                return ticketService;
        }

        public List<int[]> generateTicket()
        {
                List<int[]> tickets = new ArrayList<>();
                List<LinkedList> bingoSheet = TicketService.getInstance().generateNewBingoSheet();
                for (int ticketRowCounter = 0; ticketRowCounter < bingoSheet.size(); ticketRowCounter++) {
                        int[] columnVariation = getColumnVariation(ticketRowCounter);
                        for (int ticketColumnCounter = 0; ticketColumnCounter < columnVariation.length; ticketColumnCounter++) {
                                tickets.add(TicketService.getInstance().generateNumbersFromRange(bingoSheet.get(ticketRowCounter), columnVariation[ticketColumnCounter]));
                        }
                }
                return tickets;
        }

        private int[] getColumnVariation(int ticketColumnNo) {
                int[][] col9LenVariation = new int[][] { { 1, 2, 2, 2, 1, 1 }, { 3, 1, 1, 1, 1, 2 } };
                int[][] col10LenVariation = new int[][] { { 2, 2, 2, 2, 1, 1 }, { 3, 3, 1, 1, 1, 1 }, { 3, 2, 2, 1, 1, 1 } };
                int[][] col11LenVariation = new int[][] { { 3, 3, 2, 1, 1, 1 }, { 2, 2, 2, 2, 2, 1 } };
                final int firstColumn = 0;
                final int finalColumn = 8;
                int[] newColumnVariation;
                if (ticketColumnNo == firstColumn) {
                        //grab a random column variation from the list
                        newColumnVariation = col9LenVariation[ThreadLocalRandom.current().nextInt(2)];
                } else if (ticketColumnNo == finalColumn) {
                        //grab a random column variation from the list
                        newColumnVariation = col11LenVariation[ThreadLocalRandom.current().nextInt(2)];
                } else {
                        //grab a random column variation from the list
                        newColumnVariation = col10LenVariation[ThreadLocalRandom.current().nextInt(3)];
                }
                //shuffle the array for even more variation in our ticket output
                shuffleArray(newColumnVariation);
                return newColumnVariation;
        }

        public List<LinkedList> generateNewBingoSheet() {
                List<LinkedList> lists = new ArrayList<>();
                lists.add(new LinkedList(1, 9));
                lists.add(new LinkedList(10, 19));
                lists.add(new LinkedList(20, 29));
                lists.add(new LinkedList(30, 39));
                lists.add(new LinkedList(40, 49));
                lists.add(new LinkedList(50, 59));
                lists.add(new LinkedList(60, 69));
                lists.add(new LinkedList(70, 79));
                lists.add(new LinkedList(80, 90));
                return lists;
        }

        public int[] generateNumbersFromRange(LinkedList list, int arraySize)
        {
                int ticketColSize = 3;
                int [] generated = new int[ticketColSize];
                Arrays.fill(generated, 0);
                if(arraySize!=list.getSize()) {
                        //we don't want to give the highest numbers away at the beginning if we need multiple numbers
                        int maxRange = list.getSize() - arraySize+BUFFER_FOR_UPPER_RANGE;
                        int minRange = 1;
                        LinkedListNode pointer = list.getHead();
                        for (int i = 0; i < arraySize; i++) {
                                int nodeVal = ThreadLocalRandom.current().nextInt(minRange, maxRange);
                                //set minRange to nodeVal so next number is guaranteed to be higher than previous
                                minRange = nodeVal;
                                //no need to modify maxRange as linkedList will shrink in size
                                generated = placeValueInArray(i, arraySize, generated, list.popItem(nodeVal));
                        }
                }
                else{
                        //we don't need any complicated logic if ticket column needs every remaining number
                        generated = fillFinalColumn(list, generated);
                }
                return generated;

        }

        private int[] placeValueInArray(int iteratorCount, int requestedSize, int[] toPlace, int newValue)
        {
                //depending on the amount of spaces we need to fill for this column. Find best place to put our new value
                switch (requestedSize)
                {
                        case 3:
                                placeItemFor3LengthCol(iteratorCount, toPlace, newValue);
                                break;
                        case 2:
                                placeItemFor2LengthCol(iteratorCount, toPlace, newValue);
                                break;
                        case 1:
                                placeItemFor1LengthCol(toPlace, newValue);
                                break;
                }
                return toPlace;
        }

        private void placeItemFor1LengthCol(int[] toPlace, int newValue) {
                //we need to place one item in a possibility of 3 places
                final int availablePlaces = 3;
                toPlace[ThreadLocalRandom.current().nextInt(availablePlaces)] = newValue;
        }

        private void placeItemFor3LengthCol(int iteratorCount, int[] toPlace, int newValue) {
                //we need to fit 3 items in 3 places.
                toPlace[iteratorCount] = newValue;
        }

        private void placeItemFor2LengthCol(int iteratorCount, int[] toPlace, int newValue) {
                final int firstPlacement = 0;
                final int emptyPlace = 0;
                if(iteratorCount == firstPlacement)
                {
                        toPlace[ThreadLocalRandom.current().nextInt(2)] = newValue;
                        return;
                }

                if(toPlace[1]!=emptyPlace)
                {
                        //If we are on our second placement and the second space is full.
                        //We are forced to place our item in the final box.
                        toPlace[2] = newValue;
                        return;
                }
                //Place our item in box 2 or 3
                toPlace[ThreadLocalRandom.current().nextInt(1, 3)] = newValue;
                return;
        }

        //iterate through list and fill array with all remaining values
        private int[] fillFinalColumn(LinkedList list, int[] finalArray)
        {
                LinkedListNode pointer = list.getHead();
                for(int i=0; i<list.getSize(); i++) {
                        finalArray[i] = pointer.getValue();
                        pointer = pointer.getNext();
                }
                return finalArray;
        }

        private int[] shuffleArray(int[] array){
                for (int i=0; i<array.length; i++) {
                        int randomPosition = ThreadLocalRandom.current().nextInt(array.length);
                        int temp = array[i];
                        array[i] = array[randomPosition];
                        array[randomPosition] = temp;
                }
                return array;
        }


}
