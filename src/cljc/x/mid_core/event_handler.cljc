;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.11
; Description:
; Version: v1.2.8
; Compatibility: x4.4.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.event-handler
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.loop    :refer [reduce-indexed]]
              [mid-fruits.map     :as map :refer [update-some]]
              [mid-fruits.random  :as random]
              [mid-fruits.string  :as string]
              [mid-fruits.vector  :as vector]
              [re-frame.core      :as re-frame]
              [re-frame.db        :refer [app-db]]
              [re-frame.registrar :as registrar]
              [x.mid-core.engine  :as engine]))



;; -- Index -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Helpers
; Converters
; Event-vector converters
; DB functions
; Event self-destructing
; Metamorphic handler functions
; Metamorphic effects functions
; Effects-map functions
; Event checking
; Namespace redirecting
; Event redirecting
; Event registrating
; Registrating registrator events
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
; @name dispatch-some
;  TODO ...
;
; @name dispatch-if
;  TODO ...
;
; @name dispatch-cond
;  TODO ...



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def INCONSISTENT-DB-ERROR "Inconsistent database error")



;; -- State -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (map)
;  {:cofx (map)
;   :event (map)
;   :fx (map)
;   :sub (map)}
(def kind->id->namespace (atom {:cofx  {}
                                :event {}
                                :fx    {}
                                :sub   {}}))



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
  ; WARNING!
  ; Az (a/event-vector?) függvény az n paraméterként átadott hiccup formátumú
  ; adatot is esemény vektorként ismeri fel!
  ;
  ; @param (*) n
  ;
  ; @example
  ;  (a/event-vector? [:my-namespace/do-something! ...])
  ;  => true
  ;
  ; @return (boolean)
  [n]
  (boolean (and (vector? n)
                (let [event-id (first n)]
                     (keyword? event-id)))))

(defn subscription-vector?
  ; WARNING!
  ; Az (a/subscription-vector?) függvény az n paraméterként átadott hiccup formátumú
  ; adatot is subscription vektorként ismeri fel!
  ;
  ; @param (*) n
  ;
  ; @example
  ;  (a/subscription-vector? [:my-namespace/get-something ...])
  ;  => true
  ;
  ; @return (boolean)
  [n]
  (boolean (and (vector? n)
                (let [event-id (first n)]
                     (keyword? event-id)))))

(defn event-group-vector?
  ; @param (*) n
  ;
  ; @example
  ;  (a/event-group-vector? [{:ms 500 :dispatch [:my-namespace/do-something! ...]}])
  ;  => true
  ;
  ; @return (boolean)
  [n]
  (boolean (and (vector? n)
                (let [event-map (first n)]
                     (and (map? event-map)
                          (map/contains-key? event-map :dispatch))))))



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn event-vector->param-vector
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vector) event-vector
  ;
  ; @example
  ;  (a/event-vector->param-vector [:my-event ...])
  ;  => [...]
  ;
  ; @return (vector)
  [event-vector]
  (subvec event-vector 1))

(defn event-vector->event-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vector) event-vector
  ;
  ; @example
  ;  (a/event-vector->event-id [:my-event ...])
  ;  => :my-event
  ;
  ; @return (vector)
  [event-vector]
  (first event-vector))

(defn context->event-vector
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) context
  ;  {:coeffects (map)
  ;   {:event (vector)}}
  ;
  ; @return (vector)
  [context]
  (get-in context [:coeffects :event]))

(defn context->empty-event-vector
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) context
  ;  {:coeffects (map)
  ;   {:event (vector)}}
  ;
  ; @return (vector)
  [context]
  (let [event-vector (context->event-vector context)]
       [(event-vector->event-id event-vector)]))

(defn context->event-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) context
  ;  {:coeffects (map)
  ;   {:event (vector)}}
  ;
  ; @return (vector)
  [context]
  (let [event-vector (context->event-vector context)]
       (vector/shift-first-item event-vector)))

(defn context->event-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) context
  ;
  ; @return (keyword)
  [context]
  (let [event-vector (context->event-vector context)]
       (event-vector->event-id event-vector)))

