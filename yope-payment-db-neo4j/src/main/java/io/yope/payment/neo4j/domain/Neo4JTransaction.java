/**
 *
 */
package io.yope.payment.neo4j.domain;

import java.math.BigDecimal;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.yope.payment.domain.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author mgerardi
 *
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"type", "reference", "amount"}, includeFieldNames = false)
@RelationshipEntity(type="PAY")
public class Neo4JTransaction implements Transaction {

    @GraphId
    private Long id;

    @JsonProperty
    @Fetch
    @StartNode
    private Neo4JWallet source;

    @JsonProperty
    @Fetch
    @EndNode
    private Neo4JWallet destination;

    @Indexed
    private String transactionHash;

    @Indexed
    private String senderHash;

    @Indexed
    private String receiverHash;

    private Type type;

    private String reference;

    private Status status;

    private String description;

    private BigDecimal amount;

    private BigDecimal balance;

    private BigDecimal blockchainFees;

    private BigDecimal fees;

    private Long creationDate;

    private Long acceptedDate;

    private Long deniedDate;

    private Long failedDate;

    private Long expiredDate;

    private Long completedDate;

    private String QR;

    public static Neo4JTransaction.Neo4JTransactionBuilder from(final Transaction transaction) {
        return Neo4JTransaction.builder()
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .balance(transaction.getBalance())
                .blockchainFees(transaction.getBlockchainFees())
                .fees(transaction.getFees())
                .creationDate(transaction.getCreationDate())
                .description(transaction.getDescription())
                .destination(Neo4JWallet.from(transaction.getDestination()).build())
                .source(Neo4JWallet.from(transaction.getSource()).build())
                .status(transaction.getStatus())
                .reference(transaction.getReference())
                .id(transaction.getId())
                .QR(transaction.getQR())
                .transactionHash(transaction.getTransactionHash())
                .senderHash(transaction.getSenderHash())
                .receiverHash(transaction.getReceiverHash())
                .acceptedDate(transaction.getAcceptedDate())
                .failedDate(transaction.getFailedDate())
                .expiredDate(transaction.getExpiredDate())
                .deniedDate(transaction.getDeniedDate())
                .completedDate(transaction.getCompletedDate());
    }

}
