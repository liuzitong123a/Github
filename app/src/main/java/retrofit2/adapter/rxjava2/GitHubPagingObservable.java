package retrofit2.adapter.rxjava2;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.Response;

final class GitHubPagingObservable<GitHubPagingBody> extends Observable<GitHubPagingBody> {
    private final Observable<Response<GitHubPagingBody>> upstream;

    GitHubPagingObservable(Observable<Response<GitHubPagingBody>> upstream) {
        this.upstream = upstream;
    }

    @Override
    protected void subscribeActual(Observer<? super GitHubPagingBody> observer) {
        upstream.subscribe(new GitHubPagingObserver<>(observer));
    }

    @SuppressWarnings("deprecation")
    private static class GitHubPagingObserver<GitHubPagingBody>
            implements Observer<Response<GitHubPagingBody>> {
        private final Observer<? super GitHubPagingBody> observer;
        private boolean terminated;

        GitHubPagingObserver(Observer<? super GitHubPagingBody> observer) {
            this.observer = observer;
        }

        @Override
        public void onSubscribe(Disposable disposable) {
            observer.onSubscribe(disposable);
        }

        @Override
        public void onNext(Response<GitHubPagingBody> response) {
            if (response.isSuccessful()) {
                GitHubPaging<?> paging;
                if (response.body() instanceof GitHubPaging) {
                    paging = (GitHubPaging<?>) response.body();
                } else if (response.body() instanceof PagingWrapper) {
                    paging = ((PagingWrapper) response.body()).getPaging();
                } else {
                    throw new IllegalArgumentException("response.body type error: " + response.body().getClass());
                }
                String link = response.headers().get("link");
                if (link != null && paging != null) {
                    paging.setupLinks(link);
                }
                observer.onNext(response.body());
            } else {
                terminated = true;
                Throwable t = new HttpException(response);
                try {
                    observer.onError(t);
                } catch (Throwable inner) {
                    Exceptions.throwIfFatal(inner);
                    RxJavaPlugins.onError(new CompositeException(t, inner));
                }
            }
        }

        @Override
        public void onComplete() {
            if (!terminated) {
                observer.onComplete();
            }
        }

        @Override
        public void onError(Throwable throwable) {
            if (!terminated) {
                observer.onError(throwable);
            } else {
                // This should never happen! onNext handles and forwards errors automatically.
                Throwable broken = new AssertionError(
                        "This should never happen! Report as a bug with the full stacktrace.");
                //noinspection UnnecessaryInitCause Two-arg AssertionError constructor is 1.7+ only.
                broken.initCause(throwable);
                RxJavaPlugins.onError(broken);
            }
        }
    }
}


