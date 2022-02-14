
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v2.6.0
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.keypress-handler
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.map    :as map :refer [dissoc-in]]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]
              [x.app-environment.event-handler :as event-handler]))



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  Az [:environment/reg-keypress-event! ...] esemény meghívásával tudsz billentyű-lenyomásra
;  vagy -felengedésre reagáló eseményeket regisztrálni.
;
; @usage
;  Az [:environment/remove-keypress-event! ...] esemény meghívásával tudod eltávolítani
;  az egyes regisztrált eseményeket, amennyiben az esemény regisztrálásakor használtál
;  egyedi azonosítót.
;
; @usage
;  Az [:environment/reg-keypress-listener! ...] esemény meghívásával, billentyű-lenyomásra vagy
;  -felengedésre reagáló esemény regisztrálása nélkül is bekapcsolható, a billenytyű-lenyomás
;  és -felengedés figyelő.
;
; @usage
;  Az (r get-pressed-keys db) és az (r key-pressed? db ...) feliratkozás függvények
;  segítségével kiolvashatod a Re-Frame adatbázisból az aktuálisan lenyomott billenytyűket.



;; -- State -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (integers in vector)
(def PREVENTED-KEYS (atom []))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn keypress-prevented?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) key-code
  ;
  ; @return (boolean)
  [key-code]
  (vector/contains-item? @PREVENTED-KEYS key-code))

; @constant (function)
(def KEYDOWN-LISTENER #(let [key-code (.-keyCode %)]
                            (if (keypress-prevented? key-code)
                                (.preventDefault %))
                            (a/dispatch [:environment/key-pressed key-code])))

; @constant (function)
(def KEYUP-LISTENER #(let [key-code (.-keyCode %)]
                          (if (keypress-prevented? key-code)
                              (.preventDefault %))
                          (a/dispatch [:environment/key-released key-code])))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-pressed-keys
  ; @usage
  ;  (r environment/get-pressed-keys? db)
  ;
  ; @return (vector)
  [db _]
  (get-in db (db/meta-item-path :environment/keypress-events :pressed-keys)))

(defn key-pressed?
  ; @param (integer) key-code
  ;
  ; @usage
  ;  (r environment/key-pressed? db 27)
  ;
  ; @return (boolean)
  [db [_ key-code]]
  (let [pressed-keys (r get-pressed-keys db)]
       (vector/contains-item? pressed-keys key-code)))

(defn- get-events
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (get-in db (db/path :environment/keypress-events)))

(defn- any-keypress-event-registrated?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (let [keypress-events (get-in db (db/path :environment/keypress-events))]
       (map/nonempty? keypress-events)))

(defn- handler-active?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (let [handler-active? (get-in db (db/meta-item-path :environment/keypress-events :active?))]
       (boolean handler-active?)))

(defn- activate-handler?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (and      (r any-keypress-event-registrated? db)
       (not (r handler-active?                 db))))

(defn- deactivate-handler?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (and      (r handler-active?                 db)
       (not (r any-keypress-event-registrated? db))))

(defn- get-event-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ; @param (keyword) prop-key
  ;
  ; @return (*)
  [db [_ event-id prop-key]]
  (get-in db (db/path :environment/keypress-events event-id prop-key)))

(defn- keypress-prevented-by-event?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ;
  ; @return (boolean)
  [db [_ event-id]]
  (let [prevent-default? (r get-event-prop db event-id :prevent-default?)]
       (boolean prevent-default?)))

(defn- keypress-prevented-by-other-events?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ;
  ; @return (boolean)
  [db [_ event-id]]
  (let [keypress-events (get-in db (db/path :environment/keypress-events))
        other-events    (dissoc keypress-events event-id)]
       (map/any-key-match? other-events #(r keypress-prevented-by-event? db %))))

(defn- enable-default?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ;
  ; @return (boolean)
  [db [_ event-id]]
  ; Enable default if prevented by event and NOT prevented by other events
  (and      (r keypress-prevented-by-event?        db event-id)
       (not (r keypress-prevented-by-other-events? db event-id))))

(defn- get-cached-keydown-event-ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) key-code
  ;
  ; @return (vector)
  [db [_ key-code]]
  (get-in db (db/meta-item-path :environment/keypress-events :cache key-code :keydown-events)))

(defn- get-cached-keyup-event-ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) key-code
  ;
  ; @return (vector)
  [db [_ key-code]]
  (get-in db (db/meta-item-path :environment/keypress-events :cache key-code :keyup-events)))

(defn- get-on-keydown-events
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) key-code
  ;
  ; @return (vector)
  [db [_ key-code]]
  (let [keydown-event-ids (r get-cached-keydown-event-ids db key-code)]
       (reduce #(conj %1 (r get-event-prop db %2 :on-keydown))
                [] keydown-event-ids)))

