package cn.sexycode.myjpa.sql.jdbc;

import cn.sexycode.myjpa.sql.MysqlException;
import cn.sexycode.myjpa.sql.jdbc.ConnectionProvider;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A {@link org.hibernate.engine.jdbc.connections.spi.ConnectionProvider} that manages connections from an underlying {@link DataSource}.
 * <p/>
 * The {@link DataSource} to use may be specified by either:<ul>
 * <li>injection via {@link #setDataSource}</li>
 * <li>decaring the {@link DataSource} instance using the {@link Environment#DATASOURCE} config property</li>
 * <li>decaring the JNDI name under which the {@link DataSource} can be found via {@link Environment#DATASOURCE} config property</li>
 * </ul>
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class DatasourceConnectionProviderImpl implements ConnectionProvider {
    private DataSource dataSource;

    private String user;

    private String pass;

    private boolean useCredentials;
    //	private JndiService jndiService;

    private boolean available;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

/*
	@InjectService( required = false )
	@SuppressWarnings("UnusedDeclaration")
	public void setJndiService(JndiService jndiService) {
		this.jndiService = jndiService;
	}
*/
/*
	@Override
	public boolean isUnwrappableAs(Class unwrapType) {
		return ConnectionProvider.class.equals( unwrapType ) ||
				DatasourceConnectionProviderImpl.class.isAssignableFrom( unwrapType ) ||
				DataSource.class.isAssignableFrom( unwrapType );
	}

	@Override
	@SuppressWarnings( {"unchecked"})
	public <T> T unwrap(Class<T> unwrapType) {
		if ( ConnectionProvider.class.equals( unwrapType ) ||
				DatasourceConnectionProviderImpl.class.isAssignableFrom( unwrapType ) ) {
			return (T) this;
		}
		else if ( DataSource.class.isAssignableFrom( unwrapType ) ) {
			return (T) getDataSource();
		}
		else {
			throw new UnknownUnwrapTypeException( unwrapType );
		}
	}

	@Override
	public void configure(Map configValues) {
		if ( this.dataSource == null ) {
			final Object dataSource = configValues.get( Environment.DATASOURCE );
			if ( DataSource.class.isInstance( dataSource ) ) {
				this.dataSource = (DataSource) dataSource;
			}
			else {
				final String dataSourceJndiName = (String) dataSource;
				if ( dataSourceJndiName == null ) {
					throw new HibernateException(
							"DataSource to use was not injected nor specified by [" + Environment.DATASOURCE
									+ "] configuration property"
					);
				}
				if ( jndiService == null ) {
					throw new HibernateException( "Unable to locate JndiService to lookup Datasource" );
				}
				this.dataSource = (DataSource) jndiService.locate( dataSourceJndiName );
			}
		}
		if ( this.dataSource == null ) {
			throw new HibernateException( "Unable to determine appropriate DataSource to use" );
		}

		user = (String) configValues.get( Environment.USER );
		pass = (String) configValues.get( Environment.PASS );
		useCredentials = user != null || pass != null;
		available = true;
	}

	@Override
	public void stop() {
		available = false;
		dataSource = null;
	}*/

    @Override
    public Connection getConnection() throws SQLException {
        if (!available) {
            throw new MysqlException("Provider is closed!");
        }
        return useCredentials ? dataSource.getConnection(user, pass) : dataSource.getConnection();
    }

    @Override
    public void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return true;
    }
}
