;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.11
; Description:
; Version: v1.6.0
; Compatibility: x4.6.1



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
              [re-frame.core      :as re-frame.core]
              [re-frame.loggers   :refer [console]]
              [re-frame.registrar :as re-frame.registrar]))



;; -- Index -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Helpers
; Event-param functions
; Metamorphic handler functions
; Effects-map functions
; Event-vector<-id
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
;  vagy esemény-csoportot event-vector vagy effects-map formában is meghatározhass.
;  (a/dispatch [...])
;  (a/dispatch {:dispatch [...]})
;
; @name event-vector
;  [:do-something!]
;
; @name effects-map
;  {:dispatch-later [{:ms 500 :dispatch [:do-something-later!]}]}
;
; @name metamorphic-handler
;  A metamorphic-handler olyan formula amely lehetőve teszi, hogy egy handler-f függvény
;  helyett regisztrálhass event-vector vektort vagy effects-map térképet, illetve
;  a handler-f függvény visszatérési értéke lehet event-vector vagy effects-map egyaránt.
;  (a/reg-event-fx :my-effects            [...])
;  (a/reg-event-fx :my-effects {:dispatch [...]})
;  (a/reg-event-fx :my-effects (fn [_ _]            [...]))
;  (a/reg-event-fx :my-effects (fn [_ _] {:dispatch [...]}))
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
(def ->interceptor re-frame.core/->interceptor)
(def reg-cofx      re-frame.core/reg-cofx)
(def reg-sub       re-frame.core/reg-sub)
(def subscribe     re-frame.core/subscribe)
(def inject-cofx   re-frame.core/inject-cofx)



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
                                  (-> event-id name (string/ends-with?   "!")))
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
  (cond (event-vector? n {:strict-mode? false}) (vector/concat-items n xyz)
        (map?          n) (map/->values n #(apply metamorphic-event<-params % xyz))
        :else             (return n)))



;; -- Metamorphic handler functions -------------------------------------------
;; ----------------------------------------------------------------------------

(defn event-vector->effects-map
  ; @param (vector) event-vector
  ;
  ; @usage
  ;  (a/event-vector->effects-map [...])
  ;  =>
  ;  {:dispatch [...]}
  ;
  ; @return (map)
  [event-vector]
  {:dispatch event-vector})

(defn event-vector->handler-f
  ; @param (vector) event-vector
  ;
  ; @usage
  ;  (a/event-vector->handler-f [...])
  ;  =>
  ;  (fn [_ _] {:dispatch [...]})
  ;
  ; @return (function)
  [event-vector]
  (fn [_ _] {:dispatch event-vector}))

(defn effects-map->handler-f
  ; @param (map) effects-map
  ;
  ; @usage
  ;  (a/effects-map->handler-f {:dispatch [...]})
  ;  =>
  ;  (fn [_ _] {:dispatch [...]})
  ;
  ; @return (function)
  [effects-map]
  (fn [_ _] effects-map))

(defn metamorphic-handler->handler-f
  ; @param (metamorphic-event) n
  ;
  ; @usage
  ;  (a/metamorphic-handler->handler-f [...])
  ;  =>
  ;  (fn [_ _] {:dispatch [...]})
  ;
  ; @usage
  ;  (a/metamorphic-handler->handler-f {:dispatch [...]})
  ;  =>
  ;  (fn [_ _] {:dispatch [...]})
  ;
  ; @usage
  ;  (a/metamorphic-handler->handler-f (fn [_ _] ...))
  ;  =>
  ;  (fn [_ _] ...})
  ;
  ; @return (function)
  [n]
  (cond (map?    n) (effects-map->handler-f  n)
        (vector? n) (event-vector->handler-f n)
        :else       (return n)))