(defn- get-on-keyup-events
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) key-code
  ;
  ; @return (vector)
  [db [_ key-code]]
  (let [keyup-event-ids (r get-cached-keyup-event-ids db key-code)]
       (reduce #(conj %1 (r get-event-prop db %2 :on-keyup))
                [] keyup-event-ids)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-event-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ; @param (map) event-props
  ;
  ; @return (map)
  [db [_ event-id event-props]]
  ; XXX#1160 A regisztrált keypress események újra regisztrálásakor azok tulajdonságai felülíródnak
  (assoc-in db (db/path :environment/keypress-events event-id) event-props))

(defn- remove-event-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ;
  ; @return (map)
  [db [_ event-id]]
  (dissoc-in db (db/path :environment/keypress-events event-id)))

(defn- empty-cache!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (dissoc-in db (db/meta-item-path :environment/keypress-events :cache)))

(defn- cache-event!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; A keypress események tárolásakor az event-id azonosító egy cache vektorba
  ; kerül, így az adott billentyű lenyomáskor kisebb számításigénnyel lehetséges
  ; a billentyűkódhoz tartozó eseményeket elérni az adatbázisból
  ;
  ; @param (keyword) event-id
  ; @param (map) event-props
  ;  {:key-code (integer)
  ;   :on-keydown (metamorphic-event)(opt)
  ;   :on-keyup (metamorphic-event)(opt)}
  ;
  ; @return (map)
  [db [_ event-id {:keys [key-code on-keydown on-keyup]}]]
  ; XXX#1160 A regisztrált keypress események újra regisztrálásakor azok azonosítói nem ismétlődnek a cache vektorban
  (cond-> db on-keydown (update-in (db/meta-item-path :environment/keypress-events :cache key-code :keydown-events)
                                   vector/conj-item-once event-id)
             on-keyup   (update-in (db/meta-item-path :environment/keypress-events :cache key-code :keyup-events)
                                   vector/conj-item-once event-id)))

(defn- uncache-event!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ;
  ; @return (map)
  [db [_ event-id]]
  (let [key-code (r get-event-prop db event-id :key-code)]
       (-> db (update-in (db/meta-item-path :environment/keypress-events :cache key-code :keydown-events)
                         vector/remove-item event-id)
              (update-in (db/meta-item-path :environment/keypress-events :cache key-code :keyup-events)
                         vector/remove-item event-id))))

(defn- rebuild-cache!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (reduce-kv (fn [db event-id event-props]
                 (r cache-event! db event-id event-props))
             (r empty-cache! db)
             (get-in db (db/path :environment/keypress-events))))

(defn- reg-keypress-event!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ; @param (map) event-props
  ;
  ; @return (map)
  [db [_ event-id event-props]]
  (as-> db % (r store-event-props! % event-id event-props)
             (r cache-event!       % event-id event-props)))

(defn- remove-keypress-event!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ;
  ; @return (map)
  [db [_ event-id event-props]]
  (as-> db % (r uncache-event!      % event-id)
             (r remove-event-props! % event-id)))

(defn enable-non-required-keypress-events!
  ; @usage
  ;  (r environment/enable-non-required-keypress-events! db)
  ;
  ; @return (map)
  [db _]
  (r rebuild-cache! db))

(defn disable-non-required-keypress-events!
  ; @usage
  ;  (r environment/disable-non-required-keypress-events! db)
  ;
  ; @return (map)
  [db _]
  (reduce-kv (fn [db event-id {:keys [required?] :as event-props}]
                 (if required? (r cache-event! db event-id event-props)
                               (return db)))
             (r empty-cache! db)
             (r get-events   db)))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- prevent-keypress-default!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) key-code
  [key-code]
  (swap! PREVENTED-KEYS vector/conj-item-once key-code))

(a/reg-fx :environment/prevent-keypress-default! prevent-keypress-default!)

(defn- enable-keypress-default!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) key-code
  [key-code]
  (swap! PREVENTED-KEYS vector/remove-item key-code))

(a/reg-fx :environment/enable-keypress-default! enable-keypress-default!)

