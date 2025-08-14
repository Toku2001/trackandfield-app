package io.github.Toku2001.trackandfieldapp.dto.password;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewPasswordResponse {
	
	private String responseMessage;
	private boolean isUpdate;
	
}
