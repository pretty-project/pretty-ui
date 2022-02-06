;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.11
; Description:
; Version: v1.4.4
; Compatibility: x4.5.7



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.event-handler
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.format  :as format]
              [mid-fruits.map     :as map :refer [update-some]]
              [mid-fruits.random  :as random]
              [mid-fruits.string  :as string]
              [mid-fruits.time    :as time]
              [mid-fruits.vector  :as vector]
              [re-frame.core      :as re-frame]
              [re-frame.db        :refer [app-db]]
              [re-frame.loggers   :as loggers]
              [re-frame.registrar :as registrar]
              [x.mid-core.engine  :as engine]))



;; -- Index -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Helpers
; Event-param functions
; Metamorphic handler functions
; Metamorphic effects functions
; Effects-map functions
; Event-vector<-id
; Event self-destructing
; Event debugging
; Event checking
; Event registrating
; Dispatch functions
; Dispatch timing
; Dereferenced subscriptions
; Eyecandy functions



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name metamorphic-event
;  A metamorphic-event olyan formula amely lehetőve teszi, hogy egy eseményt
;  vagy esemény-csoportot event-vector, effects-map vagy handler-function
;  formában meghatározhass.
;
; @name event-vector
;  [:do-something!]
;
; @name effects-map
;  {:dispatch-later [{:ms 500 :dispatch [:do-something-later!]}]}
;
; @name handler-function
;  (fn [cofx event-vector] {:dispatch-n [[:do-something!] [:do-something-else!]]})
;
; @name metamorphic-effects
;  A metamorphic-effects olyan formula amely lehetőve teszi, hogy
;  egy handler-function visszatérési értéke effects-map vagy event-vector
;  is lehessen.
;  (fn [_ _] {:dispatch [:do-something!]})
;  (fn [_ _] [:do-something!])
;
; @name param-vector
;  [:foo :bar :baz]
;
; @name event-vector
;  [:my-event :my-param :your-param ...]
;
; @name do-something?!
;  A "?!" végződésű esemény nevek az adott esemény feltételes hatására utalnak.
;  Pl.:
;  (a/reg-event-fx
;    :do-something?!
;    (fn [_ _] (if (do-something?) {:dispatch [:do-something!]})))
;  A fenti példában a (do-something?) függvény kimenetelétől függően történik
;  meg a [:do-something!] esemény.
;
;  Ez nem vonatkozik azokra az eseményekre, amelyek a hatásuk megtörténtét
;  vizsgálják az ismételt megtörténés elkerülése érdekében.
;  Pl.:
;  (a/reg-event-fx
;    :hide-something!
;    (fn [_ _] (if-not (something-hidden?) {:dispatch [:lets-hide-something!]})))
;
; Továbbá nem vonatkozik azokra az eseményekre, amelyek egy esetleges hiba
; elkerülése érdekében történnek feltételesen.
; Pl.:
; (a/reg-event-fx
;   :do-something-with-element!
;   (fn [_ _] (if (element-exists?) {:dispatch [:do-something!]})])))
;
; @name dispatch-sync
;  TODO ...
;
; @name dispatch
;  TODO ...
;
; @name dispatch-n
;  TODO ...
;
; @name dispatch-last
;  TODO ...
;
; @name dispatch-once
;  TODO ...
;
; @name dispatch-tick
;  TODO ...
;
; @name dispatch-later
;  {:dispatch-later [{:ms 100 :dispatch [...]}
;                    {:ms 200 :dispatch-n [[...] [...]]}]}
;
; @name dispatch-if
;  TODO ...
;
; @name dispatch-cond
;  TODO ...



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; re-frame.core
(def ->interceptor re-frame/->interceptor)
(def reg-cofx      re-frame/reg-cofx)
(def reg-fx        re-frame/reg-fx)
(def reg-sub       re-frame/reg-sub)
(def subscribe     re-frame/subscribe)
(def inject-cofx   re-frame/inject-cofx)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn event-vector?
  ; Strict-mode:
  ;  Az esemény azonsítójának szintaxisa alapján képes megkülönböztetni az esemény-vektort
  ;  más vektoroktól (pl.: hiccup, subscription vektor, ...)
  ;
  ; @param (*) n
  ; @param (options)(opt)
  ;  {:strict-mode? (boolean)(opt)
  ;    Default: false}
  ;
  ; @example
  ;  (a/event-vector? [:my-namespace/do-something! ...])
  ;  =>
  ;  true
  ;
  ; @example
  ;  (a/event-vector? [:my-namespace/->something-happened ...])
  ;  =>
  ;  true
  ;
  ; @example
  ;  (a/event-vector? [:div ...] {:strict-mode? true})
  ;  =>
  ;  false
  ;
  ; @example
  ;  (a/event-vector? [:div ...] {:strict-mode? false})
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n {:keys [strict-mode?]}]
  (and (vector? n)
       (let [event-id (first n)]
            (if strict-mode? ; If strict-mode is enabled ...
                             (and (keyword? event-id)
                                  (or (-> event-id name (string/starts-with? "->"))
                                      (-> event-id name (string/ends-with?   "!"))))
                             ; If strict-mode is NOT enabled ...
                             (keyword? event-id)))))

