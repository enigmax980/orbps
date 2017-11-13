package com.newcore.orbps.service.business;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ReaderNotOpenException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.NonTransientFlatFileException;
import org.springframework.batch.item.file.separator.RecordSeparatorPolicy;
import org.springframework.batch.item.file.separator.SimpleRecordSeparatorPolicy;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import com.newcore.orbps.service.api.ResourceReaderFactory;

/**
 * 解析清单文件
 * 
 * @author liushuaifeng
 *
 *  创建时间：2016年9月19日下午9:26:13
 */

public class IpsnItemFileReader<T> extends AbstractItemCountingItemStreamItemReader<T>implements InitializingBean {

	private final static Logger logger = LoggerFactory.getLogger(IpsnItemFileReader.class);
	// default encoding for input files
	public static final String DEFAULT_CHARSET = Charset.defaultCharset().name();

	private RecordSeparatorPolicy recordSeparatorPolicy = new SimpleRecordSeparatorPolicy();

	private String resourceId;

	private BufferedReader reader;

	private int lineCount = 0;

	private String[] comments = new String[] { "#" };

	private boolean noInput = false;

	private String encoding = DEFAULT_CHARSET;

	private LineMapper<T> lineMapper;

	private int linesToSkip = 0;

	private LineCallbackHandler skippedLinesCallback;
	
	private int policyNum = 0;

	// private BufferedReaderFactory bufferedReaderFactory = new
	// DefaultBufferedReaderFactory();
	private ResourceReaderFactory bufferedReaderFactory;

	public IpsnItemFileReader() {
		setName(ClassUtils.getShortName(FlatFileItemReader.class));
	}

	/**
	 * @param skippedLinesCallback
	 *            will be called for each one of the initial skipped lines
	 *            before any items are read.
	 */
	public void setSkippedLinesCallback(LineCallbackHandler skippedLinesCallback) {
		this.skippedLinesCallback = skippedLinesCallback;
	}

	/**
	 * Public setter for the number of lines to skip at the start of a file. Can
	 * be used if the file contains a header without useful (column name)
	 * information, and without a comment delimiter at the beginning of the
	 * lines.
	 * 
	 * @param linesToSkip
	 *            the number of lines to skip
	 */
	public void setLinesToSkip(int linesToSkip) {
		this.linesToSkip = linesToSkip;
	}

	/**
	 * Setter for line mapper. This property is required to be set.
	 * 
	 * @param lineMapper
	 *            maps line to item
	 */
	public void setLineMapper(LineMapper<T> lineMapper) {
		this.lineMapper = lineMapper;
	}

	/**
	 * Setter for the encoding for this input source. Default value is
	 * {@link #DEFAULT_CHARSET}.
	 * 
	 * @param encoding
	 *            a properties object which possibly contains the encoding for
	 *            this input file;
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * @return the bufferedReaderFactory
	 */
	public ResourceReaderFactory getBufferedReaderFactory() {
		return bufferedReaderFactory;
	}

	/**
	 * @param bufferedReaderFactory
	 *            the bufferedReaderFactory to set
	 */
	public void setBufferedReaderFactory(ResourceReaderFactory bufferedReaderFactory) {
		this.bufferedReaderFactory = bufferedReaderFactory;
	}

	/**
	 * Setter for comment prefixes. Can be used to ignore header lines as well
	 * by using e.g. the first couple of column names as a prefix.
	 * 
	 * @param comments
	 *            an array of comment line prefixes.
	 */
	public void setComments(String[] comments) {
		this.comments = new String[comments.length];
		System.arraycopy(comments, 0, this.comments, 0, comments.length);
	}

	/**
	 * Public setter for the recordSeparatorPolicy. Used to determine where the
	 * line endings are and do things like continue over a line ending if inside
	 * a quoted string.
	 * 
	 * @param recordSeparatorPolicy
	 *            the recordSeparatorPolicy to set
	 */
	public void setRecordSeparatorPolicy(RecordSeparatorPolicy recordSeparatorPolicy) {
		this.recordSeparatorPolicy = recordSeparatorPolicy;
	}

