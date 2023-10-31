package okhttp3.internal;

import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Flushable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import okhttp3.internal.io.FileSystem;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import okio.Source;
import okio.Timeout;

public final class DiskLruCache implements Closeable, Flushable {
  static final boolean $assertionsDisabled = false;
  
  static final long ANY_SEQUENCE_NUMBER = -1L;
  
  private static final String CLEAN = "CLEAN";
  
  private static final String DIRTY = "DIRTY";
  
  static final String JOURNAL_FILE = "journal";
  
  static final String JOURNAL_FILE_BACKUP = "journal.bkp";
  
  static final String JOURNAL_FILE_TEMP = "journal.tmp";
  
  static final Pattern LEGAL_KEY_PATTERN = Pattern.compile("[a-z0-9_-]{1,120}");
  
  static final String MAGIC = "libcore.io.DiskLruCache";
  
  private static final Sink NULL_SINK = new Sink() {
      public void close() throws IOException {}
      
      public void flush() throws IOException {}
      
      public Timeout timeout() {
        return Timeout.NONE;
      }
      
      public void write(Buffer param1Buffer, long param1Long) throws IOException {
        param1Buffer.skip(param1Long);
      }
    };
  
  private static final String READ = "READ";
  
  private static final String REMOVE = "REMOVE";
  
  static final String VERSION_1 = "1";
  
  private final int appVersion;
  
  private final Runnable cleanupRunnable = new Runnable() {
      final DiskLruCache this$0;
      
      public void run() {
        synchronized (DiskLruCache.this) {
          boolean bool;
          if (!DiskLruCache.this.initialized) {
            bool = true;
          } else {
            bool = false;
          } 
          if (bool | DiskLruCache.this.closed)
            return; 
          try {
            DiskLruCache.this.trimToSize();
          } catch (IOException iOException) {
            DiskLruCache.access$302(DiskLruCache.this, true);
          } 
          try {
            if (DiskLruCache.this.journalRebuildRequired()) {
              DiskLruCache.this.rebuildJournal();
              DiskLruCache.access$602(DiskLruCache.this, 0);
            } 
            return;
          } catch (IOException iOException) {
            RuntimeException runtimeException = new RuntimeException();
            this(iOException);
            throw runtimeException;
          } 
        } 
      }
    };
  
  private boolean closed;
  
  private final File directory;
  
  private final Executor executor;
  
  private final FileSystem fileSystem;
  
  private boolean hasJournalErrors;
  
  private boolean initialized;
  
  private final File journalFile;
  
  private final File journalFileBackup;
  
  private final File journalFileTmp;
  
  private BufferedSink journalWriter;
  
  private final LinkedHashMap<String, Entry> lruEntries = new LinkedHashMap<String, Entry>(0, 0.75F, true);
  
  private long maxSize;
  
  private boolean mostRecentTrimFailed;
  
  private long nextSequenceNumber = 0L;
  
  private int redundantOpCount;
  
  private long size = 0L;
  
  private final int valueCount;
  
  DiskLruCache(FileSystem paramFileSystem, File paramFile, int paramInt1, int paramInt2, long paramLong, Executor paramExecutor) {
    this.fileSystem = paramFileSystem;
    this.directory = paramFile;
    this.appVersion = paramInt1;
    this.journalFile = new File(paramFile, "journal");
    this.journalFileTmp = new File(paramFile, "journal.tmp");
    this.journalFileBackup = new File(paramFile, "journal.bkp");
    this.valueCount = paramInt2;
    this.maxSize = paramLong;
    this.executor = paramExecutor;
  }
  