(defn- add-keypress-listeners!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (event-handler/add-event-listener! "keydown" KEYDOWN-LISTENER)
  (event-handler/add-event-listener! "keyup"     KEYUP-LISTENER))

(a/reg-fx :environment/add-keypress-listeners! add-keypress-listeners!)

(defn- remove-keypress-listeners!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (event-handler/remove-event-listener! "keydown" KEYDOWN-LISTENER)
  (event-handler/remove-event-listener! "keyup"     KEYUP-LISTENER))

(a/reg-fx :environment/remove-keypress-listeners! remove-keypress-listeners!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :environment/reg-keypress-event!
  ; @param (keyword)(opt) event-id
  ; @param (map) event-props
  ;  {:key-code (integer)
  ;   :on-keydown (metamorphic-event)(opt)
  ;   :on-keyup (metamorphic-event)(opt)
  ;   :prevent-default? (boolean)(opt)
  ;    Default: false
  ;   :required? (boolean)(opt)
  ;    A {:required? true} keypress eseményeket nem lehetséges inaktívvá tenni
  ;    Default: false}
  ;
  ; @usage
  ;  [:environment/reg-keypress-event {...}]
  ;
  ; @usage
  ;  [:environment/reg-keypress-event :my-event {...}]
  ;
  ; @usage
  ;  [:environment/reg-keypress-event {:key-code 65 :on-keydown [:do-something!]}
  [a/event-vector<-id]
  (fn [{:keys [db]} [_ event-id {:keys [key-code prevent-default?] :as event-props}]]
      (let [db (r reg-keypress-event! db event-id event-props)]
           (if prevent-default? {:fx [:environment/prevent-keypress-default! key-code]
                                 :db db :dispatch-if [(r activate-handler? db) [:environment/activate-keypress-handler!]]}
                                {:db db :dispatch-if [(r activate-handler? db) [:environment/activate-keypress-handler!]]}))))

(a/reg-event-fx
  :environment/remove-keypress-event!
  ; @param (keyword) event-id
  ;
  ; @usage
  ;  [:environment/remove-keypress-event! :my-event]
  (fn [{:keys [db]} [_ event-id]]
      (if (r enable-default? db event-id)
          (let [key-code (r get-event-prop         db event-id :key-code)
                db       (r remove-keypress-event! db event-id)]
               {:fx [:environment/enable-keypress-default! key-code]
                :db db :dispatch-if [(r deactivate-handler? db) [:environment/deactivate-keypress-handler!]]})
          (let [db (r remove-keypress-event! db event-id)]
               {:db db :dispatch-if [(r deactivate-handler? db) [:environment/deactivate-keypress-handler!]]}))))

(a/reg-event-fx
  :environment/activate-keypress-handler!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db (r db/set-item! db (db/meta-item-path :environment/keypress-events :active?) true)
       :fx [:environment/add-keypress-listeners! ]}))

(a/reg-event-fx
  :environment/deactivate-keypress-handler!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db (r db/set-item! db (db/meta-item-path :environment/keypress-events :active?) false)
       :fx [:environment/remove-keypress-listeners!]}))

(a/reg-event-fx
  :environment/reg-keypress-listener!
  ; @usage
  ;  [:environment/reg-keypress-listener! :my-listener]
  (fn [_ [_ listener-id]]
      [:environment/reg-keypress-event! listener-id {}]))

(a/reg-event-fx
  :environment/remove-keypress-listener!
  ; @usage
  ;  [:environment/remove-keypress-listener! :my-listener]
  (fn [_ [_ listener-id]]
      [:environment/remove-keypress-event! listener-id]))

(a/reg-event-fx
  :environment/key-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) key-code
  (fn [{:keys [db]} [_ key-code]]
      {; Store key-code to pressed-keys vector
       :db (r db/apply! db (db/meta-item-path :environment/keypress-events :pressed-keys)
              vector/conj-item-once key-code)
       ; Dispatch keydown events
       :dispatch-n (r get-on-keydown-events db key-code)}))

(a/reg-event-fx
  :environment/key-released
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) key-code
  (fn [{:keys [db]} [_ key-code]]
      {; Remove key-code from pressed-keys vector
       :db (r db/apply! db (db/meta-item-path :environment/keypress-events :pressed-keys)
              vector/remove-item key-code)
       ; Dispatch keyup events
       :dispatch-n (r get-on-keyup-events db key-code)}))
