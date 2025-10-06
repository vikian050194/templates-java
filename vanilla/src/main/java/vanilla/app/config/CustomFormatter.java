package vanilla.app.config;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

class CustomFormatter extends Formatter {

    final SimpleDateFormat loggerDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    @Override
    public String format(LogRecord lr) {
        var message = lr.getMessage();
        var params = lr.getParameters();

        if (params != null && params.length > 0) {
            message = MessageFormat.format(message, params);
        }

        String formattedDate = loggerDateFormat.format(Date.from(lr.getInstant()));

        // TODO refactor this complex string formatting and use join
        // TODO what is the best order of chunks ?
        return String.format("%d::%s::%s::%s::%s::%s::%s%n",
                lr.getLongThreadID(), lr.getLoggerName(), lr.getLevel(), lr.getSourceClassName(), lr.getSourceMethodName(), formattedDate, message);
    }
}
