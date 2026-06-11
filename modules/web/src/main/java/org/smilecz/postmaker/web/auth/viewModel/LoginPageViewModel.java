package org.smilecz.postmaker.web.auth.viewModel;

import io.micronaut.core.annotation.Introspected;
import org.smilecz.postmaker.web.auth.model.LoginPageModel;

@Introspected
public record LoginPageViewModel(LoginPageModel loginPageModel) {
}
