package riccardogulin.u5d9.config;


import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

	@Bean
	public Cloudinary getImageUploader(@Value("${cloudinary.name}") String cloudName,
	                                   @Value("${cloudinary.key}") String apiKey,
	                                   @Value("${cloudinary.secret}") String apiSecret) {
		// VERIFICATE SEMPRE CHE I VALUE VENGANO PRESI CORRETTAMENTE DA APPLICATION PROPERTIES (Basta un system out)
		Map<String, String> config = new HashMap<>();
		config.put("cloud_name", cloudName);
		config.put("api_key", apiKey);
		config.put("api_secret", apiSecret);
		return new Cloudinary(config);
	}
}
