package org.jordon.os.queue;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class LinkedBlockingQueue implements BlockingQueue {
    /**
     * 单向队列的元素数据结构
     */
    private static class Node<E> {
        E item;

        /**
         * One of:
         * - the real successor IndexNode
         * - this IndexNode, meaning the successor is head.next
         * - null, meaning there is no successor (this is the last node)
         */
        Node<E> next;

        Node(E x) {
            item = x;
        }
    }

    @Override
    public void addFirst(Object o) {

    }

    @Override
    public void addLast(Object o) {

    }

    @Override
    public boolean offerFirst(Object o) {
        return false;
    }

    @Override
    public boolean offerLast(Object o) {
        return false;
    }

    @Override
    public void putFirst(Object o) throws InterruptedException {

    }

    @Override
    public void putLast(Object o) throws InterruptedException {

    }

    @Override
    public boolean offerFirst(Object o, long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public boolean offerLast(Object o, long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public Object takeFirst() throws InterruptedException {
        return null;
    }

    @Override
    public Object takeLast() throws InterruptedException {
        return null;
    }

    @Override
    public Object pollFirst(long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public Object pollLast(long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean add(Object o) {
        return false;
    }

    @Override
    public boolean offer(Object o) {
        return false;
    }

    @Override
    public void put(Object o) throws InterruptedException {

    }

    @Override
    public boolean offer(Object o, long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public Object remove() {
        return null;
    }

    @Override
    public Object poll() {
        return null;
    }

    @Override
    public Object take() throws InterruptedException {
        return null;
    }

    @Override
    public Object poll(long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public Object element() {
        return null;
    }

    @Override
    public Object peek() {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public void push(Object o) {

    }
}