(defn subscription-vector?
  ; @param (*) n
  ;
  ; @example
  ;  (a/subscription-vector? [:my-namespace/get-something ...])
  ;  =>
  ; true
  ;
  ; @example
  ;  (a/subscription-vector? [:my-namespace/something-happened? ...])
  ;  =>
  ; true
  ;
  ; @example
  ;  (a/subscription-vector? [:div ...])
  ;  =>
  ; false
  ;
  ; @return (boolean)
  [n]
  (and (vector? n)
       (let [event-id (first n)]
            (and (keyword? event-id)
                 (or (-> event-id name (string/starts-with? "get-"))
                     (-> event-id name (string/ends-with?   "?")))))))

(defn event-group-vector?
  ; @param (*) n
  ;
  ; @example
  ;  (a/event-group-vector? [{:ms 500 :dispatch [:my-namespace/do-something! ...]}])
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (and (vector? n)
       (let [event-map (first n)]
            (and (map? event-map)
                 (map/contains-key? event-map :dispatch)))))

(defn event-vector->param-vector
  ; @param (vector) event-vector
  ;
  ; @example
  ;  (a/event-vector->param-vector [:my-event ...])
  ;  =>
  ;  [...]
  ;
  ; @return (vector)
  [event-vector]
  (subvec event-vector 1))

(defn event-vector->event-id
  ; @param (vector) event-vector
  ;
  ; @example
  ;  (a/event-vector->event-id [:my-event ...])
  ;  =>
  ;  :my-event
  ;
  ; @return (vector)
  [event-vector]
  (first event-vector))

(defn cofx->event-vector
  ; @param (map) cofx
  ;  {:event (vector)}
  ;
  ; @example
  ;  (a/cofx->event-vector {:event [...]})
  ;  =>
  ;  [...]
  ;
  ; @return (vector)
  [cofx]
  (get cofx :event))

(defn cofx->event-id
  ; @param (map) cofx
  ;  {:event (vector)
  ;    [(keyword) event-id]}
  ;
  ; @example
  ;  (a/cofx->event-vector {:event [:my-event ...]})
  ;  =>
  ;  :my-event
  ;
  ; @return (keyword)
  [cofx]
  (get-in cofx [:event 0]))

(defn context->event-vector
  ; @param (map) context
  ;  {:coeffects (map)
  ;   {:event (vector)}}
  ;
  ; @return (vector)
  [context]
  (get-in context [:coeffects :event]))

(defn context->event-id
  ; @param (map) context
  ;
  ; @return (keyword)
  [context]
  (-> context context->event-vector event-vector->event-id))

