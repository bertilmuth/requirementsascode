package org.requirementsascode.queue;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;

/**
 * A simple event queue that forwards events to an event consumer, one at a
 * time.
 * 
 * To do that, it internally runs its own event producer thread.
 * 
 * @author b_muth
 *
 */
public class EventQueue {
	private final BlockingDeque<Object> events;
	private final EventProducer eventProducer;
	private final Thread eventProducerThread;
	private final Consumer<Object> eventConsumer;

	/**
	 * Create an event queue whose events will be consumed by the specified
	 * consumer.
	 * 
	 * @param eventConsumer the target of events
	 */
	public EventQueue(Consumer<Object> eventConsumer) {
		this.events = new LinkedBlockingDeque<Object>();
		this.eventProducer = new EventProducer();
		this.eventProducerThread = new Thread(eventProducer);
		this.eventConsumer = eventConsumer;
		eventProducerThread.start();
	}

	/**
	 * Puts an event in the queue, that will be provided to the consumer
	 * (if the event queue hasn't been stopped).
	 * 
	 * @param event the event for the queue
	 */
	public void put(Object event) {
		try {
			events.put(event);
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Stop providing events to the consumer.
	 * 
	 * Internally, that stops the producer thread.
	 */
	public void stop() {
		eventProducer.stopProviding();
		try {
			eventProducerThread.interrupt();
			eventProducerThread.join();
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Returns whether this queue is empty.
	 * 
	 * @return true if empty, false if not
	 */
	public boolean isEmpty() {
		return events.isEmpty();
	}

	public int getSize() {
		return events.size();
	}

	private class EventProducer implements Runnable {
		private boolean isRunning = true;

		@Override
		public void run() {
			while (isRunning) {
				final Object eventObject = take();
				if (eventObject != null) {
					consume(eventObject);
				}
			}
		}

		private void consume(Object event) {
			eventConsumer.accept(event);
		}

		private Object take() {
			Object event = null;
			try {
				event = events.take();
			} catch (InterruptedException e) {
				stopProviding();
			}
			return event;
		}

		private void stopProviding() {
			isRunning = false;
		}
	}
}
