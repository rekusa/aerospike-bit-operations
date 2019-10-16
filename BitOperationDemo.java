/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BitOperationDemo;

import java.util.*;
import java.util.Random;
import com.aerospike.client.*;
import com.aerospike.client.policy.*;
import com.aerospike.client.operation.*;

/**
 *
 * @author Reuven
 */
public class BitOperationDemo {

    public static final String DB_IP = "192.168.1.25";
    public static final String NAMESPACE = "ycsb";
    public static final String SET = "events";
    public static final String EVENTS_BIN_NAME = "eventMap";
    public static final String CAMPAIGNS_BIN_NAME = "campaignsMap";
    public static final Integer BYTES_PER_RECORD = 1000;
    public static final Integer NUM_RECORDS = 1000;

    private static AerospikeClient asClient = new AerospikeClient(DB_IP, 3000);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Instantiate WritePolicy
        WritePolicy policy = new WritePolicy();
        BitPolicy bpolicy = new BitPolicy();
        byte[] bytes = new byte[BYTES_PER_RECORD];
        Bin bin = new Bin(EVENTS_BIN_NAME, bytes);
        // Initiate DB with 1K records, each with 100K of 0 bytes
        for (int i = 0; i < NUM_RECORDS; i++) {
            Key key = new Key(NAMESPACE, SET, i);
            asClient.put(policy, key, bin);
        }
        Random rand = new Random();
        int theKey, theBit;
        for (int i = 0; i < NUM_RECORDS * 10; i++) { 
            // Pick a Random Key and a Random bit to set for that key
            theKey = rand.nextInt(NUM_RECORDS);
            Key key = new Key(NAMESPACE, SET, theKey);
            theBit = rand.nextInt(BYTES_PER_RECORD * 8);
            Record bitResults = asClient.operate(policy, key,
                    //BitOperation.set(BitPolicy.Default, EVENTS_BIN_NAME, theBit, 1, new byte[]{(byte) 0b10000000}),
                    BitOperation.set(BitPolicy.Default, EVENTS_BIN_NAME, theBit, 1, new byte[]{(byte) 0b11111111}),
                    BitOperation.count(EVENTS_BIN_NAME, 0, BYTES_PER_RECORD * 8)
            );
            List<?> list = bitResults.getList(EVENTS_BIN_NAME);
            Long theCount = (Long) list.get(1);
            System.out.println("key: " + theKey + ", the bit: " + theBit + ", the result: " + theCount);
        }
    }
}
