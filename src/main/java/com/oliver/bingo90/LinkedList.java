package com.oliver.bingo90;

import sun.awt.image.ImageWatched;

public class LinkedList {
        private int size = 1;
        private LinkedListNode head;

        public LinkedList(int head)
        {
                this.head = new LinkedListNode(head);
        }

        public LinkedList(int startNum, int lastNum)
        {
                head = new LinkedListNode(startNum);
                LinkedListNode pointer = head;
                for(int i = startNum+1; i<=lastNum; i++)
                {
                        pointer.addNext(i);
                        pointer = pointer.getNext();
                        this.size++;
                }
        }

        public int popItem(int value) {
                LinkedListNode slowPointer = new LinkedListNode(0);
                slowPointer.changeNext(this.getHead());
                LinkedListNode fastPointer = this.getHead();

                for (int i = 1; i < value; i++) {
                        slowPointer = fastPointer;
                        fastPointer = fastPointer.getNext();
                }
                //      change to use internal linkedlist library eventually
                this.deleteNode(slowPointer);
                return fastPointer.getValue();
        }

        public int getSize()
        {
                return this.size;
        }

        public LinkedListNode getHead()
        {
                return this.head;
        }

        public void deleteNode(LinkedListNode nMniusOne)
        {
                this.size--;
                //TODO add null check here
                if(nMniusOne.getNext()==this.head)
                {
                        this.head = this.head.getNext();
                        return;
                }
                nMniusOne.changeNext(nMniusOne.getNext().getNext());
        }
}
