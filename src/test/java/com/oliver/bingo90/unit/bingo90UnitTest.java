package com.oliver.bingo90.unit;

import com.oliver.bingo90.FileService;
import com.oliver.bingo90.LinkedList;
import com.oliver.bingo90.LinkedListNode;
import com.oliver.bingo90.TicketService;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class bingo90UnitTest {

        @Test
        public void testGenerateNumbersAreGreaterThanPreviousSize3_10000Times() {
                for(int i=0; i<=10000; i++) {
                        System.out.println("Starting new test run");
                        LinkedList l = new LinkedList(1, 9);
                        int[] response = TicketService.getInstance().generateNumbersFromRange(l, 3);
                        assertTrue(response[0] < response[1] && response[1] < response[2]);
                }
        }

        @Test
        public void testGenerateNumbersAreGreaterThanPreviousSize3_10000Times_setOf10() {
                for(int i=0; i<=10000; i++) {
                        System.out.println("Starting new test run");
                        LinkedList l = new LinkedList(10, 19);
                        int[] response = TicketService.getInstance().generateNumbersFromRange(l, 3);
                        assertTrue(response[0] < response[1] && response[1] < response[2]);
                }
        }

        @Test
        public void testGenerateNumbersHasNoDuplicates() {
                LinkedList l = new LinkedList(1, 9);
                int[] response = TicketService.getInstance().generateNumbersFromRange(l, 3);
                assertTrue(response[0]!=response[1]&&response[1]!=response[2]);
        }

        @Test
        public void testGenerateNumbersAreGreaterThanPreviousSize2() {
                LinkedList l = new LinkedList(1, 9);
                int[] response = TicketService.getInstance().generateNumbersFromRange(l, 2);
                assertTrue(response[0] < response[1]||response[1]<response[2]);
        }

        @Test public void testDeleteNodeHead()
        {
                LinkedList l = new LinkedList(1, 9);
                LinkedListNode node = new LinkedListNode(0);
                node.changeNext(l.getHead());
                l.deleteNode(node);
                assertTrue(node!=l.getHead());
        }

        @Test public void testDeleteNode()
        {
                LinkedList l = new LinkedList(1, 9);
                LinkedListNode node = l.getHead().getNext();
                l.deleteNode(l.getHead().getNext());
                assertTrue(node!=l.getHead());
        }

        @Test public void testDeleteNodeSizeDecreases()
        {
                LinkedList l = new LinkedList(1, 9);
                int size = l.getSize();
                l.deleteNode(l.getHead().getNext());
                assertTrue(l.getSize()==(size-1));
        }

        @Test
        public void testGenerateColumnsWithLengthOf9()
        {
                int[] k = { 1, 2, 1, 1, 1, 3 };
                int[][] ticket = new int[][] { {}, {}, {}, {}, {}, {} };
                LinkedList list = new LinkedList(1, 9);
                for (int f = 0; f < k.length; f++) {
                        int[] tmp = TicketService.getInstance().generateNumbersFromRange(list, k[f]);
                        //generate blanks
                        ticket[f] = tmp;
                }
        }

        @Test
        public void testGenerateColumnsWithLengthOf10()
        {
                int [] k = {1, 2, 2, 2, 1, 2};
                int[][] ticket = new int[][]{{}, {}, {}, {}, {}, {}};
                LinkedList list = new LinkedList(10, 19);
                for(int i = 0; i<k.length; i++)
                {
                        int[] tmp = TicketService.getInstance().generateNumbersFromRange(list, k[i]);
                        //generate blanks
                        ticket[i] = tmp;
                }
        }

        @Test
        public void testGenerateColumnsWithLengthOf11()
        {
                int [] k = {1, 2, 2, 2, 1, 2};
                int[][] ticket = new int[][]{{}, {}, {}, {}, {}, {}};
                LinkedList list = new LinkedList(80, 90);
                for(int i = 0; i<k.length; i++)
                {
                        int[] tmp = TicketService.getInstance().generateNumbersFromRange(list, k[i]);
                        //generate blanks
                        ticket[i] = tmp;
                }
        }

        @Test
        public void testCreationOfFullStrip() {
                List<String> allTickets = new ArrayList<>();
                List<int []> tickets = TicketService.getInstance().generateTicket();
                for (int l = 0; l < 6; l++) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < 9; i++) {
                                sb.append(Arrays.toString(tickets.get(l + (i * 6))));
                        }
                        allTickets.add(sb.toString());
                }
                assertTrue(allTickets.size()==6);

                try {
                        FileService.getInstance().saveTextToFile(allTickets);
                } catch (IOException e) {
                        //Should not throw exception
                        //there may be a problem writing to a file
                        System.out.println("ERROR - Error creating file");
                        assertFalse(false);
                }
        }

        @Test
        public void testCreationOf10000FullStrips() {
                List<String> allTickets = new ArrayList<>();
                for (int stripCounter = 0; stripCounter < 10000; stripCounter++) {
                        List<int[]> tickets = TicketService.getInstance().generateTicket();
                        for (int l = 0; l < 6; l++) {
                                StringBuilder sb = new StringBuilder();
                                for (int i = 0; i < 9; i++) {
                                        sb.append(Arrays.toString(tickets.get(l + (i * 6))));
                                }
                                allTickets.add(sb.toString());
                        }
                        // assertTrue(allTickets.size() == 6);

                }
                try {
                        FileService.getInstance().saveTextToFile(allTickets);
                } catch (IOException e) {
                        //Should not throw exception
                        //there may be a problem writing to a file
                        System.out.println("ERROR - Error creating file");
                        assertFalse(false);
                }

        }

}
