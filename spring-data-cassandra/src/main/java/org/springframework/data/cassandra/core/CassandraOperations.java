/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.cassandra.core;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.cassandra.core.CqlOperations;
import org.springframework.cassandra.core.QueryOptions;
import org.springframework.cassandra.core.WriteOptions;
import org.springframework.cassandra.core.cql.CqlIdentifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.cassandra.convert.CassandraConverter;

import com.datastax.driver.core.Statement;

/**
 * Interface specifying a basic set of Cassandra operations. Implemented by {@link CassandraTemplate}. Not often used
 * directly, but a useful option to enhance testability, as it can easily be mocked or stubbed.
 *
 * @author Alex Shvid
 * @author David Webb
 * @author Matthew Adams
 * @author Mark Paluch
 * @see CassandraTemplate
 * @see CqlOperations
 * @see Statement
 */
public interface CassandraOperations {

	/**
	 * Returns a new {@link CassandraBatchOperations}. Each {@link CassandraBatchOperations} instance can be executed only
	 * once so you might want to obtain new {@link CassandraBatchOperations} instances for each batch.
	 *
	 * @return a new {@link CassandraBatchOperations} associated with the given entity class.
	 */
	CassandraBatchOperations batchOps();

	/**
	 * Returns the underlying {@link CassandraConverter}.
	 *
	 * @return the underlying {@link CassandraConverter}.
	 */
	CassandraConverter getConverter();

	/**
	 * Expose the underlying {@link CqlOperations} to allow CQL operations.
	 *
	 * @return the underlying {@link CqlOperations}.
	 * @see CqlOperations
	 */
	CqlOperations getCqlOperations();

	/**
	 * The table name used for the specified class by this template.
	 *
	 * @param entityClass The entity type must not be {@literal null}.
	 * @return the {@link CqlIdentifier}
	 */
	CqlIdentifier getTableName(Class<?> entityClass);

	// -------------------------------------------------------------------------
	// Methods dealing with static CQL
	// -------------------------------------------------------------------------

	/**
	 * Execute a {@code SELECT} query and convert the resulting items to a {@link List} of entities.
	 *
	 * @param cql must not be {@literal null}.
	 * @param entityClass The entity type must not be {@literal null}.
	 * @return the converted results
	 * @throws DataAccessException if there is any problem executing the query.
	 */
	<T> List<T> select(String cql, Class<T> entityClass) throws DataAccessException;

	/**
	 * Execute a {@code SELECT} query and convert the resulting items to a {@link Iterator} of entities.
	 * <p>
	 * Returns a {@link Iterator} that wraps the Cassandra {@link com.datastax.driver.core.ResultSet}.
	 *
	 * @param <T> element return type.
	 * @param cql query to execute. Must not be empty or {@literal null}.
	 * @param entityClass Class type of the elements in the {@link Iterator} stream. Must not be {@literal null}.
	 * @return an {@link Iterator} (stream) over the elements in the query result set.
	 * @throws DataAccessException if there is any problem executing the query.
	 * @since 1.5
	 */
	<T> Stream<T> stream(String cql, Class<T> entityClass) throws DataAccessException;

	/**
	 * Execute a {@code SELECT} query and convert the resulting item to an entity.
	 *
	 * @param cql must not be {@literal null}.
	 * @param entityClass The entity type must not be {@literal null}.
	 * @return the converted object or {@literal null}.
	 * @throws DataAccessException if there is any problem executing the query.
	 */
	<T> T selectOne(String cql, Class<T> entityClass) throws DataAccessException;

	// -------------------------------------------------------------------------
	// Methods dealing with com.datastax.driver.core.Statement
	// -------------------------------------------------------------------------

	/**
	 * Execute a {@code SELECT} query and convert the resulting items to a {@link List} of entities.
	 *
	 * @param statement must not be {@literal null}.
	 * @param entityClass The entity type must not be {@literal null}.
	 * @return the converted results
	 * @throws DataAccessException if there is any problem executing the query.
	 */
	<T> List<T> select(Statement statement, Class<T> entityClass) throws DataAccessException;

	/**
	 * Execute a {@code SELECT} query and convert the resulting items to a {@link Iterator} of entities.
	 * <p>
	 * Returns a {@link Iterator} that wraps the Cassandra {@link com.datastax.driver.core.ResultSet}.
	 *
	 * @param <T> element return type.
	 * @param statement query to execute. Must not be empty or {@literal null}.
	 * @param entityClass Class type of the elements in the {@link Iterator} stream. Must not be {@literal null}.
	 * @return an {@link Iterator} (stream) over the elements in the query result set.
	 * @throws DataAccessException if there is any problem executing the query.
	 * @since 1.5
	 */
	<T> Stream<T> stream(Statement statement, Class<T> entityClass) throws DataAccessException;