(defn context->db-before-effect
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) context
  ;  {:coeffects (map)
  ;   {:db (map)}}
  ;
  ; @return (map)
  [context]
  (get-in context [:coeffects :db]))

(defn context->db-after-effect
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) context
  ;  {:effects (map)
  ;   {:db (map)}}
  ;
  ; @return (map)
  [context]
  (get-in context [:effects :db]))

(defn context->db-inconsistent?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) context
  ;
  ; @return (boolean)
  [context]
  (not (map/nonempty? (context->db-before-effect context))))

(defn context->error-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) context
  ;
  ; @return (map)
  [context]
  {:event-id (context->event-id context)
   :error    INCONSISTENT-DB-ERROR})

(defn context->error-catched?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) context
  ;  {:error-event-id (keyword)}
  ;
  ; @return (boolean)
  [{:keys [error-event-id] :as context}]
  (let [event-id (context->event-id context)]
       (and (context->db-inconsistent? context)
            (not (= event-id error-event-id)))))



;; -- Param-vector converters -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn param-vector->first-id
  ; @param (vector) param-vector
  ;
  ; @example
  ;  (a/param-vector->first-id [:first-id {...}])
  ;  => :first-id
  ;
  ; @example
  ;  (a/param-vector->first-id [{...}])
  ;  => :0ce14671-e916-43ab-b057-0939329d4c1b
  ;
  ; @return (keyword)
  ;  Ha a param-vector legalább egy kulcsszót tartalmaz, akkor a visszatérési
  ;   érték az első a kulcsszó.
  ;  Ha a param-vector egy kulcsszót sem tartalmaz, akkor a visszatérési érték
  ;   egy random-uuid.
  [param-vector]
  (engine/id (vector/first-of-filtered param-vector keyword?)))

(defn param-vector->second-id
  ; @param (vector) param-vector
  ;
  ; @example
  ;  (a/param-vector->second-id [:first-id :second-id {...}])
  ;  => :second-id
  ;
  ; @example
  ;  (a/param-vector->second-id [:first-id {...}])
  ;  => :0ce14671-e916-43ab-b057-0939329d4c1b
  ;
  ; @example
  ;  (a/param-vector->second-id [{...}])
  ;  => :0ce14671-e916-43ab-b057-0939329d4c1b
  ;
  ; @return (keyword)
  ;  Ha a param-vector legalább kettő kulcsszót tartalmaz, akkor a visszatérési
  ;   érték a második kulcsszó.
  ;  Ha a param-vector egy kulcsszót tartalmaz, akkor a visszatérési érték
  ;   egy random-uuid.
  ;  Ha a param-vector egy kulcsszót sem tartalmaz, akkor a visszatérési érték
  ;   egy random-uuid.
  [param-vector]
  (engine/id (if (>= (vector/filtered-count param-vector keyword?) 2)
                 (vector/nth-of-filtered param-vector keyword? 1))))

(defn param-vector->first-props
  ; @param (vector) param-vector
  ;
  ; @example
  ;  (a/param-vector->first-props [:first-id {...}])
  ;  => {...}
  ;
  ; @example
  ;  (a/param-vector->first-props [:first-id {... 1} {... 2}])
  ;  => {... 1}
  ;
  ; @example
  ;  (a/param-vector->first-props [:first-id])
  ;  => {}
  ;
  ; @return (map)
  ;  Ha a param-vector egy térképet tartalmaz, akkor a visszatérési érték
  ;   az a térkép.
  ;  Ha a param-vector legalább kettő térképet tartalmaz, akkor a visszatérési
  ;   érték az első térkép.
  ;  Ha a param-vector egy térképet sem tartalmaz, akkor a visszatérési érték
  ;   egy üres térkép.
  [param-vector]
  (or (vector/first-of-filtered param-vector map?) (param {})))

