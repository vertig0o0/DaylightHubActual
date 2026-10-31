package dev.daylight.hub;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/** Pure Java because the standalone test provider process has no target Kotlin classpath. */
public final class BookFixtureProvider extends ContentProvider {
    @Override public boolean onCreate(){return true;}
    private File fixture(String name) {
        if(name==null || !name.matches("[A-Za-z0-9._-]{1,120}"))throw new IllegalArgumentException("Invalid fixture name");
        File directory=new File(getContext().getCacheDir(),"book-fixtures");directory.mkdirs();return new File(directory,name);
    }
    @Override public Bundle call(String method,String name,Bundle extras) {
        File source=fixture(name);
        try {
            switch(method) {
                case "create":try(FileOutputStream output=new FileOutputStream(source)){output.write(extras.getByteArray("bytes"));}break;
                case "generate": {
                    int remaining=extras.getInt("bytes");if(remaining<1 || remaining>32*1024*1024)throw new IllegalArgumentException("Fixture size");
                    byte[] block="Reading fixture text.\n".getBytes(StandardCharsets.UTF_8);
                    try(FileOutputStream output=new FileOutputStream(source)){while(remaining>0){int count=Math.min(remaining,block.length);output.write(block,0,count);remaining-=count;}}break;
                }
                case "delete":source.delete();break;
                default:throw new IllegalArgumentException("Unsupported fixture operation");
            }
        }catch(IOException error){throw new IllegalStateException(error);}
        Bundle result=new Bundle();result.putString("uri","content://dev.daylight.hub.test.books/"+name);return result;
    }
    @Override public ParcelFileDescriptor openFile(Uri uri,String mode) throws java.io.FileNotFoundException {
        if(!"r".equals(mode))throw new IllegalArgumentException("Read only");return ParcelFileDescriptor.open(fixture(uri.getLastPathSegment()),ParcelFileDescriptor.MODE_READ_ONLY);
    }
    @Override public Cursor query(Uri uri,String[] projection,String selection,String[] args,String order) {
        File source=fixture(uri.getLastPathSegment());String[] columns=projection==null?new String[]{OpenableColumns.DISPLAY_NAME,OpenableColumns.SIZE}:projection;
        Object[] values=new Object[columns.length];for(int index=0;index<columns.length;index++){
            if(OpenableColumns.DISPLAY_NAME.equals(columns[index]))values[index]=source.getName();
            if(OpenableColumns.SIZE.equals(columns[index]))values[index]=source.length();
        }
        MatrixCursor result=new MatrixCursor(columns);result.addRow(values);return result;
    }
    @Override public String getType(Uri uri) {
        String name=uri.getLastPathSegment();if(name.endsWith(".txt"))return "text/plain";if(name.endsWith(".html"))return "text/html";
        if(name.endsWith(".pdf"))return "application/pdf";if(name.endsWith(".epub"))return "application/epub+zip";return "application/octet-stream";
    }
    @Override public Uri insert(Uri uri,ContentValues values){throw new UnsupportedOperationException();}
    @Override public int update(Uri uri,ContentValues values,String where,String[] args){return 0;}
    @Override public int delete(Uri uri,String where,String[] args){return 0;}
}
