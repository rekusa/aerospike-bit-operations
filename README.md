# aerospike-bit-operations
Examples of Bit operations for the Aerospike NoSQL DBMS

Aerospike is a high throughput, low latency NoSQL distributed DBMS which supports 24x365 availability. One area where its functionality was previously limited was in the efficiency of tracking activities via bit manipulation.

Why would you wish track activities via bit manipulation? The most obvious, common use of this functionality would be to track unique daily active users for each page/site. Each user is assigned a unique id within a range of values. For each user that visits the page/site, you would want to set the appropriate bit in the array.

Of course, you were always able to read a record that included a “bin” (attribute) of the “bytes” data type, manipulate it and then update the record. But the Aerospike bytes data type did not have atomic (server-side) operations.

The downside of that approach was the need to bring the record down to the client upon read followed by transmission to the server for the write operation. With Aerospike supporting record sizes of up to 8MB (which can be compressed on the storage device using the Aerospike Enterprise Edition compression option), toggling a single bit was an expensive round-trip network operation. With the release of version 4.6 in August, Aerospike added the ability to execute bitwise operations on “bytes” data type attributes on the server. Multiple operations may be performed on a record within a single transaction performed wholly on the server. Recall that Aerospike is one of the very few NoSQL distributed DBMS’ that can be utilized for a System of Record as demonstrated by passing the extensive Jepsen tests of the Strong Consistency mode of operation.

The Java code provide snippets of a test program example that demonstrate the steps required to execute two steps within the same transaction; namely, set a bit (for a unique user) and retrieve the count of the number of bits (unique users) that are set for that bytes bin (attribute) in total.