(defn param-vector->second-props
  ; @param (vector) param-vector
  ;
  ; @example
  ;  (a/param-vector->second-props [:first-id {...}]
  ;  => {}
  ;
  ; @example
  ;  (a/param-vector->second-props [:first-id {... 1} {... 2}]
  ;  => {... 2}
  ;
  ; @example
  ;  (a/param-vector->second-props [:first-id]
  ;  => {}
  ;
  ; @return (map)
  ;  Ha a param-vector egy térképet tartalmaz, akkor a visszatérési érték
  ;   egy üres térkép.
  ;  Ha a param-vector legalább kettő térképet tartalmaz, akkor a visszatérési
  ;   érték a második térkép.
  ;  Ha a param-vector egy térképet sem tartalmaz, akkor a visszatérési érték
  ;   egy üres térkép.
  [param-vector]
  (if (>= (vector/filtered-count param-vector map?) 2)
      (vector/nth-of-filtered param-vector map? 1)
      (param {})))



;; -- Event-vector converters -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn event-vector->first-id
  ; @param (vector) event-vector
  ;
  ; @example
  ;  (a/event-vector->first-id [:my-event :my-id {...}])
  ;  => :my-event
  ;
  ; @return (keyword)
  [event-vector]
  (first event-vector))

(defn event-vector->second-id
  ; @param (vector) event-vector
  ;
  ; @example
  ;  (a/event-vector->second-id [:my-event :my-id {...}])
  ;  => :my-id
  ;
  ; @example
  ;  (a/event-vector->second-id [:my-event {...}])
  ;  => :0ce14671-e916-43ab-b057-0939329d4c1b
  ;
  ; @return (keyword)
  ;  Ha az event-vector legalább kettő kulcsszót tartalmaz (az event-id kulcsszót
  ;   is számítva), akkor a visszatérési érték az második a kulcsszó.
  ;  Ha az event-vector kevesebb, mint kettő kulcsszót tartalmaz (az event-id
  ;   kulcsszót is számítva), akkor a visszatérési érték egy random-uuid.
  [event-vector]
  (let [param-vector (event-vector->param-vector event-vector)]
       (param-vector->first-id param-vector)))

(defn event-vector->third-id
  ; @param (vector) event-vector
  ;
  ; @example
  ;  (a/event-vector->third-id [:my-event :my-id :your-id {...}])
  ;  => :your-id
  ;
  ; @example
  ;  (a/event-vector->third-id [:my-event :my-id {...}])
  ;  => :0ce14671-e916-43ab-b057-0939329d4c1b
  ;
  ; @example
  ;  (a/event-vector->third-id [:my-event {...}])
  ;  => :0ce14671-e916-43ab-b057-0939329d4c1b
  ;
  ; @return (keyword)
  ;  Ha az event-vector legalább három kulcsszót tartalmaz (az event-id kulcsszót
  ;   is számítva), akkor a visszatérési érték a harmadik kulcsszó.
  ;  Ha az event-vector kevesebb, mint három kulcsszót tartalmaz (az event-id
  ;   kulcsszót is számítva), akkor a visszatérési érték egy random-uuid.
  [event-vector]
  (let [param-vector (event-vector->param-vector event-vector)]
       (param-vector->second-id param-vector)))

(defn event-vector->first-props
  ; @param (vector) event-vector
  ;
  ; @example
  ;  (a/event-vector->first-props [:my-event :my-id {...}])
  ;  => {...}
  ;
  ; @example
  ;  (a/event-vector->first-props [:my-event :my-id {... 1} {... 2}])
  ;  => {... 1}
  ;
  ; @example
  ;  (a/event-vector->first-props [:my-event :my-id])
  ;  => {}
  ;
  ; @return (map)
  ;  Ha az event-vector egy térképet tartalmaz, akkor a visszatérési érték
  ;   az a térkép.
  ;  Ha az event-vector legalább kettő térképet tartalmaz, akkor a visszatérési
  ;   érték az első térkép.
  ;  Ha az event-vector egy térképet sem tartalmaz, akkor a visszatérési érték
  ;   egy üres térkép.
  [event-vector]
  (let [param-vector (event-vector->param-vector event-vector)]
       (param-vector->first-props param-vector)))

(defn event-vector->second-props
  ; @param (vector) event-vector
  ;
  ; @example
  ;  (a/event-vector->second-props [:my-event :my-id {...}]
  ;  => {}
  ;
  ; @example
  ;  (a/event-vector->second-props [:my-event :my-id {... 1} {... 2}]
  ;  => {... 2}
  ;
  ; @example
  ;  (a/event-vector->second-props [:my-event :my-id]
  ;  => {}
  ;
  ; @return (map)
  ;  Ha az event-vector egy térképet tartalmaz, akkor a visszatérési érték
  ;   egy üres térkép.
  ;  Ha az event-vector legalább kettő térképet tartalmaz, akkor a visszatérési
  ;   érték a második térkép.
  ;  Ha az event-vector egy térképet sem tartalmaz, akkor a visszatérési érték
  ;   egy üres térkép.
  [event-vector]
  (let [param-vector (event-vector->param-vector event-vector)]
       (param-vector->second-props param-vector)))



;; -- DB functions ------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-db!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) value
  ;
  ; @return (map)
  [value]
  (if-not (identical? @app-db value)
          (reset!      app-db value)))

(defn db
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (function) f
  ; @param (vector)(opt) event-vector
  ;
  ; @usage
  ;  (db (fn [db [event-id] (assoc-in db [:a :b] :c)))
  ;
  ; @return (map)
  ([f] (db f [::db]))

  ([f event-vector]
   (reset-db! (f @app-db event-vector))))



;; -- Event self-destructing --------------------------------------------------
;; ----------------------------------------------------------------------------

; A self-destruct! interceptor használatával az esemény megsemmisíti önmagát
; az első meghívását követően, így azt többször már nem lehet meghívni.
;
; @usage
;  (reg-event-fx
;   ::my-event
;   [self-destruct!]
;   (fn [cofx event-vector]
;       {:dispatch ...}))

(defn <-self-destruct!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) context
  ;
  ; @return (map)
  [context]
  (let [event-vector (context->event-vector  context)
        event-id     (event-vector->event-id event-vector)]
       (registrar/clear-handlers :event event-id)
       (return context)))

(def self-destruct!
  (re-frame/->interceptor
    :id    ::self-destruct!
    :after <-self-destruct!))



;; -- Event param functions ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn event-vector<-params
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (event-vector) n
  ; @param (list of *) xyz
  ;
  ; @return (event-vector)
  [n & xyz]
  (vector/concat-items n xyz))

(defn metamorphic-event<-params
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (metamorphic-event) n
  ; @param (list of *) xyz
  ;
  ; @example
  ;  (a/metamorphic-event<-params [:my-event] :my-param-1 "my-param-2")
  ;  => [:my-event :my-param-1 "my-param-2"]
  ;
  ; @usage
  ;  (a/metamorphic-event<-params {:dispatch [:my-event]} :my-param-1 "my-param-2")
  ;  => {:dispatch [:my-event :my-param-1 "my-param-2"]}
  ;
  ; @return (metamorphic-event)
  [n & xyz]
        ; Szükséges megkülönböztetni az esemény vektort a dispatch-later és dispatch-tick
        ; esemény csoport vektortól!
        ; [:do-something! ...]
        ; [{:ms 500 :dispatch [:do-something! ...]}]
  (cond (event-vector?       n)
        (vector/concat-items n xyz)
        (map?                n)
        (reduce-kv (fn [result k v]
                       (let [v (apply metamorphic-event<-params v xyz)]
                            (assoc result k v)))
                   (param {})
                   (param n))
        (event-group-vector? n)
        ; TODO ...
        (return n)
        :else (return n)))



;; -- Metamorphic handler functions -------------------------------------------
;; ----------------------------------------------------------------------------

(defn event-vector->effects-map
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vector) event-vector
  ;
  ; @return (map)
  [event-vector]
  {:dispatch event-vector})

(defn event-vector->handler-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vector) event-vector
  ;
  ; @return (function)
  [event-vector]
  (fn [_ _] {:dispatch event-vector}))

