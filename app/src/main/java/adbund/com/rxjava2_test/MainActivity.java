package adbund.com.rxjava2_test;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;


/*
 *author:殴打小熊猫 2016/12/21 16:51
 * 备注：hope it can help u
 * thx :http://blog.csdn.net/u012124438/article/details/53730717
 */
public class MainActivity extends AppCompatActivity {

    EditText et_EditText;
    TextView tv_TextView;
    ImageView iv_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_EditText = (EditText) findViewById(R.id.et_EditText);
        et_EditText.setVisibility(View.GONE);
        tv_TextView = (TextView) findViewById(R.id.tv_TextView);
        iv_logo = (ImageView) findViewById(R.id.iv_logo);


        //----------------------------------------Base----------------------------------------------
//        a();

//        b();

//        c();

//        d();

//        e();

//        f();

//        g();  //note: 观察者没有走onComplete和onError

//        h();

//        i();

//        j();

//        k();


        //----------------------------------------Operator------------------------------------------


//        map();

//        map1();

//        flatMap();//不一定保证上游发射的顺序发送

//        concatMap();//和flatMap功能基本一样，唯一的区别在于它保证上游发射过来的顺序发送

//        zip();

//        zip1();//从这里能看出其本质

//        filter();

//        take();

//        filterAndTakeLst();

//        doOnNext();//处理下一个事件之前做某些事，可以调用该方法(当然还有其他方式)

//        debounce();//指定过滤事件的时间间隔

//        compose();

//        first(); //原博客写的有点小问题

//        singleTestInteger();//it can only emit either a single successful

//        singleTest();//it can only emit either a single successful


        //用户在editText输入文字，在下面的textView显示用户输入的文字（模拟实时搜索）
//        singleAndDebounce();//原文有错误，必须指定线程

//        merge();//不保证顺序（保证顺序用concat）

//        sample();