	/**
	 * @return string corresponding to logical record according to
	 *         {@link #setRecordSeparatorPolicy(RecordSeparatorPolicy)} (might
	 *         span multiple lines in file).
	 */
	@Override
	protected T doRead() throws Exception {
		if (noInput) {
			return null;
		}
		//获取险种个数
		if (lineCount==0){
			String line1 = readLine();
			if (StringUtils.isEmpty(line1)) {
				return null;
			}
			while(line1.indexOf("险种")!=-1){
				line1 = line1.substring(line1.indexOf("险种")+2);
				policyNum++;
			}
		}
		String line = readLine();
		if (StringUtils.isEmpty(line)) {
			return null;
		}
		
		if (line.trim().matches(",+")) {
			return null;
		}
		
		line = policyNum+","+line;
		try {
			return lineMapper.mapLine(line, lineCount);
		} catch (Exception ex) {
			logger.error("csv文件解析失败");
			throw new FlatFileParseException("Parsing error at line: " + lineCount + " in resource=[" + resourceId
				+ "], input=[" + line + "],"+ex.getMessage(), ex, line, lineCount);
		}
	}

	/**
	 * @return next line (skip comments).getCurrentResource
	 */
	private String readLine() {

		if (reader == null) {
			throw new ReaderNotOpenException("Reader must be open before it can be read.");
		}

		String line = null;

		try {
			line = this.reader.readLine();
			if (line == null) {
				return null;
			}
			lineCount++;
			while (isComment(line)) {
				line = reader.readLine();
				if (line == null) {
					return null;
				}
				lineCount++;
			}

			line = applyRecordSeparatorPolicy(line);
		} catch (IOException e) {
			// Prevent IOException from recurring indefinitely
			// if client keeps catching and re-calling
			noInput = true;
			throw new NonTransientFlatFileException("Unable to read from resourceid: [" + resourceId + "]", e, line,
					lineCount);
		}
		return line;
	}

	private boolean isComment(String line) {
		for (String prefix : comments) {
			if (line.startsWith(prefix)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void doClose() throws Exception {
		lineCount = 0;
		if (reader != null) {
			reader.close();
		}
		
	}

	@Override
	protected void doOpen() throws Exception {
		Assert.notNull(resourceId, "Input resource must be set");
		Assert.notNull(recordSeparatorPolicy, "RecordSeparatorPolicy must be set");

		noInput = true;

		// if (!StringUtils.isEmpty(resourceId)) {
		// if (strict) {
		// throw new IllegalStateException("Input resource must exist (reader is
		// in 'strict' mode): " + resourceId);
		// }
		// logger.warn("Input resource does not exist " + resourceId);
		// return;
		// }

		// if (!resource.isReadable()) {
		// if (strict) {
		// throw new IllegalStateException("Input resource must be readable
		// (reader is in 'strict' mode): "
		// + resource);
		// }
		// logger.warn("Input resource is not readable " +
		// resource.getDescription());
		// return;
		// }

		reader = bufferedReaderFactory.create(resourceId, encoding);
		for (int i = 0; i < linesToSkip; i++) {
			String line = readLine();
			if (skippedLinesCallback != null) {
				skippedLinesCallback.handleLine(line);
			}
		}
		noInput = false;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(lineMapper, "LineMapper is required");
	}

	@Override
	protected void jumpToItem(int itemIndex) throws Exception {
		for (int i = 0; i < itemIndex; i++) {
			readLine();
		}
	}

	private String applyRecordSeparatorPolicy(String line) throws IOException {

		String record = line;
		while (line != null && !recordSeparatorPolicy.isEndOfRecord(record)) {
			line = this.reader.readLine();
			if (line == null) {
				if (StringUtils.hasText(record)) {
					// A record was partially complete since it hasn't ended but
					// the line is null
					throw new FlatFileParseException("Unexpected end of file before record complete", record,
							lineCount);
				} else {
					// Record has no text but it might still be post processed
					// to something (skipping preProcess since that was already
					// done)
					break;
				}
			} else {
				lineCount++;
			}
			record = recordSeparatorPolicy.preProcess(record) + line;
		}

		return recordSeparatorPolicy.postProcess(record);

	}

	/**
	 * @return the resourceId
	 */
	public String getResourceId() {
		return resourceId;
	}

	/**
	 * @param resourceId
	 *            the resourceId to set
	 */
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

}
