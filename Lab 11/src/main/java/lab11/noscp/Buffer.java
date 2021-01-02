package lab11.noscp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Semaphore;

public class Buffer {
    private final ArrayList<Integer> products;
    private final Semaphore freePlacesInBuffer;
    private final Semaphore productsInBuffer;
    private int firstFreePlace;
    private int firstStoredProduct;

    public Buffer(final int numberOfProducts) {
        this.products = new ArrayList<>(Collections.nCopies(numberOfProducts, -1));
        this.freePlacesInBuffer = new Semaphore(numberOfProducts);
        this.productsInBuffer = new Semaphore(0);
    }

    /**
     * put method checks if there are any free places in buffer using freePlacesInBuffer semaphore
     * if so it puts a product in appropriate place and signal productsInBuffer semaphore that
     * there are products in buffer
     *
     * @param product product to be put
     */
    public void put(int product) {
        try {
            freePlacesInBuffer.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        products.set(firstFreePlace, product);
        firstFreePlace = (firstFreePlace + 1) % products.size();
        productsInBuffer.release();
    }

    /**
     * get method checks if there are any products in buffer using productsInBuffer semaphore
     * if so it consumes a product from appropriate place and signal freePlacesInBuffer semaphore that
     * there is a free place in buffer
     */
    public Integer get() {
        try {
            productsInBuffer.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        Integer product = products.get(firstStoredProduct);
        firstStoredProduct = (firstStoredProduct + 1) % products.size();
        freePlacesInBuffer.release();

        return product;
    }
}
