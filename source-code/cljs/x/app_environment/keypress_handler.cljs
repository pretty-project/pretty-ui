
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v2.0.4
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.keypress-handler
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.map    :as map :refer [dissoc-in]]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]
              [x.app-user.api    :as user]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (vector)
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

(def keydown-listener
     #(let [key-code (.-keyCode %)]
           (if (keypress-prevented? key-code)
               (.preventDefault %))
           (a/dispatch [:environment/->key-pressed key-code])))

(def keyup-listener
     #(let [key-code (.-keyCode %)]
           (if (keypress-prevented? key-code)
               (.preventDefault %))
           (a/dispatch [:environment/->key-released key-code])))

(defn- event-props->prevent-default?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) event-props
  ;  {:prevent-default? (boolean)(opt)}
  ;
  ; @return (boolean)
  [{:keys [prevent-default?]}]
  (boolean prevent-default?))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-events
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (get-in db (db/path ::keypress-events)))

(defn- get-event-ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (vector)
  [db _]
  (map/get-keys (r get-events db)))

(defn- get-cache
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (get-in db (db/meta-item-path ::keypress-events :cache)))

(defn- any-keypress-event-registrated?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (map/nonempty? (get-in db (db/path ::keypress-events))))

(defn- handler-enabled-by-user?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (boolean (r user/get-user-settings-item db :hotkeys-enabled?)))

(defn- handler-enabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (boolean (get-in db (db/meta-item-path ::keypress-events :enabled?))))

(defn- handler-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (not (r handler-enabled? db)))

(defn- enable-handler?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (boolean (and (r handler-disabled?               db)
                (r handler-enabled-by-user?        db)
                (r any-keypress-event-registrated? db))))

(defn- disable-handler?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (boolean (and (r handler-enabled?                     db)
                (not (r any-keypress-event-registrated? db)))))

(defn- get-event-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ; @param (keyword) prop-id
  ;
  ; @return (*)
  [db [_ event-id prop-id]]
  (get-in db (db/path ::keypress-events event-id prop-id)))

(defn- keypress-prevented-by-event?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ;
  ; @return (boolean)
  [db [_ event-id]]
  (boolean (r get-event-prop db event-id :prevent-default?)))

(defn- keypress-prevented-by-other-events?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ;
  ; @return (boolean)
  [db [_ event-id]]
  (let [keypress-events (get-in db (db/path ::keypress-events))
        other-events    (dissoc keypress-events event-id)]
       (map/any-key-match? other-events #(r keypress-prevented-by-event? db %))))


(defn- enable-default?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ;
  ; @return (boolean)
  [db [_ event-id]]
  ; Enable default if prevented by event and not prevented by other events
  (and (r keypress-prevented-by-event?             db event-id)
       (not (r keypress-prevented-by-other-events? db event-id))))

(defn- get-cached-keydown-event-ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) key-code
  ;
  ; @return (vector)
  [db [_ key-code]]
  (get-in db (db/meta-item-path ::keypress-events :cache key-code :keydown-events)))

(defn- get-cached-keyup-event-ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) key-code
  ;
  ; @return (vector)
  [db [_ key-code]]
  (get-in db (db/meta-item-path ::keypress-events :cache key-code :keyup-events)))

(defn- get-on-keydown-events
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) key-code
  ;
  ; @return (vector)
  [db [_ key-code]]
  (let [keydown-event-ids (r get-cached-keydown-event-ids db key-code)]
       (vec (reduce #(conj %1 (r get-event-prop db %2 :on-keydown))
                    [] keydown-event-ids))))

(defn- get-on-keyup-events
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) key-code
  ;
  ; @return (vector)
  [db [_ key-code]]
  (let [keyup-event-ids (r get-cached-keyup-event-ids db key-code)]
       (vec (reduce #(conj %1 (r get-event-prop db %2 :on-keyup))
                    [] keyup-event-ids))))

(defn get-pressed-keys
  ; @return (vector)
  [db _]
  (get-in db (db/meta-item-path ::keypress-events :pressed-keys)))

(defn key-pressed?
  ; @param (integer) key-code
  ;
  ; @return (boolean)
  [db [_ key-code]]
  (let [pressed-keys (r get-pressed-keys db)]
       (vector/contains-item? pressed-keys key-code)))



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
  (assoc-in db (db/path ::keypress-events event-id) event-props))

(defn- remove-event-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ;
  ; @return (map)
  [db [_ event-id]]
  (dissoc-in db (db/path ::keypress-events event-id)))

(defn- empty-cache!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (dissoc-in db (db/meta-item-path ::keypress-events :cache)))

(defn- cache-event!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; A keypress események tárolásakor az event-id azonosító egy cache vektorba
  ; kerül, így az adott billentyű leütésekor kisebb számításigénnyel lehetséges
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
  (cond-> db (some? on-keydown)
             (update-in (db/meta-item-path ::keypress-events :cache key-code :keydown-events)
                        vector/conj-item event-id)
             (some? on-keyup)
             (update-in (db/meta-item-path ::keypress-events :cache key-code :keyup-events)
                        vector/conj-item event-id)))

(defn- uncache-event!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ;
  ; @return (map)
  [db [_ event-id]]
  (let [key-code (r get-event-prop db event-id :key-code)]
       (-> db (update-in (db/meta-item-path ::keypress-events :cache key-code :keydown-events)
                         vector/remove-item event-id)
              (update-in (db/meta-item-path ::keypress-events :cache key-code :keyup-events)
                         vector/remove-item event-id))))

