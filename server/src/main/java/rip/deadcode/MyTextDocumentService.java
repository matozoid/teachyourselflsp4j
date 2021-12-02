package rip.deadcode;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.CompletableFutures;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.stream.Collectors.toList;

public final class MyTextDocumentService implements TextDocumentService {

    private static final Logger logger = LoggerFactory.getLogger( MyTextDocumentService.class );
    private static final Splitter splitter = Splitter.on( "," );

    private final AtomicReference<List<List<String>>> src = new AtomicReference<>( ImmutableList.of() );

    private void updateDocument( String srcUriString ) {
        try {
            var uri = new URI( srcUriString );
            src.set( Files.lines( Paths.get( uri ) )
                          .map( splitter::splitToList )
                          .collect( toList() ) );
        } catch ( IOException | URISyntaxException e ) {
            logger.info( "", e );
        }
    }

    @Override
    public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion( CompletionParams position ) {

        return CompletableFutures.computeAsync( checker -> {
            var src = this.src.get();
            var currentLineIndex = position.getPosition().getLine();
            // The new line at the end of the file is not in the list.
            // ファイルの最後の新しい行はリストにいない
            if ( src.size() <= currentLineIndex ) {
                return Either.forLeft( List.of() );
            }
            var currentRow = src.get( currentLineIndex );
            var currentRowString = String.join(",", currentRow);
            var currentRowBeforeCursor = currentRowString
                    // Due to synchronization timing issues, the source may not have been updated.
                    // 同期タイミングの問題で、ソースが更新されていない場合もある
                    .substring( 0, Math.min( currentRowString.length(), position.getPosition().getCharacter() ) );
            var currentColumn = (int) currentRowBeforeCursor
                    .chars()
                    .filter( c -> c == ',' )
                    .count();
            var wordsInSameColumn = src.stream()
                                       .filter( l -> l.size() > currentColumn )
                                       .map( l -> l.get( currentColumn ) )
                                       .filter( s -> !s.isEmpty() )
                                       .distinct()
                                       .collect( toList() );
            logger.debug( "{}", wordsInSameColumn );
            var response = wordsInSameColumn.stream()
                                            .map( CompletionItem::new )
                                            .collect( toList() );

            return Either.forLeft( response );
        } );
    }

    @Override
    public CompletableFuture<CompletionItem> resolveCompletionItem( CompletionItem unresolved ) {
        return CompletableFuture.completedFuture( null );
    }

    @Override
    public CompletableFuture<Hover> hover(HoverParams params) {
        return CompletableFuture.completedFuture( null );
    }

    @Override
    public CompletableFuture<SignatureHelp> signatureHelp(SignatureHelpParams params) {
        return CompletableFuture.completedFuture( null );
    }

    @Override
    public CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> definition(DefinitionParams params) {
        return CompletableFuture.completedFuture( null );
    }

    @Override
    public CompletableFuture<List<? extends Location>> references( ReferenceParams params ) {
        return CompletableFuture.completedFuture( null );
    }

    @Override
    public CompletableFuture<List<? extends DocumentHighlight>> documentHighlight(DocumentHighlightParams params) {
        return CompletableFuture.completedFuture( null );
    }

    @Override
    public CompletableFuture<List<Either<Command, CodeAction>>> codeAction(CodeActionParams params ) {
        return CompletableFuture.completedFuture( null );
    }

    @Override
    public CompletableFuture<List<? extends CodeLens>> codeLens( CodeLensParams params ) {
        return CompletableFuture.completedFuture( null );
    }

    @Override
    public CompletableFuture<CodeLens> resolveCodeLens( CodeLens unresolved ) {
        return CompletableFuture.completedFuture( null );
    }

    @Override
    public CompletableFuture<List<? extends TextEdit>> formatting( DocumentFormattingParams params ) {
        return CompletableFuture.completedFuture( null );
    }

    @Override
    public CompletableFuture<List<? extends TextEdit>> rangeFormatting( DocumentRangeFormattingParams params ) {
        return CompletableFuture.completedFuture( null );
    }

    @Override
    public CompletableFuture<List<? extends TextEdit>> onTypeFormatting( DocumentOnTypeFormattingParams params ) {
        return CompletableFuture.completedFuture( null );
    }

    @Override
    public CompletableFuture<WorkspaceEdit> rename( RenameParams params ) {
        return CompletableFuture.completedFuture( null );
    }

    @Override
    public void didOpen( DidOpenTextDocumentParams params ) {
        updateDocument( params.getTextDocument().getUri() );
    }

    @Override
    public void didChange( DidChangeTextDocumentParams params ) {
        updateDocument( params.getTextDocument().getUri() );
    }

    @Override
    public void didClose( DidCloseTextDocumentParams params ) {
    }

    @Override
    public void didSave( DidSaveTextDocumentParams params ) {
    }
}
