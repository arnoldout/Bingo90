package com.oliver.bingo90.list;

public class LinkedListNode {
        private LinkedListNode next;
        private int value;

        public LinkedListNode(int value)
        {
                this.value = value;
        }

        public int getValue()
        {
                return this.value;
        }

        public void addNext(int value)
        {
                next = new LinkedListNode(value);
        }

        public void changeNext(LinkedListNode newNext)
        {
                this.next = newNext;
        }

        public LinkedListNode getNext()
        {
                return next;
        }

}