(defn metamorphic-event->effects-map
  ; @param (metamorphic-effects) n
  ;
  ; @example
  ;  (a/metamorphic-event->effects-map [:do-something!])
  ;  =>
  ;  {:dispatch [:do-something!]}
  ;
  ; @example
  ;  (a/metamorphic-event->effects-map {:dispatch [:do-something!])
  ;  =>
  ;  {:dispatch [:do-something!]}
  ;
  ; @return (map)
  [n]
  (cond (vector? n) (event-vector->effects-map n)
        (map?    n) (return                    n)))



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
(def event-vector<-id (re-frame.core/->interceptor :id :core/event-vector<-id
                                                   :before event-vector<-id-f))



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
(def debug! (re-frame.core/->interceptor :id :core/debug!
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
  (deref re-frame.registrar/kind->id->handler))

(defn get-event-handler
  ; @param (keyword) event-kind
  ;  :cofx, :event, :fx, :sub
  ; @param (keyword) event-id
  ;
  ; @usage
  ;  (a/get-event-handler :sub :my-subscription)
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
  ;  (a/event-handler-registrated? :sub :my-subscription)
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
   (re-frame.core/reg-event-db event-id interceptors event-handler)))

(defn reg-event-fx
  ; You can registrate metamorphic-events, not only handler-functions
  ;
  ; @param (keyword) event-id
  ; @param (vector)(opt) interceptors
  ; @param (metamorphic-event) event-handler
  ;
  ; @usage
  ;  (a/reg-event-fx :my-event [:do-something!])
  ;
  ; @usage
  ;  (a/reg-event-fx :my-event {:dispatch [:do-something!]})
  ;
  ; @usage
  ;  (a/reg-event-fx :my-event (fn [cofx event-vector] [:do-something!]})
  ;
  ; @usage
  ;  (a/reg-event-fx :my-event (fn [cofx event-vector] {:dispatch [:do-something!]})
  ([event-id event-handler]
   (reg-event-fx event-id nil event-handler))

  ([event-id interceptors event-handler]
   (let [handler-f (metamorphic-handler->handler-f event-handler)]
        (re-frame.core/reg-event-fx event-id interceptors #(metamorphic-event->effects-map (handler-f %1 %2))))))

(defn apply-fx-params
  ; @param (function) handler-f
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
  [handler-f params]
  (if (sequential?     params)
      (apply handler-f params)
      (handler-f       params)))

(defn reg-fx
  ; @param (keyword) event-id
  ; @param (function) handler-f
  ;
  ; @usage
  ;  (defn my-side-effect [a])
  ;  (a/reg-fx       :my-side-effect my-side-effect)
  ;  (a/reg-event-fx :my-effect {:my-my-side-effect "A"})
  ;
  ; @usage
  ;  (defn your-side-effect [a b])
  ;  (a/reg-fx       :your-side-effect your-side-effect)
  ;  (a/reg-event-fx :your-effect {:your-my-side-effect ["a" "b"]})
  [event-id handler-f]
  (re-frame.core/reg-fx event-id #(apply-fx-params handler-f %)))



;; -- Dispatch functions ------------------------------------------------------
;; ----------------------------------------------------------------------------

(re-frame.core/reg-event-fx
  ; @param (metamorphic-event) n
  ;
  ; @usage
  ;  [:dispatch-metamorphic-event [...]]
  ;
  ; @usage
  ;  [:dispatch-metamorphic-event {:dispatch [...]}]
  :dispatch-metamorphic-event
  (fn [_ [_ n]] (metamorphic-event->effects-map n)))

(defn dispatch
  ; @param (metamorphic-event) event-handler
  ;
  ; @usage
  ;  (a/dispatch [:foo])
  ;
  ; @usage
  ;  (a/dispatch {:dispatch [:foo]})
  ;
  ; @usage
  ;  (a/dispatch nil)
  [event-handler]

  ; Szerver-oldalon a Re-Frame nem jelez hibát, nem regisztrált esemény meghívásakor.
  ; A szerver-oldalon nem történnek meg a nem regisztrált Re-Frame események, ezért nem lehetséges
  ; interceptor-ban vizsgálni az események regisztráltságát.
  #?(:clj (let [event-id      (event-vector->event-id     event-handler)
                event-exists? (event-handler-registrated? :event event-id)]
               (if-not event-exists? (println (str "re-frame: no :event handler registrated for: " event-id)))))

  (if (vector? event-handler) (re-frame.core/dispatch event-handler)
                              (re-frame.core/dispatch [:dispatch-metamorphic-event event-handler])))

; @usage
;  {:dispatch ...}
(re-frame.registrar/clear-handlers :fx :dispatch)
(re-frame.core/reg-fx :dispatch dispatch)

(defn dispatch-sync
  ; @param (event-vector) event-handler
  ;
  ; @usage
  ;  (a/dispatch-sync [...])
  ;
  ; A dispatch-sync függvény a meghívási sebesség fontossága miatt nem kezeli
  ; a metamorphic-event kezelőket!
  [event-handler]
  (re-frame.core/dispatch-sync event-handler))

(defn dispatch-n
  ; @param (metamorphic-events in vector) event-list
  ;
  ; @usage
  ;  (a/dispatch-n [[:event-a]
  ;                 {:dispatch [:event-b]}
  ;                 (fn [_ _] {:dispatch [:event-c]})])
  [event-list]
  (doseq [event (remove nil? event-list)]
         (dispatch event)))

; @usage
;  {:dispatch-n [[...] [...]}
(re-frame.registrar/clear-handlers :fx :dispatch-n)
(re-frame.core/reg-fx :dispatch-n dispatch-n)

(defn dispatch-later
  ; @param (maps in vector) effects-map-list
  ;
  ; @usage
  ;  (a/dispatch-later [{:ms 500 :dispatch [...]}
  ;                     {:ms 600 :fx [...]
  ;                              :fx-n       [[...] [...]]
  ;                              :dispatch-n [[...] [...]]}])
  [effects-map-list]
  ; Az eredeti dispatch-later függvény clojure környezetben nem időzíti a dispatch-later eseményeket!
  (doseq [{:keys [ms] :as effects-map} (remove nil? effects-map-list)]
         (if ms (time/set-timeout! ms #(dispatch (dissoc effects-map :ms))))))

; @usage
;  {:dispatch-later [{...} {...}]}
(re-frame.registrar/clear-handlers :fx :dispatch-later)
(re-frame.core/reg-fx :dispatch-later dispatch-later)

(defn dispatch-if
  ; @param (*) condition
  ; @param (metamorphic-event) if-event-handler
  ; @param (metamorphic-event)(opt) else-event-handler
  ;
  ; @usage
  ;  (a/dispatch-if [true [:my-event] ...])
  ;
  ; @usage
  ;  (a/dispatch-if [true {:dispatch [:my-event]} ...])
  ;
  ; @usage
  ;  (a/dispatch-if [true (fn [_ _] {:dispatch [:my-event]}) ...])
  [[condition if-event-handler else-event-handler]]
  (if condition (dispatch if-event-handler)
                (if else-event-handler (dispatch else-event-handler))))

; @usage
;  {:dispatch-if [...]}
(re-frame.core/reg-fx :dispatch-if dispatch-if)

(defn dispatch-cond
  ; @param (vector) conditional-events
  ; [(*) condition
  ;  (metamorphic-event) if-event-handler
  ;  ...]
  ;
  ; @usage
  ;  (a/dispatch-cond [(some? "a") [:my-event]
  ;                    (nil?  "b") [:my-event]])
  ;
  ; @usage
  ;  (a/dispatch-cond [(some? "a") {:dispatch [:my-event]}
  ;                    (nil?  "b") {:dispatch [:my-event]}])
  ;
  ; @usage
  ;  (a/dispatch-cond [(some? "a") (fn [_ _] {:dispatch [:my-event]})
  ;                    (nil?  "b") (fn [_ _] {:dispatch [:my-event]})])
  [conditional-events]
  (letfn [(dispatch-cond-f [_ dex x]
                           (if (and (even? dex) x)
                               (let [event (nth conditional-events (inc dex))]
                                    (dispatch event))))]
         (reduce-kv dispatch-cond-f nil conditional-events)))

; @usage
;  {:dispatch-cond [...]}
(re-frame.core/reg-fx :dispatch-cond dispatch-cond)



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
  (re-frame.core/dispatch [:dispatch-tick effects-maps-vector]))

; @usage
;  (reg-event-fx
;   :my-event
;   (fn [_ _]
;       {:dispatch-tick
;        [{:tick 10
;          :dispatch [:my-event]}]}))
(re-frame.core/reg-fx :dispatch-tick dispatch-tick)

(re-frame.core/reg-event-fx
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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn fx
  ; @param (vector) effect-vector
  ;
  ; @usage
  ;  (a/reg-fx :my-side-effect (fn [a b c]))
  ;  (a/fx [:my-side-effect "a" "b" "c"])
  [[effect-id & params :as effect-vector]]
  (when (= :db effect-id)
        (console :warn "re-frame: \":fx\" effect should not contain a :db effect"))
  (if-let [effect-f (re-frame.registrar/get-handler :fx effect-id false)]
          (effect-f params)
          (console :warn "re-frame: in \":fx\" effect found " effect-id " which has no associated handler. Ignoring.")))

; @usage
;  {:fx [...]}
(re-frame.registrar/clear-handlers :fx :fx)
(re-frame.core/reg-fx :fx fx)

(defn fx-n
  ; @param (vectors in vector) effect-vector-list
  ;
  ; @usage
  ;  (a/reg-fx :my-side-effect (fn [a b c]))
  ;  (a/fx-n [[:my-side-effect "a" "b" "c"]
  ;           [...]])
  [effect-vector-list]
  (if-not (sequential? effect-vector-list)
          (console :warn "re-frame: \":fx\" effect expects a seq, but was given " (type effect-vector-list))
          (doseq [effect-vector (remove nil? effect-vector-list)]
                 (fx effect-vector))))

; @usage
;  {:fx-n [[...] [...]]}
(re-frame.core/reg-fx :fx-n fx-n)



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
  (-> subscriber re-frame.core/subscribe deref))



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