  private void checkNotClosed() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual isClosed : ()Z
    //   6: istore_1
    //   7: iload_1
    //   8: ifne -> 14
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: new java/lang/IllegalStateException
    //   17: astore_2
    //   18: aload_2
    //   19: ldc 'cache is closed'
    //   21: invokespecial <init> : (Ljava/lang/String;)V
    //   24: aload_2
    //   25: athrow
    //   26: astore_2
    //   27: aload_0
    //   28: monitorexit
    //   29: aload_2
    //   30: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	26	finally
    //   14	26	26	finally
  }
  
  private void completeEdit(Editor paramEditor, boolean paramBoolean) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokestatic access$1800 : (Lokhttp3/internal/DiskLruCache$Editor;)Lokhttp3/internal/DiskLruCache$Entry;
    //   6: astore #10
    //   8: aload #10
    //   10: invokestatic access$1000 : (Lokhttp3/internal/DiskLruCache$Entry;)Lokhttp3/internal/DiskLruCache$Editor;
    //   13: aload_1
    //   14: if_acmpne -> 483
    //   17: iconst_0
    //   18: istore #5
    //   20: iload #5
    //   22: istore #4
    //   24: iload_2
    //   25: ifeq -> 139
    //   28: iload #5
    //   30: istore #4
    //   32: aload #10
    //   34: invokestatic access$900 : (Lokhttp3/internal/DiskLruCache$Entry;)Z
    //   37: ifne -> 139
    //   40: iconst_0
    //   41: istore_3
    //   42: iload #5
    //   44: istore #4
    //   46: iload_3
    //   47: aload_0
    //   48: getfield valueCount : I
    //   51: if_icmpge -> 139
    //   54: aload_1
    //   55: invokestatic access$1900 : (Lokhttp3/internal/DiskLruCache$Editor;)[Z
    //   58: iload_3
    //   59: baload
    //   60: ifeq -> 95
    //   63: aload_0
    //   64: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   67: aload #10
    //   69: invokestatic access$1500 : (Lokhttp3/internal/DiskLruCache$Entry;)[Ljava/io/File;
    //   72: iload_3
    //   73: aaload
    //   74: invokeinterface exists : (Ljava/io/File;)Z
    //   79: ifne -> 89
    //   82: aload_1
    //   83: invokevirtual abort : ()V
    //   86: aload_0
    //   87: monitorexit
    //   88: return
    //   89: iinc #3, 1
    //   92: goto -> 42
    //   95: aload_1
    //   96: invokevirtual abort : ()V
    //   99: new java/lang/IllegalStateException
    //   102: astore_1
    //   103: new java/lang/StringBuilder
    //   106: astore #10
    //   108: aload #10
    //   110: invokespecial <init> : ()V
    //   113: aload #10
    //   115: ldc 'Newly created entry didn't create value for index '
    //   117: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   120: pop
    //   121: aload #10
    //   123: iload_3
    //   124: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   127: pop
    //   128: aload_1
    //   129: aload #10
    //   131: invokevirtual toString : ()Ljava/lang/String;
    //   134: invokespecial <init> : (Ljava/lang/String;)V
    //   137: aload_1
    //   138: athrow
    //   139: iload #4
    //   141: aload_0
    //   142: getfield valueCount : I
    //   145: if_icmpge -> 262
    //   148: aload #10
    //   150: invokestatic access$1500 : (Lokhttp3/internal/DiskLruCache$Entry;)[Ljava/io/File;
    //   153: iload #4
    //   155: aaload
    //   156: astore_1
    //   157: iload_2
    //   158: ifeq -> 246
    //   161: aload_0
    //   162: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   165: aload_1
    //   166: invokeinterface exists : (Ljava/io/File;)Z
    //   171: ifeq -> 256
    //   174: aload #10
    //   176: invokestatic access$1400 : (Lokhttp3/internal/DiskLruCache$Entry;)[Ljava/io/File;
    //   179: iload #4
    //   181: aaload
    //   182: astore #11
    //   184: aload_0
    //   185: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   188: aload_1
    //   189: aload #11
    //   191: invokeinterface rename : (Ljava/io/File;Ljava/io/File;)V
    //   196: aload #10
    //   198: invokestatic access$1300 : (Lokhttp3/internal/DiskLruCache$Entry;)[J
    //   201: iload #4
    //   203: laload
    //   204: lstore #6
    //   206: aload_0
    //   207: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   210: aload #11
    //   212: invokeinterface size : (Ljava/io/File;)J
    //   217: lstore #8
    //   219: aload #10
    //   221: invokestatic access$1300 : (Lokhttp3/internal/DiskLruCache$Entry;)[J
    //   224: iload #4
    //   226: lload #8
    //   228: lastore
    //   229: aload_0
    //   230: aload_0
    //   231: getfield size : J
    //   234: lload #6
    //   236: lsub
    //   237: lload #8
    //   239: ladd
    //   240: putfield size : J
    //   243: goto -> 256
    //   246: aload_0
    //   247: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   250: aload_1
    //   251: invokeinterface delete : (Ljava/io/File;)V
    //   256: iinc #4, 1
    //   259: goto -> 139
    //   262: aload_0
    //   263: aload_0
    //   264: getfield redundantOpCount : I
    //   267: iconst_1
    //   268: iadd
    //   269: putfield redundantOpCount : I
    //   272: aload #10
    //   274: aconst_null
    //   275: invokestatic access$1002 : (Lokhttp3/internal/DiskLruCache$Entry;Lokhttp3/internal/DiskLruCache$Editor;)Lokhttp3/internal/DiskLruCache$Editor;
    //   278: pop
    //   279: aload #10
    //   281: invokestatic access$900 : (Lokhttp3/internal/DiskLruCache$Entry;)Z
    //   284: iload_2
    //   285: ior
    //   286: ifeq -> 380
    //   289: aload #10
    //   291: iconst_1
    //   292: invokestatic access$902 : (Lokhttp3/internal/DiskLruCache$Entry;Z)Z
    //   295: pop
    //   296: aload_0
    //   297: getfield journalWriter : Lokio/BufferedSink;
    //   300: ldc 'CLEAN'
    //   302: invokeinterface writeUtf8 : (Ljava/lang/String;)Lokio/BufferedSink;
    //   307: bipush #32
    //   309: invokeinterface writeByte : (I)Lokio/BufferedSink;
    //   314: pop
    //   315: aload_0
    //   316: getfield journalWriter : Lokio/BufferedSink;
    //   319: aload #10
    //   321: invokestatic access$1600 : (Lokhttp3/internal/DiskLruCache$Entry;)Ljava/lang/String;
    //   324: invokeinterface writeUtf8 : (Ljava/lang/String;)Lokio/BufferedSink;
    //   329: pop
    //   330: aload #10
    //   332: aload_0
    //   333: getfield journalWriter : Lokio/BufferedSink;
    //   336: invokevirtual writeLengths : (Lokio/BufferedSink;)V
    //   339: aload_0
    //   340: getfield journalWriter : Lokio/BufferedSink;
    //   343: bipush #10
    //   345: invokeinterface writeByte : (I)Lokio/BufferedSink;
    //   350: pop
    //   351: iload_2
    //   352: ifeq -> 439
    //   355: aload_0
    //   356: getfield nextSequenceNumber : J
    //   359: lstore #6
    //   361: aload_0
    //   362: lconst_1
    //   363: lload #6
    //   365: ladd
    //   366: putfield nextSequenceNumber : J
    //   369: aload #10
    //   371: lload #6
    //   373: invokestatic access$1702 : (Lokhttp3/internal/DiskLruCache$Entry;J)J
    //   376: pop2
    //   377: goto -> 439
    //   380: aload_0
    //   381: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   384: aload #10
    //   386: invokestatic access$1600 : (Lokhttp3/internal/DiskLruCache$Entry;)Ljava/lang/String;
    //   389: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   392: pop
    //   393: aload_0
    //   394: getfield journalWriter : Lokio/BufferedSink;
    //   397: ldc 'REMOVE'
    //   399: invokeinterface writeUtf8 : (Ljava/lang/String;)Lokio/BufferedSink;
    //   404: bipush #32
    //   406: invokeinterface writeByte : (I)Lokio/BufferedSink;
    //   411: pop
    //   412: aload_0
    //   413: getfield journalWriter : Lokio/BufferedSink;
    //   416: aload #10
    //   418: invokestatic access$1600 : (Lokhttp3/internal/DiskLruCache$Entry;)Ljava/lang/String;
    //   421: invokeinterface writeUtf8 : (Ljava/lang/String;)Lokio/BufferedSink;
    //   426: pop
    //   427: aload_0
    //   428: getfield journalWriter : Lokio/BufferedSink;
    //   431: bipush #10
    //   433: invokeinterface writeByte : (I)Lokio/BufferedSink;
    //   438: pop
    //   439: aload_0
    //   440: getfield journalWriter : Lokio/BufferedSink;
    //   443: invokeinterface flush : ()V
    //   448: aload_0
    //   449: getfield size : J
    //   452: aload_0
    //   453: getfield maxSize : J
    //   456: lcmp
    //   457: ifgt -> 467
    //   460: aload_0
    //   461: invokespecial journalRebuildRequired : ()Z
    //   464: ifeq -> 480
    //   467: aload_0
    //   468: getfield executor : Ljava/util/concurrent/Executor;
    //   471: aload_0
    //   472: getfield cleanupRunnable : Ljava/lang/Runnable;
    //   475: invokeinterface execute : (Ljava/lang/Runnable;)V
    //   480: aload_0
    //   481: monitorexit
    //   482: return
    //   483: new java/lang/IllegalStateException
    //   486: astore_1
    //   487: aload_1
    //   488: invokespecial <init> : ()V
    //   491: aload_1
    //   492: athrow
    //   493: astore_1
    //   494: aload_0
    //   495: monitorexit
    //   496: aload_1
    //   497: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	493	finally
    //   32	40	493	finally
    //   46	86	493	finally
    //   95	139	493	finally
    //   139	157	493	finally
    //   161	243	493	finally
    //   246	256	493	finally
    //   262	351	493	finally
    //   355	377	493	finally
    //   380	439	493	finally
    //   439	467	493	finally
    //   467	480	493	finally
    //   483	493	493	finally
  }
  
  public static DiskLruCache create(FileSystem paramFileSystem, File paramFile, int paramInt1, int paramInt2, long paramLong) {
    if (paramLong > 0L) {
      if (paramInt2 > 0)
        return new DiskLruCache(paramFileSystem, paramFile, paramInt1, paramInt2, paramLong, new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), Util.threadFactory("OkHttp DiskLruCache", true))); 
      throw new IllegalArgumentException("valueCount <= 0");
    } 
    throw new IllegalArgumentException("maxSize <= 0");
  }
  
  private Editor edit(String paramString, long paramLong) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual initialize : ()V
    //   6: aload_0
    //   7: invokespecial checkNotClosed : ()V
    //   10: aload_0
    //   11: aload_1
    //   12: invokespecial validateKey : (Ljava/lang/String;)V
    //   15: aload_0
    //   16: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   19: aload_1
    //   20: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   23: checkcast okhttp3/internal/DiskLruCache$Entry
    //   26: astore #8
    //   28: lload_2
    //   29: ldc2_w -1
    //   32: lcmp
    //   33: ifeq -> 59
    //   36: aload #8
    //   38: ifnull -> 55
    //   41: aload #8
    //   43: invokestatic access$1700 : (Lokhttp3/internal/DiskLruCache$Entry;)J
    //   46: lstore #5
    //   48: lload #5
    //   50: lload_2
    //   51: lcmp
    //   52: ifeq -> 59
    //   55: aload_0
    //   56: monitorexit
    //   57: aconst_null
    //   58: areturn
    //   59: aload #8
    //   61: ifnull -> 80
    //   64: aload #8
    //   66: invokestatic access$1000 : (Lokhttp3/internal/DiskLruCache$Entry;)Lokhttp3/internal/DiskLruCache$Editor;
    //   69: astore #7
    //   71: aload #7
    //   73: ifnull -> 80
    //   76: aload_0
    //   77: monitorexit
    //   78: aconst_null
    //   79: areturn
    //   80: aload_0
    //   81: getfield mostRecentTrimFailed : Z
    //   84: ifeq -> 104
    //   87: aload_0
    //   88: getfield executor : Ljava/util/concurrent/Executor;
    //   91: aload_0
    //   92: getfield cleanupRunnable : Ljava/lang/Runnable;
    //   95: invokeinterface execute : (Ljava/lang/Runnable;)V
    //   100: aload_0
    //   101: monitorexit
    //   102: aconst_null
    //   103: areturn
    //   104: aload_0
    //   105: getfield journalWriter : Lokio/BufferedSink;
    //   108: ldc 'DIRTY'
    //   110: invokeinterface writeUtf8 : (Ljava/lang/String;)Lokio/BufferedSink;
    //   115: bipush #32
    //   117: invokeinterface writeByte : (I)Lokio/BufferedSink;
    //   122: aload_1
    //   123: invokeinterface writeUtf8 : (Ljava/lang/String;)Lokio/BufferedSink;
    //   128: bipush #10
    //   130: invokeinterface writeByte : (I)Lokio/BufferedSink;
    //   135: pop
    //   136: aload_0
    //   137: getfield journalWriter : Lokio/BufferedSink;
    //   140: invokeinterface flush : ()V
    //   145: aload_0
    //   146: getfield hasJournalErrors : Z
    //   149: istore #4
    //   151: iload #4
    //   153: ifeq -> 160
    //   156: aload_0
    //   157: monitorexit
    //   158: aconst_null
    //   159: areturn
    //   160: aload #8
    //   162: astore #7
    //   164: aload #8
    //   166: ifnonnull -> 193
    //   169: new okhttp3/internal/DiskLruCache$Entry
    //   172: astore #7
    //   174: aload #7
    //   176: aload_0
    //   177: aload_1
    //   178: aconst_null
    //   179: invokespecial <init> : (Lokhttp3/internal/DiskLruCache;Ljava/lang/String;Lokhttp3/internal/DiskLruCache$1;)V
    //   182: aload_0
    //   183: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   186: aload_1
    //   187: aload #7
    //   189: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   192: pop
    //   193: new okhttp3/internal/DiskLruCache$Editor
    //   196: astore_1
    //   197: aload_1
    //   198: aload_0
    //   199: aload #7
    //   201: aconst_null
    //   202: invokespecial <init> : (Lokhttp3/internal/DiskLruCache;Lokhttp3/internal/DiskLruCache$Entry;Lokhttp3/internal/DiskLruCache$1;)V
    //   205: aload #7
    //   207: aload_1
    //   208: invokestatic access$1002 : (Lokhttp3/internal/DiskLruCache$Entry;Lokhttp3/internal/DiskLruCache$Editor;)Lokhttp3/internal/DiskLruCache$Editor;
    //   211: pop
    //   212: aload_0
    //   213: monitorexit
    //   214: aload_1
    //   215: areturn
    //   216: astore_1
    //   217: aload_0
    //   218: monitorexit
    //   219: aload_1
    //   220: athrow
    // Exception table:
    //   from	to	target	type
    //   2	28	216	finally
    //   41	48	216	finally
    //   64	71	216	finally
    //   80	100	216	finally
    //   104	151	216	finally
    //   169	193	216	finally
    //   193	212	216	finally
  }
  
  private boolean journalRebuildRequired() {
    boolean bool;
    int i = this.redundantOpCount;
    if (i >= 2000 && i >= this.lruEntries.size()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private BufferedSink newJournalWriter() throws FileNotFoundException {
    return Okio.buffer((Sink)new FaultHidingSink(this.fileSystem.appendingSink(this.journalFile)) {
          static final boolean $assertionsDisabled = false;
          
          final DiskLruCache this$0;
          
          protected void onException(IOException param1IOException) {
            DiskLruCache.access$702(DiskLruCache.this, true);
          }
        });
  }
  
  private void processJournal() throws IOException {
    this.fileSystem.delete(this.journalFileTmp);
    Iterator<Entry> iterator = this.lruEntries.values().iterator();
    while (iterator.hasNext()) {
      Entry entry = iterator.next();
      Editor editor = entry.currentEditor;
      boolean bool = false;
      byte b = 0;
      if (editor == null) {
        while (b < this.valueCount) {
          this.size += entry.lengths[b];
          b++;
        } 
        continue;
      } 
      Entry.access$1002(entry, null);
      for (b = bool; b < this.valueCount; b++) {
        this.fileSystem.delete(entry.cleanFiles[b]);
        this.fileSystem.delete(entry.dirtyFiles[b]);
      } 
      iterator.remove();
    } 
  }
  
  private void readJournal() throws IOException {
    BufferedSource bufferedSource = Okio.buffer(this.fileSystem.source(this.journalFile));
    try {
      String str2 = bufferedSource.readUtf8LineStrict();
      String str3 = bufferedSource.readUtf8LineStrict();
      String str5 = bufferedSource.readUtf8LineStrict();
      String str4 = bufferedSource.readUtf8LineStrict();
      String str1 = bufferedSource.readUtf8LineStrict();
      if ("libcore.io.DiskLruCache".equals(str2) && "1".equals(str3) && Integer.toString(this.appVersion).equals(str5) && Integer.toString(this.valueCount).equals(str4)) {
        boolean bool = "".equals(str1);
        if (bool) {
          byte b = 0;
          try {
            while (true) {
              readJournalLine(bufferedSource.readUtf8LineStrict());
              b++;
            } 
          } catch (EOFException eOFException) {
            this.redundantOpCount = b - this.lruEntries.size();
            if (!bufferedSource.exhausted()) {
              rebuildJournal();
            } else {
              this.journalWriter = newJournalWriter();
            } 
            return;
          } 
        } 
      } 
      IOException iOException = new IOException();
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append("unexpected journal header: [");
      stringBuilder.append(str2);
      stringBuilder.append(", ");
      stringBuilder.append(str3);
      stringBuilder.append(", ");
      stringBuilder.append(str4);
      stringBuilder.append(", ");
      stringBuilder.append((String)eOFException);
      stringBuilder.append("]");
      this(stringBuilder.toString());
      throw iOException;
    } finally {
      Util.closeQuietly((Closeable)bufferedSource);
    } 
  }
  
  private void readJournalLine(String paramString) throws IOException {
    String[] arrayOfString;
    int i = paramString.indexOf(' ');
    if (i != -1) {
      String str;
      int k = i + 1;
      int j = paramString.indexOf(' ', k);
      if (j == -1) {
        String str1 = paramString.substring(k);
        str = str1;
        if (i == 6) {
          str = str1;
          if (paramString.startsWith("REMOVE")) {
            this.lruEntries.remove(str1);
            return;
          } 
        } 
      } else {
        str = paramString.substring(k, j);
      } 
      Entry entry2 = this.lruEntries.get(str);
      Entry entry1 = entry2;
      if (entry2 == null) {
        entry1 = new Entry(str);
        this.lruEntries.put(str, entry1);
      } 
      if (j != -1 && i == 5 && paramString.startsWith("CLEAN")) {
        arrayOfString = paramString.substring(j + 1).split(" ");
        Entry.access$902(entry1, true);
        Entry.access$1002(entry1, null);
        entry1.setLengths(arrayOfString);
      } else if (j == -1 && i == 5 && arrayOfString.startsWith("DIRTY")) {
        Entry.access$1002(entry1, new Editor(entry1));
      } else if (j != -1 || i != 4 || !arrayOfString.startsWith("READ")) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("unexpected journal line: ");
        stringBuilder1.append((String)arrayOfString);
        throw new IOException(stringBuilder1.toString());
      } 
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("unexpected journal line: ");
    stringBuilder.append((String)arrayOfString);
    throw new IOException(stringBuilder.toString());
  }
  
  private void rebuildJournal() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield journalWriter : Lokio/BufferedSink;
    //   6: ifnull -> 18
    //   9: aload_0
    //   10: getfield journalWriter : Lokio/BufferedSink;
    //   13: invokeinterface close : ()V
    //   18: aload_0
    //   19: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   22: aload_0
    //   23: getfield journalFileTmp : Ljava/io/File;
    //   26: invokeinterface sink : (Ljava/io/File;)Lokio/Sink;
    //   31: invokestatic buffer : (Lokio/Sink;)Lokio/BufferedSink;
    //   34: astore_1
    //   35: aload_1
    //   36: ldc 'libcore.io.DiskLruCache'
    //   38: invokeinterface writeUtf8 : (Ljava/lang/String;)Lokio/BufferedSink;
    //   43: bipush #10
    //   45: invokeinterface writeByte : (I)Lokio/BufferedSink;
    //   50: pop
    //   51: aload_1
    //   52: ldc '1'
    //   54: invokeinterface writeUtf8 : (Ljava/lang/String;)Lokio/BufferedSink;
    //   59: bipush #10
    //   61: invokeinterface writeByte : (I)Lokio/BufferedSink;
    //   66: pop
    //   67: aload_1
    //   68: aload_0
    //   69: getfield appVersion : I
    //   72: i2l
    //   73: invokeinterface writeDecimalLong : (J)Lokio/BufferedSink;
    //   78: bipush #10
    //   80: invokeinterface writeByte : (I)Lokio/BufferedSink;
    //   85: pop
    //   86: aload_1
    //   87: aload_0
    //   88: getfield valueCount : I
    //   91: i2l
    //   92: invokeinterface writeDecimalLong : (J)Lokio/BufferedSink;
    //   97: bipush #10
    //   99: invokeinterface writeByte : (I)Lokio/BufferedSink;
    //   104: pop
    //   105: aload_1
    //   106: bipush #10
    //   108: invokeinterface writeByte : (I)Lokio/BufferedSink;
    //   113: pop
    //   114: aload_0
    //   115: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   118: invokevirtual values : ()Ljava/util/Collection;
    //   121: invokeinterface iterator : ()Ljava/util/Iterator;
    //   126: astore_3
    //   127: aload_3
    //   128: invokeinterface hasNext : ()Z
    //   133: ifeq -> 236
    //   136: aload_3
    //   137: invokeinterface next : ()Ljava/lang/Object;
    //   142: checkcast okhttp3/internal/DiskLruCache$Entry
    //   145: astore_2
    //   146: aload_2
    //   147: invokestatic access$1000 : (Lokhttp3/internal/DiskLruCache$Entry;)Lokhttp3/internal/DiskLruCache$Editor;
    //   150: ifnull -> 192
    //   153: aload_1
    //   154: ldc 'DIRTY'
    //   156: invokeinterface writeUtf8 : (Ljava/lang/String;)Lokio/BufferedSink;
    //   161: bipush #32
    //   163: invokeinterface writeByte : (I)Lokio/BufferedSink;
    //   168: pop
    //   169: aload_1
    //   170: aload_2
    //   171: invokestatic access$1600 : (Lokhttp3/internal/DiskLruCache$Entry;)Ljava/lang/String;
    //   174: invokeinterface writeUtf8 : (Ljava/lang/String;)Lokio/BufferedSink;
    //   179: pop
    //   180: aload_1
    //   181: bipush #10
    //   183: invokeinterface writeByte : (I)Lokio/BufferedSink;
    //   188: pop
    //   189: goto -> 127
    //   192: aload_1
    //   193: ldc 'CLEAN'
    //   195: invokeinterface writeUtf8 : (Ljava/lang/String;)Lokio/BufferedSink;
    //   200: bipush #32
    //   202: invokeinterface writeByte : (I)Lokio/BufferedSink;
    //   207: pop
    //   208: aload_1
    //   209: aload_2
    //   210: invokestatic access$1600 : (Lokhttp3/internal/DiskLruCache$Entry;)Ljava/lang/String;
    //   213: invokeinterface writeUtf8 : (Ljava/lang/String;)Lokio/BufferedSink;
    //   218: pop
    //   219: aload_2
    //   220: aload_1
    //   221: invokevirtual writeLengths : (Lokio/BufferedSink;)V
    //   224: aload_1
    //   225: bipush #10
    //   227: invokeinterface writeByte : (I)Lokio/BufferedSink;
    //   232: pop
    //   233: goto -> 127
    //   236: aload_1
    //   237: invokeinterface close : ()V
    //   242: aload_0
    //   243: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   246: aload_0
    //   247: getfield journalFile : Ljava/io/File;
    //   250: invokeinterface exists : (Ljava/io/File;)Z
    //   255: ifeq -> 275
    //   258: aload_0
    //   259: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   262: aload_0
    //   263: getfield journalFile : Ljava/io/File;
    //   266: aload_0
    //   267: getfield journalFileBackup : Ljava/io/File;
    //   270: invokeinterface rename : (Ljava/io/File;Ljava/io/File;)V
    //   275: aload_0
    //   276: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   279: aload_0
    //   280: getfield journalFileTmp : Ljava/io/File;
    //   283: aload_0
    //   284: getfield journalFile : Ljava/io/File;
    //   287: invokeinterface rename : (Ljava/io/File;Ljava/io/File;)V
    //   292: aload_0
    //   293: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   296: aload_0
    //   297: getfield journalFileBackup : Ljava/io/File;
    //   300: invokeinterface delete : (Ljava/io/File;)V
    //   305: aload_0
    //   306: aload_0
    //   307: invokespecial newJournalWriter : ()Lokio/BufferedSink;
    //   310: putfield journalWriter : Lokio/BufferedSink;
    //   313: aload_0
    //   314: iconst_0
    //   315: putfield hasJournalErrors : Z
    //   318: aload_0
    //   319: monitorexit
    //   320: return
    //   321: astore_2
    //   322: aload_1
    //   323: invokeinterface close : ()V
    //   328: aload_2
    //   329: athrow
    //   330: astore_1
    //   331: aload_0
    //   332: monitorexit
    //   333: aload_1
    //   334: athrow
    // Exception table:
    //   from	to	target	type
    //   2	18	330	finally
    //   18	35	330	finally
    //   35	127	321	finally
    //   127	189	321	finally
    //   192	233	321	finally
    //   236	275	330	finally
    //   275	318	330	finally
    //   322	330	330	finally
  }
  
  private boolean removeEntry(Entry paramEntry) throws IOException {
    if (paramEntry.currentEditor != null)
      Editor.access$2002(paramEntry.currentEditor, true); 
    for (byte b = 0; b < this.valueCount; b++) {
      this.fileSystem.delete(paramEntry.cleanFiles[b]);
      this.size -= paramEntry.lengths[b];
      paramEntry.lengths[b] = 0L;
    } 
    this.redundantOpCount++;
    this.journalWriter.writeUtf8("REMOVE").writeByte(32).writeUtf8(paramEntry.key).writeByte(10);
    this.lruEntries.remove(paramEntry.key);
    if (journalRebuildRequired())
      this.executor.execute(this.cleanupRunnable); 
    return true;
  }
  
  private void trimToSize() throws IOException {
    while (this.size > this.maxSize)
      removeEntry(this.lruEntries.values().iterator().next()); 
    this.mostRecentTrimFailed = false;
  }
  
  private void validateKey(String paramString) {
    if (LEGAL_KEY_PATTERN.matcher(paramString).matches())
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("keys must match regex [a-z0-9_-]{1,120}: \"");
    stringBuilder.append(paramString);
    stringBuilder.append("\"");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void close() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield initialized : Z
    //   6: ifeq -> 108
    //   9: aload_0
    //   10: getfield closed : Z
    //   13: ifeq -> 19
    //   16: goto -> 108
    //   19: aload_0
    //   20: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   23: invokevirtual values : ()Ljava/util/Collection;
    //   26: aload_0
    //   27: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   30: invokevirtual size : ()I
    //   33: anewarray okhttp3/internal/DiskLruCache$Entry
    //   36: invokeinterface toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   41: checkcast [Lokhttp3/internal/DiskLruCache$Entry;
    //   44: astore_3
    //   45: aload_3
    //   46: arraylength
    //   47: istore_2
    //   48: iconst_0
    //   49: istore_1
    //   50: iload_1
    //   51: iload_2
    //   52: if_icmpge -> 82
    //   55: aload_3
    //   56: iload_1
    //   57: aaload
    //   58: astore #4
    //   60: aload #4
    //   62: invokestatic access$1000 : (Lokhttp3/internal/DiskLruCache$Entry;)Lokhttp3/internal/DiskLruCache$Editor;
    //   65: ifnull -> 76
    //   68: aload #4
    //   70: invokestatic access$1000 : (Lokhttp3/internal/DiskLruCache$Entry;)Lokhttp3/internal/DiskLruCache$Editor;
    //   73: invokevirtual abort : ()V
    //   76: iinc #1, 1
    //   79: goto -> 50
    //   82: aload_0
    //   83: invokespecial trimToSize : ()V
    //   86: aload_0
    //   87: getfield journalWriter : Lokio/BufferedSink;
    //   90: invokeinterface close : ()V
    //   95: aload_0
    //   96: aconst_null
    //   97: putfield journalWriter : Lokio/BufferedSink;
    //   100: aload_0
    //   101: iconst_1
    //   102: putfield closed : Z
    //   105: aload_0
    //   106: monitorexit
    //   107: return
    //   108: aload_0
    //   109: iconst_1
    //   110: putfield closed : Z
    //   113: aload_0
    //   114: monitorexit
    //   115: return
    //   116: astore_3
    //   117: aload_0
    //   118: monitorexit
    //   119: aload_3
    //   120: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	116	finally
    //   19	48	116	finally
    //   60	76	116	finally
    //   82	105	116	finally
    //   108	113	116	finally
  }
  
  public void delete() throws IOException {
    close();
    this.fileSystem.deleteContents(this.directory);
  }
  
  public Editor edit(String paramString) throws IOException {
    return edit(paramString, -1L);
  }
  
  public void evictAll() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual initialize : ()V
    //   6: aload_0
    //   7: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   10: invokevirtual values : ()Ljava/util/Collection;
    //   13: aload_0
    //   14: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   17: invokevirtual size : ()I
    //   20: anewarray okhttp3/internal/DiskLruCache$Entry
    //   23: invokeinterface toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   28: checkcast [Lokhttp3/internal/DiskLruCache$Entry;
    //   31: astore_3
    //   32: aload_3
    //   33: arraylength
    //   34: istore_2
    //   35: iconst_0
    //   36: istore_1
    //   37: iload_1
    //   38: iload_2
    //   39: if_icmpge -> 56
    //   42: aload_0
    //   43: aload_3
    //   44: iload_1
    //   45: aaload
    //   46: invokespecial removeEntry : (Lokhttp3/internal/DiskLruCache$Entry;)Z
    //   49: pop
    //   50: iinc #1, 1
    //   53: goto -> 37
    //   56: aload_0
    //   57: iconst_0
    //   58: putfield mostRecentTrimFailed : Z
    //   61: aload_0
    //   62: monitorexit
    //   63: return
    //   64: astore_3
    //   65: aload_0
    //   66: monitorexit
    //   67: aload_3
    //   68: athrow
    // Exception table:
    //   from	to	target	type
    //   2	35	64	finally
    //   42	50	64	finally
    //   56	61	64	finally
  }
  
  public void flush() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield initialized : Z
    //   6: istore_1
    //   7: iload_1
    //   8: ifne -> 14
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: aload_0
    //   15: invokespecial checkNotClosed : ()V
    //   18: aload_0
    //   19: invokespecial trimToSize : ()V
    //   22: aload_0
    //   23: getfield journalWriter : Lokio/BufferedSink;
    //   26: invokeinterface flush : ()V
    //   31: aload_0
    //   32: monitorexit
    //   33: return
    //   34: astore_2
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_2
    //   38: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	34	finally
    //   14	31	34	finally
  }
  
  public Snapshot get(String paramString) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual initialize : ()V
    //   6: aload_0
    //   7: invokespecial checkNotClosed : ()V
    //   10: aload_0
    //   11: aload_1
    //   12: invokespecial validateKey : (Ljava/lang/String;)V
    //   15: aload_0
    //   16: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   19: aload_1
    //   20: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   23: checkcast okhttp3/internal/DiskLruCache$Entry
    //   26: astore_2
    //   27: aload_2
    //   28: ifnull -> 120
    //   31: aload_2
    //   32: invokestatic access$900 : (Lokhttp3/internal/DiskLruCache$Entry;)Z
    //   35: ifne -> 41
    //   38: goto -> 120
    //   41: aload_2
    //   42: invokevirtual snapshot : ()Lokhttp3/internal/DiskLruCache$Snapshot;
    //   45: astore_2
    //   46: aload_2
    //   47: ifnonnull -> 54
    //   50: aload_0
    //   51: monitorexit
    //   52: aconst_null
    //   53: areturn
    //   54: aload_0
    //   55: aload_0
    //   56: getfield redundantOpCount : I
    //   59: iconst_1
    //   60: iadd
    //   61: putfield redundantOpCount : I
    //   64: aload_0
    //   65: getfield journalWriter : Lokio/BufferedSink;
    //   68: ldc 'READ'
    //   70: invokeinterface writeUtf8 : (Ljava/lang/String;)Lokio/BufferedSink;
    //   75: bipush #32
    //   77: invokeinterface writeByte : (I)Lokio/BufferedSink;
    //   82: aload_1
    //   83: invokeinterface writeUtf8 : (Ljava/lang/String;)Lokio/BufferedSink;
    //   88: bipush #10
    //   90: invokeinterface writeByte : (I)Lokio/BufferedSink;
    //   95: pop
    //   96: aload_0
    //   97: invokespecial journalRebuildRequired : ()Z
    //   100: ifeq -> 116
    //   103: aload_0
    //   104: getfield executor : Ljava/util/concurrent/Executor;
    //   107: aload_0
    //   108: getfield cleanupRunnable : Ljava/lang/Runnable;
    //   111: invokeinterface execute : (Ljava/lang/Runnable;)V
    //   116: aload_0
    //   117: monitorexit
    //   118: aload_2
    //   119: areturn
    //   120: aload_0
    //   121: monitorexit
    //   122: aconst_null
    //   123: areturn
    //   124: astore_1
    //   125: aload_0
    //   126: monitorexit
    //   127: aload_1
    //   128: athrow
    // Exception table:
    //   from	to	target	type
    //   2	27	124	finally
    //   31	38	124	finally
    //   41	46	124	finally
    //   54	116	124	finally
  }
  
  public File getDirectory() {
    return this.directory;
  }
  
  public long getMaxSize() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield maxSize : J
    //   6: lstore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: lload_1
    //   10: lreturn
    //   11: astore_3
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_3
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public void initialize() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield initialized : Z
    //   6: istore_1
    //   7: iload_1
    //   8: ifeq -> 14
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: aload_0
    //   15: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   18: aload_0
    //   19: getfield journalFileBackup : Ljava/io/File;
    //   22: invokeinterface exists : (Ljava/io/File;)Z
    //   27: ifeq -> 79
    //   30: aload_0
    //   31: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   34: aload_0
    //   35: getfield journalFile : Ljava/io/File;
    //   38: invokeinterface exists : (Ljava/io/File;)Z
    //   43: ifeq -> 62
    //   46: aload_0
    //   47: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   50: aload_0
    //   51: getfield journalFileBackup : Ljava/io/File;
    //   54: invokeinterface delete : (Ljava/io/File;)V
    //   59: goto -> 79
    //   62: aload_0
    //   63: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   66: aload_0
    //   67: getfield journalFileBackup : Ljava/io/File;
    //   70: aload_0
    //   71: getfield journalFile : Ljava/io/File;
    //   74: invokeinterface rename : (Ljava/io/File;Ljava/io/File;)V
    //   79: aload_0
    //   80: getfield fileSystem : Lokhttp3/internal/io/FileSystem;
    //   83: aload_0
    //   84: getfield journalFile : Ljava/io/File;
    //   87: invokeinterface exists : (Ljava/io/File;)Z
    //   92: istore_1
    //   93: iload_1
    //   94: ifeq -> 193
    //   97: aload_0
    //   98: invokespecial readJournal : ()V
    //   101: aload_0
    //   102: invokespecial processJournal : ()V
    //   105: aload_0
    //   106: iconst_1
    //   107: putfield initialized : Z
    //   110: aload_0
    //   111: monitorexit
    //   112: return
    //   113: astore_3
    //   114: invokestatic get : ()Lokhttp3/internal/Platform;
    //   117: astore_2
    //   118: new java/lang/StringBuilder
    //   121: astore #4
    //   123: aload #4
    //   125: invokespecial <init> : ()V
    //   128: aload #4
    //   130: ldc_w 'DiskLruCache '
    //   133: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   136: pop
    //   137: aload #4
    //   139: aload_0
    //   140: getfield directory : Ljava/io/File;
    //   143: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   146: pop
    //   147: aload #4
    //   149: ldc_w ' is corrupt: '
    //   152: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   155: pop
    //   156: aload #4
    //   158: aload_3
    //   159: invokevirtual getMessage : ()Ljava/lang/String;
    //   162: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   165: pop
    //   166: aload #4
    //   168: ldc_w ', removing'
    //   171: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   174: pop
    //   175: aload_2
    //   176: aload #4
    //   178: invokevirtual toString : ()Ljava/lang/String;
    //   181: invokevirtual logW : (Ljava/lang/String;)V
    //   184: aload_0
    //   185: invokevirtual delete : ()V
    //   188: aload_0
    //   189: iconst_0
    //   190: putfield closed : Z
    //   193: aload_0
    //   194: invokespecial rebuildJournal : ()V
    //   197: aload_0
    //   198: iconst_1
    //   199: putfield initialized : Z
    //   202: aload_0
    //   203: monitorexit
    //   204: return
    //   205: astore_2
    //   206: aload_0
    //   207: monitorexit
    //   208: aload_2
    //   209: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	205	finally
    //   14	59	205	finally
    //   62	79	205	finally
    //   79	93	205	finally
    //   97	110	113	java/io/IOException
    //   97	110	205	finally
    //   114	193	205	finally
    //   193	202	205	finally
  }
  
  public boolean isClosed() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield closed : Z
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public boolean remove(String paramString) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual initialize : ()V
    //   6: aload_0
    //   7: invokespecial checkNotClosed : ()V
    //   10: aload_0
    //   11: aload_1
    //   12: invokespecial validateKey : (Ljava/lang/String;)V
    //   15: aload_0
    //   16: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   19: aload_1
    //   20: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   23: checkcast okhttp3/internal/DiskLruCache$Entry
    //   26: astore_1
    //   27: aload_1
    //   28: ifnonnull -> 35
    //   31: aload_0
    //   32: monitorexit
    //   33: iconst_0
    //   34: ireturn
    //   35: aload_0
    //   36: aload_1
    //   37: invokespecial removeEntry : (Lokhttp3/internal/DiskLruCache$Entry;)Z
    //   40: istore_2
    //   41: iload_2
    //   42: ifeq -> 62
    //   45: aload_0
    //   46: getfield size : J
    //   49: aload_0
    //   50: getfield maxSize : J
    //   53: lcmp
    //   54: ifgt -> 62
    //   57: aload_0
    //   58: iconst_0
    //   59: putfield mostRecentTrimFailed : Z
    //   62: aload_0
    //   63: monitorexit
    //   64: iload_2
    //   65: ireturn
    //   66: astore_1
    //   67: aload_0
    //   68: monitorexit
    //   69: aload_1
    //   70: athrow
    // Exception table:
    //   from	to	target	type
    //   2	27	66	finally
    //   35	41	66	finally
    //   45	62	66	finally
  }
  
  public void setMaxSize(long paramLong) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: lload_1
    //   4: putfield maxSize : J
    //   7: aload_0
    //   8: getfield initialized : Z
    //   11: ifeq -> 27
    //   14: aload_0
    //   15: getfield executor : Ljava/util/concurrent/Executor;
    //   18: aload_0
    //   19: getfield cleanupRunnable : Ljava/lang/Runnable;
    //   22: invokeinterface execute : (Ljava/lang/Runnable;)V
    //   27: aload_0
    //   28: monitorexit
    //   29: return
    //   30: astore_3
    //   31: aload_0
    //   32: monitorexit
    //   33: aload_3
    //   34: athrow
    // Exception table:
    //   from	to	target	type
    //   2	27	30	finally
  }
  
  public long size() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual initialize : ()V
    //   6: aload_0
    //   7: getfield size : J
    //   10: lstore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: lload_1
    //   14: lreturn
    //   15: astore_3
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_3
    //   19: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	15	finally
  }
  
  public Iterator<Snapshot> snapshots() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual initialize : ()V
    //   6: new okhttp3/internal/DiskLruCache$3
    //   9: dup
    //   10: aload_0
    //   11: invokespecial <init> : (Lokhttp3/internal/DiskLruCache;)V
    //   14: astore_1
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_1
    //   18: areturn
    //   19: astore_1
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_1
    //   23: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	19	finally
  }
  
  public final class Editor {
    private boolean committed;
    
    private final DiskLruCache.Entry entry;
    
    private boolean hasErrors;
    
    final DiskLruCache this$0;
    
    private final boolean[] written;
    
    private Editor(DiskLruCache.Entry param1Entry) {
      boolean[] arrayOfBoolean;
      this.entry = param1Entry;
      if (param1Entry.readable) {
        DiskLruCache.this = null;
      } else {
        arrayOfBoolean = new boolean[DiskLruCache.this.valueCount];
      } 
      this.written = arrayOfBoolean;
    }
    
    public void abort() throws IOException {
      synchronized (DiskLruCache.this) {
        DiskLruCache.this.completeEdit(this, false);
        return;
      } 
    }
    
    public void abortUnlessCommitted() {
      synchronized (DiskLruCache.this) {
        boolean bool = this.committed;
        if (!bool)
          try {
            DiskLruCache.this.completeEdit(this, false);
          } catch (IOException iOException) {} 
        return;
      } 
    }
    
    public void commit() throws IOException {
      synchronized (DiskLruCache.this) {
        if (this.hasErrors) {
          DiskLruCache.this.completeEdit(this, false);
          DiskLruCache.this.removeEntry(this.entry);
        } else {
          DiskLruCache.this.completeEdit(this, true);
        } 
        this.committed = true;
        return;
      } 
    }
    
    public Sink newSink(int param1Int) throws IOException {
      synchronized (DiskLruCache.this) {
        if (this.entry.currentEditor == this) {
          if (!this.entry.readable)
            this.written[param1Int] = true; 
          File file = this.entry.dirtyFiles[param1Int];
          try {
            Sink sink = DiskLruCache.this.fileSystem.sink(file);
            FaultHidingSink faultHidingSink = new FaultHidingSink() {
                final DiskLruCache.Editor this$1;
                
                protected void onException(IOException param2IOException) {
                  synchronized (DiskLruCache.this) {
                    DiskLruCache.Editor.access$2002(DiskLruCache.Editor.this, true);
                    return;
                  } 
                }
              };
            super(this, sink);
            return (Sink)faultHidingSink;
          } catch (FileNotFoundException fileNotFoundException) {
            return DiskLruCache.NULL_SINK;
          } 
        } 
        IllegalStateException illegalStateException = new IllegalStateException();
        this();
        throw illegalStateException;
      } 
    }
    
    public Source newSource(int param1Int) throws IOException {
      synchronized (DiskLruCache.this) {
        if (this.entry.currentEditor == this) {
          if (!this.entry.readable)
            return null; 
          try {
            return DiskLruCache.this.fileSystem.source(this.entry.cleanFiles[param1Int]);
          } catch (FileNotFoundException fileNotFoundException) {
            return null;
          } 
        } 
        IllegalStateException illegalStateException = new IllegalStateException();
        this();
        throw illegalStateException;
      } 
    }
  }
  
  class null extends FaultHidingSink {
    final DiskLruCache.Editor this$1;
    
    null(Sink param1Sink) {
      super(param1Sink);
    }
    
    protected void onException(IOException param1IOException) {
      synchronized (DiskLruCache.this) {
        DiskLruCache.Editor.access$2002(this.this$1, true);
        return;
      } 
    }
  }
  
  private final class Entry {
    private final File[] cleanFiles;
    
    private DiskLruCache.Editor currentEditor;
    
    private final File[] dirtyFiles;
    
    private final String key;
    
    private final long[] lengths;
    
    private boolean readable;
    
    private long sequenceNumber;
    
    final DiskLruCache this$0;
    
    private Entry(String param1String) {
      this.key = param1String;
      this.lengths = new long[DiskLruCache.this.valueCount];
      this.cleanFiles = new File[DiskLruCache.this.valueCount];
      this.dirtyFiles = new File[DiskLruCache.this.valueCount];
      StringBuilder stringBuilder = new StringBuilder(param1String);
      stringBuilder.append('.');
      int i = stringBuilder.length();
      for (byte b = 0; b < DiskLruCache.this.valueCount; b++) {
        stringBuilder.append(b);
        this.cleanFiles[b] = new File(DiskLruCache.this.directory, stringBuilder.toString());
        stringBuilder.append(".tmp");
        this.dirtyFiles[b] = new File(DiskLruCache.this.directory, stringBuilder.toString());
        stringBuilder.setLength(i);
      } 
    }
    
    private IOException invalidLengths(String[] param1ArrayOfString) throws IOException {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("unexpected journal line: ");
      stringBuilder.append(Arrays.toString((Object[])param1ArrayOfString));
      throw new IOException(stringBuilder.toString());
    }
    
    private void setLengths(String[] param1ArrayOfString) throws IOException {
      if (param1ArrayOfString.length == DiskLruCache.this.valueCount) {
        byte b = 0;
        try {
          while (b < param1ArrayOfString.length) {
            this.lengths[b] = Long.parseLong(param1ArrayOfString[b]);
            b++;
          } 
          return;
        } catch (NumberFormatException numberFormatException) {
          throw invalidLengths(param1ArrayOfString);
        } 
      } 
      throw invalidLengths(param1ArrayOfString);
    }
    
    DiskLruCache.Snapshot snapshot() {
      if (Thread.holdsLock(DiskLruCache.this)) {
        Source[] arrayOfSource = new Source[DiskLruCache.this.valueCount];
        long[] arrayOfLong = (long[])this.lengths.clone();
        boolean bool = false;
        byte b = 0;
        try {
          while (b < DiskLruCache.this.valueCount) {
            arrayOfSource[b] = DiskLruCache.this.fileSystem.source(this.cleanFiles[b]);
            b++;
          } 
          return new DiskLruCache.Snapshot(this.key, this.sequenceNumber, arrayOfSource, arrayOfLong);
        } catch (FileNotFoundException fileNotFoundException) {
          for (b = bool; b < DiskLruCache.this.valueCount && arrayOfSource[b] != null; b++)
            Util.closeQuietly((Closeable)arrayOfSource[b]); 
          return null;
        } 
      } 
      throw new AssertionError();
    }
    
    void writeLengths(BufferedSink param1BufferedSink) throws IOException {
      for (long l : this.lengths)
        param1BufferedSink.writeByte(32).writeDecimalLong(l); 
    }
  }
  
  public final class Snapshot implements Closeable {
    private final String key;
    
    private final long[] lengths;
    
    private final long sequenceNumber;
    
    private final Source[] sources;
    
    final DiskLruCache this$0;
    
    private Snapshot(String param1String, long param1Long, Source[] param1ArrayOfSource, long[] param1ArrayOflong) {
      this.key = param1String;
      this.sequenceNumber = param1Long;
      this.sources = param1ArrayOfSource;
      this.lengths = param1ArrayOflong;
    }
    
    public void close() {
      Source[] arrayOfSource = this.sources;
      int i = arrayOfSource.length;
      for (byte b = 0; b < i; b++)
        Util.closeQuietly((Closeable)arrayOfSource[b]); 
    }
    
    public DiskLruCache.Editor edit() throws IOException {
      return DiskLruCache.this.edit(this.key, this.sequenceNumber);
    }
    
    public long getLength(int param1Int) {
      return this.lengths[param1Int];
    }
    
    public Source getSource(int param1Int) {
      return this.sources[param1Int];
    }
    
    public String key() {
      return this.key;
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\internal\DiskLruCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */