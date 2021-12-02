package rip.deadcode;

import org.eclipse.lsp4j.launch.LSPLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public final class Application {

    private static final Logger logger = LoggerFactory.getLogger( Application.class );

    public static void main( String[] args ) throws Exception {

        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        logger.info( "Starting server" );

//        var serverSocket = new ServerSocket( 8080 );
//        var socket = serverSocket.accept();
//        var server = new MyLanguageServer( () -> {
//            try {
//                socket.shutdownInput();
//            } catch ( IOException e ) {
//                throw new UncheckedIOException( e );
//            }
//        } );
//        var launcher = LSPLauncher.createServerLauncher( server, socket.getInputStream(), socket.getOutputStream() );

        var server = new MyLanguageServer( () -> {} );
        var launcher = LSPLauncher.createServerLauncher( server, System.in, System.out );

        var client = launcher.getRemoteProxy();

        server.connect( client );
        launcher.startListening();
    }
}