        //----------------------------------------RxAndroid------------------------------------------
        //anther demo

    }

    private void sample() {

        Observable
                .just("1", "2", "3", "4", "5", "3", "3", "6", "1","2")
//                .count()//统计发射数据的项目
                .map(s -> Integer.parseInt(s))
                .filter(integer -> integer.intValue() % 2 != 0)
                .distinct()
                .takeLast(2)
                .reduce((integer, integer2) -> {
                    Log.e("TAG", "integer : " + integer + " , integer2 : " + integer2);
                    return integer + integer2;
                })
                .delay(3,TimeUnit.SECONDS)
                .subscribe(integer -> Log.e("TAG", "integer : " + integer));

    }

    private void merge() {
        Observable<String> observable1 = Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        e.onNext("hi");
                        e.onNext("merge");
                    }
                })
                .subscribeOn(Schedulers.newThread());

        Observable<Integer> observable2 = Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        e.onNext(1);
                        e.onNext(2);
                        e.onNext(3);
                        e.onNext(4);
                    }
                })
                .subscribeOn(Schedulers.newThread());


        Observable
                .merge(observable1, observable2)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e("TAG", "onSubscribe");
                    }

                    @Override
                    public void onNext(Object value) {
                        Log.e("TAG", "value : " + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG", "onComplete");
                    }
                });

    }

    private void singleAndDebounce() {
        PublishSubject<String> subject = PublishSubject.create();

        subject
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e("TAG", "onSubscribe");
                    }

                    @Override
                    public void onNext(String value) {
                        Log.e("TAG", "onNext -> " + value);
                        tv_TextView.setText(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "onError");
                        tv_TextView.setText("onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG", "onComplte");
                    }
                });

        et_EditText.setVisibility(View.VISIBLE);
        et_EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e("TAG", "beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("TAG", "onTextChanged -> s: " + s + " start : " + start + " before : " + before + " count : " + count);
                subject.onNext(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("TAG", "afterTextChanged -> " + s);
            }
        });

    }

    private void singleTest() {
        Single  //it can only emit either a single successful
                .create(e -> e.onSuccess("hi ,single"))
                .subscribe(new SingleObserver<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e("TAG", "onSubscribe");
                    }

                    @Override
                    public void onSuccess(Object value) {
                        Log.e("TAG", "onSuccess -> " + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "onError");
                    }
                });
    }

    private void singleTestInteger() {
        Single   //it can only emit either a single successful
                .create(new SingleOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(SingleEmitter<Integer> e) throws Exception {
                        e.onSuccess(1);//yes
                        e.onSuccess(2);//nop
                        e.onSuccess(3);//nop
                        e.onSuccess(4);//nop
                        e.onSuccess(5);//nop
                    }
                })
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e("TAG", "onSubscribe");
                    }

                    @Override
                    public void onSuccess(Integer value) {
                        Log.e("TAG", "onSuccess : " + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "onError");

                    }
                });
    }

    private void first() {
        //以三级缓存为例

        //从缓存中获取
        Observable<BookList> dataFromDisk = Observable.create(new ObservableOnSubscribe<BookList>() {
            @Override
            public void subscribe(ObservableEmitter<BookList> e) throws Exception {
                BookList list = getFromDisk();
                if (list != null) {
                    e.onNext(list);
                    e.onComplete();
                } else {
//                    e.onError(new Throwable("no data of disk"));//会报错
                    e.onComplete();
                    Log.e("TAG", "data from disk -> else-> no data ->onComplete");
                }
            }

            //返回null，模拟从本没找到
            private BookList getFromDisk() {
                Log.e("TAG", " data from disk ,and return null");
                return null;
            }
        });

        //从网络中获取

        Observable<BookList> dataFromNet = Observable.create(new ObservableOnSubscribe<BookList>() {
            @Override
            public void subscribe(ObservableEmitter<BookList> e) throws Exception {
                BookList bookList = getDataFromNet();
                if (bookList != null) {
                    e.onNext(bookList);
                    e.onComplete();
                } else {
//                    e.onError(new Throwable("no data of net"));//如果返回onError则会报错
                    e.onComplete();
                    Log.e("TAG", "data from net ->else ->onComplete");
                }
            }
        });


        Observable
                .concat(dataFromDisk, dataFromNet)
                .first(new BookList("default book", 0, 0))//如果都没有找到，返回notnull类型的默认值(RxJava新增)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bookList -> Log.e("TAG", "bookList -> " + bookList.toString()));


    }

    private BookList getDataFromNet() {
        return new BookList("book from net ", 7, 287.58);
//        return null;
    }

    private void compose() {
        //see: http://gank.io/post/560e15be2dca930e00da1083#toc_20
    }

    private void debounce() {
        Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        int i = 0;
                        int[] times = new int[]{100, 1000};
                        while (true) {
                            i++;
                            if (i >= 100) break;
                            // 注意！！！！
                            // 当i为奇数时，休眠1000ms，然后才发送i+1，这时i不会被过滤掉
                            // 当i为偶数时，只休眠100ms，便发送i+1，这时i会被过滤掉
                            e.onNext(i);
                            Thread.sleep(times[i % 2]);
                        }
                        e.onComplete();
                    }
                })
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e("TAG", "d : " + d);
                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.e("TAG", "value : " + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "e : " + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG", "onComplete");
                    }
                });

    }

    private void doOnNext() {
        Observable
                .fromArray(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12})
                .filter(integer -> integer % 2 == 0)
                .take(3)
                .doOnNext(integer -> Log.e("TAG", "doOnNext : " + integer))
                .subscribe(integer -> Log.e("TAG", "integer : " + integer));
    }

    private void filterAndTakeLst() {
        Observable
                .just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .filter(integer -> integer % 2 == 0)
                .takeLast(3)
                .subscribe(integer -> Log.e("TAG", "integer : " + integer));
    }

    private void take() {
        Observable
                .just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .take(3)     //takeLast(3)
                .subscribe(integer -> Log.e("TAG", "integer : " + integer));
    }

    private void filter() {
        Observable
                .fromArray(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9})
                .filter(integer -> integer % 2 == 0)
                .subscribe(integer -> Log.e("TAG", "integer : " + integer));

    }

    private void zip1() {

        Observable<Object> observable1 = Observable
                .create(e -> {
                    Log.e("TAG", "emitter 1");
                    e.onNext(1);
                    Log.e("TAG", "emitter 2");
                    e.onNext(2);
                    Log.e("TAG", "emitter 3");
                    e.onNext(3);
                    Log.e("TAG", "emitter 4");
                    e.onNext(4);
                    Log.e("TAG", "complete 1");
                    e.onComplete();
                })
                .subscribeOn(Schedulers.io());

        Observable<Object> observable2 = Observable
                .create(e -> {
                    Log.e("TAG", "emitter A");
                    e.onNext("A");
                    Log.e("TAG", "emitter B");
                    e.onNext("B");
                    Log.e("TAG", "emitter C");
                    e.onNext("C");
                    Log.e("TAG", "complete 2");
                    e.onComplete();
                })
                .subscribeOn(Schedulers.newThread());

        Observable
                .zip(observable1, observable2, (integer, s) -> integer + "" + s)//原博客写的是有点问题的
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                               @Override
                               public void onSubscribe(Disposable d) {
                                   Log.e("TAG", "onSubscribe");
                               }

                               @Override
                               public void onNext(String value) {
                                   Log.e("TAG", "onNext : " + value);
                               }

                               @Override
                               public void onError(Throwable e) {
                                   Log.e("TAG", "e : " + e);
                               }

                               @Override
                               public void onComplete() {
                                   Log.e("TAG", "onComplete");
                               }
                           }

                );
    }

    private void zip() {
        Observable<Object> observable1 = Observable
                .create(e -> {
                    Log.e("TAG", "emitter 1");
                    e.onNext(1);
                    Log.e("TAG", "emitter 2");
                    e.onNext(2);
                    Log.e("TAG", "emitter 3");
                    e.onNext(3);
                    Log.e("TAG", "emitter 4");
                    e.onNext(4);
                    Log.e("TAG", "complete 1");
                    e.onComplete();
                });

        Observable<Object> observable2 = Observable
                .create(e -> {
                    Log.e("TAG", "emitter A");
                    e.onNext("A");
                    Log.e("TAG", "emitter B");
                    e.onNext("B");
                    Log.e("TAG", "emitter C");
                    e.onNext("C");
                    Log.e("TAG", "complete 2");
                    e.onComplete();
                });

        Observable.zip(observable1, observable2, (integer, s) -> integer + "" + s)//原博客写的是有点问题的
                .subscribe(new Observer<String>() {
                               @Override
                               public void onSubscribe(Disposable d) {
                                   Log.e("TAG", "onSubscribe");
                               }

                               @Override
                               public void onNext(String value) {
                                   Log.e("TAG", "onNext : " + value);
                               }

                               @Override
                               public void onError(Throwable e) {
                                   Log.e("TAG", "e : " + e);
                               }

                               @Override
                               public void onComplete() {
                                   Log.e("TAG", "onComplete");
                               }
                           }

                );

    }

    private void concatMap() {

        Observable
                .create(e -> {
                    e.onNext(1);
                    e.onNext(2);
                    e.onNext(3);
                    e.onNext(4);
                    e.onComplete();
                })
                .concatMap(integer -> {
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        list.add("value : " + integer);
                    }
                    return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);
                })
                .subscribe(s -> Log.e("TAG", "s : " + s));
    }

    private void flatMap() {
        Observable
                .create(e -> {
                    e.onNext(1);
                    e.onNext(2);
                    e.onNext(3);
                    e.onNext(4);
                    e.onComplete();
                })
                .flatMap(integer -> {
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        list.add("value : " + integer);
                    }
                    return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);
                })
                .subscribe(s -> Log.e("TAG", "s : " + s));

    }

    private void map1() {
        Observable
                .create((ObservableOnSubscribe<String>) e -> {
                    e.onNext("https://ss2.baidu.com/-vo3dSag_xI4khGko9WTAnF6hhy/image/h%3D200/sign=4db5130a073b5bb5a1d727fe06d2d523/cf1b9d16fdfaaf51965f931e885494eef11f7ad6.jpg");
                    e.onComplete();
                })
                .map(s -> Drawable.createFromStream(new URL(s).openStream(), "src"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(drawable -> iv_logo.setImageDrawable(drawable));//note:drawable可能为Null(官方需要在这里改进，这里不应该人为去判断)
    }

    private void map() {
        Observable
                .create((ObservableOnSubscribe<Integer>) e -> {
                    e.onNext(1);
                    e.onNext(2);
                    e.onNext(3);
                    e.onNext(4);
                    e.onComplete();
                })
                .map(integer -> "This is the result : " + integer)
                .subscribe(s -> Log.e("TAG", "s : " + s));

    }


    private void k() {
        Observable
                .create(new ObservableOnSubscribe<Drawable>() {
                    @Override
                    public void subscribe(ObservableEmitter<Drawable> e) throws Exception {
//                        Drawable drawable=Drawable.createFromStream(new URL("http://image.so.com/v?q=%E5%A6%B9%E5%AD%90&src=360pic_strong&fromurl=http%3A%2F%2Fwww.ik123.com%2Fq%2Ftuku%2Fkeai%2F17160.html#q=%E5%A6%B9%E5%AD%90&src=360pic_strong&fromurl=http%3A%2F%2Fwww.ik123.com%2Fq%2Ftuku%2Fkeai%2F17160.html&lightboxindex=4&id=b42eb3a7eca208384980069fb9d5a59f&multiple=0&itemindex=0&dataindex=6").openStream(), "src");
                        Drawable drawable = Drawable.createFromStream(new URL("https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2502144641,437990411&fm=80&w=179&h=119&img.JPEG").openStream(), "src");
                        Log.e("TAG", "drawable : " + drawable);//note:drawable may be null
                        e.onNext(drawable);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Drawable>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e("TAG", "onSubscribe");
                    }

                    @Override
                    public void onNext(Drawable value) {
                        Log.e("TAG", "onNext : " + value);
                        iv_logo.setImageDrawable(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "e : " + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG", "onComplete");

                    }
                });
    }

    private void j() {
        Observable<Integer> observable = Observable.create(e -> {
            Log.e("TAG", "Observable thread is : " + Thread.currentThread().getName());
            Log.e("TAG", "emitter 1");
            e.onNext(1);
        });

        Consumer<Integer> consumer = integer -> {
            Log.e("TAG", "observer thread is : " + Thread.currentThread().getName());
            Log.e("TAG", "onNext : " + integer);
        };

        observable
                .subscribeOn(Schedulers.newThread())//就近原则
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
        //就近原则(note : 原博客作者写错了，首先并不是执行第一次指定的线程，而是遵循就近原则)
        //其次在RxJava 2.0 里，删除了Schedulers.immediate()
    }

    private void i() {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                Log.e("TAG", "Observable thread is : " + Thread.currentThread().getName());
                Log.e("TAG", "emitter 1");
                e.onNext(1);
            }
        });

        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e("TAG", "observer thread is : " + Thread.currentThread().getName());
                Log.e("TAG", "onNext : " + integer);
            }
        };


        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);

    }

    private void h() {
        Observable
                .create((ObservableOnSubscribe<Integer>) emitter -> {
                    Log.e("TAG", "emitter 1");
                    emitter.onNext(1);
                    Log.e("TAG", "emitter 2");
                    emitter.onNext(2);
                    Log.e("TAG", "emitter 3");
                    emitter.onNext(3);
                    Log.e("TAG", "emitter 4");
                    emitter.onNext(4);
                })
                .subscribe(integer -> Log.e("TAG", "onNext :" + integer));
    }

    private void g() {
        Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        Log.e("TAG", "emitter 1");
                        e.onNext(1);
                        Log.e("TAG", "emitter 2");
                        e.onNext(2);
                        Log.e("TAG", "emitter 3");
                        e.onNext(3);
                        Log.e("TAG", "emitter 4");
                        e.onNext(4);

                        e.onComplete();

                        Log.e("TAG", "emitter 5");
                        e.onNext(5);
                    }
                })
                .subscribe(new Observer<Integer>() {
                    private Disposable l_Disposable;
                    private int i;

                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e("TAG", "onSubscribe");
                        l_Disposable = d;
                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.e("TAG", "onNext : " + value);
                        i++;
                        if (i == 2) {
                            Log.e("TAG", "dispposable");
                            l_Disposable.dispose();
                            Log.e("TAG", "isDispose : " + l_Disposable.isDisposed());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG", "onComplete");

                    }
                });
    }

    private void f() {
        Observable
                .create(new ObservableOnSubscribe<Integer>() {
                            @Override
                            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                                e.onNext(5);
                                e.onNext(4);
                                e.onNext(3);
                                e.onNext(2);
                                e.onNext(1);

                                e.onError(new NullPointerException());
                                e.onComplete();

                            }
                        }


                )
                .subscribe(new Observer<Integer>() {
                    Disposable l_Disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e("TAG", "onSubscribe");
                        l_Disposable = d;
                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.e("TAG", "value : " + value);
//                if (value < 3 && l_Disposable.isDisposed() == true) {
//                    l_Disposable.dispose();
//                }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "e : " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG", "onComplete");
                    }
                });
    }

    private void e() {
        Observable
                .create(new ObservableOnSubscribe<Drawable>() {
                    @Override
                    public void subscribe(ObservableEmitter<Drawable> e) throws Exception {
                        //get a pic as a drawable from mipmap
                        Drawable drawable = ContextCompat.getDrawable(MainActivity.this, R.mipmap.ic_launcher);

                        e.onNext(drawable);
                        e.onComplete();
                    }
                })
                .subscribe(new Observer<Drawable>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e("TAG", "onSubscribe");
                    }

                    @Override
                    public void onNext(Drawable value) {
                        iv_logo.setImageDrawable(value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void d() {
        Observable<String> observable = Observable.just("Hi", "RxJava2");
        Action complete = () -> Log.e("TAG", "onComplete");
        Consumer<String> next = s -> Log.e("TAG", "s --> " + s);
        Consumer<Throwable> error = e -> Log.e("TAG", "e : " + e);
        observable.subscribe(next, error, complete);
    }

    private void c() {
        Observable.just("Hi", "RxJava2")
                .subscribe(s -> Log.e("TAG", " s : " + s));
    }

    private void b() {
        Observable
                .create((ObservableOnSubscribe<Integer>) e -> {
                    e.onNext(5);
                    e.onNext(4);
                    e.onNext(3);
                    e.onNext(2);
                    e.onNext(1);
                    e.onComplete();
                })
                .subscribe(integer -> Log.e("TAG", "value: " + integer));
    }

    private void a() {
        Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        e.onNext(5);
                        e.onNext(4);
                        e.onNext(3);
                        e.onNext(2);
                        e.onNext(1);
                        e.onComplete();
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e("TAG", "onSubscribe ");
                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.e("TAG", "value : " + value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "error");

                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG", "onComplete");

                    }
                });

    }
}