(defn effects-map->handler-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) effects-map
  ;
  ; @return (function)
  [effects-map]
  (fn [_ _] effects-map))

(defn metamorphic-event->handler-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
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
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (metamorphic-effects) n
  ;
  ; @example
  ;  (a/metamorphic-effects->effects-map [:do-something!])
  ;  => {:dispatch [:do-something!]}
  ;
  ; @example
  ;  (a/metamorphic-effects->effects-map {:dispatch [:do-something!])
  ;  => {:dispatch [:do-something!]}
  ;
  ; @return (map)
  [n]
  (cond (vector? n) (event-vector->effects-map n)
        :else       (return n)))



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
  ;  => {:dispatch [:a1] :dispatch-n [[:a2] [:a3] [:b1] [:b2]]}
  ;
  ; @return (map)
  [a b]
  (-> a (update-some :dispatch-n     vector/conj-item    (:dispatch       b))
        (update-some :dispatch-n     vector/concat-items (:dispatch-n     b))
        (update-some :dispatch-later vector/concat-items (:dispatch-later b))))



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
  (let [event-handlers (get-event-handlers)]
       (get-in event-handlers [event-kind event-id])))

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
  (let [event-handler (get-event-handler event-kind event-id)]
       (some? event-handler)))



;; -- Namespace redirecting ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn redirect-cofx-namespace
  ; @param (keyword) source-namespace
  ; @param (keyword) target-namespace
  ;
  ; @usage
  ;  (a/redirect-cofx-namespace :my-very-long-namespace :my-ns)
  [source-namespace target-namespace])
  ; TODO ...