(defn context->db-before-effect
  ; @param (map) context
  ;  {:coeffects (map)
  ;   {:db (map)}}
  ;
  ; @return (map)
  [context]
  (get-in context [:coeffects :db]))

(defn context->db-after-effect
  ; @param (map) context
  ;  {:effects (map)
  ;   {:db (map)}}
  ;
  ; @return (map)
  [context]
  (get-in context [:effects :db]))



;; -- Event param functions ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn event-vector<-params
  ; @param (event-vector) n
  ; @param (list of *) xyz
  ;
  ; @return (event-vector)
  [n & xyz]
  (vector/concat-items n xyz))

(defn metamorphic-event<-params
  ; @param (metamorphic-event) n
  ; @param (list of *) xyz
  ;
  ; @example
  ;  (a/metamorphic-event<-params [:my-event] :my-param-1 "my-param-2")
  ;  =>
  ;  [:my-event :my-param-1 "my-param-2"]
  ;
  ; @usage
  ;  (a/metamorphic-event<-params {:dispatch [:my-event]} :my-param-1 "my-param-2")
  ;  =>
  ;  {:dispatch [:my-event :my-param-1 "my-param-2"]}
  ;
  ; @return (metamorphic-event)
  [n & xyz]
  ; Szükséges megkülönböztetni az esemény vektort a dispatch-later és dispatch-tick esemény csoport vektortól!
  ; [:do-something! ...]
  ; [{:ms 500 :dispatch [:do-something! ...]}]
  (cond (event-vector?       n {:strict-mode? false}) (vector/concat-items n xyz)
        (map?                n) (map/->values n #(apply metamorphic-event<-params % xyz))
        (event-group-vector? n) (return n) ; TODO ...
        :else                   (return n)))



;; -- Metamorphic handler functions -------------------------------------------
;; ----------------------------------------------------------------------------

(defn event-vector->effects-map
  ; @param (vector) event-vector
  ;
  ; @return (map)
  [event-vector]
  {:dispatch event-vector})

(defn event-vector->handler-function
  ; @param (vector) event-vector
  ;
  ; @return (function)
  [event-vector]
  (fn [_ _] {:dispatch event-vector}))

(defn effects-map->handler-function
  ; @param (map) effects-map
  ;
  ; @return (function)
  [effects-map]
  (fn [_ _] effects-map))

(defn metamorphic-event->handler-function
  ; @param (metamorphic-event) n
  ;
  ; @return (function)
  [n]
  (cond (map?    n) (effects-map->handler-function  n)
        (vector? n) (event-vector->handler-function n)
        :else       (return n)))



;; -- Metamorphic effects functions -------------------------------------------
;; ----------------------------------------------------------------------------

(defn metamorphic-effects->effects-map
  ; @param (metamorphic-effects) n
  ;
  ; @example
  ;  (a/metamorphic-effects->effects-map [:do-something!])
  ;  =>
  ;  {:dispatch [:do-something!]}
  ;
  ; @example
  ;  (a/metamorphic-effects->effects-map {:dispatch [:do-something!])
  ;  =>
  ;  {:dispatch [:do-something!]}
  ;
  ; @return (map)
  [n]
  (if (vector?                   n)
      (event-vector->effects-map n)
      (return                    n)))



;; -- Effects-map (fx-maps) functions -----------------------------------------
;; ----------------------------------------------------------------------------

(defn effects-map<-event
  ; @param (map) effects-map
  ; @param (vector) event-vector
  ;
  ; @example
  ;  (a/effects-map<-event {:dispatch [:foo]}
  ;                        [:bar])
  ;  =>
  ;  {:dispatch [:foo] :dispatch-n [[:bar]]}
  ;
  ; @return (map)
  [effects-map event-vector]
  (update effects-map :dispatch-n vector/conj-item event-vector))

(defn merge-effects-maps
  ; @param (map) a
  ; @param (map) b
  ;
  ; @example
  ;  (a/merge-effects-maps {:dispatch [:a1]
  ;                         :dispatch-n [[:a2] [:a3]}]}
  ;                        {:dispatch [:b1]
  ;                         :dispatch-n [[:b2]]})
  ;  =>
  ;  {:dispatch [:a1] :dispatch-n [[:a2] [:a3] [:b1] [:b2]]}
  ;
  ; @return (map)
  [a b]
  (-> a (update-some :dispatch-n     vector/conj-item    (:dispatch       b))
        (update-some :dispatch-n     vector/concat-items (:dispatch-n     b))
        (update-some :dispatch-later vector/concat-items (:dispatch-later b))))



;; -- Event-vector<-id --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn event-vector<-id-f
  ; @param (map) context
  ;
  ; @return (map)
  [context]
  (get-in context [:coeffects :event])
  (letfn [(f ; @param (vector) event-vector
             ;
             ; @example
             ;  (f [:my-event :my-id {...}])
             ;  =>
             ;  [:my-event :my-id {...}]
             ;
             ; @example
             ;  (f [:my-event {...}])
             ;  =>
             ;  [:my-event :0ce14671-e916-43ab-b057-0939329d4c1b {...}]
             ;
             ; @return (vector)
             [event-vector]
             (if (->     event-vector second keyword?)
                 (return event-vector)
                 (concat [(first event-vector) (random/generate-keyword)] (rest event-vector))))]
         (update-in context [:coeffects :event] f)))