	/**
	 * Execute a {@code SELECT} query and convert the resulting item to an entity.
	 *
	 * @param statement must not be {@literal null}.
	 * @param entityClass The entity type must not be {@literal null}.
	 * @return the converted object or {@literal null}.
	 * @throws DataAccessException if there is any problem executing the query.
	 */
	<T> T selectOne(Statement statement, Class<T> entityClass) throws DataAccessException;

	// -------------------------------------------------------------------------
	// Methods dealing with entities
	// -------------------------------------------------------------------------

	/**
	 * Returns the number of rows for the given entity class.
	 *
	 * @param entityClass must not be {@literal null}.
	 * @return the number of existing entities.
	 * @throws DataAccessException if there is any problem executing the query.
	 */
	long count(Class<?> entityClass) throws DataAccessException;

	/**
	 * Determine whether the row {@code entityClass} with the given {@code id} exists.
	 *
	 * @param id must not be {@literal null}.
	 * @param entityClass The entity type must not be {@literal null}.
	 * @return true, if the object exists.
	 * @throws DataAccessException if there is any problem executing the query.
	 */
	boolean exists(Object id, Class<?> entityClass) throws DataAccessException;

	/**
	 * Execute the Select by {@code id} for the given {@code entityClass}.
	 *
	 * @param id must not be {@literal null}.
	 * @param entityClass The entity type must not be {@literal null}.
	 * @return the converted object or {@literal null}.
	 * @throws DataAccessException if there is any problem executing the query.
	 */
	<T> T selectOneById(Object id, Class<T> entityClass) throws DataAccessException;

	/**
	 * Select objects for the given {@code entityClass} and {@code ids}.
	 *
	 * @param ids must not be {@literal null}.
	 * @param entityClass The entity type must not be {@literal null}.
	 * @return the converted results
	 * @throws DataAccessException if there is any problem executing the query.
	 */
	<T> List<T> selectBySimpleIds(Iterable<?> ids, Class<T> entityClass) throws DataAccessException;

	/**
	 * Insert the given entity and return the entity if the insert was applied.
	 *
	 * @param entity The entity to insert, must not be {@literal null}.
	 * @return the inserted entity.
	 * @throws DataAccessException if there is any problem executing the query.
	 */
	<T> T insert(T entity) throws DataAccessException;

	/**
	 * Insert the given entity applying {@link WriteOptions} and return the entity if the insert was applied.
	 *
	 * @param entity The entity to insert, must not be {@literal null}.
	 * @param options may be {@literal null}.
	 * @return the inserted entity.
	 * @throws DataAccessException if there is any problem executing the query.
	 */
	<T> T insert(T entity, WriteOptions options) throws DataAccessException;

	/**
	 * Update the given entity and return the entity if the update was applied.
	 *
	 * @param entity The entity to update, must not be {@literal null}.
	 * @return the updated entity.
	 * @throws DataAccessException if there is any problem executing the query.
	 */
	<T> T update(T entity) throws DataAccessException;

	/**
	 * Update the given entity applying {@link WriteOptions} and return the entity if the update was applied.
	 *
	 * @param entity The entity to update, must not be {@literal null}.
	 * @param options may be {@literal null}.
	 * @return the updated entity.
	 * @throws DataAccessException if there is any problem executing the query.
	 */
	<T> T update(T entity, WriteOptions options) throws DataAccessException;

	/**
	 * Delete the given entity and return the entity if the delete was applied.
	 *
	 * @param entity must not be {@literal null}.
	 * @return the deleted entity.
	 * @throws DataAccessException if there is any problem executing the query.
	 */
	<T> T delete(T entity) throws DataAccessException;

	/**
	 * Delete the given entity applying {@link QueryOptions} and return the entity if the delete was applied.
	 *
	 * @param entity must not be {@literal null}.
	 * @param options may be {@literal null}.
	 * @return the deleted entity.
	 * @throws DataAccessException if there is any problem executing the query.
	 */
	<T> T delete(T entity, QueryOptions options) throws DataAccessException;

	/**
	 * Remove the given object from the table by id.
	 *
	 * @param id must not be {@literal null}.
	 * @param entityClass The entity type must not be {@literal null}.
	 * @throws DataAccessException if there is any problem executing the query.
	 */
	boolean deleteById(Object id, Class<?> entityClass) throws DataAccessException;

	/**
	 * Execute a {@code TRUNCATE} query to remove all entities of a given class.
	 *
	 * @param entityClass The entity type must not be {@literal null}.
	 * @throws DataAccessException if there is any problem executing the query.
	 */
	void truncate(Class<?> entityClass) throws DataAccessException;

}