(defn redirect-event-namespace
  ; @param (keyword) source-namespace
  ; @param (keyword) target-namespace
  ;
  ; @usage
  ;  (a/redirect-event-namespace :my-very-long-namespace :my-ns)
  [source-namespace target-namespace]
  (swap! kind->id->namespace merge
         {:event {target-namespace {:require source-namespace}
                  source-namespace {:as      target-namespace}}}))

(defn redirect-fx-namespace
  ; @param (keyword) source-namespace
  ; @param (keyword) target-namespace
  ;
  ; @usage
  ;  (a/redirect-cofx-namespace :my-very-long-namespace :my-ns)
  [source-namespace target-namespace])
  ; TODO ...

(defn redirect-sub-namespace
  ; @param (keyword) source-namespace
  ; @param (keyword) target-namespace
  ;
  ; @usage
  ;  (a/redirect-sub-namespace :my-very-long-namespace :my-ns)
  [source-namespace target-namespace]
  (swap! kind->id->namespace merge
         {:sub {target-namespace {:require source-namespace}
                source-namespace {:as      target-namespace}}}))

(defn redirect-namespace
  ; @param (keyword) source-namespace
  ; @param (keyword) target-namespace
  ;
  ; @usage
  ;  (a/redirect-namespace :my-very-long-namespace :my-ns)
  [source-namespace target-namespace]
  (redirect-cofx-namespace  source-namespace target-namespace)
  (redirect-event-namespace source-namespace target-namespace)
  (redirect-fx-namespace    source-namespace target-namespace)
  (redirect-sub-namespace   source-namespace target-namespace))

(defn get-cofx-namespace-redirection
  ; @param (keyword) target-namespace
  ;
  ; @return (keyword)
  [target-namespace])
  ; TODO ...

(defn get-event-namespace-redirection
  ; @param (keyword) target-namespace
  ;
  ; @return (keyword)
  [target-namespace]
  (get-in @kind->id->namespace [:event target-namespace]
          ; XXX#5376
          ; Ha nincs másik névtér átirányítva a target-namespace paraméterként
          ; átadott névtérre, akkor a visszatérési érték a target-namespace értéke.
          (return target-namespace)))

(defn get-fx-namespace-redirection
  ; @param (keyword) target-namespace
  ;
  ; @return (keyword)
  [target-namespace])
  ; TODO ...

(defn get-sub-namespace-redirection
  ; @param (keyword) target-namespace
  ;
  ; @return (keyword)
  [target-namespace]
  (get-in @kind->id->namespace [:sub target-namespace]
          ; XXX#5376
          (return target-namespace)))



