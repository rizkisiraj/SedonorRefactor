package com.example.sedonortdd.data.repositories;

import com.example.sedonortdd.data.models.Article;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.List;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.tasks.TasksKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
        mv = {1, 9, 0},
        k = 1,
        xi = 48,
        d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\"\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006H\u0086@ø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b\t\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b¡\u001e0\u0001¨\u0006\u000b"},
        d2 = {"Lcom/example/sedonortdd/data/repositories/ArticleRepository;", "", "firestore", "Lcom/google/firebase/firestore/FirebaseFirestore;", "(Lcom/google/firebase/firestore/FirebaseFirestore;)V", "fetchArticles", "Lkotlin/Result;", "", "Lcom/example/sedonortdd/data/models/Article;", "fetchArticles-IoAF18A", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"}
)
public final class ArticleRepository {
    @NotNull
    private final FirebaseFirestore firestore;

    public ArticleRepository(@NotNull FirebaseFirestore firestore) {
        Intrinsics.checkNotNullParameter(firestore, "firestore");
        super();
        this.firestore = firestore;
    }

    @Nullable
    public final Object fetchArticles_IoAF18A/* $FF was: fetchArticles-IoAF18A*/(@NotNull Continuation $completion) {
        Object $continuation;
        label46: {
            if ($completion instanceof <undefinedtype>) {
                $continuation = (<undefinedtype>)$completion;
                if ((((<undefinedtype>)$continuation).label & Integer.MIN_VALUE) != 0) {
                    ((<undefinedtype>)$continuation).label -= Integer.MIN_VALUE;
                    break label46;
                }
            }

            $continuation = new ContinuationImpl($completion) {
                // $FF: synthetic field
                Object result;
                int label;

                @Nullable
                public final Object invokeSuspend(@NotNull Object $result) {
                    this.result = $result;
                    this.label |= Integer.MIN_VALUE;
                    Object var10000 = ArticleRepository.this.fetchArticles-IoAF18A((Continuation)this);
                    return var10000 == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? var10000 : Result.box-impl(var10000);
                }
            };
        }

        Result.Companion var4;
        Object var12;
        Exception var10000;
        label39: {
            Object $result = ((<undefinedtype>)$continuation).result;
            Object var8 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            Object var17;
            boolean var10001;
            switch (((<undefinedtype>)$continuation).label) {
                case 0:
                    ResultKt.throwOnFailure($result);

                    try {
                        CollectionReference var3 = this.firestore.collection("articles");
                        Intrinsics.checkNotNullExpressionValue(var3, "collection(...)");
                        CollectionReference collection = var3;
                        Task var5 = collection.get();
                        Intrinsics.checkNotNullExpressionValue(var5, "get(...)");
                        ((<undefinedtype>)$continuation).label = 1;
                        var17 = TasksKt.await(var5, (Continuation)$continuation);
                    } catch (Exception var10) {
                        var10000 = var10;
                        var10001 = false;
                        break label39;
                    }

                    if (var17 == var8) {
                        return var8;
                    }
                    break;
                case 1:
                    try {
                        ResultKt.throwOnFailure($result);
                        var17 = $result;
                        break;
                    } catch (Exception var11) {
                        var10000 = var11;
                        var10001 = false;
                        break label39;
                    }
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            try {
                Object var15 = var17;
                Intrinsics.checkNotNullExpressionValue(var15, "await(...)");
                QuerySnapshot snapshot = (QuerySnapshot)var15;
                var4 = Result.Companion;
                List var16 = snapshot.toObjects(Article.class);
                var12 = Result.constructor-impl(var16);
                return var12;
            } catch (Exception var9) {
                var10000 = var9;
                var10001 = false;
            }
        }

        Exception e = var10000;
        var4 = Result.Companion;
        var12 = Result.constructor-impl(ResultKt.createFailure((Throwable)e));
        return var12;
    }
}
