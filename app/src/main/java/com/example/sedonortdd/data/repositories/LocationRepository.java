package com.example.sedonortdd.data.repositories;

import com.example.sedonortdd.data.models.Location;
import com.google.android.gms.tasks.Task;
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
        d2 = {"Lcom/example/sedonortdd/data/repositories/LocationRepository;", "", "firestore", "Lcom/google/firebase/firestore/FirebaseFirestore;", "(Lcom/google/firebase/firestore/FirebaseFirestore;)V", "fetchLocations", "Lkotlin/Result;", "", "Lcom/example/sedonortdd/data/models/Location;", "fetchLocations-IoAF18A", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"}
)
public final class LocationRepository {
    @NotNull
    private final FirebaseFirestore firestore;

    public LocationRepository(@NotNull FirebaseFirestore firestore) {
        Intrinsics.checkNotNullParameter(firestore, "firestore");
        super();
        this.firestore = firestore;
    }

    @Nullable
    public final Object fetchLocations_IoAF18A/* $FF was: fetchLocations-IoAF18A*/(@NotNull Continuation $completion) {
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
                    Object var10000 = LocationRepository.this.fetchLocations-IoAF18A((Continuation)this);
                    return var10000 == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? var10000 : Result.box-impl(var10000);
                }
            };
        }

        Object var2;
        Exception var10000;
        label39: {
            Object $result = ((<undefinedtype>)$continuation).result;
            Object var7 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            Object var16;
            boolean var10001;
            switch (((<undefinedtype>)$continuation).label) {
                case 0:
                    ResultKt.throwOnFailure($result);

                    try {
                        Task var4 = this.firestore.collection("locations").get();
                        Intrinsics.checkNotNullExpressionValue(var4, "get(...)");
                        ((<undefinedtype>)$continuation).label = 1;
                        var16 = TasksKt.await(var4, (Continuation)$continuation);
                    } catch (Exception var9) {
                        var10000 = var9;
                        var10001 = false;
                        break label39;
                    }

                    if (var16 == var7) {
                        return var7;
                    }
                    break;
                case 1:
                    try {
                        ResultKt.throwOnFailure($result);
                        var16 = $result;
                        break;
                    } catch (Exception var10) {
                        var10000 = var10;
                        var10001 = false;
                        break label39;
                    }
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            try {
                Object var12 = var16;
                Intrinsics.checkNotNullExpressionValue(var12, "await(...)");
                QuerySnapshot snapshot = (QuerySnapshot)var12;
                Result.Companion var13 = Result.Companion;
                List var15 = snapshot.toObjects(Location.class);
                var2 = Result.constructor-impl(var15);
                return var2;
            } catch (Exception var8) {
                var10000 = var8;
                var10001 = false;
            }
        }

        Exception e = var10000;
        Result.Companion var14 = Result.Companion;
        var2 = Result.constructor-impl(ResultKt.createFailure((Throwable)e));
        return var2;
    }
}
