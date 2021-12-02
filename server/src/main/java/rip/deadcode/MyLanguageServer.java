package rip.deadcode;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public final class MyLanguageServer implements LanguageServer, LanguageClientAware {

    private static final Logger logger = LoggerFactory.getLogger( MyLanguageServer.class );

    private final Runnable shutdownHandler;
    private LanguageClient client;

    public MyLanguageServer( Runnable shutdownHandler ) {
        this.shutdownHandler = shutdownHandler;
    }

    @Override
    public CompletableFuture<InitializeResult> initialize( InitializeParams params ) {
        var capabilities = new ServerCapabilities();
        capabilities.setTextDocumentSync( TextDocumentSyncKind.Full );

        var completionOptions = new CompletionOptions();
        completionOptions.setResolveProvider( true );
        capabilities.setCompletionProvider( completionOptions );

        var result = new InitializeResult( capabilities );
        return CompletableFuture.completedFuture( result );
    }

    @Override
    public void initialized( InitializedParams params ) {
        client.logMessage( new MessageParams( MessageType.Info, "hello, world" ) );
    }

    @Override
    public CompletableFuture<Object> shutdown() {
        logger.info( "shutdown received" );
        shutdownHandler.run();
        return CompletableFuture.completedFuture( null );
    }

    @Override
    public void exit() {
        logger.info( "exit received" );
        shutdownHandler.run();
    }

    @Override
    public TextDocumentService getTextDocumentService() {
        return new MyTextDocumentService();
    }

    @Override
    public WorkspaceService getWorkspaceService() {
        return null;
    }

    @Override
    public void connect( LanguageClient client ) {
        this.client = client;
    }
}
