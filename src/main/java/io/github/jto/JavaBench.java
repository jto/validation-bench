package io.github.jto;

import java.util.Set;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import javax.validation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.jto.models.Track;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.joda.time.DateTime;
import org.joda.time.format.*;

import java.io.IOException;

public class JavaBench {

	public static class DateTimeDeserializer extends JsonDeserializer<DateTime> {

		private DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

		@SuppressWarnings("deprecation")
		@Override
		public DateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			JsonToken t = jp.getCurrentToken();


			if (t == JsonToken.VALUE_STRING) {
				String str = jp.getText().trim();
				return df.parseDateTime(str);
			}
        // TODO: in 2.4, use 'handledType()'
			throw ctxt.mappingException(DateTime.class, t);
		}
	}

	public static class Res{
		public int valid;
		public int invalid;

		public Res(int v, int i) {
			this.valid = v;
			this.invalid = i;
		}
	}

	public Res run(java.io.File res, int take) throws IOException{
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();

		int valid = 0;
		int invalid = 0;

		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(DateTime.class, new DateTimeDeserializer());
		mapper.registerModule(module);

		InputStream is = new FileInputStream(res);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		int i = 0;
		while ((line = br.readLine()) != null && i <= take) {
			i++;
			Track t = mapper.readValue(line, Track.class);
			Set<ConstraintViolation<Track>> constraintViolations = validator.validate(t);
			if(constraintViolations.isEmpty())
					valid++;
			else
					invalid++;
		}
		br.close();

		return new Res(valid, invalid);
	}
}