(defn- rebuild-cache!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (reduce-kv (fn [db event-id event-props]
                 (r cache-event! db event-id event-props))
             (r empty-cache! db)
             (get-in db (db/path ::keypress-events))))

(defn- enable-non-required-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (r rebuild-cache! db))

(a/reg-event-db :environment/enable-non-required-keypress-events! enable-non-required-keypress-events!)

(defn- disable-non-required-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (reduce-kv (fn [db event-id {:keys [required?] :as event-props}]
                 (if required? (r cache-event! db event-id event-props)
                               (return db)))
             (r empty-cache! db)
             (r get-events   db)))

(a/reg-event-db :environment/disable-non-required-keypress-events! disable-non-required-keypress-events!)



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- prevent-keypress-default!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) key-code
  [key-code]
  (swap! PREVENTED-KEYS vector/conj-item-once key-code))

(a/reg-handled-fx :environment/prevent-keypress-default! prevent-keypress-default!)

(defn- enable-keypress-default!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) key-code
  [key-code]
  (swap! PREVENTED-KEYS vector/remove-item key-code))

(a/reg-handled-fx :environment/enable-keypress-default! enable-keypress-default!)



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
  (fn [{:keys [db]} event-vector]
      (let [rf-event-id (a/event-vector->first-id    event-vector)
            event-id    (a/event-vector->second-id   event-vector)
            event-props (a/event-vector->first-props event-vector)]
            ; rf-event-id: Re-Frame event-id
            ; event-id:    Keypress event-id
           {:db (-> db (store-event-props! [rf-event-id event-id event-props])
                       (cache-event!       [rf-event-id event-id event-props]))
            ; Enable handler if ...
            :dispatch [:environment/enable-keypress-handler?!]
            ; Prevent default if ...
            :dispatch-if [(event-props->prevent-default? event-props)
                          (let [key-code (get event-props :key-code)]
                               [:environment/prevent-keypress-default! key-code])]})))

(a/reg-event-fx
  :environment/remove-keypress-event!
  ; @param (keyword) event-id
  ;
  ; @usage
  ;  [:environment/remove-keypress-event! :my-event]
  (fn [{:keys [db]} [rf-event-id event-id]]
       ; rf-event-id: Re-Frame event-id
       ; event-id:    Keypress event-id
      {:db (-> db (uncache-event!      [rf-event-id event-id])
                  (remove-event-props! [rf-event-id event-id]))
       ; Disable handler if ...
       :dispatch [:environment/disable-keypress-handler?!]
       ; Enable default if ...
       :dispatch-if [(r enable-default? db event-id)
                     (let [key-code (r get-event-prop db event-id :key-code)]
                          [:environment/enable-keypress-default! key-code])]}))

(a/reg-event-fx
  :environment/enable-keypress-handler?!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      ; Handler will be enabled, if ...
      {:dispatch-if [(r enable-handler? db) [:environment/->keypress-handler-enabled]]}))

(a/reg-event-fx
  :environment/disable-keypress-handler?!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      ; Handler will be disabled, if ...
      {:dispatch-if [(r disable-handler? db) [:environment/->keypress-handler-disabled]]}))

(a/reg-event-fx
  :environment/disable-keypress-handler!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      ; Handler will be disabled anyway!
      {:dispatch-if [(r handler-enabled? db) [:environment/->keypress-handler-disabled]]}))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :environment/->key-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) key-code
  (fn [{:keys [db]} [_ key-code]]
      (let [keydown-events (r get-on-keydown-events db key-code)]
            ; Store key-code to pressed-keys vector
           {:db (r db/apply! db (db/meta-item-path ::keypress-events :pressed-keys)
                   vector/conj-item-once key-code)
            ; Dispatch keydown events
            :dispatch-n keydown-events})))

(a/reg-event-fx
  :environment/->key-released
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) key-code
  (fn [{:keys [db]} [_ key-code]]
      (let [keyup-events (r get-on-keyup-events db key-code)]
            ; Remove key-code from pressed-keys vector
           {:db (r db/apply! db (db/meta-item-path ::keypress-events :pressed-keys)
                   vector/remove-item key-code)
            ; Dispatch keyup events
            :dispatch-n keyup-events})))

(a/reg-event-fx
  :environment/->keypress-handler-enabled
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db (r db/set-item! db (db/meta-item-path ::keypress-events :enabled?) true)
       :dispatch-n [[:environment/add-event-listener! "keydown" keydown-listener]
                    [:environment/add-event-listener! "keyup"   keyup-listener]]}))

(a/reg-event-fx
  :environment/->keypress-handler-disabled
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db (r db/set-item! db (db/meta-item-path ::keypress-events :enabled?) false)
       :dispatch-n [[:environment/remove-event-listener! "keydown" keydown-listener]
                    [:environment/remove-event-listener! "keyup"   keyup-listener]]}))

(a/reg-event-fx
  :environment/->keypress-settings-changed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:dispatch-if [(r enable-handler? db) [:environment/enable-keypress-handler?!]
                                            [:environment/disable-keypress-handler!]]}))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-login [:environment/->keypress-settings-changed]})