;; -- Event redirecting -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn redirect-event-db
  ; @param (keyword) source-event-id
  ; @param (keyword) target-event-id
  [source-event-id target-event-id]
  (let [source-event-handler (get-event-handler :event source-event-id)]
       (swap! registrar/kind->id->handler assoc-in [:event target-event-id]
              source-event-handler)))

(defn redirect-event-fx
  ; @param (keyword) source-event-id
  ; @param (keyword) target-event-id
  [source-event-id target-event-id]
  (let [source-event-handler (get-event-handler :event source-event-id)]
       (swap! registrar/kind->id->handler assoc-in [:event target-event-id]
              source-event-handler)))

(defn redirect-sub
  ; @param (keyword) source-event-id
  ; @param (keyword) target-event-id
  [source-event-id target-event-id])
  ; TODO ...



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
        (re-frame/reg-event-fx event-id interceptors
          #(metamorphic-effects->effects-map (handler-function %1 %2))))))

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
  ;   :you-are-awesome!
  ;   (fn [a b]))
  ;
  ;  (a/reg-event-fx
  ;   :happy-events
  ;   (fn [_ _]
  ;       {:you-are-awesome! [a b]
  ;        :dispatch [:you-are-awesome! a b]))
  [event-id handler-function]
  (re-frame/reg-fx       event-id (fn [param-vector]   (apply handler-function param-vector)))
  (re-frame/reg-event-fx event-id (fn [_ event-vector] {event-id (event-vector->param-vector event-vector)})))



;; -- Registrating registrator events -----------------------------------------
;; ----------------------------------------------------------------------------

; Az itt felsorolt regisztrátorok eseményként is meghívhatók.
;
; Pl.:
; (reg-event-fx
;  ::example
;  (fn [_ _]
;   {:dispatch [:reg-event-db ::db-event-id (fn [_ _])]}))

(re-frame/reg-fx
  :reg-event-db
  ; @param (keyword) event-id
  ; @param (function) event-handler
  (fn [[event-id event-handler]]
      (reg-event-db event-id event-handler)))

(re-frame/reg-event-fx
  :reg-event-db
  (fn [_ [_ event-id event-handler]]
      {:reg-event-db [event-id event-handler]}))

(re-frame/reg-fx
  :reg-event-fx
  ; @param (keyword) event-id
  ; @param (metamorphic-event) event-handler
  (fn [[event-id event-handler]]
      (reg-event-fx event-id event-handler)))

(re-frame/reg-event-fx
  :reg-event-fx
  (fn [_ [_ event-id event-handler]]
      {:reg-event-fx [event-id event-handler]}))

(re-frame/reg-fx
  :reg-sub
  ; @param (keyword) event-id
  ; @param (function) subscription-handler
  (fn [[event-id subscription-handler]]
      (re-frame/reg-sub event-id subscription-handler)))

(re-frame/reg-event-fx
  :reg-sub
  (fn [_ [_ event-id subscription-handler]]
      {:reg-sub [event-id subscription-handler]}))



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
               (if-not (boolean event-exists?)
                       (println (str "re-frame: no :event handler registrated for: " event-id)))))

  (cond ; @usage
        ;  (dispatch [:foo])
        (vector?           event-handler)
        (re-frame/dispatch event-handler)
        ; @usage
        ;  (dispatch {:dispatch [:foo]})
        (map? event-handler)
        (dispatch-function (effects-map->handler-function event-handler))
        ; @usage
        ;  (dispatch nil)
        (nil?   event-handler)
        (return :nil-handler-exception)
        ; @usage
        ;  (dispatch (fn [_ _] {:dispatch [:foo]}))
        :else (dispatch-function event-handler)))

(registrar/clear-handlers :fx :dispatch)
(re-frame/reg-fx :dispatch dispatch)

(defn dispatch-sync
  ; @param (event-vector) event-handler
  ;
  ; A dispatch-sync függvény a meghívási sebesség fontossága miatt nem kezeli
  ; a metamorphic-event kezelőket és a névtér átirányításokat.
  ; A dispatch-sync igény esetén bővíthető az említett képességekkel.
  [event-handler]
  (re-frame/dispatch-sync event-handler))