; @constant (?)
(def event-vector<-id (re-frame/->interceptor :id :core/event-vector<-id
                                              :before event-vector<-id-f))



;; -- Event self-destructing --------------------------------------------------
;; ----------------------------------------------------------------------------

; A self-destruct! interceptor használatával az esemény megsemmisíti önmagát
; az első meghívását követően, így azt többször már nem lehet meghívni.
;
; @usage
;  (reg-event-fx
;   :my-event
;   [self-destruct!]
;   (fn [cofx event-vector]
;       {:dispatch ...}))

(defn self-destruct-f
  ; @param (map) context
  ;
  ; @return (map)
  [context]
  (let [event-vector (context->event-vector  context)
        event-id     (event-vector->event-id event-vector)]
       (registrar/clear-handlers :event event-id)
       (return context)))

; @constant (?)
(def self-destruct! (re-frame/->interceptor :id :core/self-destruct!
                                            :after self-destruct-f))



;; -- Event debugging ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn debug-f
  ; @param (map) context
  ;
  ; @return (map)
  [context]
  (let [event-vector (context->event-vector context)]
       #?(:cljs (let [timestamp (-> js/performance .now time/ms->s format/decimals)]
                     (println timestamp "\n" event-vector)))
       (return context)))

; @constant (?)
(def debug! (re-frame/->interceptor :id :core/debug!
                                    :after debug-f))



;; -- Event checking ----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-event-handlers
  ; @return (map)
  ;  {:cofx (map)
  ;   :event (map)
  ;   :fx (map)
  ;   :sub (map)}
  []
  (deref registrar/kind->id->handler))

(defn get-event-handler
  ; @param (keyword) event-kind
  ;  :cofx, :event, :fx, :sub
  ; @param (keyword) event-id
  ;
  ; @usage
  ;  (get-event-handler :sub :my-subscription)
  ;
  ; @return (maps in list)
  [event-kind event-id]
  (-> (get-event-handlers)
      (get-in [event-kind event-id])))

(defn event-handler-registrated?
  ; @param (keyword) event-kind
  ;  :cofx, :event, :fx, :sub
  ; @param (keyword) event-id
  ;
  ; @usage
  ;  (event-handler-registrated? :sub :my-subscription)
  ;
  ; @return (function)
  [event-kind event-id]
  (-> (get-event-handler event-kind event-id)
      (some?)))



;; -- Event registrating ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-event-db
  ; @param (keyword) event-id
  ; @param (vector)(opt) interceptors
  ; @param (metamorphic-event) event-handler
  ([event-id event-handler]
   (reg-event-db event-id nil event-handler))

  ([event-id interceptors event-handler]
   (re-frame/reg-event-db event-id interceptors event-handler)))

