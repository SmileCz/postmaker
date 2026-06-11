package org.smilecz.postmaker.web.auth.viewModel;

import io.micronaut.core.annotation.Introspected;
import org.smilecz.postmaker.web.auth.model.RegisterPageModel;

@Introspected
public record RegisterPageViewModel(RegisterPageModel registerPageModel) {
}