(defn dispatch-n
  ; @param (metamorphic-events in vector) event-list
  ;
  ; @usage
  ;  (dispatch-n
  ;   [[:event-a]
  ;    {:dispatch [:event-b]}
  ;    (fn [_ _] {:dispatch [:event-c]})])
  [event-list]
  (doseq [event (remove nil? event-list)]
         (dispatch event)))

(registrar/clear-handlers :fx :dispatch-n)
(re-frame/reg-fx :dispatch-n dispatch-n)

(defn dispatch-some
  ; @param (metamorphic-event) event
  ;
  ; @usage
  ;  (dispatch-some [:my-event])
  ;  (dispatch-some {:dispatch [:my-event]})
  ;  (dispatch-some (fn [_ _] {:dispatch [:my-event]}))
  [event-handler]
  (if (some? event-handler)
      (dispatch event-handler)))

(re-frame/reg-fx :dispatch-some dispatch-some)

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
  (if condition
      (dispatch if-event-handler)
      (if (some? else-event-handler)
          (dispatch else-event-handler))))

(re-frame/reg-fx :dispatch-if dispatch-if)

(defn dispatch-cond
  ; @param (*) condition
  ; @param (metamorphic-event) if-event-handler
  ; @param (metamorphic-event)(opt) else-event-handler
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
  (reduce-indexed (fn [%1 %2 %3]
                      (if (even? %3)
                          (let [event (nth conditional-events (inc %3))]
                               (if (boolean %2)
                                   (dispatch event)))))
                  (param nil)
                  (param conditional-events)))

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

(defn- tick-now?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) effects-map
  ;
  ; @return (boolean)
  [effects-map]
  (= 0 (:tick effects-map)))

(defn- tick-now!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) merged-effects-map
  ; @param (map) effects-map
  ;
  ; @return (map)
  [merged-effects-map effects-map]
  (merge-effects-maps merged-effects-map effects-map))

(defn- tick-later!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) merged-effects-map
  ; @param (map) effects-map
  ;
  ; @return (map)
  [merged-effects-map effects-map]
  (update merged-effects-map :dispatch-tick vector/conj-item
          (update effects-map :tick dec)))

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
  ;
  ;   {:tick 10
  ;    :dispatch       [:my-event]
  ;    :dispatch-n     [[:my-event]]
  ;    :dispatch-later [ ... ]}
  ;
  ;   { ... }]
  (fn [_ [_ effects-maps-vector]]
      (reduce #(if (tick-now?   %2)
                   (tick-now!   %1 %2)
                   (tick-later! %1 %2))
               (param {})
               (param effects-maps-vector))))



;; -- Dereferenced subscriptions ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn subscribed
  ; @param (vector) subscriber
  ;
  ; @usage
  ;  (subscribed [:my-subscription])
  ;
  ; @return (*)
  [subscriber]
  (deref (re-frame/subscribe subscriber)))



;; -- Eyecandy functions ------------------------------------------------------
;; ----------------------------------------------------------------------------

; A re-frame események függvényei a db paraméter után egy vektort fogadnak,
; amelynek az első eleme az esemény azonosítója ami szinte kivétel nélkül soha
; nincs használva.
;
; Ezeket a függvényeket a következő formula szerint kell használni:
; (db/trim-partition! db [_ partition-id])
;
; Esztétikusabb ha nem kell ezzel a vektorral baszakodni, ezért létezik
; az r nevű függvény, amelynek az egyetlen feladata, hogy egy másik
; formula szerint is használhatóvá teszi a re-frame események függvényeit.
;
; (r db/trim-partition! db partition-id)

(defn r
  ; @param (function) f
  ; @param (*) params
  ;
  ; @example
  ;  (r db/trim-partition! db partition-id)
  ;  => (db/trim-partition! db [_ partition-id])
  ;
  ; @return (*)
  [f & params]
  (let [context      (first params)
        event-vector (vector/cons-item (rest params) nil)]
       (f context event-vector)))