(defn reg-event-fx
  ; You can registrate metamorphic-events, not only handler-functions
  ;
  ; @param (keyword) event-id
  ; @param (vector)(opt) interceptors
  ; @param (metamorphic-event) event-handler
  ;
  ; @usage
  ;  (reg-event-fx :my-event [:do-something!])
  ;
  ; @usage
  ;  (reg-event-fx :my-event {:dispatch [:do-something!]})
  ;
  ; @usage
  ;  (reg-event-fx :my-event (fn [cofx event-vector] [:do-something!]})
  ;
  ; @usage
  ;  (reg-event-fx :my-event (fn [cofx event-vector] {:dispatch [:do-something!]})
  ([event-id event-handler]
   (reg-event-fx event-id nil event-handler))

  ([event-id interceptors event-handler]
   (let [handler-function (metamorphic-event->handler-function event-handler)]
        (re-frame/reg-event-fx event-id interceptors #(metamorphic-effects->effects-map (handler-function %1 %2))))))

(defn apply-fx-params
  ; @param (function) handler-function
  ; @param (* or vector) params
  ;
  ; @usage
  ;  (apply-fx-params (fn [a] ...) "a")
  ;
  ; @usage
  ;  (apply-fx-params (fn [a] ...) ["a"])
  ;
  ; @usage
  ;  (apply-fx-params (fn [a b] ...) ["a" "b"])
  ;
  ; @return (*)
  [handler-function params]
  (if (vector?          params)
      (apply            handler-function params)
      (handler-function params)))

(defn reg-handled-fx
  ; Kezelt mellékhatás-események (Handled side-effect events)
  ;
  ; Egy időben regisztrál egy side-effect eseményt és az azt meghívó effect-event
  ; eseményt.
  ;
  ; @param (keyword) event-id
  ; @param (function) handler-function
  ;
  ; @usage
  ;  (a/reg-handled-fx :my-event (fn []))
  ;
  ; @usage
  ;  (a/reg-handled-fx :my-event (fn [a b]))
  ;
  ; @example
  ;  (a/reg-handled-fx
  ;   :my-event
  ;   (fn [a b]))
  ;
  ;  (a/reg-event-fx
  ;   :your-event
  ;   (fn [_ _]
  ;       {:my-event ["a" "b"]
  ;        :my-event "a"
  ;        :dispatch [:my-event "a" "b"]))
  [event-id handler-function]
  (re-frame/reg-fx       event-id (fn [params]         (apply-fx-params handler-function params)))
  (re-frame/reg-event-fx event-id (fn [_ event-vector] {event-id (event-vector->param-vector event-vector)})))



;; -- Dispatch functions ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn dispatch-function
  ; Dispatch a non-pure function
  ; 1. Registrate a new anonymous event-handler
  ; 2. Dispatch it
  ; 3. Remove the registrated anonymous event-handler
  ;
  ; @param (function) handler-function
  ;
  ; @usage
  ;  (dispatch-function (fn [_ _] {:dispatch [:do-something!]}))
  [handler-function]
  (let [handler-id (random/generate-keyword)]
       (re-frame/reg-event-fx handler-id [self-destruct!] handler-function)
       (re-frame/dispatch [handler-id])))

(re-frame/reg-fx :dispatch-function dispatch-function)

(defn dispatch
  ; @param (metamorphic-event) event-handler
  [event-handler]

  ; Szerver-oldalon a Re-Frame nem jelez hibát, nem regisztrált esemény meghívásakor.
  ; A szerver-oldalon nem történnek meg a nem regisztrált Re-Frame események, ezért nem lehetséges
  ; interceptor-ban vizsgálni az események regisztráltságát.
  #?(:clj (let [event-id      (event-vector->event-id     event-handler)
                event-exists? (event-handler-registrated? :event event-id)]
               (if-not event-exists? (println (str "re-frame: no :event handler registrated for: " event-id)))))

  (cond ; @usage
        ;  (dispatch [:foo])
        (vector? event-handler) (re-frame/dispatch event-handler)
        ; @usage
        ;  (dispatch {:dispatch [:foo]})
        (map? event-handler)    (-> event-handler effects-map->handler-function dispatch-function)
        ; @usage
        ;  (dispatch nil)
        (nil? event-handler)    (return :nil-handler-exception)
        ; @usage
        ;  (dispatch (fn [_ _] {:dispatch [:foo]}))
        :else                   (dispatch-function event-handler)))

(registrar/clear-handlers :fx :dispatch)
(re-frame/reg-fx              :dispatch dispatch)

(defn dispatch-sync
  ; @param (event-vector) event-handler
  ;
  ; A dispatch-sync függvény a meghívási sebesség fontossága miatt nem kezeli
  ; a metamorphic-event kezelőket!
  [event-handler]
  (re-frame/dispatch-sync event-handler))

(defn dispatch-n
  ; @param (metamorphic-events in vector) event-list
  ;
  ; @usage
  ;  (dispatch-n [[:event-a]
  ;               {:dispatch [:event-b]}
  ;               (fn [_ _] {:dispatch [:event-c]})])
  [event-list]
  (doseq [event (remove nil? event-list)]
         (dispatch event)))

(registrar/clear-handlers :fx :dispatch-n)
(re-frame/reg-fx              :dispatch-n dispatch-n)

(defn dispatch-later
  ; @param (maps in vector) event-list
  ;
  ; @usage
  ;  (a/dispatch-later [{:ms 500 :dispatch [:do-something!]}
  ;                     {:ms 600 :dispatch-n [[:do-something!]
  ;                                           [:do-something-else!]]}])
  [event-list]
  ; - Az eredeti dispatch-later függvény clojure környezetben nem időzíti a dispatch-later eseményeket!
  ; - A dispatch-f és dispatch-n-f függvénynevek használata a névütközések elkerülése miatt szükséges
  (let [dispatch-f   dispatch
        dispatch-n-f dispatch-n]
       (doseq [{:keys [ms dispatch dispatch-n]} (remove nil? event-list)]
              (cond ; Dispatch single events ...
                    (and (some?   dispatch)
                         (number? ms))
                    (do (time/set-timeout! ms #(dispatch-f dispatch)))
                    ; Dispatch multiple events ...
                    (and (some?   dispatch-n)
                         (number? ms))
                    (time/set-timeout! ms #(dispatch-n-f dispatch-n))))))

(registrar/clear-handlers :fx :dispatch-later)
(re-frame/reg-fx              :dispatch-later dispatch-later)

(defn dispatch-if
  ; @param (*) condition
  ; @param (metamorphic-event) if-event-handler
  ; @param (metamorphic-event)(opt) else-event-handler
  ;
  ; @usage
  ;  (dispatch-if [true [:my-event] ...])
  ;
  ; @usage
  ;  (dispatch-if [true {:dispatch [:my-event]} ...])
  ;
  ; @usage
  ;  (dispatch-if [true (fn [_ _] {:dispatch [:my-event]}) ...])
  [[condition if-event-handler else-event-handler]]
  (if condition (dispatch if-event-handler)
                (if else-event-handler (dispatch else-event-handler))))

(re-frame/reg-fx :dispatch-if dispatch-if)

(defn dispatch-cond
  ; @param (vector) conditional-events
  ; [(*) condition
  ;  (metamorphic-event) if-event-handler
  ;  ...]
  ;
  ; @usage
  ;  (dispatch-cond [(some? "a") [:my-event]
  ;                  (nil?  "b") [:my-event]])
  ;
  ; @usage
  ;  (dispatch-cond [(some? "a") {:dispatch [:my-event]}
  ;                  (nil?  "b") {:dispatch [:my-event]}])
  ;
  ; @usage
  ;  (dispatch-cond [(some? "a") (fn [_ _] {:dispatch [:my-event]})
  ;                  (nil?  "b") (fn [_ _] {:dispatch [:my-event]})])
  [conditional-events]
  (letfn [(dispatch-cond-f [_ dex x]
                           (if (and (even? dex) x)
                               (let [event (nth conditional-events (inc dex))]
                                    (dispatch event))))]
         (reduce-kv dispatch-cond-f nil conditional-events)))

(re-frame/reg-fx :dispatch-cond dispatch-cond)



;; -- Dispatch timing ---------------------------------------------------------
;; ----------------------------------------------------------------------------

; Ütemalapú esemény-időzítés
;
; A dispatch-later esemény-indítóhoz hasonlóan időzítve indítja az eseményeket,
; de nem ms-ban méri az időt, hanem a re-frame szerinti ütemekben (tick).
;
; @usage
;  (reg-event-fx
;   :my-event
;   (fn [_ _]
;       {:dispatch-tick
;        [{:tick 10
;          :dispatch [:my-event]}]}))

(defn dispatch-tick
  ; @param (maps in vector) effects-maps-vector
  ;  [{ ... }
  ;   {:tick 10
  ;    :dispatch       [:my-event]
  ;    :dispatch-n     [[:my-event]]
  ;    :dispatch-later [ ... ]}
  ;   { ... }]
  [effects-maps-vector]
  (re-frame/dispatch [:dispatch-tick effects-maps-vector]))

; @usage
;  (reg-event-fx
;   :my-event
;   (fn [_ _]
;       {:dispatch-tick
;        [{:tick 10
;          :dispatch [:my-event]}]}))
(re-frame/reg-fx :dispatch-tick dispatch-tick)

(re-frame/reg-event-fx
  :dispatch-tick
  ; @param (maps in vector) effects-maps-vector
  ;  [{ ... }
  ;   {:tick 10
  ;    :dispatch       [:my-event]
  ;    :dispatch-n     [[:my-event]]
  ;    :dispatch-later [ ... ]}
  ;
  ;   { ... }]
  (fn [_ [_ effects-maps-vector]]
      (letfn [(f [merged-effects-map effects-map]
                 (if ; Tick now?
                     (= 0 (:tick effects-map))
                     ; Tick now!
                     (merge-effects-maps merged-effects-map effects-map)
                     ; Tick later!
                     (update merged-effects-map :dispatch-tick vector/conj-item (update effects-map :tick dec))))]
             (reduce f {} effects-maps-vector))))



;; -- Dereferenced subscriptions ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn subscribed
  ; @param (subscription-vector) subscriber
  ;
  ; @usage
  ;  (a/subscribed [:my-subscription])
  ;
  ; @return (*)
  [subscriber]
  (-> subscriber re-frame/subscribe deref))



;; -- Eyecandy functions ------------------------------------------------------
;; ----------------------------------------------------------------------------

; - A Re-Frame események függvényei a db paraméter után egy paraméter-vektort fogadnak,
;   amelynek az első eleme az esemény azonosítója, ami szinte kivétel nélkül soha
;   nincs használva.
; - A Re-Frame függvényeket a következő formula szerint lehetséges használni:
;   (db/trim-partition! db [_ partition-id])
; - Egyszerűbb, ha nem kell ezzel a vektorral baszakodni, ezért létezik
;   az r nevű függvény, amelynek az egyetlen feladata, hogy egy másik
;   formula szerint is használhatóvá teszi a Re-frame események függvényeit:
;   (r db/trim-partition! db partition-id)
(defn r
  ; @param (function) f
  ; @param (*) params
  ;
  ; @example
  ;  (r db/trim-partition! db partition-id)
  ;  =>
  ;  (db/trim-partition! db [_ partition-id])
  ;
  ; @return (*)
  [f & params]
  (let [context      (first params)
        event-vector (vector/cons-item (rest params) nil)]
       (f context event-vector)))
